package com.gammamicroscopii.colors;

import com.gammamicroscopii.Temperature;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.color.world.FoliageColors;
import net.minecraft.client.color.world.GrassColors;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.*;

import java.awt.*;

public class DSColorHelper {

	public static final int VANILLA_GRASS_COLOR_AVERAGE_SWAMP = 5382182;
	public static final int VANILLA_GRASS_COLOR_DARK_FOREST = 2634762;

	public static final int[] WATER_COLORS = {
			new Color(57, 56, 201).getRGB() & 0xffffff,
			new Color(61, 87, 214).getRGB() & 0xffffff,
			new Color(63, 118, 228).getRGB() & 0xffffff,
			new Color(69, 173, 242).getRGB() & 0xffffff,
			new Color(67, 213, 238).getRGB() & 0xffffff,
			new Color(64, 233, 233).getRGB() & 0xffffff
	};
	public static final float[] WATER_COLOR_TEMP_THRESHOLDS = { //unit = vanilla
			Temperature.of(-3f, Temperature.Unit.CELSIUS).get(Temperature.Unit.VANILLA),
			Temperature.of(7f, Temperature.Unit.CELSIUS).get(Temperature.Unit.VANILLA),
			Temperature.of(15.5f, Temperature.Unit.CELSIUS).get(Temperature.Unit.VANILLA),
			Temperature.of(24f, Temperature.Unit.CELSIUS).get(Temperature.Unit.VANILLA),
			Temperature.of(29f, Temperature.Unit.CELSIUS).get(Temperature.Unit.VANILLA),
			Temperature.of(37f, Temperature.Unit.CELSIUS).get(Temperature.Unit.VANILLA)
	};
	public static final int[] WATER_FOG_COLORS = {
			new Color(5, 5, 51).getRGB() & 0xffffff,
			new Color(4, 22, 51).getRGB() & 0xffffff,
			new Color(4, 31,51).getRGB() & 0xffffff,
			new Color(4, 42, 51).getRGB() & 0xffffff

	};
	public static final float[] WATER_FOG_COLOR_TEMP_THRESHOLDS = { //unit = vanilla
			Temperature.of(12.5f, Temperature.Unit.CELSIUS).get(Temperature.Unit.VANILLA),
			Temperature.of(21f, Temperature.Unit.CELSIUS).get(Temperature.Unit.VANILLA),
			Temperature.of(26f, Temperature.Unit.CELSIUS).get(Temperature.Unit.VANILLA),
			Temperature.of(34f, Temperature.Unit.CELSIUS).get(Temperature.Unit.VANILLA)
	};

	public static final int[] /*int[]*/ THERMOMETER_READOUT_COLORS = {

			Formatting.DARK_BLUE.getColorIndex(),
			Formatting.DARK_PURPLE.getColorIndex(),
			Formatting.LIGHT_PURPLE.getColorIndex(),
			Formatting.AQUA.getColorIndex(),
			Formatting.WHITE.getColorIndex(),
			Formatting.BLUE.getColorIndex(),
			Formatting.DARK_AQUA.getColorIndex(),
			Formatting.GREEN.getColorIndex(),
			Formatting.YELLOW.getColorIndex(),
			Formatting.GOLD.getColorIndex(),
			Formatting.RED.getColorIndex(),
			Formatting.DARK_RED.getColorIndex(),
			Formatting.DARK_PURPLE.getColorIndex()

			/*new Color(10, 0, 70).getRGB() & 0xffffff,
			new Color(65, 0, 170).getRGB() & 0xffffff,
			new Color(147, 0, 220).getRGB() & 0xffffff,
			new Color(243, 7, 209).getRGB() & 0xffffff,
			new Color(218, 52, 236).getRGB() & 0xffffff,
			new Color(167, 87, 242).getRGB() & 0xffffff,
			new Color(144, 112, 251).getRGB() & 0xffffff,
			new Color(144, 144, 251).getRGB() & 0xffffff,
			new Color(155, 161, 252).getRGB() & 0xffffff,
			new Color(161, 163, 253).getRGB() & 0xffffff,
			new Color(21, 0, 255).getRGB() & 0xffffff,
			new Color(15, 21, 250).getRGB() & 0xffffff,
			new Color(4, 143, 240).getRGB() & 0xffffff,
			new Color(0, 219, 181).getRGB() & 0xffffff,
			new Color(62, 252, 60).getRGB() & 0xffffff,
			new Color(190, 255, 2).getRGB() & 0xffffff,
			new Color(242, 208, 0).getRGB() & 0xffffff,
			new Color(253, 144, 1).getRGB() & 0xffffff,
			new Color(244, 76, 0).getRGB() & 0xffffff,
			new Color(226, 16, 0).getRGB() & 0xffffff,
			new Color(180, 0, 0).getRGB() & 0xffffff,
			new Color(150, 0, 36).getRGB() & 0xffffff,
			new Color(180, 0, 77).getRGB() & 0xffffff,
			new Color(224, 0, 117).getRGB() & 0xffffff,
			new Color(244, 20, 111).getRGB() & 0xffffff,
			new Color(182, 48, 89).getRGB() & 0xffffff,
			new Color(132, 48, 54).getRGB() & 0xffffff,
			new Color(78, 22, 22).getRGB() & 0xffffff*/
	};

	public static final float[] THERMOMETER_READOUT_COLOR_TEMP_THRESHOLDS = { //unit = celsius

			-32f, -24f, -16f, -8f, 0f, 7.5f, 15f, 21.25f, 27.5f, 35f, 45f, 55f
			//-42.5f, -37.5f, -32.5f, -27.5f, -22.5f, -17.5f, -12.5f, -7.5f, -2.5f, -0.5f, 0.5f, 2.5f, 7.5f, 12.5f, 17.5f, 22.5f, 27.5f, 32.5f, 37.5f, 42.5f, 47.5f, 52.5f, 57.5f, 62.5f, 67.5f, 72.5f, 87.5f, 82.5f,
	};

	/**
	 * @param origin origin color
	 * @param towards towards color
	 * @return int[] because color components may be negative or >255
	 */
	public static int[] exaggerateColor(int origin, int towards) {
		int r = ((towards & 0xFF0000) >> 15) - ((origin & 0xFF0000) >> 16);
		int g = ((towards & 0xFF00) >> 7) - ((origin & 0xFF00) >> 8);
		int b = ((towards & 0xFF) << 1) - (origin & 0xFF);
		return new int[] {r, g, b};
	}

	/**
	 * simple rgb color average
	 */
	public static int average(int color1, int color2) {
		return ((color1 & 0xFEFEFE) + (color2 & 0xFEFEFE)) >> 1;
	}

	/**
	 * @param color1
	 * @param color2 [<red>, <green>, <blue>]. Allows negative or >255 values, used for exaggerated colors
	 * @return
	 */
	public static int average(int color1, int[] color2) {
		int r = (((color1 >> 16) & 0xFF) + color2[0]) >> 1;
		int g = (((color1 >> 8) & 0xFF) + color2[1]) >> 1;
		int b = ((color1 & 0xFF) + color2[2]) >> 1;
		return (MathHelper.clamp(r, 0, 255) << 16) |
				(MathHelper.clamp(g, 0, 255) << 8) |
				MathHelper.clamp(b, 0, 255);
	}

	public static int getDefaultWaterColor(float temperature) {
		return getIntervalledColor(temperature, WATER_COLORS, WATER_COLOR_TEMP_THRESHOLDS, true);
	}

	public static int getDefaultWaterFogColor(float temperature) {
		return getIntervalledColor(temperature, WATER_FOG_COLORS, WATER_FOG_COLOR_TEMP_THRESHOLDS, true);
	}

	public static int getThermometerReadoutColor(float temperature) {
		return getIntervalledColor(temperature, THERMOMETER_READOUT_COLORS, THERMOMETER_READOUT_COLOR_TEMP_THRESHOLDS, false);
	}

	private static int getIntervalledColor(float temperature, int[] colorsArray, float[] thresholdsArray, boolean interpolate) {
		int tempRange = 0;
		int arrayLength = thresholdsArray.length;
		while (temperature > thresholdsArray[tempRange]) {
			tempRange++;
			if (tempRange == arrayLength) return colorsArray[arrayLength-(interpolate ? 1 : 0)];
		}
		if (!interpolate) return colorsArray[tempRange];
		if (tempRange == 0) return colorsArray[0];
		float tempIntervalProgress = (temperature - thresholdsArray[tempRange-1]) / (thresholdsArray[tempRange] - thresholdsArray[tempRange-1]);
		return appendColorComponent(tempRange, tempIntervalProgress, 1f - tempIntervalProgress, 0xff0000, colorsArray) |
				appendColorComponent(tempRange, tempIntervalProgress, 1f - tempIntervalProgress, 0xff00, colorsArray) |
				appendColorComponent(tempRange, tempIntervalProgress, 1f - tempIntervalProgress, 0xff, colorsArray);
	}

	private static int appendColorComponent(int tempRange, float progress, float progressLeft, int mask, int[] colorsArray) {
		return ((int)((colorsArray[tempRange-1] & mask) * progressLeft + (colorsArray[tempRange] & mask) * progress) & mask);
	}


	public static int getGrassColormapColor(float temperature, float humidity) {
		float f = MathHelper.clamp((temperature+0.5f) / 1.75f, 0.0f, 1.0f);
		return GrassColors.getColor(f, MathHelper.clamp(humidity, 0.0f, 1.0f));
	}

	public static int getFoliageColormapColor(float temperature, float humidity) {
		float f = MathHelper.clamp((temperature+0.5f) / 1.75f, 0.0f, 1.0f);
		return FoliageColors.getColor(f, MathHelper.clamp(humidity, 0.0f, 1.0f));
	}

	public static void reloadColorRendering() {
		//DynamicSeasons.LOGGER.info("it works!!!!");
		MinecraftClient client = MinecraftClient.getInstance();

		//client.world.reloadColor();

		//client.worldRenderer.reload(); //igual que f3+a
		//client.worldRenderer.cleanUp(); //nada visible
		//client.worldRenderer.scheduleTerrainUpdate(); //nada visible
		//client.gameRenderer.reset(); //nada visible??


		ClientWorld world = client.world;
		if (world == null) return;
		if (client.player == null) return;
		BlockPos pos = client.player.getBlockPos();
		ChunkPos cp = new ChunkPos(pos.getX() >> 4, pos.getZ() >> 4);

		int viewDistance = client.options.getViewDistance().getValue();
		//DynamicSeasons.LOGGER.info("PLAYER CHUNK POS = "+cp.x+" "+cp.z);


		//List<Chunk> list = new ArrayList<>();
		//BlockBox blockBox = BlockBox.create(pos.add(-viewDistance, 0, -viewDistance), pos.add(viewDistance, 0, viewDistance));
		//Chunk chunk;
		for(int chunkx = cp.x - viewDistance; chunkx <= cp.x + viewDistance; ++chunkx) {
			for(int chunkz = cp.z - viewDistance; chunkz <= cp.z + viewDistance; ++chunkz) {

				world.resetChunkColor(new ChunkPos(chunkx, chunkz));
				for (int k = world.getBottomSectionCoord(); k < world.getTopSectionCoord(); k++) {
					client.worldRenderer.scheduleBlockRender(chunkx, k, chunkz);
				}

			}
		}
		/*for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				for (int k = world.getBottomSectionCoord(); k < world.getTopSectionCoord(); k++) {
					client.worldRenderer.scheduleBlockRender(serialized.pos().x + i, k, serialized.pos().z + j);
				}
			}
		}*/

		//client.worldRenderer.cleanUp();
		//client.gameRenderer.reset();
		//world.runQueuedChunkUpdates();

		/*Iterator<Chunk> var16 = list.iterator();

		while(var16.hasNext()) {
			chunk = var16.next();
			chunk.setNeedsSaving(true);
		}*/

		//world.getChunkManager().threadedAnvilChunkStorage.sendChunkBiomePackets(list);


	}
}
