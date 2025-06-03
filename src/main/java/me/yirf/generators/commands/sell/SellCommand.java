package me.yirf.generators.commands.sell;

import me.yirf.generators.Handlers.SellHandler;
import me.yirf.generators.commands.ArgumentCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.incendo.cloud.Command;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.paper.util.sender.PlayerSource;
import org.incendo.cloud.paper.util.sender.Source;

//something im missing for  loading plugin to enable this command api im 90% sure.

public record SellCommand(SellHandler handler, FileConfiguration config) implements ArgumentCommand {

    @Override
    public Command.Builder<? extends Source> build(PaperCommandManager<Source> commandManager) {
        return commandManager.commandBuilder("sell")
                .senderType(PlayerSource.class)
                .handler(this::execute);
    }


    private void execute(CommandContext<PlayerSource> ctx) {
        Player sender = ctx.sender().source();

        for (ItemStack item : sender.getInventory().getContents()) {
            item.getPersistentDataContainer().get()
        }
    }
}
