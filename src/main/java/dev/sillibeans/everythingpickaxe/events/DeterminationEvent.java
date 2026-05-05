package dev.sillibeans.everythingpickaxe.events;

import dev.sillibeans.everythingpickaxe.EverythingPickaxe;
import dev.sillibeans.everythingpickaxe.attachment.AttachmentTypes;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerXpEvent;

public class DeterminationEvent {
    private static final int THRESHOLD = 30;
    private static final float MINE_SPEED_MUL_RANGE = 30f;
    private static final float WALK_SPEED_MUL_RANGE = 50f;
    private static final float STRENGH_FACTOR = 0.1f;
    private static final float DAMAGE_REDUCTION_RANGE = 100f;

    public static int getDeterminationlevel(Player player) {
        return Math.max(player.getData(AttachmentTypes.DETERMINATION), player.experienceLevel);
    }

    public static void applyAttributes(Player player, int level) {
        final int factor = Math.max(0, level - THRESHOLD);
        final float factorF = (float)factor;
        final float mineSpeedMul = Math.min(1f + factorF / MINE_SPEED_MUL_RANGE, 2f);
        final float walkSpeedMul = Math.min(1f + factorF / WALK_SPEED_MUL_RANGE, 1.2f) * 0.1f;
        final float strengthMul = Math.min(1f + factorF * STRENGH_FACTOR, 1.5f);
        player.getAttribute(Attributes.BLOCK_BREAK_SPEED).setBaseValue(mineSpeedMul);
        player.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(walkSpeedMul);
        player.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(strengthMul);
    }

    public static void updateDeterminationLevel(Player player, int levels) {
        final int currentLevel = player.getData(AttachmentTypes.DETERMINATION);
        if (levels > currentLevel) {
            EverythingPickaxe.LOGGER.info("{}'s determination level updated: {}", player.getName().getString(), levels);
            player.setData(AttachmentTypes.DETERMINATION, levels);
            applyAttributes(player, levels);
        }
    }

    @SubscribeEvent
    public static void onPlayerHurt(LivingDamageEvent.Pre event) {
        if (event.getEntity() instanceof Player player) {
            final float factor = Math.max(0, getDeterminationlevel(player) - THRESHOLD);
            final float damageReductionMul = Math.clamp(1f - factor / DAMAGE_REDUCTION_RANGE, 0.5f, 1f);
            float newAmount = event.getNewDamage() * damageReductionMul;
            event.setNewDamage(newAmount);
        }
    }

    @SubscribeEvent
    public static void onHarvestCheck(PlayerEvent.HarvestCheck event) {
        Player player = event.getEntity();

        final boolean hasEnoughLevels = player.experienceLevel >= THRESHOLD;
        final boolean hasDetermination = player.getData(AttachmentTypes.DETERMINATION) >= THRESHOLD;

        if (hasEnoughLevels || hasDetermination) {
            updateDeterminationLevel(player, player.experienceLevel);
            event.setCanHarvest(true);
        }
    }

    @SubscribeEvent
    public static void onExpCollect(PlayerXpEvent.PickupXp event) {
        Player player = event.getEntity();
        updateDeterminationLevel(player, player.experienceLevel);
    }

    @SubscribeEvent
    public static void onLevelUpgrade(PlayerXpEvent.LevelChange event) {
        Player player = event.getEntity();
        updateDeterminationLevel(player, player.experienceLevel + event.getLevels());
    }

    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();
        applyAttributes(player, getDeterminationlevel(player));
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        Player player = event.getEntity();
        player.removeData(AttachmentTypes.DETERMINATION);
        applyAttributes(player, getDeterminationlevel(player));
    }
}