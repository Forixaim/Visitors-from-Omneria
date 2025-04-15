package net.forixaim.vfo.client.renderer.entity;

import net.forixaim.vfo.VisitorsOfOmneria;
import net.forixaim.vfo.world.entity.projectiles.FlareSlashProjectile;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class FlareSlashRenderer extends EntityRenderer<FlareSlashProjectile>
{
    protected FlareSlashRenderer(EntityRendererProvider.Context pContext)
    {
        super(pContext);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull FlareSlashProjectile flareSlashProjectile)
    {
        return new ResourceLocation(VisitorsOfOmneria.MOD_ID, "textures/entity/flare_slash.png");
    }
}
