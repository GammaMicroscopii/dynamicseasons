package com.gammamicroscopii.mixin;

import com.gammamicroscopii.Temperature;
import com.gammamicroscopii.block.SeasonallyDisappearingSoilBlock;
import com.gammamicroscopii.mixed.BiomeMixed;
import com.gammamicroscopii.mixed.ServerWorldMixed;
import com.gammamicroscopii.mixed.ServerWorldMixedDebug;
import com.gammamicroscopii.world.SeasonHelper;
import com.gammamicroscopii.world.SeasonInfo;
import com.gammamicroscopii.world.ServerWorldTick;
import com.gammamicroscopii.world.TemperatureCalculation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.*;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.GameRules;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;

@Mixin(ServerWorld.class)
public class ServerWorldMixin implements ServerWorldMixed {

	private int seasonUpdateTimeout = 0;
	private int renderReloadCountdown = 0;
	private final SeasonInfo seasonInfo = new SeasonInfo( ((ServerWorld) (Object) this).getTimeOfDay());
	/*private float season;
	private float seasonalTempDelta;
	private float diurnalTempDelta;*/

	@Override
	public int getSeasonUpdateTimeout() {
		return seasonUpdateTimeout;
	}

	@Override
	public void setSeasonUpdateTimeout(int value) {
		seasonUpdateTimeout = value;
	}

	@Override
	public int getRenderReloadCountdown() {
		return renderReloadCountdown;
	}

	@Override
	public void setRenderReloadCountdown(int value) {
		renderReloadCountdown = value;
	}

	@Override
	public SeasonInfo getSeasonInfo() {
		return seasonInfo;
	}

	@Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;setTimeOfDay(J)V", shift = At.Shift.AFTER))
	public void injected(BooleanSupplier shouldKeepTicking, CallbackInfo ci) {
		SeasonHelper.updateSeason((ServerWorld)(Object) this, true);
	}

	@Redirect(method = "tickChunk(Lnet/minecraft/world/chunk/WorldChunk;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;tickIceAndSnow(Lnet/minecraft/util/math/BlockPos;)V"))
	private void injected0(ServerWorld serverWorld, BlockPos pos) {

	}

	/**
	 * copypasted cause @Redirect syntax is extremely convoluted and unclear
	 */
	@Inject(method = "tickIceAndSnow", at = @At(value = "HEAD"), cancellable = true)
	public void injected1(BlockPos pos, CallbackInfo ci) {
		ServerWorld thiz = (ServerWorld)(Object)this;
		ServerWorldMixedDebug.injected1(pos, thiz);

/*
		BlockPos blockPos = thiz.getTopPosition(Heightmap.Type.MOTION_BLOCKING, pos);
		BlockPos.Mutable mutable = new BlockPos.Mutable(blockPos.getX(), blockPos.getY() -1, blockPos.getZ());
		Biome biome = thiz.getBiome(mutable).value();
		float temp = TemperatureCalculation.getGroundTemperature(((BiomeMixed)(Object)biome).getClimateServerSide()).get();
		ServerWorldTick.tryFreezeWaterBlock(thiz, mutable, temp, biome);

		if (thiz.isRaining() && biome.getPrecipitation(blockPos) != Biome.Precipitation.NONE) {

			BlockState blockState3 = thiz.getBlockState(mutable);
			blockState3.getBlock().precipitationTick(blockState3, thiz, mutable, biome.getPrecipitation(mutable));

			//int i = thiz.getGameRules().getInt(GameRules.SNOW_ACCUMULATION_HEIGHT);
			if (biome.getPrecipitation(mutable).equals(Biome.Precipitation.SNOW)) {
				if (ServerWorldTick.modifyPlacementPos(thiz.getBlockState(mutable),thiz, mutable, Blocks.SNOW, Properties.LAYERS, 2, 2, 2)) {
					mutable.move(Direction.UP);

					if (biome.canSetSnow(thiz, mutable)) {
						BlockState blockState = thiz.getBlockState(mutable);
						if (blockState.isOf(Blocks.SNOW)) {
							int j = blockState.get(SnowBlock.LAYERS);
							if (j < 8) {
								BlockState blockState2 = blockState.with(SnowBlock.LAYERS, j + 1);
								Block.pushEntitiesUpBeforeBlockChange(blockState, blockState2, thiz, mutable);
								thiz.setBlockState(mutable, blockState2);
							}
						} else {
							thiz.setBlockState(mutable, Blocks.SNOW.getDefaultState());
						}
					}
				}
			} else {
				BlockState blockState2 = thiz.getBlockState(blockPos);
				if (blockState2.isOf(Blocks.SNOW)) {
					float tempOver5 = temp / 5;
					int minusLayers = (int)tempOver5 + (thiz.random.nextFloat()+(int)tempOver5 < tempOver5 ? 2 : 1);
					int lay = blockState2.get(SnowBlock.LAYERS) - minusLayers;
					thiz.setBlockState(blockPos, lay < 1 ? Blocks.AIR.getDefaultState() : blockState2.with(SnowBlock.LAYERS, lay));
				} else {
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


			//if (/*i > 0 && *///biome.canSetSnow(thiz, blockPos)) {
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

//			/*Biome.Precipitation precipitation = biome.getPrecipitation(mutable);
//			if (precipitation != Biome.Precipitation.NONE) {*/
//				/*BlockState blockState3 = thiz.getBlockState(mutable);
//				blockState3.getBlock().precipitationTick(blockState3, thiz, mutable, biome.getPrecipitation(mutable));*/
			//}
/*		} else {
			if (thiz.getBlockState(mutable).isOf(Blocks.ICE)) ServerWorldTick.tryMeltIceBlock(thiz, mutable, temp);
			if (thiz.getBlockState(mutable).isOf(Blocks.SNOW)) ServerWorldTick.tryMeltSnowBlock(thiz, mutable, thiz.getBlockState(mutable), temp);
		}*/

	ci.cancel();
	}


	/*@ModifyVariable(method = "tickIceAndSnow", at = @At("STORE"), ordinal = 0)
	private BlockPos injected1(BlockPos exprResult, @Local(argsOnly = true) BlockPos pos) {
		ServerWorld thiz = (ServerWorld)(Object)this;
		return thiz.random.nextBoolean() ? thiz.getTopPosition(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, pos) : exprResult;
	}*/

}
