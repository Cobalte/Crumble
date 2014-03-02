package io.github.cobalte;

import java.util.Random;
import org.bukkit.Material;

public class CrumbleLocale {

	public static final int MAX_BUILD_HEIGHT = 40;
	
	private int chunkWidth;
	private int chunkLength;
	private int originChunkX;
	private int originChunkZ;
	private Material[][][] contents;
	private static Random rand;
	
	public int getWidth() {
		return chunkWidth;
	}
	
	public int getLength() {
		return chunkLength;
	}
	
	public int getOriginX() {
		return originChunkX;
	}
	
	public int getOriginZ() {
		return originChunkZ;
	}
	
	private void populate() {
		
		System.out.println("[CRUMBLE] Populating locale at " + String.valueOf(originChunkX) + "," + String.valueOf(originChunkZ));
		
		Material[] matList = new Material[20];
		matList[0] = Material.COBBLESTONE;
		matList[1] = Material.SMOOTH_BRICK;
		matList[2] = Material.BRICK;
		matList[3] = Material.BOOKSHELF;
		matList[4] = Material.CLAY;
		matList[5] = Material.COAL_ORE;
		matList[6] = Material.IRON_ORE;
		matList[7] = Material.DIAMOND_ORE;
		matList[8] = Material.EMERALD_ORE;
		matList[9] = Material.REDSTONE_ORE;
		matList[10] = Material.COAL_BLOCK;
		matList[11] = Material.IRON_BLOCK;
		matList[12] = Material.DIAMOND_BLOCK;
		matList[13] = Material.EMERALD_BLOCK;
		matList[14] = Material.REDSTONE_BLOCK;
		matList[15] = Material.LAPIS_ORE;
		matList[16] = Material.LAPIS_BLOCK;
		matList[17] = Material.WOOD;
		matList[18] = Material.GLOWSTONE;
		matList[19] = Material.GRAVEL;
		
		// just cover the ground with a 1-think layer of a random material for now
		for (int x = 1; x < chunkWidth * 16 - 2; x++) {
			for (int z = 1; z < chunkLength * 16 - 2; z++) {
				contents[x][0][z] = matList[rand.nextInt(matList.length - 1)];
			}
		}
	}
	
	// returns the build for the passed coordinates WITHIN THIS LOCALE
	public CrumbleBuild getBuild(int chunkLocX, int chunkLocZ) {
		CrumbleBuild build = new CrumbleBuild();
		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				for (int y = 0; y < MAX_BUILD_HEIGHT; y++) {
					build.contents[x][y][z] = this.contents[chunkLocX * 16 + x][y][chunkLocZ * 16 + z];
				}
			}
		}
		return build;
	}
	
	// constructor - initialize everything
	public CrumbleLocale(int originX, int originZ, int width, int length) {
		chunkWidth = width;
		chunkLength = length;
		originChunkX = originX;
		originChunkZ = originZ;
		contents = new Material[chunkWidth * 16][MAX_BUILD_HEIGHT][chunkLength * 16];
		rand = new Random();
		populate();
	}
	
}
