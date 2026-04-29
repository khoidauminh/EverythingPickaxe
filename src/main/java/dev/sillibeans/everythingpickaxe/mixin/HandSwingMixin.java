package dev.sillibeans.everythingpickaxe.mixin;

import dev.sillibeans.everythingpickaxe.EverythingPickaxe;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class HandSwingMixin {
    @Inject(method = "getCurrentSwingDuration", at = @At("RETURN"), cancellable = true)
    public void swingTime(CallbackInfoReturnable<Integer> cir) {
        try {
            final int currentVal = cir.getReturnValue();
            final int newVal = currentVal * EverythingPickaxe.CONFIG.swingTime.getAsInt() / LivingEntity.SWING_DURATION;
            cir.setReturnValue(newVal);
        } catch (Exception ignored) {
        }
    }
}