package dev.sillibeans.everythingpickaxe.datacomponent;


import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import dev.sillibeans.everythingpickaxe.EverythingPickaxe;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class EverythingPickaxeDataComponents {

    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENT_TYPES =
            DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, EverythingPickaxe.MOD_ID);

    public static final Supplier<DataComponentType<ResourceLocation>> BLOCK_ID =
            DATA_COMPONENT_TYPES.register("block", () ->
                    DataComponentType.<ResourceLocation>builder()
                            .persistent(ResourceLocation.CODEC)
                            .networkSynchronized(ResourceLocation.STREAM_CODEC)
                            .build());

    public static final Supplier<DataComponentType<Long>> FAN_FINISH =
            DATA_COMPONENT_TYPES.register("fan_finish", () ->
                    DataComponentType.<Long>builder()
                            .persistent(Codec.LONG)
                            .build());
}
