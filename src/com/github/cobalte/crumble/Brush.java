package com.github.cobalte.crumble;

import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.Material;

public class Brush {
	
	// ----------------------------------------------------------------------------------------------------------------\\
	//    PRIVATE VARS
	// ----------------------------------------------------------------------------------------------------------------//
	
	private static ArrayList<Palette> palettes;
	private static ArrayList<Blok[]> choices;
	
	// ----------------------------------------------------------------------------------------------------------------\\
	//    PUBLIC METHODS
	// ----------------------------------------------------------------------------------------------------------------//
	
	public static Blok pickRandom(Palette pal) {
		
		for (int i = 0; i < palettes.size(); i++) {
			if (palettes.get(i) == pal) {
				return choices.get(i)[Crumble.getRandInt(choices.get(i).length)];
			}
		}
		
		// oh shit, something is wrong
		return null;
	}
	
	// ----------------------------------------------------------------------------------------------------------------\\
	//    INITIALIZATION
	// ----------------------------------------------------------------------------------------------------------------//
	
	public static void initialize() {
		palettes = new ArrayList<Palette>();
		choices = new ArrayList<Blok[]>();
		Blok[] bloks;
		
		// define wall
		bloks = new Blok[1];
		bloks[0] = new Blok(Material.SMOOTH_BRICK);
		choices.add(bloks);
		palettes.add(Palette.WALL);
		
		// define floor
		bloks = new Blok[3];
		bloks[0] = new Blok(Material.STONE);
		bloks[1] = new Blok(Material.STONE);
		bloks[2] = new Blok(Material.COBBLESTONE);
		choices.add(bloks);
		palettes.add(Palette.FLOOR);
		
		// define road
		bloks = new Blok[4];
		bloks[0] = new Blok(Material.GRAVEL);
		bloks[1] = new Blok(Material.GRAVEL);
		bloks[2] = new Blok(Material.GRAVEL);
		bloks[3] = new Blok(Material.COBBLESTONE);
		choices.add(bloks);
		palettes.add(Palette.ROAD);
		
	}
}
