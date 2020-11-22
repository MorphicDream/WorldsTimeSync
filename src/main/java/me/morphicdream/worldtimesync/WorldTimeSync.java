package me.morphicdream.worldtimesync;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.io.FilenameFilter;
import java.util.*;

public class WorldTimeSync extends JavaPlugin {

    private static final List<String> worldNames = new ArrayList<>();
    private static WorldTimeSync instance;
    private static boolean isNight;
    private static UUID sleeperUUID = UUID.randomUUID();
    private static BukkitTask task;

    public WorldTimeSync() {
        instance = this;
    }

    public static WorldTimeSync getInstance(){
        return instance;
    }

    public UUID getSleeperUUID() {
        return sleeperUUID;
    }

    public void clearSleeperUUID() {
        sleeperUUID = null;
    }

    public void addSleeper(UUID uniqueId) {
        clearSleeperUUID();
        sleeperUUID = uniqueId;
    }

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(new WorldListener(), new WorldTimeSync());
        FilenameFilter filter = (pathname, name) -> {
            String lowercase = name.toLowerCase();
            return lowercase.endsWith(".dat");
        };
        File path = Bukkit.getServer().getWorldContainer();
        File[] files = path.listFiles();
        if (files == null) {
            System.out.println("No worlds found!");
        } else {
            for (File file : files) {
                if (file.isDirectory() && file.list(filter) != null) {
                    List<String> list = Arrays.asList(Objects.requireNonNull(file.list(filter)));
                    if (list.contains("level.dat")) {
                        System.out.println(file.getName());
                        worldNames.add(file.getName());
                    }
                }
            }
        }
    }

    static List<String> getWorldNames() {
        return worldNames;
    }

    public void syncWorlds(World world, final Player sleeper) {
        if (!getSleeperUUID().equals(sleeper.getUniqueId())) {
            System.out.println(sleeper.getName() + " called syncWorlds");
            if (world.getTime() > 13000 && world.getTime() < 23460) {
                isNight = true;
            }
            if (isNight) {
                loadWorlds();
                task = new SyncHandler(sleeper).runTaskLater(new WorldTimeSync(), 10 * 20);
            }
        }
    }

    public static BukkitTask getTask() {
        return task;
    }

    private static void loadWorlds() {
        //todo Allow for usage of non minecraft world generators
        for (String worldName : getWorldNames()) {
            WorldCreator worldCreator = new WorldCreator(worldName);
            worldCreator.createWorld();
        }
    }
}
