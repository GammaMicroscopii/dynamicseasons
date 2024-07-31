package com.gammamicroscopii.block;

import com.gammamicroscopii.DynamicSeasons;
import com.gammamicroscopii.item.ModItems;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

public class CondensationBlock extends MultifaceGrowthBlock {

	/*private static final VoxelShape UP_SHAPE = Block.createCuboidShape(0.0, 15.0, 0.0, 16.0, 16.0, 16.0);
	private static final VoxelShape DOWN_SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 1.0, 16.0);
	private static final VoxelShape EAST_SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 1.0, 16.0, 16.0);
	private static final VoxelShape WEST_SHAPE = Block.createCuboidShape(15.0, 0.0, 0.0, 16.0, 16.0, 16.0);
	private static final VoxelShape SOUTH_SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 16.0, 1.0);
	private static final VoxelShape NORTH_SHAPE = Block.createCuboidShape(0.0, 0.0, 15.0, 16.0, 16.0, 16.0);
	private static final Map<Direction, BooleanProperty> FACING_PROPERTIES;
	private static final Map<Direction, VoxelShape> SHAPES_FOR_DIRECTIONS;
	protected static final Direction[] DIRECTIONS;
	private final ImmutableMap<BlockState, VoxelShape> SHAPES;
	private final boolean hasAllHorizontalDirections;
	private final boolean canMirrorX;
	private final boolean canMirrorZ;
	static {
		FACING_PROPERTIES = ConnectingBlock.FACING_PROPERTIES;
		SHAPES_FOR_DIRECTIONS = (Map) Util.make(Maps.newEnumMap(Direction.class), (shapes) -> {
			shapes.put(Direction.NORTH, SOUTH_SHAPE);
			shapes.put(Direction.EAST, WEST_SHAPE);
			shapes.put(Direction.SOUTH, NORTH_SHAPE);
			shapes.put(Direction.WEST, EAST_SHAPE);
			shapes.put(Direction.UP, UP_SHAPE);
			shapes.put(Direction.DOWN, DOWN_SHAPE);
		});
		DIRECTIONS = Direction.values();
	}*/

	public CondensationBlock(Settings settings) {
		super(settings);
		setDefaultState(getDefaultState().with(Properties.DOWN, true).with(Properties.UP, false).with(Properties.NORTH, false).with(Properties.EAST, false).with(Properties.SOUTH, false).with(Properties.WEST, false));
		/*this.SHAPES = this.getShapesForStates(MultifaceGrowthBlock::getShapeForState);
		this.hasAllHorizontalDirections = Direction.Type.HORIZONTAL.stream().allMatch(this::canHaveDirection);
		this.canMirrorX = Direction.Type.HORIZONTAL.stream().filter(Direction.Axis.X).filter(this::canHaveDirection).count() % 2L == 0L;
		this.canMirrorZ = Direction.Type.HORIZONTAL.stream().filter(Direction.Axis.Z).filter(this::canHaveDirection).count() % 2L == 0L;*/
	}

	@Override
	protected MapCodec<? extends MultifaceGrowthBlock> getCodec() {
		return null;
	}

	/*@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(Properties.DOWN, Properties.UP, Properties.NORTH, Properties.EAST, Properties.SOUTH, Properties.WEST);
	}*/


	protected boolean canPathfindThrough(BlockState state, NavigationType type) {
		return true;
	}



	@Override
	protected boolean canReplace(BlockState state, ItemPlacementContext context) {
		ItemStack stack = context.getStack();
		return !(stack.isOf(ModBlocks.Items.DEW) || stack.isOf(ModBlocks.Items.FROST)) || super.canReplace(state, context);
	}

	@Override
	public LichenGrower getGrower() {
		return null;
	}


	@Override
	protected boolean isTransparent(BlockState state, BlockView world, BlockPos pos) {
		return true;
	}

	/*protected BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
		if (!hasAnyDirection(state)) {
			return Blocks.AIR.getDefaultState();
		} else {
			return hasDirection(state, direction) && !canGrowOn(world, direction, neighborPos, neighborState) ? disableDirection(state, getProperty(direction)) : state;
		}
	}

	protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return (VoxelShape)this.SHAPES.get(state);
	}

	protected boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
		boolean bl = false;

		for(int i = 0; i < 6; ++i) {
			Direction direction = DIRECTIONS[i];
			if (hasDirection(state, direction)) {
				BlockPos blockPos = pos.offset(direction);
				if (!canGrowOn(world, direction, blockPos, world.getBlockState(blockPos))) {
					return false;
				}

				bl = true;
			}
		}

		return bl;
	}



	protected boolean canReplace(BlockState state, ItemPlacementContext context) {
		return isNotFullBlock(state);
	}
*/
	@Nullable
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		World world = ctx.getWorld();
		BlockPos blockPos = ctx.getBlockPos();
		BlockState blockState = world.getBlockState(blockPos);

		Direction placingDirection = ctx.getPlacementDirections()[0];
		if (blockState.isOf(this)) {
			if (blockState.get(getProperty(placingDirection))) {
				return null;
			} else {
				return blockState.with(getProperty(placingDirection), true);
			}
		} else {
			return getDefaultState().with(Properties.DOWN, false).with(getProperty(placingDirection), true);
		}

		/*return (BlockState) Arrays.stream(ctx.getPlacementDirections()).map((direction) -> {
			return this.withDirection(blockState, world, blockPos, direction);
		}).filter(Objects::nonNull).findFirst().orElse(null);*/
	}
	/*
	@Nullable
	public BlockState withDirection(BlockState state, BlockView world, BlockPos pos, Direction direction) {
		if (!this.canGrowWithDirection(world, state, pos, direction)) {
			return null;
		} else {
			BlockState blockState;
			if (state.isOf(this)) {
				blockState = state;
			} else if (this.isWaterlogged() && state.getFluidState().isEqualAndStill(Fluids.WATER)) {
				blockState = (BlockState)this.getDefaultState().with(Properties.WATERLOGGED, true);
			} else {
				blockState = this.getDefaultState();
			}

			return (BlockState)blockState.with(getProperty(direction), true);
		}
	}

	protected BlockState rotate(BlockState state, BlockRotation rotation) {
		if (!this.hasAllHorizontalDirections) {
			return state;
		} else {
			Objects.requireNonNull(rotation);
			return this.mirror(state, rotation::rotate);
		}
	}

	protected BlockState mirror(BlockState state, BlockMirror mirror) {
		if (mirror == BlockMirror.FRONT_BACK && !this.canMirrorX) {
			return state;
		} else if (mirror == BlockMirror.LEFT_RIGHT && !this.canMirrorZ) {
			return state;
		} else {
			Objects.requireNonNull(mirror);
			return this.mirror(state, mirror::apply);
		}
	}

	private BlockState mirror(BlockState state, Function<Direction, Direction> mirror) {
		BlockState blockState = state;
		Direction[] var4 = DIRECTIONS;
		int var5 = var4.length;

		for(int var6 = 0; var6 < var5; ++var6) {
			Direction direction = var4[var6];
			if (this.canHaveDirection(direction)) {
				blockState = (BlockState)blockState.with(getProperty((Direction)mirror.apply(direction)), (Boolean)state.get(getProperty(direction)));
			}
		}

		return blockState;
	}

	public static boolean hasDirection(BlockState state, Direction direction) {
		BooleanProperty booleanProperty = getProperty(direction);
		return state.contains(booleanProperty) && (Boolean)state.get(booleanProperty);
	}

	public static boolean canGrowOn(BlockView world, Direction direction, BlockPos pos, BlockState state) {
		return Block.isFaceFullSquare(state.getSidesShape(world, pos), direction.getOpposite()) || Block.isFaceFullSquare(state.getCollisionShape(world, pos), direction.getOpposite());
	}

	private static BlockState disableDirection(BlockState state, BooleanProperty direction) {
		BlockState blockState = (BlockState)state.with(direction, false);
		return hasAnyDirection(blockState) ? blockState : Blocks.AIR.getDefaultState();
	}

	public static BooleanProperty getProperty(Direction direction) {
		return (BooleanProperty)FACING_PROPERTIES.get(direction);
	}

	private static BlockState withAllDirections(StateManager<Block, BlockState> stateManager) {
		BlockState blockState = (BlockState)stateManager.getDefaultState();
		Iterator var2 = FACING_PROPERTIES.values().iterator();

		while(var2.hasNext()) {
			BooleanProperty booleanProperty = (BooleanProperty)var2.next();
			if (blockState.contains(booleanProperty)) {
				blockState = (BlockState)blockState.with(booleanProperty, false);
			}
		}

		return blockState;
	}

	private static VoxelShape getShapeForState(BlockState state) {
		VoxelShape voxelShape = VoxelShapes.empty();
		Direction[] var2 = DIRECTIONS;
		int var3 = var2.length;

		for(int var4 = 0; var4 < var3; ++var4) {
			Direction direction = var2[var4];
			if (hasDirection(state, direction)) {
				voxelShape = VoxelShapes.union(voxelShape, (VoxelShape)SHAPES_FOR_DIRECTIONS.get(direction));
			}
		}

		return voxelShape.isEmpty() ? VoxelShapes.fullCube() : voxelShape;
	}

	protected static boolean hasAnyDirection(BlockState state) {
		return Arrays.stream(DIRECTIONS).anyMatch((direction) -> {
			return hasDirection(state, direction);
		});
	}

	private static boolean isNotFullBlock(BlockState state) {
		return Arrays.stream(DIRECTIONS).anyMatch((direction) -> {
			return !hasDirection(state, direction);
		});*/
}
