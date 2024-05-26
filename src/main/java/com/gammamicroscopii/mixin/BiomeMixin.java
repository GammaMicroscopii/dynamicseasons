package com.gammamicroscopii.mixin;

import com.gammamicroscopii.Temperature;
import com.gammamicroscopii.mixed.BiomeMixed;
import net.minecraft.client.color.world.FoliageColors;
import net.minecraft.client.color.world.GrassColors;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Biome.class)
public class BiomeMixin implements BiomeMixed {

	//BiomeClimateParameters climateParameters = new BiomeClimateParameters.Builder().build();

	@Inject(method = "getDefaultGrassColor", at = @At(value = "RETURN"), cancellable = true)
	public void inject0(CallbackInfoReturnable<Integer> cir) {

		Biome t = (Biome) ((Object) this);
		float f = MathHelper.clamp((t.weather.temperature()+0.5f) / 1.75f, 0.0f, 1.0f);
		cir.setReturnValue(GrassColors.getColor(f, MathHelper.clamp(t.weather.downfall(), 0.0f, 1.0f) ));
	}

	@Inject(method = "getDefaultFoliageColor", at = @At(value = "RETURN"), cancellable = true)
	public void inject00(CallbackInfoReturnable<Integer> cir) {

		Biome t = (Biome) ((Object) this);
		float f = MathHelper.clamp((t.weather.temperature()+0.5f) / 1.75f, 0.0f, 1.0f);
		cir.setReturnValue(FoliageColors.getColor(f, MathHelper.clamp(t.weather.downfall(), 0.0f, 1.0f) ));
	}

	@Inject(method = "computeTemperature", at= @At(value = "HEAD"), cancellable = true)
	public void inject1(BlockPos pos, CallbackInfoReturnable<Float> cir) {
		cir.setReturnValue( ((Biome)((Object) this)).weather.temperature() + (63 - pos.getY()) / (12 * Temperature.Unit.VANILLA.unitSize) ); //temp gets a twelfth of a degree lower per block upwards
		cir.cancel();
	}

	@ModifyConstant(method = "doesNotSnow", constant =@Constant(floatValue = 0.15f))
	public float inject2(float value) {
		return Temperature.FREEZING.get(Temperature.Unit.VANILLA);
	}

	/*@Override
	public void addClimateParameters(BiomeClimateParameters parameters) {
		this.climateParameters = parameters;
	}

	@Override
	public BiomeClimateParameters getClimateParameters() {
		return this.climateParameters;
	}*/
}