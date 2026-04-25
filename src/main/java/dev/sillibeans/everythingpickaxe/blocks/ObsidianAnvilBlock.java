package dev.sillibeans.everythingpickaxe.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AnvilBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

public class ObsidianAnvilBlock extends AnvilBlock {
	public ObsidianAnvilBlock(Properties properties) {
		super(properties);
	}

	@Override
	public @NotNull InteractionResult useWithoutItem(@NotNull BlockState state, Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull BlockHitResult hit) {
		if (!level.isClientSide) {
			ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);

			if (stack.is(Items.LAVA_BUCKET) && state.is(EverythingPickaxeBlocks.CRYING_OBSIDIAN_ANVIL.get())) {
				level.setBlock(
					pos, EverythingPickaxeBlocks.OBSIDIAN_ANVIL.get().defaultBlockState().setValue(FACING, (Direction)state.getValue(FACING)),
					Block.UPDATE_ALL
				);

				level.playSound(null, pos, SoundEvents.BUCKET_EMPTY_LAVA, SoundSource.BLOCKS, 0.5F, 1);
				level.playSound(null, pos, SoundEvents.ANVIL_USE, SoundSource.BLOCKS, 0.5F, 1);

				player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.BUCKET));

				return InteractionResult.SUCCESS;
			}
		}

		return super.useWithoutItem(state, level, pos, player, hit);
	}

	@Override
	public void onRemove(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState newState, boolean isMoving) {
		super.onRemove(state, level, pos, newState, isMoving);
		if (newState.is(Blocks.AIR)) {
			final int dropAmount = level.getRandom().nextIntBetweenInclusive(1, 2);
			final ItemStack stack = new ItemStack(Items.CRYING_OBSIDIAN, dropAmount);
			final var x = pos.getX()+0.5;
			final var y = pos.getY()+0.5;
			final var z = pos.getZ()+0.5;
			level.addFreshEntity(new ItemEntity(level, x, y, z, stack));
		}
	}
}
