package me.yirf.generators.listeners;

import me.yirf.generators.data.Cache;
import me.yirf.generators.data.PlayerData;
import me.yirf.generators.data.Repository;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class PlayerConnections implements Listener {

    private final Repository<UUID, PlayerData> repo;
    private final Cache<UUID, PlayerData> cache;

    public PlayerConnections(Repository<UUID, PlayerData> repo, Cache<UUID, PlayerData> cache) {
        this.repo = repo;
        this.cache = cache;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        PlayerData pd = new PlayerData();
        if (repo.get(uuid) != null) {
            pd = repo.get(uuid);
        }

        cache.set(uuid, pd);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        PlayerData pd = cache.get(uuid);

        if (pd == null) return;

        repo.put(uuid, pd);
    }
}
