package dev.sillibeans.everythingpickaxe;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import dev.sillibeans.everythingpickaxe.blocks.EverythingPickaxeBlocks;
import dev.sillibeans.everythingpickaxe.config.Config;
import dev.sillibeans.everythingpickaxe.datacomponent.EverythingPickaxeDataComponents;
import dev.sillibeans.everythingpickaxe.items.*;

import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.component.CustomData;

import static dev.sillibeans.everythingpickaxe.items.EverythingPickaxeItems.UNSCRAMBLED_EGG;

@Mod(EverythingPickaxe.MOD_ID)
public final class EverythingPickaxe {

    public static final String MOD_ID = "everythingpickaxe";
    public static Config CONFIG = new Config();

    public static final Logger LOGGER = LogManager.getLogger(EverythingPickaxe.class);

    public EverythingPickaxe(IEventBus modEventBus, ModContainer modContainer) {
        System.out.println("Hello from EverythingPickaxe!");

        EverythingPickaxeDataComponents.DATA_COMPONENT_TYPES.register(modEventBus);
        EverythingPickaxeBlocks.BLOCKS.register(modEventBus);
        EverythingPickaxeItems.ITEMS.register(modEventBus);

        modEventBus.addListener(EverythingPickaxe::buildContents);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        LOGGER.info("HELLO FROM COMMON SETUP");
    }

    public static void buildContents(BuildCreativeModeTabContentsEvent event) {
        final var tab = event.getTabKey();

        if (tab == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            event.accept(EverythingPickaxeItems.EVERYTHING_PICKAXE.get());
            event.accept(EverythingPickaxeItems.EVERYTHING_AXE.get());
            event.accept(EverythingPickaxeItems.EVERYTHING_DAGGER.get());
            event.accept(EverythingPickaxeItems.SPOON.get());
        }

        if (tab == CreativeModeTabs.FUNCTIONAL_BLOCKS) {
            event.accept(EverythingPickaxeItems.OBSIDIAN_ANVIL.get());
            event.accept(EverythingPickaxeItems.CRYING_OBSIDIAN_ANVIL.get());
        }

        if (tab == CreativeModeTabs.FOOD_AND_DRINKS) {
            event.accept(EverythingPickaxeItems.EVERYTHING_BAGEL.get());
            event.accept(EverythingPickaxeItems.ENCHANTED_EVERYTHING_BAGEL.get());
            event.accept(EverythingPickaxeItems.EVERYTHING_DOUGH.get());
            event.accept(EverythingPickaxeItems.ENCHANTED_EVERYTHING_DOUGH.get());
            event.accept(EverythingPickaxeItems.BAKED_SCRAMBLED_EGG.get());
            event.accept(EverythingPickaxeItems.UNSCRAMBLED_EGG.get());
            event.accept(EverythingPickaxeItems.BAKED_EGG.get());
        }
    }
}
