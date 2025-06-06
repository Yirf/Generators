package me.yirf.generators.commands.admin;

import me.yirf.generators.Handlers.AdminHandler;
import me.yirf.generators.commands.ArgumentCommand;
import me.yirf.generators.managers.PersistentManager;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.incendo.cloud.Command;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.paper.util.sender.Source;

public record ReloadCommand(AdminHandler handler) implements ArgumentCommand {

    @Override
    public Command.Builder<? extends Source> build(PaperCommandManager<Source> commandManager) {
        return commandManager.commandBuilder("reloadgens")
                .handler(this::execute);
    }

    private void execute(CommandContext<Source> ctx) {
        CommandSender sender = ctx.sender().source();

        handler.reload(sender);
    }
}
