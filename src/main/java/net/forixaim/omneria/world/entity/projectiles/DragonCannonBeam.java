package net.forixaim.omneria.world.entity.projectiles;

import com.mojang.logging.LogUtils;
import net.forixaim.omneria.client.particles.types.BeamParticleType;
import net.forixaim.omneria.registry.ParticleRegistry;
import net.mehvahdjukaar.dummmmmmy.Dummmmmmy;
import net.mehvahdjukaar.dummmmmmy.common.TargetDummyEntity;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fml.ModList;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import yesman.epicfight.api.collider.Collider;
import yesman.epicfight.api.collider.OBBCollider;
import yesman.epicfight.api.utils.math.OpenMatrix4f;
import yesman.epicfight.particle.EpicFightParticles;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.damagesource.EpicFightDamageSource;
import yesman.epicfight.world.damagesource.EpicFightDamageTypeTags;
import yesman.epicfight.world.damagesource.StunType;

import java.util.List;
import java.util.Objects;

public class DragonCannonBeam extends SimpleEnergyProjectile {

    private Vec3 originBeam = null;
    public float speed = 0;

    public DragonCannonBeam(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.lifetime = 40;
    }

    @Override
    public void shoot(double pX, double pY, double pZ, float pVelocity, float pInaccuracy)
    {
        super.shoot(pX, pY, pZ, pVelocity, pInaccuracy);
        this.speed = pVelocity;
    }

    public void setLifetime(int lifetime) {
        this.lifetime = lifetime;
    }

    public void setOrigin(Vec3 origin) {
        this.originBeam = new Vec3(origin.x, origin.y, origin.z);
    }

    public Vec3 getOrigin()
    {
        return this.originBeam;
    }

    @Override
    public void tick() {
        super.tick();
        if (originBeam != null)
        {
            float length = Mth.sqrt((float) this.distanceToSqr(originBeam));
            Vec3 mid = originBeam.add(position()).scale(0.5);

            OBBCollider hitBox = new OBBCollider(1, length, 1 , mid.x, mid.y, mid.z);
            float yaw = this.getYRot();   // horizontal rotation
            float pitch = this.getXRot(); // vertical rotation
            float roll = 0;
            float yawRad   = (float) Math.toRadians(yaw);
            float pitchRad = (float) Math.toRadians(pitch);
            float rollRad  = (float) Math.toRadians(roll);
            Matrix4f rotMatrix = new Matrix4f()
                    .identity()
                    .rotateY(yawRad)     // yaw rotates around Y-axis
                    .rotateX(pitchRad)   // pitch rotates around X-axis
                    .rotateZ(rollRad);    // roll rotates around Z-axis
            OpenMatrix4f transformMatrix = OpenMatrix4f.importFromMojangMatrix(rotMatrix);
            hitBox.transform(transformMatrix);
            List<Entity> entityList = hitBox.getCollideEntities(this);
            if (!entityList.isEmpty())
            {
                entityList.forEach(entity -> {LogUtils.getLogger().debug(entity.toString());});
            }
            if (!this.level().isClientSide && !this.isRemoved())
            {
                ((ServerLevel)this.level()).sendParticles(new BeamParticleType(1f, ParticleRegistry.DRAGON_CANNON_LASER.get(), 240, 0, 255, 255, position().x, position().y, position().z, (double) speed), originBeam.x, originBeam.y, originBeam.z, 1, 0, 0, 0, 0);
            }
        }
    }

    @Override
    protected void onHitEntity(@NotNull EntityHitResult hitResult)
    {
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
                if (dmgSrc != null)
                {
                    EpicFightDamageSource damage = dmgSrc;
                    damage.setStunType(StunType.HOLD);
                    damage.setBaseImpact(0.5F);
                    damage.addRuntimeTag(EpicFightDamageTypeTags.WEAPON_INNATE);
                    entity.invulnerableTime = 0;
                    playerpatch.attack(damage, entity, InteractionHand.MAIN_HAND);
                }
                else
                {
                    if (this.getOwner() instanceof Player player) {
                        EpicFightDamageSource damage = new EpicFightDamageSource(this.level().damageSources().playerAttack(player));
                        damage.setStunType(StunType.HOLD);
                        damage.setBaseImpact(0.5F);
                        damage.addRuntimeTag(EpicFightDamageTypeTags.WEAPON_INNATE);
                        entity.invulnerableTime = 0;
                        playerpatch.attack(damage, entity, InteractionHand.MAIN_HAND);
                    }
                    else if (this.getOwner() instanceof LivingEntity le)
                    {
                        EpicFightDamageSource damage = new EpicFightDamageSource(this.level().damageSources().mobAttack(le));
                        damage.setStunType(StunType.HOLD);
                        damage.setBaseImpact(0.5F);
                        damage.addRuntimeTag(EpicFightDamageTypeTags.WEAPON_INNATE);
                        entity.invulnerableTime = 0;
                        playerpatch.attack(damage, entity, InteractionHand.MAIN_HAND);
                    }
                }
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
            } else {
                entity.hurt(this.damageSources().magic(), 6.0F);
            }
        }

    }
}
