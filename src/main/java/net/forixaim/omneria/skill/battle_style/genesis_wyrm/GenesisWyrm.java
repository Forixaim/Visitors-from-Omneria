package net.forixaim.omneria.skill.battle_style.genesis_wyrm;

import com.google.common.collect.Lists;
import io.netty.buffer.Unpooled;
import net.forixaim.battle_arts_api.battle_arts_skills.BattleArtsSkillSlots;
import net.forixaim.battle_arts_api.client.KeyBinds;
import net.forixaim.omneria.animations.battle_style.genesis_wyrm.GenesisWyrmAnimations;
import net.forixaim.omneria.client.particles.types.TrackingParticleType;
import net.forixaim.omneria.registry.ParticleRegistry;
import net.forixaim.omneria.skill.CommonEvents;
import net.forixaim.omneria.skill.DatakeyRegistry;
import net.forixaim.omneria.skill.OmneriaSkills;
import net.forixaim.omneria.skill.battle_style.OmneriaBattleStyle;
import net.forixaim.omneria.util.NetworkUtils;
import net.forixaim.omneria.world.entity.projectiles.DragonCannonBeam;
import net.forixaim.omneria.world.entity.projectiles.FullPowerDragonCannonBeam;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.RegistryObject;
import yesman.epicfight.api.animation.AnimationManager;
import yesman.epicfight.api.animation.LivingMotions;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.utils.math.ValueModifier;
import yesman.epicfight.client.input.EpicFightKeyMappings;
import yesman.epicfight.gameasset.EpicFightSkills;
import yesman.epicfight.model.armature.HumanoidArmature;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.SkillDataKey;
import yesman.epicfight.skill.SkillSlots;
import yesman.epicfight.skill.guard.GuardSkill;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.EntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;
import yesman.epicfight.world.entity.eventlistener.ComboCounterHandleEvent;
import yesman.epicfight.world.entity.eventlistener.PlayerEventListener;

import java.util.*;

@SuppressWarnings("unchecked")
public class GenesisWyrm extends OmneriaBattleStyle
{
    private static final UUID EVENT_UUID = UUID.fromString("68440271-f5d3-49bf-ba07-d7f9bdf55951");
    private static final UUID SPRINT_SPEED_BUFF = UUID.fromString("daea41d5-63e0-4d82-8a60-a96179764679");


    public static final AnimationManager.AnimationAccessor<? extends StaticAnimation>[] HAND_COMBO = new AnimationManager.AnimationAccessor[]{
            GenesisWyrmAnimations.HEAVY_AUTO1,
            GenesisWyrmAnimations.HEAVY_AUTO2
    };

    @Override
    public AnimationManager.AnimationAccessor<? extends StaticAnimation> getJump(SkillContainer container) {
        if (container.getExecutor().getOriginal().isShiftKeyDown())
        {
            return GenesisWyrmAnimations.CROUCH_JUMP;
        }
        if (container.getExecutor().getOriginal().isSprinting())
        {
            return GenesisWyrmAnimations.JUMP_RUN;
        }
        return GenesisWyrmAnimations.JUMP;
    }

    public static final AnimationManager.AnimationAccessor<? extends StaticAnimation>[] BLAST_COMBO = new AnimationManager.AnimationAccessor[]{
            GenesisWyrmAnimations.BLAST_AUTO1,
            GenesisWyrmAnimations.BLAST_AUTO2
    };

    @Override
    public boolean unarmedMoveset() {
        return true;
    }

    public GenesisWyrm(Builder<?> builder)
    {
        super(builder);
        this.unarmedLivingMotions.put(LivingMotions.IDLE, GenesisWyrmAnimations.IDLE_SET);
        this.unarmedLivingMotions.put(LivingMotions.BLOCK, GenesisWyrmAnimations.GUARD);
        this.unarmedLivingMotions.put(LivingMotions.KNEEL, GenesisWyrmAnimations.CROUCH);
        this.unarmedLivingMotions.put(LivingMotions.WALK, GenesisWyrmAnimations.WALK);
        this.unarmedLivingMotions.put(LivingMotions.RUN, GenesisWyrmAnimations.RUN);
        this.unarmedLivingMotions.put(LivingMotions.FALL, GenesisWyrmAnimations.AIR_FALL);

        this.unarmedInnateSkill = OmneriaSkills.INITIAL_FORCE;
        this.guardMaps.put((GuardSkill) EpicFightSkills.GUARD, Map.ofEntries(Map.entry(GuardSkill.BlockType.GUARD, Lists.newArrayList(GenesisWyrmAnimations.GUARD_HIT)),
                Map.entry(GuardSkill.BlockType.GUARD_BREAK, Lists.newArrayList(GenesisWyrmAnimations.GUARD_BREAK))));
        this.guardMaps.put((GuardSkill) EpicFightSkills.IMPACT_GUARD, Map.ofEntries(Map.entry(GuardSkill.BlockType.GUARD, Lists.newArrayList(GenesisWyrmAnimations.GUARD_HIT)),
                Map.entry(GuardSkill.BlockType.GUARD_BREAK, Lists.newArrayList(GenesisWyrmAnimations.GUARD_BREAK))));
        this.guardMaps.put((GuardSkill) EpicFightSkills.PARRYING, Map.ofEntries(Map.entry(GuardSkill.BlockType.GUARD, Lists.newArrayList(GenesisWyrmAnimations.GUARD_HIT)), Map.entry(GuardSkill.BlockType.ADVANCED_GUARD, Lists.newArrayList(GenesisWyrmAnimations.GUARD_PARRY1, GenesisWyrmAnimations.GUARD_PARRY2)),
                Map.entry(GuardSkill.BlockType.GUARD_BREAK, Lists.newArrayList(GenesisWyrmAnimations.GUARD_BREAK))));
        this.guardMaps.put((GuardSkill) OmneriaSkills.PRIMORDIAL_BARRIER, Map.ofEntries(Map.entry(GuardSkill.BlockType.GUARD, Lists.newArrayList(GenesisWyrmAnimations.GUARD_HIT))));

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
        NetworkUtils.changeSkill(container.getExecutor(), BattleArtsSkillSlots.BURST_ART, OmneriaSkills.TWILIGHT);
        NetworkUtils.changeSkill(container.getExecutor(), BattleArtsSkillSlots.COMBAT_ART, OmneriaSkills.DARK_ARTS);
        NetworkUtils.changeSkill(container.getExecutor(), BattleArtsSkillSlots.ULTIMATE_ART, OmneriaSkills.ULTIMA_FINALE);


        if (!container.getExecutor().isLogicalClient() && container.getExecutor().getOriginal().getMainHandItem().is(Items.AIR))
            container.getServerExecutor().modifyLivingMotionByCurrentItem(true);

        container.getExecutor().getEventListener().addEventListener(PlayerEventListener.EventType.SKILL_CAST_EVENT, EVENT_UUID, event -> {
            if ((event.getSkillContainer().getSlot() == SkillSlots.BASIC_ATTACK || event.getSkillContainer().getSkill() == this) && container.getDataManager().getDataValue(DatakeyRegistry.COUNTER_WINDOW.get()) > 0)
            {
                event.setStateExecutable(true);
            }
        });

        container.getExecutor().getEventListener().addEventListener(PlayerEventListener.EventType.SKILL_CONSUME_EVENT, EVENT_UUID, event -> {
            if (event.getSkill() == OmneriaSkills.TRAILBLAZE)
                event.setResourceType(Resource.NONE);
        });

        container.getExecutor().getEventListener().addEventListener(PlayerEventListener.EventType.MODIFY_DAMAGE_EVENT, EVENT_UUID, event -> {
            if (container.getExecutor().getOriginal().getMainHandItem().isEmpty())
            {
                event.attachValueModifier(ValueModifier.multiplier(7));
                if (container.getDataManager().getDataValue(DatakeyRegistry.TWILIGHT.get()))
                {
                    event.attachValueModifier(ValueModifier.multiplier(1.4f));
                }
            }
        });

        container.getExecutor().getEventListener().addEventListener(PlayerEventListener.EventType.DEAL_DAMAGE_EVENT_HURT, EVENT_UUID, event ->
        {
            CommonEvents.BUILD_METER(event);
            if (event.getDamageSource().getAnimation() == GenesisWyrmAnimations.DRAGON_THROW_TRY && EpicFightCapabilities.getEntityPatch(event.getTarget(), EntityPatch.class) instanceof LivingEntityPatch<?> livingEntityPatch) {
                if (livingEntityPatch.getArmature() instanceof HumanoidArmature && !event.getTarget().isDeadOrDying()) {
                    event.getPlayerPatch().playAnimationSynchronized(GenesisWyrmAnimations.DRAGON_THROW, 0);
                }
            }
            if (event.getDamageSource().getAnimation() == GenesisWyrmAnimations.DRAGON_RUSH_ATTEMPT && EpicFightCapabilities.getEntityPatch(event.getTarget(), EntityPatch.class) instanceof LivingEntityPatch<?> livingEntityPatch)
            {
                if (livingEntityPatch.getArmature() instanceof HumanoidArmature && !event.getTarget().isDeadOrDying()) {
                    event.getPlayerPatch().playAnimationSynchronized(GenesisWyrmAnimations.DRAGON_RUSH_ATTACK, 0);
                }
            }
        });

        container.getExecutor().getEventListener().addEventListener(PlayerEventListener.EventType.MOVEMENT_INPUT_EVENT, EVENT_UUID, event -> {
            if (event.getMovementInput().shiftKeyDown) {
                event.getMovementInput().forwardImpulse = 0;
                event.getMovementInput().leftImpulse = 0;
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
        NetworkUtils.changeSkill(container.getExecutor(), BattleArtsSkillSlots.BURST_ART, null);
        NetworkUtils.changeSkill(container.getExecutor(), BattleArtsSkillSlots.COMBAT_ART, null);
        NetworkUtils.changeSkill(container.getExecutor(), BattleArtsSkillSlots.ULTIMATE_ART, null);

        container.getExecutor().getEventListener().removeListener(PlayerEventListener.EventType.MOVEMENT_INPUT_EVENT, EVENT_UUID);
        container.getExecutor().getEventListener().removeListener(PlayerEventListener.EventType.DEAL_DAMAGE_EVENT_HURT, EVENT_UUID);
        container.getExecutor().getEventListener().removeListener(PlayerEventListener.EventType.SKILL_CONSUME_EVENT, EVENT_UUID);
        container.getExecutor().getEventListener().removeListener(PlayerEventListener.EventType.BASIC_ATTACK_EVENT, EVENT_UUID);
        container.getExecutor().getEventListener().removeListener(PlayerEventListener.EventType.MODIFY_DAMAGE_EVENT, EVENT_UUID);
        container.getExecutor().getEventListener().removeListener(PlayerEventListener.EventType.SKILL_CAST_EVENT, EVENT_UUID);
    }

    @Override
    public void executeOnServer(SkillContainer container, FriendlyByteBuf args)
    {
        super.executeOnServer(container, args);
        int comboCounter;

        AnimationManager.AnimationAccessor<? extends StaticAnimation> attackAnimation;
        if (!args.readBoolean() && !container.getDataManager().getDataValue(DatakeyRegistry.RIGHT_CLICKED.get())){
            if (container.getDataManager().getDataValue(DatakeyRegistry.SHIFT.get()))
            {
                attackAnimation = GenesisWyrmAnimations.DARK_UPPER;
            } else
            {
                if (container.getExecutor().getOriginal().onGround())
                {
                    comboCounter = container.getDataManager().getDataValue(DatakeyRegistry.OMNERIA_COMBO_GENESIS_WYRM.get());
                    comboCounter %= HAND_COMBO.length;
                    attackAnimation = HAND_COMBO[comboCounter];
                    comboCounter++;
                }
                else
                {
                    attackAnimation = GenesisWyrmAnimations.DRAGON_WHIRL;
                    comboCounter = 0;
                }

                setComboCounterWithEvent(ComboCounterHandleEvent.Causal.ANOTHER_ACTION_ANIMATION, container.getServerExecutor(), container, attackAnimation, comboCounter, DatakeyRegistry.OMNERIA_COMBO_GENESIS_WYRM);
            }
        }
        else
        {
            comboCounter = container.getDataManager().getDataValue(DatakeyRegistry.OMNERIA_COMBO_GENESIS_WYRM_BLAST.get());
            comboCounter %= BLAST_COMBO.length;
            attackAnimation = BLAST_COMBO[comboCounter];
            if (comboCounter == 0)
                comboCounter++;
            setComboCounterWithEvent(ComboCounterHandleEvent.Causal.ANOTHER_ACTION_ANIMATION, container.getServerExecutor(), container, attackAnimation, comboCounter, DatakeyRegistry.OMNERIA_COMBO_GENESIS_WYRM_BLAST);
        }

        if (attackAnimation != null) {
            container.getServerExecutor().playAnimationSynchronized(attackAnimation, 0);
            container.getServerExecutor().updateEntityState();
        }
    }

    @Override
    public void updateContainer(SkillContainer container)
    {
        super.updateContainer(container);
        AttributeInstance speed = container.getExecutor().getOriginal().getAttribute(Attributes.MOVEMENT_SPEED);
        if (!container.getExecutor().getOriginal().isCreative())
            container.getExecutor().getOriginal().getAbilities().mayfly = container.getDataManager().getDataValue(DatakeyRegistry.TWILIGHT.get()) && !container.getExecutor().getOriginal().isCreative();
        if (container.getExecutor().getOriginal().isSprinting()) {
            if (speed != null && speed.getModifier(SPRINT_SPEED_BUFF) == null) {
                speed.addTransientModifier(
                        new AttributeModifier(
                                SPRINT_SPEED_BUFF,
                                "custom sprint boost",
                                1.0,
                                AttributeModifier.Operation.MULTIPLY_TOTAL
                        ));
            }
        } else {
            if (speed != null)
                speed.removeModifier(SPRINT_SPEED_BUFF);
        }
        if (container.getExecutor().isLogicalClient())
        {
            container.getDataManager().setDataSync(DatakeyRegistry.RIGHT_CLICKED.get(), Minecraft.getInstance().options.keyUse.isDown());
            container.getDataManager().setDataSync(DatakeyRegistry.MOUSE3.get(),  EpicFightKeyMappings.GUARD.isDown());
            container.getDataManager().setDataSync(DatakeyRegistry.SHIFT.get(), Minecraft.getInstance().options.keyShift.isDown());
            container.getDataManager().setDataSync(DatakeyRegistry.ULT_HELD.get(), KeyBinds.USE_ULTIMATE_ART.isDown());
        }
        else
        {
            if (container.getExecutor().getSkill(SkillSlots.WEAPON_INNATE).getSkill() != OmneriaSkills.INITIAL_FORCE && container.getExecutor().getOriginal().getMainHandItem().isEmpty())
            {
                NetworkUtils.changeSkill(container.getServerExecutor(), SkillSlots.WEAPON_INNATE, OmneriaSkills.INITIAL_FORCE);
            }
            else if (!container.getExecutor().getOriginal().getMainHandItem().isEmpty() && container.getExecutor().getHoldingItemCapability(InteractionHand.MAIN_HAND).getInnateSkill(container.getServerExecutor(), container.getExecutor().getOriginal().getMainHandItem()) == null)
            {
                NetworkUtils.changeSkill(container.getServerExecutor(), SkillSlots.WEAPON_INNATE, null);
            }
            if (container.getDataManager().getDataValue(DatakeyRegistry.REFLECT_WINDOW.get()) > 0)
            {
                container.getDataManager().setDataSyncF(DatakeyRegistry.REFLECT_WINDOW.get(), data -> data - 1);
            }
            if (container.getDataManager().getDataValue(DatakeyRegistry.COUNTER_WINDOW.get()) > 0)
            {
                container.getDataManager().setDataSyncF(DatakeyRegistry.COUNTER_WINDOW.get(), data -> data - 1);
            }
            if (container.getExecutor().getOriginal().tickCount % 4 == 0 && container.getDataManager().getDataValue(DatakeyRegistry.TWILIGHT.get())) {
                ((ServerLevel) container.getServerExecutor().getOriginal().level()).sendParticles(new TrackingParticleType(container.getServerExecutor().getOriginal().getId(), ParticleRegistry.GENESIS_AURA.get()),
                        container.getServerExecutor().getOriginal().getX(),
                        container.getServerExecutor().getOriginal().getY() +
                                container.getServerExecutor().getOriginal().getEyeHeight(),
                        container.getServerExecutor().getOriginal().getZ(), 1, 0.0, 0.0, 0.0, 0.0);
            }
        }
        if (container.getDataManager().hasData(DatakeyRegistry.BEAM.get())) {
            Entity test = container.getExecutor().getOriginal().level().getEntity(container.getDataManager().getDataValue(DatakeyRegistry.BEAM.get()));
            if (test instanceof DragonCannonBeam || test instanceof FullPowerDragonCannonBeam)
            {
                if (test.isRemoved())
                    container.getDataManager().setDataSync(DatakeyRegistry.BEAM.get(), -1);
            }
        }

        if (container.getExecutor().getTickSinceLastAction() > 16 && container.getDataManager().getDataValue(DatakeyRegistry.OMNERIA_COMBO_GENESIS_WYRM.get()) > 0)
        {
            setComboCounterWithEvent(ComboCounterHandleEvent.Causal.TIME_EXPIRED, container.getServerExecutor(), container, null, 0, DatakeyRegistry.OMNERIA_COMBO_GENESIS_WYRM);
        }
        if (container.getExecutor().getTickSinceLastAction() > 16 && container.getDataManager().getDataValue(DatakeyRegistry.OMNERIA_COMBO_GENESIS_WYRM_LEGS.get()) > 0)
        {
            setComboCounterWithEvent(ComboCounterHandleEvent.Causal.TIME_EXPIRED, container.getServerExecutor(), container, null, 0, DatakeyRegistry.OMNERIA_COMBO_GENESIS_WYRM_LEGS);
        }
        if (container.getExecutor().getTickSinceLastAction() > 5 && container.getDataManager().getDataValue(DatakeyRegistry.OMNERIA_COMBO_GENESIS_WYRM_BLAST.get()) > 0)
        {
            setComboCounterWithEvent(ComboCounterHandleEvent.Causal.TIME_EXPIRED, container.getServerExecutor(), container, null, 0, DatakeyRegistry.OMNERIA_COMBO_GENESIS_WYRM_BLAST);
        }
    }
}
