package net.forixaim.omneria.skill.battle_style.genesis_wyrm;

import io.netty.buffer.Unpooled;
import net.forixaim.omneria.animations.battle_style.genesis_wyrm.GenesisWyrmAnimations;
import net.forixaim.omneria.skill.DatakeyRegistry;
import net.forixaim.omneria.skill.OmneriaSkills;
import net.forixaim.omneria.skill.battle_style.OmneriaBattleStyle;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.RegistryObject;
import yesman.epicfight.api.animation.AnimationManager;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.utils.math.ValueModifier;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.SkillDataKey;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;
import yesman.epicfight.world.entity.eventlistener.ComboCounterHandleEvent;
import yesman.epicfight.world.entity.eventlistener.PlayerEventListener;

import java.util.UUID;

@SuppressWarnings("unchecked")
public class GenesisWyrm extends OmneriaBattleStyle
{
    private static final UUID EVENT_UUID = UUID.fromString("68440271-f5d3-49bf-ba07-d7f9bdf55951");
    public static final AnimationManager.AnimationAccessor<? extends StaticAnimation>[] HAND_COMBO = new AnimationManager.AnimationAccessor[]{
            GenesisWyrmAnimations.AUTO1,
            GenesisWyrmAnimations.AUTO2,
            GenesisWyrmAnimations.AUTO3,
            GenesisWyrmAnimations.AUTO4,
            GenesisWyrmAnimations.AUTO5
    };
    public static final AnimationManager.AnimationAccessor<? extends StaticAnimation>[] LEG_COMBO = new AnimationManager.AnimationAccessor[]{
            GenesisWyrmAnimations.LEG_AUTO1,
            GenesisWyrmAnimations.LEG_AUTO2
    };

    public static final AnimationManager.AnimationAccessor<? extends StaticAnimation>[] BLAST_COMBO = new AnimationManager.AnimationAccessor[]{
            GenesisWyrmAnimations.BLAST_AUTO1,
            GenesisWyrmAnimations.BLAST_AUTO2
    };

    public GenesisWyrm(Builder<?> builder)
    {
        super(builder);
    }

    public static void setComboCounterWithEvent(ComboCounterHandleEvent.Causal reason, ServerPlayerPatch playerpatch, SkillContainer container, AnimationManager.AnimationAccessor<? extends StaticAnimation> causalAnimation, int value,
                                                RegistryObject<SkillDataKey<Integer>> dataKey) {
        int prevValue = container.getDataManager().getDataValue(dataKey.get());
        ComboCounterHandleEvent comboResetEvent = new ComboCounterHandleEvent(reason, playerpatch, causalAnimation, prevValue, value);
        container.getExecutor().getEventListener().triggerEvents(PlayerEventListener.EventType.COMBO_COUNTER_HANDLE_EVENT, comboResetEvent);
        container.getDataManager().setData(dataKey.get(), comboResetEvent.getNextValue());
    }

    @Override
    public void onInitiate(SkillContainer container)
    {
        super.onInitiate(container);
        container.getExecutor().getEventListener().addEventListener(PlayerEventListener.EventType.SKILL_CONSUME_EVENT, EVENT_UUID, event -> {
            if (event.getSkill() == OmneriaSkills.TRAILBLAZE)
                event.setResourceType(Resource.NONE);
        });

        container.getExecutor().getEventListener().addEventListener(PlayerEventListener.EventType.MODIFY_DAMAGE_EVENT, EVENT_UUID, event -> {
            if (container.getExecutor().getOriginal().getMainHandItem().isEmpty())
            {
                event.attachValueModifier(ValueModifier.multiplier(7));
            }
        });

        container.getExecutor().getEventListener().addEventListener(PlayerEventListener.EventType.SERVER_ITEM_USE_EVENT, EVENT_UUID, event -> {
            if (container.getExecutor().getOriginal().getMainHandItem().isEmpty())
            {
                event.setCanceled(true);
                container.requestCasting(event.getPlayerPatch(), new FriendlyByteBuf(Unpooled.buffer().writeBoolean(true)));
            }
        });

        container.getExecutor().getEventListener().addEventListener(PlayerEventListener.EventType.BASIC_ATTACK_EVENT, EVENT_UUID, event -> {
            ItemStack mainhandItemStack = container.getServerExecutor().getOriginal().getMainHandItem();
            if (mainhandItemStack.isEmpty()) {
                event.setCanceled(true);
                container.requestCasting(event.getPlayerPatch(), new FriendlyByteBuf(Unpooled.buffer().writeBoolean(false)));
            }
        });
    }

    @Override
    public void onRemoved(SkillContainer container)
    {
        super.onRemoved(container);
        container.getExecutor().getEventListener().removeListener(PlayerEventListener.EventType.SKILL_CONSUME_EVENT, EVENT_UUID);
        container.getExecutor().getEventListener().removeListener(PlayerEventListener.EventType.BASIC_ATTACK_EVENT, EVENT_UUID);
        container.getExecutor().getEventListener().removeListener(PlayerEventListener.EventType.SERVER_ITEM_USE_EVENT, EVENT_UUID);
        container.getExecutor().getEventListener().removeListener(PlayerEventListener.EventType.MODIFY_DAMAGE_EVENT, EVENT_UUID);
    }

    @Override
    public void executeOnServer(SkillContainer container, FriendlyByteBuf args)
    {
        super.executeOnServer(container, args);
        int comboCounter;

        AnimationManager.AnimationAccessor<? extends StaticAnimation> attackAnimation;
        if (!args.readBoolean() && !container.getDataManager().getDataValue(DatakeyRegistry.RIGHT_CLICKED.get())){
            if (container.getExecutor().getOriginal().isShiftKeyDown())
            {
                comboCounter = container.getDataManager().getDataValue(DatakeyRegistry.OMNERIA_COMBO_GENESIS_WYRM_LEGS.get());
                comboCounter %= LEG_COMBO.length;
                attackAnimation = LEG_COMBO[comboCounter];
                comboCounter++;
                setComboCounterWithEvent(ComboCounterHandleEvent.Causal.ANOTHER_ACTION_ANIMATION, container.getServerExecutor(), container, attackAnimation, comboCounter, DatakeyRegistry.OMNERIA_COMBO_GENESIS_WYRM_LEGS);
            } else
            {
                comboCounter = container.getDataManager().getDataValue(DatakeyRegistry.OMNERIA_COMBO_GENESIS_WYRM.get());
                comboCounter %= HAND_COMBO.length;
                attackAnimation = HAND_COMBO[comboCounter];
                comboCounter++;
                setComboCounterWithEvent(ComboCounterHandleEvent.Causal.ANOTHER_ACTION_ANIMATION, container.getServerExecutor(), container, attackAnimation, comboCounter, DatakeyRegistry.OMNERIA_COMBO_GENESIS_WYRM);
            }
        }
        else
        {
            comboCounter = container.getDataManager().getDataValue(DatakeyRegistry.OMNERIA_COMBO_GENESIS_WYRM_BLAST.get());
            comboCounter %= BLAST_COMBO.length;
            attackAnimation = BLAST_COMBO[comboCounter];
            comboCounter++;
            setComboCounterWithEvent(ComboCounterHandleEvent.Causal.ANOTHER_ACTION_ANIMATION, container.getServerExecutor(), container, attackAnimation, comboCounter, DatakeyRegistry.OMNERIA_COMBO_GENESIS_WYRM_BLAST);
        }

        if (attackAnimation != null)
            container.getServerExecutor().playAnimationSynchronized(attackAnimation, 0);
    }

    @Override
    public void updateContainer(SkillContainer container)
    {
        super.updateContainer(container);
        if (container.getExecutor().isLogicalClient())
        {
            if (Minecraft.getInstance().options.keyUse.isDown())
            {
                container.getDataManager().setDataSync(DatakeyRegistry.RIGHT_CLICKED.get(),  true);
            }
            else
            {
                container.getDataManager().setDataSync(DatakeyRegistry.RIGHT_CLICKED.get(),  false);
            }
        }
        if (container.getExecutor().getTickSinceLastAction() > 16 && container.getDataManager().getDataValue(DatakeyRegistry.OMNERIA_COMBO_GENESIS_WYRM.get()) > 0)
        {
            setComboCounterWithEvent(ComboCounterHandleEvent.Causal.TIME_EXPIRED, container.getServerExecutor(), container, null, 0, DatakeyRegistry.OMNERIA_COMBO_GENESIS_WYRM);
            setComboCounterWithEvent(ComboCounterHandleEvent.Causal.TIME_EXPIRED, container.getServerExecutor(), container, null, 0, DatakeyRegistry.OMNERIA_COMBO_GENESIS_WYRM_LEGS);
            setComboCounterWithEvent(ComboCounterHandleEvent.Causal.TIME_EXPIRED, container.getServerExecutor(), container, null, 0, DatakeyRegistry.OMNERIA_COMBO_GENESIS_WYRM_BLAST);


        }
    }
}
