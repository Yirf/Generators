package me.yirf.generators.data;

import org.bukkit.Location;

import java.util.HashSet;
import java.util.Set;

public class PlayerData {

    private Set<String> locations;
    private PlayerHistory history;
    private double money;

    public PlayerData(Set<String> locations, PlayerHistory history, double money) {
        this.locations = locations;
        this.history = history;
        this.money = money;
    }

    public PlayerData() {
        locations = new HashSet<>();
        history = new PlayerHistory();
        money = 0;
    }

    public Set<String> getLocations() {
        return locations;
    }

    public void removeGen(String location) {
        history.increaseGensBroken(1);
        locations.remove(location);
    }

    public void addGen(String location) {
        history.increaseGensPlaced(1);
        locations.add(location);
    }

    public PlayerHistory getHistory() {
        return history;
    }
}
