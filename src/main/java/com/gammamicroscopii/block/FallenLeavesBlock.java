package com.gammamicroscopii.block;

import com.gammamicroscopii.DynamicSeasons;
import com.gammamicroscopii.world.SeasonHelper;
import net.minecraft.block.*;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

import static com.gammamicroscopii.world.SeasonHelper.advanceSeason;
import static com.gammamicroscopii.world.SeasonHelper.isMiddleBetweenExtremes;
import static com.gammamicroscopii.world.SeasonHelper.getRemainingIntervalProgress;
import static com.gammamicroscopii.world.SeasonHelper.calculateInverseRate;

public class FallenLeavesBlock extends Block implements Waterloggable, SeasonallyDisappearingSoilBlock{

	public static final int MAX_LAYERS = 17;
	public static final IntProperty HEIGHT = IntProperty.of("height", 1, MAX_LAYERS);
	protected static final VoxelShape[] LAYERS_TO_SHAPE = new VoxelShape[]{
			Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 0.5, 16.0),
			Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 1.0, 16.0),
			Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 2.0, 16.0),
			Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 3.0, 16.0),
			Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 4.0, 16.0),
			Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 5.0, 16.0),
			Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 6.0, 16.0),
			Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 7.0, 16.0),
			Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 8.0, 16.0),
			Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 9.0, 16.0),
			Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 10.0, 16.0),
			Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 11.0, 16.0),
			Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 12.0, 16.0),
			Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 13.0, 16.0),
			Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 14.0, 16.0),
			Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 15.0, 16.0),
			VoxelShapes.fullCube()
	};


	public FallenLeavesBlock(Settings settings) {
		super(settings);
		setDefaultState(getDefaultState().with(Properties.WATERLOGGED, false).with(HEIGHT, 1));
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(HEIGHT, Properties.WATERLOGGED);
	}






	protected boolean canPathfindThrough(BlockState state, NavigationType type) {
		return switch (type) {
			case LAND -> state.get(HEIGHT) < 10;
			case WATER, AIR -> false;
		};
	}

	protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return LAYERS_TO_SHAPE[state.get(HEIGHT) - 1];
	}

	protected VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		int height = state.get(HEIGHT);
		return height < 5 ? VoxelShapes.empty() : LAYERS_TO_SHAPE[height-5];
	}

	protected VoxelShape getSidesShape(BlockState state, BlockView world, BlockPos pos) {
		return LAYERS_TO_SHAPE[state.get(HEIGHT) - 1];
	}

	protected VoxelShape getCameraCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return LAYERS_TO_SHAPE[state.get(HEIGHT) - 1];
	}

	protected boolean hasSidedTransparency(BlockState state) {
		return true;
	}

	protected float getAmbientOcclusionLightLevel(BlockState state, BlockView world, BlockPos pos) {
		return state.get(HEIGHT) == MAX_LAYERS ? 0.2F : 1.0F;
	}

	protected boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
		BlockState blockState = world.getBlockState(pos.down());
		return Block.isFaceFullSquare(blockState.getCollisionShape(world, pos.down()), Direction.UP) || blockState.isOf(this) && blockState.get(HEIGHT) == MAX_LAYERS;
	}

	protected BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
		if (state.get(Properties.WATERLOGGED)) {
			world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
		}

		return !state.canPlaceAt(world, pos) ? Blocks.AIR.getDefaultState() : super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
	}

	protected void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {

	}

	protected boolean canReplace(BlockState state, ItemPlacementContext context) {
		int i = state.get(HEIGHT);
		if (context.getStack().isOf(this.asItem()) && i < MAX_LAYERS) {
			if (context.canReplaceExisting()) {
				return context.getSide() == Direction.UP;
			} else {
				return true;
			}
		} else {
			return i == 1;
		}
	}

	@Nullable
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		BlockState blockState = ctx.getWorld().getBlockState(ctx.getBlockPos());
		if (blockState.isOf(this)) {
			int i = blockState.get(HEIGHT);
			boolean w = blockState.get(Properties.WATERLOGGED);
			return blockState.with(HEIGHT, Math.min(MAX_LAYERS, i + 1)).with(Properties.WATERLOGGED, i < MAX_LAYERS - 1 && w);
		} else {
			return this.getDefaultState().with(Properties.WATERLOGGED, ctx.getWorld().getFluidState(ctx.getBlockPos()).isOf(Fluids.WATER));
		}
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return state.get(Properties.WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
	}


	public static final int CHANCE_OF_SEASONAL_PLACING_SUCCESS = /*100 over */DynamicSeasons.YEAR_DURATION / 15360;

	@Override
	public void onSeasonalTick(ServerWorld world, BlockState blockState, BlockPos pos, Random random, float startSeason, float currentSeason, float endSeason) {

		final float despawningSeasonYearFraction = 0.1625f;
		final float pseudoEndSeason =  advanceSeason(startSeason, despawningSeasonYearFraction);
		float remainingProgress = isMiddleBetweenExtremes(startSeason, currentSeason, pseudoEndSeason) ?
						getRemainingIntervalProgress(startSeason, currentSeason, pseudoEndSeason) : 0f;

		//float square = remainingProgress * remainingProgress;
		//float calculateInverseRate = 0.00549316406f * square * square * remainingProgress * despawningSeasonYearFraction * CHANCE_OF_SEASONAL_UPDATE_PER_TICK_FOR_ANY_BLOCK;

		if (random.nextFloat() * calculateInverseRate(remainingProgress, despawningSeasonYearFraction, 0.00549316406f, 1.25f) < 1.0f) {

			if (SeasonHelper.isMiddleBetweenExtremes(startSeason, currentSeason, endSeason) && world.getBlockState(pos.down()).isIn(ModBlocks.Tags.ORGANIC_SOIL)) {
				int height = blockState.get(HEIGHT);
				int decrease = random.nextInt(3) + 1;
				if (height <= decrease) {
					world.setBlockState(pos, Blocks.AIR.getDefaultState());
				} else {
					world.setBlockState(pos, blockState.with(HEIGHT, height - decrease), Block.NOTIFY_LISTENERS);
				}
			}
		}
	}

	@Override
	public void tryPlaceBlockOfThis(ServerWorld world, BlockPos pos, BlockState state, boolean stateIsAir, Random random) {
		if (random.nextInt(CHANCE_OF_SEASONAL_PLACING_SUCCESS) > 99) return;

		if (stateIsAir) {
			world.setBlockState(pos, getDefaultState());
		} else {
			world.setBlockState(pos, state.with(HEIGHT, Math.min(MAX_LAYERS, state.get(HEIGHT) + 1)));
		}

	}


}
