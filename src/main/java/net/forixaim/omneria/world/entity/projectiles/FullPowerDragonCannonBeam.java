package net.forixaim.omneria.world.entity.projectiles;

import net.forixaim.omneria.client.particles.types.BeamParticleType;
import net.forixaim.omneria.combat.OmneriaDamageSources;
import net.forixaim.omneria.registry.ParticleRegistry;
import net.forixaim.omneria.registry.SoundRegistry;
import net.forixaim.omneria.util.ParticleUtil;
import net.mehvahdjukaar.dummmmmmy.Dummmmmmy;
import net.mehvahdjukaar.dummmmmmy.common.TargetDummyEntity;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.ModList;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;
import yesman.epicfight.api.animation.Joint;
import yesman.epicfight.api.animation.JointTransform;
import yesman.epicfight.api.animation.Pose;
import yesman.epicfight.api.collider.OBBCollider;
import yesman.epicfight.api.model.Armature;
import yesman.epicfight.api.utils.math.OpenMatrix4f;
import yesman.epicfight.api.utils.math.ValueModifier;
import yesman.epicfight.particle.EpicFightParticles;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.EntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.damagesource.EpicFightDamageSource;
import yesman.epicfight.world.damagesource.EpicFightDamageTypeTags;
import yesman.epicfight.world.damagesource.StunType;

import java.util.List;
import java.util.Objects;

public class FullPowerDragonCannonBeam extends Projectile {
    protected int lifetime;
    protected Vec3 deceleration = null;
    protected double decelerationConstant = 0.2;
    private Vec3 originBeam = null;
    public float speed = 0;
    protected EpicFightDamageSource dmgSrc = null;


    public FullPowerDragonCannonBeam(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.lifetime = 20;
    }

    @Override
    public void shoot(double pX, double pY, double pZ, float pVelocity, float pInaccuracy)
    {
        super.shoot(pX, pY, pZ, pVelocity, pInaccuracy);
        this.speed = pVelocity;
    }

    @Override
    protected void defineSynchedData() {

    }
    private double magnitude(Vec3 vec)
    {
        return Math.sqrt(vec.x * vec.x + vec.y * vec.y + vec.z * vec.z);
    }


    public void setOrigin(Vec3 origin) {
        this.originBeam = new Vec3(origin.x, origin.y, origin.z);
    }

    @Override
    public void tick() {
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
        if (tickCount % 4 == 0 && !this.level().isClientSide)
        {
            ParticleUtil.sendAlwaysVisibleParticles((ServerLevel) this.level(), ParticleRegistry.DARK_BANG_EXPLOSION.get(), position().x, position().y, position().z, 1, 0, 0, 0, 0);
        }
        if (originBeam != null)
        {
            if (!this.level().isClientSide && !this.isRemoved())
            {
                if (EpicFightCapabilities.getEntityPatch(this.getOwner(), EntityPatch.class) instanceof LivingEntityPatch<?> entityPatch) {
                    List<Entity> entities = updateAndSelectCollideEntity(entityPatch, getBeamOBB(), entityPatch.getArmature().rootJoint);
                    if (!entities.isEmpty()) {
                        entities.forEach(entity -> {
                            if (entity instanceof LivingEntity livingTarget) {
                                entityPatch.attack(new EpicFightDamageSource(this.level().damageSources().mobAttack(entityPatch.getOriginal())).attachDamageModifier(ValueModifier.setter(44)).setStunType(StunType.HOLD).setBaseImpact(4f), livingTarget, InteractionHand.MAIN_HAND);

                                livingTarget.invulnerableTime = 0;
                            }
                        });
                    }
                }
                ((ServerLevel)this.level()).sendParticles(new BeamParticleType(6f, ParticleRegistry.DRAGON_CANNON_LASER.get(), 240, 0, 255, 255, position().x, position().y, position().z, (double) speed), originBeam.x, originBeam.y, originBeam.z, 1, 0, 0, 0, 0);
            }
        }

        lifetime--;
        if (lifetime <= 0)
        {
            this.discard();
        }
    }

    public List<Entity> updateAndSelectCollideEntity(LivingEntityPatch<?> entitypatch, OBBCollider collider, Joint joint) {
        OpenMatrix4f transformMatrix;
        Armature armature = entitypatch.getArmature();
        Vec3 origin = this.originBeam; // fixed start
        Vec3 head = this.position();
        Vec3 midpoint = origin.add(head).scale(0.5);

        if (armature.rootJoint.equals(joint)) {
            Pose rootPose = new Pose();
            rootPose.putJointData("Root", JointTransform.empty());
            transformMatrix = rootPose.orElseEmpty("Root").getAnimationBoundMatrix(armature.rootJoint, new OpenMatrix4f()).removeTranslation();
        } else {
            transformMatrix = armature.getBoundTransformFor(entitypatch.getAnimator().getPose(0f), joint);
        }

        OpenMatrix4f toWorldCoord = OpenMatrix4f.createTranslation(-(float)midpoint.x, (float)midpoint.y, -(float)midpoint.z);
        transformMatrix.mulFront(toWorldCoord.mulBack(entitypatch.getModelMatrix(0.0F)));
        collider.transform(transformMatrix);

        return collider.getCollideEntities(entitypatch.getOriginal());
    }

    public OBBCollider getBeamOBB() {
        Vec3 origin = this.originBeam; // fixed start
        Vec3 head = this.position(); // projectile position
        Vec3 dir; // vector along beam

        if (this.level().isClientSide() && this.getOwner() != null)
        {
            dir = head.subtract(this.getOwner().position());
        }
        else
        {
            dir = head.subtract(origin);
        }
        float length = (float) dir.length();
        if (length <= 0.01f) length = 0.01f; // avoid zero length


        Vector3f halfSize = new Vector3f(3, 3, length); // adjust thickness

        return new OBBCollider(halfSize.x, halfSize.y, halfSize.z, 0, 0, 0);
    }

    @Override
    public void remove(@NotNull RemovalReason pReason) {
        super.remove(pReason);
        if (!this.level().isClientSide) {
            ParticleUtil.sendAlwaysVisibleParticles((ServerLevel) level(), ParticleRegistry.DRACONIC_EXPLOSION.get(), getX(), getY(), getZ(), 1, 0, 0, 0, 0);
            playSound(SoundRegistry.FPDC_EXPLOSION.get(), 32.0f, 1);
            AABB areaDamage = AABB.ofSize(this.position(), 50, 50, 50);
            this.level().getEntities(this, areaDamage).forEach(entity -> {
                if (this.getOwner() instanceof LivingEntity livingEntity) {
                    if (EpicFightCapabilities.getEntityPatch(livingEntity, EntityPatch.class) instanceof LivingEntityPatch<?> entityPatch) {
                        entityPatch.attack(new EpicFightDamageSource(level().damageSources().mobAttack(entityPatch.getOriginal())).attachDamageModifier(ValueModifier.setter(225)).setStunType(StunType.HOLD).setBaseImpact(4f), entity, InteractionHand.MAIN_HAND);
                    }
                    else
                    {
                        entity.hurt(this.level().damageSources().mobAttack(livingEntity),  50);

                    }
                }
            });
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

    @Override
    protected void onHitBlock(BlockHitResult pResult) {
        if (!this.level().isClientSide())
        {
            this.playSound(SoundEvents.GENERIC_EXPLODE, 1.0f, 1.0f);
        }
        this.discard();
    }
}
