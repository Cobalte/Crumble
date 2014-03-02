package io.github.cobalte;

import org.bukkit.Material;

public class CrumbleBuild {

	public Material[][][] contents;
	
	// sets this build's contents to the same as the passed build's contents
	public void copyContents(CrumbleBuild build) {
		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				for (int y = 0; y < CrumbleLocale.MAX_BUILD_HEIGHT; y++) {
					contents[x][y][z] = build.getMaterial(x, y, z);
				}
			}
		}
	}
	
	// gets the material at a specific location
	public Material getMaterial(int X, int Y, int Z) {
		return contents[X][Y][Z];
	}
	
	// constructor - makes a new empty contents
	public CrumbleBuild() {
		contents = new Material[16][CrumbleLocale.MAX_BUILD_HEIGHT][16];
	}
	
}
