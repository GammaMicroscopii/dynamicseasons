package com.gammamicroscopii.sounds;

import com.gammamicroscopii.DynamicSeasons;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;

public class ModSounds {

	public static final SoundEvent BRANCHES_BREAK = registerSoundEvent("branches_break");
	public static final SoundEvent BRANCHES_FALL = registerSoundEvent("branches_fall");
	public static final SoundEvent BRANCHES_HIT = registerSoundEvent("branches_hit");
	public static final SoundEvent BRANCHES_PLACE = registerSoundEvent("branches_place");
	public static final SoundEvent BRANCHES_STEP = registerSoundEvent("branches_step");

	public static final SoundEvent LEAFY_BRANCHES_BREAK = registerSoundEvent("leafy_branches_break");
	public static final SoundEvent LEAFY_BRANCHES_FALL = registerSoundEvent("leafy_branches_fall");
	public static final SoundEvent LEAFY_BRANCHES_HIT = registerSoundEvent("leafy_branches_hit");
	public static final SoundEvent LEAFY_BRANCHES_PLACE = registerSoundEvent("leafy_branches_place");
	public static final SoundEvent LEAFY_BRANCHES_STEP = registerSoundEvent("leafy_branches_step");

	public static final SoundEvent BLOSSOMING_CHERRY_BRANCHES_BREAK = registerSoundEvent("blossoming_cherry_branches_break");
	public static final SoundEvent BLOSSOMING_CHERRY_BRANCHES_FALL = registerSoundEvent("blossoming_cherry_branches_fall");
	public static final SoundEvent BLOSSOMING_CHERRY_BRANCHES_HIT = registerSoundEvent("blossoming_cherry_branches_hit");
	public static final SoundEvent BLOSSOMING_CHERRY_BRANCHES_PLACE = registerSoundEvent("blossoming_cherry_branches_place");
	public static final SoundEvent BLOSSOMING_CHERRY_BRANCHES_STEP = registerSoundEvent("blossoming_cherry_branches_step");

	public static final SoundEvent DEW_BREAK = registerSoundEvent("dew_break");
	public static final SoundEvent DEW_FALL = registerSoundEvent("dew_fall");
	public static final SoundEvent DEW_HIT = registerSoundEvent("dew_hit");
	public static final SoundEvent DEW_PLACE = registerSoundEvent("dew_place");
	public static final SoundEvent DEW_STEP = registerSoundEvent("dew_step");

	public static final SoundEvent FROST_BREAK = registerSoundEvent("frost_break");
	public static final SoundEvent FROST_FALL = registerSoundEvent("frost_fall");
	public static final SoundEvent FROST_HIT = registerSoundEvent("frost_hit");
	public static final SoundEvent FROST_PLACE = registerSoundEvent("frost_place");
	public static final SoundEvent FROST_STEP = registerSoundEvent("frost_step");

	public static final SoundEvent FALLEN_LEAVES_BREAK = registerSoundEvent("fallen_leaves_break");
	public static final SoundEvent FALLEN_LEAVES_FALL = registerSoundEvent("fallen_leaves_fall");
	public static final SoundEvent FALLEN_LEAVES_HIT = registerSoundEvent("fallen_leaves_hit");
	public static final SoundEvent FALLEN_LEAVES_PLACE = registerSoundEvent("fallen_leaves_place");
	public static final SoundEvent FALLEN_LEAVES_STEP = registerSoundEvent("fallen_leaves_step");

	public static final SoundEvent DEFLOWERING_CHERRY_LEAVES_BREAK = registerSoundEvent("deflowering_cherry_leaves_break");
	public static final SoundEvent DEFLOWERING_CHERRY_LEAVES_FALL = registerSoundEvent("deflowering_cherry_leaves_fall");
	public static final SoundEvent DEFLOWERING_CHERRY_LEAVES_HIT = registerSoundEvent("deflowering_cherry_leaves_hit");
	public static final SoundEvent DEFLOWERING_CHERRY_LEAVES_PLACE = registerSoundEvent("deflowering_cherry_leaves_place");
	public static final SoundEvent DEFLOWERING_CHERRY_LEAVES_STEP = registerSoundEvent("deflowering_cherry_leaves_step");

	public static final SoundEvent SNOWY_GRASS_BREAK = registerSoundEvent("snowy_grass_break");
	public static final SoundEvent SNOWY_GRASS_FALL = registerSoundEvent("snowy_grass_fall");
	public static final SoundEvent SNOWY_GRASS_HIT = registerSoundEvent("snowy_grass_hit");
	public static final SoundEvent SNOWY_GRASS_PLACE = registerSoundEvent("snowy_grass_place");
	public static final SoundEvent SNOWY_GRASS_STEP = registerSoundEvent("snowy_grass_step");

	public static final BlockSoundGroup BRANCHES = new BlockSoundGroup(1f, 1f, BRANCHES_BREAK, BRANCHES_STEP, BRANCHES_PLACE, BRANCHES_HIT, BRANCHES_FALL);
	public static final BlockSoundGroup LEAFY_BRANCHES = new BlockSoundGroup(1f, 1f, LEAFY_BRANCHES_BREAK, LEAFY_BRANCHES_STEP, LEAFY_BRANCHES_PLACE, LEAFY_BRANCHES_HIT, LEAFY_BRANCHES_FALL);
	public static final BlockSoundGroup BLOSSOMING_CHERRY_BRANCHES = new BlockSoundGroup(1f, 1f, BLOSSOMING_CHERRY_BRANCHES_BREAK, BLOSSOMING_CHERRY_BRANCHES_STEP, BLOSSOMING_CHERRY_BRANCHES_PLACE, BLOSSOMING_CHERRY_BRANCHES_HIT, BLOSSOMING_CHERRY_BRANCHES_FALL);
	public static final BlockSoundGroup DEW = new BlockSoundGroup(1f, 1f, DEW_BREAK, SoundEvents.INTENTIONALLY_EMPTY, DEW_PLACE, SoundEvents.INTENTIONALLY_EMPTY, SoundEvents.INTENTIONALLY_EMPTY);
	public static final BlockSoundGroup FROST = new BlockSoundGroup(1f, 1f, FROST_BREAK, FROST_STEP, FROST_PLACE, FROST_HIT, FROST_FALL);
	public static final BlockSoundGroup FALLEN_LEAVES = new BlockSoundGroup(1f, 1f, FALLEN_LEAVES_BREAK, FALLEN_LEAVES_STEP, FALLEN_LEAVES_PLACE, FALLEN_LEAVES_HIT, FALLEN_LEAVES_FALL);
	public static final BlockSoundGroup DEFLOWERING_CHERRY_LEAVES = new BlockSoundGroup(1f, 1f, DEFLOWERING_CHERRY_LEAVES_BREAK, DEFLOWERING_CHERRY_LEAVES_STEP, DEFLOWERING_CHERRY_LEAVES_PLACE, DEFLOWERING_CHERRY_LEAVES_HIT, DEFLOWERING_CHERRY_LEAVES_FALL);
	public static final BlockSoundGroup SNOWY_GRASS = new BlockSoundGroup(1f, 1f, SNOWY_GRASS_BREAK, SNOWY_GRASS_STEP, SNOWY_GRASS_PLACE, SNOWY_GRASS_HIT, SNOWY_GRASS_FALL);


	private static SoundEvent registerSoundEvent(String name) {
		Identifier id = new Identifier(DynamicSeasons.MOD_ID, name);
		return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
	}

	public static void registerModSounds() {
		DynamicSeasons.LOGGER.info("Registering sounds for mod "+DynamicSeasons.MOD_ID);
	}

}
