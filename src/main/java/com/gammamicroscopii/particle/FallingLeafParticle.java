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
public class FallingLeafParticle extends SpriteBillboardParticle {

	protected float ageIncreaseChance;
	protected int actualMaxAge;
	protected SpriteProvider spriteProvider;
	ClientWorld clientWorld;

	protected FallingLeafParticle(ClientWorld clientWorld, double x, double y, double z, double xd, double yd, double zd, SpriteProvider spriteProvider) {
		super(clientWorld, x, y, z, xd, yd, zd);
		this.clientWorld = clientWorld;
		this.spriteProvider = spriteProvider;
		this.setSprite(spriteProvider.getSprite(random));
		this.gravityStrength = 0.00225f + 0.00075f * random.nextFloat();
		this.scale = 0.2f + random.nextFloat() * 0.075f;
		ageIncreaseChance = 0.6f + random.nextFloat() * 0.3f;
		actualMaxAge = 100 + (int)(random.nextFloat() * 200);
		this.angle = 0f;
		this.prevAngle = 0f;
		this.velocityY = 0f;
	}

	@Override
	public ParticleTextureSheet getType() {
		return ParticleTextureSheet.PARTICLE_SHEET_OPAQUE;
	}

	@Override
	public void tick() {
		this.prevPosX = this.x;
		this.prevPosY = this.y;
		this.prevPosZ = this.z;
		boolean aged = false;
		if (random.nextFloat() < ageIncreaseChance) {
			age++;
			aged = true;
		}
		if (age >= actualMaxAge) {
			this.markDead();
			return;
		}
		if (age >= maxAge) {
			age-=maxAge;
		}
		if (age < 0 ) {
			age += maxAge;
		}
		this.setSprite(spriteProvider.getSprite(age, maxAge));

		this.velocityX +=  -0.0075 + 0.015 * random.nextFloat();
		this.velocityZ +=  -0.0075 + 0.015 * random.nextFloat();
		this.velocityY -= (double)this.gravityStrength;


		if (this.onGround) {
			this.markDead();
			return;
		}
		BlockPos pos = new BlockPos((int)x, (int)y, (int)z);
		if (this.world.isWater(pos)) {
			this.velocityY = Math.max(0.0, velocityY+0.0001);
			this.actualMaxAge -= 3;
			this.velocityX *= 0.5;
			this.velocityZ *= 0.5;
			if (aged) {
				age--;
			}
		}
		FluidState fluid = this.clientWorld.getBlockState(pos).getFluidState();
		if (fluid.isOf(Fluids.FLOWING_LAVA) || fluid.isOf(Fluids.FLOWING_LAVA)) {
			this.markDead();
			return;
		}
		this.move(this.velocityX, this.velocityY, this.velocityZ);
		this.velocityX *= 0.95;
		this.velocityY *= 0.9715;
		this.velocityZ *= 0.95;
	}
}
