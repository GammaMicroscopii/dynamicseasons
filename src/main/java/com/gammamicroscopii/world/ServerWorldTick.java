package com.gammamicroscopii.world;

import com.gammamicroscopii.mixed.ServerWorldMixed;
import com.gammamicroscopii.resourceload.data.SeasonalBlockCycle;
import com.gammamicroscopii.resourceload.data.SeasonalBlockCycles;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.Heightmap;
import net.minecraft.world.chunk.WorldChunk;

import java.util.HashMap;

public class ServerWorldTick {

	static MinecraftServer server = null;
	public static MinecraftServer getServer() {return server;}

	public static void init() {

		ServerWorldEvents.LOAD.register((minecraftServer, serverWorld) -> {
			server = minecraftServer;
			//DSNetworking.loadBiomeClimatesForAllPlayers(server, BiomeClimates.getClimateMap());
			//server.getRegistryManager().get(RegistryKeys.BIOME).getEntry();
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

		ChunkPos chunkPos = chunk.getPos();
		BlockPos pos = world.getRandomPosInChunk(chunkPos.getStartX(), 0, chunkPos.getStartZ(), 15);
		int topPos = world.getTopPosition(Heightmap.Type.WORLD_SURFACE, pos).getY();
		int bottomPos = Math.min(world.getTopPosition(Heightmap.Type.OCEAN_FLOOR, pos).getY(),
								 world.getTopPosition(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, pos).getY());
		BlockPos.Mutable mutable = new BlockPos.Mutable(pos.getX(), topPos, pos.getZ());

		//HashMap<BlockState, SeasonalBlockCycle> blockStateSeasonalRuleHashMap = null;

		while (mutable.getY() >= bottomPos) {
			BlockState blockState = world.getBlockState(mutable);
			Block block = blockState.getBlock();
			Identifier id = world.getRegistryManager().get(RegistryKeys.BLOCK).getId(block);


			if (!(blockState.isAir() || blockState.isOf(Blocks.WATER) || blockState.isOf(Blocks.LAVA))) {
				Pair<SeasonalBlockCycle, Integer> pair = SeasonalBlockCycles.getBlockSeasonalCycle(id);
				if (pair != null) {
					//if (blockStateSeasonalRuleHashMap == null)
					//	blockStateSeasonalRuleHashMap = new HashMap<BlockState, SeasonalBlockCycle>();

					checkSeasonalUpdate(world, mutable.toImmutable(), blockState, block, id, pair.getLeft(), ((ServerWorldMixed)world).getSeasonInfo().getSeason()/*, blockStateSeasonalRuleHashMap*/);
				}
			}

			mutable.move(Direction.DOWN);
		}

	}

	private static void checkSeasonalUpdate(ServerWorld world, BlockPos pos, BlockState blockState, Block block, Identifier id, SeasonalBlockCycle cycle, float season /*, HashMap<BlockState, SeasonalBlockCycle> blockStateSeasonalRuleHashMap*/) {

		if(SeasonalBlockCycles.isBlockStateAffected(blockState, id)) {
			if (!(cycle.getStableBlock(season).id().equals(id.toString()))) {
				world.setBlockState(pos, SeasonalBlockCycles.nextBlockState(blockState, id, world.getRegistryManager()));
			}
		}


		//

		//If the blockstate is not air, water or lava, check all logic related to seasonal checks of blockState.
		//If blockState.getBlock() is known to be unaffected by any SeasonalRule, cache it and return.
		//Hay que implementar con resources las reglas estacionales y traducirlas a una estructura de datos que pueda consultarse desde este método.
		//Además, hay que optimizar
	}
}
