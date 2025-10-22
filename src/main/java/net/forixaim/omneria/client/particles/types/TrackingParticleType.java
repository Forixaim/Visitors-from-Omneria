package net.forixaim.omneria.client.particles.types;

import com.mojang.serialization.Codec;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class TrackingParticleType extends ParticleType<TrackingParticleOptions> {


    private final int entityId;

    public TrackingParticleType(TrackingParticleType existingType, int entityId) {
        super(existingType.getOverrideLimiter(), TrackingParticleOptions.DESERIALIZER);
        this.entityId = entityId;
    }

    public TrackingParticleType(boolean override) {
        super(override, TrackingParticleOptions.DESERIALIZER);
        this.entityId = -1;
    }

    public void writeToNetwork(@NotNull FriendlyByteBuf pBuffer)
    {
        pBuffer.writeVarInt(this.entityId);
    }

    public @NotNull String writeToString() {
        return Objects.requireNonNull(BuiltInRegistries.PARTICLE_TYPE.getKey(this)).toString();
    }

    @Override
    public Codec<TrackingParticleOptions> codec() {
        return TrackingParticleOptions.CODEC;
    }

    public @NotNull TrackingParticleType getType() {
        return this;
    }

    public int getEntityId() {
        return entityId;
    }
}