package net.forixaim.omneria.skill.battle_style.imperatrice_lumiere;

import net.forixaim.bs_api.battle_arts_skills.BattleArtsSkillSlots;
import net.forixaim.efm_ex.skill.ExCapWeaponPassive;
import net.forixaim.omneria.skill.OmneriaSkills;
import yesman.epicfight.client.ClientEngine;
import yesman.epicfight.client.events.engine.ControllEngine;
import yesman.epicfight.gameasset.EpicFightSkills;
import yesman.epicfight.network.EpicFightNetworkManager;
import yesman.epicfight.network.server.SPChangeSkill;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillBuilder;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.SkillSlots;

public class ImperatriceWP extends ExCapWeaponPassive
{
    public ImperatriceWP(SkillBuilder<? extends Skill> builder)
    {
        super(builder);
    }

    @Override
    public void onInitiate(SkillContainer container)
    {
        swapSkills(container);
        super.onInitiate(container);
    }

    @Override
    public void onRemoved(SkillContainer container)
    {
        resetSkills(container);
        super.onRemoved(container);
    }

    private void swapSkills(SkillContainer container)
    {
        if (!container.getServerExecutor().getSkill(SkillSlots.BASIC_ATTACK).hasSkill(OmneriaSkills.FLARE_BLITZ))
        {
            container.getServerExecutor().getSkillCapability().skillContainers[SkillSlots.BASIC_ATTACK.universalOrdinal()].setSkill(OmneriaSkills.FLARE_BLITZ);
            EpicFightNetworkManager.sendToPlayer(new SPChangeSkill(SkillSlots.BASIC_ATTACK, OmneriaSkills.FLARE_BLITZ.toString(), SPChangeSkill.State.ENABLE), container.getServerExecutor().getOriginal());
        }
        if (!container.getServerExecutor().getSkill(BattleArtsSkillSlots.COMBAT_ART).hasSkill(OmneriaSkills.FIRE_ARTS))
        {
            container.getServerExecutor().getSkill(BattleArtsSkillSlots.COMBAT_ART).setSkill(OmneriaSkills.FIRE_ARTS);
            EpicFightNetworkManager.sendToPlayer(new SPChangeSkill(BattleArtsSkillSlots.COMBAT_ART, OmneriaSkills.FIRE_ARTS.toString(), SPChangeSkill.State.ENABLE), container.getServerExecutor().getOriginal());
        }
    }

    private void resetSkills(SkillContainer container)
    {
        if (!container.getServerExecutor().getSkill(SkillSlots.BASIC_ATTACK).hasSkill(EpicFightSkills.BASIC_ATTACK))
        {
            container.getServerExecutor().getSkillCapability().skillContainers[SkillSlots.BASIC_ATTACK.universalOrdinal()].setSkill(EpicFightSkills.BASIC_ATTACK);
            EpicFightNetworkManager.sendToPlayer(new SPChangeSkill(SkillSlots.BASIC_ATTACK, EpicFightSkills.BASIC_ATTACK.toString(), SPChangeSkill.State.ENABLE), container.getServerExecutor().getOriginal());
        }
        if (!container.getServerExecutor().getSkill(BattleArtsSkillSlots.COMBAT_ART).isEmpty())
        {
            container.getServerExecutor().getSkill(BattleArtsSkillSlots.COMBAT_ART).setSkill(null);
            EpicFightNetworkManager.sendToPlayer(new SPChangeSkill(BattleArtsSkillSlots.COMBAT_ART, "empty", SPChangeSkill.State.DISABLE), container.getServerExecutor().getOriginal());
        }
    }
}
