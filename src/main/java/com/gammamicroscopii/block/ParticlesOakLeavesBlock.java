package com.gammamicroscopii.block;

import com.gammamicroscopii.particle.ModParticles;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.sound.BlockSoundGroup;

public class ParticlesOakLeavesBlock extends ParticleEmittingLeavesBlock {

	public ParticlesOakLeavesBlock(BlockSoundGroup soundGroup) {
		super(soundGroup);
	}

	@Override
	public SimpleParticleType[] getTreeTypeSpecificParticleArray() {
		return ModParticles.OAK_PARTICLES;
	}
}
