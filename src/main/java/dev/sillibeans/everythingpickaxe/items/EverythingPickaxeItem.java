package dev.sillibeans.everythingpickaxe.items;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class EverythingPickaxeItem extends TieredItem {

    private static final Tier TOOL_TIER = new Tier() {
        @Override
        public int getUses() {
            return 3461;
        }

        @Override
        public float getSpeed() {
            return (float) Math.PI;
        }

        @Override
        public float getAttackDamageBonus() {
            return 5;
        }

        @Override
        public @NotNull TagKey<Block> getIncorrectBlocksForDrops() {
            return BlockTags.AIR;
        }

        @Override
        public int getEnchantmentValue() {
            return 5;
        }

        @Override
        public @NotNull Ingredient getRepairIngredient() {
            return Ingredient.of(new ItemStack(Items.OBSIDIAN));
        }
    };

    public EverythingPickaxeItem() {
        super(
            TOOL_TIER,
            new Item.Properties()
                .attributes(
                    ItemAttributeModifiers.builder()
                        .add(
                            Attributes.ATTACK_DAMAGE,
                            new AttributeModifier(
                                BASE_ATTACK_DAMAGE_ID,
                                4,
                                AttributeModifier.Operation.ADD_VALUE
                            ),
                            EquipmentSlotGroup.MAINHAND
                        )
                        .add(
                            Attributes.ATTACK_SPEED,
                            new AttributeModifier(
                                BASE_ATTACK_SPEED_ID,
                                -2.8,
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

    @Override
    public float getDestroySpeed(@NotNull ItemStack itemstack, BlockState blockstate) {
        return blockstate.is(Blocks.COBWEB) ? 15F : (float) Math.PI;
    }

    @Override
    public boolean isCorrectToolForDrops(
            @NotNull ItemStack itemstack,
            @NotNull BlockState blockstate
    ) {
        return true;
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext ctx) {
        Level level = ctx.getLevel();
        BlockPos pos = ctx.getClickedPos();
        BlockState state = level.getBlockState(pos);
        Player player = ctx.getPlayer();
        assert player != null;

        if (state.is(Blocks.DIRT) || state.is(Blocks.GRASS_BLOCK) && player.isShiftKeyDown()) {
            level.setBlock(pos, Blocks.FARMLAND.defaultBlockState(), 3);

            level.playSound(
                player,
                pos,
                SoundEvents.HOE_TILL,
                SoundSource.BLOCKS
            );

            ctx
                .getItemInHand()
                .hurtAndBreak(
                    1,
                    player,
                    LivingEntity.getSlotForHand(ctx.getHand())
                );

            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        return InteractionResult.PASS;
    }
}
