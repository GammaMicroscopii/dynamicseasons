package com.gammamicroscopii.mixin;

import com.gammamicroscopii.mixed.ServerWorldMixed;
import com.gammamicroscopii.world.SeasonHelper;
import com.gammamicroscopii.world.SeasonInfo;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;

@Mixin(ServerWorld.class)
public class ServerWorldMixin implements ServerWorldMixed {

	private int seasonUpdateTimeout = 0;
	private int renderReloadCountdown = 0;
	private final SeasonInfo seasonInfo = new SeasonInfo( ((ServerWorld) (Object) this).getTimeOfDay());
	/*private float season;
	private float seasonalTempDelta;
	private float diurnalTempDelta;*/

	@Override
	public int getSeasonUpdateTimeout() {
		return seasonUpdateTimeout;
	}

	@Override
	public void setSeasonUpdateTimeout(int value) {
		seasonUpdateTimeout = value;
	}

	@Override
	public int getRenderReloadCountdown() {
		return renderReloadCountdown;
	}

	@Override
	public void setRenderReloadCountdown(int value) {
		renderReloadCountdown = value;
	}

	@Override
	public SeasonInfo getSeasonInfo() {
		return seasonInfo;
	}

	@Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;setTimeOfDay(J)V", shift = At.Shift.AFTER))
	public void injected(BooleanSupplier shouldKeepTicking, CallbackInfo ci) {
		SeasonHelper.updateSeason((ServerWorld)(Object) this, true);
	}

}
