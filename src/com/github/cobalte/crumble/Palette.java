package com.github.cobalte.crumble;

import java.util.ArrayList;
import org.bukkit.Material;

public class Palette {

    // ----------------------------------------------------------------------------------------------------------------\\
  	//    PRIVATE VARS
  	// ----------------------------------------------------------------------------------------------------------------//
	
	private static ArrayList<String> brushNames;
	private static ArrayList<Material[]> brushMats;
	
    // ----------------------------------------------------------------------------------------------------------------\\
  	//    PRIVATE INITIALIZATION METHOD
  	// ----------------------------------------------------------------------------------------------------------------//
	
	// this method is called by all public methods in this class 
	private static void init() {
		if (brushNames == null) {
		
			Crumble.log("Initializing palette.", false);
	    	brushNames = new ArrayList<String>();
	    	brushMats = new ArrayList<Material[]>();
			
	    	Material[] mats;
	    	
	    	// wall
	    	mats = new Material[4];
	    	mats[0] = Material.SMOOTH_BRICK;
	    	mats[1] = Material.SMOOTH_BRICK;
	    	mats[2] = Material.SMOOTH_BRICK;
	    	mats[3] = Material.COBBLESTONE;
	    	Palette.addBrush("wall", mats);
	    	
	    	// floor
	    	mats = new Material[3];
	    	mats[0] = Material.STONE;
	    	mats[1] = Material.STONE;
	    	mats[2] = Material.COBBLESTONE;
	    	Palette.addBrush("floor", mats);
	    	
	    	// air
	    	mats = new Material[1];
	    	mats[0] = Material.AIR;
	    	Palette.addBrush("air", mats);
			
		}
	}
	
    // ----------------------------------------------------------------------------------------------------------------\\
  	//    PUBLIC METHODS
  	// ----------------------------------------------------------------------------------------------------------------//
	
	// returns a random block from the selected brush
	public static Material getBrush(String name) {
		init();
		
		for(int i = 0; i < brushNames.size(); i++){
		    if (brushNames.get(i) == name) {
		    	int selection = Crumble.rand.nextInt(brushMats.get(i).length);
		    	return brushMats.get(i)[selection];
		    }
		}
		
		// this means something fucked up
		return Material.SPONGE;
	}
	
	// adds a new brush to the available brushes
	public static void addBrush(String Name, Material[] Choices) {
		init();
		
		if (brushNames == null || brushMats == null) {
			brushNames = new ArrayList<String>();
			brushMats = new ArrayList<Material[]>();
		}
		
		brushNames.add(Name);
		brushMats.add(Choices);
		
	}
	
	// returns the number of brushes added
	public static int getBrushCount() {
		init();
		
		if (brushNames == null || brushMats == null) {
			brushNames = new ArrayList<String>();
			brushMats = new ArrayList<Material[]>();
		}
		
		return brushNames.size();
		
	}
	
    // ----------------------------------------------------------------------------------------------------------------\\
  	//    PRIVATE METHODS
  	// ----------------------------------------------------------------------------------------------------------------//
	
}
