package com.gammamicroscopii;

import com.gammamicroscopii.block.ModBlocks;
import com.gammamicroscopii.colors.DSColorHelper;
import com.gammamicroscopii.item.ModItems;
import com.gammamicroscopii.network.DSNetworking;
import com.gammamicroscopii.particle.ModParticles;
import com.gammamicroscopii.resourceload.data.BiomeClimates;
import com.gammamicroscopii.resourceload.data.SeasonalBlockCycles;
import com.gammamicroscopii.sounds.ModSounds;
import com.gammamicroscopii.world.ServerWorldTick;
import com.sun.jna.platform.win32.Winsvc;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resource.ResourceType;
import net.minecraft.world.biome.BiomeEffects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;

public class DynamicSeasons implements ModInitializer {

	public static final String MOD_ID = "dynamicseasons";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final int DAYLIGHT_CYCLE_DURATION = 24000;
	public static final int YEAR_DURATION = DAYLIGHT_CYCLE_DURATION*96;

	@Override
	public void onInitialize() {
		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new BiomeClimates());
		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new SeasonalBlockCycles());

		ModItems.registerModItems();
		ModBlocks.registerModBlocks();
		ModSounds.registerModSounds();
		ModParticles.registerModParticles();

		ServerWorldTick.init();

		DSNetworking.registerModPackets();

		/*Color c;
		for (float temp = -0.2f; temp <= 1.2f; temp+=0.01f) {
			c = new Color(DSColorHelper.getDefaultWaterColor(temp));
			LOGGER.info("temp = "+temp+"; waterColor = { r = "+c.getRed()+"; g = "+c.getGreen()+"; b = "+c.getBlue()+" }");
		}*/

	}

}