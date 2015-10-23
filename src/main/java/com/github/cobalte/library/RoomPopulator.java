
package com.github.cobalte.library;

import java.util.Random;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.generator.BlockPopulator;

public class RoomPopulator extends BlockPopulator {

    public void populate(World world, Random random, Chunk source) {
        final int ROOM_CHANCE = 2;
        if (random.nextInt(100) < ROOM_CHANCE) {
        	
        	int startX = (source.getX() << 4) + random.nextInt(16);
            int startZ = (source.getZ() << 4) + random.nextInt(16);
            int roomLevel = world.getHighestBlockAt(startX, startZ).getY();
            int roomSizeX = 20;
            int roomSizeY = 6;
            int roomSizeZ = 20;
            
            // north wall
            fillArea(world, startX, roomLevel, startZ, roomSizeX, roomSizeY, 1, Material.SMOOTH_BRICK);
            
            // west wall
            fillArea(world, startX, roomLevel, startZ, 1, roomSizeY, roomSizeZ, Material.SMOOTH_BRICK);
            
            // south wall
            fillArea(world, startX, roomLevel, startZ + roomSizeZ, roomSizeX + 1, roomSizeY, 1, Material.SMOOTH_BRICK);
            
            // east wall
            fillArea(world, startX + roomSizeX, roomLevel, startZ, 1, roomSizeY, roomSizeZ, Material.SMOOTH_BRICK);
            
            // floor
            fillArea(world, startX, roomLevel, startZ, roomSizeX + 1, 1, roomSizeZ + 1, Material.SMOOTH_BRICK);
            
            //ceiling
            fillArea(world, startX, roomLevel + roomSizeY, startZ, roomSizeX + 1, 1, roomSizeZ + 1, Material.SMOOTH_BRICK);
            
            // door
            fillArea(world, startX + 3, roomLevel + 1, startZ, 2, 2, 1, Material.AIR);
            
            // inside
            fillArea(world, startX + 1, roomLevel + 1, startZ + 1, roomSizeX - 1, roomSizeY - 1, roomSizeZ - 1, Material.AIR);
            
        }
    	
    }
    
    private void fillArea(World world, int locX, int locY, int locZ, int sizeX, int sizeY, int sizeZ, Material mat) {
		
		for (int x = locX; x < locX + sizeX; x++) {
			for (int z = locZ; z < locZ + sizeZ; z++) {
				for (int y = locY; y < locY + sizeY; y++) {
					world.getBlockAt(x, y, z).setType(mat);
				}
			}
		}
		
	}
    
}
