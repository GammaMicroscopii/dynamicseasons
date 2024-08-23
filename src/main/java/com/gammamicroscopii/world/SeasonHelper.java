package com.gammamicroscopii.world;

import com.gammamicroscopii.DynamicSeasons;
import com.gammamicroscopii.mixed.ServerWorldMixed;
import com.gammamicroscopii.network.DSNetworking;
import com.gammamicroscopii.resourceload.data.SeasonalBlockCycle;
import com.gammamicroscopii.resourceload.data.SeasonalBlockCycles;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.MathHelper;

public class SeasonHelper {



	/**
	 *
	 * @return The corresponding server's season
	 */
	private static SeasonInfo serverSeasonInfo() {
		return ((ServerWorldMixed) ServerWorldTick.server.getOverworld()).getSeasonInfo();
	}

	/**
	 * Called every 200 ticks from ServerWorldTick.init().
	 */
	public static void updateSeason(ServerWorld world, boolean shouldForceRenderReload) {
		ServerWorldMixed swm = ((ServerWorldMixed)world);
		swm.setSeasonUpdateTimeout(100);
		serverSeasonInfo().setSeason(ServerWorldTick.server.getOverworld().getTimeOfDay());
		if (shouldForceRenderReload || swm.getRenderReloadCountdown() <= 0) {
			swm.setRenderReloadCountdown(6);
			DSNetworking.updateRenderingForAllPlayers(ServerWorldTick.server);

		}
		swm.setRenderReloadCountdown(swm.getRenderReloadCountdown() - 1);
		for (SeasonalBlockCycle cycle : SeasonalBlockCycles.getSeasonalBlockCycleMap().values()) {
			cycle.updateCycleStage(swm.getSeasonInfo().getSeason());
		}
	}

	public static float getCurrentSeason() {
		return serverSeasonInfo().getSeason();
	}

	public static float getSeasonalTempDeltaFactor() {
		return serverSeasonInfo().getSeasonalTempDelta();
	}

	public static float getDiurnalTempDeltaFactor() {
		return serverSeasonInfo().getDiurnalTempDelta();
	}

	public static float timeOfDayToSeason(long time) {
		double years = time / (double) DynamicSeasons.YEAR_DURATION;
		return (float)(years - (int) years);
	}

	public static boolean isMiddleBetweenExtremes(float startSeason, float currentSeason, float endSeason) {
		if (hasReachedNewYear(startSeason, endSeason)) {
			return currentSeason > startSeason || currentSeason < endSeason;
		} else {
			return currentSeason > startSeason && currentSeason < endSeason;
		}
	}

	/**
	 * Both earlierSeason and laterSeason are assumed as truly being earlier and later, respectively
	 */
	public static boolean hasReachedNewYear(float earlierSeason, float laterSeason) {
		return laterSeason < earlierSeason;
	}

	public static float getRemainingIntervalProgress(float startSeason, float currentSeason, float endSeason) {
		float elapsedFromBeginning = currentSeason - startSeason;
		if (elapsedFromBeginning < 0) elapsedFromBeginning += 1f;
		float remaining = endSeason - currentSeason;
		if (remaining < 0) remaining += 1f;
		return remaining / (elapsedFromBeginning + remaining);
	}

	public static float advanceSeason(float initialSeason, float lapse) {
		float r = initialSeason + lapse;
		return r > 1f ? r - 1 : (r < 0f ? r + 1 : r);
	}

	public static float calculateInverseRate(float remainingConversionProgress, float conversionLength, float rateMultiplier, float lowerCap) {
		float square = remainingConversionProgress * remainingConversionProgress;
		return Math.max(rateMultiplier * square * square * remainingConversionProgress * conversionLength * ServerWorldTick.CHANCE_OF_SEASONAL_UPDATE_PER_TICK_FOR_ANY_BLOCK, lowerCap);
	}
}
