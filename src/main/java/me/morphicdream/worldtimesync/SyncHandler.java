package me.morphicdream.worldtimesync;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;

class SyncHandler extends BukkitRunnable {

    private final int time = 1000;//This should be 7am mc time
    private final Player sleeper;

    SyncHandler(Player sleeper) {
        this.sleeper = sleeper;
    }

    @Override
    public void run() {
        for (String string : WorldTimeSync.getInstance().getWorldNames()) {
            World w = Bukkit.getWorld(string);
            if (Objects.requireNonNull(w).getEnvironment() == World.Environment.NORMAL) {
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
        WorldTimeSync.clearSleeperUUID();
    }
}
