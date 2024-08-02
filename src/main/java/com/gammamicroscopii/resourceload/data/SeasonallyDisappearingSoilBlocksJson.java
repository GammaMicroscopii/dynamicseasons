package com.gammamicroscopii.resourceload.data;

import com.gammamicroscopii.DynamicSeasons;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;

public class SeasonallyDisappearingSoilBlocksJson implements SimpleSynchronousResourceReloadListener {

	private static final HashMap<Identifier, Pair<Float, Float>> SEASONALLY_DISAPPEARING_SOIL_BLOCKS = new HashMap<>();

	/**
	 * @return LEFT = start of vanishing season; RIGHT = end of vanishing season
	 */
	public static Pair<Float, Float> getSeasonPair(Identifier blockId) {
		return SEASONALLY_DISAPPEARING_SOIL_BLOCKS.get(blockId);
	}

	@Override
	public Identifier getFabricId() {
		return new Identifier(DynamicSeasons.MOD_ID, "seasonally_disappearing_soil_blocks");
	}

	@Override
	public void reload(ResourceManager manager) {
		SEASONALLY_DISAPPEARING_SOIL_BLOCKS.clear();
		try {
			Resource resource = manager.getResourceOrThrow(new Identifier("seasonally_disappearing_soil_blocks.json"));

			SeasonallyDisappearingSoilBlockJson[] sdsbs = SeasonallyDisappearingSoilBlockJson.fromJson(JsonParser.parseReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)));

			for (SeasonallyDisappearingSoilBlockJson sdsb: sdsbs) {
				SEASONALLY_DISAPPEARING_SOIL_BLOCKS.put(new Identifier(sdsb.id()), new Pair<>(sdsb.startingSeason(), sdsb.endSeason()));
			}
			/*resource.forEach((id, resource) -> {
				String[] split = id.getPath().split("/");
				Identifier blockCycleId = new Identifier(id.getNamespace(), split[split.length-1].replace(".json", ""));
				try {
					SeasonalBlockCycle blockCycle = SeasonalBlockCycle.fromJson(JsonParser.parseReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)));
					SEASONAL_BLOCK_CYCLE_MAP.put(blockCycleId, blockCycle);
				} catch (Exception e) {
					DynamicSeasons.LOGGER.error("Error trying to register block cycle: "+blockCycleId, e);
				}
			});*/
		} catch (Exception e) {
			DynamicSeasons.LOGGER.error("Error trying to register SDSBs: ", e);
		}
		if (!SEASONALLY_DISAPPEARING_SOIL_BLOCKS.isEmpty()) {
			DynamicSeasons.LOGGER.info("Seasonally disappearing soil blocks loaded successfully\n");

		} else {
			DynamicSeasons.LOGGER.error("SDSBs map is empty!");
		}
	}


	public record SeasonallyDisappearingSoilBlockJson(String id, float startingSeason, float endSeason) {

		static SeasonallyDisappearingSoilBlockJson[] fromJson(JsonElement jsEl) {

			List<JsonElement> sdsbs = jsEl.getAsJsonObject().getAsJsonArray("values").asList();

			SeasonallyDisappearingSoilBlockJson[] finalArray = new SeasonallyDisappearingSoilBlockJson[sdsbs.size()];
			int i = 0;
			JsonObject jsOb;
			for (JsonElement sdsb : sdsbs) {
				jsOb = sdsb.getAsJsonObject();
				finalArray[i++] = new SeasonallyDisappearingSoilBlockJson(jsOb.get("block").getAsString(), jsOb.get("start_season").getAsFloat(), jsOb.get("end_season").getAsFloat());
			}
			return finalArray;
		}

	}
}
