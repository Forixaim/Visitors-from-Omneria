package net.forixaim.omneria.client.renderer.entity.projectile;

import com.mojang.blaze3d.vertex.PoseStack;
import net.forixaim.omneria.registry.MeshRegistry;
import net.forixaim.omneria.world.entity.projectiles.DragonPulse;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import yesman.epicfight.api.client.model.Mesh;
import yesman.epicfight.api.utils.math.OpenMatrix4f;

public class DragonPulseRenderer extends EntityRenderer<DragonPulse>
{

    protected DragonPulseRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull DragonPulse pEntity) {
        return ResourceLocation.fromNamespaceAndPath("omneria", "dragon_pulse");
    }

    @Override
    public void render(DragonPulse pEntity, float pEntityYaw, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        MeshRegistry.SPHERE.get().draw(pPoseStack, pBuffer, RenderType.solid(), Mesh.DrawingFunction.POSITION_TEX_COLOR_LIGHTMAP, pPackedLight, 1f, 1f, 1f,1f, 1, null, new OpenMatrix4f[]{});
    }
}
