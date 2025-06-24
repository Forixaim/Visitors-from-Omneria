package net.forixaim.omneria.capabilities.ex_cap_weapons;

import net.forixaim.bs_api.battle_arts_skills.BattleArtsSkillSlots;
import net.forixaim.efm_ex.api.providers.ProviderConditional;
import net.forixaim.efm_ex.api.providers.ProviderConditionalType;
import net.forixaim.omneria.capabilities.styles.LumiereStyles;
import net.forixaim.omneria.registry.EntityRegistry;
import net.forixaim.omneria.skill.OmneriaSkills;
import net.forixaim.omneria.world.entity.patches.CharlemagnePatch;

public class OmneriaProviders
{
    public static ProviderConditional IMPERATRICE_SWORD_PROVIDER = ProviderConditional.builder()
            .setType(ProviderConditionalType.SKILL_EXISTENCE)
            .setSlot(BattleArtsSkillSlots.BATTLE_STYLE)
            .setSkillToCheck(OmneriaSkills.IMPERATRICE_LUMIERE)
            .setWieldStyle(LumiereStyles.IMPERATRICE_SWORD)
            .isVisibleOffHand(false)
            .build();

    public static ProviderConditional CHARLEMAGNE_UNCONDITIONAL = ProviderConditional.builder()
            .setType(ProviderConditionalType.CUSTOM)
            .setWieldStyle(LumiereStyles.IMPERATRICE_SWORD)
            .isVisibleOffHand(false)
            .setCustomFunction(
                    livingEntityPatch -> livingEntityPatch instanceof CharlemagnePatch
            )
            .build();

}
