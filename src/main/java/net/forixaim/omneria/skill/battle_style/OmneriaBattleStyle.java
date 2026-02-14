package net.forixaim.omneria.skill.battle_style;

import net.forixaim.battle_arts_api.battle_arts_skills.battle_style.BattleStyle;
import net.forixaim.omneria.animations.types.OmneriaEntityStates;
import net.forixaim.omneria.skill.DatakeyRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.phys.Vec3;
import yesman.epicfight.api.animation.AnimationManager;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.entity.eventlistener.PlayerEventListener;

import java.util.UUID;

public class OmneriaBattleStyle extends BattleStyle implements IIgnoresCEStunImmunity
{
    private static final UUID EUUID = UUID.fromString("cb2a68ae-8abf-4bab-b721-368ac695ee23");



    public OmneriaBattleStyle(Builder<?> builder)
    {
        super(builder);
    }

    @Override
    public void onInitiate(SkillContainer container) {
        super.onInitiate(container);
        container.getExecutor().getEventListener().addEventListener(PlayerEventListener.EventType.MOVEMENT_INPUT_EVENT, EUUID, event ->
        {

        });
    }

    @Override
    public void onRemoved(SkillContainer container) {
        super.onRemoved(container);
    }

    public AnimationManager.AnimationAccessor<? extends StaticAnimation> getJump(SkillContainer container)
    {
        return null;
    }

    private Vec3 accelerate(Vec3 vel, Vec3 wishDir, float maxSpeed, float accel) {
        float currentSpeed = (float)vel.dot(wishDir);
        float addSpeed = maxSpeed - currentSpeed;
        if (addSpeed <= 0) return vel;

        float accelSpeed = accel * maxSpeed;
        if (accelSpeed > addSpeed) accelSpeed = addSpeed;

        return vel.add(wishDir.scale(accelSpeed));
    }

    @Override
    public void updateContainer(SkillContainer container)
    {
        super.updateContainer(container);
        LivingEntityPatch<?> entityPatch = EpicFightCapabilities.getEntityPatch(container.getExecutor().getOriginal().getLastHurtMob(), LivingEntityPatch.class);
        if ((entityPatch != null && (!entityPatch.isStunned() || entityPatch.getOriginal().isDeadOrDying()) && container.getDataManager().hasData(DatakeyRegistry.TRUE_COMBO_COUNT.get())))
        {
            container.getDataManager().setDataSync(DatakeyRegistry.TRUE_COMBO_COUNT.get(),  0);
        }
        container.getExecutor().getOriginal().resetFallDistance();

        if (!container.getExecutor().isLogicalClient())
        {
            Vec3 vel = container.getExecutor().getOriginal().getDeltaMovement();
            Vec3 horizontal = new Vec3(vel.x, 0, vel.z);

            boolean onGround = container.getExecutor().getOriginal().onGround();
            boolean sprint = container.getExecutor().getOriginal().isSprinting();

            float maxSpeed = sprint ? 0.45f : 0.30f;
            float accel = onGround ? 0.20f : 0.04f;
            float friction = onGround ? 0.10f : 0.01f;

            Vec3 wishDir = new Vec3(container.getExecutor().getOriginal().xxa, 0, container.getExecutor().getOriginal().zza);
            if (wishDir.lengthSqr() > 0) wishDir = wishDir.normalize();

            if (wishDir.lengthSqr() > 0) {
                horizontal = accelerate(horizontal, wishDir, maxSpeed, accel);
            }

            if (onGround) {
                horizontal = horizontal.scale(1f - friction);
            }

            container.getExecutor().getOriginal().setDeltaMovement(horizontal.x, vel.y, horizontal.z);
        }
    }
}
