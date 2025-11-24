package net.forixaim.omneria.skill.battle_style.genesis_wyrm;

import net.forixaim.battle_arts_api.battle_arts_skills.BattleArtsSkillSlots;
import net.forixaim.battle_arts_api.battle_arts_skills.CoreAPIDataKeys;
import net.forixaim.battle_arts_api.battle_arts_skills.active.ultimate_arts.UltimateArt;
import net.forixaim.omneria.animations.battle_style.genesis_wyrm.GenesisWyrmAnimations;
import net.forixaim.omneria.registry.SoundRegistry;
import net.forixaim.omneria.skill.OmneriaSkills;
import net.minecraft.network.FriendlyByteBuf;
import yesman.epicfight.skill.SkillBuilder;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.SkillDataManager;
import yesman.epicfight.world.capabilities.item.CapabilityItem;

public class UltimaFinale extends UltimateArt {
    public UltimaFinale(SkillBuilder<? extends UltimateArt> builder) {
        super(builder);
        this.allowedWeapons.add(CapabilityItem.WeaponCategories.FIST);

    }

    @Override
    public boolean canExecute(SkillContainer container) {
        return super.canExecute(container) &&
                container.getExecutor().getSkill(BattleArtsSkillSlots.BATTLE_STYLE).hasSkill(OmneriaSkills.GENESIS_WYRM);
    }

    private float consumeMeter(float data, float amount) {
        return data - amount;
    }

    protected boolean consumeMeterFor(SkillContainer container, float usage)
    {
        SkillDataManager dm = container.getExecutor().getSkill(BattleArtsSkillSlots.BATTLE_STYLE).getDataManager();
        if (container.getExecutor().getOriginal().isCreative())
            return true;
        if (!dm.hasData(CoreAPIDataKeys.METER_FILL.get()))
        {
            return false;
        }
        else
        {
            if (dm.getDataValue(CoreAPIDataKeys.METER_FILL.get()) - usage <= 0)
            {
                return false;
            }
            else
            {
                dm.setDataSyncF(CoreAPIDataKeys.METER_FILL.get(), data -> consumeMeter(data, usage));
                return true;
            }
        }
    }

    @Override
    public void executeOnServer(SkillContainer container, FriendlyByteBuf args) {
        if (this.consumeMeterFor(container, 500))
        {
            container.getExecutor().playSound(SoundRegistry.ULTIMATE_ART_EXECUTE.get(),   1, 0, 0);
            container.getExecutor().playAnimationSynchronized(GenesisWyrmAnimations.FULL_POWER_DRAGON_CANNON, 0);
        }
    }
}
