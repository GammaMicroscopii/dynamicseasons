package com.gammamicroscopii.mixin;

import com.gammamicroscopii.block.ModBlocks;
import com.gammamicroscopii.block.SeasonallyDisappearingSoilBlock;
import com.gammamicroscopii.world.SeasonHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowerbedBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;

import java.util.Random;

@Mixin(FlowerbedBlock.class)
public class FlowerbedBlockMixin implements SeasonallyDisappearingSoilBlock {

	@Override
	public void onSeasonalTick(ServerWorld world, BlockState blockState, BlockPos pos, Random random, float startSeason, float currentSeason, float endSeason) {
		if (SeasonHelper.isMiddleBetweenExtremes(startSeason, currentSeason, endSeason) && world.getBlockState(pos.down()).isIn(ModBlocks.Tags.ORGANIC_SOIL)) {
			int flowerAmount = blockState.get(FlowerbedBlock.FLOWER_AMOUNT);
			if (flowerAmount == 1) {
				world.setBlockState(pos, Blocks.AIR.getDefaultState());
			} else {
				world.setBlockState(pos, blockState.with(FlowerbedBlock.FLOWER_AMOUNT, flowerAmount - 1), Block.NOTIFY_LISTENERS);
			}
		}
	}
}
