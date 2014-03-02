package io.github.cobalte;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Random;

public class CrumbleLocaleManager {
	
	private static final int MAX_LOCALE_CHUNK_SIZE = 3;
	private static Random rand;
	private static ArrayList<CrumbleLocale> locales;
	
	// returns true if the chunk found at the passed coordinates is already claimed by a locale
	private static boolean isChunkClaimed(int locX, int locZ) {
		
		for(int i = 0; i < locales.size(); i++) {
			
			// get info on this locale to make the following equasion easier to read
			int originX = locales.get(i).getOriginX();
			int originZ = locales.get(i).getOriginZ();
			int sizeX = locales.get(i).getWidth();
			int sizeZ = locales.get(i).getLength();
			
			// check if the passed X and Z are within the bounds of this locale
			if (originX <= locX && locX <= originX + sizeX - 1 && originZ <= locZ && locZ <= originZ + sizeZ - 1) {
				return true;
			}
		}
		return false;
	}
	
	// creates a locale covering the chunk at the passed X and Z locations
	private static void createLocale(int locX, int locZ) {
		
		// logging is weird
		try {
			PrintStream out = new PrintStream(new FileOutputStream("D:\\Minecraft\\CraftBukkit\\locale-creation-log.txt"));
			System.setOut(out);
		}
		catch(IOException ex) {
	        System.out.println("Error during reading/writing");
		}
		
		// statics are weird
		if (rand == null) {
			rand = new Random();
		}
		
		System.out.println("[CRUMBLE] Creating locale at " + String.valueOf(locX) + "," + String.valueOf(locZ));
		
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
		
		System.out.println("[CRUMBLE] - growth allowed: " +
			"north (" + String.valueOf(maxGrowNorth) + ") " + 
			"south (" + String.valueOf(maxGrowSouth) + ") " + 
			"west (" + String.valueOf(maxGrowWest) + ") " + 
			"east (" + String.valueOf(maxGrowEast) + ")");
		
		// determine if the locale is going to extend east or west (and set size in that direction)
		if (maxGrowWest == 0) {
			// can only grow east
			sizeX = rand.nextInt(maxGrowEast) + 1;
			originX = locX;
			System.out.println("[CRUMBLE] - growing east with size " + String.valueOf(sizeX) + " and origin " + String.valueOf(originX));
		} else if (maxGrowEast == 0) {
			// can only grow west
			sizeX = rand.nextInt(maxGrowWest) + 1;
			originX = locX - (sizeX - 1);
			System.out.println("[CRUMBLE] - growing west with size " + String.valueOf(sizeX) + " and origin " + String.valueOf(originX));
		} else {
			// could grow either way
			if (rand.nextInt(100) < 50) {
				// growing east
				sizeX = rand.nextInt(maxGrowEast);
				originX = locX;
				System.out.println("[CRUMBLE] - growing east with size " + String.valueOf(sizeX) + " and origin " + String.valueOf(originX));
			} else {
				// growing west
				sizeX = rand.nextInt(maxGrowWest);
				originX = locX - (sizeX - 1);
				System.out.println("[CRUMBLE] - growing west with size " + String.valueOf(sizeX) + " and origin " + String.valueOf(originX));
			}
		}
		
		// determine if the locale is going to extend north or south (and set size in that direction)
		if (maxGrowNorth == 0) {
			// can only grow south
			sizeZ = rand.nextInt(maxGrowSouth);
			originZ = locZ;
			System.out.println("[CRUMBLE] - growing south with size " + String.valueOf(sizeZ) + " and origin " + String.valueOf(originZ));
		} else if (maxGrowSouth == 0) {
			// can only grow north
			sizeZ = rand.nextInt(maxGrowNorth);
			originZ = locZ - (sizeZ - 1);
			System.out.println("[CRUMBLE] - growing north with size " + String.valueOf(sizeZ) + " and origin " + String.valueOf(originZ));
		} else {
			// could grow either way
			if (rand.nextInt(100) < 50) {
				// growing south
				sizeZ = rand.nextInt(maxGrowSouth);
				originZ = locZ;
				System.out.println("[CRUMBLE] - growing south with size " + String.valueOf(sizeZ) + " and origin " + String.valueOf(originZ));
			} else {
				// growing north
				sizeZ = rand.nextInt(maxGrowNorth);
				originZ = locZ - (sizeZ - 1);
				System.out.println("[CRUMBLE] - growing north with size " + String.valueOf(sizeZ) + " and origin " + String.valueOf(originZ));
			}
		}
		
		CrumbleLocale newLocale = new CrumbleLocale(originX, originZ, sizeX, sizeZ);
		locales.add(newLocale);
		
	}
	
	// searches for the locale covering the chunk at the passed X and Z, and returns the build found at that exact chunk
	public static CrumbleBuild getBuild(int chunkX, int chunkZ) {
		
		if (locales == null) {
			System.out.println("[CRUMBLE] Initializing locale array");
			locales = new ArrayList<CrumbleLocale>();
			locales.clear();
		}
		
		if (!isChunkClaimed(chunkX, chunkZ)) {
			// chunk is not claimed - make a new locale and populate it
			createLocale(chunkX, chunkZ);
		}

		// get the build mats for the requested chunk and return them
		CrumbleBuild buildMats = new CrumbleBuild();

		for(int i = 0; i < locales.size(); i++) {
			
			// get info on this locale to make the following equasion easier to read
			int originX = locales.get(i).getOriginX();
			int originZ = locales.get(i).getOriginZ();
			int sizeX = locales.get(i).getWidth();
			int sizeZ = locales.get(i).getLength();
			
			// check if the passed X and Z are within the bounds of this locale
			if (originX <= chunkX && chunkX <= originX + sizeX - 1 && originZ <= chunkZ && chunkZ <= originZ + sizeZ - 1) {
				int offsetX = chunkX - originX;
				int offsetZ = chunkZ - originZ;
				buildMats = locales.get(i).getBuild(offsetX, offsetZ);
			}
		}
		
		return buildMats;
		
	}
	
}