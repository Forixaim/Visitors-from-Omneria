package net.forixaim.omneria.client.particles.types;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.ForgeRenderTypes;
import net.minecraftforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;

public abstract class MeshParticle extends Particle
{
    private final BakedModel model;

    protected MeshParticle(ClientLevel pLevel, double pX, double pY, double pZ, BakedModel pModel) {
        super(pLevel, pX, pY, pZ);
        this.model = pModel;
    }

    public MeshParticle(ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed,  BakedModel pModel) {
        super(pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed);
        this.model = pModel;
    }

    /** Subclasses define how to animate the mesh transform */
    protected abstract void applyTransform(PoseStack poseStack, float partialTicks);

    @Override
    public void render(@NotNull VertexConsumer buffer, Camera camera, float v) {
        PoseStack poseStack = new PoseStack();
        poseStack.pushPose();

        Vec3 camPos = camera.getPosition();
        poseStack.translate(x - camPos.x, y - camPos.y, z - camPos.z);

        // Apply scaling/rotation
        applyTransform(poseStack, v);

        // Grab current matrix
        PoseStack.Pose pose = poseStack.last();

        // Get quads from the model
        RandomSource rand = RandomSource.create();
        for (Direction direction : Direction.values()) {
            for (BakedQuad quad : model.getQuads(null, direction, rand, ModelData.EMPTY, RenderType.solid())) {
                buffer.putBulkData(pose, quad,
                        1.0f, 1.0f, 1.0f,   // RGB color
                        1.0f,               // alpha
                        0xF000F0,           // lightmap
                        OverlayTexture.NO_OVERLAY,
                        true);
            }
        }

        // Also handle "general" quads (not tied to a face)
        for (BakedQuad quad : model.getQuads(null, null, rand, ModelData.EMPTY, RenderType.solid())) {
            buffer.putBulkData(pose, quad,
                    1.0f, 1.0f, 1.0f,
                    1.0f,
                    0xF000F0,
                    OverlayTexture.NO_OVERLAY,
                    true);
        }

        poseStack.popPose();
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.NO_RENDER;
    }
}
