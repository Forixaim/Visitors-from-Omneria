package net.forixaim.omneria.combat;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import yesman.epicfight.world.damagesource.EpicFightDamageSource;
import yesman.epicfight.world.damagesource.EpicFightDamageSources;
import yesman.epicfight.world.damagesource.EpicFightDamageTypes;

public interface OmneriaDamageSources
{
    static OmneriaDamageSources of(Level level) {
        return () -> level;
    }

    static OmneriaDamageSource copy(DamageSource damageSource) {
        return new OmneriaDamageSource(damageSource);
    }

    default EpicFightDamageSource shockwave(LivingEntity owner) {
        Holder<DamageType> damageType = this.getDamageTypeHolder(EpicFightDamageTypes.SHOCKWAVE);
        return new EpicFightDamageSource(damageType, owner, owner, (Vec3)null);
    }

    default EpicFightDamageSource witherBeam(LivingEntity owner) {
        Holder<DamageType> damageType = this.getDamageTypeHolder(EpicFightDamageTypes.WITHER_BEAM);
        EpicFightDamageSource damageSource = new EpicFightDamageSource(damageType, owner, owner, (Vec3)null);
        damageSource.addRuntimeTag(DamageTypes.MAGIC);
        return damageSource;
    }

    default EpicFightDamageSource trident(Entity owner, Entity causingEntity) {
        return copy(this.getDamageSources().trident(owner, causingEntity));
    }

    default EpicFightDamageSource mobAttack(LivingEntity owner) {
        return copy(this.getDamageSources().mobAttack(owner));
    }

    default EpicFightDamageSource playerAttack(Player owner) {
        return copy(this.getDamageSources().playerAttack(owner));
    }

    default EpicFightDamageSource enderDragonBreath(LivingEntity owner, Entity causingEntity) {
        EpicFightDamageSource damageSource = copy(this.getDamageSources().indirectMagic(owner, causingEntity));
        damageSource.addRuntimeTag(DamageTypes.MAGIC);
        return damageSource;
    }

    default DamageSources getDamageSources() {
        return this.getLevel().damageSources();
    }

    default Holder<DamageType> getDamageTypeHolder(ResourceKey<DamageType> key) {
        return this.getLevel().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(key);
    }

    Level getLevel();
}
