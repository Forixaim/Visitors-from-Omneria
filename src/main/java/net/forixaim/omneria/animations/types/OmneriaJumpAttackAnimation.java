package net.forixaim.omneria.animations.types;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.PartEntity;
import org.jetbrains.annotations.Nullable;
import yesman.epicfight.api.animation.AnimationManager;
import yesman.epicfight.api.animation.Joint;
import yesman.epicfight.api.animation.Keyframe;
import yesman.epicfight.api.animation.TransformSheet;
import yesman.epicfight.api.animation.property.AnimationProperty;
import yesman.epicfight.api.animation.property.MoveCoordFunctions;
import yesman.epicfight.api.animation.types.AirSlashAnimation;
import yesman.epicfight.api.animation.types.EntityState;
import yesman.epicfight.api.asset.AssetAccessor;
import yesman.epicfight.api.collider.Collider;
import yesman.epicfight.api.model.Armature;
import yesman.epicfight.api.utils.AttackResult;
import yesman.epicfight.api.utils.HitEntityList;
import yesman.epicfight.api.utils.math.OpenMatrix4f;
import yesman.epicfight.api.utils.math.Vec3f;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.HurtableEntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.damagesource.EpicFightDamageSource;
import yesman.epicfight.world.damagesource.StunType;
import yesman.epicfight.world.effect.EpicFightMobEffects;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class OmneriaJumpAttackAnimation extends AirSlashAnimation
{
    public OmneriaJumpAttackAnimation(float transitionTime, float antic, float contact, float recovery, @Nullable Collider collider, Joint colliderJoint, AnimationManager.AnimationAccessor<? extends AirSlashAnimation> accessor, AssetAccessor<? extends Armature> armature)
    {
        super(transitionTime, antic, contact, recovery, collider, colliderJoint, accessor, armature);
        this.newTimePair(0.0F, Float.MAX_VALUE);
        this.addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.FALL);
        this.addProperty(AnimationProperty.ActionAnimationProperty.COORD_SET_BEGIN, MoveCoordFunctions.TRACE_TARGET_DISTANCE);
        this.addProperty(AnimationProperty.ActionAnimationProperty.COORD_SET_TICK, (self, entitypatch, transformSheet) -> {
            LivingEntity attackTarget = entitypatch.getTarget();
            if (!(Boolean)self.getProperty(AnimationProperty.AttackAnimationProperty.FIXED_MOVE_DISTANCE).orElse(false) && attackTarget != null) {
                TransformSheet transform = self.getTransfroms().get("Root").copyAll();
                Keyframe[] keyframes = transform.getKeyframes();
                int startFrame = 0;
                int endFrame = transform.getKeyframes().length - 1;
                Vec3f keyLast = keyframes[endFrame].transform().translation();
                Vec3 pos = entitypatch.getOriginal().getEyePosition();
                Vec3 targetpos = attackTarget.position().add(attackTarget.getDeltaMovement().scale(8.0));
                float horizontalDistance = Math.max((float)targetpos.subtract(pos).horizontalDistance() * 1.3F - (attackTarget.getBbWidth() + entitypatch.getOriginal().getBbWidth()), 0.0F);
                Vec3f worldPosition = new Vec3f(keyLast.x, 0.0F, -horizontalDistance);
                float scale = Math.min(worldPosition.length() / keyLast.length(), 2.0F);

                for(int i = startFrame; i <= endFrame; ++i) {
                    Vec3f translation = keyframes[i].transform().translation();
                    translation.z *= scale;
                }

                transformSheet.readFrom(transform);
            } else {
                transformSheet.readFrom(self.getTransfroms().get("Root"));
            }

        });
    }

    public OmneriaJumpAttackAnimation(float transitionTime, float antic, float preDelay, float contact, float recovery, boolean directional, @Nullable Collider collider, Joint colliderJoint, AnimationManager.AnimationAccessor<? extends AirSlashAnimation> accessor, AssetAccessor<? extends Armature> armature)
    {
        super(transitionTime, antic, preDelay, contact, recovery, directional, collider, colliderJoint, accessor, armature);
        this.newTimePair(0.0F, Float.MAX_VALUE);
        this.addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.FALL);
        this.addProperty(AnimationProperty.ActionAnimationProperty.COORD_SET_BEGIN, MoveCoordFunctions.TRACE_TARGET_DISTANCE);
        this.addProperty(AnimationProperty.ActionAnimationProperty.COORD_SET_TICK, (self, entitypatch, transformSheet) -> {
            LivingEntity attackTarget = entitypatch.getTarget();
            if (!(Boolean)self.getProperty(AnimationProperty.AttackAnimationProperty.FIXED_MOVE_DISTANCE).orElse(false) && attackTarget != null) {
                TransformSheet transform = self.getTransfroms().get("Root").copyAll();
                Keyframe[] keyframes = transform.getKeyframes();
                int startFrame = 0;
                int endFrame = transform.getKeyframes().length - 1;
                Vec3f keyLast = keyframes[endFrame].transform().translation();
                Vec3 pos = entitypatch.getOriginal().getEyePosition();
                Vec3 targetpos = attackTarget.position().add(attackTarget.getDeltaMovement().scale(8.0));
                float horizontalDistance = Math.max((float)targetpos.subtract(pos).horizontalDistance() * 1.3F - (attackTarget.getBbWidth() + entitypatch.getOriginal().getBbWidth()), 0.0F);
                Vec3f worldPosition = new Vec3f(keyLast.x, 0.0F, -horizontalDistance);
                float scale = Math.min(worldPosition.length() / keyLast.length(), 2.0F);

                for(int i = startFrame; i <= endFrame; ++i) {
                    Vec3f translation = keyframes[i].transform().translation();
                    translation.z *= scale;
                }

                transformSheet.readFrom(transform);
            } else {
                transformSheet.readFrom(self.getTransfroms().get("Root"));
            }

        });
    }

    public OmneriaJumpAttackAnimation(float transitionTime, AnimationManager.AnimationAccessor<? extends AirSlashAnimation> accessor, AssetAccessor<? extends Armature> armature, Phase... phases)
    {
        super(transitionTime, accessor, armature, phases);
        this.newTimePair(0.0F, Float.MAX_VALUE);
        this.addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.FALL);
        this.addProperty(AnimationProperty.ActionAnimationProperty.COORD_SET_BEGIN, MoveCoordFunctions.TRACE_TARGET_DISTANCE);
        this.addProperty(AnimationProperty.ActionAnimationProperty.COORD_SET_TICK, (self, entitypatch, transformSheet) -> {
            LivingEntity attackTarget = entitypatch.getTarget();
            if (!(Boolean)self.getProperty(AnimationProperty.AttackAnimationProperty.FIXED_MOVE_DISTANCE).orElse(false) && attackTarget != null) {
                TransformSheet transform = self.getTransfroms().get("Root").copyAll();
                Keyframe[] keyframes = transform.getKeyframes();
                int startFrame = 0;
                int endFrame = transform.getKeyframes().length - 1;
                Vec3f keyLast = keyframes[endFrame].transform().translation();
                Vec3 pos = entitypatch.getOriginal().getEyePosition();
                Vec3 targetpos = attackTarget.position().add(attackTarget.getDeltaMovement().scale(8.0));
                float horizontalDistance = Math.max((float)targetpos.subtract(pos).horizontalDistance() * 1.3F - (attackTarget.getBbWidth() + entitypatch.getOriginal().getBbWidth()), 0.0F);
                Vec3f worldPosition = new Vec3f(keyLast.x, 0.0F, -horizontalDistance);
                float scale = Math.min(worldPosition.length() / keyLast.length(), 2.0F);

                for(int i = startFrame; i <= endFrame; ++i) {
                    Vec3f translation = keyframes[i].transform().translation();
                    translation.z *= scale;
                }

                transformSheet.readFrom(transform);
            } else {
                transformSheet.readFrom(self.getTransfroms().get("Root"));
            }

        });
    }

    public OmneriaJumpAttackAnimation(float transitionTime, String path, AssetAccessor<? extends Armature> armature, Phase... phases)
    {
        super(transitionTime, path, armature, phases);
        this.newTimePair(0.0F, Float.MAX_VALUE);
        this.addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.FALL);
        this.addProperty(AnimationProperty.ActionAnimationProperty.COORD_SET_BEGIN, MoveCoordFunctions.TRACE_TARGET_DISTANCE);
        this.addProperty(AnimationProperty.ActionAnimationProperty.COORD_SET_TICK, (self, entitypatch, transformSheet) -> {
            LivingEntity attackTarget = entitypatch.getTarget();
            if (!(Boolean)self.getProperty(AnimationProperty.AttackAnimationProperty.FIXED_MOVE_DISTANCE).orElse(false) && attackTarget != null) {
                TransformSheet transform = self.getTransfroms().get("Root").copyAll();
                Keyframe[] keyframes = transform.getKeyframes();
                int startFrame = 0;
                int endFrame = transform.getKeyframes().length - 1;
                Vec3f keyLast = keyframes[endFrame].transform().translation();
                Vec3 pos = entitypatch.getOriginal().getEyePosition();
                Vec3 targetpos = attackTarget.position().add(attackTarget.getDeltaMovement().scale(8.0));
                float horizontalDistance = Math.max((float)targetpos.subtract(pos).horizontalDistance() * 1.3F - (attackTarget.getBbWidth() + entitypatch.getOriginal().getBbWidth()), 0.0F);
                Vec3f worldPosition = new Vec3f(keyLast.x, 0.0F, -horizontalDistance);
                float scale = Math.min(worldPosition.length() / keyLast.length(), 2.0F);

                for(int i = startFrame; i <= endFrame; ++i) {
                    Vec3f translation = keyframes[i].transform().translation();
                    translation.z *= scale;
                }

                transformSheet.readFrom(transform);
            } else {
                transformSheet.readFrom(self.getTransfroms().get("Root"));
            }

        });
    }



    public EpicFightDamageSource getEpicFightDamageSource(LivingEntityPatch<?> entitypatch, Entity target, Phase phase) {
        return this.getEpicFightDamageSource(entitypatch.getDamageSource(this.getAccessor(), phase.hand), entitypatch, target, phase);
    }

    protected void hurtCollidingEntities(LivingEntityPatch<?> entitypatch, float prevElapsedTime, float elapsedTime, EntityState prevState, EntityState state, Phase phase) {
        LivingEntity attacker = entitypatch.getOriginal();
        float prevPoseTime = prevState.attacking() ? prevElapsedTime : phase.preDelay;
        float poseTime = state.attacking() ? elapsedTime : phase.contact;
        List<Entity> list = this.getPhaseByTime(elapsedTime).getCollidingEntities(entitypatch, this, prevPoseTime, poseTime, this.getPlaySpeed(entitypatch, this));
        if (!list.isEmpty()) {
            HitEntityList hitEntities = new HitEntityList(entitypatch, list, phase.getProperty(AnimationProperty.AttackPhaseProperty.HIT_PRIORITY).orElse(HitEntityList.Priority.DISTANCE));
            int maxStrikes = this.getMaxStrikes(entitypatch, phase);
            while (entitypatch.getCurrentlyActuallyHitEntities().size() < maxStrikes && hitEntities.next())
            {
                Entity target = hitEntities.getEntity();
                LivingEntity trueEntity = this.getTrueEntity(target);
                HurtableEntityPatch<?> hitHurtableEntityPatch = EpicFightCapabilities.getEntityPatch(target, HurtableEntityPatch.class);
                if (trueEntity != null && trueEntity.isAlive() && !entitypatch.getCurrentlyAttackTriedEntities().contains(trueEntity) && !entitypatch.isTargetInvulnerable(target) && (target instanceof LivingEntity || target instanceof PartEntity) && attacker.hasLineOfSight(target)) {
                    EpicFightDamageSource source = this.getEpicFightDamageSource(entitypatch, target, phase);
                    int prevInvulTime = target.invulnerableTime;
                    target.invulnerableTime = 0;

                    AttackResult attackResult = entitypatch.attack(source, target, phase.hand);
                    target.invulnerableTime = prevInvulTime;

                    if (attackResult.resultType.dealtDamage()) {
                        target.level().playSound(null, target.getX(), target.getY(), target.getZ(), this.getHitSound(entitypatch, phase), target.getSoundSource(), 1.0F, 1.0F);
                        this.spawnHitParticle((ServerLevel) target.level(), entitypatch, target, phase);
                        if (hitHurtableEntityPatch != null && phase.getProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE).isPresent() && !hitHurtableEntityPatch.getOriginal().hasEffect(EpicFightMobEffects.STUN_IMMUNITY.get())) {
                            float stunTime;
                            if (phase.getProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE).get() == StunType.FALL) {
                                stunTime = (float) ((double) (source.getBaseImpact() * 0.4F) * (1.0 - trueEntity.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE)));
                                if (hitHurtableEntityPatch.getOriginal().isAlive()) {
                                    hitHurtableEntityPatch.applyStun(StunType.SHORT, stunTime);
                                    AtomicReference<Double> power = new AtomicReference<>((double) source.getBaseImpact() * 0.3F);

                                    phase.getProperty(BattleArtsAttackPhaseProperties.KNOCKBACK_POWER).ifPresent(power::set);

                                    Vec3f directionVector = new Vec3f(0f, 0f, 1f);
                                    OpenMatrix4f rotation = new OpenMatrix4f().rotate(-(float)Math.toRadians(entitypatch.getOriginal().yBodyRotO), new Vec3f(0.0F, 1.0F, 0.0F));
                                    OpenMatrix4f.transform3v(rotation, directionVector, directionVector);
                                    Vec3 lateralDirection = directionVector.toDoubleVector().normalize().scale(-1);
                                    Vec3 finalVector = null;
                                    if (phase.getProperty(BattleArtsAttackPhaseProperties.KNOCKBACK_ANGLE).isPresent()) {
                                        double angleDeg = phase.getProperty(BattleArtsAttackPhaseProperties.KNOCKBACK_ANGLE).get();
                                        double angleRad = Math.toRadians(angleDeg);
                                        finalVector = new Vec3(lateralDirection.x() * Math.cos(angleRad), -Math.sin(angleRad), lateralDirection.z() * Math.cos(angleRad)).normalize();
                                    }

                                    if (finalVector == null) {
                                        finalVector = lateralDirection;
                                    }

                                    if (!(trueEntity instanceof Player)) {
                                        power.updateAndGet(v -> v * (1.0 - trueEntity.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE)));
                                    }

                                    if (power.get() > 0.0) {
                                        target.hasImpulse = true;
                                        Vec3 attackerDeltaMovement = attacker.getDeltaMovement();
                                        Vec3 launchVector = (new Vec3(finalVector.x(), finalVector.y(), finalVector.z())).normalize().scale(power.get());
                                        if (!(trueEntity instanceof Player) || !(entitypatch instanceof PlayerPatch)) {
                                            target.setDeltaMovement(attackerDeltaMovement.x / 2.0 - launchVector.x, attackerDeltaMovement.y / 2.0 - launchVector.y, attackerDeltaMovement.z / 2.0 - launchVector.z);
                                        }
                                    }

                                    if (trueEntity instanceof Player && entitypatch instanceof PlayerPatch) {
                                        trueEntity.addEffect(new MobEffectInstance(MobEffects.LEVITATION, 5, (int) (power.get() * 4.0 * 6.0), true, false, false));
                                    }
                                }
                            }
                        }
                    }

                    entitypatch.getCurrentlyAttackTriedEntities().add(trueEntity);
                    if (attackResult.resultType.shouldCount()) {
                        entitypatch.getCurrentlyActuallyHitEntities().add(trueEntity);
                    }
                }
            }

        }
    }
}
