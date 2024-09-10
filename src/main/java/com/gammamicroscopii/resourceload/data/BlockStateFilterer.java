package com.gammamicroscopii.resourceload.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.state.property.Property;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class BlockStateFilterer {

	String id;
	PropertyValue[] propertyValues;

	private final HashMap<BlockState, Boolean> FILTER_CACHE = new HashMap<>();


	public BlockStateFilterer(String id, PropertyValue[] propertyValues) {
		this.id = id;
		this.propertyValues = propertyValues;
	}

	public boolean filterBlockState(BlockState blockState, String blockId) {
		if (!blockId.equals(id)) return false;
		Boolean cacheValue = FILTER_CACHE.get(blockState);
		if (cacheValue != null) return cacheValue;

		boolean b = true;
		List<Property<?>> blockStateProperties = blockState.getProperties().stream().toList();
		ArrayList<String> blockStatePropertyNames = new ArrayList<>();
		for (Property<?> property : blockStateProperties) {
			blockStatePropertyNames.add(property.getName());
		}


		for (int i = 0; i < propertyValues.length; i++) {
			int indexof = blockStatePropertyNames.indexOf(propertyValues[i].property());
			if (indexof != -1) {
				b &= Arrays.asList(propertyValues[i].affectedValues()).contains(blockState.get(blockStateProperties.get(indexof)));
			} else return false;
		}
		FILTER_CACHE.put(blockState, b);
		return b;
	}

	static BlockStateFilterer fromJson(JsonElement json) {
		JsonObject jsOb = json.getAsJsonObject();
		return new BlockStateFilterer(jsOb.get("id").getAsString(), parseProperties(jsOb.get("blockstates").getAsJsonArray().asList()));
	}

	static PropertyValue[] parseProperties(List<JsonElement> PBBSJson) {
		PropertyValue[] PBBSs = new PropertyValue[PBBSJson.size()];
		for (int i = 0; i < PBBSJson.size(); i++) {
			PBBSs[i] = parseProperty(PBBSJson.get(i));
		}
		return PBBSs;
	}

	static PropertyValue parseProperty(JsonElement jsonElement) {
		JsonObject jsOb = jsonElement.getAsJsonObject();
		return new PropertyValue(jsOb.get("property").getAsString(), converttoObjectArray(jsOb.get("affected_values").getAsJsonArray().asList()));
	}

	static Object[] converttoObjectArray(List<JsonElement> affected_values) {
		Object[] finalList = new Object[affected_values.size()];
		for (int i = 0; i < affected_values.size(); i++) {
			finalList[i] = jsonElementToBlockstateValue(affected_values.get(i));
		}
		return finalList;
	}

	static Object jsonElementToBlockstateValue(JsonElement value) {
		JsonPrimitive primitive = value.getAsJsonPrimitive();
		return primitive.isBoolean() ? primitive.getAsBoolean() : (primitive.isNumber() ? primitive.getAsInt() : primitive.getAsString());
	}


}

record PropertyValue(String property, Object[] affectedValues) {

}
