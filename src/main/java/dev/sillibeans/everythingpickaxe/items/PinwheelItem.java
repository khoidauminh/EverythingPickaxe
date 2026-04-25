package dev.sillibeans.everythingpickaxe.items;

import dev.sillibeans.everythingpickaxe.datacomponent.EverythingPickaxeDataComponents;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class PinwheelItem extends Item {
    public PinwheelItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, @NotNull InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);

        long fanTick = stack.getOrDefault(EverythingPickaxeDataComponents.FAN_FINISH, 0L);
        long time = level.getGameTime();

        fanTick = Math.clamp(fanTick + 30, time, time + 80L);
        stack.set(EverythingPickaxeDataComponents.FAN_FINISH, fanTick);

        player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 80, 0, false, false));

        return InteractionResultHolder.consume(stack);
    }

    @Override
    public void inventoryTick(@NotNull ItemStack stack, Level level, @NotNull Entity entity, int slotId, boolean isSelected) {
        if (level.getGameTime() % 10 != 0) {
            return;
        }

        long fanTick = stack.getOrDefault(EverythingPickaxeDataComponents.FAN_FINISH, 0L);

        if (!level.isClientSide() && fanTick > level.getGameTime()) {
            level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.FIREWORK_ROCKET_LAUNCH, SoundSource.NEUTRAL);
        }
    }
}
