package com.gammamicroscopii.block;

import com.gammamicroscopii.particle.ModParticles;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.sound.BlockSoundGroup;

public class ParticlesDarkOakLeavesBlock extends ParticleEmittingLeavesBlock{

	public ParticlesDarkOakLeavesBlock(BlockSoundGroup soundGroup) {
		super(soundGroup);
	}

	@Override
	public SimpleParticleType[] getTreeTypeSpecificParticleArray() {
		return ModParticles.DARK_OAK_PARTICLES;
	}
}
