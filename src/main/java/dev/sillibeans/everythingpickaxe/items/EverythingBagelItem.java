package dev.sillibeans.everythingpickaxe.items;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class EverythingBagelItem extends Item {

    private final boolean isEnchanted;
    private final boolean isCooked;

    public EverythingBagelItem(boolean isenchanted, boolean iscooked) {
        super(
            new Item.Properties()
                .stacksTo(1)
                .food(
                    (new FoodProperties.Builder()).nutrition(iscooked ? 200 : 1)
                        .saturationModifier(iscooked ? 200 : 1)
                        .alwaysEdible()
                        .build()
                )
        );
        this.isEnchanted = isenchanted;
        this.isCooked = iscooked;
    }

    @Override
    public boolean isFoil(@NotNull ItemStack itemstack) {
        return this.isEnchanted;
    }

    @Override
    public int getUseDuration(@NotNull ItemStack itemstack, @NotNull LivingEntity livingEntity) {
        return 40;
    }

    @Override
    public @NotNull ItemStack finishUsingItem(
        @NotNull ItemStack itemstack,
        @NotNull Level world,
        @NotNull LivingEntity player
    ) {
        ItemStack retval = super.finishUsingItem(itemstack, world, player);

        if (!world.isClientSide() && isCooked) {
            player.addEffect(
                new MobEffectInstance(
                    MobEffects.SATURATION,
                    (this.isEnchanted) ? 576000 : 192000,
                    1,
                    false,
                    false
                )
            );
        }

        return retval;
    }
}
