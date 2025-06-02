package me.yirf.generators.generator;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

public class Gens {

    private static FileConfiguration config;

    public Gens(FileConfiguration config) {
        this.config = config;
    }

    public Material getMaterial(String id) {
        return Material.valueOf(id);
    }

    public double getSell(String id) {
        return config.getDouble("generators." + id + ".sell");
    }

    public ItemStack getDrop(String id) {
        Material material = Material.valueOf(config.getString("generators." + id + ".drop"));
        return new ItemStack(material);
    }
}