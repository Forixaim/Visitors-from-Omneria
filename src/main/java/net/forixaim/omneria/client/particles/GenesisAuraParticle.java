package net.forixaim.omneria.client.particles;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.forixaim.omneria.client.particles.types.TrackingParticleOptions;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class GenesisAuraParticle extends TextureSheetParticle {
    private final SpriteSet frames;

    private final int trackedEntityId;

    protected GenesisAuraParticle(ClientLevel pLevel, double pX, double pY, double pZ, int id, SpriteSet sprites) {
        super(pLevel, pX, pY, pZ);
        this.trackedEntityId = id;
        this.rCol = 1.0F;
        this.gCol = 1.0F;
        this.bCol = 1.0F;
        this.lifetime = 4; // total ticks
        this.quadSize = 2.5f;

        frames = sprites;
        setSpriteFromAge(frames);
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;

        if (this.age++ >= this.lifetime) {
            this.remove();
        } else {
            this.setSpriteFromAge(this.frames);
        }
    }

    @Override
    public void render(@NotNull VertexConsumer pBuffer, @NotNull Camera pRenderInfo, float pPartialTicks) {
        super.render(pBuffer, pRenderInfo, pPartialTicks);
        Entity e = level.getEntity(trackedEntityId);
        if (e != null) {
            setPos(e.getX(), e.getY() + e.getEyeHeight(), e.getZ());
        }
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    protected int getLightColor(float pPartialTick) {
        return 15728880;
    }

    public record Provider(SpriteSet sprites) implements ParticleProvider<TrackingParticleOptions> {

        @Override
            public Particle createParticle(@NotNull TrackingParticleOptions typeIn, @NotNull ClientLevel worldIn,
                                           double x, double y, double z,
                                           double xSpeed, double ySpeed, double zSpeed) {
                return new GenesisAuraParticle(worldIn, x, y, z, typeIn.entityId(), sprites);

        }
    }
}
