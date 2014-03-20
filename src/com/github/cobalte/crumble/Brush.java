package com.github.cobalte.crumble;

import java.util.ArrayList;
import java.util.Random;
import org.bukkit.Material;

public class Brush {

	// ----------------------------------------------------------------------------------------------------------------\\
	//    PRIVATE VARS
	// ----------------------------------------------------------------------------------------------------------------//
	
	private static Random rand;
	private static ArrayList<Palette> palettes;
	private static ArrayList<Material[]> choices;
	
	// ----------------------------------------------------------------------------------------------------------------\\
	//    PUBLIC METHODS
	// ----------------------------------------------------------------------------------------------------------------//
	
	public static Material pickRandom(Palette pal) {
		
		for (int i = 0; i < palettes.size(); i++) {
			if (palettes.get(i) == pal) {
				return choices.get(i)[rand.nextInt(choices.get(i).length)];
			}
		}
		
		// oh shit, something is wrong
		return Material.SPONGE;
	}
	
	// ----------------------------------------------------------------------------------------------------------------\\
	//    INITIALIZATION
	// ----------------------------------------------------------------------------------------------------------------//
	
	public static void initialize() {
		rand = new Random();
		palettes = new ArrayList<Palette>();
		choices = new ArrayList<Material[]>();
		Material[] mats;
		
		// define wall		
		mats = new Material[1];
		mats[0] = Material.SMOOTH_BRICK;
		choices.add(mats);
		palettes.add(Palette.Wall);
		
		// define floor
		mats = new Material[3];
		mats[0] = Material.STONE;
		mats[1] = Material.STONE;
		mats[2] = Material.COBBLESTONE;
		choices.add(mats);
		palettes.add(Palette.Floor);
		
		// define road
		mats = new Material[4];
		mats[0] = Material.GRAVEL;
		mats[1] = Material.GRAVEL;
		mats[2] = Material.GRAVEL;
		mats[3] = Material.COBBLESTONE;
		choices.add(mats);
		palettes.add(Palette.Road);
		
	}
}
