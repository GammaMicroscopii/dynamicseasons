package com.gammamicroscopii.mixin.block;


import com.gammamicroscopii.Temperature;
import com.gammamicroscopii.mixed.BiomeMixed;
import com.gammamicroscopii.world.ServerWorldTick;
import com.gammamicroscopii.world.TemperatureCalculation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SnowBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Heightmap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SnowBlock.class)
public class SnowBlockMixin {


	/** MELT SNOW, NOT EXPOSED TO SKY */
	@Inject(method = "randomTick", at = @At("HEAD"), cancellable = true)
	public void injected (BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci) {
		if (random.nextInt(3) != 0 ) return; //a third for each randomTickSpeed, emulates climate tick speed
		if (world.getTopPosition(Heightmap.Type.MOTION_BLOCKING, pos).getY() == pos.getY()) return;
		ServerWorldTick.tryMeltSnowBlock(world, pos, state);
		ci.cancel();
	}

	@ModifyConstant(method = "randomTick", constant = @Constant(intValue = 11))
	private int injected61(int value) {
		return 16;
	}

}
