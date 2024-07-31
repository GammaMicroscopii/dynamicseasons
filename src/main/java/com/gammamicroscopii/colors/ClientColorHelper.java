package com.gammamicroscopii.colors;

import com.gammamicroscopii.block.ModBlocks;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.color.world.FoliageColors;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;

import java.util.function.BiFunction;

@Environment(EnvType.CLIENT)
public class ClientColorHelper {

	public static int averageColors(int color1, int color2) {
		return 0xff000000 | (((color1 & 0xfefefe) >> 1) + ((color2 & 0xfefefe) >> 1));
	}

	public static int getBirchColor(BlockRenderView brv, BlockPos bp) {
		return averageColors(BiomeColors.getFoliageColor(brv, bp), 0xa7a07a);
	}

	public static int getSpruceColor(BlockRenderView brv, BlockPos bp) {
		return averageColors(BiomeColors.getFoliageColor(brv, bp), 0x5a8e5e);
	}

	public static void registerBlockColors() {
		registerAutumnalLeavesColor(ModBlocks.DISCOLORED_BIRCH_LEAVES, ModBlocks.Items.DISCOLORED_BIRCH_LEAVES, 0xdcff78/*0x9f9503*/, ClientColorHelper::getBirchColor, FoliageColors.getBirchColor());
		registerAutumnalLeavesColor(ModBlocks.DISCOLORED_CHERRY_LEAVES, ModBlocks.Items.DISCOLORED_CHERRY_LEAVES, 0xffb026/*0x956d08*/, BiomeColors::getFoliageColor, FoliageColors.getDefaultColor());
		registerAutumnalLeavesColor(ModBlocks.DISCOLORED_DARK_OAK_LEAVES, ModBlocks.Items.DISCOLORED_DARK_OAK_LEAVES, 0xd96814/*0x915c03*/, BiomeColors::getFoliageColor, FoliageColors.getDefaultColor());
		registerAutumnalLeavesColor(ModBlocks.DISCOLORED_OAK_LEAVES, ModBlocks.Items.DISCOLORED_OAK_LEAVES, 0xffa418/*0x9e8d03*/, BiomeColors::getFoliageColor, FoliageColors.getDefaultColor());
		registerSpringOrSummerLeavesColor(ModBlocks.LEAFING_OUT_BIRCH_BRANCHES, ModBlocks.Items.LEAFING_OUT_BIRCH_BRANCHES, ClientColorHelper::getBirchColor, FoliageColors.getDefaultColor());
		registerSpringOrSummerLeavesColor(ModBlocks.LEAFING_OUT_DARK_OAK_BRANCHES, ModBlocks.Items.LEAFING_OUT_DARK_OAK_BRANCHES, BiomeColors::getFoliageColor, FoliageColors.getDefaultColor());
		registerSpringOrSummerLeavesColor(ModBlocks.LEAFING_OUT_OAK_BRANCHES, ModBlocks.Items.LEAFING_OUT_OAK_BRANCHES, BiomeColors::getFoliageColor, FoliageColors.getDefaultColor());
		registerSpringOrSummerLeavesColor(ModBlocks.FLOWERLESS_CHERRY_LEAVES, ModBlocks.Items.FLOWERLESS_CHERRY_LEAVES, BiomeColors::getFoliageColor, FoliageColors.getDefaultColor());
		ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> world != null && pos != null ? getBirchColor(world, pos) : FoliageColors.getBirchColor(), Blocks.BIRCH_LEAVES);
		ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> world != null && pos != null ? getSpruceColor(world, pos) : FoliageColors.getSpruceColor(), Blocks.SPRUCE_LEAVES);
	}

	private static void registerAutumnalLeavesColor(Block blockForm, Item itemForm, int autumnalTextureColor, BiFunction<BlockRenderView, BlockPos, Integer> biomeTintedColorFunction, int defaultLeavesColor) {
		ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> world != null && pos != null ? ClientColorHelper.averageColors(autumnalTextureColor, biomeTintedColorFunction.apply(world, pos)) : ClientColorHelper.averageColors(autumnalTextureColor, defaultLeavesColor), blockForm);
		ColorProviderRegistry.ITEM.register((stack, tintIndex) -> ClientColorHelper.averageColors(autumnalTextureColor, defaultLeavesColor), itemForm);

	}

	private static void registerSpringOrSummerLeavesColor(Block blockForm, Item itemForm, BiFunction<BlockRenderView, BlockPos, Integer> biomeTintedColorFunction, int defaultLeavesColor) {
		ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> world != null && pos != null ?  biomeTintedColorFunction.apply(world, pos) :  defaultLeavesColor, blockForm);
		ColorProviderRegistry.ITEM.register((stack, tintIndex) -> defaultLeavesColor, itemForm);

	}

}
