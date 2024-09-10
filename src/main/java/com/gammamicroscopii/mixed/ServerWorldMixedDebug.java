package com.gammamicroscopii.mixed;

import com.gammamicroscopii.mixin.block.IceBlockMixin;
import com.gammamicroscopii.mixin.block.SnowBlockMixin;
import com.gammamicroscopii.mixin.block.WaterFluidMixin;
import com.gammamicroscopii.world.ServerWorldTick;
import com.gammamicroscopii.world.TemperatureCalculation;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SnowBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.GameRules;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.Biome;

public class ServerWorldMixedDebug {


	public static void injected1(BlockPos pos, /*CallbackInfo ci,*/ ServerWorld thiz) {


		BlockPos blockPos = thiz.getTopPosition(Heightmap.Type.MOTION_BLOCKING, pos);
		BlockPos.Mutable mutable = new BlockPos.Mutable(blockPos.getX(), blockPos.getY() -1, blockPos.getZ());
		Biome biome = thiz.getBiome(mutable).value();
		float temp = TemperatureCalculation.getGroundTemperature(((BiomeMixed)(Object)biome).getClimateServerSide()).get();

		/** SECTION 1 ---- WATER -> ICE, EXPOSED TO SKY */
		ServerWorldTick.tryFreezeWaterBlock(thiz, mutable, temp, biome);

		if (thiz.isRaining() && biome.getPrecipitation(blockPos) != Biome.Precipitation.NONE) {

			BlockState blockState3 = thiz.getBlockState(mutable);

			//PRECIPITATION TICK, USED BY CAULDRONS
			blockState3.getBlock().precipitationTick(blockState3, thiz, mutable, biome.getPrecipitation(mutable));

			//int i = thiz.getGameRules().getInt(GameRules.SNOW_ACCUMULATION_HEIGHT);

			/** SECTION 2 ---- PLACE SNOW, EXPOSED TO SKY */
			if (biome.getPrecipitation(mutable).equals(Biome.Precipitation.SNOW)) {
				if (ServerWorldTick.modifyPlacementPos(thiz.getBlockState(mutable),thiz, mutable, Blocks.SNOW, Properties.LAYERS, 3, 2, 4)) {
					mutable.move(Direction.UP);

					if (biome.canSetSnow(thiz, mutable)) {
						BlockState blockState = thiz.getBlockState(mutable);
						if (blockState.isOf(Blocks.SNOW)) {
							int j = blockState.get(SnowBlock.LAYERS);
							if (j < Math.min(8, thiz.getGameRules().getInt(GameRules.SNOW_ACCUMULATION_HEIGHT))) {
								BlockState blockState2 = blockState.with(SnowBlock.LAYERS, j + 1);
								Block.pushEntitiesUpBeforeBlockChange(blockState, blockState2, thiz, mutable);
								thiz.setBlockState(mutable, blockState2, Block.NOTIFY_LISTENERS | Block.NO_REDRAW);
							}
						} else {
							thiz.setBlockState(mutable, Blocks.SNOW.getDefaultState(), Block.NOTIFY_ALL | Block.NO_REDRAW);
						}
					}
				}
			} else {

				/** SECTION 3 ---- MELT SNOW, EXPOSED TO SKY, RAINING */
				BlockState blockState2 = thiz.getBlockState(blockPos);
				if (blockState2.isOf(Blocks.SNOW)) {
					float tempOver5 = temp / 5;
					int minusLayers = (int)tempOver5 + (thiz.random.nextFloat()+(int)tempOver5 < tempOver5 ? 2 : 1);
					int lay = blockState2.get(SnowBlock.LAYERS) - minusLayers;
					thiz.setBlockState(blockPos, lay < 1 ? Blocks.AIR.getDefaultState() : blockState2.with(SnowBlock.LAYERS, lay));
				} else {

					/** SECTION 4 ---- ICE -> WATER, EXPOSED TO SKY, RAINING*/
					BlockState blockState4 = thiz.getBlockState(mutable);
					if (blockState4.isOf(Blocks.ICE)) {
						ServerWorldTick.tryMeltIceBlock(thiz, mutable, temp * 5);
					}
				}
			}




			//Direction direction = Direction.Type.HORIZONTAL.random(thiz.random);
			//BlockPos myPos = blockPos.offset(direction);

			//if (thiz.getBlockState(myPos).isAir()) {
			//	BlockState myDownState = thiz.getBlockState(myPos.down());
			//	if ((myDownState.isAir() || myDownState.isOf(Blocks.SNOW)) && Blocks.SNOW.getDefaultState().canPlaceAt(thiz, myPos.down())) {
			//		thiz.setBlockState(blockPos, Blocks.SNOW.getDefaultState());
			//	}
			//}


			//if (/*i > 0 && */biome.canSetSnow(thiz, blockPos)) {
			//	BlockState blockState = thiz.getBlockState(blockPos);
			//	if (blockState.isOf(Blocks.SNOW)) {




			//			int layers = (Integer)blockState.get(SnowBlock.LAYERS);

			//		if (layers < 8/*Math.min(i, 8)*/) {
			//			BlockState blockState2 = blockState.with(SnowBlock.LAYERS, layers + 1);
			//			Block.pushEntitiesUpBeforeBlockChange(blockState, blockState2, thiz, blockPos);
			//			thiz.setBlockState(blockPos, blockState2);
			//		}
			//	} else {
			//		thiz.setBlockState(blockPos, Blocks.SNOW.getDefaultState());
			//	}
			//}

			/*Biome.Precipitation precipitation = biome.getPrecipitation(mutable);
			if (precipitation != Biome.Precipitation.NONE) {*/
				/*BlockState blockState3 = thiz.getBlockState(mutable);
				blockState3.getBlock().precipitationTick(blockState3, thiz, mutable, biome.getPrecipitation(mutable));*/
			//}
		} else {

			/** SECTION 5 ---- ICE -> WATER, EXPOSED TO SKY, NOT RAINING */
			if (thiz.getBlockState(mutable).isOf(Blocks.ICE))
				ServerWorldTick.tryMeltIceBlock(thiz, mutable, temp);

			/** SECTION 6 ---- MELT SNOW, EXPOSED TO SKY, NOT RAINING */
			if (thiz.getBlockState(blockPos).isOf(Blocks.SNOW))
				ServerWorldTick.tryMeltSnowBlock(thiz, blockPos, thiz.getBlockState(blockPos), temp);
		}

		//ci.cancel();
	}

}

/** PLACE SNOW, EXPOSED TO SKY (snowing only)  					SECTION 2												*/
//* PLACE SNOW, NOT EXPOSED TO SKY 											DOESN´T EXIST										*/
/** MELT SNOW, EXPOSED TO SKY, RAINING  								SECTION 3												*/
/** MELT SNOW, EXPOSED TO SKY, NOT RAINING  						SECTION 6												*/
/** MELT SNOW, NOT EXPOSED TO SKY  											==> mixin.block.SnowBlockMixin	*/

/** WATER -> ICE, EXPOSED TO SKY (snowing or not) 			SECTION 1												*/
/** WATER -> ICE, NOT EXPOSED TO SKY 										==> mixin.block.WaterFluidMixin	*/
/** ICE -> WATER, EXPOSED TO SKY, RAINING 							SECTION 4												*/
/** ICE -> WATER, EXPOSED TO SKY, NOT RAINING 					SECTION 5												*/
/** ICE -> WATER, NOT EXPOSED TO SKY							 			==> mixin.block.IceBlockMixin		*/

class UncommentTheseAndDoAltIntro {

	//	SnowBlockMixin dummySnow;
	//	WaterFluidMixin dummyWater;
	//	IceBlockMixin dummyIce;
}