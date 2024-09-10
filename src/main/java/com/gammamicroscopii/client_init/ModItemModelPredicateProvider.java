package com.gammamicroscopii.client_init;

import com.gammamicroscopii.DynamicSeasons;
import com.gammamicroscopii.Temperature;
import com.gammamicroscopii.item.ModItems;
import com.gammamicroscopii.item.ThermometerItem;
import com.gammamicroscopii.mixed.BiomeMixed;
import com.gammamicroscopii.world.TemperatureCalculation;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.ClampedModelPredicateProvider;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class ModItemModelPredicateProvider {

	public static void registerPredicates() {
		DynamicSeasons.LOGGER.info("Registering item model predicates for mod "+DynamicSeasons.MOD_ID);

		/*ModelPredicateProviderRegistry.register(ModItems.CALENDAR, new Identifier("season"), (itemStack, clientWorld, livingEntity, seed) -> {
			float years = (float)clientWorld.getTimeOfDay() / DynamicSeasons.YEAR_DURATION;
			return years - (int) years;
		});*/

		ModelPredicateProviderRegistry.register(ModItems.CALENDAR, new Identifier("season"), new ClampedModelPredicateProvider() {
			private double time;
			private double step;
			private long lastTick;

			@Override
			public float unclampedCall(ItemStack itemStack, @Nullable ClientWorld clientWorld, @Nullable LivingEntity livingEntity, int i) {
				Entity entity = livingEntity != null ? livingEntity : itemStack.getHolder();
				if (entity == null) {
					return 0.0F;
				} else {
					if (clientWorld == null && entity.getWorld() instanceof ClientWorld) {
						clientWorld = (ClientWorld)entity.getWorld();
					}

					if (clientWorld == null) {
						return 0.0F;
					} else {
						double d;
						if (clientWorld.getDimension().natural()) {
							float years = (float)clientWorld.getTimeOfDay() / DynamicSeasons.YEAR_DURATION;
							d = years - (int) years;
						} else {
							d = Math.random();
						}

						d = this.getTime(clientWorld, d);
						return (float)d;
					}
				}
			}

			private double getTime(World world, double skyAngle) {
				if (world.getTime() != this.lastTick) {
					this.lastTick = world.getTime();
					double d = skyAngle - this.time;
					d = MathHelper.floorMod(d + 0.5, 1.0) - 0.5;
					this.step += d * 0.1;
					this.step *= 0.9;
					this.time = MathHelper.floorMod(this.time + this.step, 1.0);
				}

				return this.time;
			}
		});



		ModelPredicateProviderRegistry.register(ModItems.THERMOMETER, new Identifier("displayed_temperature"), (itemStack, clientWorld, livingEntity, seed) -> {
			Entity entity = livingEntity != null ? livingEntity : (itemStack.getHolder() != null ? itemStack.getHolder() : MinecraftClient.getInstance().player);
			if (entity == null) {
				return 0.0f;
			}
			//return (45.0f + Temperature.of(clientWorld.getBiome(BlockPos.ofFloored(entity.getPos())).value().getTemperature(), Temperature.Unit.VANILLA).get()) / 130f;

			//return (45.0f + TemperatureCalculation.getThermometerTemperature(((BiomeMixed)(Object)clientWorld.getBiome(BlockPos.ofFloored(entity.getPos())).value()).getClimate()).get(Temperature.Unit.CELSIUS)) / 130f;
			return (45.0f + itemStack.get(ThermometerItem.TEMPERATURE_COMPONENT)) / 130f;

		});
	}

}
