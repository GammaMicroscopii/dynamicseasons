package com.gammamicroscopii.world;

import com.gammamicroscopii.block.SeasonallyDisappearingSoilBlock;
import com.gammamicroscopii.mixed.ServerWorldMixed;
import com.gammamicroscopii.resourceload.data.SeasonalBlockCycle;
import com.gammamicroscopii.resourceload.data.SeasonalBlockCycles;
import com.gammamicroscopii.resourceload.data.SeasonallyDisappearingSoilBlocksJson;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.minecraft.block.*;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.GameRules;
import net.minecraft.world.Heightmap;
import net.minecraft.world.chunk.WorldChunk;

import java.util.Random;

public class ServerWorldTick {

	static MinecraftServer server = null;
	public static MinecraftServer getServer() {return server;}

	public static int SEASONAL_TICK_SKIP_COLUMN_CHANCE = /*100 over */4000;
	public static int SEASONAL_TICK_SKIP_BLOCK_CHANCE = /*100 over */200;
	public static float CHANCE_OF_SEASONAL_UPDATE_PER_TICK_FOR_ANY_BLOCK = 256f * SEASONAL_TICK_SKIP_COLUMN_CHANCE * SEASONAL_TICK_SKIP_BLOCK_CHANCE / 10000f;
	public static Random random = new Random();

	public static void init() {

		ServerWorldEvents.LOAD.register((minecraftServer, serverWorld) -> {
			server = minecraftServer;
			//DSNetworking.loadBiomeClimatesForAllPlayers(server, BiomeClimates.getClimateMap());
			//server.getRegistryManager().get(RegistryKeys.BIOME).getEntry();

			for (SeasonalBlockCycle cycle : SeasonalBlockCycles.getSeasonalBlockCycleMap().values()) {
				cycle.updateCycleStage(((ServerWorldMixed)minecraftServer.getOverworld()).getSeasonInfo().getSeason());
			}
		});

		ServerWorldEvents.UNLOAD.register((minecraftServer, serverWorld) -> {
			server = null;
		});

		ServerTickEvents.START_WORLD_TICK.register((serverWorld) -> {

			if (!(serverWorld == server.getOverworld())) return;

			ServerWorldMixed swm = ((ServerWorldMixed) (Object) serverWorld);
			swm.setSeasonUpdateTimeout(swm.getSeasonUpdateTimeout() -1);
			if (swm.getSeasonUpdateTimeout() <= 0) {
				//swm.setSeasonUpdateTimeout(600);
				SeasonHelper.updateSeason(serverWorld, false);
			}
		});
	}

	public static void tickSeasonalBlockUpdates(WorldChunk chunk, int randomTickSpeed, ServerWorld world/*, ChunkPos chunkPos*/) {

		if (!world.getGameRules().getBoolean(GameRules.DO_DAYLIGHT_CYCLE)) return;
		if (random.nextInt(SEASONAL_TICK_SKIP_COLUMN_CHANCE) > 99) return;

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
				Pair<SeasonalBlockCycle, Integer> pair = SeasonalBlockCycles.getBlockSeasonalCycle(id);
				if (pair != null) {
					//if (blockStateSeasonalRuleHashMap == null)
					//	blockStateSeasonalRuleHashMap = new HashMap<BlockState, SeasonalBlockCycle>();

					checkSeasonalUpdate(world, mutable.toImmutable(), blockState, block, id, pair.getLeft(), ((ServerWorldMixed)world).getSeasonInfo().getSeason()/*, blockStateSeasonalRuleHashMap*/);
				} else {
					Pair<Float, Float> pair2 = SeasonallyDisappearingSoilBlocksJson.getSeasonPair(id);
					if (pair2 != null) {
						if (block instanceof SeasonallyDisappearingSoilBlock sdsb) {
							sdsb.onSeasonalTick(world, blockState, mutable.toImmutable(), random, pair2.getLeft(), ((ServerWorldMixed)world).getSeasonInfo().getSeason(), pair2.getRight());
						}
					}
				}
			}

			mutable.move(Direction.DOWN);
		}

	}

	private static void checkSeasonalUpdate(ServerWorld world, BlockPos pos, BlockState blockState, Block block, Identifier id, SeasonalBlockCycle cycle, float season /*, HashMap<BlockState, SeasonalBlockCycle> blockStateSeasonalRuleHashMap*/) {

		if(SeasonalBlockCycles.isBlockStateAffected(blockState, id)) {

			String currentBlockId = id.toString();
			//String stableBlockId = cycle.getStableBlock(season).id();
			//String previouslyStableBlockId = cycle.getPreviouslyStableBlock(season).id();

			if (!(cycle.getStableBlock(season).id().equals(currentBlockId))) {

				if (currentBlockId.equals(cycle.getPreviouslyStableBlock(season).id())) {
					//float conversionRemainingProgress = cycle.getConversionRemainingProgress(season);
					int remainingConversionTicks = cycle.getConversionRemainingTicks(season);
					float remainingConversionProgress = cycle.getConversionRemainingProgress(season);
					float square = remainingConversionProgress * remainingConversionProgress;
					float inverseRate = 3f * square * square;
					if (random.nextFloat() * inverseRate < CHANCE_OF_SEASONAL_UPDATE_PER_TICK_FOR_ANY_BLOCK / (remainingConversionTicks+1)) {
						world.setBlockState(pos, SeasonalBlockCycles.nextBlockState(blockState, id, world.getRegistryManager()), Block.NOTIFY_LISTENERS);
					}
				} else {
					world.setBlockState(pos, SeasonalBlockCycles.nextBlockState(blockState, id, world.getRegistryManager()), Block.NOTIFY_LISTENERS);
				}
			}
		}


		//

		//If the blockstate is not air, water or lava, check all logic related to seasonal checks of blockState.
		//If blockState.getBlock() is known to be unaffected by any SeasonalRule, cache it and return.
		//Hay que implementar con resources las reglas estacionales y traducirlas a una estructura de datos que pueda consultarse desde este método.
		//Además, hay que optimizar
	}
}
