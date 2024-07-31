package com.gammamicroscopii.client_init;

import com.gammamicroscopii.colors.ClientColorHelper;
import com.gammamicroscopii.resourceload.assets.ModdedBiomeColors;
import com.gammamicroscopii.network.DSNetworking;
import com.gammamicroscopii.world.ClientWorldTick;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resource.ResourceType;

@Environment(EnvType.CLIENT)
public class DynamicSeasonsClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new ModdedBiomeColors());

		ModItemModelPredicateProvider.registerPredicates();
		TransparentModBlocks.registerTransparency();
		ClientColorHelper.registerBlockColors();
		ClientModParticles.registerClientParticles();

		ClientWorldTick.init();

		DSNetworking.registerClientReceivers();
	}
}