
package com.github.cobalte.crumble;

import java.util.ArrayList;
import java.util.Random;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;

public class TowerPopulator extends BlockPopulator {

    // ----------------------------------------------------------------------------------------------------------------\\
  	//    PRIVATE VARS
  	// ----------------------------------------------------------------------------------------------------------------//
	
	private final int TOWER_CHANCE = 1; // out of 200
	
	private final int ROOM_HEIGHT = 5;
	private final int ROOM_WIDTH = 12;
	private final int MIN_FLOORS = 14;
	private final int MAX_FLOORS = 28;
	private final int LOWEST_TOWER_Y = 80;
	
	private ArrayList<TowerColumn> columns;
	
    // ----------------------------------------------------------------------------------------------------------------\\
  	//    PUBLIC METHODS
  	// ----------------------------------------------------------------------------------------------------------------//
	
    public void populate(World world, Random random, Chunk source) {
    	
    	if (random.nextInt(200) < TOWER_CHANCE) {
    		
    		int startX = (source.getX() << 4) + random.nextInt(16);
            int startZ = (source.getZ() << 4) + random.nextInt(16);
            Crumble.log("Building new tower at coords " + startX + "," + startZ, true);
            
            // generate tower columns
            int startingColumns =  Crumble.rand.nextInt(MAX_FLOORS - MIN_FLOORS) + MIN_FLOORS;
            columns = new ArrayList<TowerColumn>();
            columns.add(new TowerColumn(0, 0, startingColumns));
            spreadColumns(0, 0, startingColumns);
    		
            // build tower columns
            for (TowerColumn col : columns) {
            	for (int f = 0; f < col.floors; f++) {
            		buildRoom(world, col.offsetX * ROOM_WIDTH + startX, f * ROOM_HEIGHT + LOWEST_TOWER_Y, col.offsetZ * ROOM_WIDTH + startZ);
            	}
            	// add a roof to this column
            	int roofX = col.offsetX * ROOM_WIDTH + startX;
            	int roofY = col.floors * ROOM_HEIGHT + LOWEST_TOWER_Y;
            	int roofZ = col.offsetZ * ROOM_WIDTH + startZ;
            	fillArea(world, roofX, roofY, roofZ, ROOM_WIDTH + 1, 1, ROOM_WIDTH + 1, "wall");
            }
            
    	}
    	
    }
    
    // ----------------------------------------------------------------------------------------------------------------\\
  	//    PRIVATE METHODS
  	// ----------------------------------------------------------------------------------------------------------------//
    
    private void spreadColumns(int spreaderX, int spreaderZ, int spreaderFloors) {
    	
    	if (spreaderFloors > 4) { // we don't want columns to trail out forever

			// spread north
    		if (getColumnFloorCount(spreaderX, spreaderZ - 1) == 0) {
    			int newFloors = Crumble.rand.nextInt(spreaderFloors/2) + spreaderFloors/2;
    			columns.add(new TowerColumn(spreaderX, spreaderZ - 1, newFloors));
    			spreadColumns(spreaderX, spreaderZ - 1, newFloors);
    		}

    		// spread south
    		if (getColumnFloorCount(spreaderX, spreaderZ + 1) == 0) {
    			int newFloors = Crumble.rand.nextInt(spreaderFloors/2) + spreaderFloors/2;
    			columns.add(new TowerColumn(spreaderX, spreaderZ + 1, newFloors));
    			spreadColumns(spreaderX, spreaderZ + 1, newFloors);
    		}
    		
    		// spread west
    		if (getColumnFloorCount(spreaderX - 1, spreaderZ) == 0) {
    			int newFloors = Crumble.rand.nextInt(spreaderFloors/2) + spreaderFloors/2;
    			columns.add(new TowerColumn(spreaderX - 1, spreaderZ, newFloors));
    			spreadColumns(spreaderX - 1, spreaderZ, newFloors);
    		}
    		
    		// spread east
    		if (getColumnFloorCount(spreaderX + 1, spreaderZ) == 0) {
    			int newFloors = Crumble.rand.nextInt(spreaderFloors/2) + spreaderFloors/2;
    			columns.add(new TowerColumn(spreaderX + 1, spreaderZ, newFloors));
    			spreadColumns(spreaderX + 1, spreaderZ, newFloors);
    		}
        		
    	}
    }
    
    private void buildRoom(World world, int locX, int locY, int locZ) {
    	
    	// north wall
        fillArea(world, locX, locY, locZ, ROOM_WIDTH, ROOM_HEIGHT, 1, "wall");
        
        // west wall
        fillArea(world, locX, locY, locZ, 1, ROOM_HEIGHT, ROOM_WIDTH, "wall");
        
        // south wall
        fillArea(world, locX, locY, locZ + ROOM_WIDTH, ROOM_WIDTH + 1, ROOM_HEIGHT, 1, "wall");
        
        // east wall
        fillArea(world, locX + ROOM_WIDTH, locY, locZ, 1, ROOM_HEIGHT, ROOM_WIDTH, "wall");
        
        // floor
        fillArea(world, locX + 1, locY, locZ + 1, ROOM_WIDTH - 1, 1, ROOM_WIDTH - 1, "floor");
        
        // inside
        fillArea(world, locX + 1, locY + 1, locZ + 1, ROOM_WIDTH - 1, ROOM_HEIGHT - 1, ROOM_WIDTH - 1, "air");
        
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
    
    private int getColumnFloorCount(int OffsetX, int OffsetZ) {
    	
    	for (TowerColumn loc : columns) {
    		if (loc.offsetX == OffsetX && loc.offsetZ == OffsetZ) {
    			return loc.floors;
    		}
    	}
    	return 0;
    	
    }
    
    // ----------------------------------------------------------------------------------------------------------------\\
  	//    PRIVATE CLASS - ROOMLOC
  	// ----------------------------------------------------------------------------------------------------------------//
    
    private class TowerColumn {
    	
    	public int offsetX;
    	public int offsetZ;
    	public int floors;
    	
    	public TowerColumn(int OffsetX, int OffsetZ, int Floors) {
    		
    		offsetX = OffsetX;
    		offsetZ = OffsetZ;
    		floors = Floors;
    		
    	}
    	
    }
    
}
