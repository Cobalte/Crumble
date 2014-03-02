package io.github.cobalte;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.generator.BlockPopulator;

public class CrumbleStructurePopulator extends BlockPopulator {

	private static final int GROUND_LEVEL = 40;
	
	public void populate(World world, Random random, Chunk chunk) {
		
		//get build mats from the crumble locale manager
		CrumbleBuild build = CrumbleLocaleManager.getBuild(chunk.getX(), chunk.getZ());
		
		// debug
		int n = 0;
		
		// import the build materials to this chunk
		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				for (int y = 0; y < CrumbleLocale.MAX_BUILD_HEIGHT; y++) {
					
					Material thisMat = build.getMaterial(x, y, z);
					if (thisMat != null) {
						int absX = (chunk.getX() << 4) + x;
						int absZ = (chunk.getZ() << 4) + z;
						world.getBlockAt(absX, y + GROUND_LEVEL, absZ).setType(thisMat);
					}
					
					if (thisMat == Material.SMOOTH_BRICK) {
						n += 1;
					}
				}
			}
		}
	}
}
