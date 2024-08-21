package com.gammamicroscopii.mixin.block;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SnowBlock;
import net.minecraft.block.SpreadableBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

//@Debug(export = true, print = true)
@Mixin(SpreadableBlock.class)
public class SpreadableBlockMixin {

	@Inject(method = "canSurvive", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/WorldView;getBlockState(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/BlockState;", shift = At.Shift.AFTER), cancellable = true)
	private static void injected61(BlockState state, WorldView world, BlockPos pos, CallbackInfoReturnable<Boolean> cir, @Local(ordinal = 1) BlockPos blockPos) {
		BlockState blockState0 = world.getBlockState(blockPos);
		boolean bl = blockState0.isOf(Blocks.SNOW);
		if (bl) {
			cir.setReturnValue(bl);
			cir.cancel();
		}
	}

}
