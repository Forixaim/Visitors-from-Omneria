package net.forixaim.omneria.client.particles.types;

import com.mojang.brigadier.StringReader;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.forixaim.omneria.registry.ParticleRegistry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import org.jetbrains.annotations.NotNull;

public record TrackingParticleOptions(int entityId) implements ParticleOptions {
    public static final Codec<TrackingParticleOptions> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("entity_id").forGetter(o -> o.entityId)
    ).apply(instance, TrackingParticleOptions::new));

    public static final Deserializer<TrackingParticleOptions> DESERIALIZER = new Deserializer<>() {
        @Override
        public @NotNull TrackingParticleOptions fromCommand(@NotNull ParticleType<TrackingParticleOptions> type, StringReader reader) {
            int entityId = Integer.parseInt(reader.readUnquotedString());
            return new TrackingParticleOptions(entityId);
        }

        @Override
        public @NotNull TrackingParticleOptions fromNetwork(@NotNull ParticleType<TrackingParticleOptions> type, FriendlyByteBuf buf) {
            return new TrackingParticleOptions(buf.readVarInt());
        }
    };

    @Override
    public @NotNull ParticleType<?> getType() {
        return ParticleRegistry.GENESIS_AURA.get();
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf buf) {
        buf.writeVarInt(this.entityId);
    }

    @Override
    public @NotNull String writeToString() {
        return Integer.toString(this.entityId);
    }
}