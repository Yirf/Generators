package me.yirf.generators.data;

import org.bukkit.Location;

import java.util.UUID;

public class GenData {

    private Location location;
    private String identifier;
    private UUID owner;

    public GenData(Location location, String identifier, UUID owner) {
        this.location = location;
        this.identifier = identifier;
        this.owner = owner;
    }

    public Location getLocation() {return location;}
    public String getIdentifier() {return identifier;}
    public UUID getOwner() {return owner;}
}
