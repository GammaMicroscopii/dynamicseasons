package com.gammamicroscopii.network;

import com.gammamicroscopii.DynamicSeasons;
import com.gammamicroscopii.colors.DSColorHelper;
import com.gammamicroscopii.resourceload.data.BiomeClimate;
import com.gammamicroscopii.resourceload.data.ClientBiomeClimates;
import io.netty.buffer.ByteBuf;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.include.com.google.common.base.Charsets;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class DSNetworking {



	public static void updateRenderingForAllPlayers(MinecraftServer server) {
		Iterator<ServerPlayerEntity> iter = server.getPlayerManager().getPlayerList().iterator();
		ServerPlayerEntity spe;
		while (iter.hasNext()) {
			spe = iter.next();
			ServerPlayNetworking.send(spe, new UpdateRenderingEmptyPayload());
		}
	}

	/*public static void loadBiomeClimatesForAllPlayers(MinecraftServer server, HashMap<Identifier, BiomeClimate> climateMap) {
		Iterator<ServerPlayerEntity> iter = server.getPlayerManager().getPlayerList().iterator();
		ServerPlayerEntity spe;
		while (iter.hasNext()) {
			spe = iter.next();
			ServerPlayNetworking.send(spe, new UpdateRenderingEmptyPayload());
		}
	}*/

	public static void registerModPackets() {
		PayloadTypeRegistry.playS2C().register(UpdateRenderingEmptyPayload.ID, UpdateRenderingEmptyPayload.CODEC);
		//PayloadTypeRegistry.playS2C().register(LoadClimatePayload.ID, LoadClimatePayload.CODEC);
	}

	public static void registerClientReceivers() {
		ClientPlayNetworking.registerGlobalReceiver(UpdateRenderingEmptyPayload.ID, (payload, context) -> {
			context.client().execute(() -> {
				DSColorHelper.reloadColorRendering();
			});
		});

		/*ClientPlayNetworking.registerGlobalReceiver(LoadClimatePayload.ID, (payload, context) -> {
			context.client().execute(() -> {
				ClientBiomeClimates.fillClimateMap(payload.climateMap());
			});
		});*/
	}

	public record UpdateRenderingEmptyPayload() implements CustomPayload {

		public static final CustomPayload.Id<UpdateRenderingEmptyPayload> ID = new CustomPayload.Id<>(new Identifier(DynamicSeasons.MOD_ID, "reload_render"));
		public static final PacketCodec<RegistryByteBuf, UpdateRenderingEmptyPayload> CODEC = PacketCodec.unit(new UpdateRenderingEmptyPayload());


		@Override
		public Id<? extends CustomPayload> getId() {
			return ID;
		}
	}

	/*public record LoadClimatePayload(HashMap<Identifier, BiomeClimate> climateMap) implements CustomPayload {

		public static final PacketCodec<ByteBuf, HashMap<Identifier, BiomeClimate>> CLIMATE_MAP_PACKET_CODEC = new PacketCodec<>() {
			@Override
			public HashMap<Identifier, BiomeClimate> decode(ByteBuf buf) {
				HashMap<Identifier, BiomeClimate> finalMap = new HashMap<>();
				int identifierLength;
				String identifierString;
				BiomeClimate climate;
				while (buf.readableBytes() > 0) {
					identifierLength = buf.readInt();
					identifierString = buf.readCharSequence(identifierLength, Charsets.US_ASCII).toString();
					climate = new BiomeClimate(buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat());
					finalMap.put(new Identifier(identifierString), climate);
				}
				return finalMap;
			}

			@Override
			public void encode(ByteBuf buf, HashMap<Identifier, BiomeClimate> value) {
				Iterator<Map.Entry<Identifier, BiomeClimate>> i = value.entrySet().iterator();
				Map.Entry<Identifier, BiomeClimate> entry;
				BiomeClimate climate;
				String keyString;
				while (i.hasNext()) {
					entry = i.next();
					keyString = entry.getKey().toString();
					climate = entry.getValue();

					buf.writeInt(keyString.length());
					buf.writeCharSequence(entry.getKey().toString(), Charsets.US_ASCII);
					buf.writeFloat(climate.averageTemperature());
					buf.writeFloat(climate.seasonalTempOscillation());
					buf.writeFloat(climate.diurnalTempOscillation());
					buf.writeFloat(climate.temperatureVariability());
					buf.writeFloat(climate.downfall());
				}
			}
		};

		public static final CustomPayload.Id<LoadClimatePayload> ID = new CustomPayload.Id<>(new Identifier(DynamicSeasons.MOD_ID, "load_climates"));
		public static final PacketCodec<RegistryByteBuf, LoadClimatePayload> CODEC = PacketCodec.tuple(CLIMATE_MAP_PACKET_CODEC, LoadClimatePayload::climateMap, LoadClimatePayload::new);

		@Override
		public Id<? extends CustomPayload> getId() {
			return ID;
			//BlockPos.PACKET_CODEC
		}
	}*/
}
