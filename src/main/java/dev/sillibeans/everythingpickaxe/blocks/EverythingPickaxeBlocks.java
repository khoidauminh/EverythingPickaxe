package dev.sillibeans.everythingpickaxe.blocks;

import net.minecraft.core.registries.Registries;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static dev.sillibeans.everythingpickaxe.EverythingPickaxe.MOD_ID;

public class EverythingPickaxeBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(
            Registries.BLOCK,
            MOD_ID
    );

    public static final Supplier<Block> OBSIDIAN_ANVIL = BLOCKS.register(
        "obsidian_anvil",
        () -> new ObsidianAnvilBlock(
                Properties.of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(5.0F, 1200.0F).sound(SoundType.ANVIL).pushReaction(PushReaction.BLOCK)
        )
    );

    public static final Supplier<Block> CRYING_OBSIDIAN_ANVIL = BLOCKS.register(
        "crying_obsidian_anvil",
        () -> new ObsidianAnvilBlock(
                Properties.of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(5.0F, 1200.0F).sound(SoundType.ANVIL).pushReaction(PushReaction.BLOCK)
        )
    );
}
