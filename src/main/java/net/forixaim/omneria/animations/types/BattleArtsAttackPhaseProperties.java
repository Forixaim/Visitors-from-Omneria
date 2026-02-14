package net.forixaim.omneria.animations.types;

import com.mojang.serialization.Codec;
import yesman.epicfight.api.animation.property.AnimationProperty;

public class BattleArtsAttackPhaseProperties
{
    public static final AnimationProperty.AttackPhaseProperty<Double> KNOCKBACK_POWER =
            new AnimationProperty.AttackPhaseProperty<>("knockback_power", Codec.DOUBLE);
    public static final AnimationProperty.AttackPhaseProperty<Double> KNOCKBACK_ANGLE =
            new AnimationProperty.AttackPhaseProperty<>("knockback_angle", Codec.DOUBLE);
    public static final AnimationProperty.AttackPhaseProperty<Double> KNOCKBACK_LATERAL_ANGLE =
            new AnimationProperty.AttackPhaseProperty<>("knockback_lateral_angle", Codec.DOUBLE);
    public static final AnimationProperty.AttackPhaseProperty<Integer> HITSTUN_TICKS =
            new AnimationProperty.AttackPhaseProperty<>("stun_time", Codec.INT);
    public static final AnimationProperty.AttackPhaseProperty<Integer> PRIORITY =
            new AnimationProperty.AttackPhaseProperty<>("priority", Codec.INT);
    public static final AnimationProperty.AttackPhaseProperty<Integer> ENDLAG_TICKS =
            new AnimationProperty.AttackPhaseProperty<>("endlag_ticks", Codec.INT);
    public static final AnimationProperty.AttackAnimationProperty<Boolean> IS_AERIAL = new AnimationProperty.AttackAnimationProperty<>("aerial_attack", Codec.BOOL);
}
