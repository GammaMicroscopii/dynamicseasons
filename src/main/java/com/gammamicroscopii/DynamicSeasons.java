package com.gammamicroscopii;

import com.gammamicroscopii.block.ModBlocks;
import com.gammamicroscopii.item.ModItems;
import com.gammamicroscopii.particles.ModParticles;
import com.gammamicroscopii.sounds.ModSounds;
import com.sun.jna.platform.win32.Winsvc;
import net.fabricmc.api.ModInitializer;

import net.minecraft.world.biome.BiomeEffects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DynamicSeasons implements ModInitializer {

	public static final String MOD_ID = "dynamicseasons";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final int YEAR_DURATION = 24000*96;

	@Override
	public void onInitialize() {

		LOGGER.info("Hello Fabric world!");

		ModItems.registerModItems();
		ModBlocks.registerModBlocks();
		ModSounds.registerModSounds();
		ModParticles.registerModParticles();
	}

}