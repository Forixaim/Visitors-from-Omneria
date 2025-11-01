package net.forixaim.omneria.skill.battle_style.genesis_wyrm;

import net.forixaim.omneria.animations.battle_style.genesis_wyrm.GenesisWyrmAnimations;
import net.forixaim.omneria.skill.DatakeyRegistry;
import net.minecraft.network.FriendlyByteBuf;
import yesman.epicfight.api.animation.AnimationManager;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.skill.SkillBuilder;
import yesman.epicfight.skill.SkillContainer;
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
    public boolean canExecute(SkillContainer container) {
        return container.getExecutor().getOriginal().getMainHandItem().isEmpty();
    }

    @Override
    public void executeOnServer(SkillContainer container, FriendlyByteBuf args) {
        super.executeOnServer(container, args);
        int combo = container.getDataManager().getDataValue(DatakeyRegistry.INITIAL_FORCE_COMBO.get());
        combo %= DEFAULT_COMBO.length;
        AnimationManager.AnimationAccessor<? extends StaticAnimation> anim = DEFAULT_COMBO[combo];
        combo++;
        GenesisWyrm.setComboCounterWithEvent(ComboCounterHandleEvent.Causal.ANOTHER_ACTION_ANIMATION, container.getServerExecutor(), container, anim, combo, DatakeyRegistry.INITIAL_FORCE_COMBO);
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
