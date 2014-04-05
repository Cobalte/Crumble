
package com.github.cobalte.library;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import org.bukkit.Location;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.util.noise.NoiseGenerator;
import org.bukkit.util.noise.SimplexNoiseGenerator;

public class LibraryChunkGenerator extends ChunkGenerator {
    
    // ----------------------------------------------------------------------------------------------------------------\\
  	//    PRIVATE VARS
  	// ----------------------------------------------------------------------------------------------------------------//
	
	private NoiseGenerator generator;

    // ----------------------------------------------------------------------------------------------------------------\\
  	//    REQUIRED THINGS
  	// ----------------------------------------------------------------------------------------------------------------//
	
    private NoiseGenerator getGenerator(World world) {
        if (generator == null) {
            generator = new SimplexNoiseGenerator(world);
        }

        return generator;
    }

    private int getHeight(World world, double x, double y, int variance) {
        NoiseGenerator gen = getGenerator(world);

        double result = gen.noise(x, y);
        result *= variance;
        return NoiseGenerator.floor(result);
    }
    
    public byte[] generate(World world, Random random, int cx, int cz) {
        
    	byte[] result = new byte[32768];
        int height = getHeight(world, cx * 0.0625, cz * 0.0625, 2) + 60;
        
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int y = 0; y < height; y++) {
                    result[(x * 16 + z) * 128 + y] = (byte)Material.SMOOTH_BRICK.getId();
                }
            }
        }
        

        return result;
    }

    @Override
    public List<BlockPopulator> getDefaultPopulators(World world) {
        return Arrays.asList((BlockPopulator)new RoomPopulator(), new StackPopulator(), new OrbPopulator());
    }

    @Override
    public Location getFixedSpawnLocation(World world, Random random) {
        int x = random.nextInt(200) - 100;
        int z = random.nextInt(200) - 100;
        int y = world.getHighestBlockYAt(x, z);
        return new Location(world, x, y, z);
    }
}
