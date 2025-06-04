package me.yirf.generators.managers;

import me.yirf.generators.Generators;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class PersistentManager {

    private final ItemStack item;

    public static class DataTypes {
        public static final PersistentDataType<String,String> STRING = PersistentDataType.STRING;
        public static final PersistentDataType<Double, Double> DOUBLE = PersistentDataType.DOUBLE;
    }

    public static class Keys {
        public static final NamespacedKey IDENTIFIR = new NamespacedKey(Generators.instance, "gens.id");
        public static final NamespacedKey SELL = new NamespacedKey(Generators.instance, "gens.sell");
    }

    public PersistentManager(ItemStack item) {
        this.item = item;
    }

    public String getId() {
        return pdc().get(Keys.IDENTIFIR, DataTypes.STRING);
    }

    public Double getSell() {
        return pdc().get(Keys.SELL, DataTypes.DOUBLE);
    }

    public PersistentDataContainer pdc() {
        return item.getItemMeta().getPersistentDataContainer();
    }
}
