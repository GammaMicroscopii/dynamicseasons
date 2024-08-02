package com.gammamicroscopii.block;

import com.gammamicroscopii.DynamicSeasons;
import com.gammamicroscopii.sounds.ModSounds;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

public class ModBlocks {

	public static final Block DISCOLORED_BIRCH_LEAVES = registerBlock("discolored_birch_leaves", Blocks.createLeavesBlock(BlockSoundGroup.GRASS));
	public static final Block DISCOLORED_CHERRY_LEAVES = registerBlock("discolored_cherry_leaves", Blocks.createLeavesBlock(BlockSoundGroup.GRASS));
	public static final Block DISCOLORED_DARK_OAK_LEAVES = registerBlock("discolored_dark_oak_leaves", Blocks.createLeavesBlock(BlockSoundGroup.GRASS));
	public static final Block DISCOLORED_OAK_LEAVES = registerBlock("discolored_oak_leaves", Blocks.createLeavesBlock(BlockSoundGroup.GRASS));
	public static final Block UNSTABLE_BIRCH_LEAVES = registerBlock("unstable_birch_leaves", new ParticlesBirchLeavesBlock(BlockSoundGroup.GRASS));
	public static final Block UNSTABLE_CHERRY_LEAVES = registerBlock("unstable_cherry_leaves", new ParticlesCherryLeavesBlock(BlockSoundGroup.GRASS));
	public static final Block UNSTABLE_DARK_OAK_LEAVES = registerBlock("unstable_dark_oak_leaves", new ParticlesDarkOakLeavesBlock(BlockSoundGroup.GRASS));
	public static final Block UNSTABLE_OAK_LEAVES = registerBlock("unstable_oak_leaves", new ParticlesOakLeavesBlock(BlockSoundGroup.GRASS));
	public static final Block FALLING_BIRCH_LEAVES = registerBlock("falling_birch_leaves", new ParticlesBirchLeavesBlock(ModSounds.LEAFY_BRANCHES));
	public static final Block FALLING_CHERRY_LEAVES = registerBlock("falling_cherry_leaves", new ParticlesCherryLeavesBlock(ModSounds.LEAFY_BRANCHES));
	public static final Block FALLING_DARK_OAK_LEAVES = registerBlock("falling_dark_oak_leaves", new ParticlesDarkOakLeavesBlock(ModSounds.LEAFY_BRANCHES));
	public static final Block FALLING_OAK_LEAVES = registerBlock("falling_oak_leaves", new ParticlesOakLeavesBlock(ModSounds.LEAFY_BRANCHES));
	public static final Block BIRCH_BRANCHES = registerBlock("birch_branches", Blocks.createLeavesBlock(ModSounds.BRANCHES));
	public static final Block CHERRY_BRANCHES = registerBlock("cherry_branches", Blocks.createLeavesBlock(ModSounds.BRANCHES));
	public static final Block DARK_OAK_BRANCHES = registerBlock("dark_oak_branches", Blocks.createLeavesBlock(ModSounds.BRANCHES));
	public static final Block OAK_BRANCHES = registerBlock("oak_branches", Blocks.createLeavesBlock(ModSounds.BRANCHES));
	public static final Block LEAFING_OUT_BIRCH_BRANCHES = registerBlock("leafing_out_birch_branches", Blocks.createLeavesBlock(ModSounds.LEAFY_BRANCHES));
	public static final Block BLOSSOMING_CHERRY_BRANCHES = registerBlock("blossoming_cherry_branches", Blocks.createLeavesBlock(ModSounds.BLOSSOMING_CHERRY_BRANCHES));
	public static final Block LEAFING_OUT_DARK_OAK_BRANCHES = registerBlock("leafing_out_dark_oak_branches", Blocks.createLeavesBlock(ModSounds.LEAFY_BRANCHES));
	public static final Block LEAFING_OUT_OAK_BRANCHES = registerBlock("leafing_out_oak_branches", Blocks.createLeavesBlock(ModSounds.LEAFY_BRANCHES));
	public static final Block FLOWERLESS_CHERRY_LEAVES = registerBlock("flowerless_cherry_leaves", Blocks.createLeavesBlock(BlockSoundGroup.GRASS));
	public static final Block DEW = registerBlock("dew", new DewBlock(AbstractBlock.Settings.create().noCollision().nonOpaque().replaceable().breakInstantly().dropsNothing().pistonBehavior(PistonBehavior.DESTROY).ticksRandomly().sounds(ModSounds.DEW)));
	public static final Block FROST = registerBlock("frost", new CondensationBlock(AbstractBlock.Settings.create().noCollision().nonOpaque().replaceable().breakInstantly().pistonBehavior(PistonBehavior.DESTROY).ticksRandomly().sounds(ModSounds.FROST)));
	public static final Block FALLEN_LEAVES = registerBlock("fallen_leaves", new FallenLeavesBlock(AbstractBlock.Settings.create().nonOpaque().velocityMultiplier(0.9f).strength(0.1f, 0f).sounds(ModSounds.FALLEN_LEAVES)));
	
	private static Block registerBlock(String name, Block block) {
		return Registry.register(Registries.BLOCK, new Identifier(DynamicSeasons.MOD_ID, name), block);
	}

	private static Item registerBlockItem(String name, Block block) {
		return Registry.register(Registries.ITEM, new Identifier(DynamicSeasons.MOD_ID, name), new BlockItem(block, new Item.Settings()));
	}

	public static void registerModBlocks() {
		DynamicSeasons.LOGGER.info("Registering blocks for mod "+DynamicSeasons.MOD_ID);

		ItemGroupEvents.modifyEntriesEvent(ItemGroups.NATURAL).register(entries -> entries.add(DISCOLORED_BIRCH_LEAVES));
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.NATURAL).register(entries -> entries.add(DISCOLORED_CHERRY_LEAVES));
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.NATURAL).register(entries -> entries.add(DISCOLORED_DARK_OAK_LEAVES));
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.NATURAL).register(entries -> entries.add(DISCOLORED_OAK_LEAVES));
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.NATURAL).register(entries -> entries.add(UNSTABLE_BIRCH_LEAVES));
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.NATURAL).register(entries -> entries.add(UNSTABLE_CHERRY_LEAVES));
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.NATURAL).register(entries -> entries.add(UNSTABLE_DARK_OAK_LEAVES));
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.NATURAL).register(entries -> entries.add(UNSTABLE_OAK_LEAVES));
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.NATURAL).register(entries -> entries.add(FALLING_BIRCH_LEAVES));
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.NATURAL).register(entries -> entries.add(FALLING_CHERRY_LEAVES));
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.NATURAL).register(entries -> entries.add(FALLING_DARK_OAK_LEAVES));
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.NATURAL).register(entries -> entries.add(FALLING_OAK_LEAVES));
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.NATURAL).register(entries -> entries.add(BIRCH_BRANCHES));
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.NATURAL).register(entries -> entries.add(CHERRY_BRANCHES));
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.NATURAL).register(entries -> entries.add(DARK_OAK_BRANCHES));
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.NATURAL).register(entries -> entries.add(OAK_BRANCHES));
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.NATURAL).register(entries -> entries.add(LEAFING_OUT_BIRCH_BRANCHES));
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.NATURAL).register(entries -> entries.add(BLOSSOMING_CHERRY_BRANCHES));
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.NATURAL).register(entries -> entries.add(LEAFING_OUT_DARK_OAK_BRANCHES));
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.NATURAL).register(entries -> entries.add(LEAFING_OUT_OAK_BRANCHES));
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.NATURAL).register(entries -> entries.add(FLOWERLESS_CHERRY_LEAVES));
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.NATURAL).register(entries -> entries.add(DEW));
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.NATURAL).register(entries -> entries.add(FROST));
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.NATURAL).register(entries -> entries.add(FALLEN_LEAVES));
	}

	public static class Tags {
		private Tags() {}

		public static final TagKey<Block> ORGANIC_SOIL = TagKey.of(RegistryKeys.BLOCK, new Identifier(DynamicSeasons.MOD_ID, "organic_soil"));
	}

	public static class Items {

		private Items() {}

		public static final Item DISCOLORED_BIRCH_LEAVES = registerBlockItem("discolored_birch_leaves", ModBlocks.DISCOLORED_BIRCH_LEAVES);
		public static final Item DISCOLORED_CHERRY_LEAVES = registerBlockItem("discolored_cherry_leaves", ModBlocks.DISCOLORED_CHERRY_LEAVES);
		public static final Item DISCOLORED_DARK_OAK_LEAVES = registerBlockItem("discolored_dark_oak_leaves", ModBlocks.DISCOLORED_DARK_OAK_LEAVES);
		public static final Item DISCOLORED_OAK_LEAVES = registerBlockItem("discolored_oak_leaves", ModBlocks.DISCOLORED_OAK_LEAVES);
		public static final Item UNSTABLE_BIRCH_LEAVES = registerBlockItem("unstable_birch_leaves", ModBlocks.UNSTABLE_BIRCH_LEAVES);
		public static final Item UNSTABLE_CHERRY_LEAVES = registerBlockItem("unstable_cherry_leaves", ModBlocks.UNSTABLE_CHERRY_LEAVES);
		public static final Item UNSTABLE_DARK_OAK_LEAVES = registerBlockItem("unstable_dark_oak_leaves", ModBlocks.UNSTABLE_DARK_OAK_LEAVES);
		public static final Item UNSTABLE_OAK_LEAVES = registerBlockItem("unstable_oak_leaves", ModBlocks.UNSTABLE_OAK_LEAVES);
		public static final Item FALLING_BIRCH_LEAVES = registerBlockItem("falling_birch_leaves", ModBlocks.FALLING_BIRCH_LEAVES);
		public static final Item FALLING_CHERRY_LEAVES = registerBlockItem("falling_cherry_leaves", ModBlocks.FALLING_CHERRY_LEAVES);
		public static final Item FALLING_DARK_OAK_LEAVES = registerBlockItem("falling_dark_oak_leaves", ModBlocks.FALLING_DARK_OAK_LEAVES);
		public static final Item FALLING_OAK_LEAVES = registerBlockItem("falling_oak_leaves", ModBlocks.FALLING_OAK_LEAVES);
		public static final Item BIRCH_BRANCHES = registerBlockItem("birch_branches", ModBlocks.BIRCH_BRANCHES);
		public static final Item CHERRY_BRANCHES = registerBlockItem("cherry_branches", ModBlocks.CHERRY_BRANCHES);
		public static final Item DARK_OAK_BRANCHES = registerBlockItem("dark_oak_branches", ModBlocks.DARK_OAK_BRANCHES);
		public static final Item OAK_BRANCHES = registerBlockItem("oak_branches", ModBlocks.OAK_BRANCHES);
		public static final Item LEAFING_OUT_BIRCH_BRANCHES = registerBlockItem("leafing_out_birch_branches", ModBlocks.LEAFING_OUT_BIRCH_BRANCHES);
		public static final Item BLOSSOMING_CHERRY_BRANCHES = registerBlockItem("blossoming_cherry_branches", ModBlocks.BLOSSOMING_CHERRY_BRANCHES);
		public static final Item LEAFING_OUT_DARK_OAK_BRANCHES = registerBlockItem("leafing_out_dark_oak_branches", ModBlocks.LEAFING_OUT_DARK_OAK_BRANCHES);
		public static final Item LEAFING_OUT_OAK_BRANCHES = registerBlockItem("leafing_out_oak_branches", ModBlocks.LEAFING_OUT_OAK_BRANCHES);
		public static final Item FLOWERLESS_CHERRY_LEAVES = registerBlockItem("flowerless_cherry_leaves", ModBlocks.FLOWERLESS_CHERRY_LEAVES);
		public static final Item DEW = registerBlockItem("dew", ModBlocks.DEW);
		public static final Item FROST = registerBlockItem("frost", ModBlocks.FROST);
		public static final Item FALLEN_LEAVES = registerBlockItem("fallen_leaves", ModBlocks.FALLEN_LEAVES);
	
	}

}
