package com.github.cobalte.crumble;

import java.util.Random;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.generator.BlockPopulator;

public class SurfaceClutterPopulator extends BlockPopulator {

    // ----------------------------------------------------------------------------------------------------------------\\
    //    CONSTANTS & PRIVATE VARS
    // ----------------------------------------------------------------------------------------------------------------//
    
    private final int MIN_CLUTTER_PER_CHUNK = 4;
    private final int MAX_CLUTTER_PER_CHUNK = 12;
    
    // ----------------------------------------------------------------------------------------------------------------\\
    //    POPULATOR
    // ----------------------------------------------------------------------------------------------------------------//
    
    public void populate(World world, Random random, Chunk source) {
        placeLongGrass(world, source);
    }
    
    // ----------------------------------------------------------------------------------------------------------------\\
    //    BLOCK PLACEMENT
    // ----------------------------------------------------------------------------------------------------------------//
    
    private void placeLongGrass(World world, Chunk source) {
        int clutterCount = Crumble.rand.nextInt(MAX_CLUTTER_PER_CHUNK - MIN_CLUTTER_PER_CHUNK + 1) + MIN_CLUTTER_PER_CHUNK;
        
        for (int n = 0; n < clutterCount; n++) {
            int localX = (source.getX() << 4) + Crumble.rand.nextInt(16);
            int localZ = (source.getZ() << 4) + Crumble.rand.nextInt(16);
            int localY = world.getHighestBlockYAt(localX, localZ);
            
            if (world.getBlockAt(localX, localY, localZ).getType() == Material.GRASS) {
                Block block = world.getBlockAt(localX, localY, localZ); 
                block.setType(Material.LONG_GRASS);
                block.setData((byte) 0x1); // tall grass
            }
        }
    }
    
}
