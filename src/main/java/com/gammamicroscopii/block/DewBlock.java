package com.gammamicroscopii.block;

import com.gammamicroscopii.particle.ModParticles;
import net.minecraft.block.BlockState;
import net.minecraft.block.MultifaceGrowthBlock;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class DewBlock extends CondensationBlock{

	public DewBlock(Settings settings) {
		super(settings);
	}

	@Override
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
		if (random.nextInt(18) != 0) {
			return;
		}
		Direction direction = Direction.random(random);
		if(direction == Direction.DOWN) {
			return;
		}
		if (state.get(directionToProperty(direction))) {
			/*double x = pos.getX() - 0.5;
			double y = pos.getY() + 0.485;
			double z = pos.getZ() - 0.5;*/

			double d = direction.getOffsetX() == 0 ? randomPosition(random) : 0.5 + (double)direction.getOffsetX() * 0.45;
			double e = direction.getOffsetY() == 0 ? randomPosition(random) : 0.5 + (double)direction.getOffsetY() * 0.45;
			double f = direction.getOffsetZ() == 0 ? randomPosition(random) : 0.5 + (double)direction.getOffsetZ() * 0.45;

			/*if (direction.getAxis() == Direction.Axis.X) {
				y += randomPosition(random);
				z += randomPosition(random);
				x += direction == Direction.EAST ? 0.985f : 0.015f;
			} else if (direction.getAxis() == Direction.Axis.Z) {
				y += randomPosition(random);
				x += randomPosition(random);
				z += direction == Direction.SOUTH ? 0.985f : 0.015f;
			} else {
				x += randomPosition(random);
				z += randomPosition(random);

			}*/
			world.addParticle(ModParticles.DRIPPING_DEW, (double)pos.getX() + d, (double)pos.getY() + e, (double)pos.getZ() + f, 0.0, 0.0, 0.0);
		}

	}

	double randomPosition(Random random) {
		return 0.05 + 0.9 * random.nextFloat();
	}

	public static BooleanProperty directionToProperty(Direction d) {
		switch (d) {
			case UP: return Properties.UP;
			case NORTH: return Properties.NORTH;
			case SOUTH: return Properties.SOUTH;
			case EAST: return Properties.EAST;
			case WEST: return Properties.WEST;
			default: return null;
		}
	}
}
