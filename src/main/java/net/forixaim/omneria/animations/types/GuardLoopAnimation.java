package net.forixaim.omneria.animations.types;

import yesman.epicfight.api.animation.AnimationManager;
import yesman.epicfight.api.animation.types.DynamicAnimation;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.asset.AssetAccessor;
import yesman.epicfight.api.model.Armature;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;

public class GuardLoopAnimation extends StaticAnimation
{
    private AnimationManager.AnimationAccessor<? extends StaticAnimation> animationAccessor;

    public GuardLoopAnimation(AnimationManager.AnimationAccessor<? extends StaticAnimation> accessor, AssetAccessor<? extends Armature> armature, AnimationManager.AnimationAccessor<? extends StaticAnimation> guardLoop) {
        super(0.15F, true, accessor, armature);
        this.animationAccessor = guardLoop;
    }

    public GuardLoopAnimation(float transitionTime, AnimationManager.AnimationAccessor<? extends StaticAnimation> accessor, AssetAccessor<? extends Armature> armature, AnimationManager.AnimationAccessor<? extends StaticAnimation> guardLoop) {
        super(transitionTime, true, accessor, armature);
        this.animationAccessor = guardLoop;
    }

    @Override
    public void end(LivingEntityPatch<?> entitypatch, AssetAccessor<? extends DynamicAnimation> nextAnimation, boolean isEnd)
    {
        super.end(entitypatch, nextAnimation, isEnd);
        entitypatch.playAnimationSynchronized(animationAccessor, 0);
    }
}
