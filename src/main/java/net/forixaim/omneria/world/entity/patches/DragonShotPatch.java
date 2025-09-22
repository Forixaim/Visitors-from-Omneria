package net.forixaim.omneria.world.entity.patches;

import net.forixaim.omneria.world.entity.projectiles.DragonShotProjectile;
import yesman.epicfight.world.capabilities.projectile.ArrowPatch;
import yesman.epicfight.world.capabilities.projectile.ProjectilePatch;
import yesman.epicfight.world.damagesource.EpicFightDamageSource;
import yesman.epicfight.world.damagesource.EpicFightDamageSources;

public class DragonShotPatch extends ProjectilePatch<DragonShotProjectile>
{
    @Override
    protected void setMaxStrikes(DragonShotProjectile dragonShotProjectile, int i)
    {

    }

    @Override
    public EpicFightDamageSource createEpicFightDamageSource()
    {
        if (original.getOwner() == null)
            return EpicFightDamageSources.fromVanillaDamageSource(this.original.damageSources().generic());
        return EpicFightDamageSources.fromVanillaDamageSource(this.original.getOwner().damageSources().generic());
    }
}
