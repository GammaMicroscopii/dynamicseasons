package com.gammamicroscopii.mixin;

import com.gammamicroscopii.world.SeasonHelper;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.command.TimeCommand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TimeCommand.class)
public class TimeCommandMixin {

	@Inject(method = "executeSet", at = @At(value = "TAIL"))
	private static void injected(ServerCommandSource source, int time, CallbackInfoReturnable<Integer> cir) {
		SeasonHelper.updateSeason(source.getServer().getOverworld(), true);
	}

	@Inject(method = "executeAdd", at = @At(value = "TAIL"))
	private static void injected0(ServerCommandSource source, int time, CallbackInfoReturnable<Integer> cir) {
		SeasonHelper.updateSeason(source.getServer().getOverworld(), true);
	}

}
