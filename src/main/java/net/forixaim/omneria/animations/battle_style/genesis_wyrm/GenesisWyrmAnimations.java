package net.forixaim.omneria.animations.battle_style.genesis_wyrm;

import net.forixaim.omneria.animations.ReusableEvents;
import net.forixaim.omneria.animations.types.BattleArtsAttackPhaseProperties;
import net.forixaim.omneria.animations.types.OmneriaAttackAnimation;
import net.forixaim.omneria.animations.types.OmneriaEntityStates;
import net.forixaim.omneria.animations.types.OmneriaGrabAnimation;
import net.forixaim.omneria.colliders.GenesisWyrmColliders;
import net.forixaim.omneria.combat.OmneriaDamageTypes;
import net.forixaim.omneria.registry.ParticleRegistry;
import net.forixaim.omneria.registry.SoundRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.RelativeMovement;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import yesman.epicfight.api.animation.AnimationManager;
import yesman.epicfight.api.animation.property.AnimationEvent;
import yesman.epicfight.api.animation.property.AnimationProperty;
import yesman.epicfight.api.animation.property.MoveCoordFunctions;
import yesman.epicfight.api.animation.types.*;
import yesman.epicfight.api.animation.types.grappling.GrapplingAttackAnimation;
import yesman.epicfight.api.animation.types.grappling.GrapplingHitAnimation;
import yesman.epicfight.api.animation.types.grappling.GrapplingTryAnimation;
import yesman.epicfight.api.utils.TimePairList;
import yesman.epicfight.api.utils.math.OpenMatrix4f;
import yesman.epicfight.api.utils.math.ValueModifier;
import yesman.epicfight.api.utils.math.Vec3f;
import yesman.epicfight.client.world.capabilites.entitypatch.player.LocalPlayerPatch;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.gameasset.Armatures;
import yesman.epicfight.gameasset.ColliderPreset;
import yesman.epicfight.gameasset.EpicFightSounds;
import yesman.epicfight.particle.EpicFightParticles;
import yesman.epicfight.skill.guard.GuardSkill;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.damagesource.EpicFightDamageTypeTags;
import yesman.epicfight.world.damagesource.StunType;

import java.util.List;
import java.util.Set;

public class GenesisWyrmAnimations
{
    public static AnimationManager.AnimationAccessor<StaticAnimation> IDLE;
    public static AnimationManager.AnimationAccessor<StaticAnimation> IDLE_INJURED;
    public static AnimationManager.AnimationAccessor<StaticAnimation> IDLE_EXHAUSTED;

    public static AnimationManager.AnimationAccessor<SelectiveAnimation> IDLE_SET;

    public static AnimationManager.AnimationAccessor<MovementAnimation> WALK;
    public static AnimationManager.AnimationAccessor<MovementAnimation> WALK_BACK;
    public static AnimationManager.AnimationAccessor<SelectiveAnimation> WALK_SET;


    public static AnimationManager.AnimationAccessor<StaticAnimation> GUARD;
    public static AnimationManager.AnimationAccessor<StaticAnimation> CROUCH;

    public static AnimationManager.AnimationAccessor<GuardAnimation> GUARD_HIT;
    public static AnimationManager.AnimationAccessor<GuardAnimation> REFLECTION;

    public static AnimationManager.AnimationAccessor<BasicAttackAnimation> AUTO1;
    public static AnimationManager.AnimationAccessor<BasicAttackAnimation> AUTO2;
    public static AnimationManager.AnimationAccessor<OmneriaAttackAnimation> AUTO3;
    public static AnimationManager.AnimationAccessor<OmneriaAttackAnimation> AUTO4;
    public static AnimationManager.AnimationAccessor<OmneriaAttackAnimation> AUTO5;
    public static AnimationManager.AnimationAccessor<OmneriaAttackAnimation> MELEE_COUNTER;
    public static AnimationManager.AnimationAccessor<OmneriaAttackAnimation> RANGED_COUNTER;




    public static AnimationManager.AnimationAccessor<OmneriaAttackAnimation> LEG_AUTO1;
    public static AnimationManager.AnimationAccessor<OmneriaAttackAnimation> LEG_AUTO2;

    public static AnimationManager.AnimationAccessor<OmneriaAttackAnimation> HEAVY_AUTO1;
    public static AnimationManager.AnimationAccessor<OmneriaAttackAnimation> HEAVY_AUTO2;
    public static AnimationManager.AnimationAccessor<OmneriaAttackAnimation> HEAVY_AUTO3;

    public static AnimationManager.AnimationAccessor<OmneriaAttackAnimation> UMBRAL_HAMMER;
    public static AnimationManager.AnimationAccessor<OmneriaGrabAnimation> DRAGON_THROW_TRY;
    public static AnimationManager.AnimationAccessor<OmneriaAttackAnimation> DRAGON_THROW;
    public static AnimationManager.AnimationAccessor<LongHitAnimation> DRAGON_THROW_VICTIM_BIPED;



    public static AnimationManager.AnimationAccessor<BasicAttackAnimation> BLAST_AUTO1;
    public static AnimationManager.AnimationAccessor<BasicAttackAnimation> BLAST_AUTO2;



    public static void build(AnimationManager.AnimationBuilder builder)
    {
        IDLE = builder.nextAccessor("battle_style/legendary/genesis_wyrm/idle", access -> new StaticAnimation(0.1f, true, access, Armatures.BIPED));
        IDLE_INJURED = builder.nextAccessor("battle_style/legendary/genesis_wyrm/idle_injured", access -> new StaticAnimation(0.1f, true, access, Armatures.BIPED));
        IDLE_EXHAUSTED = builder.nextAccessor("battle_style/legendary/genesis_wyrm/idle_exhausted", access -> new StaticAnimation(0.1f, true, access, Armatures.BIPED));

        IDLE_SET = builder.nextAccessor("battle_style/legendary/genesis_wyrm/idle_set", access -> new SelectiveAnimation(
                patch -> {
                    float percentage = patch.getOriginal().getHealth() / patch.getOriginal().getMaxHealth();
                    if (percentage < 0.5f)
                    {
                        if (percentage < 0.25f)
                            return 2;
                        return 1;
                    }
                    return 0;
                }, access, IDLE, IDLE_INJURED, IDLE_EXHAUSTED
        ));

        WALK = builder.nextAccessor("battle_style/legendary/genesis_wyrm/walk", access -> new MovementAnimation(0.1f, true, access, Armatures.BIPED)
                .addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (dynamicAnimation, livingEntityPatch, v, v1, v2) ->
                        v * 2f));
        WALK_BACK = builder.nextAccessor("battle_style/legendary/genesis_wyrm/walk_back", access -> new MovementAnimation(0.1f, true, access, Armatures.BIPED)
                .addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (dynamicAnimation, livingEntityPatch, v, v1, v2) ->
                        v * 2f));

        WALK_SET = builder.nextAccessor("battle_style/legendary/genesis_wyrm/walk_set", access -> new SelectiveAnimation(
                patch -> {
                    Vec3 look = patch.getOriginal().getLookAngle();
                    Vec3 motion = patch.getOriginal().getDeltaMovement();
                    double d = look.normalize().dot(motion.normalize());
                    if (d > 0.5)
                        return 0;
                    return 1;
                }, access, WALK, WALK_BACK
        ));

        GUARD = builder.nextAccessor("battle_style/legendary/genesis_wyrm/guard", access -> new StaticAnimation(0.1f, true, access, Armatures.BIPED));
        CROUCH = builder.nextAccessor("battle_style/legendary/genesis_wyrm/crouch", access -> new StaticAnimation(0.1f, true, access, Armatures.BIPED));


        GUARD_HIT = builder.nextAccessor("battle_style/legendary/genesis_wyrm/guard_hit", access -> new GuardAnimation(0.1f, access, Armatures.BIPED));
        REFLECTION = builder.nextAccessor("battle_style/legendary/genesis_wyrm/reflect", access -> new GuardAnimation(0.1f, access, Armatures.BIPED)
                .addProperty(AnimationProperty.StaticAnimationProperty.FIXED_HEAD_ROTATION, true));

        AUTO1 = builder.nextAccessor("battle_style/legendary/genesis_wyrm/auto1", access -> new BasicAttackAnimation(
                0.1f, 0.0f, 0.1f, 0.2f, 0.2f, GenesisWyrmColliders.GW_CLAW, Armatures.BIPED.get().handL, access, Armatures.BIPED
        ).addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.HOLD)
                .addProperty(AnimationProperty.AttackPhaseProperty.SOURCE_TAG, Set.of(DamageTypeTags.BYPASSES_RESISTANCE))
                .addProperty(AnimationProperty.AttackPhaseProperty.SWING_SOUND, EpicFightSounds.SWORDMASTER_SWING.get())
                .addProperty(AnimationProperty.AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLADE_HIT.get())
                .addProperty(AnimationProperty.AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLADE)
                .addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, Animations.ReusableSources.CONSTANT_ONE));

        AUTO2 = builder.nextAccessor("battle_style/legendary/genesis_wyrm/auto2", access -> new BasicAttackAnimation(
                0.1f, 0.0f, 0.1f, 0.2f, 0.2f, GenesisWyrmColliders.GW_CLAW, Armatures.BIPED.get().handR, access, Armatures.BIPED
        ).addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.HOLD)
                .addProperty(AnimationProperty.AttackPhaseProperty.SWING_SOUND, EpicFightSounds.SWORDMASTER_SWING.get())
                .addProperty(AnimationProperty.AttackPhaseProperty.SOURCE_TAG, Set.of(DamageTypeTags.BYPASSES_RESISTANCE))

                .addProperty(AnimationProperty.AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLADE_HIT.get())
                .addProperty(AnimationProperty.AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLADE)
                .addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, Animations.ReusableSources.CONSTANT_ONE));

        AUTO3 = builder.nextAccessor("battle_style/legendary/genesis_wyrm/auto3", access -> new OmneriaAttackAnimation(
                0.1f,access, Armatures.BIPED,
                new AttackAnimation.Phase(0.0f, 0.0f, 0.1f, 0.15f, 0.15f, 0.15f, Armatures.BIPED.get().rootJoint, GenesisWyrmColliders.GW_CIRCLE_CLAW)
                        .addProperty(AnimationProperty.AttackPhaseProperty.SOURCE_TAG, Set.of(DamageTypeTags.BYPASSES_RESISTANCE))
                ,
                new AttackAnimation.Phase(0.15f, 0.0f, 0.15f, 0.2f, 0.2f, 0.4f, Armatures.BIPED.get().rootJoint, GenesisWyrmColliders.GW_CIRCLE_CLAW)
                        .addProperty(AnimationProperty.AttackPhaseProperty.SWING_SOUND, EpicFightSounds.SWORDMASTER_SWING.get())
                        .addProperty(AnimationProperty.AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLADE_HIT.get())
                        .addProperty(AnimationProperty.AttackPhaseProperty.SOURCE_TAG, Set.of(DamageTypeTags.BYPASSES_RESISTANCE))


        ).addProperty(BattleArtsAttackPhaseProperties.KNOCKBACK_ANGLE, 25d)
                .addProperty(BattleArtsAttackPhaseProperties.KNOCKBACK_POWER, 3.0d)
                .addProperty(AnimationProperty.AttackPhaseProperty.SWING_SOUND, EpicFightSounds.SWORDMASTER_SWING.get())
                .addProperty(AnimationProperty.AttackPhaseProperty.SOURCE_TAG, Set.of(DamageTypeTags.BYPASSES_RESISTANCE))

                .addProperty(AnimationProperty.AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLADE_HIT.get())
                .addProperty(AnimationProperty.AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLADE)
                .addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.HOLD)
                .addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, Animations.ReusableSources.CONSTANT_ONE));

        AUTO4 = builder.nextAccessor("battle_style/legendary/genesis_wyrm/auto4", access -> new OmneriaAttackAnimation(
                0.1f,access, Armatures.BIPED,
                new AttackAnimation.Phase(0.0f, 0.0f, 0.1f, 0.15f, 0.15f, 0.15f, Armatures.BIPED.get().rootJoint, GenesisWyrmColliders.GW_CIRCLE_CLAW_BURST)
                        .addProperty(AnimationProperty.AttackPhaseProperty.SOURCE_TAG, Set.of(DamageTypeTags.BYPASSES_RESISTANCE))
                ,
                new AttackAnimation.Phase(0.15f, 0.0f, 0.15f, 0.2f, 0.4f, 0.4f, Armatures.BIPED.get().rootJoint, GenesisWyrmColliders.GW_CIRCLE_CLAW_BURST)
                        .addProperty(AnimationProperty.AttackPhaseProperty.SWING_SOUND, EpicFightSounds.SWORDMASTER_SWING.get())
                        .addProperty(AnimationProperty.AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLADE_HIT.get())
                        .addProperty(AnimationProperty.AttackPhaseProperty.SOURCE_TAG, Set.of(DamageTypeTags.BYPASSES_RESISTANCE))


        ).addProperty(BattleArtsAttackPhaseProperties.KNOCKBACK_ANGLE, 25d, 1)
                .addProperty(BattleArtsAttackPhaseProperties.KNOCKBACK_POWER, 3.0d, 1)
                .addProperty(AnimationProperty.AttackPhaseProperty.SWING_SOUND, EpicFightSounds.SWORDMASTER_SWING.get())
                .addProperty(AnimationProperty.AttackPhaseProperty.SOURCE_TAG, Set.of(DamageTypeTags.BYPASSES_RESISTANCE))

                .addProperty(AnimationProperty.AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLADE_HIT.get())
                .addProperty(AnimationProperty.AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLADE)
                .addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.HOLD)
                .addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, Animations.ReusableSources.CONSTANT_ONE));

        MELEE_COUNTER = builder.nextAccessor("battle_style/legendary/genesis_wyrm/melee_counter", access -> new OmneriaAttackAnimation(
                0.1f, 0.0f, 0.05f, 0.2f, 0.5f, GenesisWyrmColliders.GW_CLAW, Armatures.BIPED.get().handR, access, Armatures.BIPED
        )
                .addProperty(AnimationProperty.AttackPhaseProperty.IMPACT_MODIFIER, ValueModifier.setter(2))
                .addProperty(BattleArtsAttackPhaseProperties.KNOCKBACK_ANGLE, 5d)
                .addProperty(BattleArtsAttackPhaseProperties.KNOCKBACK_POWER, 1.2)
                .addProperty(AnimationProperty.AttackPhaseProperty.SOURCE_TAG, Set.of(DamageTypeTags.BYPASSES_RESISTANCE))
                .addProperty(AnimationProperty.AttackPhaseProperty.SWING_SOUND, EpicFightSounds.SWORDMASTER_SWING.get())
                .addProperty(AnimationProperty.AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLADE_HIT.get())
                .addProperty(AnimationProperty.AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLADE)
                .addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, Animations.ReusableSources.CONSTANT_ONE));

        RANGED_COUNTER = builder.nextAccessor("battle_style/legendary/genesis_wyrm/ranged_counter", access -> new OmneriaAttackAnimation(
                0.1f, 0.3f, 0.6f, 0.7f, 1.5f, GenesisWyrmColliders.GW_CLAW_CLEAVE, Armatures.BIPED.get().rootJoint, access, Armatures.BIPED
        )
                .addProperty(AnimationProperty.AttackPhaseProperty.IMPACT_MODIFIER, ValueModifier.setter(2))
                .addProperty(BattleArtsAttackPhaseProperties.KNOCKBACK_ANGLE, 35d)
                .addProperty(BattleArtsAttackPhaseProperties.KNOCKBACK_POWER, 1.4)
                .addProperty(AnimationProperty.AttackPhaseProperty.SOURCE_TAG, Set.of(DamageTypeTags.BYPASSES_RESISTANCE))
                .addProperty(AnimationProperty.AttackPhaseProperty.SWING_SOUND, EpicFightSounds.WHOOSH_BIG.get())
                .addProperty(AnimationProperty.AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLUNT_HIT_HARD.get())
                .addProperty(AnimationProperty.AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLADE)
                .addProperty(AnimationProperty.AttackAnimationProperty.FIXED_MOVE_DISTANCE, true)
                .addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, Animations.ReusableSources.CONSTANT_ONE)
                .addEvents(
                        AnimationEvent.InTimeEvent.create(0.2f, Animations.ReusableSources.PLAY_SOUND, AnimationEvent.Side.CLIENT).params(EpicFightSounds.WHOOSH_ROD.get()),
                        AnimationEvent.InTimeEvent.create(0.3f, Animations.ReusableSources.PLAY_SOUND, AnimationEvent.Side.CLIENT).params(EpicFightSounds.WHOOSH_ROD.get()),
                        AnimationEvent.InTimeEvent.create(0.4f, Animations.ReusableSources.PLAY_SOUND, AnimationEvent.Side.CLIENT).params(EpicFightSounds.WHOOSH_ROD.get()),
                        AnimationEvent.InTimeEvent.create(0.5f, Animations.ReusableSources.PLAY_SOUND, AnimationEvent.Side.CLIENT).params(EpicFightSounds.WHOOSH_ROD.get())
                ));

        AUTO5 = builder.nextAccessor("battle_style/legendary/genesis_wyrm/auto5", access -> new OmneriaAttackAnimation(
                0.1f, 0.0f, 0.1f, 0.2f, 1f, GenesisWyrmColliders.GW_CLAW_CLEAVE, Armatures.BIPED.get().rootJoint, access, Armatures.BIPED
        )
                .addProperty(BattleArtsAttackPhaseProperties.KNOCKBACK_ANGLE, 45d)
                .addProperty(BattleArtsAttackPhaseProperties.KNOCKBACK_POWER, 3.0d)
                .addProperty(AnimationProperty.AttackPhaseProperty.IMPACT_MODIFIER, ValueModifier.setter(4))
                .addProperty(AnimationProperty.AttackPhaseProperty.SOURCE_TAG, Set.of(DamageTypeTags.BYPASSES_RESISTANCE))

                .addProperty(AnimationProperty.AttackPhaseProperty.SWING_SOUND, EpicFightSounds.WHOOSH_SHARP.get())
                .addProperty(AnimationProperty.AttackPhaseProperty.HIT_SOUND, SoundRegistry.IMPERATRICE_HIT_FINISHER.get())
                .addProperty(AnimationProperty.AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLADE)
                .addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, Animations.ReusableSources.CONSTANT_ONE));

        LEG_AUTO1 = builder.nextAccessor("battle_style/legendary/genesis_wyrm/leg_auto1", access -> new OmneriaAttackAnimation(
                0.0f, access, Armatures.BIPED,
                new AttackAnimation.Phase(0.0f, 0.0f, 0.05f, 0.1f, 0.1f, 0.1f, Armatures.BIPED.get().legR, ColliderPreset.FIST),
                new AttackAnimation.Phase(0.1f, 0.0f, 0.15f, 0.2f, 0.2f, 0.2f, Armatures.BIPED.get().legR, ColliderPreset.FIST),
                new AttackAnimation.Phase(0.2f, 0.0f, 0.25f, 0.3f, 0.3f, 0.3f, Armatures.BIPED.get().legR, ColliderPreset.FIST),
                new AttackAnimation.Phase(0.3f, 0.0f, 0.35f, 0.4f, 0.4f, 0.4f, Armatures.BIPED.get().legR, ColliderPreset.FIST),
                new AttackAnimation.Phase(0.4f, 0.0f, 0.45f, 0.5f, 0.6f, 1.0f, Armatures.BIPED.get().legR, ColliderPreset.FIST)
                        .addProperty(BattleArtsAttackPhaseProperties.KNOCKBACK_ANGLE, 25d)
                        .addProperty(BattleArtsAttackPhaseProperties.KNOCKBACK_POWER, 3.0d)
        ).addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, Animations.ReusableSources.CONSTANT_ONE));

        LEG_AUTO2 = builder.nextAccessor("battle_style/legendary/genesis_wyrm/leg_auto2", access -> new OmneriaAttackAnimation(
                0.1f, 0.0f, 0.1f, 0.2f, 0.4f, ColliderPreset.FIST, Armatures.BIPED.get().legL, access, Armatures.BIPED
        ).addProperty(BattleArtsAttackPhaseProperties.KNOCKBACK_ANGLE, 25d)
                .addProperty(BattleArtsAttackPhaseProperties.KNOCKBACK_POWER, 3.0d)
                .addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.NEUTRALIZE)
                .addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, Animations.ReusableSources.CONSTANT_ONE));

        BLAST_AUTO1 = builder.nextAccessor("battle_style/legendary/genesis_wyrm/blast_auto1", access -> new BasicAttackAnimation(
                0.1f, 0.0f, 0.0f, 0.35f, 0.5f, ColliderPreset.FIST, Armatures.BIPED.get().handR, access, Armatures.BIPED
        ).addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.HOLD)
                .addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, Animations.ReusableSources.CONSTANT_ONE)
                .addEvents(AnimationEvent.InTimeEvent.create(0.35f, ReusableEvents::FIRE_DRAGON_SHOT, AnimationEvent.Side.BOTH).params(Armatures.BIPED.get().handR, 0.35f)));



        BLAST_AUTO2 = builder.nextAccessor("battle_style/legendary/genesis_wyrm/blast_auto2", access -> new BasicAttackAnimation(
                0.1f, 0.0f, 0.0f, 0.05f, 0.15f, ColliderPreset.FIST, Armatures.BIPED.get().handR, access, Armatures.BIPED
        ).addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.HOLD)
                .addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, Animations.ReusableSources.CONSTANT_ONE)
                .addEvents(AnimationEvent.InTimeEvent.create(0.0f, ReusableEvents::FIRE_DRAGON_SHOT, AnimationEvent.Side.BOTH).params(Armatures.BIPED.get().handR, 0.0f)));

        HEAVY_AUTO1 = builder.nextAccessor("battle_style/legendary/genesis_wyrm/heavy_auto1", access -> new OmneriaAttackAnimation(
                0.1f, 0.0f, 0.15f, 0.35f, 0.4f, ColliderPreset.FIST, Armatures.BIPED.get().handL, access, Armatures.BIPED
        ).addProperty(BattleArtsAttackPhaseProperties.KNOCKBACK_ANGLE, 25d)
                .addProperty(AnimationProperty.AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLUNT_HIT_HARD.get())

                .addProperty(BattleArtsAttackPhaseProperties.KNOCKBACK_POWER, 0.2d)
                .addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (dynamicAnimation, livingEntityPatch, v, v1, v2) -> 1f));

        HEAVY_AUTO2 = builder.nextAccessor("battle_style/legendary/genesis_wyrm/heavy_auto2", access -> new OmneriaAttackAnimation(
                0.05f, 0.0f, 0.15f, 0.3f, 0.4f, ColliderPreset.FIST, Armatures.BIPED.get().handR, access, Armatures.BIPED
        ).addProperty(BattleArtsAttackPhaseProperties.KNOCKBACK_ANGLE, 25d)
                .addProperty(AnimationProperty.AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLUNT_HIT_HARD.get())

                .addProperty(BattleArtsAttackPhaseProperties.KNOCKBACK_POWER, 0.2d)
                .addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (dynamicAnimation, livingEntityPatch, v, v1, v2) -> 1f));

        HEAVY_AUTO3 = builder.nextAccessor("battle_style/legendary/genesis_wyrm/heavy_auto3", access -> new OmneriaAttackAnimation(
                0.05f, 0.0f, 0.1f, 0.25f, 1.65f, ColliderPreset.FIST, Armatures.BIPED.get().legL, access, Armatures.BIPED
        ).addProperty(BattleArtsAttackPhaseProperties.KNOCKBACK_ANGLE, 70d)
                .addProperty(AnimationProperty.AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLUNT_HIT_HARD.get())
                .addProperty(BattleArtsAttackPhaseProperties.KNOCKBACK_POWER, 1d)
                .addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (dynamicAnimation, livingEntityPatch, v, v1, v2) -> 1f));

        UMBRAL_HAMMER = builder.nextAccessor("battle_style/legendary/genesis_wyrm/umbral_hammer", access -> new OmneriaAttackAnimation(
                0.05f, 0.0f, 0.2f, 0.35f, 1.65f, ColliderPreset.BATTOJUTSU_DASH, Armatures.BIPED.get().rootJoint, access, Armatures.BIPED
        ).addProperty(BattleArtsAttackPhaseProperties.KNOCKBACK_ANGLE, 35d)
                .addProperty(AnimationProperty.AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLUNT_HIT_HARD.get())
                .addProperty(BattleArtsAttackPhaseProperties.KNOCKBACK_POWER, 1d)
                .addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (dynamicAnimation, livingEntityPatch, speed, prevElapsedTime, elapsedTime) ->
                {
                    if (elapsedTime >= 0.1F && elapsedTime < 0.2F) {
                        float dpx = (float) livingEntityPatch.getOriginal().getX();
                        float dpy = (float) livingEntityPatch.getOriginal().getY();
                        float dpz = (float) livingEntityPatch.getOriginal().getZ();

                        for(BlockState block = livingEntityPatch.getOriginal().level().getBlockState(new BlockPos.MutableBlockPos(dpx, dpy, dpz)); (block.getBlock() instanceof BushBlock || block.isAir()) && !block.is(Blocks.VOID_AIR); block = livingEntityPatch.getOriginal().level().getBlockState(new BlockPos.MutableBlockPos(dpx, dpy, dpz))) {
                            --dpy;
                        }

                        LivingEntity livingentity = livingEntityPatch.getOriginal();
                        Vec3f direction = new Vec3f(4F, -0F, 0.0F);
                        OpenMatrix4f rotation = new OpenMatrix4f().rotate(-(float)Math.toRadians(livingEntityPatch.getOriginal().yBodyRotO + 90.0F), new Vec3f(0.0F, 1.0F, 0.0F));
                        OpenMatrix4f.transform3v(rotation, direction, direction);
                        AABB box = AABB.ofSize(livingentity.getPosition(1.0F), 3.0F, 2.0F, 3.0F);
                        List<Entity> entities = livingentity.level().getEntities(livingentity, box);
                        if (entities.isEmpty()) {
                            livingentity.move(MoverType.SELF, direction.toDoubleVector());

                        }
                        if (!livingentity.level().isClientSide())
                        {
                            ((ServerLevel)livingentity.level()).sendParticles(ParticleRegistry.DRACONIC_BLAST_IMPACT.get(), livingentity.getX(), livingentity.getY(), livingentity.getZ(), 1, 0, 0, 0, 0);
                        }
                        return 1;
                    } else {
                        return 1;
                    }
                }));

        DRAGON_THROW = builder.nextAccessor("battle_style/legendary/genesis_wyrm/dragon_throw_attack", access ->
                new OmneriaAttackAnimation(0.0f, 0.0f, 1.2f, 1.6f, 2.0f, GenesisWyrmColliders.GW_CIRCLE_CLAW_BURST, Armatures.BIPED.get().rootJoint, access, Armatures.BIPED)
                        .addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.NONE)
                        .addState(EntityState.ATTACKING, true)
                        .addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (dynamicAnimation, livingEntityPatch, v, v1, v2) -> 1f)
                        .addEvents(AnimationEvent.InTimeEvent.create(0.2f, Animations.ReusableSources.PLAY_SOUND, AnimationEvent.Side.SERVER).params(EpicFightSounds.WHOOSH_ROD.get()),
                                AnimationEvent.InTimeEvent.create(0.4f, Animations.ReusableSources.PLAY_SOUND, AnimationEvent.Side.SERVER).params(EpicFightSounds.WHOOSH_ROD.get()),
                                AnimationEvent.InTimeEvent.create(0.6f, Animations.ReusableSources.PLAY_SOUND, AnimationEvent.Side.SERVER).params(EpicFightSounds.WHOOSH_ROD.get()),
                                AnimationEvent.InTimeEvent.create(0.8f, Animations.ReusableSources.PLAY_SOUND, AnimationEvent.Side.SERVER).params(EpicFightSounds.WHOOSH_ROD.get()),
                                AnimationEvent.InTimeEvent.create(0.95f, Animations.ReusableSources.PLAY_SOUND, AnimationEvent.Side.SERVER).params(EpicFightSounds.WHOOSH_ROD.get()),
                                AnimationEvent.InTimeEvent.create(1.05f, Animations.ReusableSources.PLAY_SOUND, AnimationEvent.Side.SERVER).params(EpicFightSounds.WHOOSH_ROD.get()),
                                AnimationEvent.InTimeEvent.create(1.15f, Animations.ReusableSources.PLAY_SOUND, AnimationEvent.Side.SERVER).params(EpicFightSounds.WHOOSH_ROD.get()),
                                AnimationEvent.InTimeEvent.create(1.25f, Animations.ReusableSources.PLAY_SOUND, AnimationEvent.Side.SERVER).params(EpicFightSounds.WHOOSH_ROD.get()),
                                AnimationEvent.InTimeEvent.create(1.35f, Animations.ReusableSources.PLAY_SOUND, AnimationEvent.Side.SERVER).params(EpicFightSounds.WHOOSH_ROD.get()),
                                AnimationEvent.InTimeEvent.create(1.45f, Animations.ReusableSources.PLAY_SOUND, AnimationEvent.Side.SERVER).params(EpicFightSounds.WHOOSH_ROD.get()),
                                AnimationEvent.InTimeEvent.create(1.55f, Animations.ReusableSources.PLAY_SOUND, AnimationEvent.Side.SERVER).params(EpicFightSounds.WHOOSH_ROD.get())));



        DRAGON_THROW_VICTIM_BIPED = builder.nextAccessor("battle_style/legendary/genesis_wyrm/victim/dragon_throw_stun", access -> new LongHitAnimation(
                0.2f, access, Armatures.BIPED
        ).addProperty(AnimationProperty.ActionAnimationProperty.MOVE_VERTICAL, true)
                .addProperty(AnimationProperty.ActionAnimationProperty.ActionAnimationProperty.MOVE_TIME, TimePairList.create(1.4f, 2.0f))
                .addProperty(AnimationProperty.ActionAnimationProperty.IS_DEATH_ANIMATION, true)
                .addProperty(AnimationProperty.ActionAnimationProperty.REMOVE_DELTA_MOVEMENT, true)
                .addProperty(AnimationProperty.StaticAnimationProperty.NO_PHYSICS, true)
                .addState(OmneriaEntityStates.CAN_BE_PUSHED, false)
                .addState(EntityState.ATTACKING, true));

        DRAGON_THROW_TRY = builder.nextAccessor("battle_style/legendary/genesis_wyrm/dragon_throw_try", access ->
                new OmneriaGrabAnimation(0.2f, 0.0f, 0.4f, 0.5f, 1.0f, ColliderPreset.BIPED_BODY_COLLIDER, Armatures.BIPED.get().rootJoint, access, Armatures.BIPED, DRAGON_THROW_VICTIM_BIPED)
                        .addProperty(AnimationProperty.AttackPhaseProperty.SOURCE_TAG, Set.of(OmneriaDamageTypes.GRAB))
                        .addProperty(AnimationProperty.ActionAnimationProperty.COORD_START_KEYFRAME_INDEX, 1)
                .addProperty(AnimationProperty.ActionAnimationProperty.NO_GRAVITY_TIME, TimePairList.create(0.15F, 0.35F))
                .addProperty(AnimationProperty.ActionAnimationProperty.DEST_LOCATION_PROVIDER, MoveCoordFunctions.SYNCHED_TARGET_ENTITY_LOCATION_VARIABLE)
                        .addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (dynamicAnimation, livingEntityPatch, v, v1, v2) -> 1f));
    }
}
