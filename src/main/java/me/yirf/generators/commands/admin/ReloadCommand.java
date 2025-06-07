package me.yirf.generators.commands.admin;

import me.yirf.generators.handlers.AdminHandler;
import me.yirf.generators.commands.ArgumentCommand;
import org.bukkit.command.CommandSender;
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
