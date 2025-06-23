package net.forixaim.omneria.skill.battle_style.imperatrice_lumiere;

import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillBuilder;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.world.entity.eventlistener.PlayerEventListener;

import java.util.UUID;

public class LumiereMovements extends Skill
{
    private static final UUID EVENT_ID = UUID.fromString("a3b00953-4379-4d93-b8a1-7d9a705e06f7");
    public LumiereMovements(SkillBuilder<? extends Skill> builder)
    {
        super(builder);
    }

    @Override
    public void onInitiate(SkillContainer container)
    {
        super.onInitiate(container);

        container.getExecutor().getEventListener().addEventListener(PlayerEventListener.EventType.MOVEMENT_INPUT_EVENT, EVENT_ID, event ->
        {
            if (event.getPlayerPatch().getOriginal().getVehicle() != null || !event.getPlayerPatch().isBattleMode() || event.getPlayerPatch().getOriginal().getAbilities().flying
                    || event.getPlayerPatch().isChargingSkill() || event.getPlayerPatch().getEntityState().inaction()) {
                return;
            }

            if (!event.getPlayerPatch().isInAir())
            {

            }
        });
    }
}
