package net.forixaim.vfo.client.models.entity.projectile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.forixaim.vfo.registry.MeshRegistry;
import net.forixaim.vfo.world.entity.projectiles.FlareSlashProjectile;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import yesman.epicfight.api.client.model.Mesh;
import yesman.epicfight.api.client.model.SkinnedMesh;

import java.util.function.Function;

public class FlareSlashModel extends Model
{
    public FlareSlashModel(Function<ResourceLocation, RenderType> p_103110_)
    {
        super(p_103110_);
    }

    @Override
    public void renderToBuffer(@NotNull PoseStack poseStack, @NotNull VertexConsumer vertexConsumer, int packedLight, int overlay, float red, float green, float blue, float alpha)
    {
        MeshRegistry.FLARE_SLASH.get().draw(poseStack, vertexConsumer, Mesh.DrawingFunction.NEW_ENTITY, packedLight, red, green, blue, alpha, overlay);
    }

}
