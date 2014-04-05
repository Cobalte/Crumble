package com.github.cobalte.library;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.cobalte.crumble.Crumble;

public class LibraryCommandExec implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player)sender;
            player.teleport(Crumble.getLibraryWorld().getSpawnLocation());
        } else {
            sender.sendMessage("Only players can execute that command.");
        }

        return true;
    }
}
