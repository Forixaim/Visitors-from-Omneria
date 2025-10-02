package net.forixaim.omneria.animations.battle_style.genesis_wyrm;

import net.forixaim.omneria.animations.ReusableEvents;
import net.forixaim.omneria.animations.types.BattleArtsAttackPhaseProperties;
import net.forixaim.omneria.animations.types.OmneriaAttackAnimation;
import net.forixaim.omneria.colliders.GenesisWyrmColliders;
import yesman.epicfight.api.animation.AnimationManager;
import yesman.epicfight.api.animation.property.AnimationEvent;
import yesman.epicfight.api.animation.property.AnimationProperty;
import yesman.epicfight.api.animation.types.AttackAnimation;
import yesman.epicfight.api.animation.types.BasicAttackAnimation;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.gameasset.Armatures;
import yesman.epicfight.gameasset.ColliderPreset;
import yesman.epicfight.gameasset.EpicFightSounds;
import yesman.epicfight.particle.EpicFightParticles;
import yesman.epicfight.world.damagesource.StunType;

public class GenesisWyrmAnimations
{
    public static AnimationManager.AnimationAccessor<BasicAttackAnimation> AUTO1;
    public static AnimationManager.AnimationAccessor<BasicAttackAnimation> AUTO2;
    public static AnimationManager.AnimationAccessor<OmneriaAttackAnimation> AUTO3;
    public static AnimationManager.AnimationAccessor<OmneriaAttackAnimation> LEG_AUTO1;
    public static AnimationManager.AnimationAccessor<OmneriaAttackAnimation> LEG_AUTO2;

    public static AnimationManager.AnimationAccessor<BasicAttackAnimation> BLAST_AUTO1;
    public static AnimationManager.AnimationAccessor<BasicAttackAnimation> BLAST_AUTO2;



    public static void build(AnimationManager.AnimationBuilder builder)
    {
        AUTO1 = builder.nextAccessor("battle_style/legendary/genesis_wyrm/auto1", access -> new BasicAttackAnimation(
                0.1f, 0.0f, 0.1f, 0.2f, 0.4f, GenesisWyrmColliders.GW_CLAW, Armatures.BIPED.get().handL, access, Armatures.BIPED
        ).addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.HOLD)
                .addProperty(AnimationProperty.AttackPhaseProperty.SWING_SOUND, EpicFightSounds.SWORDMASTER_SWING.get())
                .addProperty(AnimationProperty.AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLADE_HIT.get())
                .addProperty(AnimationProperty.AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLADE)
                .addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, Animations.ReusableSources.CONSTANT_ONE));

        AUTO2 = builder.nextAccessor("battle_style/legendary/genesis_wyrm/auto2", access -> new BasicAttackAnimation(
                0.1f, 0.0f, 0.1f, 0.2f, 0.4f, GenesisWyrmColliders.GW_CLAW, Armatures.BIPED.get().handR, access, Armatures.BIPED
        ).addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.HOLD)
                .addProperty(AnimationProperty.AttackPhaseProperty.SWING_SOUND, EpicFightSounds.SWORDMASTER_SWING.get())
                .addProperty(AnimationProperty.AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLADE_HIT.get())
                .addProperty(AnimationProperty.AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLADE)
                .addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, Animations.ReusableSources.CONSTANT_ONE));

        AUTO3 = builder.nextAccessor("battle_style/legendary/genesis_wyrm/auto3", access -> new OmneriaAttackAnimation(
                0.1f,access, Armatures.BIPED,
                new AttackAnimation.Phase(0.0f, 0.0f, 0.1f, 0.15f, 0.15f, 0.15f, Armatures.BIPED.get().rootJoint, GenesisWyrmColliders.GW_CIRCLE_CLAW),
                new AttackAnimation.Phase(0.15f, 0.0f, 0.15f, 0.2f, 0.2f, 0.4f, Armatures.BIPED.get().rootJoint, GenesisWyrmColliders.GW_CIRCLE_CLAW)
                        .addProperty(AnimationProperty.AttackPhaseProperty.SWING_SOUND, EpicFightSounds.SWORDMASTER_SWING.get())
                        .addProperty(AnimationProperty.AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLADE_HIT.get())

        ).addProperty(BattleArtsAttackPhaseProperties.KNOCKBACK_ANGLE, 25d)
                .addProperty(BattleArtsAttackPhaseProperties.KNOCKBACK_POWER, 3.0d)
                .addProperty(AnimationProperty.AttackPhaseProperty.SWING_SOUND, EpicFightSounds.SWORDMASTER_SWING.get())
                .addProperty(AnimationProperty.AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLADE_HIT.get())
                .addProperty(AnimationProperty.AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLADE)
                .addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.HOLD)
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
                0.1f, 0.0f, 0.0f, 0.05f, 0.05f, ColliderPreset.FIST, Armatures.BIPED.get().handL, access, Armatures.BIPED
        ).addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.HOLD)
                .addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, Animations.ReusableSources.CONSTANT_ONE)
                .addEvents(AnimationEvent.InTimeEvent.create(0.05f, ReusableEvents.FIRE_DRAGON_SHOT, AnimationEvent.Side.BOTH)));

        BLAST_AUTO2 = builder.nextAccessor("battle_style/legendary/genesis_wyrm/blast_auto2", access -> new BasicAttackAnimation(
                0.1f, 0.0f, 0.0f, 0.05f, 0.05f, ColliderPreset.FIST, Armatures.BIPED.get().handL, access, Armatures.BIPED
        ).addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.HOLD)
                .addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, Animations.ReusableSources.CONSTANT_ONE)
                .addEvents(AnimationEvent.InTimeEvent.create(0.05f, ReusableEvents.FIRE_DRAGON_SHOT, AnimationEvent.Side.BOTH)));
    }
}
