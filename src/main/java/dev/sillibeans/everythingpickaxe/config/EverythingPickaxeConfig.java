package dev.sillibeans.everythingpickaxe.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public record EverythingPickaxeConfig(ModConfigSpec.IntValue swingTime) {
    public EverythingPickaxeConfig(ModConfigSpec.Builder swingTime) {
        this.swingTime = swingTime
            .translation("config.everythingpickaxe.swing_time")
            .comment("Adjust your hand swing animation duration (1 to disable).")
            .defineInRange("swingTime", 6, 1, 20);
    }
}