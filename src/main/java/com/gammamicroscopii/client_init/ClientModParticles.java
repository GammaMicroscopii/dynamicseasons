package com.gammamicroscopii.client_init;

import com.gammamicroscopii.particle.*;
import com.gammamicroscopii.particle.ModParticles;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;

@Environment(EnvType.CLIENT)
public class ClientModParticles {

	public static void registerClientParticles() {

		ParticleFactoryRegistry.getInstance().register(ModParticles.BIRCH_FULL_LONG, FullLongParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(ModParticles.BIRCH_FULL, FullParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(ModParticles.BIRCH_HALF_LONG, HalfLongParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(ModParticles.BIRCH_HALF, HalfParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(ModParticles.BIRCH_SHORTEST, ShortestParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(ModParticles.CHERRY_FULL_LONG, FullLongParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(ModParticles.CHERRY_FULL, FullParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(ModParticles.CHERRY_HALF_LONG, HalfLongParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(ModParticles.CHERRY_HALF, HalfParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(ModParticles.CHERRY_SHORTEST, ShortestParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(ModParticles.DARK_OAK_FULL_LONG, FullLongParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(ModParticles.DARK_OAK_FULL, FullParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(ModParticles.DARK_OAK_HALF_LONG, HalfLongParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(ModParticles.DARK_OAK_HALF, HalfParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(ModParticles.DARK_OAK_SHORTEST, ShortestParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(ModParticles.OAK_FULL_LONG, FullLongParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(ModParticles.OAK_FULL, FullParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(ModParticles.OAK_HALF_LONG, HalfLongParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(ModParticles.OAK_HALF, HalfParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(ModParticles.OAK_SHORTEST, ShortestParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(ModParticles.DRIPPING_DEW, DrippingDewParticle.Factory::new);
	}

}
