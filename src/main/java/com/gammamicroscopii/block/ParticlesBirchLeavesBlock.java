package com.gammamicroscopii.block;

import com.gammamicroscopii.particle.ModParticles;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.sound.BlockSoundGroup;

public class ParticlesBirchLeavesBlock extends ParticleEmittingLeavesBlock{

	public ParticlesBirchLeavesBlock(BlockSoundGroup soundGroup) {
		super(soundGroup);
	}

	@Override
	public SimpleParticleType[] getTreeTypeSpecificParticleArray() {
		return ModParticles.BIRCH_PARTICLES;
	}
}
