package net.forixaim.omneria.client.renderer.entity.projectile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.forixaim.omneria.VisitorsOfOmneria;
import net.forixaim.omneria.client.models.entity.projectile.DragonCannonModel;
import net.forixaim.omneria.world.entity.projectiles.DragonCannonBeam;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.joml.*;
import yesman.epicfight.api.collider.OBBCollider;
import yesman.epicfight.api.utils.math.OpenMatrix4f;

import java.lang.Math;

public class DragonCannonRenderer extends EntityRenderer<DragonCannonBeam> {

    private final DragonCannonModel model;

    public DragonCannonRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
        model = new DragonCannonModel(pContext.bakeLayer(DragonCannonModel.LAYER_LOCATION));
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull DragonCannonBeam pEntity) {
        return ResourceLocation.fromNamespaceAndPath(VisitorsOfOmneria.MOD_ID, "textures/entity/dragon_cannon_body.png");
    }

    @Override
    public void render(DragonCannonBeam pEntity, float pEntityYaw, float pPartialTick,
                       @NotNull PoseStack pPoseStack, @NotNull MultiBufferSource pBuffer, int pPackedLight) {

        pPoseStack.pushPose();
        pPoseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(pPartialTick, pEntity.yRotO, pEntity.getYRot()) - 90.0F));
        pPoseStack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(pPartialTick, pEntity.xRotO, pEntity.getXRot()) + 90.0F));
        VertexConsumer consumer = ItemRenderer.getFoilBufferDirect(pBuffer, this.model.renderType(this.getTextureLocation(pEntity)), false, false);
        if (Minecraft.getInstance().getEntityRenderDispatcher().shouldRenderHitBoxes())
        {
            float length = Mth.sqrt((float) pEntity.distanceToSqr(pEntity.getOrigin()));
            Vec3 mid = pEntity.getOrigin().add(pEntity.getOrigin()).scale(0.5);
            OBBCollider hitBox = new OBBCollider(1, length, 1 , mid.x, mid.y, mid.z);
            float yaw = pEntity.getYRot();   // horizontal rotation
            float pitch = pEntity.getXRot(); // vertical rotation
            float roll = 0;
            float yawRad   = (float) Math.toRadians(yaw);
            float pitchRad = (float) Math.toRadians(pitch);
            float rollRad  = (float) Math.toRadians(roll);
            Matrix4f rotMatrix = new Matrix4f()
                    .identity()
                    .rotateY(yawRad)     // yaw rotates around Y-axis
                    .rotateX(pitchRad)   // pitch rotates around X-axis
                    .rotateZ(rollRad);    // roll rotates around Z-axis
            OpenMatrix4f transformMatrix = OpenMatrix4f.importFromMojangMatrix(rotMatrix);
            hitBox.transform(transformMatrix);
            hitBox.draw(pPoseStack, pBuffer, pPackedLight);
        }
        pPoseStack.popPose();
        super.render(pEntity, pEntityYaw, pPartialTick, pPoseStack, pBuffer, pPackedLight);
    }
}
