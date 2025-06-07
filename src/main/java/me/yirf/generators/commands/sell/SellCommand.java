package me.yirf.generators.commands.sell;

import me.yirf.generators.hanlders.SellHandler;
import me.yirf.generators.commands.ArgumentCommand;
import me.yirf.generators.managers.PersistentManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.incendo.cloud.Command;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.paper.util.sender.PlayerSource;
import org.incendo.cloud.paper.util.sender.Source;

public record SellCommand(SellHandler handler, FileConfiguration config) implements ArgumentCommand {

    @Override
    public Command.Builder<? extends Source> build(PaperCommandManager<Source> commandManager) {
        return commandManager.commandBuilder("sell")
                .senderType(PlayerSource.class)
                .handler(this::execute);
    }

    private void execute(CommandContext<PlayerSource> ctx) {
        Player sender = ctx.sender().source();
        double result = 0.00D;
        int amount = 0;

        for (ItemStack item : sender.getInventory().getContents()) {
            if (item == null) continue;

            PersistentManager pdc = new PersistentManager(item);
            if (pdc.getSell() != null) {
                sender.getInventory().remove(item);
                result += pdc.getSell();
                amount++;
            }
        }

        handler.sellItems(sender, amount, result);
        //add Messenger stuff and CurrencyManager stuff as well.
    }
}
