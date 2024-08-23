package com.gammamicroscopii.block;

import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;


public interface SeasonallyDisappearingSoilBlock {

	void onSeasonalTick(ServerWorld world, BlockState blockState, BlockPos pos, Random random, float startSeason, float currentSeason, float endSeason);

	void tryPlaceBlockOfThis(ServerWorld world, BlockPos pos, BlockState state, boolean stateIsAir, Random random);
}
