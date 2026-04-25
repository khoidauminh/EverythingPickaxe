package dev.sillibeans.everythingpickaxe.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ParticleUtils;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BoneMealItem.class)
public class BonemealMixin {
    @Inject(method = "useOn", at = @At("HEAD"), cancellable = true)
    public void useOn(UseOnContext useOnContext, CallbackInfoReturnable<InteractionResult> cir) {
        try {
            Level level = useOnContext.getLevel();

            BlockPos blockPos = useOnContext.getClickedPos();
            BlockState state = level.getBlockState(blockPos);

            if (state.is(BlockTags.SMALL_FLOWERS)) {
                Player player = useOnContext.getPlayer();
                assert player != null;

                if (!level.isClientSide()) {
                    ItemStack bonemeal = player.getItemInHand(useOnContext.getHand());
                    bonemeal.shrink(1);

                    ItemStack item = new ItemStack(state.getBlock().asItem(), 1);

                    final var x = blockPos.getX() + 0.5;
                    final var y = blockPos.getY() + 0.5;
                    final var z = blockPos.getZ() + 0.5;

                    level.playSound(null, blockPos, SoundEvents.BONE_MEAL_USE, SoundSource.NEUTRAL);

                    ItemEntity entity = new ItemEntity(level, x, y, z, item);
                    entity.setDefaultPickUpDelay();
                    level.addFreshEntity(entity);
                } else {
                    ParticleUtils.spawnParticles(level, blockPos, 5, 0.5, 0.5, true, ParticleTypes.HAPPY_VILLAGER);
                }

                useOnContext.getPlayer().gameEvent(GameEvent.ITEM_INTERACT_FINISH);

                cir.setReturnValue(InteractionResult.sidedSuccess(level.isClientSide()));
            }
        } catch (Exception ignored) {}
    }
}
