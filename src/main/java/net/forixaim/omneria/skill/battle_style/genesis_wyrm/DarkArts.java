package net.forixaim.omneria.skill.battle_style.genesis_wyrm;

import com.mojang.logging.LogUtils;
import net.forixaim.battle_arts_api.battle_arts_skills.BattleArtsSkillSlots;
import net.forixaim.battle_arts_api.battle_arts_skills.active.combat_arts.CombatArt;
import net.forixaim.omneria.animations.battle_style.genesis_wyrm.GenesisWyrmAnimations;
import net.forixaim.omneria.skill.OmneriaSkills;
import net.minecraft.network.FriendlyByteBuf;
import yesman.epicfight.skill.SkillBuilder;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.world.capabilities.item.CapabilityItem;

public class DarkArts extends CombatArt {
    public DarkArts(SkillBuilder<? extends CombatArt> builder) {
        super(builder);
        this.allowedWeapons.add(CapabilityItem.WeaponCategories.FIST);
    }

    @Override
    public boolean canExecute(SkillContainer container) {
        LogUtils.getLogger().debug("{}", container.getExecutor().getSkill((BattleArtsSkillSlots.BATTLE_STYLE)).hasSkill(OmneriaSkills.GENESIS_WYRM));
        return container.getExecutor().getSkill(BattleArtsSkillSlots.BATTLE_STYLE).hasSkill(OmneriaSkills.GENESIS_WYRM);
    }

    @Override
    public void executeOnServer(SkillContainer container, FriendlyByteBuf args) {
        super.executeOnServer(container, args);
        LogUtils.getLogger().debug("guh");

        container.getExecutor().playAnimationSynchronized(GenesisWyrmAnimations.DARK_BANG, 0);
    }
}
