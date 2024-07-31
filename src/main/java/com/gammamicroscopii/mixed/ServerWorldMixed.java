package com.gammamicroscopii.mixed;

import com.gammamicroscopii.world.SeasonInfo;

public interface ServerWorldMixed {

	int getSeasonUpdateTimeout();

	void setSeasonUpdateTimeout(int value);

	int getRenderReloadCountdown();

	void setRenderReloadCountdown(int value);

	SeasonInfo getSeasonInfo();
}
