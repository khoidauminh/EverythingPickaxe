package dev.sillibeans.everythingpickaxe.mixin;

import net.minecraft.world.level.block.AnvilBlock;
import net.minecraft.world.level.block.state.BlockState;
import dev.sillibeans.everythingpickaxe.blocks.EverythingPickaxeBlocks;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AnvilBlock.class)
public class ObsidianAnvilMixin {
    @Inject(method = "damage", at = @At("HEAD"), cancellable = true)
    private static void damage(BlockState blockState, CallbackInfoReturnable<BlockState> cir) {
        try {
            if (blockState.is(EverythingPickaxeBlocks.OBSIDIAN_ANVIL.get())) {
                cir.setReturnValue(EverythingPickaxeBlocks.CRYING_OBSIDIAN_ANVIL.get().defaultBlockState());
            }

            if (blockState.is(EverythingPickaxeBlocks.CRYING_OBSIDIAN_ANVIL.get())) {
                cir.setReturnValue(null);
            }
        } catch (Exception ignored) {}
    }
}
