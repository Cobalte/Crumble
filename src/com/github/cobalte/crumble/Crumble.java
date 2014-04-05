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
import com.github.cobalte.library.*;

public class Crumble extends JavaPlugin {

	// ----------------------------------------------------------------------------------------------------------------\\
	//    PRIVATE VARS
	// ----------------------------------------------------------------------------------------------------------------//
	
	private final static boolean SHOW_DEBUG_MESSAGES = false;
	private final static String CRUMBLE_WORLD_NAME = "world_crumble";
	private final static String LIBRARY_WORLD_NAME = "world_library";
    public static World crumbleWorld = null;
    public static World libraryWorld = null;
    public static Random rand;

    // ----------------------------------------------------------------------------------------------------------------\\
  	//    REQUIRED THINGS
  	// ----------------------------------------------------------------------------------------------------------------//
    
    public void onDisable() {
    }
    
    public void onEnable() {
        PluginDescriptionFile desc = this.getDescription();
        getCommand("crumble").setExecutor(new CrumbleCommandExec());
        getCommand("library").setExecutor(new LibraryCommandExec());
        rand = new Random();
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
        if (crumbleWorld == null) {
        	WorldCreator worldCreator = new WorldCreator(CRUMBLE_WORLD_NAME).environment(World.Environment.NORMAL).generator(new CrumbleChunkGenerator());
        	crumbleWorld = Bukkit.getServer().createWorld(worldCreator);
        	crumbleWorld.setMonsterSpawnLimit(0);
        }

        return crumbleWorld;
    }

    public static World getLibraryWorld() {
        if (libraryWorld == null) {
        	WorldCreator worldCreator = new WorldCreator(LIBRARY_WORLD_NAME).environment(World.Environment.THE_END).generator(new LibraryChunkGenerator());
        	libraryWorld = Bukkit.getServer().createWorld(worldCreator);
        	libraryWorld.setMonsterSpawnLimit(0);
        }

        return libraryWorld;
    }
    
    @Override
    public ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {
        return new CrumbleChunkGenerator();
    }
    
    // ----------------------------------------------------------------------------------------------------------------\\
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
    
}