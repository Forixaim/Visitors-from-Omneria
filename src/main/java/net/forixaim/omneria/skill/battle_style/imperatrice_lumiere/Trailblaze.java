package net.forixaim.omneria.skill.battle_style.imperatrice_lumiere;

import net.forixaim.omneria.animations.battle_style.imperatrice_lumiere.sword.LumiereSwordAnims;
import net.minecraft.network.FriendlyByteBuf;
import yesman.epicfight.client.events.engine.ControlEngine;
import yesman.epicfight.network.EpicFightNetworkManager;
import yesman.epicfight.network.server.SPSkillExecutionFeedback;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.dodge.DodgeSkill;
import yesman.epicfight.skill.modules.ChargeableSkill;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;
import yesman.epicfight.world.entity.eventlistener.PlayerEventListener;

import java.util.UUID;

public class Trailblaze extends DodgeSkill
{
    private static final UUID EVENT_UUID = UUID.fromString("a4b31d74-8a44-4f47-8c0c-7a6e06be7e13");
    public Trailblaze(Builder builder)
    {
        super(builder);
    }

    @Override
    public FriendlyByteBuf gatherArguments(SkillContainer container, ControlEngine controlEngine)
    {
        return ArgumentGatherers.UniversalDirectionalInput(container, null);
    }

    @Override
    public void onInitiate(SkillContainer container)
    {
        super.onInitiate(container);
        container.getExecutor().getEventListener().addEventListener(PlayerEventListener.EventType.SKILL_CAST_EVENT, EVENT_UUID, event ->
        {
            if (event.getSkillContainer().getSkill() == this && !event.getPlayerPatch().getEntityState().attacking() && this.canExecute(container))
                event.setStateExecutable(true);

        });
    }

    @Override
    public void onRemoved(SkillContainer container)
    {
        super.onRemoved(container);
        container.getExecutor().getEventListener().removeListener(PlayerEventListener.EventType.SKILL_CAST_EVENT, EVENT_UUID);
    }

    @Override
    public Object getExecutionPacket(SkillContainer skillContainer, FriendlyByteBuf args)
    {
        return ArgumentGatherers.DirectionalExecutionPacket(skillContainer, args, this);
    }

    @Override
    public boolean canExecute(SkillContainer container)
    {
        return super.canExecute(container);
    }

    @Override
    public void executeOnServer(SkillContainer container, FriendlyByteBuf args)
    {
        SPSkillExecutionFeedback feedbackPacket = SPSkillExecutionFeedback.executed(container.getSlotId());
        ServerPlayerPatch executor = container.getServerExecutor();
        if (executor.isHoldingAny()) {
            if (executor.getHoldingSkill() instanceof ChargeableSkill) {
                feedbackPacket.getBuffer().writeInt(executor.getAccumulatedChargeAmount());
            }

            if (executor.getHoldingSkill() == this) {
                executor.getHoldingSkill().onStopHolding(container, feedbackPacket);
            }

            executor.resetHolding();
        } else {
            container.activate();
        }

        EpicFightNetworkManager.sendToPlayer(feedbackPacket, executor.getOriginal());

        int fw = args.readInt();
        int sw = args.readInt();
        int ud = args.readInt();

        if (fw == 1)
        {
            container.getExecutor().playAnimationSynchronized(LumiereSwordAnims.IMPERATRICE_SWORD_TRAILBLAZE_FORWARD, 0);
        }
        else if (fw == -1)
        {
            container.getExecutor().playAnimationSynchronized(LumiereSwordAnims.IMPERATRICE_SWORD_TRAILBLAZE_BACKWARD, 0);
        }
        else if (sw == -1)
        {
            container.getExecutor().playAnimationSynchronized(LumiereSwordAnims.IMPERATRICE_SWORD_TRAILBLAZE_RIGHT, 0);
        }
        else if (sw == 1)
        {
            container.getExecutor().playAnimationSynchronized(LumiereSwordAnims.IMPERATRICE_SWORD_TRAILBLAZE_LEFT, 0);
        }
        else if (ud == 1)
        {
            container.getExecutor().playAnimationSynchronized(LumiereSwordAnims.IMPERATRICE_SWORD_TRAILBLAZE_UP, 0);
        }
        else
        {
            container.getExecutor().playAnimationSynchronized(LumiereSwordAnims.IMPERATRICE_SWORD_TRAILBLAZE_VANISH, 0);
        }
    }
}
