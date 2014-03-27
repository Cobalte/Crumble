package com.github.cobalte.crumble;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Material;

public class Locale {
	
	// ----------------------------------------------------------------------------------------------------------------\\
	//    PRIVATE VARS
	// ----------------------------------------------------------------------------------------------------------------//
	
	private int blockSizeX;
	private int blockSizeY;
	private int blockSizeZ;
	private int originChunkX;
	private int originChunkZ;
	private Blok[][][] contents;
	
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
	
	public Blok getBlock(int blockX, int blockY, int blockZ) {
		return contents[blockX][blockY][blockZ];
	}
	
	// ----------------------------------------------------------------------------------------------------------------\\
	//    PUBLIC METHODS
	// ----------------------------------------------------------------------------------------------------------------//
	
	// returns a Material matrix comprising the materials for one chunk of this locale
	public Blok[][][] getBuild(int offsetX, int offsetZ) {
		
		// ensure the caller isn't trying to access a chunk not contained in this locale
		if (offsetX < 0 || getChunkSizeX() <= offsetX || offsetZ < 0 || getChunkSizeZ() <= offsetZ) {
			Crumble.log("Could not get a material matrix for a locale: offset out of bounds.");
			return null;
		}
		
		// copy the pertient materials 
		Blok[][][] result = new Blok[16][blockSizeY][16];
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
		
		int wallHeight = Crumble.getRandInt(blockSizeY - 20) + 20;
		int grassWidth = Crumble.getRandInt(2) * 2 + 2; // 2 or 4
		int roadWidth = 7;
		
		// building
		createStructure(grassWidth + roadWidth, 0, grassWidth + roadWidth, blockSizeX - (grassWidth * 2) - roadWidth, wallHeight, blockSizeZ - (grassWidth * 2) - roadWidth);

		// road running north/south
		fillArea(0, 0, 0, roadWidth, 1, blockSizeZ, Palette.ROAD);
		
		// road running east/west
		fillArea(0, 0, 0, blockSizeX, 1, roadWidth, Palette.ROAD);
		
		// make things...crumble
		causeDecay();
		
	}
	
	// run the various decay passes on the locale
	private void causeDecay() {
		Material[] sourceMats;
		
		// turn bricks to fence
		sourceMats = new Material[1];
		sourceMats[0] = Material.SMOOTH_BRICK;
		clusteredDecayPass(sourceMats, Material.FENCE);
		risingDecayPass(sourceMats, Material.FENCE);
		
		// turn bricks and fence to air
		sourceMats = new Material[2];
		sourceMats[0] = Material.SMOOTH_BRICK;
		sourceMats[1] = Material.SMOOTH_BRICK;
		clusteredDecayPass(sourceMats, Material.AIR);
		risingDecayPass(sourceMats, Material.AIR);
		
	}
	
	// create sections of decay - splotch-like areas all over
	private void clusteredDecayPass(Material[] sourceMats, Material decayMat) {
		
		final int decayChance = 5;
		
		for (int x = 0; x < blockSizeX; x++) {
			for (int z = 0; z < blockSizeZ; z++) {
				for (int y = 0; y < blockSizeY; y++) {
					
					// only decay this block if the material matches and a random check is passed 
					if (materialFoundInArray(contents[x][y][z].getMaterial(), sourceMats) && Crumble.getRandInt(100) < decayChance) {
						omnidirectionalDecaySpread(x, y, z, 25, 25, 25, sourceMats, decayMat);
					}
					
				}
			}
		}
		
	}
	
	// create V-shaped areas of decay that look like the building has collapsed
	private void risingDecayPass(Material[] sourceMats, Material decayMat) {
		
		final int decayChance = 1;
		
		for (int x = 0; x < blockSizeX; x++) {
			for (int z = 0; z < blockSizeZ; z++) {
				for (int y = 0; y < blockSizeY; y++) {
					
					// only decay this block if the material matches and a random check is passed 
					if (materialFoundInArray(contents[x][y][z].getMaterial(), sourceMats) && Crumble.getRandInt(100) < decayChance) {
						omnidirectionalDecaySpread(x, y, z, 15, 100, 0, sourceMats, decayMat);
					}
					
				}
			}
		}
		
		// TODO: make rising decay real V-shaped decay
		
		// TODO: add rising air decay
		
	}
	
	// make decay spread (recursively) from a starting location
	private void omnidirectionalDecaySpread(int x, int y, int z, int lateralDecayChance, int upwardDecayChance, int downwardDecayChance, Material[] sourceMats, Material decayMat) {
		
		// decay this block
		contents[x][y][z] = new Blok(decayMat);
		
		// check nearby blocks and decay based on decay chance
		if (x + 1 < blockSizeX) {
			if (materialFoundInArray(contents[x + 1][y][z].getMaterial(), sourceMats) && Crumble.getRandInt(100) < lateralDecayChance) {
				omnidirectionalDecaySpread(x + 1, y, z, lateralDecayChance, upwardDecayChance, downwardDecayChance, sourceMats, decayMat);
			}
		}
		if (x - 1 >= 0) {
			if (materialFoundInArray(contents[x - 1][y][z].getMaterial(), sourceMats) && Crumble.getRandInt(100) < lateralDecayChance) {
				omnidirectionalDecaySpread(x - 1, y, z, lateralDecayChance, upwardDecayChance, downwardDecayChance, sourceMats, decayMat);
			}
		}
		if (z + 1 < blockSizeZ) {
			if (materialFoundInArray(contents[x][y][z + 1].getMaterial(), sourceMats) && Crumble.getRandInt(100) < lateralDecayChance) {
				omnidirectionalDecaySpread(x, y, z + 1, lateralDecayChance, upwardDecayChance, downwardDecayChance, sourceMats, decayMat);
			}
		}
		if (z - 1 >= 0) {
			if (materialFoundInArray(contents[x][y][z - 1].getMaterial(), sourceMats) && Crumble.getRandInt(100) < lateralDecayChance) {
				omnidirectionalDecaySpread(x, y, z - 1, lateralDecayChance, upwardDecayChance, downwardDecayChance, sourceMats, decayMat);
			}
		}
		if (y + 1 < blockSizeY) {
			if (materialFoundInArray(contents[x][y + 1][z].getMaterial(), sourceMats) && Crumble.getRandInt(100) < upwardDecayChance) {
				omnidirectionalDecaySpread(x, y + 1, z, lateralDecayChance, upwardDecayChance, downwardDecayChance, sourceMats, decayMat);
			}
		}
		if (y - 1 >= 0) {
			if (materialFoundInArray(contents[x][y - 1][z].getMaterial(), sourceMats) && Crumble.getRandInt(100) < downwardDecayChance) {
				omnidirectionalDecaySpread(x, y - 1, z, lateralDecayChance, upwardDecayChance, downwardDecayChance, sourceMats, decayMat);
			}
		}
		
	}

	private boolean materialFoundInArray(Material mat, Material[] matArray) {
		
		for (int i = 0; i < matArray.length; i++) {
			if (mat == matArray[i]) {
				return true;
			}
		}
		
		return false;
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
		Material mat = matList[Crumble.getRandInt(matList.length)];
		
		// cover the ground with that material
		for (int x = 1; x < blockSizeX - 1; x++) {
			for (int z = 1; z < blockSizeZ - 1; z++) {
				contents[x][0][z] = new Blok(mat);
			}
		}
	}
	
	// ----------------------------------------------------------------------------------------------------------------\\
	//    PATTERN POPULATION
	// ----------------------------------------------------------------------------------------------------------------//
	
	private void createStructure(int locX, int locY, int locZ, int sizeX, int sizeY, int sizeZ) {
		
		// north wall
		fillArea(locX, locY, locZ, sizeX, sizeY, 1, Palette.WALL);
		
		// west wall
		fillArea(locX, locY, locZ, 1, sizeY, sizeZ, Palette.WALL);
		
		// south wall
		fillArea(locX, locY, locZ + sizeZ - 1, sizeX, sizeY, 1, Palette.WALL);
		
		// east wall
		fillArea(locX + sizeX - 1, locY, locZ, 1, sizeY, sizeZ, Palette.WALL);
		
		// floor
		fillArea(locX, locY, locZ, sizeX, 1, sizeZ, Palette.FLOOR);
				
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

	public Locale(int originX, int originZ, int blockWidth, int blockHeight, int blockLength) {
		
		// init general things
		originChunkX = originX;
		originChunkZ = originZ;
		blockSizeX = blockWidth;
		blockSizeY = blockHeight;
		blockSizeZ = blockLength;
		contents = new Blok[blockSizeX][blockSizeY][blockSizeZ];
		
		// init contents matrix
		for (int x = 0; x < blockSizeX; x++) {
			for (int y = 0; y < blockSizeY; y++) {
				for (int z = 0; z < blockSizeZ; z++) {
					contents[x][y][z] = new Blok();
				}
			}
		}
		
		// populate!
		populate();
		
	}
	
}
