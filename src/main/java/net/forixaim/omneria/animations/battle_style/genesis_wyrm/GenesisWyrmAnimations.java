package net.forixaim.omneria.animations.battle_style.genesis_wyrm;

import net.forixaim.omneria.animations.ReusableEvents;
import net.forixaim.omneria.animations.types.BattleArtsAttackPhaseProperties;
import net.forixaim.omneria.animations.types.OmneriaAttackAnimation;
import net.forixaim.omneria.colliders.GenesisWyrmColliders;
import net.forixaim.omneria.registry.SoundRegistry;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.phys.Vec3;
import yesman.epicfight.api.animation.AnimationManager;
import yesman.epicfight.api.animation.property.AnimationEvent;
import yesman.epicfight.api.animation.property.AnimationProperty;
import yesman.epicfight.api.animation.types.*;
import yesman.epicfight.api.utils.math.ValueModifier;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.gameasset.Armatures;
import yesman.epicfight.gameasset.ColliderPreset;
import yesman.epicfight.gameasset.EpicFightSounds;
import yesman.epicfight.particle.EpicFightParticles;
import yesman.epicfight.skill.guard.GuardSkill;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.damagesource.StunType;

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
                0.1f, 0.0f, 0.3f, 0.5f, 0.6f, ColliderPreset.FIST, Armatures.BIPED.get().handL, access, Armatures.BIPED
        ).addProperty(BattleArtsAttackPhaseProperties.KNOCKBACK_ANGLE, 25d)
                .addProperty(BattleArtsAttackPhaseProperties.KNOCKBACK_POWER, 0.2d)
                .addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (dynamicAnimation, livingEntityPatch, v, v1, v2) -> 1f));

        HEAVY_AUTO2 = builder.nextAccessor("battle_style/legendary/genesis_wyrm/heavy_auto2", access -> new OmneriaAttackAnimation(
                0.1f, 0.0f, 0.2f, 0.3f, 0.65f, ColliderPreset.FIST, Armatures.BIPED.get().handR, access, Armatures.BIPED
        ).addProperty(BattleArtsAttackPhaseProperties.KNOCKBACK_ANGLE, 25d)
                .addProperty(BattleArtsAttackPhaseProperties.KNOCKBACK_POWER, 0.2d)
                .addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (dynamicAnimation, livingEntityPatch, v, v1, v2) -> 1f));
    }
}
