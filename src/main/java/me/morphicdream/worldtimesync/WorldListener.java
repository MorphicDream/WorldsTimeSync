package me.morphicdream.worldtimesync;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;

class WorldListener implements Listener {

    @EventHandler
    public void onSleep(PlayerBedEnterEvent event) {
        if(WorldTimeSync.getInstance().getSleeperUUID() != null) {
            WorldTimeSync.getInstance().addSleeper(event.getPlayer().getUniqueId());
            WorldTimeSync.getInstance().syncWorlds(event.getPlayer().getWorld(), event.getPlayer());
        }
    }

    @EventHandler
    public void onAbortSleep(PlayerBedLeaveEvent event) {
        System.out.println(event.getPlayer().getName() + " left bed");
        if (WorldTimeSync.getInstance().getSleeperUUID().equals(event.getPlayer().getUniqueId())) {
            WorldTimeSync.getTask().cancel();
            WorldTimeSync.getInstance().clearSleeperUUID();
            System.out.println("Task cancelled by " + event.getPlayer().getName());
        }
    }

}
