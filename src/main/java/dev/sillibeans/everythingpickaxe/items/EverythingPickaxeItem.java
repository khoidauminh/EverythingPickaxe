package dev.sillibeans.everythingpickaxe.items;

import dev.sillibeans.everythingpickaxe.datacomponent.EverythingPickaxeDataComponents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class EverythingPickaxeItem extends TieredItem {
    final float miningSpeed;

    public EverythingPickaxeItem(float miningSpeed, float attackSpeed, float attackDamange, int uses) {
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
        return Math.max(1f, (float) Math.sqrt(Math.E * blockstate.getBlock().defaultDestroyTime()));
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
            for (final var i : uses) {
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
        return ItemAbilities.DEFAULT_AXE_ACTIONS.contains(toolAction) ||
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
        PotionContents effects = itemstack.get(DataComponents.POTION_CONTENTS);
        var durabilityReduce = 2;

        if (effects != null) {
            effects.forEachEffect(entity::addEffect);
            durabilityReduce = 10;

            int use = itemstack.get(EverythingPickaxeDataComponents.PICKAXE_TIP_USE.get()) - 1;

            if (use == 0) {
                itemstack.remove(DataComponents.POTION_CONTENTS);
                itemstack.remove(EverythingPickaxeDataComponents.PICKAXE_TIP_USE.get());
                itemstack.remove(DataComponents.LORE);
                itemstack.set(DataComponents.ITEM_NAME, Component.translatable("item.everythingpickaxe.everything_pickaxe"));
            }

            itemstack.set(EverythingPickaxeDataComponents.PICKAXE_TIP_USE.get(), use);

            return true;
        }

        itemstack.hurtAndBreak(
            durabilityReduce,
            entity,
            LivingEntity.getSlotForHand(entity.getUsedItemHand())
        );

        return true;
    }

    @Override
    public boolean overrideOtherStackedOnMe(@NotNull ItemStack itemStack, ItemStack itemStack2, @NotNull Slot slot, @NotNull ClickAction clickAction, @NotNull Player player, @NotNull SlotAccess slotAccess) {
        if (itemStack2.is(Items.LINGERING_POTION) && itemStack.get(DataComponents.POTION_CONTENTS) == null && clickAction == ClickAction.SECONDARY) {
            itemStack.set(DataComponents.ITEM_NAME, Component.literal("Tipped Everything Pickaxe"));

            PotionContents potion = itemStack2.get(DataComponents.POTION_CONTENTS);

            List<Component> effects = new ArrayList<>();
            for (MobEffectInstance e : potion.getAllEffects()) {
                effects.add(e.getEffect().value().getDisplayName());
            }

            itemStack.set(DataComponents.POTION_CONTENTS, potion);
            itemStack.set(EverythingPickaxeDataComponents.PICKAXE_TIP_USE.get(), 4);
            player.playSound(SoundEvents.BOTTLE_FILL);
            itemStack.set(DataComponents.LORE, new ItemLore(effects));
            itemStack2.setCount(0);

            return true;
        }

        return false;
    }
}
