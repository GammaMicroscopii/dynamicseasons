package com.gammamicroscopii.mixed;

//import com.gammamicroscopii.seasonalweather.BiomeClimateParameters;

import com.gammamicroscopii.resourceload.data.BiomeClimate;
import net.minecraft.world.biome.Biome;

public interface BiomeMixed {

	/*public void addClimateParameters(BiomeClimateParameters parameters);

	public BiomeClimateParameters getClimateParameters();*/

	void setClimate(BiomeClimate climate);

	BiomeClimate getClimateClientSide();

	BiomeClimate getClimateServerSide();
}
