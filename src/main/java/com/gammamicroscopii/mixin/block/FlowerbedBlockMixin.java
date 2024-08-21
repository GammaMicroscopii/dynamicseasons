package com.gammamicroscopii.mixin.block;

import com.gammamicroscopii.block.ModBlocks;
import com.gammamicroscopii.block.SeasonallyDisappearingSoilBlock;
import com.gammamicroscopii.world.SeasonHelper;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowerbedBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;

import static com.gammamicroscopii.world.SeasonHelper.*;
import static com.gammamicroscopii.world.SeasonHelper.calculateInverseRate;


@Mixin(FlowerbedBlock.class)
public class FlowerbedBlockMixin implements SeasonallyDisappearingSoilBlock {

	@Override
	public void onSeasonalTick(ServerWorld world, BlockState blockState, BlockPos pos, Random random, float startSeason, float currentSeason, float endSeason) {

		final float despawningSeasonYearFraction = 0.1625f;
		final float pseudoEndSeason =  advanceSeason(startSeason, despawningSeasonYearFraction);
		float remainingProgress = isMiddleBetweenExtremes(startSeason, currentSeason, pseudoEndSeason) ?
						getRemainingIntervalProgress(startSeason, currentSeason, pseudoEndSeason) : 0f;

		//float square = remainingProgress * remainingProgress;
		//float calculateInverseRate = 0.00549316406f * square * square * remainingProgress * despawningSeasonYearFraction * CHANCE_OF_SEASONAL_UPDATE_PER_TICK_FOR_ANY_BLOCK;

		if (random.nextFloat() * calculateInverseRate(remainingProgress, despawningSeasonYearFraction, 0.00549316406f, 1.75f) < 1.0f) {

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

	@Override
	public void tryPlaceBlockOfThis(ServerWorld world, BlockPos pos, BlockState state, boolean stateIsAir, Random random) {
		FlowerbedBlock thiz = (FlowerbedBlock) (Object) this;
		if (stateIsAir) {
			world.setBlockState(pos, thiz.getDefaultState().with(Properties.HORIZONTAL_FACING, Direction.Type.HORIZONTAL.random(random)));
		} else {
			world.setBlockState(pos, state.with(Properties.FLOWER_AMOUNT, Math.min(4, state.get(Properties.FLOWER_AMOUNT) + 1)));
		}
	}
}
