package com.gammamicroscopii.resourceload.data;

import com.gammamicroscopii.DynamicSeasons;
import com.google.gson.JsonParser;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.state.property.*;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class SeasonalBlockCycles implements SimpleSynchronousResourceReloadListener {

	private static final HashMap<String, IntProperty> INT_PROPERTIES = new HashMap<>();
	private static final HashMap<String, BooleanProperty> BOOLEAN_PROPERTIES = new HashMap<>();
	//private static final HashMap<String, EnumProperty> ENUM_PROPERTIES = new HashMap<>();

	private static final HashMap<BlockState, Boolean> IS_BLOCKSTATE_AFFECTED_CACHE = new HashMap<>();
	private static final HashMap<BlockState, BlockState> NEXT_BLOCKSTATE_CACHE = new HashMap<>();

	static {
		INT_PROPERTIES.put("distance", Properties.DISTANCE_1_7);
		BOOLEAN_PROPERTIES.put("waterlogged", Properties.WATERLOGGED);
		BOOLEAN_PROPERTIES.put("persistent", Properties.PERSISTENT);
	}

	private static final HashMap<Identifier, SeasonalBlockCycle> SEASONAL_BLOCK_CYCLE_MAP = new HashMap<>();
	private static final HashMap<Identifier, Pair<SeasonalBlockCycle, Integer>> BLOCKS_IN_SEASONAL_CYCLES = new HashMap<>();

	public static HashMap<Identifier, SeasonalBlockCycle> getSeasonalBlockCycleMap() {
		return SEASONAL_BLOCK_CYCLE_MAP;
	}

	public static boolean areSeasonalCyclesStillUnloaded() {
		return SEASONAL_BLOCK_CYCLE_MAP.isEmpty();
	}

	public static boolean isBlockInAnySeasonalCycle(Identifier blockId) {
		return getBlockSeasonalCycle(blockId) != null;
	}

	/**
	 * Gets the cycle, and conversion index of it, that contains the TurnsInto referring the block with the name specified.
	 * @param blockId
	 * @return
	 */
	public static Pair<SeasonalBlockCycle, Integer> getBlockSeasonalCycle(Identifier blockId) {
		return BLOCKS_IN_SEASONAL_CYCLES.get(blockId);
	}

	public static boolean isBlockStateAffected(BlockState state, Identifier blockId) {
/*		Boolean cacheValue = IS_BLOCKSTATE_AFFECTED_CACHE.get(state);
		if (cacheValue != null) return cacheValue;

*/	//The blockID could actually correspond to ANY season interval!
		Pair<SeasonalBlockCycle, Integer> pair = getBlockSeasonalCycle(blockId);
		return pair.getLeft().conversions()[pair.getRight()].blockStateFilterer().filterBlockState(state, blockId.toString());
/*	boolean b = true;
		List<Property<?>> blockProperties = state.getProperties().stream().toList();
		//Property<?>[] properties = new Property<?>[blockProperties.size()];
		//int i = 0;
		//properties[i] = property;
		//i++;
		ArrayList<String> propertyNames = new ArrayList<>();
		for (Property<?> property : blockProperties) {
			propertyNames.add(property.getName());
		}

		for (int i = 0; i < blockPropertiesFromJson.length; i++) {
			int indexof = propertyNames.indexOf(blockPropertiesFromJson[i].property());
			if( indexof != -1){
				b &= Arrays.asList(blockPropertiesFromJson[i].affectedValues()).contains(state.get(blockProperties.get(indexof)));
			}
		}
		/*TurnsInto previousId = pair.getLeft().getPreviouslyStableBlock(season);
*///		TurnsInto stableBlockId = pair.getLeft().getStableBlock(season);*/
/*
		IS_BLOCKSTATE_AFFECTED_CACHE.put(state, b);
		return b;
*/}

	//blockId is the ID of state's block
	@SuppressWarnings("unchecked")
	public static BlockState nextBlockState(BlockState state, Identifier blockId, DynamicRegistryManager registryManager) {
		BlockState cacheValue = NEXT_BLOCKSTATE_CACHE.get(state);
		if (cacheValue != null) return cacheValue;


		Pair<SeasonalBlockCycle, Integer> pair = getBlockSeasonalCycle(blockId);
		//TurnsInto is a class of mine, which comes from Json parsing, and holds the block ID and, optionally, its states as Strings,
		//of the blockstate that should replace state at its position

		//SeasonalBlockCycle.TurnsInto turnsInto = pair.getLeft().nextConversion(pair.getRight()).turnsInto();
		SeasonalBlockCycle.TurnsInto turnsInto = pair.getLeft().conversions[pair.getRight()].turnsInto();

		Block block = registryManager.get(RegistryKeys.BLOCK).get(new Identifier(turnsInto.id()));

		//Get the block's properties through vanilla methods, which yield Property<?>
		List<Property<?>> blockProperties = block.getStateManager().getProperties().stream().toList();

		//create the blockstate that should be returned at the end, after receiving the property updates
		BlockState newState = block.getDefaultState();

		//get the TurnsInto's properties and values (as Strings) that should be applied to newState
		ArrayList<String> jsonBlockProperties = new ArrayList<>();
		for (int i = 0; i < turnsInto.propertyValues().length; i++) {
			jsonBlockProperties.add(turnsInto.propertyValues()[i].property());
		}
		//iterate the (unfortunately wildcard) Properties
		for (Property property : blockProperties) {
			int indexOf = jsonBlockProperties.indexOf(property.getName());
			if (indexOf != -1) {

				/*Object value = turnsInto.propertyValues()[indexOf].value();

				if (value instanceof Boolean) {
					newState.with(property, (Boolean) value);
				}

				newState.with(property, value);*/

				//if a property with the same name as property is found in the TurnsInto, set newState's property's value to that from the TurnsInto.
				String name = property.getName();
				BooleanProperty bp = BOOLEAN_PROPERTIES.get(name);
				if (bp != null) {
					newState = newState.with(bp, (Boolean) turnsInto.propertyValues()[indexOf].value());
				} else {
					IntProperty ip = INT_PROPERTIES.get(name);
					if(ip != null) {
						newState = newState.with(ip, (Integer) turnsInto.propertyValues()[indexOf].value());
					} else {
						throw new RuntimeException("Unregistered property: "+name);
					}
				}

			} else {
				//here is where I would fetch state's properties

				newState = state.contains(property) ? newState.with(property, state.get(property)) : newState;
			}
		}

		NEXT_BLOCKSTATE_CACHE.put(state, newState);
		return newState;
	}

	private void registerBlocks() {
		Iterator<SeasonalBlockCycle> iter = SEASONAL_BLOCK_CYCLE_MAP.values().iterator();
		SeasonalBlockCycle sbc;
		while (iter.hasNext()) {
			sbc = iter.next();
			SeasonalBlockCycle.Conversion[] conversions = sbc.conversions();
			for (int i = 0 ; i < conversions.length; i++) {
				boolean b = i == conversions.length-1;
				BLOCKS_IN_SEASONAL_CYCLES.put(new Identifier(conversions[i].turnsInto().id()), new Pair<>(sbc, b ? 0 : i+1));
			}
		}
	}

	@Override
	public Identifier getFabricId() {
		return new Identifier(DynamicSeasons.MOD_ID, "seasonal_block_cycles");
	}

	@Override
	public void reload(ResourceManager manager) {
		SEASONAL_BLOCK_CYCLE_MAP.clear();
		BLOCKS_IN_SEASONAL_CYCLES.clear();
		try {
			Map<Identifier, Resource> resources = manager.findResources("seasonal_block_cycles", id -> id.getPath().endsWith(".json"));
			resources.forEach((id, resource) -> {
				String[] split = id.getPath().split("/");
				Identifier blockCycleId = new Identifier(id.getNamespace(), split[split.length-1].replace(".json", ""));
				try {
					SeasonalBlockCycle blockCycle = SeasonalBlockCycle.fromJson(JsonParser.parseReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)));
					SEASONAL_BLOCK_CYCLE_MAP.put(blockCycleId, blockCycle);
				} catch (Exception e) {
					DynamicSeasons.LOGGER.error("Error trying to register block cycle: "+blockCycleId, e);
				}
			});
		} catch (Exception e) {
			DynamicSeasons.LOGGER.error("Error trying to register block cycles: ", e);
		}
		if (!SEASONAL_BLOCK_CYCLE_MAP.isEmpty()) {
			DynamicSeasons.LOGGER.info("Seasonal block cycles loaded successfully\n");
			registerBlocks();

		} else {
			DynamicSeasons.LOGGER.error("Seasonal block cycles map is empty!");
		}
	}
}