package net.forixaim.omneria.skill.battle_style.genesis_wyrm;

import com.mojang.logging.LogUtils;
import net.forixaim.battle_arts_api.battle_arts_skills.BattleArtsSkillSlots;
import net.forixaim.battle_arts_api.battle_arts_skills.active.combat_arts.CombatArt;
import net.forixaim.omneria.animations.battle_style.genesis_wyrm.GenesisWyrmAnimations;
import net.forixaim.omneria.skill.DatakeyRegistry;
import net.forixaim.omneria.skill.OmneriaSkills;
import net.forixaim.omneria.skill.battle_style.imperatrice_lumiere.ArgumentGatherers;
import net.forixaim.omneria.world.entity.projectiles.DragonCannonBeam;
import net.minecraft.network.FriendlyByteBuf;
import yesman.epicfight.client.events.engine.ControlEngine;
import yesman.epicfight.skill.SkillBuilder;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.world.capabilities.item.CapabilityItem;

public class DarkArts extends CombatArt {
    public DarkArts(SkillBuilder<? extends CombatArt> builder) {
        super(builder);
        this.allowedWeapons.add(CapabilityItem.WeaponCategories.FIST);
    }

    @Override
    public FriendlyByteBuf gatherArguments(SkillContainer container, ControlEngine controlEngine) {
        return ArgumentGatherers.UniversalDirectionalInput(container, controlEngine);
    }

    @Override
    public Object getExecutionPacket(SkillContainer container, FriendlyByteBuf args) {
        return ArgumentGatherers.DirectionalExecutionPacket(container, args);
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
        int fw = args.readInt();
        int sw = args.readInt();
        int ud = args.readInt();

        if (fw == -1)
        {
            container.getExecutor().playAnimationSynchronized(GenesisWyrmAnimations.DRAGON_CANNON, 0);

        }
        else
        {
            container.getExecutor().playAnimationSynchronized(GenesisWyrmAnimations.DARK_BANG, 0);
        }
    }

    @Override
    public void updateContainer(SkillContainer container) {
        super.updateContainer(container);
        if (container.getDataManager().hasData(DatakeyRegistry.BEAM.get()) && container.getExecutor().getOriginal().level().getEntity(container.getDataManager().getDataValue(DatakeyRegistry.BEAM.get())) instanceof DragonCannonBeam beam)
        {
            if (beam.isRemoved())
                container.getDataManager().setDataSync(DatakeyRegistry.BEAM.get(), -1);
        }
    }
}
