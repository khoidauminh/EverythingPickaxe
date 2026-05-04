package dev.sillibeans.everythingpickaxe;

import dev.sillibeans.everythingpickaxe.attachment.AttachmentTypes;
import dev.sillibeans.everythingpickaxe.blocks.EverythingPickaxeBlocks;
import dev.sillibeans.everythingpickaxe.config.EverythingPickaxeConfig;
import dev.sillibeans.everythingpickaxe.datacomponent.EverythingPickaxeDataComponents;
import dev.sillibeans.everythingpickaxe.events.DeterminationEvent;
import dev.sillibeans.everythingpickaxe.items.EverythingPickaxeItems;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(EverythingPickaxe.MOD_ID)
public final class EverythingPickaxe {

    public static final String MOD_ID = "everythingpickaxe";

    public static final EverythingPickaxeConfig CONFIG;
    public static final Logger LOGGER = LogManager.getLogger(EverythingPickaxe.class);
    static final ModConfigSpec CONFIG_SPEC;

    static {
        Pair<EverythingPickaxeConfig, ModConfigSpec> pair = new ModConfigSpec.Builder()
            .configure(EverythingPickaxeConfig::new);
        CONFIG = pair.getLeft();
        CONFIG_SPEC = pair.getRight();
    }

    public EverythingPickaxe(IEventBus modEventBus, ModContainer container) {
        LOGGER.info("Hello from EverythingPickaxe!");

        EverythingPickaxeDataComponents.DATA_COMPONENT_TYPES.register(modEventBus);
        AttachmentTypes.ATTACHMENT_TYPES.register(modEventBus);

        EverythingPickaxeBlocks.BLOCKS.register(modEventBus);
        EverythingPickaxeItems.ITEMS.register(modEventBus);

        container.registerConfig(ModConfig.Type.CLIENT, CONFIG_SPEC);

        modEventBus.addListener(EverythingPickaxe::buildContents);
        NeoForge.EVENT_BUS.register(DeterminationEvent.class);
    }

    public static void buildContents(BuildCreativeModeTabContentsEvent event) {
        final var tab = event.getTabKey();

        if (tab == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            event.accept(EverythingPickaxeItems.EVERYTHING_PICKAXE.get());
            event.accept(EverythingPickaxeItems.EVERYTHING_DAGGER.get());
            event.accept(EverythingPickaxeItems.SPOON.get());
            event.accept(EverythingPickaxeItems.PINWHEEL_ITEM.get());
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
