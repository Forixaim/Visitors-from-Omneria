package net.forixaim.omneria.world.entity.patches;

import com.mojang.logging.LogUtils;
import net.forixaim.omneria.client.particles.types.TrailParticleType;
import net.forixaim.omneria.combat.OmneriaDamageSources;
import net.forixaim.omneria.registry.ParticleRegistry;
import net.forixaim.omneria.world.entity.projectiles.DarkBangProjectile;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import yesman.epicfight.api.client.animation.property.TrailInfo;
import yesman.epicfight.main.EpicFightMod;
import yesman.epicfight.particle.EpicFightParticles;
import yesman.epicfight.world.damagesource.EpicFightDamageSource;
import yesman.epicfight.world.damagesource.StunType;

public class DarkBangPatch extends OmneriaProjectilePatch<DarkBangProjectile> {


    @Override
    protected void setMaxStrikes(DarkBangProjectile darkBangProjectile, int i) {

    }

    @Override
    public EpicFightDamageSource createEpicFightDamageSource() {
        if (this.getOriginal().getOwner() instanceof LivingEntity livingOwner)
            return new EpicFightDamageSource(OmneriaDamageSources.blast(livingOwner, this.getOriginal())).setStunType(StunType.LONG).setBaseImpact(5.2f);
        return new EpicFightDamageSource(this.getOriginal().level().damageSources().generic());
    }

    @Override
    public void onAddedToWorld() {
        LogUtils.getLogger().debug("onAddedToWorld");
        if (this.getOriginal().level().isClientSide()) {
            double entityId = Double.longBitsToDouble(this.getOriginal().getId());
            this.getOriginal().level().addParticle(new TrailParticleType(TrailInfo.builder()
                    .type(EpicFightParticles.PROJECTILE_TRAIL.get())
                    .r(1)
                    .g(0)
                    .b(1)
                    .startPos(new Vec3(-0.7D, 0.0D, -0.0D))
                    .endPos(new Vec3(0.7D, 0.0D, 0.0D))
                    .interpolations(4)
                    .lifetime(20)
                    .updateInterval(1)
                    .texture(ResourceLocation.fromNamespaceAndPath(EpicFightMod.MODID, "textures/particle/projectile_trail.png"))
                    .create(), ParticleRegistry.OMNERIA_PROJECTILE_TRAIL.get()), entityId, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);

            this.getOriginal().level().addParticle(new TrailParticleType(TrailInfo.builder()
                    .type(EpicFightParticles.PROJECTILE_TRAIL.get())
                    .r(1)
                    .g(0)
                    .b(1)
                    .startPos(new Vec3(-0.0D, -0.7D, -0.0D))
                    .endPos(new Vec3(0.0D, 0.7D, 0.0D))
                    .interpolations(4)
                    .lifetime(20)
                    .updateInterval(1)
                    .texture(ResourceLocation.fromNamespaceAndPath(EpicFightMod.MODID, "textures/particle/projectile_trail.png"))
                    .create(), ParticleRegistry.OMNERIA_PROJECTILE_TRAIL.get()), entityId, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        }
    }

    @Override
    public @NotNull TrailInfo getTrailInfo() {
        return TrailInfo.builder()
                .type(EpicFightParticles.PROJECTILE_TRAIL.get())
                .r(1)
                .g(0)
                .b(1)
                .startPos(new Vec3(-0.7D, 0.0D, -0.0D))
                .endPos(new Vec3(0.7D, 0.0D, 0.0D))
                .interpolations(4)
                .lifetime(20)
                .updateInterval(1)
                .texture(ResourceLocation.fromNamespaceAndPath(EpicFightMod.MODID, "textures/particle/projectile_trail.png"))
                .create();
    }
}
