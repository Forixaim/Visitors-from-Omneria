package net.forixaim.omneria.combat;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;

public final class OmneriaDamageSources
{
    //Genesis Wyrm Specific Damage Sources

    public static OmneriaDamageSource blast(LivingEntity owner, Projectile projectile)
    {
        return new OmneriaDamageSource(OmneriaDamageTypes.getHolder(owner, OmneriaDamageTypes.ENERGY_BLAST), projectile, owner, owner.position());
    }
    public static OmneriaDamageSource claw(LivingEntity owner)
    {
        return new OmneriaDamageSource(OmneriaDamageTypes.getHolder(owner, OmneriaDamageTypes.DRAGON_CLAW), owner, owner, owner.position());
    }
    public static OmneriaDamageSource dragonKick(LivingEntity owner)
    {
        return new OmneriaDamageSource(OmneriaDamageTypes.getHolder(owner, OmneriaDamageTypes.DRAGON_KICK), owner, owner, owner.position());
    }
}
