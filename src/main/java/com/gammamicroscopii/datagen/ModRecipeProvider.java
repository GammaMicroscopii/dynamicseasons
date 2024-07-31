package com.gammamicroscopii.datagen;

import com.gammamicroscopii.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends FabricRecipeProvider {

	public ModRecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
		super(output, registriesFuture);
	}

	@Override
	public void generate(RecipeExporter exporter) {
		ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.CALENDAR, 1)
				.pattern(" c ")
				.pattern("cCc")
				.pattern(" c ")
				.input('c', Items.COPPER_INGOT).input('C', Items.CLOCK)
				.criterion(hasItem(Items.CLOCK), conditionsFromItem(Items.CLOCK))
				.criterion(hasItem(Items.COPPER_INGOT), conditionsFromItem(Items.COPPER_INGOT))
				.criterion(hasItem(Items.GOLD_INGOT), conditionsFromItem(Items.GOLD_INGOT))
				.criterion(hasItem(Items.REDSTONE), conditionsFromItem(Items.REDSTONE))
				.offerTo(exporter, new Identifier(getRecipeName(ModItems.CALENDAR)));

		ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.THERMOMETER, 1)
				.pattern("  G")
				.pattern(" A ")
				.pattern("F  ")
				.input('F', Items.FERMENTED_SPIDER_EYE).input('A', Items.AMETHYST_SHARD).input('G', Items.GLASS_PANE)
				.criterion(hasItem(Items.AMETHYST_SHARD), conditionsFromItem(Items.AMETHYST_SHARD))
				.criterion(hasItem(Items.SPIDER_EYE), conditionsFromItem(Items.SPIDER_EYE))
				.criterion(hasItem(Items.GLASS_PANE), conditionsFromItem(Items.GLASS_PANE))
				.offerTo(exporter, new Identifier(getRecipeName(ModItems.THERMOMETER)));
	}
}
