package me.yirf.generators.utils;

import me.yirf.generators.managers.PersistentManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

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

            List<Component> lore = config.getStringList(path).stream()
                    .map(line -> {
                        for (int i = 0; i < placeholders.size(); i++) {
                            line = line.replace(placeholders.get(i), values.get(i));
                        }
                        return mini.deserialize(line);
                    })
                    .toList();

            Component fullName = mini.deserialize(config.getString(path).replaceAll("<name>", name));

            meta.lore(lore);
            meta.displayName(fullName);

            if (identifier != null) {
                meta.getPersistentDataContainer().set();
            }

            item.setItemMeta(meta);
            return item;
        }
    }
}
