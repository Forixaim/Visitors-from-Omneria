package net.forixaim.omneria.animations.types;

import net.minecraft.world.phys.Vec3;
import yesman.epicfight.api.animation.AnimationManager;
import yesman.epicfight.api.animation.AnimationPlayer;
import yesman.epicfight.api.animation.property.AnimationEvent;
import yesman.epicfight.api.animation.property.AnimationProperty;
import yesman.epicfight.api.animation.types.ActionAnimation;
import yesman.epicfight.api.animation.types.EntityState;
import yesman.epicfight.api.asset.AssetAccessor;
import yesman.epicfight.api.model.Armature;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;

public class JumpAnimation extends ActionAnimation
{
    private final float jumpTime;
    public JumpAnimation(float jumpTime, float jumpPower, float jumpAngle, AnimationManager.AnimationAccessor<? extends ActionAnimation> accessor, AssetAccessor<? extends Armature> armature) {
        super(0.05F, jumpTime + 0.05f, accessor, armature);
        this.jumpTime = jumpTime;
        this.addState(OmneriaEntityStates.CAN_JUMP, false);
        this.stateSpectrumBlueprint.newTimePair(jumpTime + 0.05f, Float.MAX_VALUE).addState(OmneriaEntityStates.CAN_AUTOCANCEL, true);
        this.addState(EntityState.UPDATE_LIVING_MOTION, false);
        this.addEvents(AnimationEvent.InTimeEvent.create(jumpTime, (livingEntityPatch, assetAccessor, animationParameters) ->
        {

            Vec3 vel = livingEntityPatch.getOriginal().getDeltaMovement();
            double angle = Math.toRadians(jumpAngle);
            double up = Math.sin(angle);      // vertical component
            double horiz = Math.cos(angle);   // horizontal component
            Vec3 hdir = new Vec3(vel.x, 0, vel.z);
            if (hdir.lengthSqr() > 0) {
                hdir = hdir.normalize();
            } else {
                hdir = livingEntityPatch.getOriginal().getLookAngle().multiply(1, 0, 1).normalize();
            }
            Vec3 launch = new Vec3(
                    hdir.x * horiz,
                    up,
                    hdir.z * horiz
            ).scale(jumpPower);

            livingEntityPatch.getOriginal().setDeltaMovement(vel.add(launch));
        }, AnimationEvent.Side.BOTH));
    }

    @Override
    public void tick(LivingEntityPatch<?> entitypatch) {
        super.tick(entitypatch);
        AnimationPlayer player = entitypatch.getAnimator().getPlayerFor(this.getAccessor());
        if (player == null)
            return;
        float elapsedTime = player.getElapsedTime();
        if (entitypatch.getOriginal().onGround() && elapsedTime > (jumpTime + 0.05f))
        {
            entitypatch.stopPlaying(accessor);
        }
    }

    public JumpAnimation(float transitionTime, float jumpTime, float jumpPower, float jumpAngle, AnimationManager.AnimationAccessor<? extends ActionAnimation> accessor, AssetAccessor<? extends Armature> armature) {
        super(transitionTime, jumpTime + 0.05f, accessor, armature);
        this.jumpTime = jumpTime;
        this.addState(OmneriaEntityStates.CAN_JUMP, false);
        this.stateSpectrumBlueprint.newTimePair(jumpTime + 0.05f, Float.MAX_VALUE).addState(OmneriaEntityStates.CAN_AUTOCANCEL, true);

        this.addState(EntityState.UPDATE_LIVING_MOTION, false);

        this.addEvents(AnimationEvent.InTimeEvent.create(jumpTime, (livingEntityPatch, assetAccessor, animationParameters) ->
        {
            Vec3 vel = livingEntityPatch.getOriginal().getDeltaMovement();
            double angle = Math.toRadians(jumpAngle);
            double up = Math.sin(angle);      // vertical component
            double horiz = Math.cos(angle);   // horizontal component
            Vec3 hdir = new Vec3(vel.x, 0, vel.z);
            if (hdir.lengthSqr() > 0) {
                hdir = hdir.normalize();
            } else {
                hdir = livingEntityPatch.getOriginal().getLookAngle().multiply(1, 0, 1).normalize();
            }
            Vec3 launch = new Vec3(
                    hdir.x * horiz,
                    up,
                    hdir.z * horiz
            ).scale(jumpPower);

            livingEntityPatch.getOriginal().setDeltaMovement(vel.add(launch));
        }, AnimationEvent.Side.BOTH));
    }



}
