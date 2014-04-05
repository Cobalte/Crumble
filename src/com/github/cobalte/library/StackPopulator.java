
package com.github.cobalte.library;

import java.util.Random;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;

public class StackPopulator extends BlockPopulator {

    // ----------------------------------------------------------------------------------------------------------------\\
  	//    PRIVATE VARS
  	// ----------------------------------------------------------------------------------------------------------------//
	
	private final int MIN_STACKS_PER_CHUNK = 4;
	private final int MAX_STACKS_PER_CHUNK = 16;
	
    // ----------------------------------------------------------------------------------------------------------------\\
  	//    PUBLIC METHODS
  	// ----------------------------------------------------------------------------------------------------------------//
	
    public void populate(World world, Random random, Chunk source) {
        
    	int stackCount = MIN_STACKS_PER_CHUNK + random.nextInt(MAX_STACKS_PER_CHUNK - MIN_STACKS_PER_CHUNK);
    	
    	for (int stack = 0; stack < stackCount; stack++) {
            
        	int centerX = (source.getX() << 4) + random.nextInt(16);
            int centerZ = (source.getZ() << 4) + random.nextInt(16);
            int height = random.nextInt(12) + 4;
            
            stackBooks(world, centerX, centerZ, height);
            stackBooks(world, centerX - 1, centerZ, Math.round(height * random.nextFloat()));
            stackBooks(world, centerX + 1, centerZ, Math.round(height * random.nextFloat()));
            stackBooks(world, centerX, centerZ - 1, Math.round(height * random.nextFloat()));
            stackBooks(world, centerX, centerZ + 1, Math.round(height * random.nextFloat()));
            
        }
    }
    
    private void stackBooks(World world, int locX, int locZ, int height) {
    	
    	int locY =  world.getHighestBlockYAt(locX, locZ);
    	for (int y = 0; y < height; y++) {
    		world.getBlockAt(locX, locY + y, locZ).setType(Material.BOOKSHELF);
    	}
    }
    
}
