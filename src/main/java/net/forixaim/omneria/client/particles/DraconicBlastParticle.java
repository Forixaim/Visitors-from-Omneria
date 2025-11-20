package net.forixaim.omneria.client.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

public class DraconicBlastParticle extends TextureSheetParticle
{
    private final SpriteSet sprites;

    protected DraconicBlastParticle(ClientLevel pLevel, double pX, double pY, double pZ, SpriteSet pSprites) {
        super(pLevel, pX, pY, pZ);
        this.sprites = pSprites;
        this.setSpriteFromAge(pSprites);
        this.lifetime = 8;
        this.quadSize = 2f;
    }

    protected DraconicBlastParticle(ClientLevel pLevel, double pX, double pY, double pZ, SpriteSet pSprites, float q) {
        super(pLevel, pX, pY, pZ);
        this.sprites = pSprites;
        this.setSpriteFromAge(pSprites);
        this.lifetime = 8;
        this.quadSize = q;
    }

    @Override
    public void tick() {
        super.tick();
        this.setSpriteFromAge(sprites);
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    public int getLightColor(float pPartialTick) {
        float f = ((float)this.age + pPartialTick) / (float)this.lifetime;
        f = Mth.clamp(f, 0.0F, 1.0F);
        int i = super.getLightColor(pPartialTick);
        int j = i & 255;
        int k = i >> 16 & 255;
        j += (int)(f * 15.0F * 16.0F);
        if (j > 240) {
            j = 240;
        }

        return j | k << 16;
    }


    public record Provider(SpriteSet spriteSet) implements ParticleProvider<SimpleParticleType>
    {
        @Override
        public @NotNull Particle createParticle(@NotNull SimpleParticleType pType, @NotNull ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
            return new DraconicBlastParticle(pLevel, pX, pY, pZ, spriteSet);
        }
    }

    public record ProviderSmall(SpriteSet spriteSet) implements ParticleProvider<SimpleParticleType>
    {
        @Override
        public @NotNull Particle createParticle(@NotNull SimpleParticleType pType, @NotNull ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
            return new DraconicBlastParticle(pLevel, pX, pY, pZ, spriteSet, 0.5f);
        }
    }

    public record ProviderLarge(SpriteSet spriteSet) implements ParticleProvider<SimpleParticleType>
    {
        @Override
        public @NotNull Particle createParticle(@NotNull SimpleParticleType pType, @NotNull ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
            return new DraconicBlastParticle(pLevel, pX, pY, pZ, spriteSet, 10f);
        }
    }
}

