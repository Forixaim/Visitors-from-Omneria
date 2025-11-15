package net.forixaim.omneria.client.particles;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.forixaim.omneria.client.particles.types.TrackingParticleType;
import net.forixaim.omneria.client.renderer.entity.projectile.DragonCannonRenderer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

public class DragonCannonParticle extends TextureSheetParticle
{
    private final Entity entity;


    protected DragonCannonParticle(ClientLevel pLevel, double pX, double pY, double pZ, int entityId) {
        super(pLevel, pX, pY, pZ);
        entity = pLevel.getEntity(entityId);
        this.lifetime = 1;
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public void render(@NotNull VertexConsumer pBuffer, @NotNull Camera pRenderInfo, float pPartialTicks) {
        EntityRenderer<?> entityRenderer = Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(entity);
        if (entityRenderer instanceof DragonCannonRenderer dragonCannonRenderer) {
            PoseStack pose = new PoseStack();
            pose.pushPose();

            pose.translate(this.x - pRenderInfo.getPosition().x,
                    this.y - pRenderInfo.getPosition().y,
                    this.z - pRenderInfo.getPosition().z);

            // scale / rotate if needed
            pose.scale(this.quadSize, this.quadSize, this.quadSize);

            Matrix4f mat = pose.last().pose();
            pose.popPose();
        }
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_LIT;
    }

    public record Provider() implements ParticleProvider<TrackingParticleType> {

        @Override
        public Particle createParticle(@NotNull TrackingParticleType typeIn, @NotNull ClientLevel worldIn,
                                       double x, double y, double z,
                                       double xSpeed, double ySpeed, double zSpeed) {
            return new DragonCannonParticle(worldIn, x, y, z, typeIn.getEntityId());

        }
    }
}
