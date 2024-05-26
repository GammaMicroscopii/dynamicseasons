package com.gammamicroscopii;

import com.gammamicroscopii.block.ModBlocks;
import com.gammamicroscopii.colors.DSColorHelper;
import com.gammamicroscopii.colors.ModdedBiomeColors;
import com.gammamicroscopii.sounds.ModSounds;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.color.world.FoliageColors;
import net.minecraft.resource.ResourceType;

public class DynamicSeasonsClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new ModdedBiomeColors());

		ModItemModelPredicateProvider.registerPredicates();
		TransparentModBlocks.registerTransparency();
		DSColorHelper.registerBlockColors();
		ClientModParticles.registerClientParticles();
	}
}