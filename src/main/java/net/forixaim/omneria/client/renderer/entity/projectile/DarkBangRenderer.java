package net.forixaim.omneria.client.renderer.entity.projectile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.forixaim.battle_arts_api.battle_arts_skills.BattleArtsSkillSlots;
import net.forixaim.omneria.VisitorsOfOmneria;
import net.forixaim.omneria.skill.DatakeyRegistry;
import net.forixaim.omneria.world.entity.projectiles.DarkBangProjectile;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.EntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;

public class DarkBangRenderer extends EntityRenderer<DarkBangProjectile>
{

    @Override
    public @NotNull ResourceLocation getTextureLocation(DarkBangProjectile pEntity) {
        if (EpicFightCapabilities.getEntityPatch(pEntity.getOwner(), EntityPatch.class) instanceof PlayerPatch<?> playerPatch)
        {
            if (playerPatch.getSkill(BattleArtsSkillSlots.BATTLE_STYLE).getDataManager().hasData(DatakeyRegistry.TWILIGHT.get()) && playerPatch.getSkill(BattleArtsSkillSlots.BATTLE_STYLE).getDataManager().getDataValue(DatakeyRegistry.TWILIGHT.get()))
            {
                return ResourceLocation.fromNamespaceAndPath(VisitorsOfOmneria.MOD_ID, "textures/entity/dark_bang/twilight.png");
            }
        }
        return ResourceLocation.fromNamespaceAndPath(VisitorsOfOmneria.MOD_ID, "textures/entity/dark_bang/base.png");
    }

    public DarkBangRenderer(EntityRendererProvider.Context context) {
        super(context);
    }


    @Override
    public void render(@NotNull DarkBangProjectile entity, float entityYaw, float partialTicks, PoseStack poseStack,
                       MultiBufferSource buffer, int packedLight) {

        poseStack.pushPose();

        // Align to camera
        Quaternionf rotation = this.entityRenderDispatcher.cameraOrientation();
        poseStack.mulPose(rotation);

        // Optional: scale size
        float scale = 1.0f;

        poseStack.scale(scale, scale, scale);

        // Draw a flat quad
        VertexConsumer builder = buffer.getBuffer(RenderType.entityTranslucentEmissive(getTextureLocation(entity)));
        Matrix4f matrix = poseStack.last().pose();

        builder.vertex(matrix, -0.5f, -0.5f, 0f)
                .color(255, 255, 255, 255)
                .uv(0f, 1f)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(packedLight)
                .normal(0, 0, 1)
                .endVertex();

        builder.vertex(matrix, 0.5f, -0.5f, 0f)
                .color(255, 255, 255, 255)
                .uv(1f, 1f)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(packedLight)
                .normal(0, 0, 1)
                .endVertex();

        builder.vertex(matrix, 0.5f, 0.5f, 0f)
                .color(255, 255, 255, 255)
                .uv(1f, 0f)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(packedLight)
                .normal(0, 0, 1)
                .endVertex();

        builder.vertex(matrix, -0.5f, 0.5f, 0f)
                .color(255, 255, 255, 255)
                .uv(0f, 0f)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(packedLight)
                .normal(0, 0, 1)
                .endVertex();

        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }
}
