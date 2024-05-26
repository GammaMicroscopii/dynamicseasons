package com.gammamicroscopii;

import com.gammamicroscopii.item.ModItems;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class ModItemModelPredicateProvider {

	public static void registerPredicates() {
		DynamicSeasons.LOGGER.info("Registering item model predicates for mod "+DynamicSeasons.MOD_ID);

		ModelPredicateProviderRegistry.register(ModItems.CALENDAR, new Identifier("season"), (itemStack, clientWorld, livingEntity, seed) -> {
			float years = (float)clientWorld.getTimeOfDay() / DynamicSeasons.YEAR_DURATION;
			return years - (int) years;
		});

		ModelPredicateProviderRegistry.register(ModItems.THERMOMETER, new Identifier("displayed_temperature"), (itemStack, clientWorld, livingEntity, seed) -> {
			Entity entity = livingEntity == null ? itemStack.getHolder() : livingEntity;
			if (entity == null) {
				return 0.0f;
			}
			return (45.0f + Temperature.of(clientWorld.getBiome(BlockPos.ofFloored(entity.getPos())).value().getTemperature(), Temperature.Unit.VANILLA).get()) / 130f;
		});
	}

}
