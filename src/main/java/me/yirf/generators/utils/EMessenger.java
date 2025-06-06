package me.yirf.generators.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class EMessenger {

    private final FileConfiguration config;
    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    public EMessenger(FileConfiguration config) {
        this.config = config;
    }

    public Component fromConfig(String path, List<String> placeholders, List<String> values) {
        List<String> lines = config.getStringList(path);

        if (lines.isEmpty()) {
            return miniMessage.deserialize("");
        }

        List<Component> components = lines.stream()
                .map(line -> {
                    for (int i = 0; i < placeholders.size(); i++) {
                        line = line.replace(placeholders.get(i), values.get(i));
                    }
                    return miniMessage.deserialize(line);
                })
                .toList();


        return Component.join(JoinConfiguration.separator(Component.newline()), components);
    }

    public Component fromConfig(String path) {
        return fromConfig(path, List.of(), List.of());
    }
}
