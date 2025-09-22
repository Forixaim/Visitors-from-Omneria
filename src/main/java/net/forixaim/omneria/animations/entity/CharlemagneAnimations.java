package net.forixaim.omneria.animations.entity;

import net.forixaim.omneria.registry.ArmatureRegistry;
import yesman.epicfight.api.animation.AnimationManager;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.gameasset.Armatures;

public class CharlemagneAnimations
{
    public static AnimationManager.AnimationAccessor<StaticAnimation> IDLE;
    public static AnimationManager.AnimationAccessor<StaticAnimation> FACE_NEUTRAL;

    public static void build(AnimationManager.AnimationBuilder builder)
    {
        IDLE = builder.nextAccessor("entity/charlemagne/idle", access -> new StaticAnimation(0.1f, true, access, Armatures.BIPED));
        FACE_NEUTRAL = builder.nextAccessor("entity/charlemagne/facial_neutral",  access -> new StaticAnimation(0.1f, true, access, ArmatureRegistry.CHARLEMAGNE));
    }
}
