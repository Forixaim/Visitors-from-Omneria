package net.forixaim.omneria.animations.types;

import com.mojang.serialization.Codec;
import yesman.epicfight.api.animation.property.AnimationProperty;

public class OmneriaAttackAnimationProperties
{
    public static final AnimationProperty.AttackAnimationProperty<Boolean> MULTI_PHASE_ATTACK =
            new AnimationProperty.AttackAnimationProperty<>("multi_phase", Codec.BOOL);
}
