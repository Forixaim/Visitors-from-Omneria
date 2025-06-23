package net.forixaim.omneria.animations.types;

import net.forixaim.bs_api.battle_arts_skills.BattleArtsSkillSlots;
import net.forixaim.omneria.Config;
import net.forixaim.omneria.combat.OmneriaDamageSource;
import net.forixaim.omneria.combat.OmneriaDamageSources;
import net.forixaim.omneria.skill.DatakeyRegistry;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
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
import yesman.epicfight.api.animation.property.AnimationProperty;
import yesman.epicfight.api.animation.types.AttackAnimation;
import yesman.epicfight.api.animation.types.EntityState;
import yesman.epicfight.api.asset.AssetAccessor;
import yesman.epicfight.api.collider.Collider;
import yesman.epicfight.api.model.Armature;
import yesman.epicfight.api.utils.AttackResult;
import yesman.epicfight.api.utils.HitEntityList;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.EntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.HurtableEntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.damagesource.EpicFightDamageSource;
import yesman.epicfight.world.damagesource.StunType;

import java.util.List;
import java.util.Objects;

public class OmneriaAttackAnimation extends AttackAnimation
{
    public OmneriaAttackAnimation(float transitionTime, float antic, float preDelay, float contact, float recovery, @Nullable Collider collider, Joint colliderJoint, AnimationManager.AnimationAccessor<? extends AttackAnimation> accessor, AssetAccessor<? extends Armature> armature)
    {
        super(transitionTime, antic, preDelay, contact, recovery, collider, colliderJoint, accessor, armature);
    }

    public OmneriaAttackAnimation(float transitionTime, float antic, float preDelay, float contact, float recovery, InteractionHand hand, @Nullable Collider collider, Joint colliderJoint, AnimationManager.AnimationAccessor<? extends AttackAnimation> accessor, AssetAccessor<? extends Armature> armature)
    {
        super(transitionTime, antic, preDelay, contact, recovery, hand, collider, colliderJoint, accessor, armature);
    }

    public OmneriaAttackAnimation(float transitionTime, AnimationManager.AnimationAccessor<? extends AttackAnimation> accessor, AssetAccessor<? extends Armature> armature, Phase... phases)
    {
        super(transitionTime, accessor, armature, phases);
    }

    public OmneriaAttackAnimation(float convertTime, float antic, float preDelay, float contact, float recovery, InteractionHand hand, @Nullable Collider collider, Joint colliderJoint, String path, AssetAccessor<? extends Armature> armature)
    {
        super(convertTime, antic, preDelay, contact, recovery, hand, collider, colliderJoint, path, armature);
    }

    public OmneriaAttackAnimation(float convertTime, String path, AssetAccessor<? extends Armature> armature, Phase... phases)
    {
        super(convertTime, path, armature, phases);
    }

    @Override
    public EpicFightDamageSource getEpicFightDamageSource(DamageSource originalSource, LivingEntityPatch<?> entitypatch, Entity target, Phase phase) {
        if (phase == null) {
            phase = this.getPhaseByTime(Objects.requireNonNull(entitypatch.getAnimator().getPlayerFor(this.getAccessor())).getElapsedTime());
        }

        OmneriaDamageSource extendedSource;
        if (originalSource instanceof OmneriaDamageSource epicfightDamageSource) {
            extendedSource = epicfightDamageSource;
        } else {
            extendedSource = OmneriaDamageSources.copy(originalSource).setAnimation(this.getAccessor());
        }

        phase.getProperty(AnimationProperty.AttackPhaseProperty.DAMAGE_MODIFIER).ifPresent(extendedSource::setDamageModifier);
        phase.getProperty(AnimationProperty.AttackPhaseProperty.ARMOR_NEGATION_MODIFIER).ifPresent((opt) -> extendedSource.setArmorNegation(opt.getTotalValue(extendedSource.getArmorNegation())));
        phase.getProperty(AnimationProperty.AttackPhaseProperty.IMPACT_MODIFIER).ifPresent((opt) -> extendedSource.setImpact(opt.getTotalValue(extendedSource.getImpact())));
        phase.getProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE).ifPresent(extendedSource::setStunType);
        phase.getProperty(AnimationProperty.AttackPhaseProperty.SOURCE_TAG).ifPresent((opt) -> {
            Objects.requireNonNull(extendedSource);
            opt.forEach(extendedSource::addRuntimeTag);
        });
        phase.getProperty(AnimationProperty.AttackPhaseProperty.EXTRA_DAMAGE).ifPresent((opt) -> {
            Objects.requireNonNull(extendedSource);
            opt.forEach(extendedSource::addExtraDamage);
        });
        phase.getProperty(AnimationProperty.AttackPhaseProperty.SOURCE_LOCATION_PROVIDER).ifPresent((opt) -> extendedSource.setInitialPosition((Vec3)opt.apply(entitypatch)));
        phase.getProperty(AnimationProperty.AttackPhaseProperty.SOURCE_LOCATION_PROVIDER).ifPresentOrElse((opt) -> extendedSource.setInitialPosition((Vec3)opt.apply(entitypatch)), () -> extendedSource.setInitialPosition(((LivingEntity)entitypatch.getOriginal()).position()));
        return extendedSource;
    }

    public EpicFightDamageSource getEpicFightDamageSource(LivingEntityPatch<?> entitypatch, Entity target, Phase phase) {
        return this.getEpicFightDamageSource(entitypatch.getDamageSource(this.getAccessor(), phase.hand), entitypatch, target, phase);
    }

    protected void hurtCollidingEntities(LivingEntityPatch<?> entitypatch, float prevElapsedTime, float elapsedTime, EntityState prevState, EntityState state, Phase phase) {
        LivingEntity entity = entitypatch.getOriginal();
        float prevPoseTime = prevState.attacking() ? prevElapsedTime : phase.preDelay;
        float poseTime = state.attacking() ? elapsedTime : phase.contact;
        List<Entity> list = this.getPhaseByTime(elapsedTime).getCollidingEntities(entitypatch, this, prevPoseTime, poseTime, this.getPlaySpeed(entitypatch, this));
        if (!list.isEmpty()) {
            HitEntityList hitEntities = new HitEntityList(entitypatch, list, phase.getProperty(AnimationProperty.AttackPhaseProperty.HIT_PRIORITY).orElse(HitEntityList.Priority.DISTANCE));
            int maxStrikes = this.getMaxStrikes(entitypatch, phase);

            while(entitypatch.getCurrenltyHurtEntities().size() < maxStrikes && hitEntities.next()) {
                Entity target = hitEntities.getEntity();
                LivingEntity trueEntity = this.getTrueEntity(target);
                if (trueEntity != null && trueEntity.isAlive() && !entitypatch.getCurrenltyAttackedEntities().contains(trueEntity) && !entitypatch.isTargetInvulnerable(target) && (target instanceof LivingEntity || target instanceof PartEntity) && entity.hasLineOfSight(target)) {
                    HurtableEntityPatch<?> hitHurtableEntityPatch = EpicFightCapabilities.getEntityPatch(trueEntity, HurtableEntityPatch.class);
                    EpicFightDamageSource damageSource = this.getEpicFightDamageSource(entitypatch, target, phase);
                    int prevInvulTime = target.invulnerableTime;
                    target.invulnerableTime = 0;
                    if (entitypatch instanceof PlayerPatch<?> playerPatch)
                    {
                        if (playerPatch.getSkill(BattleArtsSkillSlots.BATTLE_STYLE).getDataManager().hasData(DatakeyRegistry.TRUE_COMBO_COUNT.get()) && EpicFightCapabilities.getEntityPatch(target, EntityPatch.class) instanceof LivingEntityPatch<?> livingEntityPatch && livingEntityPatch.isStunned())
                        {
                            int combo = playerPatch.getSkill(BattleArtsSkillSlots.BATTLE_STYLE).getDataManager().getDataValue(DatakeyRegistry.TRUE_COMBO_COUNT.get());
                            damageSource.setImpact((float) (damageSource.getImpact() - (double) combo * Config.hitstunDecayMultiplier));
                            if (damageSource.getImpact() < 0)
                            {
                                damageSource.setImpact(0);
                            }
                            combo++;
                            if (!playerPatch.isLogicalClient())
                            {
                                playerPatch.getSkill(BattleArtsSkillSlots.BATTLE_STYLE).getDataManager().setDataSync(DatakeyRegistry.TRUE_COMBO_COUNT.get(), combo, (ServerPlayer) playerPatch.getOriginal());
                            }
                        }
                    }
                    AttackResult attackResult = entitypatch.attack(damageSource, target, phase.hand);
                    target.invulnerableTime = prevInvulTime;
                    if (attackResult.resultType.dealtDamage()) {
                        target.level().playSound(null, target.getX(), target.getY(), target.getZ(), this.getHitSound(entitypatch, phase), target.getSoundSource(), 1.0F, 1.0F);
                        this.spawnHitParticle((ServerLevel)target.level(), entitypatch, target, phase);
                        if (phase.getProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE).isPresent() && phase.getProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE).get() == StunType.FALL) {
                            uppercutEnemy(entitypatch, damageSource, trueEntity, hitHurtableEntityPatch, entity, target);
                        }

                    }

                    entitypatch.getCurrenltyAttackedEntities().add(trueEntity);
                    if (attackResult.resultType.shouldCount()) {
                        entitypatch.getCurrenltyHurtEntities().add(trueEntity);
                    }
                }
            }
        }

    }

    private static void uppercutEnemy(LivingEntityPatch<?> entitypatch, EpicFightDamageSource damageSource, LivingEntity trueEntity, HurtableEntityPatch<?> hitHurtableEntityPatch, LivingEntity entity, Entity target)
    {
        float stunTime = (float)((double)(damageSource.getImpact() * 0.4F) * ((double)1.0F - trueEntity.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE)));
        if ((hitHurtableEntityPatch.getOriginal()).isAlive()) {
            hitHurtableEntityPatch.applyStun(damageSource.getStunType() == StunType.KNOCKDOWN ? StunType.KNOCKDOWN : StunType.SHORT, stunTime);
            double power = damageSource.getImpact() * 0.25F;
            double d1 = entity.getX() - target.getX();
            double d2 = entity.getY() - (double)8.0F - target.getY();

            double d0;
            for(d0 = entity.getZ() - entity.getZ(); d1 * d1 + d0 * d0 < 1.0E-4; d0 = (Math.random() - Math.random()) * 0.01) {
                d1 = (Math.random() - Math.random()) * 0.01;
            }

            if (!(trueEntity instanceof Player)) {
                power *= (double)1.0F - trueEntity.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE);
            }

            if (power > (double)0.0F) {
                target.hasImpulse = true;
                Vec3 vec3 = entity.getDeltaMovement();
                Vec3 vec31 = (new Vec3(d1, d2, d0)).normalize().scale(power);
                if (!(trueEntity instanceof Player) || !(entitypatch instanceof PlayerPatch)) {
                    target.setDeltaMovement(vec3.x / (double)2.0F - vec31.x, vec3.y / (double)2.0F - vec31.y, vec3.z / (double)2.0F - vec31.z);
                }
            }

            if (trueEntity instanceof Player && entitypatch instanceof PlayerPatch) {
                trueEntity.addEffect(new MobEffectInstance(MobEffects.LEVITATION, 5, (int)(power * (double)4.0F * (double)6.0F), true, false, false));
            }

            trueEntity.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, (int)(power * (double)4.0F * (double)6.0F), 20, true, false, false));
        }
    }
}

