package net.forixaim.omneria.client.particles.types;

import com.mojang.brigadier.StringReader;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class TrackingParticleType extends ParticleType<TrackingParticleType> implements ParticleOptions {

    public static final Codec<TrackingParticleType> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("entity_id").forGetter(o -> o.entityId)
    ).apply(instance, TrackingParticleType::new));

    private final int entityId;
    private final TrackingParticleType type;

    public static final Deserializer<TrackingParticleType> DESERIALIZER = new Deserializer<>() {
        @Override
        public @NotNull TrackingParticleType fromCommand(@NotNull ParticleType<TrackingParticleType> type, StringReader reader) {
            int entityId = Integer.parseInt(reader.readUnquotedString());
            return new TrackingParticleType(entityId, false);
        }

        @Override
        public @NotNull TrackingParticleType fromNetwork(@NotNull ParticleType<TrackingParticleType> type, FriendlyByteBuf buf) {
            return new TrackingParticleType(buf.readVarInt(), false);
        }
    };

    public TrackingParticleType(int entityId, boolean override) {
        super(override, DESERIALIZER);
        this.type = null;
        this.entityId = entityId;
    }

    public TrackingParticleType(boolean override) {
        super(override, DESERIALIZER);
        this.type = null;
        this.entityId = -1;
    }

    public TrackingParticleType(Integer entityId) {
        this(entityId, false);
    }

    public TrackingParticleType(Integer entityId, TrackingParticleType type) {
        super(false, DESERIALIZER);
        this.entityId = entityId;
        this.type = type;
    }

    public void writeToNetwork(@NotNull FriendlyByteBuf pBuffer)
    {
        pBuffer.writeVarInt(this.entityId);
    }

    public @NotNull String writeToString() {
        return Objects.requireNonNull(BuiltInRegistries.PARTICLE_TYPE.getKey(this)).toString();
    }

    @Override
    public Codec<TrackingParticleType> codec() {
        return CODEC;
    }

    public @NotNull TrackingParticleType getType() {
        return type != null ? type : this;
    }

    public int getEntityId() {
        return entityId;
    }
}