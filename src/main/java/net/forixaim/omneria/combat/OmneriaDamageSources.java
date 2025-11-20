package net.forixaim.omneria.combat;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

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

    public static DamageSource fpdc(Level level, @Nullable Entity attacker, @Nullable Entity direct) {
        return new DamageSource(
                level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(OmneriaDamageTypes.FP_DRAGON_CANNON),
                direct, attacker
        );
    }

    public static OmneriaDamageSource dragonKick(LivingEntity owner)
    {
        return new OmneriaDamageSource(OmneriaDamageTypes.getHolder(owner, OmneriaDamageTypes.DRAGON_KICK), owner, owner, owner.position());
    }
}
