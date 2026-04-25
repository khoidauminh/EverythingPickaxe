package dev.sillibeans.everythingpickaxe.items;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class EverythingAxeItem extends AxeItem {

    private static final int DAMAGE = 34;

    private static final Tier TOOL_TIER = new Tier() {
        @Override
        public int getUses() {
            return 200;
        }

        @Override
        public float getSpeed() {
            return 1.5f;
        }

        @Override
        public float getAttackDamageBonus() {
            return 0;
        }

        @Override
        public @NotNull TagKey<Block> getIncorrectBlocksForDrops() {
            return BlockTags.INCORRECT_FOR_NETHERITE_TOOL;
        }

        @Override
        public int getEnchantmentValue() {
            return 4;
        }

        @Override
        public @NotNull Ingredient getRepairIngredient() {
            return Ingredient.of(new ItemStack(Items.OBSIDIAN));
        }
    };

    public EverythingAxeItem() {
        super(
            TOOL_TIER,
            new Item.Properties()
                .attributes(
                    ItemAttributeModifiers.builder()
                        .add(
                            Attributes.ATTACK_DAMAGE,
                            new AttributeModifier(
                                BASE_ATTACK_DAMAGE_ID,
                                DAMAGE - 1,
                                AttributeModifier.Operation.ADD_VALUE
                            ),
                            EquipmentSlotGroup.MAINHAND
                        )
                        .add(
                            Attributes.ATTACK_SPEED,
                            new AttributeModifier(
                                BASE_ATTACK_SPEED_ID,
                                -3.3,
                                AttributeModifier.Operation.ADD_VALUE
                            ),
                            EquipmentSlotGroup.MAINHAND
                        )
                        .build()
                )
                .fireResistant()
        );
    }

    @Override
    public boolean isCorrectToolForDrops(
            @NotNull ItemStack itemstack,
            @NotNull BlockState blockstate
    ) {
        return true;
    }

    @Override
    public boolean hurtEnemy(
            ItemStack itemstack,
            @NotNull LivingEntity entity,
            @NotNull LivingEntity sourceentity
    ) {
        itemstack.hurtAndBreak(
            2,
            entity,
            LivingEntity.getSlotForHand(entity.getUsedItemHand())
        );

        if (sourceentity instanceof Player player) {
            final var duration =
                20 *
                (int) Math.min(
                    Math.sqrt(Math.min(DAMAGE, entity.getMaxHealth())),
                    20f
                );

            player.getCooldowns().addCooldown(itemstack.getItem(), duration);
            player.addEffect(
                new MobEffectInstance(
                    MobEffects.WEAKNESS,
                    duration,
                    999,
                    false,
                    false
                )
            );

            player.addEffect(
                new MobEffectInstance(
                    MobEffects.DAMAGE_RESISTANCE,
                    20,
                    3,
                    false,
                    false
                )
            );
        }

        return true;
    }

    @Override
    public float getDestroySpeed(@NotNull ItemStack itemstack, BlockState blockstate) {
        return blockstate.is(BlockTags.MINEABLE_WITH_AXE) ? 40f : 0.15f;
    }
}
