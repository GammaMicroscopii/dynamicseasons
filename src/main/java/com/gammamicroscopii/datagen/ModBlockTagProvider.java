package com.gammamicroscopii.datagen;

import com.gammamicroscopii.block.ModBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends FabricTagProvider.BlockTagProvider{

	public ModBlockTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
		super(output, registriesFuture);
	}

	@Override
	protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
		getOrCreateTagBuilder(BlockTags.COMBINATION_STEP_SOUND_BLOCKS).add(ModBlocks.FROST).add(ModBlocks.FALLEN_LEAVES);
		getOrCreateTagBuilder(BlockTags.REPLACEABLE).add(ModBlocks.DEW).add(ModBlocks.FROST).add(ModBlocks.FALLEN_LEAVES);
		getOrCreateTagBuilder(BlockTags.LEAVES)
				.add(ModBlocks.DISCOLORED_BIRCH_LEAVES).add(ModBlocks.DISCOLORED_OAK_LEAVES).add(ModBlocks.DISCOLORED_CHERRY_LEAVES).add(ModBlocks.DISCOLORED_DARK_OAK_LEAVES)
				.add(ModBlocks.UNSTABLE_BIRCH_LEAVES).add(ModBlocks.UNSTABLE_OAK_LEAVES).add(ModBlocks.UNSTABLE_CHERRY_LEAVES).add(ModBlocks.UNSTABLE_DARK_OAK_LEAVES)
				.add(ModBlocks.FALLING_BIRCH_LEAVES).add(ModBlocks.FALLING_OAK_LEAVES).add(ModBlocks.FALLING_CHERRY_LEAVES).add(ModBlocks.FALLING_DARK_OAK_LEAVES)
				.add(ModBlocks.BIRCH_BRANCHES).add(ModBlocks.OAK_BRANCHES).add(ModBlocks.CHERRY_BRANCHES).add(ModBlocks.DARK_OAK_BRANCHES)
				.add(ModBlocks.LEAFING_OUT_BIRCH_BRANCHES).add(ModBlocks.LEAFING_OUT_OAK_BRANCHES).add(ModBlocks.BLOSSOMING_CHERRY_BRANCHES).add(ModBlocks.LEAFING_OUT_DARK_OAK_BRANCHES)
				.add(ModBlocks.FLOWERLESS_CHERRY_LEAVES);
		getOrCreateTagBuilder(BlockTags.HOE_MINEABLE)
				.add(ModBlocks.DISCOLORED_BIRCH_LEAVES).add(ModBlocks.DISCOLORED_OAK_LEAVES).add(ModBlocks.DISCOLORED_CHERRY_LEAVES).add(ModBlocks.DISCOLORED_DARK_OAK_LEAVES)
				.add(ModBlocks.UNSTABLE_BIRCH_LEAVES).add(ModBlocks.UNSTABLE_OAK_LEAVES).add(ModBlocks.UNSTABLE_CHERRY_LEAVES).add(ModBlocks.UNSTABLE_DARK_OAK_LEAVES)
				.add(ModBlocks.FALLING_BIRCH_LEAVES).add(ModBlocks.FALLING_OAK_LEAVES).add(ModBlocks.FALLING_CHERRY_LEAVES).add(ModBlocks.FALLING_DARK_OAK_LEAVES)
				.add(ModBlocks.BIRCH_BRANCHES).add(ModBlocks.OAK_BRANCHES).add(ModBlocks.CHERRY_BRANCHES).add(ModBlocks.DARK_OAK_BRANCHES)
				.add(ModBlocks.LEAFING_OUT_BIRCH_BRANCHES).add(ModBlocks.LEAFING_OUT_OAK_BRANCHES).add(ModBlocks.BLOSSOMING_CHERRY_BRANCHES).add(ModBlocks.LEAFING_OUT_DARK_OAK_BRANCHES)
				.add(ModBlocks.FLOWERLESS_CHERRY_LEAVES);
	}
}
