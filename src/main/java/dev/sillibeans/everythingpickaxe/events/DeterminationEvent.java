package dev.sillibeans.everythingpickaxe.events;

import dev.sillibeans.everythingpickaxe.attachment.AttachmentTypes;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerXpEvent;

public class DeterminationEvent {
    @SubscribeEvent
    public static void onHarvestCheck(PlayerEvent.HarvestCheck event) {
        Player player = event.getEntity();
        final boolean hasEnoughLevels = player.experienceLevel >= 30f;
        final boolean hasUnlockedDetermination = player.getData(AttachmentTypes.DETERMINATION);

        if (hasEnoughLevels || hasUnlockedDetermination) {
            if (!hasUnlockedDetermination) {
                player.setData(AttachmentTypes.DETERMINATION, true);
            }

            event.setCanHarvest(true);
        }
    }
}