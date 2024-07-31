package com.gammamicroscopii.mixin;

import com.gammamicroscopii.resourceload.assets.ModdedBiomeColors;
import net.minecraft.client.color.world.FoliageColors;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Probably unused, along with GrassColors
 */
@Mixin(FoliageColors.class)
public class FoliageColorsMixin {

	@Inject(method = "getColor", at = @At("HEAD"), cancellable = true)
	private static void injected0(double temperature, double humidity, CallbackInfoReturnable<Integer> cir) {
		cir.setReturnValue(ModdedBiomeColors.getFoliageColor(temperature, humidity));
		cir.cancel();
	}
}
