package me.morphicdream.worldtimesync;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;

class WorldListener implements Listener {

    @EventHandler
    public void onSleep(PlayerBedEnterEvent event) {
        WorldTimeSync.getInstance().syncWorlds(event.getPlayer().getWorld(), event.getPlayer());
    }

}
