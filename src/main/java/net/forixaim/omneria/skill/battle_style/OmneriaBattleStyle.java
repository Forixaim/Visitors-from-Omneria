package net.forixaim.omneria.skill.battle_style;

import net.forixaim.battle_arts_api.battle_arts_skills.battle_style.BattleStyle;
import net.forixaim.omneria.animations.types.OmneriaEntityStates;
import net.forixaim.omneria.skill.DatakeyRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.world.damagesource.DamageTypes;
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
            if (event.getPlayerPatch().getOriginal().isSpectator() || (!event.getPlayerPatch().getOriginal().onGround() && event.getPlayerPatch().getOriginal().getAbilities().mayfly))
            {
                event.getMovementInput().jumping = false;
                if (getJump(container) != null && Minecraft.getInstance().options.keyJump.consumeClick()) {
                    if (container.getExecutor().getOriginal().onGround() && container.getExecutor().getEntityState().getState(OmneriaEntityStates.CAN_JUMP) != null && container.getExecutor().getEntityState().getState(OmneriaEntityStates.CAN_JUMP))
                    {
                        container.getExecutor().playAnimationSynchronized(getJump(container), 0);
                    }
                }
            }
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
    }
}
