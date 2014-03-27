package com.github.cobalte.crumble;

import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class Crumble extends JavaPlugin {

	//----------------------------------------------------------------------------------------------------------------\\
	//    PRIVATE VARS
	// ----------------------------------------------------------------------------------------------------------------//
	
	private final static boolean SHOW_DEBUG_MESSAGES = false;
	private final static String WORLD_NAME = "world_crumble";
    public static World crumble = null;
    public static Random rand;

    //----------------------------------------------------------------------------------------------------------------\\
  	//    REQUIRED METHODS
  	// ----------------------------------------------------------------------------------------------------------------//
    
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
    
    //----------------------------------------------------------------------------------------------------------------\\
  	//    ADDITIONAL METHODS
  	// ----------------------------------------------------------------------------------------------------------------//
    
    public static void log(String message) {
    	System.out.println("[Crumble] " + message);
    }
    
    public static void log(String message, boolean debugOnly) {
    	if (debugOnly && SHOW_DEBUG_MESSAGES == true) {
    		System.out.println("[Crumble] " + message);
    	}
    }
    
    public static int getRandInt(int upperBound) {
    	if (rand == null) {
    		rand = new Random();
    	}
    	return rand.nextInt(upperBound);
    }
}