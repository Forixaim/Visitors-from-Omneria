package net.forixaim.omneria.world.entity.projectiles;

import net.forixaim.omneria.animations.battle_style.genesis_wyrm.GenesisWyrmAnimations;
import net.forixaim.omneria.registry.ParticleRegistry;
import net.mehvahdjukaar.dummmmmmy.Dummmmmmy;
import net.mehvahdjukaar.dummmmmmy.common.TargetDummyEntity;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.ModList;
import org.jetbrains.annotations.NotNull;
import yesman.epicfight.particle.EpicFightParticles;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.damagesource.EpicFightDamageSource;
import yesman.epicfight.world.damagesource.EpicFightDamageTypeTags;
import yesman.epicfight.world.damagesource.StunType;

import java.util.Objects;

public class SimpleEnergyProjectile extends Projectile
{
    protected int lifetime = 80;
    protected Vec3 deceleration = null;
    protected double decelerationConstant = 0.2;
    protected float damage = 1;
    protected int maxStrikes = 1;
    protected EpicFightDamageSource dmgSrc = null;


    public SimpleEnergyProjectile(EntityType<? extends Projectile> pEntityType, Level pLevel)
    {
        super(pEntityType, pLevel);
    }

    @Override
    protected void defineSynchedData()
    {

    }

    @Override
    public void tick()
    {
        super.tick();

        Vec3 originalVec = this.getDeltaMovement();
        if (deceleration == null)
        {
            deceleration = originalVec.multiply(decelerationConstant, decelerationConstant, decelerationConstant);
        }

        HitResult hitresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
        if (hitresult.getType() != HitResult.Type.MISS && !ForgeEventFactory.onProjectileImpact(this, hitresult)) {
            this.onHit(hitresult);
        }

        double d7;
        double d2;
        double d3;
        if (magnitude(originalVec) > 0.0)
        {
            Vec3 moveVec = originalVec.subtract(deceleration);
            double d5 = moveVec.x;
            double d6 = moveVec.y;
            double d1 = moveVec.z;
            d7 = this.getX() + d5;
            d2 = this.getY() + d6;
            d3 = this.getZ() + d1;
        }
        else
        {
            d7 = this.getX() + 0;
            d2 = this.getY() + 0;
            d3 = this.getZ() + 0;
        }
        this.setPos(d7, d2, d3);
        lifetime--;
        if (lifetime <= 0)
        {
            this.discard();
        }
    }

    @Override
    protected void onHitBlock(@NotNull BlockHitResult pResult)
    {
        if (!this.level().isClientSide())
        {
            this.playSound(SoundEvents.GENERIC_EXPLODE, 1.0f, 1.0f);
            ((ServerLevel)this.level()).sendParticles(ParticleRegistry.DRACONIC_BLAST_IMPACT.get(), this.getX(), this.getY(), this.getZ(), 1, 0, 0, 0, 0);
            for (int i = 0; i < 10; i++)
            {
                RandomSource rng = this.level().getRandom();
                ((ServerLevel)this.level()).sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE, this.getX() + rng.nextFloat() * 0.5f, this.getY() + rng.nextFloat() * 0.5f, this.getZ() + rng.nextFloat() * 0.5f, 1, 0, 0, 0, 0.05f);
            }
        }
        this.discard();
    }

    public void setDamageSource(EpicFightDamageSource damageSource)
    {
        dmgSrc = damageSource;
    }

    @Override
    protected void onHitEntity(@NotNull EntityHitResult hitResult) {
        super.onHitEntity(hitResult);
        if (!this.level().isClientSide()) {
            Entity entity = hitResult.getEntity();
            Entity entity1 = this.getOwner();
            PlayerPatch<?> playerpatch = EpicFightCapabilities.getEntityPatch(this.getOwner(), PlayerPatch.class);
            if (entity1 instanceof LivingEntity livingEntity && playerpatch != null)
            {
                if (!(entity instanceof Enemy || (ModList.get().isLoaded(Dummmmmmy.MOD_ID) && (entity instanceof TargetDummyEntity)))) {
                    if (entity instanceof TamableAnimal pet) {
                        if (Objects.requireNonNull(pet.getOwner()).is(entity1) || pet.getOwner().getTeam() == entity1.getTeam() || (pet.getOwner().getTeam() != null && pet.getOwner().getTeam().isAlliedTo(entity1.getTeam()))) {
                            return;
                        }
                    }
                    if (livingEntity.getTeam() == entity1.getTeam() || (livingEntity.getTeam() != null && livingEntity.getTeam().isAlliedTo(entity1.getTeam()))) {
                        return;
                    }
                }
                EpicFightDamageSource damage = dmgSrc;
                damage.setStunType(StunType.HOLD);
                damage.setBaseImpact(0.5F);
                damage.addRuntimeTag(EpicFightDamageTypeTags.WEAPON_INNATE);
                entity.invulnerableTime = 0;
                playerpatch.attack(damage, entity, InteractionHand.MAIN_HAND);
                entity.playSound(SoundEvents.GENERIC_EXPLODE, 1.0f, 1.0f);
                entity.level().addParticle(EpicFightParticles.HIT_BLADE.get(), entity.getX(), entity.getY(), entity.getZ(), 0.0D, 0.0D, 0.0D);
                if (!entity.level().isClientSide())
                {
                    ((ServerLevel)entity.level()).sendParticles(ParticleRegistry.DRACONIC_BLAST_IMPACT.get(), this.getX(), this.getY(), this.getZ(), 1, 0, 0, 0, 0);
                    for (int i = 0; i < 10; i++)
                    {
                        RandomSource rng = this.level().getRandom();
                        ((ServerLevel)entity.level()).sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE, this.getX() + rng.nextFloat() * 0.5f, this.getY() + rng.nextFloat() * 0.5f, this.getZ() + rng.nextFloat() * 0.5f, 1, 0, 0, 0, 0.05f);
                    }
                }
                this.discard();
            } else {
                entity.hurt(this.damageSources().magic(), 6.0F);
                this.discard();
            }
        }
    }

    private double magnitude(Vec3 vec)
    {
        return Math.sqrt(vec.x * vec.x + vec.y * vec.y + vec.z * vec.z);
    }

}
