package com.gammamicroscopii.mixin.client;

import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.sound.BiomeMoodSound;
import net.minecraft.sound.MusicSound;
import net.minecraft.world.biome.*;
import net.minecraft.world.gen.carver.ConfiguredCarver;
import net.minecraft.world.gen.feature.PlacedFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.function.Consumer;

@Mixin(OverworldBiomeCreator.class)
public class OverworldBiomeCreatorMixin {

	@Inject(method = "createSwamp", at = @At(value = "RETURN"), locals = LocalCapture.CAPTURE_FAILEXCEPTION, cancellable = true)
	public void eraseSwampSpecialGrassColorRules(RegistryEntryLookup<PlacedFeature> featureLookup, RegistryEntryLookup<ConfiguredCarver<?>> carverLookup, Consumer<SpawnSettings.Builder> additionalSpawnsAdder, CallbackInfoReturnable<Biome> cir, SpawnSettings.Builder builder, GenerationSettings.LookupBackedBuilder lookupBackedBuilder, MusicSound musicSound) {
		//cir.setReturnValue(new Biome.Builder().precipitation(true).temperature(0.8f).downfall(0.9f).effects(new BiomeEffects.Builder().waterColor(6388580).waterFogColor(2302743).fogColor(12638463).skyColor(OverworldBiomeCreator.getSkyColor(0.8f)).foliageColor(6975545).grassColor(5382182).moodSound(BiomeMoodSound.CAVE).music(musicSound).build()).spawnSettings(builder.build()).generationSettings(lookupBackedBuilder.build()).build());
		cir.cancel();

	}

	@Inject(method = "createMangroveSwamp", at = @At(value = "RETURN"), locals = LocalCapture.CAPTURE_FAILEXCEPTION, cancellable = true)
	public void eraseMangroveSwampSpecialGrassColorRules(RegistryEntryLookup<PlacedFeature> featureLookup, RegistryEntryLookup<ConfiguredCarver<?>> carverLookup, Consumer<SpawnSettings.Builder> additionalSpawnsAdder, CallbackInfoReturnable<Biome> cir, SpawnSettings.Builder builder, GenerationSettings.LookupBackedBuilder lookupBackedBuilder, MusicSound musicSound) {
		cir.setReturnValue(new Biome.Builder().precipitation(true).temperature(0.8f).downfall(0.9f).effects(new BiomeEffects.Builder().waterColor(3832426).waterFogColor(5077600).fogColor(12638463).skyColor(OverworldBiomeCreator.getSkyColor(0.8f)).foliageColor(9285927).grassColor(5382182).moodSound(BiomeMoodSound.CAVE).music(musicSound).build()).spawnSettings(builder.build()).generationSettings(lookupBackedBuilder.build()).build());
		cir.cancel();
	}

}
