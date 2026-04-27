package dev.sillibeans.everythingpickaxe.items;

import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class EverythingAxeItem extends EverythingTool {
    private static final int DAMAGE = 9;

    public EverythingAxeItem() {
        super((float)Math.E, 0.8f, DAMAGE, 3000);
    }

    @Override
    public boolean mineBlock(
            @NotNull ItemStack itemstack,
            Level world,
            @NotNull BlockState blockstate,
            @NotNull BlockPos pos,
            @NotNull LivingEntity entity
    ) {
        if (!world.isClientSide()) {
            entity.addEffect(
                new MobEffectInstance(
                    MobEffects.WEAKNESS,
                    100,
                    999,
                    false,
                    false
                )
            );
        }

        return super.mineBlock(itemstack, world, blockstate, pos, entity);
    }

    @Override
    public boolean hurtEnemy(
            ItemStack itemstack,
            @NotNull LivingEntity entity,
            @NotNull LivingEntity sourceentity
    ) {
        if (sourceentity instanceof Player player) {
            player.addEffect(
                new MobEffectInstance(
                    MobEffects.WEAKNESS,
                    20*2,
                    999,
                    false,
                    false
                )
            );
        }

        return super.hurtEnemy(itemstack, entity, sourceentity);
    }
}
