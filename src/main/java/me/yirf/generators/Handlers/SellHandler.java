package me.yirf.generators.Handlers;

import me.yirf.generators.commands.ArgumentCommand;
import me.yirf.generators.commands.sell.SellCommand;
import me.yirf.generators.data.Cache;
import me.yirf.generators.data.PlayerData;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class SellHandler implements Handler {

    private final FileConfiguration gensConfig;
    private Cache<UUID, PlayerData> playerCache;

    public SellHandler(FileConfiguration gensConfig, Cache<UUID, PlayerData> playerCache) {
        this.gensConfig = gensConfig;
        this.playerCache = playerCache;
    }

    @Override
    public String key() {
        return "";
    }

    @Override
    public List<ArgumentCommand> commands() {
        return List.of(
                new SellCommand(this, gensConfig, playerCache)
        );
    }

//    public double getResult(Map<String, Integer> map) {
//        double result = 0.00D;
//        for (Map.Entry<String, Integer> entry : map.entrySet()) {
//            double singlePrice =  gensConfig.getDouble("generators." +  entry.getKey() +  ".sell");
//            result += singlePrice * entry.getValue();
//        }
//
//        return result;
//    }
}
