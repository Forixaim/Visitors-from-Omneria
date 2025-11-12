package net.forixaim.omneria.world.entity.patches;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import yesman.epicfight.api.client.animation.property.TrailInfo;
import yesman.epicfight.main.EpicFightMod;
import yesman.epicfight.particle.EpicFightParticles;
import yesman.epicfight.world.capabilities.projectile.ProjectilePatch;
import yesman.epicfight.world.damagesource.EpicFightDamageSource;

public abstract class OmneriaProjectilePatch<T extends Projectile> extends ProjectilePatch<T> {


    public @NotNull TrailInfo getTrailInfo()
    {
        return TrailInfo.builder()
            .type(EpicFightParticles.PROJECTILE_TRAIL.get())
            .startPos(new Vec3(-0.1D, 0.0D, 0.7D))
            .endPos(new Vec3(0.1D, 0.0D, 0.7D))
            .interpolations(4)
            .lifetime(9)
            .updateInterval(1)
            .texture(ResourceLocation.fromNamespaceAndPath(EpicFightMod.MODID, "textures/particle/projectile_trail.png"))
            .create();
    }
}
