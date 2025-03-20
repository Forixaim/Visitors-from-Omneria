package net.forixaim.vfo.animations.types;

import com.google.common.collect.Maps;
import net.forixaim.bs_api.battle_arts_skills.BattleArtsSkillSlots;
import net.forixaim.vfo.skill.DatakeyRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import yesman.epicfight.api.animation.AnimationManager;
import yesman.epicfight.api.animation.LivingMotions;
import yesman.epicfight.api.animation.types.DynamicAnimation;
import yesman.epicfight.api.animation.types.StateSpectrum;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.asset.AssetAccessor;
import yesman.epicfight.api.model.Armature;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;

public class GuardTransitionAnimation extends StaticAnimation
{
    private AnimationManager.AnimationAccessor<? extends StaticAnimation> animationAccessor;

    public GuardTransitionAnimation(AnimationManager.AnimationAccessor<? extends StaticAnimation> accessor, AssetAccessor<? extends Armature> armature, AnimationManager.AnimationAccessor<? extends StaticAnimation> guardLoop) {
        super(0.15F, false, accessor, armature);
        this.animationAccessor = guardLoop;
    }

    public GuardTransitionAnimation(float transitionTime, AnimationManager.AnimationAccessor<? extends StaticAnimation> accessor, AssetAccessor<? extends Armature> armature, AnimationManager.AnimationAccessor<? extends StaticAnimation> guardLoop) {
        super(transitionTime, false, accessor, armature);
        this.animationAccessor = guardLoop;
    }

    @Override
    public void end(LivingEntityPatch<?> entitypatch, AssetAccessor<? extends DynamicAnimation> nextAnimation, boolean isEnd)
    {
        if (isEnd)
            entitypatch.playAnimationSynchronized(animationAccessor, 0);
        super.end(entitypatch, nextAnimation, isEnd);

    }
}
