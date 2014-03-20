package com.github.cobalte.crumble;

import java.util.ArrayList;
import java.util.Random;
import org.bukkit.Material;

public class CrumbleLocale {
	
	// ----------------------------------------------------------------------------------------------------------------\\
	//    PRIVATE VARS
	// ----------------------------------------------------------------------------------------------------------------//
	
	private int blockSizeX;
	private int blockSizeY;
	private int blockSizeZ;
	private int originChunkX;
	private int originChunkZ;
	private Material[][][] contents;
	private static Random rand;
	
	private Brush wallPalette;
	private Brush floorPalette;
	private Brush roadPalette;
	
	// ----------------------------------------------------------------------------------------------------------------\\
	//    PUBLIC PROPERTIES
	// ----------------------------------------------------------------------------------------------------------------//
	
	public int getBlockSizeX() {
		return blockSizeX;
	}
	
	public int getBlockSizeY() {
		return blockSizeY;
	}
	
	public int getBlockSizeZ() {
		return blockSizeZ;
	}
	
	public int getChunkSizeX() {
		return blockSizeX / 16;
	}
	
	public int getChunkSizeZ() {
		return blockSizeZ /  16;
	}
	
	public int getOriginX() {
		return originChunkX;
	}
	
	public int getOriginZ() {
		return originChunkZ;
	}
	
	public Material getMaterial(int blockX, int blockY, int blockZ) {
		return contents[blockX][blockY][blockZ];
	}
	
	// ----------------------------------------------------------------------------------------------------------------\\
	//    PUBLIC METHODS
	// ----------------------------------------------------------------------------------------------------------------//
	
	// returns a Material matrix comprising the materials for one chunk of this locale
	public Material[][][] getBuild(int offsetX, int offsetZ) {
		
		// ensure the caller isn't trying to access a chunk not contained in this locale
		if (offsetX < 0 || getChunkSizeX() <= offsetX || offsetZ < 0 || getChunkSizeZ() <= offsetZ) {
			System.out.println("[Crumble] Could not get a material matrix for a locale: offset out of bounds.");
			return null;
		}
		
		// copy the pertient materials 
		Material[][][] result = new Material[16][blockSizeY][16];
		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				for (int y = 0; y < blockSizeY; y++) {
					result[x][y][z] = contents[offsetX * 16 + x][y][offsetZ * 16 + z];
				}
			}
		}
		
		return result;
	}
	
	// ----------------------------------------------------------------------------------------------------------------\\
	//    GENERAL LOCALE POPULATION
	// ----------------------------------------------------------------------------------------------------------------//
	
	// populates this locale
	private void populate() {
		
		int wallHeight = rand.nextInt(blockSizeY - 20) + 20;
		int grassWidth = rand.nextInt(2) * 2 + 2; // 2 or 4
		int roadWidth = 7;
		
		// make things
		createStructure(
			grassWidth + roadWidth,
			0,
			grassWidth + roadWidth,
			blockSizeX - (grassWidth * 2) - roadWidth,
			wallHeight,
			blockSizeZ - (grassWidth * 2) - roadWidth);

		// road running north/south
		fillArea(0, 0, 0, roadWidth, 1, blockSizeZ, Palette.Road);
		
		// road running east/west
		fillArea(0, 0, 0, blockSizeX, 1, roadWidth, Palette.Road);
		
		// make things...crumble
		clusteredDecayPass();
		risingDecayPass();
		
	}
	
	// create sections of decay - splotch-like areas all over
	private void clusteredDecayPass() {
		
		// fence decay (smooth brick --> fence)
		final int fenceDecayChance = 5;
		for (int x = 0; x < blockSizeX; x++) {
			for (int z = 0; z < blockSizeZ; z++) {
				for (int y = 0; y < blockSizeY; y++) {
					if (contents[x][y][z] == Material.SMOOTH_BRICK && rand.nextInt(100) < fenceDecayChance) {
						
						causeDecay(x, y, z, 25, 25, 25);
						
					}
				}
			}
		}
		
		// TODO: add clustered air decay
		
	}
	
	// create V-shaped areas of decay that look like the building has collapsed
	private void risingDecayPass() {
		
		// fence decay (smooth brick --> fence)
		final int fenceDecayChance = 2;
		for (int x = 0; x < blockSizeX; x++) {
			for (int z = 0; z < blockSizeZ; z++) {
				for (int y = 0; y < blockSizeY; y++) {
					if (contents[x][y][z] == Material.SMOOTH_BRICK && rand.nextInt(100) < fenceDecayChance) {
						
						causeDecay(x, y, z, 20, 100, 0);
						
					}
				}
			}
		}
		
		// TODO: make rising decay real V-shaped decay
		
		// TODO: add rising air decay
		
	}
	
	// make decay spread (recursively) from a starting location
	private void causeDecay(int x, int y, int z, int lateralDecayChance, int upwardDecayChance, int downwardDecayChance) {
		
		// decay this block
		contents[x][y][z] = Material.FENCE;
		
		// check nearby blocks and decay based on decay chance
		if (x + 1 < blockSizeX) {
			if (contents[x + 1][y][z] == Material.SMOOTH_BRICK && rand.nextInt(100) < lateralDecayChance) {
				causeDecay(x + 1, y, z, lateralDecayChance, upwardDecayChance, downwardDecayChance);
			}
		}
		if (x - 1 >= 0) {
			if (contents[x - 1][y][z] == Material.SMOOTH_BRICK && rand.nextInt(100) < lateralDecayChance) {
				causeDecay(x - 1, y, z, lateralDecayChance, upwardDecayChance, downwardDecayChance);
			}
		}
		if (z + 1 < blockSizeZ) {
			if (contents[x][y][z + 1] == Material.SMOOTH_BRICK && rand.nextInt(100) < lateralDecayChance) {
				causeDecay(x, y, z + 1, lateralDecayChance, upwardDecayChance, downwardDecayChance);
			}
		}
		if (z - 1 >= 0) {
			if (contents[x][y][z - 1] == Material.SMOOTH_BRICK && rand.nextInt(100) < lateralDecayChance) {
				causeDecay(x, y, z - 1, lateralDecayChance, upwardDecayChance, downwardDecayChance);
			}
		}
		if (y + 1 < blockSizeY) {
			if (contents[x][y + 1][z] == Material.SMOOTH_BRICK && rand.nextInt(100) < upwardDecayChance) {
				causeDecay(x, y + 1, z, lateralDecayChance, upwardDecayChance, downwardDecayChance);
			}
		}
		if (y - 1 >= 0) {
			if (contents[x][y - 1][z] == Material.SMOOTH_BRICK && rand.nextInt(100) < downwardDecayChance) {
				causeDecay(x, y - 1, z, lateralDecayChance, upwardDecayChance, downwardDecayChance);
			}
		}
		
	}
	
	// populates this locale
	private void populateBoring() {

		//just cover the ground with a 1-think layer of a random material for now
		//System.out.println("[Crumble] Populating locale with origin at " + String.valueOf(originChunkX) + "," + String.valueOf(originChunkZ));
		
		// init array of materials
		Material[] matList = new Material[20];
		matList[0] = Material.COBBLESTONE;
		matList[1] = Material.SMOOTH_BRICK;
		matList[2] = Material.BRICK;
		matList[3] = Material.BOOKSHELF;
		matList[4] = Material.CLAY;
		matList[5] = Material.COAL_ORE;
		matList[6] = Material.IRON_ORE;
		matList[7] = Material.DIAMOND_ORE;
		matList[8] = Material.EMERALD_ORE;
		matList[9] = Material.REDSTONE_ORE;
		matList[10] = Material.COAL_BLOCK;
		matList[11] = Material.IRON_BLOCK;
		matList[12] = Material.DIAMOND_BLOCK;
		matList[13] = Material.EMERALD_BLOCK;
		matList[14] = Material.REDSTONE_BLOCK;
		matList[15] = Material.LAPIS_ORE;
		matList[16] = Material.LAPIS_BLOCK;
		matList[17] = Material.WOOD;
		matList[18] = Material.GLOWSTONE;
		matList[19] = Material.GRAVEL;
		
		// pick a random material
		Material mat = matList[rand.nextInt(matList.length)];
		
		// cover the ground with that material
		for (int x = 1; x < blockSizeX - 1; x++) {
			for (int z = 1; z < blockSizeZ - 1; z++) {
				contents[x][0][z] = mat;
			}
		}
	}
	
	// ----------------------------------------------------------------------------------------------------------------\\
	//    PATTERN POPULATION
	// ----------------------------------------------------------------------------------------------------------------//
	
	private void createStructure(int locX, int locY, int locZ, int sizeX, int sizeY, int sizeZ) {
		
		// north wall
		fillArea(locX, locY, locZ, sizeX, sizeY, 1, Palette.Wall);
		
		// west wall
		fillArea(locX, locY, locZ, 1, sizeY, sizeZ, Palette.Wall);
		
		// south wall
		fillArea(locX, locY, locZ + sizeZ - 1, sizeX, sizeY, 1, Palette.Wall);
		
		// east wall
		fillArea(locX + sizeX - 1, locY, locZ, 1, sizeY, sizeZ, Palette.Wall);
		
		// floor
		fillArea(locX, locY, locZ, sizeX, 1, sizeZ, Palette.Floor);
				
	}
	
	private void fillArea(int locX, int locY, int locZ, int sizeX, int sizeY, int sizeZ, Palette pal) {
		
		for (int x = locX; x < locX + sizeX; x++) {
			for (int z = locZ; z < locZ + sizeZ; z++) {
				for (int y = locY; y < locY + sizeY; y++) {
					contents[x][y][z] = Brush.pickRandom(pal);
				}
			}
		}
		
	}
	
	// ----------------------------------------------------------------------------------------------------------------\\
	//    CONSTRUCTORS
	// ----------------------------------------------------------------------------------------------------------------//

	public CrumbleLocale(int originX, int originZ, int blockWidth, int blockHeight, int blockLength) {
		
		// init general things
		originChunkX = originX;
		originChunkZ = originZ;
		blockSizeX = blockWidth;
		blockSizeY = blockHeight;
		blockSizeZ = blockLength;
		contents = new Material[blockSizeX][blockSizeY][blockSizeZ];
		rand = new Random();
		
		// populate!
		populate();
		
	}
	
}
