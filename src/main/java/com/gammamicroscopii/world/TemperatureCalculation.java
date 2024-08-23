package com.gammamicroscopii.world;

import com.gammamicroscopii.Temperature;
import com.gammamicroscopii.colors.DSColorHelper;
import com.gammamicroscopii.mixed.BiomeMixed;
import com.gammamicroscopii.resourceload.data.BiomeClimate;
import com.gammamicroscopii.resourceload.data.BiomeClimates;
import net.minecraft.world.biome.Biome;

public class TemperatureCalculation {

	public static Temperature getBiomeColorTemperature(BiomeClimate climate) {
		//BiomeClimate climate = ((BiomeMixed)(Object) biome).getClimate();
		float seasonalDelta = climate.seasonalTempOscillation() * SeasonHelper.getSeasonalTempDeltaFactor();//+new Temperature(climate.seasonalTempOscillation()).get(Temperature.Unit.VANILLA);
		float diurnalDelta = climate.diurnalTempOscillation() * SeasonHelper.getDiurnalTempDeltaFactor();//+new Temperature(climate.diurnalTempOscillation()).get(Temperature.Unit.VANILLA);
		return new Temperature(climate.averageTemperature() + seasonalDelta + diurnalDelta);

	}

	public static Temperature getGroundTemperature(BiomeClimate climate) {
		return getBiomeColorTemperature(climate);
	}

	public static Temperature getThermometerTemperature(BiomeClimate climate) {
		return getGroundTemperature(climate);
	}

}
