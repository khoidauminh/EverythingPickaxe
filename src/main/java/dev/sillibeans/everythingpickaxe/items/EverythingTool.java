package dev.sillibeans.everythingpickaxe.items;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class EverythingTool extends TieredItem {
    final float miningSpeed;

    public EverythingTool(float miningSpeed, float attackSpeed, float attackDamange, int uses) {
        super(new Tier() {
            @Override
            public int getUses() {
                return uses;
            }

            @Override
            public float getSpeed() {
                return miningSpeed;
            }

            @Override
            public float getAttackDamageBonus() {
                return 0;
            }

            @Override
            public @NotNull TagKey<Block> getIncorrectBlocksForDrops() {
                return BlockTags.AIR;
            }

            @Override
            public int getEnchantmentValue() {
                return 4;
            }

            @Override
            public @NotNull Ingredient getRepairIngredient() {
                return Ingredient.of(new ItemStack(Items.OBSIDIAN));
            }
        },
            new Item.Properties()
                .attributes(
                    ItemAttributeModifiers.builder()
                        .add(
                            Attributes.ATTACK_DAMAGE,
                            new AttributeModifier(
                                BASE_ATTACK_DAMAGE_ID,
                                attackDamange - 1,
                                AttributeModifier.Operation.ADD_VALUE
                            ),
                            EquipmentSlotGroup.MAINHAND
                        )
                        .add(
                            Attributes.ATTACK_SPEED,
                            new AttributeModifier(
                                BASE_ATTACK_SPEED_ID,
                                attackSpeed - 4,
                                AttributeModifier.Operation.ADD_VALUE
                            ),
                            EquipmentSlotGroup.MAINHAND
                        )
                        .build()
                )
                .fireResistant()
        );

        this.miningSpeed = miningSpeed;
    }

    @Override
    public float getDestroySpeed(@NotNull ItemStack itemstack, BlockState blockstate) {
        return blockstate.is(Blocks.COBWEB) ? 15F : this.miningSpeed;
    }

    @Override
    public @NotNull InteractionResult useOn(@NotNull UseOnContext ctx) {
        final List<Item> uses = List.of(
            Items.NETHERITE_PICKAXE,
            Items.NETHERITE_AXE,
            Items.NETHERITE_SWORD,
            Items.NETHERITE_SHOVEL
        );

        if (ctx.getPlayer().isShiftKeyDown()) {
            final InteractionResult res = Items.NETHERITE_HOE.useOn(ctx);
            if (res != InteractionResult.PASS) {
                return res;
            }
        } else {
            for (final var i: uses) {
                final InteractionResult res = i.useOn(ctx);
                if (res != InteractionResult.PASS) {
                    return res;
                }
            }
        }

        return super.useOn(ctx);
    }

    @Override
    public boolean isCorrectToolForDrops(
        @NotNull ItemStack itemstack,
        @NotNull BlockState blockstate
    ) {
        return true;
    }

    @Override
    public boolean canPerformAction(@NotNull ItemStack stack, @NotNull ItemAbility toolAction) {
        return  ItemAbilities.DEFAULT_AXE_ACTIONS.contains(toolAction) ||
                ItemAbilities.DEFAULT_HOE_ACTIONS.contains(toolAction) ||
                ItemAbilities.DEFAULT_SHOVEL_ACTIONS.contains(toolAction) ||
                ItemAbilities.DEFAULT_PICKAXE_ACTIONS.contains(toolAction) ||
                ItemAbilities.DEFAULT_SWORD_ACTIONS.contains(toolAction);
    }

    @Override
    public boolean mineBlock(
        @NotNull ItemStack itemstack,
        Level world,
        @NotNull BlockState blockstate,
        @NotNull BlockPos pos,
        @NotNull LivingEntity entity
    ) {
        if (!world.isClientSide()) {
            itemstack.hurtAndBreak(
                1,
                entity,
                LivingEntity.getSlotForHand(entity.getUsedItemHand())
            );
        }
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

        return true;
    }
}
