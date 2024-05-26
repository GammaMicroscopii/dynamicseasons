package com.gammamicroscopii.particle;

import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;
import org.jetbrains.annotations.Nullable;

public class HalfLongParticle  extends FallingLeafParticle{

	protected HalfLongParticle(ClientWorld clientWorld, double x, double y, double z, double xd, double yd, double zd, SpriteProvider spriteProvider) {
		super(clientWorld, x, y, z, xd, yd, zd, spriteProvider);
		maxAge = 30;
	}

	public static class Factory implements ParticleFactory<SimpleParticleType> {

		private final SpriteProvider spriteProvider;

		public Factory(SpriteProvider spriteProvider) {
			this.spriteProvider = spriteProvider;
		}

		@Nullable
		@Override
		public Particle createParticle(SimpleParticleType parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
			return new HalfLongParticle(world, x,y,z, 0.0, 0.0, 0.0, this.spriteProvider);
		}
	}
}

