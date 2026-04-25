package dev.sillibeans.everythingpickaxe.mixin;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MapItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static dev.sillibeans.everythingpickaxe.util.Utils.getMapScale;

@Mixin(Item.class)
public class MapShiftMixin {
    @Inject(method = "overrideOtherStackedOnMe", at = @At("HEAD"), cancellable = true)
    public void overrideOtherStackedOnMe(ItemStack itemStack, ItemStack itemStack2, Slot slot, ClickAction clickAction, Player player, SlotAccess slotAccess, CallbackInfoReturnable<Boolean> cir) {
        if (itemStack2.is(Items.WATER_BUCKET) && itemStack.is(Items.FILLED_MAP) && clickAction == ClickAction.SECONDARY) {
            var map = MapItem.create(player.level(), player.getBlockX(), player.getBlockZ(), getMapScale(itemStack2, player.level()), true, false);
            slot.set(map);
            player.playSound(SoundEvents.PLAYER_SPLASH);
            player.playSound(SoundEvents.UI_CARTOGRAPHY_TABLE_TAKE_RESULT);
            cir.setReturnValue(true);
        }
    }
}
