package net.forixaim.omneria.animations.types;

import net.forixaim.battle_arts_api.battle_arts_skills.BattleArtsSkillSlots;
import net.forixaim.omneria.skill.DatakeyRegistry;
import net.minecraft.world.phys.Vec3;
import yesman.epicfight.api.animation.AnimationManager;
import yesman.epicfight.api.animation.property.AnimationEvent;
import yesman.epicfight.api.animation.types.ActionAnimation;
import yesman.epicfight.api.animation.types.DynamicAnimation;
import yesman.epicfight.api.animation.types.EntityState;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.asset.AssetAccessor;
import yesman.epicfight.api.model.Armature;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;

public class JumpAnimation extends ActionAnimation
{

    public JumpAnimation(float jumpTime, float jumpPower, AnimationManager.AnimationAccessor<? extends ActionAnimation> accessor, AssetAccessor<? extends Armature> armature) {
        super(0.15F, jumpTime + 0.05f, accessor, armature);

        this.addState(EntityState.UPDATE_LIVING_MOTION, false);
        this.addEvents(AnimationEvent.InTimeEvent.create(jumpTime, (livingEntityPatch, assetAccessor, animationParameters) ->
        {
            Vec3 movement = livingEntityPatch.getOriginal().getDeltaMovement();
            Vec3 jump = new Vec3(movement.x, jumpPower, movement.z);
            livingEntityPatch.getOriginal().setDeltaMovement(jump);
        }, AnimationEvent.Side.BOTH));

    }

    public JumpAnimation(float transitionTime, float jumpTime, float jumpPower, AnimationManager.AnimationAccessor<? extends ActionAnimation> accessor, AssetAccessor<? extends Armature> armature) {
        super(transitionTime, jumpTime + 0.05f, accessor, armature);
        this.addState(EntityState.UPDATE_LIVING_MOTION, false);

        this.addEvents(AnimationEvent.InTimeEvent.create(jumpTime, (livingEntityPatch, assetAccessor, animationParameters) ->
        {
            Vec3 movement = livingEntityPatch.getOriginal().getDeltaMovement();
            Vec3 jump = new Vec3(movement.x, jumpPower, movement.z);
            livingEntityPatch.getOriginal().setDeltaMovement(jump);
        }, AnimationEvent.Side.BOTH));
    }



}
