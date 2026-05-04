package dev.sillibeans.everythingpickaxe.attachment;

import com.mojang.serialization.Codec;
import dev.sillibeans.everythingpickaxe.EverythingPickaxe;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.attachment.AttachmentSyncHandler;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class AttachmentTypes {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, "modid");

    public static final Supplier<AttachmentType<Boolean>> DETERMINATION = ATTACHMENT_TYPES.register("determination", () ->
        AttachmentType.builder(() -> false)
            .serialize(Codec.BOOL)
            .sync(ByteBufCodecs.BOOL)
            .build()
    );
}
