package me.yirf.generators.hanlders;

import me.yirf.generators.Generators;
import me.yirf.generators.commands.ArgumentCommand;
import me.yirf.generators.commands.admin.ReloadCommand;
import me.yirf.generators.utils.EMessenger;
import org.bukkit.command.CommandSender;

import java.util.List;

public class AdminHandler implements Handler {

    private final Generators plugin;
    private EMessenger emessenger;

    public AdminHandler(Generators plugin, EMessenger emessenger) {
        this.plugin = plugin;
        this.emessenger = emessenger;
    }

    @Override
    public String key() {
        return "";
    }

    @Override
    public List<ArgumentCommand> commands() {
        return List.of(
                new ReloadCommand(this)
        );
    }

    public void reload(CommandSender sender) {
        plugin.load();
        sender.sendMessage(emessenger.simple("<green>You reloaded all configurations."));
    }
}
