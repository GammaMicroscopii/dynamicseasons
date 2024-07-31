package com.gammamicroscopii.resourceload.data;

import com.gammamicroscopii.DynamicSeasons;
import com.google.gson.JsonParser;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class BiomeClimates implements SimpleSynchronousResourceReloadListener {

	public static final BiomeClimate DEFAULT_CLIMATE = new BiomeClimate(15.0f, 0f, 0f, 0f, 0.5f);

	private static final HashMap<Identifier, BiomeClimate> CLIMATE_MAP = new HashMap<>();

	public static HashMap<Identifier, BiomeClimate> getClimateMap() {
		return CLIMATE_MAP;
	}

	public static BiomeClimate getBiomeClimate(Identifier biomeId) {
		return CLIMATE_MAP.getOrDefault(biomeId, DEFAULT_CLIMATE);
	}

	public static boolean areClimatesStillUnloaded() {
		return CLIMATE_MAP.isEmpty();
	}

	@Override
	public Identifier getFabricId() {
		return new Identifier(DynamicSeasons.MOD_ID, "biome_climates");
	}

	@Override
	public void reload(ResourceManager manager) {
		CLIMATE_MAP.clear();

		try {
			Map<Identifier, Resource> resources = manager.findResources("biome_climates", id -> id.getPath().endsWith(".json"));
			resources.forEach((id, resource) -> {
				String[] split = id.getPath().split("/");
				Identifier biomeId = new Identifier(id.getNamespace(), split[split.length-1].replace(".json", ""));
				try {
					BiomeClimate climate = BiomeClimate.fromJson(JsonParser.parseReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)));
					CLIMATE_MAP.put(biomeId, climate);
				} catch (Exception e) {
					DynamicSeasons.LOGGER.error("Error trying to register climate for biome: "+biomeId, e);
				}
			});
		} catch (Exception e) {
			DynamicSeasons.LOGGER.error("Error trying to register biome climates: ", e);
		}
		if (!CLIMATE_MAP.isEmpty()) {
			DynamicSeasons.LOGGER.info("Biome climates loaded successfully\n");
			//climateMap.forEach((identifier, biomeClimate) -> DynamicSeasons.LOGGER.info("Climate for biome \""+identifier+"\": "+biomeClimate.toString()));

		} else {
			DynamicSeasons.LOGGER.error("Biome climates map is empty!");
		}
	}
}