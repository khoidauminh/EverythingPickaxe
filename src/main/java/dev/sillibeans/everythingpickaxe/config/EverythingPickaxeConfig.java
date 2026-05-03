package dev.sillibeans.everythingpickaxe.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public record EverythingPickaxeConfig(ModConfigSpec.IntValue swingTime, ModConfigSpec.BooleanValue determination) {
    public EverythingPickaxeConfig(ModConfigSpec.Builder builder) {
        this(
            builder
                .translation("config.everythingpickaxe.swing_time")
                .comment("Adjust your hand swing animation duration (1 to disable).")
                .defineInRange("swingTime", 6, 1, 20),

            builder
                .translation("config.everythingpickaxe.determination")
                .comment("Once you reach level 20 you can mine anything with any tool, even with bare hands.")
                .define("determination", true)
        );
    }
}