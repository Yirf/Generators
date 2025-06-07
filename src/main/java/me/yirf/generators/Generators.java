package me.yirf.generators;

import com.squareup.moshi.Moshi;
import me.yirf.generators.hanlders.AdminHandler;
import me.yirf.generators.hanlders.Handler;
import me.yirf.generators.hanlders.SellHandler;
import me.yirf.generators.data.*;
import me.yirf.generators.generator.Gens;
import me.yirf.generators.listeners.PlayerBlocks;
import me.yirf.generators.listeners.PlayerConnections;
import me.yirf.generators.managers.DropManager;
import me.yirf.generators.utils.EMessenger;
import me.yirf.generators.utils.LocationAdapter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.incendo.cloud.execution.ExecutionCoordinator;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.paper.util.sender.PaperSimpleSenderMapper;
import org.incendo.cloud.paper.util.sender.Source;

import java.io.File;
import java.util.*;

public final class Generators extends JavaPlugin {
    public static Generators instance;

    private PaperCommandManager<Source> commandManager;

    public FileConfiguration config;
    public FileConfiguration gensConfig;

    private Gens gens;

    private Repository<UUID, PlayerData> playerRepo;
    private Repository<String, GenData> gensRepo;
    private Cache<UUID, PlayerData> playerCache;
    private Cache<String, GenData> gensCache;

    private Mongo mongo;
    private final Moshi moshi = new Moshi.Builder()
            .add(Location.class, new LocationAdapter())
            .build();

    private AutoSave autoSave;
    private DropManager dropManager;
    private EMessenger emessenger;

    public static final Set<Material> genMaterials = new HashSet<>();
    private final PluginManager pm = getServer().getPluginManager();

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        this.config = getConfig();
        emessenger = new EMessenger(config);

        // Early exit on config error
        if (config.getString("database.uri").isEmpty() || config.getString("database.name").isEmpty()) {
            getLogger().severe("Please input your MongoDB details before running.");
            pm.disablePlugin(this);
            return;
        }

        this.mongo = new Mongo(
                config.getString("database.uri"),
                config.getString("database.name")
        );
        mongo.connect();

        // Set up repositories and cache before load
        playerRepo = new Repository<>(moshi, mongo.getDatabase(), "playerData", PlayerData.class);
        gensRepo = new Repository<>(moshi, mongo.getDatabase(), "gensData", GenData.class);
        playerCache = new Cache<>();
        gensCache = new Cache<>();

        commandManager = PaperCommandManager.builder(PaperSimpleSenderMapper.simpleSenderMapper())
                .executionCoordinator(ExecutionCoordinator.simpleCoordinator())
                .buildOnEnable(this);

        List<Handler> handlers = getHandlers();
        handlers.forEach(this::registerCommandsForHandler);

        createSchedules();
        load(); // everything safe now
    }

    @Override
    public void onDisable() {
        // Save all cached player and generator data
        playerCache.keys().forEach(uuid -> {
            playerRepo.put(uuid, playerCache.get(uuid));
            playerCache.remove(uuid);
        });
        gensCache.keys().forEach(loc -> {
            gensRepo.put(loc, gensCache.get(loc));
            gensCache.remove(loc);
        });

        if (autoSave != null) autoSave.stop();
    }

    public void load() {
        File gensFile = new File(getDataFolder(), "generators.yml");
        if (!gensFile.exists()) {
            saveResource("generators.yml", false);
        }
        this.gensConfig = YamlConfiguration.loadConfiguration(gensFile);

        reloadConfig();
        config = getConfig();
        this.gens = new Gens(gensConfig);
        emessenger.setConfig(config);
        loadGenerators();

        unregisterListeners();
        registerListeners();
    }

    private void unregisterListeners() {
        if (HandlerList.getHandlerLists().isEmpty()) return;
        HandlerList.unregisterAll(this);
    }

    private void registerListeners() {
        pm.registerEvents(new PlayerConnections(playerRepo, playerCache), this);
        pm.registerEvents(new PlayerBlocks(playerCache, gensCache, gens, config), this);
    }

    private void loadGenerators() {
        ConfigurationSection section = gensConfig.getConfigurationSection("generators");
        if (section == null) {
            getLogger().warning("No generators found in generators.yml.");
            return;
        }

        for (String key : section.getKeys(false)) {
            try {
                genMaterials.add(Material.valueOf(key));
            } catch (IllegalArgumentException ex) {
                getLogger().warning("Invalid material in generators.yml: " + key);
            }
        }
    }

    private void createSchedules() {
        long saveInterval = config.getLong("database.auto-save", 5L);
        this.autoSave = new AutoSave(saveInterval, playerCache, playerRepo, gensCache, gensRepo);
        autoSave.start();

        long dropTime = config.getLong("drop-time", 5L);
        if (dropTime < 1) {
            getLogger().severe("Please make sure your drop-time is >= 1.");
            pm.disablePlugin(this);
            return;
        }
        this.dropManager = new DropManager(dropTime, this);
        dropManager.start();
    }

    private void registerCommandsForHandler(Handler handler) {
        if (commandManager == null) return;
        handler.commands().forEach(command -> commandManager.command(command.build(commandManager)));
    }

    private List<Handler> getHandlers() {
        return List.of(
                new SellHandler(gensConfig, playerCache, emessenger),
                new AdminHandler(this, emessenger)
        );
    }

    public Cache<UUID, PlayerData> getPlayerCache() { return playerCache; }
    public Cache<String, GenData> getGenCache() { return gensCache; }
    public Gens getGens() { return gens; }
    public static Set<Material> getGenMaterials() { return genMaterials; }
}
