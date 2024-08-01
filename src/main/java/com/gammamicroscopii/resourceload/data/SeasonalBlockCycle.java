package com.gammamicroscopii.resourceload.data;

import com.gammamicroscopii.DynamicSeasons;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.util.Pair;

import java.util.List;

public class SeasonalBlockCycle {

	final Conversion[] conversions;
	final float[] intervalChangeSeasons;
	final String[] blockIds;
	float currentIntervalBeginning;
	float currentIntervalEnd;
	//Pair<Integer, Boolean> currentCycleStageAsPair = null;
	int currentConversionIndex;
	boolean isConversionInProgress;

	SeasonalBlockCycle(Conversion[] conversions) {
		this.conversions = conversions;
		this.intervalChangeSeasons = new float[conversions.length * 2];
		for (int i = 0; i < conversions.length; i++) {
			intervalChangeSeasons[i << 1] = conversions[i].startSeason();
			intervalChangeSeasons[i << 1 | 1] = conversions[i].endSeason();
		}
		this.blockIds = new String[conversions.length];
		for (int i = 0; i < conversions.length; i++) {
			blockIds[i] = conversions[i].turnsInto().toString();
		}
	}

	public Conversion[] conversions() {
		return conversions;
	}

	public void updateCycleStage(float season) {
		//if (!(currentIntervalBeginning == null || currentIntervalEnd == null || currentConversionIndex == null || isConversionInProgress == null)) {
			if (hasJustWrappedAround(currentIntervalBeginning, currentIntervalEnd)) {
				if (season > currentIntervalBeginning || season < currentIntervalEnd) return;
			} else {
				if (season > currentIntervalBeginning && season < currentIntervalEnd) return;
			}
		//}

		int intervalIndex = 0;

		int i = 0;
		while (true) {

			if (hasJustWrappedAround(previousInterval(i), intervalChangeSeasons[i])) {
				if (season > previousInterval(i) || season < intervalChangeSeasons[i]) {
					intervalIndex = i;
					break;
				}
			} else {
				if (season > previousInterval(i) && season < intervalChangeSeasons[i]) {
					intervalIndex = i;
					break;
				}
			}

			i++;
			if (i >= intervalChangeSeasons.length) i -= intervalChangeSeasons.length;
		}

		currentIntervalBeginning = previousInterval(intervalIndex);
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
			return previousConversion(currentConversionIndex).turnsInto();
		}
	}

	public TurnsInto getPreviouslyStableBlock(float season) {
		//updateCycleStage(season);
		return previousConversion(currentConversionIndex).turnsInto();
	}

	public float getConversionRemainingProgress(float season) {
		//getCycleStage(season);
		float elapsedFromBeginning = season - currentIntervalBeginning;
		if (elapsedFromBeginning < 0) elapsedFromBeginning += 1f;
		float remaining = currentIntervalEnd - season;
		if (remaining < 0) remaining += 1f;
		return remaining / (elapsedFromBeginning + remaining);
	}

	public int getConversionRemainingTicks(float season) {
		//updateCycleStage(season);
		//float elapsedFromBeginning = season - currentIntervalBeginning;
		//if (elapsedFromBeginning < 0) elapsedFromBeginning += 1f;
		float remaining = currentIntervalEnd - season;
		if (remaining < 0) remaining += 1f;
		return (int)(remaining * (double)DynamicSeasons.YEAR_DURATION);
	}


	private boolean hasJustWrappedAround(float lower, float higher) {
		return higher < lower;
	}

	public float previousInterval(int i) {
		if (i == 0) {
			return intervalChangeSeasons[intervalChangeSeasons.length - 1];
		} else {
			return intervalChangeSeasons[i - 1];
		}
	}

	public Conversion previousConversion(int i) {
		return conversions[previousConversionIndex(i)];
	}

	public int previousConversionIndex(int i) {
		return i == 0 ? conversions.length - 1 : i - 1;
	}

	public Conversion nextConversion(int i) {
		return conversions[nextConversionIndex(i)];
	}

	public int nextConversionIndex(int i) {
		return i == conversions.length - 1 ? 0 : i + 1;
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
		return conversions;
	}

	private static Conversion parseConversion(JsonElement jsonElement) {
		JsonObject jsOb = jsonElement.getAsJsonObject();
		JsonElement previousBlockBlockstates = jsOb.get("previous_block_blockstates");
		return new Conversion(
				(previousBlockBlockstates == null ? new PreviousBlockBlockState[0] : parsePreviousBlockBlockstates(previousBlockBlockstates.getAsJsonArray().asList())),
				parseTurnsInto(jsOb.get("turns_into")),
				jsOb.get("start_season").getAsFloat(),
				jsOb.get("end_season").getAsFloat()
		);
	}

	private static PreviousBlockBlockState[] parsePreviousBlockBlockstates(List<JsonElement> PBBSJson) {
		PreviousBlockBlockState[] PBBSs = new PreviousBlockBlockState[PBBSJson.size()];
		for (int i = 0; i < PBBSJson.size(); i++) {
			PBBSs[i] = parsePreviousBlockBlockstate(PBBSJson.get(i));
		}
		return PBBSs;
	}

	private static PreviousBlockBlockState parsePreviousBlockBlockstate(JsonElement jsonElement) {
		JsonObject jsOb = jsonElement.getAsJsonObject();
		return new PreviousBlockBlockState(jsOb.get("property").getAsString(), converttoObjectArray(jsOb.get("affected_values").getAsJsonArray().asList()));
	}

	private static Object[] converttoObjectArray(List<JsonElement> affected_values) {
		Object[] finalList = new Object[affected_values.size()];
		for (int i = 0; i < affected_values.size(); i++) {
			finalList[i] = jsonElementToBlockstateValue(affected_values.get(i));
		}
		return finalList;
	}


	private static TurnsInto parseTurnsInto(JsonElement jsonElement) {
		JsonObject jsOb = jsonElement.getAsJsonObject();
		JsonElement blockState = jsOb.get("blockstate");
		return new TurnsInto(
				jsOb.get("id").getAsString(),
				(blockState == null ? new PropertyValue[0] : parsePropertyValuePairs(blockState.getAsJsonArray().asList()))
		);
	}

	private static PropertyValue[] parsePropertyValuePairs(List<JsonElement> propertyValuePairs) {
		PropertyValue[] finalList = new PropertyValue[propertyValuePairs.size()];
		for (int i = 0; i < propertyValuePairs.size(); i++) {
			finalList[i] = parsePropertyValuePair(propertyValuePairs.get(i));
		}
		return finalList;
	}

	private static PropertyValue parsePropertyValuePair(JsonElement jsonElement) {
		JsonObject jsOb = jsonElement.getAsJsonObject();
		return new PropertyValue(jsOb.get("property").getAsString(), jsonElementToBlockstateValue(jsOb.get("value")));
	}

	private static Object jsonElementToBlockstateValue(JsonElement value) {
		JsonPrimitive primitive = value.getAsJsonPrimitive();
		return primitive.isBoolean() ? primitive.getAsBoolean() : (primitive.isNumber() ? primitive.getAsInt() : primitive.getAsString());
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


	public record Conversion(PreviousBlockBlockState[] previousBlockBlockStates,
					  TurnsInto turnsInto,
					  float startSeason,
					  float endSeason) {

	}

	public record PreviousBlockBlockState(String property,
								   Object[] affectedValues) {

	}

	public record TurnsInto(String id,
					 PropertyValue[] propertyValues) {

	}

	public record PropertyValue(String property,
						 Object value) {

	}
}
