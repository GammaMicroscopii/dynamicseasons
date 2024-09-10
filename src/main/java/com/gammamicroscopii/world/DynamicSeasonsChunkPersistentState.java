package com.gammamicroscopii.world;

import com.gammamicroscopii.DynamicSeasons;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtType;
import net.minecraft.nbt.scanner.NbtScanner;
import net.minecraft.nbt.visitor.NbtElementVisitor;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Pair;
import net.minecraft.util.Util;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.World;

import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class DynamicSeasonsChunkPersistentState extends PersistentState {

	public long lastUnloadedTime = Long.MIN_VALUE;



	@Override
	public NbtCompound writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
		//nbt.put("last_unloaded", );
		//nbt.putLong("last_unloaded", lastUnloadedTime);
		return nbt;
	}

	public static DynamicSeasonsChunkPersistentState createFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
		DynamicSeasonsChunkPersistentState state = new DynamicSeasonsChunkPersistentState();
		state.lastUnloadedTime = tag.getInt("last_unloaded");
		return state;
	}

	private static final Type<DynamicSeasonsChunkPersistentState> type = new Type<>(
					DynamicSeasonsChunkPersistentState::new, // If there's no 'StateSaverAndLoader' yet create one
					DynamicSeasonsChunkPersistentState::createFromNbt, // If there is a 'StateSaverAndLoader' NBT, parse it with 'createFromNbt'
					null // Supposed to be an 'DataFixTypes' enum, but we can just pass null
	);

	public static DynamicSeasonsChunkPersistentState getServerState(MinecraftServer server, World world) {

		PersistentStateManager persistentStateManager = server.getWorld(world.getRegistryKey()).getPersistentStateManager();


		DynamicSeasonsChunkPersistentState state = persistentStateManager.getOrCreate(type, DynamicSeasons.MOD_ID);


		state.markDirty();

		return state;
	}
}
