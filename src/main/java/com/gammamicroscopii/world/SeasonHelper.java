package com.gammamicroscopii.world;

import com.gammamicroscopii.DynamicSeasons;
import com.gammamicroscopii.mixed.ServerWorldMixed;
import com.gammamicroscopii.network.DSNetworking;
import com.gammamicroscopii.resourceload.data.SeasonalBlockCycle;
import com.gammamicroscopii.resourceload.data.SeasonalBlockCycles;
import net.minecraft.server.world.ServerWorld;

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
}
