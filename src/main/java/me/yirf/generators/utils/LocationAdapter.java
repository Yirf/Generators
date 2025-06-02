package me.yirf.generators.utils;
import com.squareup.moshi.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.io.IOException;

public class LocationAdapter extends JsonAdapter<Location> {

    @Override
    public Location fromJson(JsonReader reader) throws IOException {
        reader.beginObject();
        String worldName = null;
        double x = 0, y = 0, z = 0;
        float yaw = 0, pitch = 0;

        while (reader.hasNext()) {
            switch (reader.nextName()) {
                case "world": worldName = reader.nextString(); break;
                case "x": x = reader.nextDouble(); break;
                case "y": y = reader.nextDouble(); break;
                case "z": z = reader.nextDouble(); break;
                default: reader.skipValue(); break;
            }
        }
        reader.endObject();

        World world = Bukkit.getWorld(worldName);
        return (world != null) ? new Location(world, x, y, z, yaw, pitch) : null;
    }

    @Override
    public void toJson(JsonWriter writer, Location location) throws IOException {
        writer.beginObject();
        writer.name("world").value(location.getWorld().getName());
        writer.name("x").value(location.getX());
        writer.name("y").value(location.getY());
        writer.name("z").value(location.getZ());
        writer.endObject();
    }

    public static String toString(Location location) {
        if (location == null || location.getWorld() == null) return "null";

        int x = location.getBlockX();
        int y = location.getBlockY();
        int z = location.getBlockZ();
        String world = location.getWorld().getName();

        return x + "," + y + "," + z + "," + world;
    }

    public static Location stringToLocation(String s) {
        if (s == null || s.isEmpty()) return null;

        String[] parts = s.split(",");
        if (parts.length != 4) return null;

        try {
            int x = Integer.parseInt(parts[0]);
            int y = Integer.parseInt(parts[1]);
            int z = Integer.parseInt(parts[2]);
            World world = Bukkit.getWorld(parts[3]);
            if (world == null) return null;

            return new Location(world, x, y, z);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return null;
        }
    }
}