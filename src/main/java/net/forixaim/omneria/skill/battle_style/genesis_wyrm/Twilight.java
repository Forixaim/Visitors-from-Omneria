package net.forixaim.omneria.skill.battle_style.genesis_wyrm;

import net.forixaim.battle_arts_api.battle_arts_skills.BattleArtsSkillSlots;
import net.forixaim.battle_arts_api.battle_arts_skills.active.burst_arts.BurstArt;
import net.forixaim.omneria.animations.battle_style.genesis_wyrm.GenesisWyrmAnimations;
import net.forixaim.omneria.skill.DatakeyRegistry;
import net.forixaim.omneria.skill.OmneriaSkills;
import net.minecraft.network.FriendlyByteBuf;
import yesman.epicfight.skill.SkillBuilder;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.world.capabilities.item.CapabilityItem;

public class Twilight extends BurstArt {
    public Twilight(SkillBuilder<? extends BurstArt> builder) {
        super(builder);
        this.allowedWeapons.add(CapabilityItem.WeaponCategories.FIST);
    }


    @Override
    public boolean canExecute(SkillContainer container) {
        return super.canExecute(container) &&
                container.getExecutor().getSkill(BattleArtsSkillSlots.BATTLE_STYLE).hasSkill(OmneriaSkills.GENESIS_WYRM)
                && !container.getExecutor().getSkill(BattleArtsSkillSlots.BATTLE_STYLE).getDataManager().getDataValue(DatakeyRegistry.TWILIGHT.get());
    }

    @Override
    public void executeOnServer(SkillContainer container, FriendlyByteBuf args) {
        super.executeOnServer(container, args);
        container.getExecutor().playAnimationSynchronized(GenesisWyrmAnimations.TWILIGHT_ACTIVATION, 0);
    }
}
