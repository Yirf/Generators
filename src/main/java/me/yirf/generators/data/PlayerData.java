package me.yirf.generators.data;

import org.bukkit.Location;

import java.util.HashSet;
import java.util.Set;

public class PlayerData {

    private Set<String> locations;

    public PlayerData(Set<String> locations) {
        this.locations = this.locations;
    }

    public PlayerData() {
        locations = new HashSet<>();
    }

    public Set<String> getLocations() {
        return locations;
    }

    public void removeGen(String location) {
        locations.remove(location);
    }

    public void addGen(String location) {
        locations.add(location);
    }
}
