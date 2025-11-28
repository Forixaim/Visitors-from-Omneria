package net.forixaim.omneria.skill.battle_style.genesis_wyrm;

import net.forixaim.battle_arts_api.battle_arts_skills.BattleArtsSkillSlots;
import net.forixaim.omneria.animations.battle_style.genesis_wyrm.GenesisWyrmAnimations;
import net.forixaim.omneria.skill.DatakeyRegistry;
import net.forixaim.omneria.skill.battle_style.imperatrice_lumiere.ArgumentGatherers;
import net.minecraft.network.FriendlyByteBuf;
import yesman.epicfight.api.animation.AnimationManager;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.client.events.engine.ControlEngine;
import yesman.epicfight.skill.SkillBuilder;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.SkillDataManager;
import yesman.epicfight.skill.weaponinnate.WeaponInnateSkill;
import yesman.epicfight.world.entity.eventlistener.ComboCounterHandleEvent;

@SuppressWarnings("unchecked")
public class InitialForce extends WeaponInnateSkill
{
    private final AnimationManager.AnimationAccessor<? extends StaticAnimation>[] DEFAULT_COMBO = new  AnimationManager.AnimationAccessor[]{
            GenesisWyrmAnimations.AUTO1,
            GenesisWyrmAnimations.AUTO2,
            GenesisWyrmAnimations.AUTO3,
            GenesisWyrmAnimations.AUTO4,
            GenesisWyrmAnimations.AUTO5
    };

    public InitialForce(SkillBuilder<? extends WeaponInnateSkill> builder) {
        super(builder);
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
        return container.getExecutor().getOriginal().getMainHandItem().isEmpty();
    }

    @Override
    public void executeOnServer(SkillContainer container, FriendlyByteBuf args) {
        super.executeOnServer(container, args);
        int combo = container.getDataManager().getDataValue(DatakeyRegistry.INITIAL_FORCE_COMBO.get());
        SkillDataManager skillDataManager = container.getExecutor().getSkill(BattleArtsSkillSlots.BATTLE_STYLE).getDataManager();
        AnimationManager.AnimationAccessor<? extends StaticAnimation> anim;
        int fw = args.readInt();
        int sw = args.readInt();
        int ud = args.readInt();

        if (ud == -1)
        {
            anim = GenesisWyrmAnimations.DRAGON_UPPERCUT;
        }
        else
        {
            if (container.getExecutor().getOriginal().isSprinting()) {
                anim = GenesisWyrmAnimations.UMBRAL_HAMMER;
            } else if (skillDataManager.hasData(DatakeyRegistry.RIGHT_CLICKED.get()) && skillDataManager.getDataValue(DatakeyRegistry.RIGHT_CLICKED.get())) {
                anim = GenesisWyrmAnimations.DRAGON_THROW_TRY;
            } else {
                anim = GenesisWyrmAnimations.MOONLIGHT_FINISH;
            }
        }


        if (anim != null)
        {
            container.getExecutor().playAnimationSynchronized(anim, 0);
            container.getExecutor().updateEntityState();
        }
    }

    @Override
    public void updateContainer(SkillContainer container) {
        super.updateContainer(container);
        if (container.getExecutor().getTickSinceLastAction() > 16 && container.getDataManager().getDataValue(DatakeyRegistry.INITIAL_FORCE_COMBO.get()) > 0)
        {
            GenesisWyrm.setComboCounterWithEvent(ComboCounterHandleEvent.Causal.TIME_EXPIRED, container.getServerExecutor(), container, null, 0, DatakeyRegistry.INITIAL_FORCE_COMBO);
        }
    }
}
