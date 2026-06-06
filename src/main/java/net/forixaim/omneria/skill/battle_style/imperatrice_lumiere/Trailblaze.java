package net.forixaim.omneria.skill.battle_style.imperatrice_lumiere;

import net.forixaim.omneria.animations.battle_style.imperatrice_lumiere.sword.LumiereSwordAnims;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import yesman.epicfight.api.event.EntityEventListener;
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
    public void gatherArguments(SkillContainer container, ControlEngine controlEngine, CompoundTag arguments) {
        ArgumentGatherers.UniversalDirectionalInput(arguments);
    }

    @Override
    public void onInitiate(SkillContainer container, EntityEventListener listener)
    {
        super.onInitiate(container, listener);
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
    }

    @Override
    public CustomPacketPayload getExecutionPacket(SkillContainer container, CompoundTag args)
    {
        return ArgumentGatherers.DirectionalExecutionPacket(container, args);
    }

    @Override
    public boolean canExecute(SkillContainer container)
    {
        return super.canExecute(container);
    }

    @Override
    public void executeOnServer(SkillContainer container, CompoundTag args)
    {
        
    }
}
