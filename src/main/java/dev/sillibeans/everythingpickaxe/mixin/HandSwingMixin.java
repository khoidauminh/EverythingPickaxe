package dev.sillibeans.everythingpickaxe.mixin;

import net.minecraft.world.entity.LivingEntity;
import dev.sillibeans.everythingpickaxe.EverythingPickaxe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class HandSwingMixin {
    @Inject(method = "getCurrentSwingDuration", at = @At("HEAD"), cancellable = true)
    public void swingTime(CallbackInfoReturnable<Integer> cir) {
        try {
            cir.setReturnValue(EverythingPickaxe.CONFIG.swingTime.getAsInt());
        } catch (Exception ignored) {}
    }
}