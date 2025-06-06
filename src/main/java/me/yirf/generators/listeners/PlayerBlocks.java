package me.yirf.generators.listeners;

import me.yirf.generators.Generators;
import me.yirf.generators.data.Cache;
import me.yirf.generators.data.GenData;
import me.yirf.generators.data.PlayerData;
import me.yirf.generators.data.Repository;
import me.yirf.generators.generator.Gens;
import me.yirf.generators.utils.LocationAdapter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.persistence.PersistentDataContainer;

import java.util.Set;
import java.util.UUID;

public class PlayerBlocks implements Listener {

    private final Cache<UUID, PlayerData> playerCache;
    private final Cache<String, GenData> gensCache;
    private final Gens gens;

    private static final Set<Material> genMaterials = Generators.getGenMaterials();

    public PlayerBlocks(
            Cache<UUID, PlayerData> playerCache,
            Cache<String, GenData> gensCache,
            Gens gens
    ) {
        this.playerCache = playerCache;
        this.gensCache = gensCache;
        this.gens = gens;
    }

//    @EventHandler
//    public void onBlockBreak(BlockBreakEvent event) {
//        UUID uuid = event.getPlayer().getUniqueId();
//        Block block = event.getBlock();
//
//        if (!genMaterials.contains(block.getType())) return;
//
//        String locString = LocationAdapter.toString(block.getLocation());
//        GenData gd = gensCache.get(locString);
//        if (gd == null) return;
//
//        if (gd.getOwner() == uuid) {
//            playerCache.get(uuid).removeGen(locString);
//            gensCache.remove(locString);
////            event.getPlayer().getInventory().addItem(gens.getMaterial(gd.getIdentifier()));
//        }
//    }

    @EventHandler
    public void onBlockDamage(BlockDamageEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        Block block = event.getBlock();

        if (!genMaterials.contains(block.getType())) return;

        String locString = LocationAdapter.toString(block.getLocation());
        GenData gd = gensCache.get(locString);
        if (gd == null) return;

        if (gd.getOwner() == uuid) {
            playerCache.get(uuid).removeGen(locString);
            gensCache.remove(locString);
            block.setType(Material.AIR);

        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        Block block = event.getBlock();

        if (!genMaterials.contains(block.getType())) return;

        String locString = LocationAdapter.toString(block.getLocation());
        GenData gd = new GenData(block.getLocation(), block.getType().toString(), uuid);

        gensCache.set(locString, gd);
        playerCache.get(uuid).addGen(locString);
    }
}
