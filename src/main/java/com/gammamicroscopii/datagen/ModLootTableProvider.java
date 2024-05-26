package com.gammamicroscopii.datagen;

import com.gammamicroscopii.block.ModBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class ModLootTableProvider extends FabricBlockLootTableProvider {
	
	protected ModLootTableProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
		super(dataOutput, registryLookup);
	}

	@Override
	public void generate() {
		addDrop(ModBlocks.DISCOLORED_BIRCH_LEAVES);
		addDrop(ModBlocks.DISCOLORED_CHERRY_LEAVES);
		addDrop(ModBlocks.DISCOLORED_DARK_OAK_LEAVES);
		addDrop(ModBlocks.DISCOLORED_OAK_LEAVES);
		addDrop(ModBlocks.UNSTABLE_BIRCH_LEAVES);
		addDrop(ModBlocks.UNSTABLE_CHERRY_LEAVES);
		addDrop(ModBlocks.UNSTABLE_DARK_OAK_LEAVES);
		addDrop(ModBlocks.UNSTABLE_OAK_LEAVES);
		addDrop(ModBlocks.FALLING_BIRCH_LEAVES);
		addDrop(ModBlocks.FALLING_CHERRY_LEAVES);
		addDrop(ModBlocks.FALLING_DARK_OAK_LEAVES);
		addDrop(ModBlocks.FALLING_OAK_LEAVES);
		addDrop(ModBlocks.BIRCH_BRANCHES);
		addDrop(ModBlocks.CHERRY_BRANCHES);
		addDrop(ModBlocks.DARK_OAK_BRANCHES);
		addDrop(ModBlocks.OAK_BRANCHES);
		addDrop(ModBlocks.LEAFING_OUT_BIRCH_BRANCHES);
		addDrop(ModBlocks.BLOSSOMING_CHERRY_BRANCHES);
		addDrop(ModBlocks.LEAFING_OUT_DARK_OAK_BRANCHES);
		addDrop(ModBlocks.LEAFING_OUT_OAK_BRANCHES);
		addDrop(ModBlocks.FLOWERLESS_CHERRY_LEAVES);
		addDrop(ModBlocks.DEW);
		addDrop(ModBlocks.FROST);
		addDrop(ModBlocks.FALLEN_LEAVES);
	}
}
