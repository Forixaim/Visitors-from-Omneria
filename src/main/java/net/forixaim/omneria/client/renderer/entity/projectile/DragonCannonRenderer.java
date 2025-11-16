package net.forixaim.omneria.client.renderer.entity.projectile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.forixaim.omneria.VisitorsOfOmneria;
import net.forixaim.omneria.client.models.entity.projectile.DragonCannonModel;
import net.forixaim.omneria.world.entity.projectiles.DragonCannonBeam;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import org.joml.*;

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
        //model.renderToBuffer(pPoseStack, consumer, pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        pPoseStack.popPose();
        super.render(pEntity, pEntityYaw, pPartialTick, pPoseStack, pBuffer, pPackedLight);
    }
}
