package io.github.cobalte;

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

public class CrumbleChunkGenerator extends ChunkGenerator {

	private static final int GROUND_LEVEL = 40;
	
	private NoiseGenerator generator;

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

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
            	
            	// bedrock layer
            	for (int y = 0; y <= 1; y++) {
            		result[(x * 16 + z) * 128 + y] = (byte)Material.BEDROCK.getId();
            	}
            	
            	// stone layer
            	for (int y = 2; y <= GROUND_LEVEL - 3; y++) {
            		result[(x * 16 + z) * 128 + y] = (byte)Material.STONE.getId();
            	}
            	
            	// dirt layer
            	for (int y = GROUND_LEVEL - 3; y <= GROUND_LEVEL - 1; y++) {
            		result[(x * 16 + z) * 128 + y] = (byte)Material.DIRT.getId();
            	}
            	
            	// grass layer
            	result[(x * 16 + z) * 128 + GROUND_LEVEL] = (byte)Material.GRASS.getId();
            	
            	// old generation left over from moon tutorial
            	/*int height = getHeight(world, cx + x * 0.0625, cz + z * 0.0625, 2) + base_height;
                for (int y = 0; y < height; y++) {
                    result[(x * 16 + z) * 128 + y] = (byte)Material.DIRT.getId();
                }
                result[(x * 16 + z) * 128 + height] = (byte)Material.GRASS.getId();*/
            }
        }

        return result;
    }

    @Override
    public List<BlockPopulator> getDefaultPopulators(World world) {
        return Arrays.asList((BlockPopulator)new CrumbleStructurePopulator());
    }

    @Override
    public Location getFixedSpawnLocation(World world, Random random) {
        int x = random.nextInt(200) - 100;
        int z = random.nextInt(200) - 100;
        int y = world.getHighestBlockYAt(x, z);
        return new Location(world, x, y, z);
    }
}

