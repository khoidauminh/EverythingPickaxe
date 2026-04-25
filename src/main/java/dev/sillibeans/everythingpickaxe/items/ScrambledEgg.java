package dev.sillibeans.everythingpickaxe.items;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class ScrambledEgg extends Item {

    private final boolean isBaked;

    public ScrambledEgg(boolean isBaked) {
        super(
            new Item.Properties()
                .stacksTo(16)
                .food(
                    (new FoodProperties.Builder()).nutrition(isBaked ? 4 : 1)
                        .saturationModifier(isBaked ? 1 : 0)
                        .build()
                )
        );
        this.isBaked = isBaked;
    }

    @Override
    public @NotNull ItemStack finishUsingItem(
            @NotNull ItemStack itemstack,
            @NotNull Level world,
            @NotNull LivingEntity entity
    ) {
        super.finishUsingItem(itemstack, world, entity);

        if (!this.isBaked) {
            final var rand = entity.getRandom().nextFloat();

            if (rand < 0.6f) {
                entity.addEffect(
                    new MobEffectInstance(
                        MobEffects.CONFUSION,
                        200,
                        0,
                        false,
                        false
                    )
                );
            }
        }

        itemstack.setCount(itemstack.getCount()-1);

        if (!world.isClientSide()) {
            final var x = entity.getX();
            final var y = entity.getY();
            final var z = entity.getZ();

            ItemEntity itemEntity = new ItemEntity(
                world,
                x,
                y,
                z,
                new ItemStack(Items.BOWL, 1)
            );

            world.addFreshEntity(itemEntity);
        }

        return itemstack;
    }
}
