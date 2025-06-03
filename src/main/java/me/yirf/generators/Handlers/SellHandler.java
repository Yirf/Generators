package me.yirf.generators.Handlers;

import me.yirf.generators.commands.ArgumentCommand;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class SellHandler implements Handler {

    private final FileConfiguration gensConfig;

    public SellHandler(FileConfiguration gensConfig) {
        this.gensConfig = gensConfig;
    }

    @Override
    public String key() {
        return "";
    }

    @Override
    public List<ArgumentCommand> commands() {
        return List.of();
    }
}
