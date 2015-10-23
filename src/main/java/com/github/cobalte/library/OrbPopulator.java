
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

public class OrbPopulator extends BlockPopulator {

    public void populate(World world, Random random, Chunk source) {
        
    	final int ORB_CHANCE = 10;
    	
    	if (random.nextInt(100) < ORB_CHANCE ) {
    	
    		int centerX = (source.getX() << 4) + random.nextInt(16);
            int centerZ = (source.getZ() << 4) + random.nextInt(16);
            int centerY = world.getHighestBlockYAt(centerX, centerZ) + random.nextInt(20) + 20;
            
            world.getBlockAt(centerX, centerY, centerZ).setType(Material.REDSTONE_BLOCK);
            world.getBlockAt(centerX + 1, centerY, centerZ).setType(Material.REDSTONE_LAMP_ON);
            world.getBlockAt(centerX - 1, centerY, centerZ).setType(Material.REDSTONE_LAMP_ON);
            world.getBlockAt(centerX, centerY, centerZ + 1).setType(Material.REDSTONE_LAMP_ON);
            world.getBlockAt(centerX, centerY, centerZ - 1).setType(Material.REDSTONE_LAMP_ON);
            world.getBlockAt(centerX, centerY + 1, centerZ).setType(Material.REDSTONE_LAMP_ON);
            world.getBlockAt(centerX, centerY - 1, centerZ).setType(Material.REDSTONE_LAMP_ON);
            
    	}
    	
    }
}
