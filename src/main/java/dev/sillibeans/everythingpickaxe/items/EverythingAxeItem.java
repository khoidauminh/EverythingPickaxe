package dev.sillibeans.everythingpickaxe.items;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class EverythingAxeItem extends EverythingTool {
    private final static float ATTACK_SPEED = 0.8f;

    public EverythingAxeItem() {
        super((float)Math.E, ATTACK_SPEED, 10, 3000);
    }

    @Override
    public boolean hurtEnemy(ItemStack itemstack, @NotNull LivingEntity entity, @NotNull LivingEntity sourceentity) {

        if (sourceentity instanceof Player player) {
            final int cooldown = (int)(2.3f * 20f / ATTACK_SPEED);

            player.addEffect(new MobEffectInstance(
                MobEffects.WEAKNESS,
                cooldown,
                1,
                false,
                true
            ));

            player.getCooldowns().addCooldown(itemstack.getItem(), cooldown);
        }

        return super.hurtEnemy(itemstack, entity, sourceentity);
    }
}
