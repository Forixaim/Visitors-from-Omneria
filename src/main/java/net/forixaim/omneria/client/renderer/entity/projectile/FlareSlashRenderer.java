package net.forixaim.omneria.client.renderer.entity.projectile;

import net.forixaim.omneria.VisitorsOfOmneria;
import net.forixaim.omneria.client.models.entity.projectile.DragonShotModel;
import net.forixaim.omneria.world.entity.projectiles.FlareSlashProjectile;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class FlareSlashRenderer extends EntityRenderer<FlareSlashProjectile>
{

    private final Model model;

    protected FlareSlashRenderer(EntityRendererProvider.Context pContext)
    {
        super(pContext);
        this.model = new DragonShotModel<>(pContext.bakeLayer(DragonShotModel.LAYER_LOCATION));

    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull FlareSlashProjectile flareSlashProjectile)
    {
        return ResourceLocation.fromNamespaceAndPath(VisitorsOfOmneria.MOD_ID, "textures/entity/flare_slash.png");
    }
}
