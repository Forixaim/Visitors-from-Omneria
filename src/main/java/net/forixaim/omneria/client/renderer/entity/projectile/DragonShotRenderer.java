package net.forixaim.omneria.client.renderer.entity.projectile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.forixaim.omneria.VisitorsOfOmneria;
import net.forixaim.omneria.client.models.entity.projectile.DragonShotModel;
import net.forixaim.omneria.world.entity.projectiles.DragonShotProjectile;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

public class DragonShotRenderer extends EntityRenderer<DragonShotProjectile>
{
    private final Model model;


    public DragonShotRenderer(EntityRendererProvider.Context pContext)
    {
        super(pContext);
        this.model = new DragonShotModel<>(pContext.bakeLayer(DragonShotModel.LAYER_LOCATION));
    }

    @Override
    public void render(DragonShotProjectile pEntity, float pEntityYaw, float pPartialTick, PoseStack pPoseStack, @NotNull MultiBufferSource pBuffer, int pPackedLight)
    {
        pPoseStack.pushPose();
        pPoseStack.scale(2, 2, 2);
        pPoseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(pPartialTick, pEntity.yRotO, pEntity.getYRot()) - 90.0F));
        pPoseStack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(pPartialTick, pEntity.xRotO, pEntity.getXRot()) + 90.0F));
        VertexConsumer idk_what_this_is = ItemRenderer.getFoilBufferDirect(pBuffer, this.model.renderType(this.getTextureLocation(pEntity)), false, false);
        this.model.renderToBuffer(pPoseStack, idk_what_this_is, pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        pPoseStack.popPose();
        super.render(pEntity, pEntityYaw, pPartialTick, pPoseStack, pBuffer, pPackedLight);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull DragonShotProjectile pEntity)
    {
        return ResourceLocation.fromNamespaceAndPath(VisitorsOfOmneria.MOD_ID, "textures/entity/dragon_shot.png");
    }
}
