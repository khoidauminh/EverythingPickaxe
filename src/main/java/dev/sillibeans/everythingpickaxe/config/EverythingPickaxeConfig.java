package dev.sillibeans.everythingpickaxe.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class EverythingPickaxeConfig {
    public final ModConfigSpec.IntValue swingTime;

    public EverythingPickaxeConfig(ModConfigSpec.Builder builder) {
        swingTime = builder
                .translation("config.everythingpickaxe.swing_time")
                .comment("Adjust your hand swing animation duration (1 to disable).")
                .defineInRange("swingTime", 6, 1, 20);
    }
}