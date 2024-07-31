package com.gammamicroscopii.resourceload.assets;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.client.util.RawTextureDataLoader;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

import static com.gammamicroscopii.DynamicSeasons.MOD_ID;

@Environment(EnvType.CLIENT)
public class ModdedBiomeColors implements SimpleSynchronousResourceReloadListener {

	private static final Identifier GRASS_COLORMAP_IDENTIFIER = new Identifier(MOD_ID, "textures/colormap/grass_extended.png");
	private static final Identifier FOLIAGE_COLORMAP_IDENTIFIER = new Identifier(MOD_ID, "textures/colormap/foliage_extended.png");

	private static int[] grassColorMap = new int[65536];
	private static int[] foliageColorMap = new int[65536];

	public static int getGrassColor(double temperature, double humidity) {
		humidity *= temperature;
		int i = (int)((1.0D - temperature) * 255.0D);
		int j = (int)((1.0D - humidity) * 255.0D);
		int k = j << 8 | i;
		return k > grassColorMap.length ? -65281 : grassColorMap[k];
	}

	public static int getFoliageColor(double temperature, double humidity) {
		humidity *= temperature;
		int i = (int)((1.0D - temperature) * 255.0D);
		int j = (int)((1.0D - humidity) * 255.0D);
		int k = j << 8 | i;
		return k > foliageColorMap.length ? -65281 : foliageColorMap[k];
	}

	@Override
	public Identifier getFabricId() {
		return new Identifier("modded_biome_colormaps");
	}

	@SuppressWarnings("deprecation")
	@Override
	public void reload(ResourceManager manager) {
		try {
			grassColorMap = RawTextureDataLoader.loadRawTextureData(manager, GRASS_COLORMAP_IDENTIFIER);
			foliageColorMap = RawTextureDataLoader.loadRawTextureData(manager, FOLIAGE_COLORMAP_IDENTIFIER);
		} catch (Exception e) {
			throw new IllegalStateException("Failed to load colormaps", e);
		}
	}

}