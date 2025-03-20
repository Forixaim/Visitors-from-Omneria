package net.forixaim.vfo.capabilities.ex_cap_weapons;

import net.forixaim.bs_api.battle_arts_skills.BattleArtsSkillSlots;
import net.forixaim.efm_ex.api.providers.ProviderConditional;
import net.forixaim.efm_ex.api.providers.ProviderConditionalType;
import net.forixaim.vfo.capabilities.styles.LumiereStyles;
import net.forixaim.vfo.skill.OmneriaSkills;

public class OmneriaProviders
{
    public static ProviderConditional IMPERATRICE_SWORD_PROVIDER = ProviderConditional.builder()
            .setType(ProviderConditionalType.SKILL_EXISTENCE)
            .setSlot(BattleArtsSkillSlots.BATTLE_STYLE)
            .setSkillToCheck(OmneriaSkills.IMPERATRICE_LUMIERE)
            .setWieldStyle(LumiereStyles.IMPERATRICE_SWORD)
            .isVisibleOffHand(false)
            .build();

}
