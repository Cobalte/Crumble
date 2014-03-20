package com.github.cobalte.crumble;

import java.util.Random;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;

public class CrumbleStructurePopulator extends BlockPopulator {

	// ----------------------------------------------------------------------------------------------------------------\\
	//    PRIVATE VARS
	// ----------------------------------------------------------------------------------------------------------------//
	
	private static final int GROUND_LEVEL = 40;
	
	// ----------------------------------------------------------------------------------------------------------------\\
	//    PUBLIC PROPERTIES
	// ----------------------------------------------------------------------------------------------------------------//
	
	// ----------------------------------------------------------------------------------------------------------------\\
	//    PUBLIC METHODS
	// ----------------------------------------------------------------------------------------------------------------//
	
	// this method gets called every time a new chunk is generated
	public void populate(World world, Random random, Chunk chunk) {
		
		//System.out.println("[Crumble] Requesting structure build for chunk at " + String.valueOf(chunk.getX()) + "," + String.valueOf(chunk.getZ()));
		
		// ensure this chunk is covered by an existing locale, then import the materials into this chunk
		if (!CrumbleLocaleManager.isChunkClaimed(chunk.getX(), chunk.getZ())) {
			CrumbleLocaleManager.initNewLocale(chunk.getX(), chunk.getZ());
		}
		importLocaleToWorld(world, chunk.getX(), chunk.getZ());
		
	}
	
	// ----------------------------------------------------------------------------------------------------------------\\
	//    PRIVATE METHODS
	// ----------------------------------------------------------------------------------------------------------------//
	
	// populates the new chunk with a material matrix provided by the locale manager 
	private void importLocaleToWorld(World world, int coveringX, int coveringZ) {
		
		//System.out.println("[Crumble] Importing build for chunk at " + String.valueOf(coveringX) + "," + String.valueOf(coveringZ));
		
		// get the new locale
		Material[][][] mats = CrumbleLocaleManager.getBuildAtCoord(coveringX, coveringZ);
		
		// import the build materials to this chunk
		for (int x = 0; x < 16 ; x++) {
			for (int z = 0; z < 16; z++) {
				for (int y = 0; y < mats[0].length; y++) {
					
					Material thisMat = mats[x][y][z];
					if (thisMat != null) {
						int absX = (coveringX << 4) + x;
						int absZ = (coveringZ << 4) + z;
						world.getBlockAt(absX, y + GROUND_LEVEL, absZ).setType(thisMat);
					}
				}
			}
		
		/*
		// get the new locale
		CrumbleLocale locale = CrumbleLocaleManager.getLocaleContainingCoord(coveringX, coveringZ);
		int originX = locale.getOriginX();
		int originZ = locale.getOriginZ();
		
		System.out.println("[Crumble] Importing locale at " + String.valueOf(originX) + "," + String.valueOf(originZ));
		
		// import the build materials to this chunk
		for (int x = 0; x < locale.getBlockSizeX() ; x++) {
			for (int z = 0; z < locale.getBlockSizeZ(); z++) {
				for (int y = 0; y < locale.getBlockSizeY(); y++) {
					
					Material thisMat = locale.getMaterial(x, y, z);
					if (thisMat != null) {
						int absX = (originX << 4) + x;
						int absZ = (originZ << 4) + z;
						world.getBlockAt(absX, y + GROUND_LEVEL, absZ).setType(thisMat);
					}
				}
			}
		*/
		
		}
	}
	
}