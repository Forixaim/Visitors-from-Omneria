package net.forixaim.omneria.client.particles.types;

import com.mojang.serialization.Codec;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;

public class JointParticleType extends ParticleType<JointParticleType> implements ParticleOptions {
    public JointParticleType(boolean pOverrideLimiter, Deserializer<JointParticleType> pDeserializer) {
        super(pOverrideLimiter, pDeserializer);
    }

    @Override
    public ParticleType<?> getType() {
        return null;
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf pBuffer) {

    }

    @Override
    public String writeToString() {
        return "";
    }

    @Override
    public Codec<JointParticleType> codec() {
        return null;
    }
}
