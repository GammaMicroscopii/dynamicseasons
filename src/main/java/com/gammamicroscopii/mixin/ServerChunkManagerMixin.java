package com.gammamicroscopii.mixin;

import com.gammamicroscopii.world.ServerWorldTick;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerChunkManager.class)
public class ServerChunkManagerMixin {

	@Inject(method = "tickChunks", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/chunk/WorldChunk;getPos()Lnet/minecraft/util/math/ChunkPos;", shift = At.Shift.AFTER))
	private void inject(CallbackInfo ci, @Local(ordinal = 0) WorldChunk chunk, @Local(ordinal = 1) int randomTickSpeed/*, @Local(ordinal = 0) ChunkPos chunkPos*/) {

		ServerWorld serverWorld = (ServerWorld) ((ServerChunkManager) (Object) this).getWorld();
		ServerWorldTick.tickSeasonalBlockUpdates(chunk, randomTickSpeed, serverWorld/*, chunkPos*/);

		for (int k = 0; k < randomTickSpeed; k++) {
			if (serverWorld.random.nextInt(48) == 0) {
				ChunkPos chunkPos = chunk.getPos();
				serverWorld.tickIceAndSnow(serverWorld.getRandomPosInChunk(chunkPos.getStartX(), 0, chunkPos.getStartZ(), 15));
			}
		}

	}

}
