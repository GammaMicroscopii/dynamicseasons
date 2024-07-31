package com.gammamicroscopii.item;

import com.gammamicroscopii.Temperature;
import com.gammamicroscopii.colors.DSColorHelper;
import com.gammamicroscopii.mixed.BiomeMixed;
import com.gammamicroscopii.world.TemperatureCalculation;
import com.mojang.serialization.Codec;
import net.minecraft.client.item.TooltipType;
import net.minecraft.component.DataComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ThermometerItem extends Item {

	public static final DataComponentType<Float> TEMPERATURE_COMPONENT = DataComponentTypes.register("temperature", builder -> builder.codec(Codec.FLOAT).packetCodec(PacketCodecs.FLOAT));

	public ThermometerItem(Settings settings) {
		super(settings);
	}

	@Override
	public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
		if (world instanceof ServerWorld) {

			stack.set(TEMPERATURE_COMPONENT, TemperatureCalculation.getThermometerTemperature( ((BiomeMixed)(Object) world.getBiomeAccess().getBiome(entity.getBlockPos()).value()).getClimateServerSide() ).get(/*Temperature.Unit.CELSIUS*/));
		}
	}

	@Override
	public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
		float temperature = stack.get(TEMPERATURE_COMPONENT) != null ? stack.get(TEMPERATURE_COMPONENT) : Temperature.ABSOLUTE_ZERO.get();
		tooltip.add(Text.translatable("item.dynamicseasons.thermometer.temperature").append(": ").append(   Text.empty().append( String.format("%.1f", temperature) ).append(ScreenTexts.SPACE).append("ºC").formatted(Formatting.BOLD, Formatting.byColorIndex(DSColorHelper.getThermometerReadoutColor(temperature)))  )    );
	}

	@Override
	public boolean allowComponentsUpdateAnimation(PlayerEntity player, Hand hand, ItemStack oldStack, ItemStack newStack) {
		return oldStack.getCount() != newStack.getCount();
	}
}
