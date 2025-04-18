package net.forixaim.vfo.skill.battle_style.imperatrice_lumiere;

import net.forixaim.bs_api.battle_arts_skills.BattleArtsSkillSlots;
import net.forixaim.vfo.animations.battle_style.imperatrice_lumiere.sword.LumiereSwordAnims;
import net.forixaim.vfo.capabilities.styles.LumiereStyles;
import net.forixaim.vfo.skill.DatakeyRegistry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.PlayerRideableJumping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import yesman.epicfight.api.animation.AnimationManager;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.client.events.engine.ControllEngine;
import yesman.epicfight.client.world.capabilites.entitypatch.player.LocalPlayerPatch;
import yesman.epicfight.skill.*;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.entity.eventlistener.BasicAttackEvent;
import yesman.epicfight.world.entity.eventlistener.ComboCounterHandleEvent;
import yesman.epicfight.world.entity.eventlistener.PlayerEventListener;
import yesman.epicfight.world.entity.eventlistener.SkillConsumeEvent;

import java.util.UUID;

/**
 * This class replaces the basic attack motions when created.
 */
public class FlareBlitz extends BasicAttack
{
	private static final UUID EVENT_UUID = UUID.fromString("bb4af80f-603a-4b52-a92d-1d4a444749af");
	public static SkillBuilder<FlareBlitz> createImperatriceAttackSet()
	{

		return (new SkillBuilder<FlareBlitz>()).setCategory(SkillCategories.BASIC_ATTACK).setActivateType(ActivateType.ONE_SHOT).setResource(Resource.NONE);
	}

	public FlareBlitz(SkillBuilder<? extends BasicAttack> builder)
	{
		super(builder);
	}

	@Override
	public void onInitiate(SkillContainer container)
	{
		super.onInitiate(container);
		container.getExecutor().getEventListener().addEventListener(PlayerEventListener.EventType.SKILL_EXECUTE_EVENT, EVENT_UUID, event ->
		{
			if (event.getSkillContainer().getSkill().getCategory() == SkillCategories.BASIC_ATTACK && container.getDataManager().getDataValue(DatakeyRegistry.HIT.get()) && container.getExecutor().getStamina() >= 2f && !event.getPlayerPatch().getEntityState().attacking())
			{
				container.getExecutor().consumeForSkill(this, Resource.STAMINA, 2f);
				event.setStateExecutable(true);
			}
		});

		container.getExecutor().getEventListener().addEventListener(PlayerEventListener.EventType.ATTACK_ANIMATION_END_EVENT, EVENT_UUID, event ->
		{
			container.getDataManager().setDataSync(DatakeyRegistry.PREV_ANIM.get(), -1, container.getServerExecutor().getOriginal());
			container.getDataManager().setDataSync(DatakeyRegistry.HIT.get(), false, container.getServerExecutor().getOriginal());

		});

		container.getExecutor().getEventListener().addEventListener(PlayerEventListener.EventType.DEALT_DAMAGE_EVENT_ATTACK, EVENT_UUID, event ->
		{
			if (container.getExecutor().getHoldingItemCapability(InteractionHand.MAIN_HAND).getStyle(container.getExecutor()) == LumiereStyles.IMPERATRICE_SWORD)
			{
				container.getDataManager().setDataSync(DatakeyRegistry.PREV_ANIM.get(), event.getDamageSource().getAnimation().id(), container.getServerExecutor().getOriginal());
				container.getDataManager().setDataSync(DatakeyRegistry.HIT.get(), true, container.getServerExecutor().getOriginal());
			}
		});
	}

	@Override
	public void onRemoved(SkillContainer container)
	{
		container.getExecutor().getEventListener().removeListener(PlayerEventListener.EventType.SKILL_EXECUTE_EVENT, EVENT_UUID);
		container.getExecutor().getEventListener().removeListener(PlayerEventListener.EventType.ATTACK_ANIMATION_END_EVENT, EVENT_UUID);
		container.getExecutor().getEventListener().removeListener(PlayerEventListener.EventType.DEALT_DAMAGE_EVENT_HURT, EVENT_UUID);
		super.onRemoved(container);
	}

	public static void setComboCounterWithEvent(ComboCounterHandleEvent.Causal reason, ServerPlayerPatch playerpatch, SkillContainer container, AnimationManager.AnimationAccessor<? extends StaticAnimation> causalAnimation, int value)
	{
		int prevValue = container.getDataManager().getDataValue(DatakeyRegistry.BLAZE_COMBO.get());
		ComboCounterHandleEvent comboResetEvent = new ComboCounterHandleEvent(reason, playerpatch, causalAnimation, prevValue, value);
		container.getExecutor().getEventListener().triggerEvents(PlayerEventListener.EventType.COMBO_COUNTER_HANDLE_EVENT, comboResetEvent);
		container.getDataManager().setData(DatakeyRegistry.BLAZE_COMBO.get(), comboResetEvent.getNextValue());
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public FriendlyByteBuf gatherArguments(SkillContainer container, ControllEngine controllEngine)
	{
		return ArgumentGatherers.UniversalDirectionalInput((LocalPlayerPatch) container.getExecutor(), null);
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public Object getExecutionPacket(SkillContainer container, FriendlyByteBuf args)
	{
		return ArgumentGatherers.DirectionalExecutionPacket((LocalPlayerPatch) container.getExecutor(), args, this);
	}

	@Override
	public void executeOnServer(SkillContainer container, FriendlyByteBuf args)
	{
		if (container.getExecutor().getHoldingItemCapability(InteractionHand.MAIN_HAND).getStyle(container.getServerExecutor()).equals(LumiereStyles.IMPERATRICE_SWORD) || container.getServerExecutor().getHoldingItemCapability(InteractionHand.MAIN_HAND).getStyle(container.getServerExecutor()).equals(LumiereStyles.FORIXAIM_SWORD))
		{

			SkillConsumeEvent event = new SkillConsumeEvent(container.getExecutor(), this, this.resource);
			container.getExecutor().getEventListener().triggerEvents(PlayerEventListener.EventType.SKILL_CONSUME_EVENT, event);

			if (!event.isCanceled())
			{
				event.getResourceType().consumer.consume(container, (ServerPlayerPatch) container.getExecutor(), event.getAmount());
			}

			if (container.getExecutor().getEventListener().triggerEvents(PlayerEventListener.EventType.BASIC_ATTACK_EVENT, new BasicAttackEvent(container.getServerExecutor())))
			{
				return;
			}

			CapabilityItem cap = container.getExecutor().getHoldingItemCapability(InteractionHand.MAIN_HAND);
			AnimationManager.AnimationAccessor<? extends StaticAnimation> attackMotion = null;
			ServerPlayer player = (ServerPlayer) container.getExecutor().getOriginal();
			SkillDataManager dataManager = container.getDataManager();
			int comboCounter = dataManager.getDataValue(DatakeyRegistry.BLAZE_COMBO.get());

			int prevAnim = container.getDataManager().getDataValue(DatakeyRegistry.PREV_ANIM.get());
            if (player.isPassenger())
			{
				Entity entity = player.getVehicle();

				if ((entity instanceof PlayerRideableJumping ridable && ridable.canJump()) && cap.availableOnHorse() && cap.getMountAttackMotion() != null)
				{
					comboCounter %= cap.getMountAttackMotion().size();
					attackMotion = cap.getMountAttackMotion().get(comboCounter);
					comboCounter++;
				}
			} else
			{
				int fw = args.readInt();
				int sw = args.readInt();
				int ud = args.readInt();

				if (ud == -1)
				{
					attackMotion = LumiereSwordAnims.IMPERATRICE_SWORD_CROUCH_ATTACK;
				}
				else if (fw == 1)
				{
					if (prevAnim == LumiereSwordAnims.IMPERATRICE_SWORD_RIGHT_ATTACK.id() || prevAnim == LumiereSwordAnims.IMPERATRICE_SWORD_NEUTRAL_ATTACK.id() || prevAnim == LumiereSwordAnims.IMPERATRICE_SWORD_BACK_ATTACK_ALT.id() || prevAnim == LumiereSwordAnims.IMPERATRICE_SWORD_FRONT_ATTACK.id() || prevAnim == LumiereSwordAnims.IMPERATRICE_SWORD_CROUCH_ATTACK.id())
						attackMotion = LumiereSwordAnims.IMPERATRICE_SWORD_FRONT_ATTACK_ALT;
					else
						attackMotion = LumiereSwordAnims.IMPERATRICE_SWORD_FRONT_ATTACK;
				}
				else if (fw == -1)
				{
					if (prevAnim == LumiereSwordAnims.IMPERATRICE_SWORD_RIGHT_ATTACK.id() || prevAnim == LumiereSwordAnims.IMPERATRICE_SWORD_NEUTRAL_ATTACK.id() || prevAnim == LumiereSwordAnims.IMPERATRICE_SWORD_BACK_ATTACK_ALT.id() || prevAnim == LumiereSwordAnims.IMPERATRICE_SWORD_FRONT_ATTACK.id() || prevAnim == LumiereSwordAnims.IMPERATRICE_SWORD_CROUCH_ATTACK.id())
						attackMotion = LumiereSwordAnims.IMPERATRICE_SWORD_BACK_ATTACK;
					else
						attackMotion = LumiereSwordAnims.IMPERATRICE_SWORD_BACK_ATTACK_ALT;
				}
				else if (sw == 1)
				{
					attackMotion = LumiereSwordAnims.IMPERATRICE_SWORD_LEFT_ATTACK;
				}
				else if (sw == -1)
				{
					attackMotion = LumiereSwordAnims.IMPERATRICE_SWORD_RIGHT_ATTACK;
				}
				else
				{
					if (prevAnim == LumiereSwordAnims.IMPERATRICE_SWORD_RIGHT_ATTACK.id() || prevAnim == LumiereSwordAnims.IMPERATRICE_SWORD_NEUTRAL_ATTACK_ALT.id() || prevAnim == LumiereSwordAnims.IMPERATRICE_SWORD_NEUTRAL_ATTACK.id() || prevAnim == LumiereSwordAnims.IMPERATRICE_SWORD_BACK_ATTACK_ALT.id() || prevAnim == LumiereSwordAnims.IMPERATRICE_SWORD_FRONT_ATTACK.id() || prevAnim == LumiereSwordAnims.IMPERATRICE_SWORD_CROUCH_ATTACK.id())
						attackMotion = LumiereSwordAnims.IMPERATRICE_SWORD_NEUTRAL_ATTACK_ALT;
					else
						attackMotion = LumiereSwordAnims.IMPERATRICE_SWORD_NEUTRAL_ATTACK;
				}
            }

			setComboCounterWithEvent(ComboCounterHandleEvent.Causal.TIME_EXPIRED, container.getServerExecutor(), container, attackMotion, comboCounter);

			if (attackMotion != null && (prevAnim != attackMotion.id() || prevAnim == LumiereSwordAnims.IMPERATRICE_SWORD_NEUTRAL_ATTACK_ALT.id()))
			{
				float startupReduction = container.getDataManager().getDataValue(DatakeyRegistry.HIT.get()) ? startupReduction(attackMotion) : 0.0f;
				if (prevAnim == LumiereSwordAnims.IMPERATRICE_SWORD_RIGHT_ATTACK.id())
					startupReduction -= 0f;
				else if (prevAnim == LumiereSwordAnims.IMPERATRICE_SWORD_BACK_ATTACK_ALT.id())
					startupReduction -= 0f;
				if (container.getExecutor().getSkill(BattleArtsSkillSlots.BATTLE_STYLE).getDataManager().hasData(DatakeyRegistry.JUMPING.get()) && container.getExecutor().getSkill(BattleArtsSkillSlots.BATTLE_STYLE).getDataManager().getDataValue(DatakeyRegistry.JUMPING.get()))
					container.getExecutor().playAnimationSynchronized(LumiereSwordAnims.IMPERATRICE_SWORD_SUNRISE, 0);
				else
					container.getExecutor().playAnimationSynchronized(attackMotion, startupReduction);

				container.getDataManager().setDataSync(DatakeyRegistry.HIT.get(), false, container.getServerExecutor().getOriginal());


			}
			container.getExecutor().updateEntityState();
		}
		else
		{
			super.executeOnServer(container, args);
		}

	}

	private float startupReduction(AnimationManager.AnimationAccessor<? extends StaticAnimation> anim)
	{
		if (anim.id() == LumiereSwordAnims.IMPERATRICE_SWORD_LEFT_ATTACK.id())
		{
			return -0f;
		}
		if (anim.id() == LumiereSwordAnims.IMPERATRICE_SWORD_NEUTRAL_ATTACK.id())
		{
			return -0f;
		}
		if (anim.id() == LumiereSwordAnims.IMPERATRICE_SWORD_RIGHT_ATTACK.id())
		{
			return -0f;
		}
		if (anim.id() == LumiereSwordAnims.IMPERATRICE_SWORD_BACK_ATTACK.id())
		{
			return -0f;
		}
		return 0.0f;
	}
}
