
package com.github.cobalte.crumble;

import java.util.Random;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;

public class TowerPopulator extends BlockPopulator {

    // ----------------------------------------------------------------------------------------------------------------\\
  	//    PRIVATE VARS
  	// ----------------------------------------------------------------------------------------------------------------//
	
	private final int TOWER_CHANCE = 3; // out of 200
	
	private final int ROOM_HEIGHT = 6;
	private final int ROOM_WIDTH = 10;
	private final int MIN_FLOORS = 8;
	private final int MAX_FLOORS = 14;
	private final int LOWEST_TOWER_Y = 90;
	
	
    // ----------------------------------------------------------------------------------------------------------------\\
  	//    PUBLIC METHODS
  	// ----------------------------------------------------------------------------------------------------------------//
	
    public void populate(World world, Random random, Chunk source) {
    	
    	if (random.nextInt(200) < TOWER_CHANCE) {
    		
    		int startX = (source.getX() << 4) + random.nextInt(16);
            int startZ = (source.getZ() << 4) + random.nextInt(16);
            
            int maxFloors = Crumble.rand.nextInt(MAX_FLOORS - MIN_FLOORS) + MIN_FLOORS;
        	int floorCount;
        	
            // central column
            floorCount = maxFloors;
        	for (int f = 0; f < floorCount; f++) {
            	makeRoom(world, startX, (f * ROOM_HEIGHT) + LOWEST_TOWER_Y, startZ, ROOM_WIDTH, ROOM_HEIGHT, ROOM_WIDTH);
            }
        	fillArea(world, startX, (floorCount * ROOM_HEIGHT) + LOWEST_TOWER_Y, startZ, ROOM_WIDTH + 1, 1, ROOM_WIDTH + 1, "wall");
        	
        	// north column
        	floorCount = Crumble.rand.nextInt(maxFloors - 2) + 1;
        	for (int f = 0; f < floorCount; f++) {
            	makeRoom(world, startX, (f * ROOM_HEIGHT) + LOWEST_TOWER_Y, startZ - ROOM_WIDTH, ROOM_WIDTH, ROOM_HEIGHT, ROOM_WIDTH);
            }
        	fillArea(world, startX, (floorCount * ROOM_HEIGHT) + LOWEST_TOWER_Y, startZ - ROOM_WIDTH, ROOM_WIDTH + 1, 1, ROOM_WIDTH + 1, "wall");
        	
        	// west column
        	floorCount = Crumble.rand.nextInt(maxFloors - 2) + 1;
        	for (int f = 0; f < floorCount; f++) {
            	makeRoom(world, startX - ROOM_WIDTH, (f * ROOM_HEIGHT) + LOWEST_TOWER_Y, startZ, ROOM_WIDTH, ROOM_HEIGHT, ROOM_WIDTH);
            }
        	fillArea(world, startX - ROOM_WIDTH, (floorCount * ROOM_HEIGHT) + LOWEST_TOWER_Y, startZ, ROOM_WIDTH + 1, 1, ROOM_WIDTH + 1, "wall");
        	
        	// east column
        	floorCount = Crumble.rand.nextInt(maxFloors - 2) + 1;
        	for (int f = 0; f < floorCount; f++) {
            	makeRoom(world, startX + ROOM_WIDTH, (f * ROOM_HEIGHT) + LOWEST_TOWER_Y, startZ, ROOM_WIDTH, ROOM_HEIGHT, ROOM_WIDTH);
            }
        	fillArea(world, startX + ROOM_WIDTH, (floorCount * ROOM_HEIGHT) + LOWEST_TOWER_Y, startZ, ROOM_WIDTH + 1, 1, ROOM_WIDTH + 1, "wall");
        	
        	// south column
        	floorCount = Crumble.rand.nextInt(maxFloors - 2) + 1;
        	for (int f = 0; f < floorCount; f++) {
            	makeRoom(world, startX, (f * ROOM_HEIGHT) + LOWEST_TOWER_Y, startZ + ROOM_WIDTH, ROOM_WIDTH, ROOM_HEIGHT, ROOM_WIDTH);
            }
        	fillArea(world, startX, (floorCount * ROOM_HEIGHT) + LOWEST_TOWER_Y, startZ + ROOM_WIDTH, ROOM_WIDTH + 1, 1, ROOM_WIDTH + 1, "wall");
    		
    	}
    	
    }
    
    // ----------------------------------------------------------------------------------------------------------------\\
  	//    PRIVATE METHODS
  	// ----------------------------------------------------------------------------------------------------------------//
    
    private void makeRoom(World world, int locX, int locY, int locZ, int sizeX, int sizeY, int sizeZ) {
    	
    	// north wall
        fillArea(world, locX, locY, locZ, sizeX, sizeY, 1, "wall");
        
        // west wall
        fillArea(world, locX, locY, locZ, 1, sizeY, sizeZ, "wall");
        
        // south wall
        fillArea(world, locX, locY, locZ + sizeZ, sizeX + 1, sizeY, 1, "wall");
        
        // east wall
        fillArea(world, locX + sizeX, locY, locZ, 1, sizeY, sizeZ, "wall");
        
        // floor
        fillArea(world, locX + 1, locY, locZ + 1, sizeX - 1, 1, sizeZ - 1, "floor");
        
        // inside
        fillArea(world, locX + 1, locY + 1, locZ + 1, sizeX - 1, sizeY - 1, sizeZ - 1, "air");
        
    }
    
    private void fillArea(World world, int locX, int locY, int locZ, int sizeX, int sizeY, int sizeZ, String brush) {
		
    	for (int x = locX; x < locX + sizeX; x++) {
			for (int z = locZ; z < locZ + sizeZ; z++) {
				for (int y = locY; y < locY + sizeY; y++) {
					world.getBlockAt(x, y, z).setType(Palette.getBrush(brush));
				}
			}
		}
		
	}
    
}
