package net.forixaim.omneria.client.particles.types;

import com.mojang.brigadier.StringReader;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import org.jetbrains.annotations.NotNull;
import yesman.epicfight.api.animation.Joint;

import java.util.Objects;

public class JointParticleType extends ParticleType<JointParticleType> implements ParticleOptions {

    public static final Codec<JointParticleType> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("entity_id").forGetter(o -> o.joint.getId())
    ).apply(instance, JointParticleType::new));

    private final Joint joint;
    private final JointParticleType type;

    public static final Deserializer<JointParticleType> DESERIALIZER = new Deserializer<>() {
        @Override
        public @NotNull JointParticleType fromCommand(@NotNull ParticleType<JointParticleType> type, StringReader reader) {
            int entityId = Integer.parseInt(reader.readUnquotedString());
            return new JointParticleType(entityId, false);
        }

        @Override
        public @NotNull JointParticleType fromNetwork(@NotNull ParticleType<JointParticleType> type, FriendlyByteBuf buf) {
            return new JointParticleType(buf.readVarInt(), false);
        }
    };

    public JointParticleType(int entityId, boolean override) {
        super(override, DESERIALIZER);
        this.type = null;
        this.joint = null;
    }

    public JointParticleType(boolean override) {
        super(override, DESERIALIZER);
        this.type = null;
        this.joint = null;
    }

    public JointParticleType(int entityId) {
        this(false);
    }

    public JointParticleType(Joint joint, JointParticleType type) {
        super(false, DESERIALIZER);
        this.joint = joint;
        this.type = type;
    }

    public void writeToNetwork(@NotNull FriendlyByteBuf pBuffer)
    {
        pBuffer.writeVarInt(this.joint.getId());
    }

    public @NotNull String writeToString() {
        return Objects.requireNonNull(BuiltInRegistries.PARTICLE_TYPE.getKey(this)).toString();
    }

    @Override
    public Codec<JointParticleType> codec() {
        return CODEC;
    }

    public @NotNull JointParticleType getType() {
        return type != null ? type : this;
    }

    public Joint getEntityId() {
        return joint;
    }
}