package com.gammamicroscopii.item;

import com.gammamicroscopii.DynamicSeasons;
import com.gammamicroscopii.Temperature;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {

	public static final Item THERMOMETER = registerItem("thermometer", new ThermometerItem(new Item.Settings().component(ThermometerItem.TEMPERATURE_COMPONENT, Temperature.ABSOLUTE_ZERO.get())));
	public static final Item CALENDAR = registerItem("calendar", new Item(new Item.Settings()));

	private static Item registerItem(String name, Item item) {
		return Registry.register(Registries.ITEM, new Identifier(DynamicSeasons.MOD_ID, name), item);
	}

	public static void registerModItems() {
		DynamicSeasons.LOGGER.info("Registering items for mod "+DynamicSeasons.MOD_ID);
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(entries -> entries.add(THERMOMETER));
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(entries -> entries.add(CALENDAR));
	}

}
