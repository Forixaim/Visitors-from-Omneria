package net.forixaim.omneria.skill.battle_style.genesis_wyrm;

import net.forixaim.battle_arts_api.battle_arts_skills.BattleArtsSkillSlots;
import net.forixaim.battle_arts_api.battle_arts_skills.CoreAPIDataKeys;
import net.forixaim.battle_arts_api.battle_arts_skills.active.combat_arts.CombatArt;
import net.forixaim.omneria.animations.battle_style.genesis_wyrm.GenesisWyrmAnimations;
import net.forixaim.omneria.skill.OmneriaSkills;
import net.forixaim.omneria.skill.battle_style.imperatrice_lumiere.ArgumentGatherers;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import yesman.epicfight.client.events.engine.ControlEngine;
import yesman.epicfight.skill.SkillBuilder;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.SkillDataManager;
import yesman.epicfight.world.capabilities.item.CapabilityItem;

public class DarkArts extends CombatArt {
    public DarkArts(SkillBuilder builder) {
        super(builder);
        this.allowedWeapons.add(CapabilityItem.WeaponCategories.FIST);
    }

    @Override
    public void gatherArguments(SkillContainer container, ControlEngine controlEngine, CompoundTag arguments) {
        ArgumentGatherers.UniversalDirectionalInput(arguments);
    }

    @Override
    public CustomPacketPayload getExecutionPacket(SkillContainer container, CompoundTag args) {
        return ArgumentGatherers.DirectionalExecutionPacket(container, args);
    }

    @Override
    public boolean canExecute(SkillContainer container) {
        return container.getExecutor().getSkill(BattleArtsSkillSlots.BATTLE_STYLE).hasSkill(OmneriaSkills.GENESIS_WYRM);
    }

    private float consumeMeter(float data, float amount) {
        return data - amount;
    }

    protected boolean consumeMeterFor(SkillContainer container, float usage)
    {
        if (container.getExecutor().getOriginal().isCreative())
            return true;
        SkillDataManager dm = container.getExecutor().getSkill(BattleArtsSkillSlots.BATTLE_STYLE).getDataManager();
        if (!dm.hasData(CoreAPIDataKeys.METER_FILL))
        {
            return false;
        }
        else
        {
            if (dm.getDataValue(CoreAPIDataKeys.METER_FILL) - usage <= 0)
            {
                return false;
            }
            else
            {
                dm.setDataSyncF(CoreAPIDataKeys.METER_FILL, data -> consumeMeter(data, usage));
                return true;
            }
        }
    }

    @Override
    public void executeOnServer(SkillContainer container, FriendlyByteBuf args) {
        int fw = args.readInt();
        int sw = args.readInt();
        int ud = args.readInt();

        if (ud == -1)
        {
            container.getExecutor().playAnimationSynchronized(GenesisWyrmAnimations.DRAGONSLAYER_LEAP, 0);
        }
        else if (fw == 1 && this.consumeMeterFor(container, 100))
        {
            container.getExecutor().playAnimationSynchronized(GenesisWyrmAnimations.DRAGON_RUSH_ATTEMPT, 0);
        }
        else if (fw == -1 && this.consumeMeterFor(container, 100))
        {
            container.getExecutor().playAnimationSynchronized(GenesisWyrmAnimations.DRAGON_CANNON, 0);
        }
        else
        {
            if (this.consumeMeterFor(container, 50))
                container.getExecutor().playAnimationSynchronized(GenesisWyrmAnimations.DARK_BANG, 0);
            else
                container.getExecutor().playAnimationSynchronized(GenesisWyrmAnimations.DARK_UPPER, 0);
        }
    }

    @Override
    public void updateContainer(SkillContainer container) {
        super.updateContainer(container);

    }
}
