package net.forixaim.omneria.animations.types;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.RelativeMovement;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.PartEntity;
import org.jetbrains.annotations.Nullable;
import yesman.epicfight.api.animation.AnimationManager;
import yesman.epicfight.api.animation.Joint;
import yesman.epicfight.api.animation.property.AnimationProperty;
import yesman.epicfight.api.animation.types.AttackAnimation;
import yesman.epicfight.api.animation.types.EntityState;
import yesman.epicfight.api.animation.types.LongHitAnimation;
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

public class OmneriaGrabAnimation extends OmneriaAttackAnimation
{
    private AnimationManager.AnimationAccessor<? extends LongHitAnimation> grabbedAnimation = null;

    public OmneriaGrabAnimation(float transitionTime, float antic, float preDelay, float contact, float recovery, @Nullable Collider collider, Joint colliderJoint, AnimationManager.AnimationAccessor<? extends AttackAnimation> accessor, AssetAccessor<? extends Armature> armature, AnimationManager.AnimationAccessor<? extends LongHitAnimation> victimStunAnimation)
    {
        super(transitionTime, antic, preDelay, contact, recovery, collider, colliderJoint, accessor, armature);
        grabbedAnimation = victimStunAnimation;
    }

    public OmneriaGrabAnimation(float transitionTime, float antic, float preDelay, float contact, float recovery, InteractionHand hand, @Nullable Collider collider, Joint colliderJoint, AnimationManager.AnimationAccessor<? extends AttackAnimation> accessor, AssetAccessor<? extends Armature> armature)
    {
        super(transitionTime, antic, preDelay, contact, recovery, hand, collider, colliderJoint, accessor, armature);
    }

    public OmneriaGrabAnimation(float transitionTime, AnimationManager.AnimationAccessor<? extends AttackAnimation> accessor, AssetAccessor<? extends Armature> armature, Phase... phases)
    {
        super(transitionTime, accessor, armature, phases);
    }

    public OmneriaGrabAnimation(float convertTime, float antic, float preDelay, float contact, float recovery, InteractionHand hand, @Nullable Collider collider, Joint colliderJoint, String path, AssetAccessor<? extends Armature> armature)
    {
        super(convertTime, antic, preDelay, contact, recovery, hand, collider, colliderJoint, path, armature);
    }

    public OmneriaGrabAnimation(float convertTime, String path, AssetAccessor<? extends Armature> armature, Phase... phases)
    {
        super(convertTime, path, armature, phases);
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
                            if (phase.getProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE).get() == StunType.FALL) {
                                if (hitHurtableEntityPatch.getOriginal().isAlive()) {
                                    if (hitHurtableEntityPatch instanceof LivingEntityPatch<?> livingEntityPatch && grabbedAnimation != null && !attacker.level().isClientSide) {
                                        trueEntity.teleportTo((ServerLevel) attacker.level(), attacker.getX(),  attacker.getY(), attacker.getZ(), RelativeMovement.ALL, entitypatch.getYRot(), 0f);
                                        livingEntityPatch.playAnimationSynchronized(grabbedAnimation, 0);

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

