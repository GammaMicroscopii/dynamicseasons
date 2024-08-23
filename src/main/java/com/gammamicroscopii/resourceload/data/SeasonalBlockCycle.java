package com.gammamicroscopii.resourceload.data;

import com.gammamicroscopii.DynamicSeasons;
import com.gammamicroscopii.world.SeasonHelper;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.List;

import static com.gammamicroscopii.resourceload.data.BlockStateFilterer.jsonElementToBlockstateValue;
import static com.gammamicroscopii.resourceload.data.BlockStateFilterer.parseProperties;

public class SeasonalBlockCycle {

	final Conversion[] conversions;
	final Float[] intervalChangeSeasons;
	//final String[] blockIds;
	float currentIntervalBeginning;
	float currentIntervalEnd;
	//Pair<Integer, Boolean> currentCycleStageAsPair = null;
	int currentConversionIndex;
	boolean isConversionInProgress;

	SeasonalBlockCycle(Conversion[] conversions) {
		this.conversions = conversions;
		this.intervalChangeSeasons = new Float[conversions.length * 2];
		for (int i = 0; i < conversions.length; i++) {
			intervalChangeSeasons[i << 1] = conversions[i].startSeason();
			intervalChangeSeasons[i << 1 | 1] = conversions[i].endSeason();
		}
		/*this.blockIds = new String[conversions.length];
		for (int i = 0; i < conversions.length; i++) {
			blockIds[i] = conversions[i].turnsInto().toString();
		}*/
	}

	public Conversion[] conversions() {
		return conversions;
	}

	public void updateCycleStage(float season) {
		//if (!(currentIntervalBeginning == null || currentIntervalEnd == null || currentConversionIndex == null || isConversionInProgress == null)) {
			if (SeasonHelper.hasReachedNewYear(currentIntervalBeginning, currentIntervalEnd)) {
				if (season > currentIntervalBeginning - 1E-7f || season < currentIntervalEnd) return;
			} else {
				if (season > currentIntervalBeginning - 1E-7f && season < currentIntervalEnd) return;
			}
		//}

		int intervalIndex;

		int i = 0;
		Float previousIntervalOfI;
		while (true) {
			previousIntervalOfI = previousElement(i, intervalChangeSeasons) - 1E-7f;
			if (SeasonHelper.hasReachedNewYear(previousIntervalOfI, intervalChangeSeasons[i])) {
				if (season > previousIntervalOfI || season < intervalChangeSeasons[i]) {
					intervalIndex = i;
					break;
				}
			} else {
				if (season > previousIntervalOfI && season < intervalChangeSeasons[i]) {
					intervalIndex = i;
					break;
				}
			}

			i++;
			if (i >= intervalChangeSeasons.length) i -= intervalChangeSeasons.length;
		}

		currentIntervalBeginning = previousElement(intervalIndex, intervalChangeSeasons);
		currentIntervalEnd = intervalChangeSeasons[intervalIndex];

		//currentCycleStageAsPair = new Pair<>(intervalIndex >> 1, (intervalIndex & 1) == 1);
		currentConversionIndex = intervalIndex >> 1;
		isConversionInProgress = (intervalIndex & 1) == 1;

	}

	public TurnsInto getStableBlock(float season) {
		//updateCycleStage(season);
		if (isConversionInProgress) { //if conversion is in progress
			return conversions[currentConversionIndex].turnsInto();
		} else {
			return previousElement(currentConversionIndex, conversions).turnsInto();
		}
	}

	public TurnsInto getPreviouslyStableBlock(float season) {
		//updateCycleStage(season);
		return previousElement(currentConversionIndex, conversions).turnsInto();
	}

	public float getConversionRemainingProgress(float season) {
		//getCycleStage(season);
		return SeasonHelper.getRemainingIntervalProgress(currentIntervalBeginning, season, currentIntervalEnd);
	}

	public float getConversionLengthInYearFraction() {
		//updateCycleStage(season);
		//float elapsedFromBeginning = season - currentIntervalBeginning;
		//if (elapsedFromBeginning < 0) elapsedFromBeginning += 1f;
		float fract = currentIntervalEnd - currentIntervalBeginning;
		if (fract < 0) fract += 1f;
		return fract;
	}

	public static int previousArrayIndex(int index, Object[] array) {
		return index == 0 ? array.length - 1 : index - 1;
	}

	public static int nextArrayIndex(int index, Object[] array) {
		return index == array.length - 1 ? 0 : index + 1;
	}

	public static <T> T previousElement(int index, T[] array) {
		return array[previousArrayIndex(index, array)];
	}

	public static <T> T nextElement(int index, T[] array) {
		return array[nextArrayIndex(index, array)];
	}

	public static SeasonalBlockCycle fromJson(JsonElement json) {
		JsonObject jsOb = json.getAsJsonObject();
		JsonArray conversions = jsOb.getAsJsonArray("conversions");

		return new SeasonalBlockCycle(parseConversions(conversions.asList()));
	}

	private static Conversion[] parseConversions(List<JsonElement> conversionsJson) {

		if (conversionsJson.size() < 2) throw new RuntimeException("found less than 2 conversions");

		Conversion[] conversions = new Conversion[conversionsJson.size()];
		for (int i = 0; i < conversionsJson.size(); i++) {
			conversions[i] = parseConversion(conversionsJson.get(i));
		}
		adjustFiltererIds(conversions);
		return conversions;
	}

	private static void adjustFiltererIds(Conversion[] conversions) {
		for (int i = 0; i < conversions.length; i++) {
			conversions[i].blockStateFilterer.id = previousElement(i, conversions).turnsInto.id;
		}
	}

	private static Conversion parseConversion(JsonElement jsonElement) {
		JsonObject jsOb = jsonElement.getAsJsonObject();
		JsonElement previousBlockBlockstates = jsOb.get("previous_block_blockstates");
		TurnsInto turnsInto = parseTurnsInto(jsOb.get("turns_into"));
		return new Conversion(
				new BlockStateFilterer(turnsInto.id(), previousBlockBlockstates == null ? new com.gammamicroscopii.resourceload.data.PropertyValue[0] : parseProperties(previousBlockBlockstates.getAsJsonArray().asList())),
				turnsInto,
				jsOb.get("start_season").getAsFloat(),
				jsOb.get("end_season").getAsFloat()
		);
	}




	private static TurnsInto parseTurnsInto(JsonElement jsonElement) {
		JsonObject jsOb = jsonElement.getAsJsonObject();
		JsonElement blockState = jsOb.get("blockstate");
		return new TurnsInto(
				jsOb.get("id").getAsString(),
				(blockState == null ? new TIPropertyValue[0] : parseTIPropertyValuePairs(blockState.getAsJsonArray().asList()))
		);
	}

	private static TIPropertyValue[] parseTIPropertyValuePairs(List<JsonElement> propertyValuePairs) {
		TIPropertyValue[] finalList = new TIPropertyValue[propertyValuePairs.size()];
		for (int i = 0; i < propertyValuePairs.size(); i++) {
			finalList[i] = parseTIPropertyValuePair(propertyValuePairs.get(i));
		}
		return finalList;
	}

	private static TIPropertyValue parseTIPropertyValuePair(JsonElement jsonElement) {
		JsonObject jsOb = jsonElement.getAsJsonObject();
		return new TIPropertyValue(jsOb.get("property").getAsString(), jsonElementToBlockstateValue(jsOb.get("value")));
	}



	/*private static Conversion[] ifNullReturnEmpty(JsonElement jsEl) {
		return jsEl == null ? Optional.empty() : Optional.of(jsEl.getAsFloat());
	}*/


	/*public String toString() {
		return "[ average_temperature = " + averageTemperature() +
				", seasonal_temp_oscillation = " + seasonalTempOscillation() +
				", diurnal_temp_oscillation = " + diurnalTempOscillation() +
				", temperature_variability = " + temperatureVariability() +
				", downfall = " + downfall() + " ]";
	}*/


	public record Conversion(BlockStateFilterer blockStateFilterer,
					  TurnsInto turnsInto,
					  float startSeason,
					  float endSeason) {

	}

	public record PreviousBlockBlockState(String property,
								   Object[] affectedValues) {

	}

	public record TurnsInto(String id,
													TIPropertyValue[] propertyValues) {

	}

	public record TIPropertyValue(String property,
																Object value) {

	}
}
