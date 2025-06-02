package me.yirf.generators.managers;

import me.yirf.generators.Generators;
import me.yirf.generators.data.Cache;
import me.yirf.generators.data.GenData;
import me.yirf.generators.data.PlayerData;
import me.yirf.generators.generator.Gens;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.*;

public class DropManager {

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private final long period;
    private final Gens gens;
    private final Cache<UUID, PlayerData> playerCache;
    private final Cache<String, GenData> gensCache;

    private final Generators plugin;

    public DropManager(long period, Generators plugin) {
        this.period = period;
        this.playerCache = plugin.getPlayerCache();
        this.gensCache = plugin.getGenCache();
        this.plugin = plugin;
        this.gens = plugin.getGens();
    }

    public void start() {
        scheduler.scheduleAtFixedRate(() -> {
            CompletableFuture.runAsync(() -> {
                for (UUID uuid : playerCache.keys()) {
                    Set<String> locations = playerCache.get(uuid).getLocations();
                    if (locations == null || locations.isEmpty()) continue;

                    for (String locKey : locations) {
                        GenData gd = gensCache.get(locKey);
                        if (gd == null || gd.getLocation() == null) continue;

                        Location loc = gd.getLocation();
                        World world = loc.getWorld();
                        if (!world.isChunkLoaded(loc.getBlockX() >> 4, loc.getBlockZ() >> 4)) continue;

                        Bukkit.getScheduler().runTask(plugin, () -> {
                            ItemStack item = gens.getDrop(gd.getIdentifier());
                            Item dropped = world.dropItem(loc.clone().add(0, 1, 0), item);
                            dropped.setVelocity(new Vector(0, 0, 0));
                        });
                    }
                }
            });

        }, 0, period, TimeUnit.SECONDS);
    }

    public void stop() {
        scheduler.shutdown();
    }
}