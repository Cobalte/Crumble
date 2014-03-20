package com.github.cobalte.crumble;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Random;
import org.bukkit.Material;

public class CrumbleLocaleManager {
	
	// ----------------------------------------------------------------------------------------------------------------\\
	//    PRIVATE VARS
	// ----------------------------------------------------------------------------------------------------------------//
	
	private static final int MAX_LOCALE_CHUNK_SIZE = 5;
	private static Random rand;
	private static ArrayList<CrumbleLocale> locales;
	
	// ----------------------------------------------------------------------------------------------------------------\\
	//    PUBLIC PROPERTIES
	// ----------------------------------------------------------------------------------------------------------------//
	
	// ----------------------------------------------------------------------------------------------------------------\\
	//    PUBLIC METHODS
	// ----------------------------------------------------------------------------------------------------------------//
	
	// returns true if the chunk found at the passed coordinates is already claimed by a locale
	public static boolean isChunkClaimed(int locX, int locZ) {
		ensureManagerIsInitialized();
		
		// iterate through all locales, looking for one that covers this chunk
		for(CrumbleLocale locale : locales) {
			
			// get data on this locale to make the following equation easier to read
			int originX = locale.getOriginX();
			int originZ = locale.getOriginZ();
			int sizeX = locale.getChunkSizeX();
			int sizeZ = locale.getChunkSizeZ();
			
			// check if the passed X and Z are within the bounds of this locale
			if (originX <= locX && locX < originX + sizeX &&
				originZ <= locZ && locZ < originZ + sizeZ) {
				return true;
			}
		}
		return false;
	}
	
	// creates a locale covering the chunk at the passed X and Z locations
	public static void initNewLocale(int locX, int locZ) {
		ensureManagerIsInitialized();
		
		//System.out.println("[Crumble] Creating locale covering " + String.valueOf(locX) + "," + String.valueOf(locZ));
		
		/*
		int sizeX;
		int sizeZ;
		int originX;
		int originZ;
		
		// determine how far this new locale can stretch in each direction
		int maxGrowNorth = 0;
		while (maxGrowNorth < MAX_LOCALE_CHUNK_SIZE - 1 && !isChunkClaimed(locX, locZ - maxGrowNorth - 1)) {
			maxGrowNorth += 1;
		}
		int maxGrowSouth = 0;
		while (maxGrowSouth < MAX_LOCALE_CHUNK_SIZE - 1 && !isChunkClaimed(locX, locZ + maxGrowSouth + 1)) {
			maxGrowSouth += 1;
		}
		int maxGrowWest = 0;
		while (maxGrowWest < MAX_LOCALE_CHUNK_SIZE - 1 && !isChunkClaimed(locX - maxGrowWest - 1, locZ)) {
			maxGrowWest += 1;
		}
		int maxGrowEast = 0;
		while (maxGrowEast < MAX_LOCALE_CHUNK_SIZE - 1 && !isChunkClaimed(locX + maxGrowEast + 1, locZ)) {
			maxGrowEast += 1;
		}
		
		System.out.println("[Crumble] - growth allowed: " +
			"north (" + String.valueOf(maxGrowNorth) + ") " + 
			"south (" + String.valueOf(maxGrowSouth) + ") " + 
			"west (" + String.valueOf(maxGrowWest) + ") " + 
			"east (" + String.valueOf(maxGrowEast) + ")");
		
		// determine if the locale is going to extend east or west (and set size in that direction)
		if (maxGrowWest == 0) {
			// can only grow east
			sizeX = rand.nextInt(maxGrowEast) + 1;
			originX = locX;
			System.out.println("[Crumble] - growing east with size " + String.valueOf(sizeX) + " and origin " + String.valueOf(originX));
		} else if (maxGrowEast == 0) {
			// can only grow west
			sizeX = rand.nextInt(maxGrowWest) + 1;
			originX = locX - (sizeX - 1);
			System.out.println("[Crumble] - growing west with size " + String.valueOf(sizeX) + " and origin " + String.valueOf(originX));
		} else {
			// could grow either way
			if (rand.nextInt(100) < 50) {
				// growing east
				sizeX = rand.nextInt(maxGrowEast) + 1;
				originX = locX;
				System.out.println("[Crumble] - growing east with size " + String.valueOf(sizeX) + " and origin " + String.valueOf(originX));
			} else {
				// growing west
				sizeX = rand.nextInt(maxGrowWest) + 1;
				originX = locX - (sizeX - 1);
				System.out.println("[Crumble] - growing west with size " + String.valueOf(sizeX) + " and origin " + String.valueOf(originX));
			}
		}
		
		// determine if the locale is going to extend north or south (and set size in that direction)
		if (maxGrowNorth == 0) {
			// can only grow south
			sizeZ = rand.nextInt(maxGrowSouth) + 1;
			originZ = locZ;
			System.out.println("[Crumble] - growing south with size " + String.valueOf(sizeZ) + " and origin " + String.valueOf(originZ));
		} else if (maxGrowSouth == 0) {
			// can only grow north
			sizeZ = rand.nextInt(maxGrowNorth) + 1;
			originZ = locZ - (sizeZ - 1);
			System.out.println("[Crumble] - growing north with size " + String.valueOf(sizeZ) + " and origin " + String.valueOf(originZ));
		} else {
			// could grow either way
			if (rand.nextInt(100) < 50) {
				// growing south
				sizeZ = rand.nextInt(maxGrowSouth) + 1;
				originZ = locZ;
				System.out.println("[Crumble] - growing south with size " + String.valueOf(sizeZ) + " and origin " + String.valueOf(originZ));
			} else {
				// growing north
				sizeZ = rand.nextInt(maxGrowNorth) + 1;
				originZ = locZ - (sizeZ - 1);
				System.out.println("[Crumble] - growing north with size " + String.valueOf(sizeZ) + " and origin " + String.valueOf(originZ));
			}
		}
		System.out.println("[Crumble] - done");
		*/
		
		//CrumbleLocale newLocale = new CrumbleLocale(originX, originZ, sizeX * 16, 40, sizeZ * 16);
		
		//    ^ real      |
		//    |           v testes 
		
		
		// create a 3x3 locale every 3 chunks and use that for now
		int newOriginX;
		if (locX >= 0) {
			newOriginX = locX - (locX % 3);
		} else {
			newOriginX = locX - (locX % 3) - 2;
		}
		int newOriginZ;
		if (locZ >= 0) {
			newOriginZ = locZ - (locZ % 3);
		} else {
			newOriginZ = locZ - (locZ % 3) - 2;
		}
		CrumbleLocale newLocale = new CrumbleLocale(newOriginX, newOriginZ, 48, 50, 48);
		
		// add the new locale to the locale array
		locales.add(newLocale);
		
	}
	
	// returns the locale originating at the passed X and Z chunk coordinates
	public static CrumbleLocale getLocaleContainingCoord(int chunkX, int chunkZ) {
		ensureManagerIsInitialized();
		
		for (CrumbleLocale result: locales) {
			
			int originX = result.getOriginX();
			int originZ = result.getOriginZ();
			int sizeX = result.getChunkSizeX();
			int sizeZ = result.getChunkSizeZ();
			
			if (originX <= chunkX && chunkX < originX + sizeX &&
				originZ <= chunkZ && chunkZ < originZ + sizeZ) {
				return result;
			}
		}
		
		return null;
	}
	
	// return a material matrix comprising the materials found in a specific locale with a specific chunk offset
	public static Material[][][] getBuildAtCoord(int chunkX, int chunkZ) {
		ensureManagerIsInitialized();
		
		// search for the locale covering the passed X and Z chunk coords
		for (CrumbleLocale locale : locales) {
			
			// assign vars to make the following equation easier to read
			int originX = locale.getOriginX();
			int originZ = locale.getOriginZ();
			int sizeX = locale.getChunkSizeX();
			int sizeZ = locale.getChunkSizeZ();
			
			// ensure this locale contains the passed X and Z chunk coords
			if (originX <= chunkX && chunkX < originX + sizeX &&
				originZ <= chunkZ && chunkZ < originZ + sizeZ) {

				//copy the pertient materials from the locale into the result
				int offsetX = chunkX - originX;
				int offsetZ = chunkZ - originZ;
				return locale.getBuild(offsetX, offsetZ);
			} 
		}
		
		System.out.println("[Crumble] No locale exists that contains the chunk at " + String.valueOf(chunkX) + "," + String.valueOf(chunkZ));
		return null;
		
	}
	
	// ----------------------------------------------------------------------------------------------------------------\\
	//    PRIVATE METHODS
	// ----------------------------------------------------------------------------------------------------------------//
	
	private static void ensureManagerIsInitialized() {
		
		if (locales == null) {
			System.out.println("[Crumble] Initializing locale manager.");
			locales = new ArrayList<CrumbleLocale>();
			rand = new Random();
			Brush.initialize();
		}
		
	}
	
}