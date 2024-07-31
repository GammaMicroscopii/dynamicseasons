package com.gammamicroscopii.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class DrippingDewParticle extends SpriteBillboardParticle {

	ClientWorld clientWorld;
	MovementMode movementMode;
	final Direction attachedDirection;
	static final float MAX_SCALE = 0.125f;
	final float scale_increment;
	int halt;

	protected DrippingDewParticle(ClientWorld clientWorld, double x, double y, double z, SpriteProvider spriteProvider) {
		super(clientWorld, x, y, z);
		this.sprite = spriteProvider.getSprite(0, 1);
		this.clientWorld = clientWorld;
		this.gravityStrength = 0.06f;
		this.maxAge = 400 + random.nextInt(300);
		this.red = 0.3f;
		this.green = 0.425f;
		this.blue = 1.0f;
		this.scale = 0f;
		this.velocityX = 0.0f;
		this.velocityY = 0.0f;
		this.velocityZ = 0.0f;
		this.movementMode = MovementMode.GROWING;
		this.attachedDirection = getBlockFace(x - Math.floor(x), z - Math.floor(z));
		this.scale_increment = (0.008f + 0.002f * random.nextFloat()) * MAX_SCALE;
		this.collidesWithWorld = false;
		this.age = 0;
		this.halt = 0;
	}

	private Direction getBlockFace(double xBlock, double zBlock) {
		Direction xD = xBlock < 0.1f ? Direction.WEST : (xBlock > 0.9f ? Direction.EAST : Direction.UP);
		Direction zD = zBlock < 0.1f ? Direction.NORTH : (zBlock > 0.9f ? Direction.SOUTH : Direction.UP);
		if (xD == Direction.UP) return zD;
		if (zD == Direction.UP) return xD;
		return random.nextBoolean() ? xD : zD;
	}


	@Override
	public ParticleTextureSheet getType() {
		return ParticleTextureSheet.PARTICLE_SHEET_OPAQUE;
	}

	@Override
	public void tick() {
		if (age++ >= maxAge) {
			markDead();
			return;
		}
		this.prevPosX = this.x;
		this.prevPosY = this.y;
		this.prevPosZ = this.z;
		if (movementMode == MovementMode.GROWING) {
			if (this.scale >= MAX_SCALE) {
				this.movementMode = attachedDirection == Direction.UP ? MovementMode.FALLING : MovementMode.ROLLING_DOWN;
			} else {
				this.scale += scale_increment;
			}
			return;
		}
		BlockPos pos = new BlockPos((int)Math.floor(x), (int)Math.floor(y), (int)Math.floor(z));
		if (!(movementMode == MovementMode.FALLING)) { //movementMode == either HALT or FALLING
			BlockPos pos2 = new BlockPos((int)Math.floor(x), (int)Math.floor(y + 0.05), (int)Math.floor(z)).offset(attachedDirection);
			if (movementMode == MovementMode.ROLLING_DOWN) {
				if (!Block.isFaceFullSquare(clientWorld.getBlockState(pos.offset(attachedDirection)).getCollisionShape(clientWorld, pos.offset(attachedDirection)), attachedDirection.getOpposite())) {
					this.halt = 10 + random.nextInt(50);
					movementMode = MovementMode.HALT;
					return;
				}
			} else {
				if (halt-- <= 0 || !Block.isFaceFullSquare(clientWorld.getBlockState(pos2).getCollisionShape(clientWorld, pos2), attachedDirection.getOpposite())) {
					movementMode = MovementMode.FALLING;
					return;
				}
			}

			double horizontalVelocity = velocityX + velocityZ;
			if (random.nextInt(20) == 0) {
				velocityY = Math.max(-0.04, velocityY * (0.4 + 1.4 * random.nextFloat()) - 0.0075 * random.nextFloat());
				horizontalVelocity = Math.min(Math.abs(horizontalVelocity), velocityY / (-1.666666666667)) * Math.signum(horizontalVelocity);
			}
			int anInt = random.nextInt(24);
			if (anInt < 3) {
				if (anInt == 0) {
					horizontalVelocity = -0.0075 + 0.015 * random.nextFloat();
				} else {
					horizontalVelocity += -0.015 + 0.03 * random.nextFloat();
				}
				horizontalVelocity = Math.min(Math.abs(horizontalVelocity), velocityY / (-1.666666666667)) * Math.signum(horizontalVelocity);
			}
			if (attachedDirection.getAxis() == Direction.Axis.X) {
				velocityX = 0.0;
				velocityZ = horizontalVelocity;
			} else {
				velocityX = horizontalVelocity;
				velocityZ = 0.0;
			}

			BlockState state = clientWorld.getBlockState(pos);
			if (state.isSideSolidFullSquare(clientWorld, pos, Direction.DOWN)) {
				this.markDead();
				return;
			}
			if (state.isSideSolidFullSquare(clientWorld, pos, Direction.UP)) {
				this.markDead();
				return;
			}



		} else { //MovementMode == MovementMode.FALLING
			velocityX += -0.02 + 0.04 * random.nextFloat();
			velocityY -= gravityStrength;
			velocityZ += -0.02 + 0.04 * random.nextFloat();

			velocityX *= 0.98;
			velocityY *= 0.98;
			velocityZ *= 0.98;

			if (this.onGround) {
				this.markDead();
				return;
			}

		}

		if (clientWorld.isWater(pos)) {
			this.markDead();

		}
		this.move(velocityX, movementMode == MovementMode.HALT ? 0 : velocityY, velocityZ);
	}

	enum MovementMode {
		GROWING,
		ROLLING_DOWN,
		HALT,
		FALLING
	}

	public static class Factory implements ParticleFactory<SimpleParticleType> {

		private final SpriteProvider spriteProvider;

		public Factory(SpriteProvider spriteProvider) {
			this.spriteProvider = spriteProvider;
		}

		@Nullable
		@Override
		public Particle createParticle(SimpleParticleType parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
			return new DrippingDewParticle(world, x,y,z, this.spriteProvider);
		}
	}
}
