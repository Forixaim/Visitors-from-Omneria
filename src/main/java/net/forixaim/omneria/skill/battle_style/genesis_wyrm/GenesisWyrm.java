package net.forixaim.omneria.skill.battle_style.genesis_wyrm;

import com.google.common.collect.Lists;
import io.netty.buffer.Unpooled;
import net.forixaim.battle_arts_api.battle_arts_skills.BattleArtsSkillSlots;
import net.forixaim.omneria.animations.battle_style.genesis_wyrm.GenesisWyrmAnimations;
import net.forixaim.omneria.client.particles.types.TrackingParticleType;
import net.forixaim.omneria.registry.ParticleRegistry;
import net.forixaim.omneria.registry.SoundRegistry;
import net.forixaim.omneria.skill.CommonEvents;
import net.forixaim.omneria.skill.DatakeyRegistry;
import net.forixaim.omneria.skill.OmneriaSkills;
import net.forixaim.omneria.skill.battle_style.OmneriaBattleStyle;
import net.forixaim.omneria.util.NetworkUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.RelativeMovement;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.RegistryObject;
import yesman.epicfight.api.animation.AnimationManager;
import yesman.epicfight.api.animation.LivingMotions;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.utils.AttackResult;
import yesman.epicfight.api.utils.math.ValueModifier;
import yesman.epicfight.client.input.EpicFightKeyMappings;
import yesman.epicfight.client.world.capabilites.entitypatch.player.LocalPlayerPatch;
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
import yesman.epicfight.world.damagesource.StunType;
import yesman.epicfight.world.entity.eventlistener.ComboCounterHandleEvent;
import yesman.epicfight.world.entity.eventlistener.PlayerEventListener;

import java.util.*;

@SuppressWarnings("unchecked")
public class GenesisWyrm extends OmneriaBattleStyle
{
    private static final UUID EVENT_UUID = UUID.fromString("68440271-f5d3-49bf-ba07-d7f9bdf55951");
    private static final UUID SPRINT_SPEED_BUFF = UUID.fromString("daea41d5-63e0-4d82-8a60-a96179764679");


    public static final AnimationManager.AnimationAccessor<? extends StaticAnimation>[] DODGES = new AnimationManager.AnimationAccessor[]{
            GenesisWyrmAnimations.DRACONIC_DODGE1,
            GenesisWyrmAnimations.DRACONIC_DODGE2,
            GenesisWyrmAnimations.DRACONIC_DODGE3
    };
    public static final AnimationManager.AnimationAccessor<? extends StaticAnimation>[] HAND_COMBO = new AnimationManager.AnimationAccessor[]{
            GenesisWyrmAnimations.HEAVY_AUTO1,
            GenesisWyrmAnimations.HEAVY_AUTO2
    };

    boolean isMovingBackward(Player player) {
        Vec3 vel = player.getDeltaMovement();
        Vec3 forward = player.getLookAngle(); // includes pitch but that's fine

        // We only care about horizontal movement
        Vec3 velH = new Vec3(vel.x, 0, vel.z);
        Vec3 forwardH = new Vec3(forward.x, 0, forward.z);

        if (velH.lengthSqr() < 0.0001)
            return false; // not moving at all

        velH = velH.normalize();
        forwardH = forwardH.normalize();

        double dot = velH.dot(forwardH);

        return dot < 0; // negative = moving opposite direction
    }

    @Override
    public AnimationManager.AnimationAccessor<? extends StaticAnimation> getJump(SkillContainer container) {
        boolean flag = isMovingBackward(container.getExecutor().getOriginal());
        if (container.getExecutor().getOriginal().isSprinting())
        {
            return GenesisWyrmAnimations.JUMP_RUN;
        }
        else if (flag)
        {
            return GenesisWyrmAnimations.JUMP_BACK;
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

        this.unarmedInnateSkill = OmneriaSkills.INITIAL_FORCE;
        this.guardMaps.put((GuardSkill) EpicFightSkills.GUARD, Map.ofEntries(Map.entry(GuardSkill.BlockType.GUARD, GenesisWyrmAnimations.GUARD_HIT)));
        this.guardMaps.put((GuardSkill) EpicFightSkills.IMPACT_GUARD, Map.ofEntries(Map.entry(GuardSkill.BlockType.GUARD, GenesisWyrmAnimations.GUARD_HIT)));
        this.guardMaps.put((GuardSkill) EpicFightSkills.PARRYING, Map.ofEntries(Map.entry(GuardSkill.BlockType.GUARD, GenesisWyrmAnimations.GUARD_HIT)));

        this.guardMaps.put((GuardSkill) OmneriaSkills.PRIMORDIAL_BARRIER, Map.ofEntries(Map.entry(GuardSkill.BlockType.GUARD, GenesisWyrmAnimations.GUARD_HIT)));

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
                event.attachValueModifier(ValueModifier.setter(7));
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



        container.getExecutor().getEventListener().addEventListener(PlayerEventListener.EventType.SERVER_ITEM_USE_EVENT, EVENT_UUID, event -> {
            if (container.getExecutor().getOriginal().getMainHandItem().isEmpty())
            {
                event.setCanceled(true);
                container.requestCasting(event.getPlayerPatch(), new FriendlyByteBuf(Unpooled.buffer().writeBoolean(true)));
            }
        });

        container.getExecutor().getEventListener().addEventListener(PlayerEventListener.EventType.MOVEMENT_INPUT_EVENT, EVENT_UUID, event -> {
            if (event.getMovementInput().shiftKeyDown) {
                event.getMovementInput().forwardImpulse = 0;
                event.getMovementInput().leftImpulse = 0;
                event.getMovementInput().jumping = false;
            }
            else
            {
                float percentage = event.getPlayerPatch().getOriginal().getHealth() / event.getPlayerPatch().getOriginal().getMaxHealth();
                if (percentage < 0.5)
                {
                    if (percentage < 0.25)
                    {
                        event.getMovementInput().forwardImpulse *= 0.5f;
                        event.getMovementInput().leftImpulse *= 0.5f;
                    }
                    else {
                        event.getMovementInput().forwardImpulse *= 0.75f;
                        event.getMovementInput().leftImpulse *= 0.75f;
                    }
                }
            }
        });

        container.getExecutor().getEventListener().addEventListener(PlayerEventListener.EventType.TAKE_DAMAGE_EVENT_ATTACK, EVENT_UUID, event -> {
            boolean isFront = false;
            Vec3 sourceLocation = event.getDamageSource().getSourcePosition();
            if (sourceLocation != null) {
                Vec3 viewVector = event.getPlayerPatch().getOriginal().getViewVector(1.0F);
                viewVector = viewVector.subtract(0.0F, viewVector.y, 0.0F).normalize();
                Vec3 toSourceLocation = sourceLocation.subtract(event.getPlayerPatch().getOriginal().position()).normalize();
                if (toSourceLocation.dot(viewVector) > (double)0.0F) {
                    isFront = true;
                }
            }
            if (container.getDataManager().getDataValue(DatakeyRegistry.REFLECT_WINDOW.get()) > 0 && isFront)
            {

                event.setParried(true);
                event.setResult(AttackResult.ResultType.BLOCKED);
                event.setCanceled(true);
                event.getPlayerPatch().playSound(SoundEvents.ANVIL_PLACE, 1.0F, 1.0F);
                container.getDataManager().setDataSync(DatakeyRegistry.REFLECT_WINDOW.get(), 0);

                if (event.getDamageSource().getDirectEntity() instanceof Projectile projectile)
                {
                    if (event.getDamageSource().getEntity() instanceof LivingEntity livingEntity)
                        container.getDataManager().setDataSync(DatakeyRegistry.OPPONENT.get(), livingEntity.getId());
                    container.getDataManager().setDataSync(DatakeyRegistry.PROJECTILE.get(), true);
                    if (projectile.getTags().contains("omneria:energy_projectile"))
                        projectile.discard();
                }
                else
                {
                    container.getDataManager().setDataSync(DatakeyRegistry.PROJECTILE.get(), false);
                }
                container.getDataManager().setDataSync(DatakeyRegistry.COUNTER_WINDOW.get(), 12);
            }
            List<ResourceKey<DamageType>> IGNORED_DAMAGES = Lists.newArrayList(
                    DamageTypes.ON_FIRE,
                    DamageTypes.FALL,
                    DamageTypes.STALAGMITE,
                    DamageTypes.IN_FIRE,
                    DamageTypes.HOT_FLOOR,
                    DamageTypes.LAVA
            );
            boolean flag = true;
            for (ResourceKey<DamageType> resourceKey : IGNORED_DAMAGES)
            {
                if (event.getDamageSource().is(resourceKey))
                {
                    flag = false;
                    break;
                }
            }
            if (!event.getPlayerPatch().getOriginal().isSprinting() && !event.getPlayerPatch().getEntityState().attacking() && flag)
            {

                List<AnimationManager.AnimationAccessor<? extends StaticAnimation>> dodges = Lists.newArrayList(DODGES);
                if (container.getDataManager().getDataValue(DatakeyRegistry.DODGE_ANIM.get()) >= 0 && dodges.contains(AnimationManager.byId(container.getDataManager().getDataValue(DatakeyRegistry.DODGE_ANIM.get()))))
                {
                    AnimationManager.AnimationAccessor<? extends  StaticAnimation> dodge = AnimationManager.byId(container.getDataManager().getDataValue(DatakeyRegistry.DODGE_ANIM.get()));
                    dodges.remove(dodge);
                }
                RandomSource rng = event.getPlayerPatch().getOriginal().getRandom();
                int index =  rng.nextInt(dodges.size());
                container.getDataManager().setDataSync(DatakeyRegistry.DODGE_ANIM.get(), dodges.get(index).id());
                event.getPlayerPatch().onDodgeSuccess(event.getDamageSource(), event.getDamageSource().getSourcePosition());
                event.getPlayerPatch().playSound(SoundRegistry.WYRMDODGE.get(), 1, 1);
                event.getPlayerPatch().playAnimationSynchronized(dodges.get(index), 0);
                event.setResult(AttackResult.ResultType.MISSED);
                event.setCanceled(true);
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

        container.getExecutor().getEventListener().removeListener(PlayerEventListener.EventType.TAKE_DAMAGE_EVENT_ATTACK, EVENT_UUID);
        container.getExecutor().getEventListener().removeListener(PlayerEventListener.EventType.MOVEMENT_INPUT_EVENT, EVENT_UUID);
        container.getExecutor().getEventListener().removeListener(PlayerEventListener.EventType.DEAL_DAMAGE_EVENT_HURT, EVENT_UUID);
        container.getExecutor().getEventListener().removeListener(PlayerEventListener.EventType.SKILL_CONSUME_EVENT, EVENT_UUID);
        container.getExecutor().getEventListener().removeListener(PlayerEventListener.EventType.BASIC_ATTACK_EVENT, EVENT_UUID);
        container.getExecutor().getEventListener().removeListener(PlayerEventListener.EventType.SERVER_ITEM_USE_EVENT, EVENT_UUID);
        container.getExecutor().getEventListener().removeListener(PlayerEventListener.EventType.MODIFY_DAMAGE_EVENT, EVENT_UUID);
        container.getExecutor().getEventListener().removeListener(PlayerEventListener.EventType.SKILL_CAST_EVENT, EVENT_UUID);
    }

    @Override
    public void executeOnServer(SkillContainer container, FriendlyByteBuf args)
    {
        super.executeOnServer(container, args);
        int comboCounter;

        AnimationManager.AnimationAccessor<? extends StaticAnimation> attackAnimation;
        if (container.getDataManager().getDataValue(DatakeyRegistry.COUNTER_WINDOW.get()) > 0)
        {
            if (container.getDataManager().getDataValue(DatakeyRegistry.PROJECTILE.get()) && container.getDataManager().getDataValue(DatakeyRegistry.OPPONENT.get()) >= 0)
            {
                Entity opponent = container.getServerExecutor().getOriginal().serverLevel().getEntity(container.getDataManager().getDataValue(DatakeyRegistry.OPPONENT.get()));
                container.getDataManager().setDataSync(DatakeyRegistry.OPPONENT.get(), -1);
                if (EpicFightCapabilities.getEntityPatch(opponent, EntityPatch.class) instanceof LivingEntityPatch<?> lol && lol.getOriginal().isDeadOrDying())
                    lol.applyStun(StunType.HOLD, 2);
                if (opponent instanceof LivingEntity livingOpponent && !livingOpponent.isDeadOrDying())
                {
                    Vec3 opponentPos = livingOpponent.position();
                    Vec3 lookVec = livingOpponent.getLookAngle().normalize();
                    Vec3 behindPos = opponentPos.subtract(lookVec.scale(2f));
                    container.getExecutor().getOriginal().teleportTo((ServerLevel) livingOpponent.level(), behindPos.x, opponentPos.y, behindPos.z, RelativeMovement.ALL, livingOpponent.yHeadRot, container.getServerExecutor().getOriginal().getViewXRot(1.0f));
                    Vec3 toOpponent = opponentPos.subtract(behindPos);
                    double yaw = Math.toDegrees(Math.atan2(-toOpponent.x, -toOpponent.z));
                    double pitch = Math.toDegrees(-Math.atan2(toOpponent.y, Math.sqrt(toOpponent.x * toOpponent.x + toOpponent.z * toOpponent.z)));
                    container.getExecutor().getOriginal().setYRot((float)yaw);
                    container.getExecutor().getOriginal().setXRot((float)pitch);
                    if (container.getExecutor() instanceof LocalPlayerPatch)
                    {
                        Minecraft mc = Minecraft.getInstance();
                        if (mc.player != null) {
                            mc.player.setYRot((float)yaw);
                            mc.player.setXRot((float)pitch);
                            mc.player.yRotO = (float)yaw;
                            mc.player.xRotO = (float)pitch;
                        }
                    }
                    container.getServerExecutor().playSound(SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F);
                    attackAnimation = GenesisWyrmAnimations.RANGED_COUNTER;
                }
                else
                {
                    attackAnimation = GenesisWyrmAnimations.MELEE_COUNTER;
                }
            }
            else
            {
                attackAnimation = GenesisWyrmAnimations.MELEE_COUNTER;
            }

            container.getDataManager().setDataSync(DatakeyRegistry.COUNTER_WINDOW.get(), 0);
        }

        else if (container.getDataManager().getDataValue(DatakeyRegistry.MOUSE3.get()))
        {
            attackAnimation = GenesisWyrmAnimations.REFLECTION;
            container.getExecutor().resetHolding();
            container.getDataManager().setDataSync(DatakeyRegistry.REFLECT_WINDOW.get(), 8);
        }
        else if (!args.readBoolean() && !container.getDataManager().getDataValue(DatakeyRegistry.RIGHT_CLICKED.get())){
            if (container.getDataManager().getDataValue(DatakeyRegistry.SHIFT.get()))
            {
                attackAnimation = GenesisWyrmAnimations.DARK_UPPER;
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
            // remove modifier when not sprinting
            if (speed != null)
                speed.removeModifier(SPRINT_SPEED_BUFF);
        }
        if (container.getExecutor().isLogicalClient())
        {
            container.getDataManager().setDataSync(DatakeyRegistry.RIGHT_CLICKED.get(), Minecraft.getInstance().options.keyUse.isDown());
            container.getDataManager().setDataSync(DatakeyRegistry.MOUSE3.get(),  EpicFightKeyMappings.GUARD.isDown());
            container.getDataManager().setDataSync(DatakeyRegistry.SHIFT.get(), Minecraft.getInstance().options.keyShift.isDown());

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
