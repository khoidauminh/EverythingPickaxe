package dev.sillibeans.everythingpickaxe.items;


import net.minecraft.core.registries.Registries;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;

import dev.sillibeans.everythingpickaxe.EverythingPickaxe;

import dev.sillibeans.everythingpickaxe.blocks.EverythingPickaxeBlocks;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class EverythingPickaxeItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(
            Registries.ITEM,
            EverythingPickaxe.MOD_ID
    );

    public static final Supplier<Item> EVERYTHING_BAGEL =
            ITEMS.register("everything_bagel", () ->
                    new EverythingBagelItem(false, true)
            );

    public static final Supplier<Item> ENCHANTED_EVERYTHING_BAGEL =
            ITEMS.register("enchanted_everything_bagel", () ->
                    new EverythingBagelItem(true, true)
            );

    public static final Supplier<Item> EVERYTHING_DOUGH =
            ITEMS.register("everything_dough", () ->
                    new EverythingBagelItem(false, false)
            );

    public static final Supplier<Item> BAKED_EGG = ITEMS.register(
            "baked_egg",
            () ->
                    new Item(
                            new Item.Properties()
                                    .stacksTo(64)
                                    .food(
                                            (new FoodProperties.Builder()).nutrition(3)
                                                    .saturationModifier(0.8f)
                                                    .build()
                                    )
                    )
    );

    public static final Supplier<Item> BAKED_SCRAMBLED_EGG = ITEMS.register(
            "baked_scrambled_egg",
            () -> new ScrambledEgg(true)
    );

    public static final Supplier<Item> RAW_SCRAMBLED_EGG =
            ITEMS.register("raw_scrambled_egg", () -> new ScrambledEgg(false));

    public static final Supplier<Item> ENCHANTED_EVERYTHING_DOUGH =
            ITEMS.register("enchanted_everything_dough", () ->
                    new EverythingBagelItem(true, false)
            );

    public static final Supplier<Item> EVERYTHING_PICKAXE =
            ITEMS.register("everything_pickaxe", EverythingPickaxeItem::new);

    public static final Supplier<Item> EVERYTHING_AXE = ITEMS.register(
            "everything_axe",
            EverythingAxeItem::new
    );

    public static final Supplier<Item> EVERYTHING_DAGGER =
            ITEMS.register("everything_dagger", EverythingDaggerItem::new);

    public static final Supplier<Item> SPOON = ITEMS.register(
            "wooden_spoon",
            () -> new SpoonItem(false)
    );

    public static final Supplier<Item> SPOON_FILLED = ITEMS.register(
            "filled_wooden_spoon",
            () -> new SpoonItem(true)
    );

    public static final Supplier<Item> UNSCRAMBLED_EGG = ITEMS.register(
            "unscrambled_egg",
            UnscrambledEgg::new
    );

    public static final Supplier<Item> OBSIDIAN_ANVIL = ITEMS.register(
            "obsidian_anvil",
            () -> new BlockItem(EverythingPickaxeBlocks.OBSIDIAN_ANVIL.get(), new Item.Properties().stacksTo(1))
    );

    public static final Supplier<Item> CRYING_OBSIDIAN_ANVIL = ITEMS.register(
            "crying_obsidian_anvil",
            () -> new BlockItem(EverythingPickaxeBlocks.CRYING_OBSIDIAN_ANVIL.get(), new Item.Properties().stacksTo(1))
    );
}
