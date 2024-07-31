package com.gammamicroscopii.resourceload.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Optional;

public record BiomeClimate(float averageTemperature,
						   float seasonalTempOscillation,
						   float diurnalTempOscillation,
						   float temperatureVariability,
						   float downfall) {



	public static BiomeClimate fromJson(JsonElement json) {
		JsonObject jsOb = json.getAsJsonObject();
		return new BiomeClimate(ifNullReturnEmpty(jsOb.get("average_temperature")).orElse(BiomeClimates.DEFAULT_CLIMATE.averageTemperature()),
				ifNullReturnEmpty(jsOb.get("seasonal_temp_oscillation")).orElse(BiomeClimates.DEFAULT_CLIMATE.seasonalTempOscillation()),
				ifNullReturnEmpty(jsOb.get("diurnal_temp_oscillation")).orElse(BiomeClimates.DEFAULT_CLIMATE.diurnalTempOscillation()),
				ifNullReturnEmpty(jsOb.get("temperature_variability")).orElse(BiomeClimates.DEFAULT_CLIMATE.temperatureVariability()),
				ifNullReturnEmpty(jsOb.get("downfall")).orElse(BiomeClimates.DEFAULT_CLIMATE.downfall()));
	}

	private static Optional<Float> ifNullReturnEmpty(JsonElement jsEl) {
		return jsEl == null ? Optional.empty() : Optional.of(jsEl.getAsFloat());
	}


	public String toString() {
		return "[ average_temperature = " + averageTemperature() +
				", seasonal_temp_oscillation = " + seasonalTempOscillation() +
				", diurnal_temp_oscillation = " + diurnalTempOscillation() +
				", temperature_variability = " + temperatureVariability() +
				", downfall = " + downfall() + " ]";
	}

}
