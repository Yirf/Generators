package me.yirf.generators.commands.sell;

import me.yirf.generators.Handlers.SellHandler;
import me.yirf.generators.commands.ArgumentCommand;
import me.yirf.generators.data.Cache;
import me.yirf.generators.data.PlayerData;
import me.yirf.generators.managers.PersistentManager;
import me.yirf.generators.utils.Messenger;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.incendo.cloud.Command;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.paper.util.sender.PlayerSource;
import org.incendo.cloud.paper.util.sender.Source;

import java.util.HashMap;
import java.util.UUID;

//something im missing for  loading plugin to enable this command api im 90% sure.

public record SellCommand(SellHandler handler, FileConfiguration config, Cache<UUID, PlayerData> playerCache) implements ArgumentCommand {

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
            PersistentManager pdc = new PersistentManager(item);
            if (pdc.getSell() != null) {
                result += pdc.getSell();
                amount++;
            }
        }

        playerCache.get(sender.getUniqueId()).getHistory().increaseDropEarnings(result);
        playerCache.get(sender.getUniqueId()).getHistory().increaseTotal(amount);

        //add Messenger stuff and CurrencyManager stuff as well.
    }
}
