package me.morphicdream.worldtimesync.commands;

import me.morphicdream.worldtimesync.WorldTimeSync;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SleepCommand implements CommandExecutor {

    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            WorldTimeSync.getInstance().syncWorlds(player.getWorld(), player);
        }
        return false;
    }
}
