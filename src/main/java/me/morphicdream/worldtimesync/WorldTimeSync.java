package me.morphicdream.worldtimesync;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class WorldTimeSync extends JavaPlugin {

    private static final List<String> worldNames = new ArrayList<>();
    private List excluded = Arrays.asList("plugins", "logs", "crash-reports");
    private static WorldTimeSync instance;
    private final int time = 1000;//This should be 7am mc time
    private boolean isNight;

    public WorldTimeSync() {
        instance = this;
    }

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(new WorldListener(), getInstance());
        //getInstance().getCommand("sleep").setExecutor(new SleepCommand());
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
                    List list = Arrays.asList(Objects.requireNonNull(file.list(filter)));
                    if (list.contains("level.dat")) {
                        System.out.println(file.getName());
                        worldNames.add(file.getName());
                    }
                }
            }
        }
    }

    private List<String> getWorldNames() {
        return worldNames;
    }

    public static WorldTimeSync getInstance() {
        return instance;
    }

    public void syncWorlds(World world, final Player sleeper) {
        System.out.println(sleeper.getName() + " called syncWorlds");
        if (world.getTime() > 13000 && world.getTime() < 23460) {
            isNight = true;
        }
        if (isNight) {
            loadWorlds();
            new BukkitRunnable() {
                @Override
                public void run() {
                    for (String string : getWorldNames()) {
                        World w = Bukkit.getWorld(string);
                        if (w.getEnvironment() == World.Environment.NORMAL) {
                            if (w.isThundering()) {
                                w.setThundering(false);
                            }
                            if (w.hasStorm()) {
                                w.setStorm(false);
                            }
                            w.setTime(time);
                            System.out.println(w.getName() + " has had its time set to " + time);
                        }
                        for (Player p : w.getPlayers()) {
                            if (w == p.getWorld()) {
                                p.sendMessage(sleeper.getDisplayName() + " has slept and moved time forward!");
                            } else {
                                p.sendMessage(sleeper.getDisplayName() + " has slept in another world and moved time forward");
                            }
                        }
                    }
                }
            }.runTaskLater(WorldTimeSync.getInstance(), 8 * 20);

        }
    }

    private void loadWorlds() {
        //todo Allow for usage of non minecraft world generators
        for (String worldName : getWorldNames()) {
            WorldCreator worldCreator = new WorldCreator(worldName);
            worldCreator.createWorld();
        }
    }
}
