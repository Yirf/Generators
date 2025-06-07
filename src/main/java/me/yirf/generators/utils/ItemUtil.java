package me.yirf.generators.utils;

import me.yirf.generators.managers.PersistentManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ItemUtil {

    public static class Creator {
        private List<String> placeholders;
        private List<String> values;
        private Material material;

        private final String name;
        private final String path;
        private final FileConfiguration config;
        private String identifier = null;
        private Double sell = null;

        MiniMessage mini = MiniMessage.miniMessage();

        public Creator(FileConfiguration config, String path, String name) {
            this.path = path;
            this.config = config;
            this.name = name;
        }

        public Creator setPlaceholders(List<String> placeholders) {
            this.placeholders = placeholders;
            return this;
        }

        public Creator setValues(List<String> values) {
            this.values = values;
            return this;
        }

        public Creator setMaterial(Material material) {
            this.material = material;
            return this;
        }

        public Creator setId(String id) {
            this.identifier = id;
            return this;
        }

        public Creator setSell(Double sell) {
            this.sell = sell;
            return this;
        }

        public ItemStack generate() {
            ItemStack item = new ItemStack(material);
            ItemMeta meta = item.getItemMeta();

            List<Component> lore = config.getStringList(path + ".lore").stream()
                    .map(line -> {
                        for (int i = 0; i < placeholders.size(); i++) {
                            line = line.replace("<" + placeholders.get(i) + ">", values.get(i));
                        }
                        return mini.deserialize(line)
                                .decoration(TextDecoration.ITALIC, false);
                    })
                    .toList();

            Component fullName = mini.deserialize(config.getString(path + ".name").replaceAll("<name>", name))
                    .decoration(TextDecoration.ITALIC, false);

            meta.lore(lore);
            meta.displayName(fullName);

            if (identifier != null) {
                meta.getPersistentDataContainer().set(
                        PersistentManager.Keys.IDENTIFIR,
                        PersistentManager.DataTypes.STRING,
                        identifier
                );
            }
            if (sell != null) {
                meta.getPersistentDataContainer().set(
                        PersistentManager.Keys.SELL,
                        PersistentManager.DataTypes.DOUBLE,
                        sell
                );
            }

            item.setItemMeta(meta);
            return item;
        }
    }
}
