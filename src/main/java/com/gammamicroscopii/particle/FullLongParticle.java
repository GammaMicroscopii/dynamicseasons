package com.gammamicroscopii.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class FullLongParticle extends FallingLeafParticle{

	protected FullLongParticle(ClientWorld clientWorld, double x, double y, double z, double xd, double yd, double zd, SpriteProvider spriteProvider) {
		super(clientWorld, x, y, z, xd, yd, zd, spriteProvider);
		maxAge = 36;
	}

	public static class Factory implements ParticleFactory<SimpleParticleType> {

		private final SpriteProvider spriteProvider;

		public Factory(SpriteProvider spriteProvider) {
			this.spriteProvider = spriteProvider;
		}

		@Nullable
		@Override
		public Particle createParticle(SimpleParticleType parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
			return new FullLongParticle(world, x,y,z, 0.0, 0.0, 0.0, this.spriteProvider);
		}
	}
}
