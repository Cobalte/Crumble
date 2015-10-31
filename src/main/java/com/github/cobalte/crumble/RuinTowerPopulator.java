package com.github.cobalte.crumble;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.generator.BlockPopulator;

public class RuinTowerPopulator extends BlockPopulator {

    // ----------------------------------------------------------------------------------------------------------------\\
  	//    CONSTANTS & PRIVATE VARS
  	// ----------------------------------------------------------------------------------------------------------------//
	
    private final float POPULATION_CHANCE_PER_CHUNK = (float)0.0010;
    
	private final int ROOM_HEIGHT = 5;
	private final int ROOM_WIDTH = 12;
	private final int MIN_FLOORS = 14;
	private final int MAX_FLOORS = 28;
	private final int LOWEST_TOWER_Y = 80;
	
	private final float STAIR_CHANCE_PER_ROOM = (float)0.33;
	private final int STAIR_WIDTH = 2;
	private final int STAIR_OFFSET_FROM_WALL = 3;
	
	private final float HOLE_CRUMBLE_CHANCE_PER_BLOCK = (float)0.008;
	private final float HOLE_CRUMBLE_SPREAD_CHANCE_PER_RECURSE = (float)0.2;
	private final float CONE_CRUMBLE_SPREAD_CHANCE = (float)0.2;
	private final float RUBBLE_CHANCE_PER_BLOCK = (float)0.1;
	
	private final List<Material> TOWER_FLOOR_BRUSH = Arrays.asList(
        Material.STONE,
        Material.STONE,
        Material.COBBLESTONE);
	
	private final List<Material> TOWER_WALL_BRUSH = Arrays.asList(
        Material.SMOOTH_BRICK,
        Material.SMOOTH_BRICK,
        Material.SMOOTH_BRICK,
        Material.COBBLESTONE);
	
	private final List<Material> AIR_BRUSH = Arrays.asList(Material.AIR);
	
	private ArrayList<TowerColumn> columns;
	
    // ----------------------------------------------------------------------------------------------------------------\\
  	//    POPULATOR
  	// ----------------------------------------------------------------------------------------------------------------//
	
    public void populate(World world, Random random, Chunk source) {
    	if (random.nextFloat() < POPULATION_CHANCE_PER_CHUNK) {
    	    long startTime = System.currentTimeMillis();
    	    int startX = (source.getX() << 4) + random.nextInt(16);
            int startZ = (source.getZ() << 4) + random.nextInt(16);
            
            // generate tower columns
            int centerColumnFloorCount =  Crumble.rand.nextInt(MAX_FLOORS - MIN_FLOORS) + MIN_FLOORS;
            columns = new ArrayList<TowerColumn>();
            columns.add(new TowerColumn(0, 0, centerColumnFloorCount));
            spreadColumns(0, 0, centerColumnFloorCount);
    		
            // build each column
            for (TowerColumn col : columns) {
            	for (int f = 0; f < col.floors; f++) {
            		buildRoom(world, col.offsetX * ROOM_WIDTH + startX, f * ROOM_HEIGHT + LOWEST_TOWER_Y, col.offsetZ * ROOM_WIDTH + startZ);
            	}
            	
            	// add a roof to this column
            	fillArea(world, col.offsetX * ROOM_WIDTH + startX, col.floors * ROOM_HEIGHT + LOWEST_TOWER_Y, col.offsetZ * ROOM_WIDTH + startZ, ROOM_WIDTH + 1, 1, ROOM_WIDTH + 1, TOWER_WALL_BRUSH);
            }
            
            // build stairs up the central column
            for (int f = 0; f < columns.get(0).floors; f++) {
                if (Crumble.rand.nextFloat() < STAIR_CHANCE_PER_ROOM) {                    
                    addStairsToRoom(world, columns.get(0).offsetX * ROOM_WIDTH + startX, f * ROOM_HEIGHT + LOWEST_TOWER_Y, columns.get(0).offsetZ * ROOM_WIDTH + startZ);
                }
            }
            
            // build stairs randomly in other columns
            for (TowerColumn col : columns.subList(1, columns.size())) {
                for (int f = 0; f < col.floors; f++) {
                    if (Crumble.rand.nextFloat() < STAIR_CHANCE_PER_ROOM) {
                        addStairsToRoom(world, columns.get(0).offsetX * ROOM_WIDTH + startX, f * ROOM_HEIGHT + LOWEST_TOWER_Y, columns.get(0).offsetZ * ROOM_WIDTH + startZ);
                    }
                }
            }
            
            // crumble the tower!
            addHoles(world, startX, startZ);
            addConeDecay(world, startX, startZ);
            
            // all done!
            Crumble.log(String.format("Built a ruined tower in chunk %d, %d (took %.0f ms).",
                source.getX(),
                source.getZ(),
                (float) (System.currentTimeMillis() - startTime)));
    	}
    }
    
    // ----------------------------------------------------------------------------------------------------------------\\
    //    UTILITY
    // ----------------------------------------------------------------------------------------------------------------//
    
    private void fillArea(World world, int locX, int locY, int locZ, int sizeX, int sizeY, int sizeZ, List<Material> brush) {
        for (int x = locX; x < locX + sizeX; x++) {
            for (int z = locZ; z < locZ + sizeZ; z++) {
                for (int y = locY; y < locY + sizeY; y++) {
                    world.getBlockAt(x, y, z).setType(brush.get(Crumble.rand.nextInt(brush.size())));
                }
            }
        }
    }
    
    private int getFloorCountOfColumnAtOffset(int OffsetX, int OffsetZ) {
        for (TowerColumn col : columns) {
            if (col.offsetX == OffsetX && col.offsetZ == OffsetZ) {
                return col.floors;
            }
        }
        return 0;
    }
    
    // ----------------------------------------------------------------------------------------------------------------\\
  	//    TOWER CONSTRUCTION
  	// ----------------------------------------------------------------------------------------------------------------//
    
    private void spreadColumns(int spreaderX, int spreaderZ, int spreaderFloors) {    	
    	if (spreaderFloors > 4) { // we don't want to keep spreading if we're down to 4 floors in a column
			
    	    // spread north
    		if (getFloorCountOfColumnAtOffset(spreaderX, spreaderZ - 1) == 0) {
    			int newFloors = Crumble.rand.nextInt(spreaderFloors/2) + spreaderFloors/2;
    			columns.add(new TowerColumn(spreaderX, spreaderZ - 1, newFloors));
    			spreadColumns(spreaderX, spreaderZ - 1, newFloors);
    		}

    		// spread south
    		if (getFloorCountOfColumnAtOffset(spreaderX, spreaderZ + 1) == 0) {
    			int newFloors = Crumble.rand.nextInt(spreaderFloors/2) + spreaderFloors/2;
    			columns.add(new TowerColumn(spreaderX, spreaderZ + 1, newFloors));
    			spreadColumns(spreaderX, spreaderZ + 1, newFloors);
    		}
    		
    		// spread west
    		if (getFloorCountOfColumnAtOffset(spreaderX - 1, spreaderZ) == 0) {
    			int newFloors = Crumble.rand.nextInt(spreaderFloors/2) + spreaderFloors/2;
    			columns.add(new TowerColumn(spreaderX - 1, spreaderZ, newFloors));
    			spreadColumns(spreaderX - 1, spreaderZ, newFloors);
    		}
    		
    		// spread east
    		if (getFloorCountOfColumnAtOffset(spreaderX + 1, spreaderZ) == 0) {
    			int newFloors = Crumble.rand.nextInt(spreaderFloors/2) + spreaderFloors/2;
    			columns.add(new TowerColumn(spreaderX + 1, spreaderZ, newFloors));
    			spreadColumns(spreaderX + 1, spreaderZ, newFloors);
    		}
    	}
    }
    
    private void buildRoom(World world, int locX, int locY, int locZ) {
        fillArea(world, locX, locY, locZ, ROOM_WIDTH, ROOM_HEIGHT, 1, TOWER_WALL_BRUSH); // north wall
        fillArea(world, locX, locY, locZ, 1, ROOM_HEIGHT, ROOM_WIDTH, TOWER_WALL_BRUSH); // west wall
        fillArea(world, locX, locY, locZ + ROOM_WIDTH, ROOM_WIDTH + 1, ROOM_HEIGHT, 1, TOWER_WALL_BRUSH); // south wall
        fillArea(world, locX + ROOM_WIDTH, locY, locZ, 1, ROOM_HEIGHT, ROOM_WIDTH, TOWER_WALL_BRUSH); // east wall
        fillArea(world, locX + 1, locY, locZ + 1, ROOM_WIDTH - 1, 1, ROOM_WIDTH - 1, TOWER_FLOOR_BRUSH); // floor
        fillArea(world, locX + 1, locY + 1, locZ + 1, ROOM_WIDTH - 1, ROOM_HEIGHT - 1, ROOM_WIDTH - 1, AIR_BRUSH); // inside
        
        // rubble on the floor
        for (int localX = 1; localX < ROOM_WIDTH - 1; localX++) {
            for (int localZ = 1; localZ < ROOM_WIDTH - 1; localZ++) {
                if (Crumble.rand.nextFloat() < RUBBLE_CHANCE_PER_BLOCK) {
                    Block block = world.getBlockAt(locX + localX, locY + 1, locZ + localZ);
                    
                    if (Crumble.rand.nextFloat() < (float)0.25) {
                        block.setType(Material.COBBLESTONE);
                    } else {
                        block.setType(Material.STEP);
                        block.setData((byte) 0x3);
                    }
                }
            }
        }
    }
    
    private void addStairsToRoom(World world, int roomLocX, int roomLocY, int roomLocZ) {
        // air over stairs
        for (int localX = 0; localX < ROOM_HEIGHT - 1; localX++) {
            for (int localZ = 0; localZ < STAIR_WIDTH; localZ++) {
                world.getBlockAt(
                    roomLocX + localX + STAIR_OFFSET_FROM_WALL,
                    roomLocY + ROOM_HEIGHT,
                    roomLocZ + localZ + STAIR_OFFSET_FROM_WALL)
                    .setType(Material.AIR);
            }
        }
        
        // upward-facing stairs
        for (int localX = 0; localX < ROOM_HEIGHT; localX++) {
            for (int localZ = 0; localZ < STAIR_WIDTH; localZ++) {
                Block block = world.getBlockAt(
                    roomLocX + localX + STAIR_OFFSET_FROM_WALL,
                    roomLocY + localX + 1,
                    roomLocZ + localZ + STAIR_OFFSET_FROM_WALL); 
                
                block.setType(Material.COBBLESTONE_STAIRS);
                block.setData((byte) 0x0); // make stairs face east
            }
        }
        
     // downward-facing stairs
        for (int localX = 1; localX < ROOM_HEIGHT; localX++) {
            for (int localZ = 0; localZ < STAIR_WIDTH; localZ++) {
                Block block = world.getBlockAt(
                    roomLocX + localX + STAIR_OFFSET_FROM_WALL,
                    roomLocY + localX,
                    roomLocZ + localZ + STAIR_OFFSET_FROM_WALL); 
                
                block.setType(Material.COBBLESTONE_STAIRS);
                block.setData((byte) 0x5); // make stairs face west upside-down
            }
        }
    }
    
    // ----------------------------------------------------------------------------------------------------------------\\
    //    TOWER DESTRUCTION
    // ----------------------------------------------------------------------------------------------------------------//
    
    private void addHoles(World world, int startX, int startZ) {
        for (TowerColumn col : columns) {
            for (int f = 0; f < col.floors; f++) {
                for (int localX = 0; localX <= ROOM_WIDTH; localX++) {
                    for (int localZ = 0; localZ <= ROOM_WIDTH; localZ++) {
                        for (int localY = 0; localY <= ROOM_HEIGHT; localY++) {
                            if (Crumble.rand.nextFloat() < HOLE_CRUMBLE_CHANCE_PER_BLOCK) {
                                spreadHole(
                                    world,
                                    col.offsetX * ROOM_WIDTH + startX + localX,
                                    col.offsetZ * ROOM_WIDTH + startZ + localZ,
                                    f * ROOM_HEIGHT + LOWEST_TOWER_Y + localY,
                                    Crumble.rand.nextFloat());
                            }
                        }
                    }
                }
            }
        }
    }
    
    private void spreadHole(World world, int absoluteX, int absoluteZ, int absoluteY, float spreadChance) {
        world.getBlockAt(absoluteX, absoluteY, absoluteZ).setType(Material.AIR);
        
        if (Crumble.rand.nextFloat() < spreadChance) {
            spreadHole(world, absoluteX - 1, absoluteZ, absoluteY, spreadChance - HOLE_CRUMBLE_SPREAD_CHANCE_PER_RECURSE);
        }
        if (Crumble.rand.nextFloat() < spreadChance) {
            spreadHole(world, absoluteX + 1, absoluteZ, absoluteY, spreadChance - HOLE_CRUMBLE_SPREAD_CHANCE_PER_RECURSE);
        }
        if (Crumble.rand.nextFloat() < spreadChance) {
            spreadHole(world, absoluteX, absoluteZ - 1, absoluteY, spreadChance - HOLE_CRUMBLE_SPREAD_CHANCE_PER_RECURSE);
        }
        if (Crumble.rand.nextFloat() < spreadChance) {
            spreadHole(world, absoluteX, absoluteZ + 1, absoluteY, spreadChance - HOLE_CRUMBLE_SPREAD_CHANCE_PER_RECURSE);
        }
        if (Crumble.rand.nextFloat() < spreadChance) {
            spreadHole(world, absoluteX, absoluteZ, absoluteY - 1, spreadChance - HOLE_CRUMBLE_SPREAD_CHANCE_PER_RECURSE);
        }
        if (Crumble.rand.nextFloat() < spreadChance) {
            spreadHole(world, absoluteX, absoluteZ, absoluteY + 1, spreadChance - HOLE_CRUMBLE_SPREAD_CHANCE_PER_RECURSE);
        }
    }
    
    private void addConeDecay(World world, int startX, int startZ) {        
        int maxHeight = columns.get(0).floors * ROOM_HEIGHT + LOWEST_TOWER_Y + 2;
        
        for (TowerColumn col : columns) {
            int startFloor = Crumble.rand.nextInt(col.floors / 2) + (col.floors / 2) + 1;
            int localX = Crumble.rand.nextInt(ROOM_WIDTH - 2) + 1;
            int localY = Crumble.rand.nextInt(ROOM_HEIGHT - 2) + 1;
            int localZ = Crumble.rand.nextInt(ROOM_WIDTH - 2) + 1;
            
            spreadCone(
                world,
                localX + (col.offsetX * ROOM_WIDTH) + startX,
                localY + (startFloor * ROOM_HEIGHT) + LOWEST_TOWER_Y,
                localZ + (col.offsetZ * ROOM_WIDTH) + startZ,
                maxHeight);
        }
    }
   
    private void spreadCone(World world, int absoluteX, int absoluteY, int absoluteZ, int maxHeight) {
        if (absoluteY < maxHeight) {
            fillArea(world, absoluteX, absoluteY, absoluteZ, 1, maxHeight - absoluteY, 1, AIR_BRUSH);
            
            if (Crumble.rand.nextFloat() < CONE_CRUMBLE_SPREAD_CHANCE && absoluteY + 1 < world.getHighestBlockYAt(absoluteX + 1, absoluteZ)) {
                spreadCone(world, absoluteX + 1, absoluteY + 1, absoluteZ, maxHeight);
            }
            if (Crumble.rand.nextFloat() < CONE_CRUMBLE_SPREAD_CHANCE && absoluteY + 1 < world.getHighestBlockYAt(absoluteX, absoluteZ - 1)) {
                spreadCone(world, absoluteX - 1, absoluteY + 1, absoluteZ, maxHeight);
            }
            if (Crumble.rand.nextFloat() < CONE_CRUMBLE_SPREAD_CHANCE && absoluteY + 1 < world.getHighestBlockYAt(absoluteX, absoluteZ + 1)) {
                spreadCone(world, absoluteX, absoluteY + 1, absoluteZ + 1, maxHeight);
            }
            if (Crumble.rand.nextFloat() < CONE_CRUMBLE_SPREAD_CHANCE && absoluteY + 1 < world.getHighestBlockYAt(absoluteX, absoluteZ - 1)) {
                spreadCone(world, absoluteX, absoluteY + 1, absoluteZ - 1, maxHeight);
            }
            
            spreadCone(world, absoluteX, absoluteY + 1, absoluteZ, maxHeight);
        }
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
