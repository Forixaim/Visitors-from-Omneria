package net.forixaim.omneria.animations;

import net.forixaim.omneria.registry.EntityRegistry;
import net.forixaim.omneria.world.entity.projectiles.DragonShotProjectile;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.phys.Vec3;
import yesman.epicfight.api.animation.property.AnimationEvent;
import yesman.epicfight.gameasset.EpicFightSounds;

public class ReusableEvents
{
    public static AnimationEvent.E0 FIRE_DRAGON_SHOT = (livingEntityPatch, assetAccessor, animationParameters) ->
    {
        float ang = (float) ((livingEntityPatch.getYRot()+90)/180 * Math.PI);

        Vec3 position = new Vec3(livingEntityPatch.getOriginal().getLookAngle().x, 0, livingEntityPatch.getOriginal().getLookAngle().z).normalize().scale(1.5);
        Vec3 shootVec = new Vec3(Math.cos(ang), 0 , Math.sin(ang));
        Vec3 shootPos = livingEntityPatch.getOriginal().position().add(0, livingEntityPatch.getOriginal().getEyeHeight(), 0).add(position);

        DragonShotProjectile projectile = EntityRegistry.DRAGON_SHOT.get().create(livingEntityPatch.getOriginal().level());

        float multiplier = 1.5f;

        if (projectile != null)
        {
            projectile.setPos(shootPos);
            projectile.setOwner(livingEntityPatch.getOriginal());
            projectile.shoot(shootVec.x(), 0, shootVec.z(), 4.2f, 0);
            livingEntityPatch.getOriginal().level().addFreshEntity(projectile);
            if (!livingEntityPatch.isLogicalClient())
                livingEntityPatch.playSound(EpicFightSounds.LASER_BLAST.get(), 0.5f, 20.0f, 20.0f);
        }
    };
}
