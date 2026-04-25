package dev.sillibeans.everythingpickaxe.items;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class UnscrambledEgg extends Item {

    public static final String MIX_TAG = "mix";
    private static final String PROGRESS_TAG = "progress";
    private static final int PROGRESS = 10;

    public UnscrambledEgg() {
        super(
            new Item.Properties()
                .stacksTo(1)
        );
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(
            @NotNull Level world,
            @NotNull Player player,
            @NotNull InteractionHand hand
    ) {
        InteractionResultHolder<ItemStack> ar = super.use(world, player, hand);

        if (!world.isClientSide()) {
            player.swing(hand, true);
        }

        var itemstack = player.getItemInHand(hand);

        final int progressVal = itemstack
            .getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY)
            .copyTag()
            .getInt(PROGRESS_TAG);

        if (progressVal >= PROGRESS) {
            if (!world.isClientSide()) {
                final var x = player.getX();
                final var y = player.getY();
                final var z = player.getZ();

                ItemEntity itemEntity = new ItemEntity(
                    world,
                    x,
                    y,
                    z,
                    new ItemStack(EverythingPickaxeItems.SPOON.get(), 1)
                );

                world.addFreshEntity(itemEntity);
            }

            return InteractionResultHolder.success(
                new ItemStack(EverythingPickaxeItems.RAW_SCRAMBLED_EGG.get(), 1)
            );
        }

        final int mixVal =
            (itemstack
                    .getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY)
                    .copyTag()
                    .getInt(MIX_TAG) +
                1) %
            2;

        CustomData.update(DataComponents.CUSTOM_DATA, itemstack, tag ->
            tag.putInt(MIX_TAG, mixVal)
        );

        CustomData.update(DataComponents.CUSTOM_DATA, itemstack, tag ->
            tag.putInt(
                PROGRESS_TAG,
                progressVal + 1 + (player.getRandom().nextInt() % 2)
            )
        );

        return ar;
    }
}
