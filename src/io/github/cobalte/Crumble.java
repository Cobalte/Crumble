package io.github.cobalte;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class Crumble extends JavaPlugin {
    
	private final static String WORLD_NAME = "world_crumble";
    private static World crumble = null;

    public void onDisable() {
    }
    
    public void onEnable() {
        PluginDescriptionFile desc = this.getDescription();
        getCommand("crumble").setExecutor(new CrumbleCommandExec());
    }
    
    public boolean anonymousCheck(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can execute this command.");
            return true;
        } else {
            return false;
        }
    }

    public static World getCrumbleWorld() {
        if (crumble == null) {
        	WorldCreator worldCreator = new WorldCreator(WORLD_NAME).environment(World.Environment.NORMAL).generator(new CrumbleChunkGenerator());
        	crumble = Bukkit.getServer().createWorld(worldCreator);
        }

        return crumble;
    }

    @Override
    public ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {
        return new CrumbleChunkGenerator();
    }
}