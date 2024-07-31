package com.gammamicroscopii.particle;

import com.gammamicroscopii.DynamicSeasons;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModParticles {

	public static final SimpleParticleType BIRCH_FULL_LONG = FabricParticleTypes.simple();
	public static final SimpleParticleType BIRCH_FULL = FabricParticleTypes.simple();
	public static final SimpleParticleType BIRCH_HALF_LONG = FabricParticleTypes.simple();
	public static final SimpleParticleType BIRCH_HALF = FabricParticleTypes.simple();
	public static final SimpleParticleType BIRCH_SHORTEST = FabricParticleTypes.simple();
	public static final SimpleParticleType CHERRY_FULL_LONG = FabricParticleTypes.simple();
	public static final SimpleParticleType CHERRY_FULL = FabricParticleTypes.simple();
	public static final SimpleParticleType CHERRY_HALF_LONG = FabricParticleTypes.simple();
	public static final SimpleParticleType CHERRY_HALF = FabricParticleTypes.simple();
	public static final SimpleParticleType CHERRY_SHORTEST = FabricParticleTypes.simple();
	public static final SimpleParticleType DARK_OAK_FULL_LONG = FabricParticleTypes.simple();
	public static final SimpleParticleType DARK_OAK_FULL = FabricParticleTypes.simple();
	public static final SimpleParticleType DARK_OAK_HALF_LONG = FabricParticleTypes.simple();
	public static final SimpleParticleType DARK_OAK_HALF = FabricParticleTypes.simple();
	public static final SimpleParticleType DARK_OAK_SHORTEST = FabricParticleTypes.simple();
	public static final SimpleParticleType OAK_FULL_LONG = FabricParticleTypes.simple();
	public static final SimpleParticleType OAK_FULL = FabricParticleTypes.simple();
	public static final SimpleParticleType OAK_HALF_LONG = FabricParticleTypes.simple();
	public static final SimpleParticleType OAK_HALF = FabricParticleTypes.simple();
	public static final SimpleParticleType OAK_SHORTEST = FabricParticleTypes.simple();
	public static final SimpleParticleType DRIPPING_DEW = FabricParticleTypes.simple();

	public static final SimpleParticleType[] BIRCH_PARTICLES = {BIRCH_FULL_LONG, BIRCH_FULL, BIRCH_HALF_LONG, BIRCH_HALF, BIRCH_SHORTEST};
	public static final SimpleParticleType[] CHERRY_PARTICLES = {CHERRY_FULL_LONG, CHERRY_FULL, CHERRY_HALF_LONG, CHERRY_HALF, CHERRY_SHORTEST};
	public static final SimpleParticleType[] DARK_OAK_PARTICLES = {DARK_OAK_FULL_LONG, DARK_OAK_FULL, DARK_OAK_HALF_LONG, DARK_OAK_HALF, DARK_OAK_SHORTEST};
	public static final SimpleParticleType[] OAK_PARTICLES = {OAK_FULL_LONG, OAK_FULL, OAK_HALF_LONG, OAK_HALF, OAK_SHORTEST};

	public static void registerModParticles() {
		DynamicSeasons.LOGGER.info("Registering particle for "+DynamicSeasons.MOD_ID);
		Registry.register(Registries.PARTICLE_TYPE, new Identifier(DynamicSeasons.MOD_ID, "birch_full_long"), BIRCH_FULL_LONG);
		Registry.register(Registries.PARTICLE_TYPE, new Identifier(DynamicSeasons.MOD_ID, "birch_full"), BIRCH_FULL);
		Registry.register(Registries.PARTICLE_TYPE, new Identifier(DynamicSeasons.MOD_ID, "birch_half_long"), BIRCH_HALF_LONG);
		Registry.register(Registries.PARTICLE_TYPE, new Identifier(DynamicSeasons.MOD_ID, "birch_half"), BIRCH_HALF);
		Registry.register(Registries.PARTICLE_TYPE, new Identifier(DynamicSeasons.MOD_ID, "birch_shortest"), BIRCH_SHORTEST);
		Registry.register(Registries.PARTICLE_TYPE, new Identifier(DynamicSeasons.MOD_ID, "cherry_full_long"), CHERRY_FULL_LONG);
		Registry.register(Registries.PARTICLE_TYPE, new Identifier(DynamicSeasons.MOD_ID, "cherry_full"), CHERRY_FULL);
		Registry.register(Registries.PARTICLE_TYPE, new Identifier(DynamicSeasons.MOD_ID, "cherry_half_long"), CHERRY_HALF_LONG);
		Registry.register(Registries.PARTICLE_TYPE, new Identifier(DynamicSeasons.MOD_ID, "cherry_half"), CHERRY_HALF);
		Registry.register(Registries.PARTICLE_TYPE, new Identifier(DynamicSeasons.MOD_ID, "cherry_shortest"), CHERRY_SHORTEST);
		Registry.register(Registries.PARTICLE_TYPE, new Identifier(DynamicSeasons.MOD_ID, "dark_oak_full_long"), DARK_OAK_FULL_LONG);
		Registry.register(Registries.PARTICLE_TYPE, new Identifier(DynamicSeasons.MOD_ID, "dark_oak_full"), DARK_OAK_FULL);
		Registry.register(Registries.PARTICLE_TYPE, new Identifier(DynamicSeasons.MOD_ID, "dark_oak_half_long"), DARK_OAK_HALF_LONG);
		Registry.register(Registries.PARTICLE_TYPE, new Identifier(DynamicSeasons.MOD_ID, "dark_oak_half"), DARK_OAK_HALF);
		Registry.register(Registries.PARTICLE_TYPE, new Identifier(DynamicSeasons.MOD_ID, "dark_oak_shortest"), DARK_OAK_SHORTEST);
		Registry.register(Registries.PARTICLE_TYPE, new Identifier(DynamicSeasons.MOD_ID, "oak_full_long"), OAK_FULL_LONG);
		Registry.register(Registries.PARTICLE_TYPE, new Identifier(DynamicSeasons.MOD_ID, "oak_full"), OAK_FULL);
		Registry.register(Registries.PARTICLE_TYPE, new Identifier(DynamicSeasons.MOD_ID, "oak_half_long"), OAK_HALF_LONG);
		Registry.register(Registries.PARTICLE_TYPE, new Identifier(DynamicSeasons.MOD_ID, "oak_half"), OAK_HALF);
		Registry.register(Registries.PARTICLE_TYPE, new Identifier(DynamicSeasons.MOD_ID, "oak_shortest"), OAK_SHORTEST);
		Registry.register(Registries.PARTICLE_TYPE, new Identifier(DynamicSeasons.MOD_ID, "dripping_dew"), DRIPPING_DEW);
	}

}
