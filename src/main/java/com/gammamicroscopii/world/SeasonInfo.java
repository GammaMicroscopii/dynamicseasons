package com.gammamicroscopii.world;

import com.gammamicroscopii.DynamicSeasons;
import net.minecraft.util.math.MathHelper;

/**
 * Record-like class that stores a ServerWorld's season value, along with its seasonal and diurnal deltas (-1 to 1)
 */
public class SeasonInfo {

	private float season;
	private float seasonalTempDelta;
	private float diurnalTempDelta;

	/**
	 * @param time the timeOfDay of the ServerWorld.
	 */
	public SeasonInfo(long time) {
		this.setSeason(time);
	}

	/**
	 * @param time the timeOfDay of the ServerWorld.
	 */
	public void setSeason(long time) {
		float season = SeasonHelper.timeOfDayToSeason(time);
		this.season = season;
		this.seasonalTempDelta = MathHelper.sin(2.0f * MathHelper.PI * season);
		double days =  time / (double) DynamicSeasons.DAYLIGHT_CYCLE_DURATION;
		this.diurnalTempDelta = getDiurnalDelta((float)(days - (int) days));
		//DynamicSeasons.LOGGER.info("SEASON = "+season+"; SD = "+seasonalTempDelta+"; DD = "+diurnalTempDelta);
	}

	public float getSeason() {
		return season;
	}

	public float getSeasonalTempDelta() {
		return seasonalTempDelta;
	}

	public float getDiurnalTempDelta() {
		return diurnalTempDelta;
	}

	/**
	 *
	 * @param dayTime float such that it's 0 dawn, 0.25 noon, 0.5 sunset, 0.75 midnight
	 * @return -1 to 1
	 */
	public static float getDiurnalDelta(float dayTime) {
		//DynamicSeasons.LOGGER.info(Float.toString(dayTime));
		if (dayTime > 17f/48f && dayTime < 46f/48) {
			return MathHelper.cos((dayTime - 17f/48)*48 / (46f - 17) * MathHelper.PI);
		} else {
			if (dayTime > 45.999f/48) dayTime -= 1;
			return -MathHelper.cos((dayTime + 2f/48)*48 / (17f + 2) * MathHelper.PI);
		}
	}

}
