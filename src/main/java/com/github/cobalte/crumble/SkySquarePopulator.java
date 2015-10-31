package com.github.cobalte.crumble;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;

public class SkySquarePopulator extends BlockPopulator {

    // ----------------------------------------------------------------------------------------------------------------\\
    //    CONSTANTS & PRIVATE VARS
    // ----------------------------------------------------------------------------------------------------------------//
    
    private final float POPULATION_CHANCE_PER_CHUNK = (float)0.005;
    
    private final int MIN_START_Y = 140;
    private final int MAX_START_Y = 200;
    
    // ----------------------------------------------------------------------------------------------------------------\\
    //    POPULATOR
    // ----------------------------------------------------------------------------------------------------------------//
    
    public void populate(World world, Random random, Chunk source) {
        if (Crumble.rand.nextFloat() < POPULATION_CHANCE_PER_CHUNK) {
            long startTime = System.currentTimeMillis();
            int startX = (source.getX() << 4) + Crumble.rand.nextInt(16);
            int startZ = (source.getZ() << 4) + Crumble.rand.nextInt(16);
            int startY = MIN_START_Y + Crumble.rand.nextInt(MAX_START_Y - MIN_START_Y);
            
            int squareSize = 20;
            fillArea(world, startX, startY, startZ, squareSize, 2, squareSize, Material.SMOOTH_BRICK);
            fillArea(world, startX+1, startY+1, startZ+1, squareSize-2, 1, squareSize-2, Material.GRASS);
            
             // all done!
            Crumble.log(String.format("Built a sky square in chunk %d, %d at height %d (took %.0f ms).",
                source.getX(),
                source.getZ(),
                startY,
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
    
    private void fillArea(World world, int locX, int locY, int locZ, int sizeX, int sizeY, int sizeZ, Material material) {
        for (int x = locX; x < locX + sizeX; x++) {
            for (int z = locZ; z < locZ + sizeZ; z++) {
                for (int y = locY; y < locY + sizeY; y++) {
                    world.getBlockAt(x, y, z).setType(material);
                }
            }
        }
    }
    
}
