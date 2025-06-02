package me.yirf.generators;

import com.squareup.moshi.Moshi;
import me.yirf.generators.data.*;
import me.yirf.generators.generator.Gens;
import me.yirf.generators.listeners.PlayerBlocks;
import me.yirf.generators.listeners.PlayerConnections;
import me.yirf.generators.managers.DropManager;
import me.yirf.generators.utils.LocationAdapter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public final class Generators extends JavaPlugin {

    private FileConfiguration config;
    private FileConfiguration gensConfig;
    private static final Set<Material> genMaterials = new HashSet<>();

    private Gens gens;

    private Repository<UUID, PlayerData> playerRepo;
    private Repository<String, GenData> gensRepo;
    private Cache<UUID, PlayerData> playerCache;
    private Cache<String, GenData> gensCache;

    private Mongo mongo;
    private Moshi moshi = new Moshi.Builder()
            .add(Location.class, new LocationAdapter())
            .build();
    private AutoSave autoSave;
    private DropManager dropManager;

    private final PluginManager pm = getServer().getPluginManager();

    @Override
    public void onEnable() {
        saveDefaultConfig();
        this.config = getConfig();
        if (!new File(getDataFolder(), "generators.yml").exists()) {
            saveResource("generators.yml", false);
        }
        this.gensConfig = YamlConfiguration.loadConfiguration(
                new File(getDataFolder(), "generators.yml")
        );
        this.gensConfig = YamlConfiguration.loadConfiguration(
                new File(getDataFolder(), "generators.yml")
        );
        this.gens = new Gens(gensConfig);

        if (config.getString("database.uri").equalsIgnoreCase("") ||
                config.getString("database.name").equalsIgnoreCase(""))
        {
            getServer().getLogger().severe("Please input your mongo details before running.");
            pm.disablePlugin(this);
        }

        this.mongo = new Mongo(
                config.getString("database.uri"),
                config.getString("database.name"));
        mongo.connect();

        playerRepo = new Repository<>(
                moshi, mongo.getDatabase(),
                "playerData",
                PlayerData.class);
        gensRepo = new Repository<>(moshi,
                mongo.getDatabase(),
                "gensData",
                GenData.class);
        gensCache = new Cache<>(gensRepo);
        playerCache = new Cache<>(playerRepo);

        createSchedules();
        registerListeners();
        load();
    }

    @Override
    public void onDisable() {
        for (UUID uuid: playerCache.keys()) {
            PlayerData pd = playerCache.get(uuid);
            playerRepo.put(uuid, pd);
            playerCache.remove(uuid);
        }
        for (String loc : gensCache.keys()) {
            GenData gd = gensCache.get(loc);
            gensRepo.put(loc, gd);
            gensCache.remove(loc);
        }
        autoSave.stop();
    }

    public Repository<UUID, PlayerData> getPlayerRepo() {
        return playerRepo;
    }

    public Repository<String, GenData> getGenRepo() {
        return gensRepo;
    }

    public Cache<UUID, PlayerData> getPlayerCache() {
        return playerCache;
    }

    public Cache<String, GenData> getGenCache() {
        return gensCache;
    }

    public FileConfiguration getGensConfig() {
        return gensConfig;
    }

    public Gens getGens() {return gens;}

    public static Set<Material> getGenMaterials() {
        return genMaterials;
    }

    private void registerListeners() {
        pm.registerEvents(
                new PlayerConnections(
                        playerRepo,
                        playerCache),
                this);
        pm.registerEvents(
                new PlayerBlocks(
                        playerCache,
                        gensCache,
                        gens),
                this);
    }

    private void load() {
        ConfigurationSection section = gensConfig.getConfigurationSection("generators");

        Set<String> keys = section.getKeys(false);
        if (section == null) return;
        if (keys == null) return;

        for (String key: keys) {
            genMaterials.add(
                    Material.valueOf(key)
            );
        }

        plugmanIsNotCool();
    }

    private void plugmanIsNotCool() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            UUID uuid = player.getUniqueId();
            PlayerData pd = new PlayerData();
            if (playerRepo.get(uuid) != null) {
                pd = playerRepo.get(uuid);
            }

            playerCache.set(uuid, pd);
        }
    }

    private void createSchedules() {
        this.autoSave = new AutoSave(
                config.getLong("database.auto-save"),
                playerCache, playerRepo,
                gensCache, gensRepo
        );
        autoSave.start();

        if (config.getLong("drop-time") < 1) {
            this.getLogger().severe("Please make sure your drop-time is >= 1.");
            pm.disablePlugin(this);
        }
        this.dropManager = new DropManager(
                config.getLong("drop-time"),
                this
        );
        dropManager.start();
    }
}
