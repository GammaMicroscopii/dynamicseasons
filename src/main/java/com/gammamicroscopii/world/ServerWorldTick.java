package com.gammamicroscopii.world;

import com.gammamicroscopii.DynamicSeasons;
import com.gammamicroscopii.Temperature;
import com.gammamicroscopii.block.FallenLeavesBlock;
import com.gammamicroscopii.block.ModBlocks;
import com.gammamicroscopii.block.SeasonallyDisappearingSoilBlock;
import com.gammamicroscopii.mixed.BiomeMixed;
import com.gammamicroscopii.mixed.ServerWorldMixed;
import com.gammamicroscopii.resourceload.data.SeasonalBlockCycle;
import com.gammamicroscopii.resourceload.data.SeasonalBlockCycles;
import com.gammamicroscopii.resourceload.data.SeasonallyDisappearingSoilBlocksJson;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonWriter;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerChunkEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.minecraft.block.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.Util;
import net.minecraft.util.WorldSavePath;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.LocalRandom;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.GameRules;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.WorldChunk;

import java.io.*;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.RandomAccess;

import static com.gammamicroscopii.Temperature.*;
import static com.gammamicroscopii.world.ModChunkDataEncoding.createDataFileIfNotExists;
import static com.gammamicroscopii.world.ModChunkDataEncoding.getPathToDimension;


public class ServerWorldTick {

	static MinecraftServer server = null;
	public static MinecraftServer getServer() {return server;}

	public static final int SEASONAL_TICK_SKIP_COLUMN_CHANCE = DynamicSeasons.YEAR_DURATION / 57600; //<- 57600 = the number of column updates per chunk per year, on average
	public static final int SEASONAL_TICK_SKIP_BLOCK_CHANCE = /*100 over */200; //higher = fewer updates
	public static final float CHANCE_OF_SEASONAL_UPDATE_PER_TICK_FOR_ANY_BLOCK = 256f * SEASONAL_TICK_SKIP_COLUMN_CHANCE * SEASONAL_TICK_SKIP_BLOCK_CHANCE / 100f;
	public static final Random random = new LocalRandom(20240804);
	public static final HashMap<Block, IntProperty> seasonallyDisappearingStackablePropertiedBlocksWithTheirStackingProperties = Util.make(new HashMap<>(), hashMap -> {
		hashMap.put(Blocks.PINK_PETALS, Properties.FLOWER_AMOUNT);
		hashMap.put(ModBlocks.FALLEN_LEAVES, FallenLeavesBlock.HEIGHT);
	});



	public static void init() {

		ServerWorldEvents.LOAD.register((minecraftServer, serverWorld) -> {
			server = minecraftServer;
			//DSNetworking.loadBiomeClimatesForAllPlayers(server, BiomeClimates.getClimateMap());
			//server.getRegistryManager().get(RegistryKeys.BIOME).getEntry();

			for (SeasonalBlockCycle cycle : SeasonalBlockCycles.getSeasonalBlockCycleMap().values()) {
				cycle.updateCycleStage(((ServerWorldMixed)minecraftServer.getOverworld()).getSeasonInfo().getSeason());
			}




			try {
				/*Path path = minecraftServer.getSavePath(WorldSavePath.ROOT).resolve("dynamicseasons_regions");

				File f = path.toFile();
				f.mkdir();
				//DynamicSeasons.LOGGER.info(serverWorld.getRegistryKey().getValue().toString());

				Path path2 = ;*/

				File f = getPathToDimension(serverWorld).toFile();
				f.mkdirs();

				//RandomAccessFile raf = new RandomAccessFile(f, "rw");
				//raf.writeLong(0);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		});

		ServerWorldEvents.UNLOAD.register((minecraftServer, serverWorld) -> {
			server = null;
		});

		ServerTickEvents.START_WORLD_TICK.register((serverWorld) -> {

			if (!(serverWorld == server.getOverworld())) return;

			ServerWorldMixed swm = ((ServerWorldMixed) serverWorld);
			swm.setSeasonUpdateTimeout(swm.getSeasonUpdateTimeout() -1);
			if (swm.getSeasonUpdateTimeout() <= 0) {
				//swm.setSeasonUpdateTimeout(600);
				SeasonHelper.updateSeason(serverWorld, false);
			}
		});

		ServerChunkEvents.CHUNK_LOAD.register((serverWorld, worldChunk) -> {
			long lastUnloadedTime = ModChunkDataEncoding.getLastUnloadedTime(serverWorld, worldChunk.getPos());
			postProcessChunk(worldChunk, serverWorld, lastUnloadedTime);
		});

		ServerChunkEvents.CHUNK_UNLOAD.register((serverWorld, worldChunk) -> {
			ModChunkDataEncoding.setLastUnloadedTime(serverWorld, worldChunk.getPos());
		});

	}

	private static void postProcessChunk(WorldChunk worldChunk, ServerWorld serverWorld, long lastUnloadedTime) {
		System.out.println("chunk: " + worldChunk.getPos() + ", last unloaded: " + lastUnloadedTime + ", time: " + serverWorld.getTimeOfDay() + "; " + (serverWorld.getTimeOfDay() - lastUnloadedTime) + " ticks ago");
	}

	public static void tickSeasonalBlockUpdates(WorldChunk chunk, int randomTickSpeed, ServerWorld world/*, ChunkPos chunkPos*/) {

		if (!world.getGameRules().getBoolean(GameRules.DO_DAYLIGHT_CYCLE)) return;
		if (random.nextInt(SEASONAL_TICK_SKIP_COLUMN_CHANCE) != 0) return;

		ChunkPos chunkPos = chunk.getPos();
		BlockPos pos = world.getRandomPosInChunk(chunkPos.getStartX(), 0, chunkPos.getStartZ(), 15);
		int topPos = world.getTopPosition(Heightmap.Type.WORLD_SURFACE, pos).getY();
		int bottomPos = Math.min(world.getTopPosition(Heightmap.Type.OCEAN_FLOOR, pos).getY(),
								 world.getTopPosition(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, pos).getY());
		BlockPos.Mutable mutable = new BlockPos.Mutable(pos.getX(), bottomPos, pos.getZ());
		int cumulativeSolidness = 0;
		BlockState temp;
		int bottomY = world.getBottomY();
		while (cumulativeSolidness < 16 && mutable.getY() > bottomY) {
			temp = world.getBlockState(mutable);

			if (temp.getBlock() instanceof LeavesBlock) {

			} else if (temp.isIn(BlockTags.OVERWORLD_NATURAL_LOGS)) {
				cumulativeSolidness+= 2;
			} else if (temp.getBlock() instanceof MushroomBlock) {
				cumulativeSolidness+= 2;
			} else if (temp.isSolidBlock(world, mutable)) {
				cumulativeSolidness+= 10;
			} else {
				cumulativeSolidness++;
			}


			mutable.move(Direction.DOWN);
		}
		bottomPos = mutable.getY();
		mutable.setY(topPos);

		//HashMap<BlockState, SeasonalBlockCycle> blockStateSeasonalRuleHashMap = null;
		BlockState blockState;
		Block block;
		Identifier id;
		while (mutable.getY() >= bottomPos) {

			if (random.nextInt(SEASONAL_TICK_SKIP_BLOCK_CHANCE) > 99) {
				mutable.move(Direction.DOWN);
				continue;
			}

			blockState = world.getBlockState(mutable);
			block = blockState.getBlock();
			id = world.getRegistryManager().get(RegistryKeys.BLOCK).getId(block);


			if (!(blockState.isAir() || blockState.isOf(Blocks.WATER) || blockState.isOf(Blocks.LAVA))) {

				/**SECTION: SEASONAL BLOCK CYCLES - CHECK FOR ADVANCING TO NEXT CYCLE STAGE*/

				Pair<SeasonalBlockCycle, Integer> pair = SeasonalBlockCycles.getBlockSeasonalCycle(id);
				if (pair != null) {
					//if (blockStateSeasonalRuleHashMap == null)
					//	blockStateSeasonalRuleHashMap = new HashMap<BlockState, SeasonalBlockCycle>();

					checkSeasonalCycleUpdate(world, mutable.toImmutable(), blockState, block, id, pair.getLeft(), ((ServerWorldMixed)world).getSeasonInfo().getSeason()/*, blockStateSeasonalRuleHashMap*/);
				} else {

					/** SECTION: SEASONALLY DISAPPEARING SOIL BLOCKS - CHECK FOR DECAYING FALLEN LEAVES OR PINK PETALS */

					Pair<Float, Float> pair2 = SeasonallyDisappearingSoilBlocksJson.getVanishingSeasonBounds(id);
					if (pair2 != null) {
						if (block instanceof SeasonallyDisappearingSoilBlock sdsb) {
							sdsb.onSeasonalTick(world, blockState, mutable.toImmutable(), random, pair2.getLeft(), ((ServerWorldMixed)world).getSeasonInfo().getSeason(), pair2.getRight());
						}
					}
				}

				/** SECTION: SDSBs - CHECK FOR LEAF BLOCKS PLACING FALLEN LEAVES OR PINK PETALS ON THE GROUND */

				Identifier seasonallyDisappearingBlockBeingPlacedByAMightBePlacingBlockBeingReferencedByTheMutableBlockPosObject = SeasonallyDisappearingSoilBlocksJson.getSoilBlockPlacedByBlock(id);
				if (seasonallyDisappearingBlockBeingPlacedByAMightBePlacingBlockBeingReferencedByTheMutableBlockPosObject != null && world.getBlockState(mutable.down()).isAir()) {
					placeSeasonalBlock(world, mutable.down(), (world.getRegistryManager().get(RegistryKeys.BLOCK).get(seasonallyDisappearingBlockBeingPlacedByAMightBePlacingBlockBeingReferencedByTheMutableBlockPosObject)));
				}
			}

			mutable.move(Direction.DOWN);
		}



	}

	private static void placeSeasonalBlock(ServerWorld world, BlockPos pos, Block seasonallyDisappearingSoilBlock) {
		BlockPos.Mutable mutable = new BlockPos.Mutable(pos.getX(), pos.getY(), pos.getZ());
		BlockState state = world.getBlockState(mutable);
		boolean finished = false;
		Direction randomDir;
		for (int steps = 16; steps > 0 && !finished; steps--) {

			if (!state.isSolidBlock(world, mutable)) {
				mutable.move(Direction.DOWN);
				if (random.nextBoolean()) {
					randomDir = Direction.Type.HORIZONTAL.random(random);
					int randombtwn1and3 = 1+random.nextInt(3);
					for (int i = 0; i < randombtwn1and3; i++) {
						if (!world.getBlockState(mutable.offset(randomDir)).isSolidBlock(world, mutable)) {
							mutable.move(randomDir);
						} else {
							break;
						}
					}
				}
				state = world.getBlockState(mutable);
			} else {
				finished = true;
			}

		}
		if (modifyPlacementPos(state, world, mutable, seasonallyDisappearingSoilBlock, seasonallyDisappearingStackablePropertiedBlocksWithTheirStackingProperties.get(seasonallyDisappearingSoilBlock), 4, 5, 2)) {


			BlockState state2 = world.getBlockState(mutable.up());

			if (state.isSolidBlock(world, mutable) && (state2.isAir() || state2.isOf(seasonallyDisappearingSoilBlock))) {
				((SeasonallyDisappearingSoilBlock) seasonallyDisappearingSoilBlock).tryPlaceBlockOfThis(world, mutable.up(), state2, state2.isAir(), random);
			}
		}
		/*if (state.isSolidBlock(world, mutable)) {
			Direction direction;
			BlockState state3;
			for (int i = 0; i < 4; i++) {
				direction = Direction.Type.HORIZONTAL.random(random);
				state3 = world.getBlockState(mutable.offset(direction));
				if (!(state3.isSolidBlock(world, mutable)) || (state3.isOf(seasonallyDisappearingSoilBlock))) {
					mutable.move(direction);
					boolean solidDown = false;
					for (int j = 0; j < 5 && !solidDown; j++) {
						mutable.move(Direction.DOWN);
						solidDown = world.getBlockState(mutable).isSolidBlock(world, mutable);
					}
					if (!solidDown) return;
					else break;
				}
			}
		}
		BlockState state2 = world.getBlockState(mutable.up());
		if (state.isSolidBlock(world, mutable) && (state2.isAir() || state2.isOf(seasonallyDisappearingSoilBlock))) {
			((SeasonallyDisappearingSoilBlock)seasonallyDisappearingSoilBlock).tryPlaceBlockOfThis(world, mutable.up(), state2, state2.isAir(), random);
		}*/

	}

	/**
	 *
	 * @param state must be the solid block at the ground
	 * @param world the ServerWorld
	 * @param mutable state's position, as BlockPos.Mutable THIS MUTABLE GETS MUTATED.
	 * @param seasonallyDisappearingSoilBlock the block we are placing
	 * @param stackingProperty the IntProperty of the number of layers or petals
	 *
	 * @return whether it should be placed
	 */
	public static boolean modifyPlacementPos(BlockState state, ServerWorld world, BlockPos.Mutable mutable, Block seasonallyDisappearingSoilBlock, IntProperty stackingProperty, int maxFallingDirectionTries, int maxFallDistance, int maxLevelingUpGroundTries) {
		//if (state.isSolidBlock(world, mutable)) { //state is the ground
		if (state.isSideSolidFullSquare(world, mutable, Direction.UP)) { //state is the ground
			Direction direction;
			BlockState state3;

			for (int i = 0; i < maxFallingDirectionTries/*4*/; i++) {
				direction = Direction.Type.HORIZONTAL.random(random);
				state3 = world.getBlockState(mutable.offset(direction));
				//if (!(state3.isSolidBlock(world, mutable))) {
				if (!(state3.isSideSolidFullSquare(world, mutable, Direction.UP))) {
					if (world.getBlockState(mutable.up().offset(direction)).isSideSolidFullSquare(world, mutable.up(), direction.getOpposite())) continue;
					mutable.move(direction);
					boolean solidDown = false;
					for (int j = 0; j < maxFallDistance/*5*/ && !solidDown; j++) {
						mutable.move(Direction.DOWN);
						solidDown = world.getBlockState(mutable)./**/isSideSolidFullSquare(world, mutable, Direction.UP);//isSolidBlock(world, mutable);
					}
					if (!solidDown) return false;
					else break;
				}
			}
			for (int i = 0; i < maxLevelingUpGroundTries/*2*/ ; i++) {
				direction = Direction.Type.HORIZONTAL.random(random);
				BlockState state999 = world.getBlockState(mutable.up());
				int effectiveLayersCenter = state999.isAir() ? 0 : (state999.isOf(seasonallyDisappearingSoilBlock) ? state999.get(stackingProperty) : -1);
				BlockState state9999 = world.getBlockState(mutable.up().offset(direction));
				if (!world.getBlockState(mutable.offset(direction)).isSolidBlock(world, mutable.offset(direction))) continue;
				int effectiveLayersOffsetted = state9999.isAir() ? 0 : (state9999.isOf(seasonallyDisappearingSoilBlock) ? state9999.get(stackingProperty) : Integer.MAX_VALUE);
				if (effectiveLayersOffsetted < effectiveLayersCenter) {
					mutable.move(direction);
					return true;
				}
			}
		}
		return true;
	}

	private static void checkSeasonalCycleUpdate(ServerWorld world, BlockPos pos, BlockState blockState, Block block, Identifier id, SeasonalBlockCycle cycle, float season /*, HashMap<BlockState, SeasonalBlockCycle> blockStateSeasonalRuleHashMap*/) {

		if(SeasonalBlockCycles.isBlockStateAffected(blockState, id)) {

			String currentBlockId = id.toString();
			//String stableBlockId = cycle.getStableBlock(season).id();
			//String previouslyStableBlockId = cycle.getPreviouslyStableBlock(season).id();

			if (!(cycle.getStableBlock(season).id().equals(currentBlockId))) {

				if (currentBlockId.equals(cycle.getPreviouslyStableBlock(season).id())) {
					//float conversionRemainingProgress = cycle.getConversionRemainingProgress(season);
					//int remainingConversionTicks = cycle.getConversionRemainingTicks(season);
					float remainingConversionProgress = cycle.getConversionRemainingProgress(season);
					float conversionLength = cycle.getConversionLengthInYearFraction();
					//float square = remainingConversionProgress * remainingConversionProgress;
					//float calculateInverseRate = 0.00549316406f * square * square * remainingConversionProgress * conversionLength * CHANCE_OF_SEASONAL_UPDATE_PER_TICK_FOR_ANY_BLOCK;
					if (random.nextFloat() * SeasonHelper.calculateInverseRate(remainingConversionProgress, conversionLength, 0.00549316406f, 0f) < 1.0f) {
						world.setBlockState(pos, SeasonalBlockCycles.nextBlockState(blockState, id, world.getRegistryManager()), Block.NOTIFY_LISTENERS | Block.NO_REDRAW);
					}
				} else {
					world.setBlockState(pos, SeasonalBlockCycles.nextBlockState(blockState, id, world.getRegistryManager()), Block.NOTIFY_LISTENERS | Block.NO_REDRAW);
				}
			}
		}
	}

	public static void tryFreezeWaterBlock(World world, BlockPos pos) {
		Biome biome = world.getBiome(pos).value();
		tryFreezeWaterBlock( world,  pos,TemperatureCalculation.getGroundTemperature(((BiomeMixed)(Object)biome).getClimateServerSide()).get(), biome);
	}

	public static void tryFreezeWaterBlock(World world, BlockPos pos, float temp, Biome biome) {
		//Biome biome = world.getBiome(pos).value();
		//float temp = TemperatureCalculation.getGroundTemperature(((BiomeMixed)biome).getClimateServerSide()).get();


		if (temp > FREEZING.get()) return;
		float chance;
		if (temp > FREEZING_WATER_CHECK_THRESHOLD.get()) {
			chance = (temp - FREEZING.get()) / (FREEZING_WATER_CHECK_THRESHOLD.get() - FREEZING.get());
			if (random.nextFloat() < chance && biome.canSetIce(world, pos)) {
				world.setBlockState(pos, Blocks.ICE.getDefaultState());
			}
		} else {
			chance = (temp - FASTEST_FREEZING_POSSIBLE.get()) / (FREEZING_WATER_CHECK_THRESHOLD.get() - FASTEST_FREEZING_POSSIBLE.get());
			if (biome.canSetIce(world, pos, random.nextFloat() < chance)) {
				world.setBlockState(pos, Blocks.ICE.getDefaultState());
			}
		}
	}

	public static void tryMeltSnowBlock(World world, BlockPos pos, BlockState state) {
		Biome biome = world.getBiome(pos).value();
		tryMeltSnowBlock(world, pos, state, TemperatureCalculation.getGroundTemperature(((BiomeMixed)(Object)biome).getClimateServerSide()).get());
	}

	public static void tryMeltSnowBlock(World world, BlockPos pos, BlockState state, float temp) {
		if (temp < Temperature.MINIMUM_TEMPERATURE_FOR_ICE_SUBLIMATION.get()) return;
		float chance = temp > 0f ? 0.01f + temp / 25f : (temp - Temperature.MINIMUM_TEMPERATURE_FOR_ICE_SUBLIMATION.get()) / 1000f;

		int minusLayers = (int) chance + ((random.nextFloat() < chance - (int) chance) ? 1 : 0);
		if (minusLayers == 0) return;
		int layers = state.get(SnowBlock.LAYERS) - minusLayers;
		if (layers <= 0) {
			world.setBlockState(pos, Blocks.AIR.getDefaultState(), Block.NOTIFY_ALL | Block.NO_REDRAW);
		} else {
			world.setBlockState(pos, state.with(SnowBlock.LAYERS, layers), 0);
		}
	}

	public static void tryMeltIceBlock(World world, BlockPos pos) {
		Biome biome = world.getBiome(pos).value();
		tryMeltIceBlock(world, pos, TemperatureCalculation.getGroundTemperature(((BiomeMixed)(Object)biome).getClimateServerSide()).get());
	}

	public static void tryMeltIceBlock(World world, BlockPos pos, float temp) {
		if (temp < Temperature.MINIMUM_TEMPERATURE_FOR_ICE_SUBLIMATION.get()) return;
		float chance = temp > 0f ? 0.001f + temp / 45f : (temp - Temperature.MINIMUM_TEMPERATURE_FOR_ICE_SUBLIMATION.get()) / 10000f;

		int nonIcyNeighbours = 6 - (world.getBlockState(pos.up()).isIn(BlockTags.ICE) ? 1 : 0) -
						(world.getBlockState(pos.down()).isIn(BlockTags.ICE) ? 1 : 0) -
						(world.getBlockState(pos.east()).isIn(BlockTags.ICE) ? 1 : 0) -
						(world.getBlockState(pos.west()).isIn(BlockTags.ICE) ? 1 : 0) -
						(world.getBlockState(pos.north()).isIn(BlockTags.ICE) ? 1 : 0) -
						(world.getBlockState(pos.south()).isIn(BlockTags.ICE) ? 1 : 0);

		chance *= nonIcyNeighbours / 6f;

		if (!(random.nextFloat() < chance)) return;

		((IceBlock)Blocks.ICE).melt(null, world, pos);
	}

		//

		//If the blockstate is not air, water or lava, check all logic related to seasonal checks of blockState.
		//If blockState.getBlock() is known to be unaffected by any SeasonalRule, cache it and return.
		//Hay que implementar con resources las reglas estacionales y traducirlas a una estructura de datos que pueda consultarse desde este método.
		//Además, hay que optimizar


}
