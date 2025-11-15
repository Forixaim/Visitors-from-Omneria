package net.forixaim.omneria.client.particles.types;

import com.mojang.brigadier.StringReader;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class BeamParticleType extends ParticleType<BeamParticleType> implements ParticleOptions {
    public static final Codec<BeamParticleType> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.FLOAT.fieldOf("width").forGetter(o -> o.width)
    ).apply(instance, BeamParticleType::new));

    private final float width;
    private final BeamParticleType type;
    private final int r;
    private final int g;
    private final int b;
    private final int a;
    private final double x;
    private final double y;
    private final double z;
    private final double speed;

    public static final Deserializer<BeamParticleType> DESERIALIZER = new Deserializer<>() {
        @Override
        public @NotNull BeamParticleType fromCommand(@NotNull ParticleType<BeamParticleType> type, StringReader reader) {
            float entityId = Float.parseFloat(reader.readUnquotedString());
            return new BeamParticleType(entityId, false);
        }

        @Override
        public @NotNull BeamParticleType fromNetwork(@NotNull ParticleType<BeamParticleType> type, FriendlyByteBuf buf) {
            return new BeamParticleType(buf.readFloat(), false);
        }
    };

    public BeamParticleType(float width, boolean override) {
        super(override, DESERIALIZER);
        this.type = null;
        this.width = width;
        this.r = 255;
        this.g = 255;
        this.b = 255;
        this.a = 255;
        this.x = 0.0F;
        this.y = 0.0F;
        this.z = 0.0F;
        this.speed = 0.0F;
    }

    public BeamParticleType(boolean override) {
        super(override, DESERIALIZER);
        this.type = null;
        this.width = -1;
        this.r = 255;
        this.g = 255;
        this.b = 255;
        this.a = 255;
        this.x = 0.0F;
        this.y = 0.0F;
        this.z = 0.0F;
        this.speed = 0.0F;
    }

    public BeamParticleType(float width) {
        this(width, false);
    }

    public BeamParticleType(Float width, BeamParticleType type, Integer r, Integer g, Integer b, Integer a, Double x, Double y, Double z, Double speed) {
        super(false, DESERIALIZER);
        this.width = width;
        this.type = type;
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
        this.x = x;
        this.y = y;
        this.z = z;
        this.speed = speed;
    }

    public float getRed()
    {
        return this.r / 255.0F;
    }

    public float getGreen()
    {
        return this.g / 255.0F;
    }

    public float getBlue()
    {
        return this.b / 255.0F;
    }

    public Vec3 getEndPos()
    {
        return new Vec3(x, y, z);
    }

    public float getAlpha()
    {
        return this.a / 255.0F;
    }

    public double getSpeed()
    {
        return this.speed;
    }

    public void writeToNetwork(@NotNull FriendlyByteBuf pBuffer)
    {
        pBuffer.writeFloat(this.width);
    }

    public @NotNull String writeToString() {
        return Objects.requireNonNull(BuiltInRegistries.PARTICLE_TYPE.getKey(this)).toString();
    }

    @Override
    public Codec<BeamParticleType> codec() {
        return CODEC;
    }

    public @NotNull BeamParticleType getType() {
        return type != null ? type : this;
    }

    public float getWidth() {
        return width;
    }
}
