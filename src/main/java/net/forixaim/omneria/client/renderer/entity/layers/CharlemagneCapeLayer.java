package net.forixaim.omneria.client.renderer.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.forixaim.omneria.client.models.entity.mob.CharlemagneModel;
import net.forixaim.omneria.client.renderer.entity.CharlemagneRenderer;
import net.forixaim.omneria.world.entity.charlemagne.Charlemagne;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class CharlemagneCapeLayer extends RenderLayer<Charlemagne, CharlemagneModel>
{
    public CharlemagneCapeLayer(RenderLayerParent<Charlemagne, CharlemagneModel> parent) {
        super(parent);
    }

    @Override
    public void render(@NotNull PoseStack stack, @NotNull MultiBufferSource buffer, int light, Charlemagne entity,
                       float limbSwing, float limbSwingAmount,
                       float partialTicks, float ageInTicks,
                       float netHeadYaw, float headPitch) {
        ResourceLocation capeTex = entity.getCapeTexture();
        if (capeTex == null) return;

        VertexConsumer vc = buffer.getBuffer(RenderType.entityTranslucent(capeTex));
        stack.pushPose();

        Vec3 motion = entity.getDeltaMovement();
        double dx = Mth.lerp(partialTicks, entity.xOld, entity.getX()) - Mth.lerp(partialTicks, entity.xOld - motion.x, entity.getX() - motion.x);
        double dy = Mth.lerp(partialTicks, entity.yOld, entity.getY()) - Mth.lerp(partialTicks, entity.yOld - motion.y, entity.getY() - motion.y);
        double dz = Mth.lerp(partialTicks, entity.zOld, entity.getZ()) - Mth.lerp(partialTicks, entity.zOld - motion.z, entity.getZ() - motion.z);

        float yaw = Mth.lerp(partialTicks, entity.yBodyRotO, entity.yBodyRot);
        double sinYaw = Math.sin(Math.toRadians(yaw));
        double cosYaw = -Math.cos(Math.toRadians(yaw));

        float swayX = (float) (dx * sinYaw + dz * cosYaw) * 100f;
        float swayY = (float) dy * 10f;

        swayX = Mth.clamp(swayX, -6.0F, 32.0F);
        swayY = Mth.clamp(swayY, -5.0F, 5.0F);

        // Slight sway using the movement
        stack.translate(0.0D, 0.0D, 0.125D); // offset cape backward a little
        stack.mulPose(Axis.XP.rotationDegrees(6.0F + swayX / 2.0F + swayY));
        stack.mulPose(Axis.ZP.rotationDegrees(swayX / 2.0F));

        // Positioning logic similar to PlayerRenderer's
        // e.g. stack.translate(0, 0, 0.125);
        // entity motion-based sway logic here...

        this.getParentModel().renderToBuffer(stack, vc, light,
                OverlayTexture.NO_OVERLAY, 1f, 1f, 1f, 1f);
        stack.popPose();
    }
}
