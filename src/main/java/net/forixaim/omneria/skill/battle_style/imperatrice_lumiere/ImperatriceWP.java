package net.forixaim.omneria.skill.battle_style.imperatrice_lumiere;

import net.forixaim.battle_arts_api.battle_arts_skills.BattleArtsSkillSlots;
import net.forixaim.omneria.NetworkUtils;
import net.forixaim.omneria.skill.OmneriaSkills;
import yesman.epicfight.gameasset.EpicFightSkills;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillBuilder;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.SkillSlots;

public class ImperatriceWP extends Skill
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
        NetworkUtils.changeSkill(container.getServerExecutor(), SkillSlots.BASIC_ATTACK, OmneriaSkills.FLARE_BLITZ);
        NetworkUtils.changeSkill(container.getServerExecutor(), BattleArtsSkillSlots.COMBAT_ART, OmneriaSkills.FIRE_ARTS);
    }

    private void resetSkills(SkillContainer container)
    {
        NetworkUtils.changeSkill(container.getServerExecutor(), SkillSlots.BASIC_ATTACK, EpicFightSkills.BASIC_ATTACK);
        NetworkUtils.changeSkill(container.getServerExecutor(), BattleArtsSkillSlots.COMBAT_ART, null);
    }
}
