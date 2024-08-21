package com.gammamicroscopii.mixin.block;

import com.gammamicroscopii.world.ServerWorldTick;
import com.gammamicroscopii.world.TemperatureCalculation;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.WaterFluid;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(WaterFluid.Still.class)
public class WaterFluidMixin extends WaterFluid {

	/** WATER -> ICE, NOT EXPOSED TO SKY */
	@Unique
	@Override
	protected void onRandomTick(World world, BlockPos pos, FluidState state, Random random) {
		if (!world.getBlockState(pos.up()).isAir()) return;
		if (random.nextInt(3) != 0 ) return; //a third for each randomTickSpeed, emulates climate tick speed
		if (world.getTopPosition(Heightmap.Type.MOTION_BLOCKING, pos).getY() == pos.getY()) return;

		ServerWorldTick.tryFreezeWaterBlock(world, pos);
	}

	@Unique
	@Override
	protected boolean hasRandomTicks() {
		return true;
	}

	@Override
	public boolean isStill(FluidState state) {
		return true;
	}


	@Override
	public int getLevel(FluidState state) {
		return 8;
	}
}
