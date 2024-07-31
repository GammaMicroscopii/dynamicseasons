package com.gammamicroscopii.mixin;

import com.gammamicroscopii.resourceload.assets.ModdedBiomeColors;
import net.minecraft.client.color.world.GrassColors;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GrassColors.class)
public class GrassColorsMixin {

	@Inject(method = "getColor", at = @At("HEAD"), cancellable = true)
	private static void injected0(double temperature, double humidity, CallbackInfoReturnable<Integer> cir) {
		cir.setReturnValue(ModdedBiomeColors.getGrassColor(temperature, humidity));
		cir.cancel();
	}


}
