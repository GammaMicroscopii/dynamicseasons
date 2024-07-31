package com.gammamicroscopii.mixin;

import com.gammamicroscopii.DynamicSeasons;
import com.gammamicroscopii.Temperature;
import com.gammamicroscopii.colors.DSColorHelper;
import com.gammamicroscopii.mixed.BiomeMixed;
import com.gammamicroscopii.resourceload.data.BiomeClimate;
import com.gammamicroscopii.resourceload.data.BiomeClimates;
import com.gammamicroscopii.world.SeasonHelper;
import com.gammamicroscopii.world.ServerWorldTick;
import com.gammamicroscopii.world.TemperatureCalculation;
import net.minecraft.client.MinecraftClient;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(Biome.class)
public class BiomeMixin implements BiomeMixed {

	//BiomeClimateParameters climateParameters = new BiomeClimateParameters.Builder().build();
	private BiomeClimate climate = null;

	@Override
	@Unique
	public void setClimate(BiomeClimate climate) {
		this.climate = climate;
	}

	@Unique
	private BiomeClimate getClimate(DynamicRegistryManager registry) {
		if (climate == null) {
			Identifier biomeId = registry.get(RegistryKeys.BIOME).getId((Biome)(Object)this);
			if (biomeId == null) return BiomeClimates.DEFAULT_CLIMATE;
			setClimate(BiomeClimates.getBiomeClimate(biomeId));
			//DynamicSeasons.LOGGER.info(climate.toString());
		}
		return climate;
	}

	@Override
	public BiomeClimate getClimateClientSide() {
		return getClimate(MinecraftClient.getInstance().world.getRegistryManager());
	}

	@Override
	public BiomeClimate getClimateServerSide() {
		return getClimate(ServerWorldTick.getServer().getRegistryManager());
	}

	/**
	 * called from vanilla to render grass, for the season-agnostic variant see {@link BiomeMixin::getVanillaDefaultGrassColor}
	 */
	@Inject(method = "getDefaultGrassColor", at = @At(value = "RETURN"), cancellable = true)
	public void inject0(CallbackInfoReturnable<Integer> cir) {

		//Biome t = (Biome) ((Object) this);
		//DynamicSeasons.LOGGER.info(MinecraftClient.getInstance().world.getRegistryManager().get(RegistryKeys.BIOME).getEntry(t).getIdAsString());
		//TemperatureCalculation.getBiomeColorTemperature(t);
		//float seasonalTemp = SeasonHelper.getSeasonalTempDeltaFactor()+new Temperature(climate.seasonalTempOscillation()).get(Temperature.Unit.VANILLA);
		//float diurnalTemp = SeasonHelper.getDiurnalTempDeltaFactor()+new Temperature(climate.diurnalTempOscillation()).get(Temperature.Unit.VANILLA);
		cir.setReturnValue(DSColorHelper.getGrassColormapColor(TemperatureCalculation.getBiomeColorTemperature(getClimateClientSide()).get(Temperature.Unit.VANILLA), getClimateClientSide().downfall()));
	}

	@Inject(method = "getDefaultFoliageColor", at = @At(value = "RETURN"), cancellable = true)
	public void inject00(CallbackInfoReturnable<Integer> cir) {

		//Biome t = (Biome) ((Object) this);
		//float seasonalTemp = SeasonHelper.getSeasonalTempDeltaFactor()+new Temperature(climate.seasonalTempOscillation()).get(Temperature.Unit.VANILLA);
		//float diurnalTemp = SeasonHelper.getDiurnalTempDeltaFactor()+new Temperature(climate.seasonalTempOscillation()).get(Temperature.Unit.VANILLA);
		cir.setReturnValue(DSColorHelper.getFoliageColormapColor(TemperatureCalculation.getBiomeColorTemperature(getClimateClientSide()).get(Temperature.Unit.VANILLA), getClimateClientSide().downfall()));
	}

	@Inject(method = "computeTemperature", at= @At(value = "HEAD"), cancellable = true)
	public void inject1(BlockPos pos, CallbackInfoReturnable<Float> cir) {
		//cir.setReturnValue( ((Biome)((Object) this)).weather.temperature() + (63 - pos.getY()) / (12 * Temperature.Unit.VANILLA.unitSize) ); //temp gets a twelfth of a degree lower per block upwards
		cir.setReturnValue( ((Biome)((Object) this)).getTemperature() /* + (63 - pos.getY()) / (12 * Temperature.Unit.VANILLA.unitSize) */ );
	}

	@Inject(method = "getTemperature()F", at= @At(value = "TAIL"), cancellable = true)
	public void injected11(CallbackInfoReturnable<Float> cir) {
		//float seasonalTemp = SeasonHelper.getSeasonalTempDeltaFactor()+new Temperature(climate.seasonalTempOscillation()).get(Temperature.Unit.VANILLA);
		//float diurnalTemp = SeasonHelper.getDiurnalTempDeltaFactor()+new Temperature(climate.seasonalTempOscillation()).get(Temperature.Unit.VANILLA);
		cir.setReturnValue(TemperatureCalculation.getBiomeColorTemperature(getClimateServerSide()).get(Temperature.Unit.VANILLA));
	}

	@ModifyConstant(method = "doesNotSnow", constant =@Constant(floatValue = 0.15f))
	public float inject2(float value) {
		return Temperature.FREEZING.get(Temperature.Unit.VANILLA);
	}

	@Inject(method = "getFoliageColor", at = @At(value = "TAIL"), cancellable = true)
	public void injected30(CallbackInfoReturnable<Integer> cir) {
		Optional<Integer> defFC = ((Biome)((Object) this)).getEffects().getFoliageColor(); //vanilla hardcoded foliage color
		int correspondingTmpDwfColor = ((Biome)((Object) this)).getDefaultFoliageColor(); //with season
		if (defFC.isPresent()) {
			correspondingTmpDwfColor = DSColorHelper.average(correspondingTmpDwfColor, DSColorHelper.exaggerateColor(getVanillaDefaultFoliageColor(), defFC.get()));
		}
		cir.setReturnValue(correspondingTmpDwfColor);
		cir.cancel();
	}

	@Inject(method = "getGrassColorAt" , at = @At(value = "RETURN"), cancellable = true)
	public void injected3(double x, double z, CallbackInfoReturnable<Integer> cir) {

		Optional<Integer> defGC = ((Biome)((Object) this)).getEffects().getGrassColor(); //vanilla hardcoded grass color, optional
		BiomeEffects.GrassColorModifier defgcm = ((Biome)((Object) this)).getEffects().getGrassColorModifier(); //vanilla grass modifier
		int correspondingTmpDwfColor = ((Biome)((Object) this)).getDefaultGrassColor(); //default color, with season

		if (defGC.isPresent()) {
			correspondingTmpDwfColor = DSColorHelper.average(correspondingTmpDwfColor, DSColorHelper.exaggerateColor(getVanillaDefaultGrassColor()/**/, defGC.get())); // *default color, without season
		}
		if(defgcm == BiomeEffects.GrassColorModifier.SWAMP) {
			cir.setReturnValue(DSColorHelper.average(correspondingTmpDwfColor, DSColorHelper.VANILLA_GRASS_COLOR_AVERAGE_SWAMP));
		} else if (defgcm == BiomeEffects.GrassColorModifier.DARK_FOREST) {
			cir.setReturnValue(DSColorHelper.average(correspondingTmpDwfColor, DSColorHelper.VANILLA_GRASS_COLOR_DARK_FOREST));
		} else {
			cir.setReturnValue(correspondingTmpDwfColor);
		}
		cir.cancel();
	}

	@Inject(method = "getWaterColor", at = @At(value = "TAIL"), cancellable = true)
	public void injected4(CallbackInfoReturnable<Integer> cir) {

		int correspondingWC = DSColorHelper.getDefaultWaterColor(((Biome)(Object) this).getTemperature()); //with season, from getTemperature (mixined here at "injected11")
		if (((Biome)((Object) this)).getEffects().getGrassColorModifier() == BiomeEffects.GrassColorModifier.SWAMP) {
			int defWC = ((Biome)((Object) this)).getEffects().getWaterColor(); //the vanilla hardcoded one
			correspondingWC = DSColorHelper.average(correspondingWC, DSColorHelper.exaggerateColor(getVanillaDefaultWaterColor(), defWC));
		}
		cir.setReturnValue(correspondingWC);
		cir.cancel();
	}

	@Inject(method = "getWaterFogColor", at = @At(value = "TAIL"), cancellable = true)
	public void injected5(CallbackInfoReturnable<Integer> cir) {

		int correspondingWFC = DSColorHelper.getDefaultWaterFogColor(((Biome)(Object) this).getTemperature()); //with season, from getTemperature (mixined here at "injected11")
		if (((Biome)((Object) this)).getEffects().getGrassColorModifier() == BiomeEffects.GrassColorModifier.SWAMP) {
			int defWFC = ((Biome)((Object) this)).getEffects().getWaterFogColor(); //the vanilla hardcoded one
			correspondingWFC = DSColorHelper.average(correspondingWFC, DSColorHelper.exaggerateColor(getVanillaDefaultWaterFogColor(), defWFC));
		}
		cir.setReturnValue(correspondingWFC);
		cir.cancel();
	}

	/*@Unique
	public int getVanillaGrassColor() {
		BiomeEffects effects = ((Biome) ((Object) this)).getEffects();
		switch (effects.getGrassColorModifier()) {
			case SWAMP:
				return DSColorHelper.VANILLA_GRASS_COLOR_AVERAGE_SWAMP;
			case DARK_FOREST:
				return DSColorHelper.VANILLA_GRASS_COLOR_DARK_FOREST;
			case NONE:
			default:
				return effects.getGrassColor().orElseGet(this::getVanillaDefaultGrassColor);
		}
	}*/

	@Unique
	public int getVanillaDefaultGrassColor() {
		Biome.Weather w = ((Biome) ((Object) this)).weather;
		return DSColorHelper.getGrassColormapColor(w.temperature(), w.downfall());
	}

	/*@Unique
	public int getVanillaFoliageColor() {
		return ((Biome) ((Object) this)).getEffects().getFoliageColor().orElseGet(this::getVanillaDefaultFoliageColor);
	}*/

	@Unique
	private int getVanillaDefaultFoliageColor() {
		Biome.Weather w = ((Biome) ((Object) this)).weather;
		return DSColorHelper.getFoliageColormapColor(w.temperature(), w.downfall());
	}

	@Unique
	public int getVanillaDefaultWaterColor() {
		return DSColorHelper.getDefaultWaterColor(((Biome) ((Object) this)).weather.temperature());
	}

	@Unique
	public int getVanillaDefaultWaterFogColor() {
		return DSColorHelper.getDefaultWaterFogColor(((Biome) ((Object) this)).weather.temperature());
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