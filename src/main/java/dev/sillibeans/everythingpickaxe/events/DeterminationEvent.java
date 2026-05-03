package dev.sillibeans.everythingpickaxe.events;

import dev.sillibeans.everythingpickaxe.EverythingPickaxe;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

public class DeterminationEvent {
    @SubscribeEvent
    public static void onHarvestCheck(PlayerEvent.HarvestCheck event) {
        Player player = event.getEntity();
        final boolean hasEnoughLevels = player.experienceLevel >= 40f;

        if (EverythingPickaxe.CONFIG.determination().get() && hasEnoughLevels) {
            event.setCanHarvest(true);
        }
    }
}