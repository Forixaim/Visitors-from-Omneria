package net.forixaim.omneria.client.particles;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.forixaim.omneria.client.particles.types.TrackingParticleType;
import net.forixaim.omneria.registry.MeshRegistry;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;
import yesman.epicfight.api.client.model.ClassicMesh;
import yesman.epicfight.api.client.model.Mesh;
import yesman.epicfight.api.collider.OBBCollider;
import yesman.epicfight.api.utils.math.QuaternionUtils;
import yesman.epicfight.client.particle.CustomModelParticle;
import yesman.epicfight.client.particle.EpicFightParticleRenderTypes;

public class FPDCExplosion extends CustomModelParticle<ClassicMesh>
{

    protected FPDCExplosion(ClientLevel pLevel, double pX, double pY, double pZ) {
        super(pLevel, pX, pY, pZ, 0, 0, 0, MeshRegistry.SPHERE);
        this.zd = 0;
        this.xd = 0;
        this.yd = 0;
        this.gCol = 0.0f;
        this.rCol = 0.941f;
        this.lifetime = 80;
        this.scale = 0;

    }


    @Override
    public @NotNull AABB getBoundingBox() {
        return new AABB(
                x - 500, y - 500, z - 500,
                x + 500, y + 500, z + 500
        );
    }


    @Override
    public void tick() {
        super.tick();
        if (scale < 12 && age < 70)
        {
            scale += 1.8f;
        }
        else
        {
            scale += 1.8f;
            alpha -= 0.1f;
            if (alpha <= 0)
            {
                this.remove();
            }
        }
    }

    @Override
    public boolean shouldCull() {
        return false;
    }



    @Override
    protected void setupPoseStack(PoseStack poseStack, Camera camera, float partialTicks) {
        Vec3 cameraPosition = camera.getPosition();
        float x = (float)(this.x - cameraPosition.x());
        float y = (float)(this.y - cameraPosition.y());
        float z = (float)(this.z - cameraPosition.z());
        poseStack.translate(x, y, z);
        Quaternionf rotation = new Quaternionf(0.0F, 0.0F, 0.0F, 1.0F);
        float roll = Mth.lerp(partialTicks, this.oRoll, this.roll);
        float pitch = Mth.lerp(partialTicks, this.pitchO, this.pitch);
        float yaw = Mth.lerp(partialTicks, this.yawO, this.yaw);
        rotation.mul(QuaternionUtils.YP.rotationDegrees(180.0F - yaw));
        rotation.mul(QuaternionUtils.XP.rotationDegrees(pitch));
        rotation.mul(QuaternionUtils.ZP.rotationDegrees(roll));
        poseStack.mulPose(rotation);
        float scale = Mth.lerp(partialTicks, this.scaleO, this.scale);
        poseStack.scale(scale, scale, scale);
    }

    @Override
    public void render(@NotNull VertexConsumer pBuffer, @NotNull Camera pRenderInfo, float pPartialTicks) {
        PoseStack poseStack = new PoseStack();
        poseStack.pushPose();
        this.setupPoseStack(poseStack, pRenderInfo, pPartialTicks);
        this.prepareDraw(poseStack, pPartialTicks);
        this.particleMeshProvider.get().draw(poseStack, pBuffer, Mesh.DrawingFunction.POSITION_TEX_COLOR_LIGHTMAP, this.getLightColor(pPartialTicks), this.rCol, this.gCol, this.bCol, this.alpha, OverlayTexture.NO_OVERLAY);
        this.revert(poseStack);
        poseStack.popPose();
    }

    public static final ParticleRenderType LONG_DIST = new ParticleRenderType() {
        public void begin(BufferBuilder bufferBuilder, TextureManager textureManager) {
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.depthMask(false);
            RenderSystem.setShader(GameRenderer::getPositionColorShader);
            bufferBuilder.begin(VertexFormat.Mode.TRIANGLES, DefaultVertexFormat.POSITION_COLOR);
        }

        public void end(Tesselator tesselator) {
            tesselator.end();
        }

        public String toString() {
            return "omneria:TRANSLUCENT_GLOWING";
        }
    };

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return EpicFightParticleRenderTypes.TRANSLUCENT_GLOWING;
    }

    public record Provider() implements ParticleProvider<SimpleParticleType> {

        @Override
        public Particle createParticle(@NotNull SimpleParticleType typeIn, @NotNull ClientLevel worldIn,
                                       double x, double y, double z,
                                       double xSpeed, double ySpeed, double zSpeed) {
            return new FPDCExplosion(worldIn, x, y, z);

        }
    }
}
