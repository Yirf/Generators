package me.yirf.generators.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class EMessenger {

    private FileConfiguration config;
    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    public EMessenger(FileConfiguration config) {
        this.config = config;
    }

    public void setConfig(FileConfiguration config) {
        this.config = config;
    }

    public Component fromConfig(String path, List<String> placeholders, List<String> values) {
        if (config.getStringList(path) != null) {
            return configList(path, placeholders, values);
        } else if (config.getString(path) != null) {
            return configSingle(path, placeholders, values);
        } else {
            return miniMessage.deserialize("");
        }
    }

    public Component fromConfig(String path) {
        return fromConfig(path, List.of(), List.of());
    }

    private Component configList(String path, List<String> placeholders, List<String> values) {
        List<String> lines = config.getStringList(path);

        if (lines.isEmpty()) {
            return miniMessage.deserialize("");
        }

        List<Component> components = lines.stream()
                .map(line -> {
                    for (int i = 0; i < placeholders.size(); i++) {
                        line = line.replace("<" + placeholders.get(i) + ">", values.get(i));
                    }
                    return miniMessage.deserialize(line);
                })
                .toList();


        return Component.join(JoinConfiguration.separator(Component.newline()), components);
    }

    private Component configSingle(String path, List<String> placeholders, List<String> values) {
        String line = config.getString(path);

        Bukkit.broadcastMessage("path: " + path + "| result: " + line);

        if (line.isEmpty()) {
            return miniMessage.deserialize("");
        }

        for (int i = 0; i < placeholders.size(); i++) {
            line = line.replace(placeholders.get(i), values.get(i));
        }

        return miniMessage.deserialize(line);
    }

    public Component simple(String string) {
        return miniMessage.deserialize(string);
    }
}
