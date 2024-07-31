package com.gammamicroscopii.block;


import net.minecraft.block.*;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.client.util.ParticleUtil;
import net.minecraft.fluid.Fluids;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public abstract class ParticleEmittingLeavesBlock extends LeavesBlock {

	public ParticleEmittingLeavesBlock(BlockSoundGroup soundGroup) {
		super(AbstractBlock.Settings.create().mapColor(MapColor.DARK_GREEN).strength(0.2f).ticksRandomly().sounds(soundGroup).nonOpaque().allowsSpawning(Blocks::canSpawnOnLeaves).suffocates(Blocks::never).blockVision(Blocks::never).burnable().pistonBehavior(PistonBehavior.DESTROY).solidBlock(Blocks::never));
	}

	public abstract SimpleParticleType[] getTreeTypeSpecificParticleArray();

	@Override
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
		super.randomDisplayTick(state, world, pos, random);
		if (random.nextInt(60) != 0) {
			return;
		}
		BlockPos blockPos = pos.down();
		BlockState blockState = world.getBlockState(blockPos);
		if (Block.isFaceFullSquare(blockState.getCollisionShape(world, blockPos), Direction.UP) || !blockState.getFluidState().isEmpty()) {
			return;
		}

		SimpleParticleType[] particles = getTreeTypeSpecificParticleArray();

		ParticleUtil.spawnParticle(world, pos, random, particles[random.nextInt(particles.length)]);
	}
}
