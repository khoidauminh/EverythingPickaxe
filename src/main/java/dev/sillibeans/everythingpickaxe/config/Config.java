package dev.sillibeans.everythingpickaxe.config;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;

import java.nio.file.Paths;

public class Config {
    public int swingTime = 6;
    public Config()  {
        String currentDir = Paths.get(".").toAbsolutePath().normalize().toString();
        System.out.println("Working Directory = " + currentDir);
    }

    private static boolean validateItemName(final Object obj) {
        return obj instanceof String itemName && BuiltInRegistries.ITEM.containsKey(ResourceLocation.parse(itemName));
    }
}