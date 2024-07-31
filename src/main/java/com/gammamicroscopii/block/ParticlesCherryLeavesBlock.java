package com.gammamicroscopii.block;

import com.gammamicroscopii.particle.ModParticles;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.sound.BlockSoundGroup;

public class ParticlesCherryLeavesBlock extends ParticleEmittingLeavesBlock{

	public ParticlesCherryLeavesBlock(BlockSoundGroup soundGroup) {
		super(soundGroup);
	}

	@Override
	public SimpleParticleType[] getTreeTypeSpecificParticleArray() {
		return ModParticles.CHERRY_PARTICLES;
	}

}
