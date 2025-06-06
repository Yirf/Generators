package me.yirf.generators.Handlers;

import me.yirf.generators.commands.ArgumentCommand;
import me.yirf.generators.commands.sell.SellCommand;
import me.yirf.generators.data.Cache;
import me.yirf.generators.data.PlayerData;
import me.yirf.generators.utils.EMessenger;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class SellHandler implements Handler {

    private final FileConfiguration gensConfig;
    private Cache<UUID, PlayerData> playerCache;
    private EMessenger emessenger;

    public SellHandler(
            FileConfiguration gensConfig, Cache<UUID, PlayerData> playerCache, EMessenger emessenger
    ) {
        this.gensConfig = gensConfig;
        this.playerCache = playerCache;
        this.emessenger = emessenger;
    }

    @Override
    public String key() {
        return "";
    }

    @Override
    public List<ArgumentCommand> commands() {
        return List.of(
                new SellCommand(this, gensConfig)
        );
    }

    public void sellItems(Player player, int amount, double earned) {
        if (amount == 0) {
            player.sendMessage(emessenger.fromConfig("messages.sell-empty"));
            return;
        }
        player.sendMessage(emessenger.fromConfig(
                "messages.sell",
                List.of("amount", "earned"),
                List.of("" + amount, "" + earned)
        ));

        playerCache.get(player.getUniqueId()).getHistory()
                .increaseDropEarnings(earned);
        playerCache.get(player.getUniqueId()).getHistory()
                .increaseTotal(amount);

        //  currency.give(player, earned)
        //
    }

}
