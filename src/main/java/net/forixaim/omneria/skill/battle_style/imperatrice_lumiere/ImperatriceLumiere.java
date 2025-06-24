package net.forixaim.omneria.skill.battle_style.imperatrice_lumiere;


import com.brandon3055.draconicevolution.DraconicEvolution;
import com.brandon3055.draconicevolution.items.equipment.ModularChestpiece;
import com.mojang.blaze3d.vertex.PoseStack;
import mekanism.common.Mekanism;
import mekanism.common.item.gear.ItemMekaSuitArmor;
import moze_intel.projecte.api.ProjectEAPI;
import moze_intel.projecte.gameObjs.items.armor.DMArmor;
import moze_intel.projecte.gameObjs.items.armor.GemArmorBase;
import moze_intel.projecte.gameObjs.items.armor.RMArmor;
import net.forixaim.bs_api.battle_arts_skills.BattleArtsSkillSlots;
import net.forixaim.bs_api.battle_arts_skills.battle_style.BattleStyle;
import net.forixaim.omneria.Config;
import net.forixaim.omneria.animations.battle_style.imperatrice_lumiere.sword.LumiereSwordAnims;
import net.forixaim.omneria.animations.battle_style.imperatrice_lumiere.sword.LumiereUnarmedAnims;
import net.forixaim.omneria.capabilities.styles.LumiereStyles;
import net.forixaim.omneria.registry.ItemRegistry;
import net.forixaim.omneria.registry.SoundRegistry;
import net.forixaim.omneria.skill.DatakeyRegistry;
import net.forixaim.omneria.skill.OmneriaSkills;
import net.forixaim.omneria.skill.battle_style.OmneriaBattleStyle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.ModList;
import yesman.epicfight.api.animation.LivingMotions;
import yesman.epicfight.client.gui.BattleModeGui;
import yesman.epicfight.network.EpicFightNetworkManager;
import yesman.epicfight.network.server.SPChangeSkill;
import yesman.epicfight.skill.ChargeableSkill;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.SkillSlots;
import yesman.epicfight.skill.guard.GuardSkill;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.entity.eventlistener.DealtDamageEvent;
import yesman.epicfight.world.entity.eventlistener.PlayerEventListener;

import java.util.UUID;

public class ImperatriceLumiere extends OmneriaBattleStyle
{
	private static final UUID EVENT_UUID = UUID.fromString("fceabee5-64fc-40dd-a7a2-4470ed8ff00a");

	@Override
	public void onInitiate(SkillContainer container)
	{
		container.getExecutor().getEventListener().addEventListener(PlayerEventListener.EventType.MOVEMENT_INPUT_EVENT, EVENT_UUID, event -> {
            if (Minecraft.getInstance().options.keyJump.isDown() && event.getPlayerPatch().isEpicFightMode() && event.getPlayerPatch().getHoldingItemCapability(InteractionHand.MAIN_HAND).getStyle(event.getPlayerPatch()) == LumiereStyles.IMPERATRICE_SWORD)
			{
				if (event.getPlayerPatch().getOriginal().onGround() && !container.getExecutor().getOriginal().getAbilities().flying && !container.getDataManager().getDataValue(DatakeyRegistry.JUMPING.get()))
				{
					container.getDataManager().setDataSync(DatakeyRegistry.JUMPING.get(), true, event.getPlayerPatch().getOriginal());
					if (container.getExecutor().getOriginal().isSprinting())
						container.getExecutor().playAnimationSynchronized(LumiereSwordAnims.IMPERATRICE_SWORD_JUMP_FORWARD, 0);
					else
						container.getExecutor().playAnimationSynchronized(LumiereSwordAnims.IMPERATRICE_SWORD_JUMP, 0);
					event.getMovementInput().jumping = false;
				}
			}
		});

		container.getExecutor().getEventListener().addEventListener(PlayerEventListener.EventType.SERVER_ITEM_STOP_EVENT, EVENT_UUID, event -> {
			if (container.getExecutor().isEpicFightMode() && container.getExecutor().getHoldingItemCapability(InteractionHand.MAIN_HAND).getUseAnimation(event.getPlayerPatch()) == UseAnim.BLOCK && event.getPlayerPatch().getHoldingItemCapability(InteractionHand.MAIN_HAND).getStyle(event.getPlayerPatch()) == LumiereStyles.IMPERATRICE_SWORD)
			{
				container.getExecutor().playAnimationSynchronized(LumiereSwordAnims.IMPERATRICE_SWORD_GUARD_OUT, 0);
			}
		});

		container.getExecutor().getEventListener().addEventListener(PlayerEventListener.EventType.HURT_EVENT_PRE, EVENT_UUID, event -> {
			if (container.getDataManager().getDataValue(DatakeyRegistry.JUMPING.get()))
			{
				container.getDataManager().setDataSync(DatakeyRegistry.JUMPING.get(), false, event.getPlayerPatch().getOriginal());
			}
		});

		container.getExecutor().getEventListener().addEventListener(PlayerEventListener.EventType.DEALT_DAMAGE_EVENT_ATTACK, EVENT_UUID, event ->
		{
			if (event.getPlayerPatch().getOriginal().getItemInHand(InteractionHand.MAIN_HAND).is(ItemRegistry.ORIGIN_JOYEUSE.get()))
			{
				event.getDamageSource().addRuntimeTag(DamageTypeTags.BYPASSES_INVULNERABILITY);
				event.getDamageSource().addRuntimeTag(DamageTypeTags.BYPASSES_ENCHANTMENTS);
			}
			if (Config.triggerAntiCheese)
			{
				boolean cheeseFound = false;
				if (event.getTarget() instanceof Player player && !player.isCreative())
				{
					cheeseFound = isCheeseFound(event, cheeseFound);
					if (cheeseFound)
					{
						EpicFightCapabilities.getEntityPatch(player, PlayerPatch.class).playSound(SoundEvents.ITEM_BREAK, 1f, 0, 0);
						EpicFightCapabilities.getEntityPatch(player, PlayerPatch.class).playSound(SoundRegistry.CHEESE.get(), 0, 0);
						EpicFightCapabilities.getEntityPatch(player, PlayerPatch.class).playSound(SoundRegistry.IMPERATRICE_ANTI_CHEESE.get(), 0, 0);
					}
				}
				else if (!(event.getTarget() instanceof Player))
				{
					cheeseFound = isCheeseFound(event, cheeseFound);
					if (cheeseFound)
					{
						EpicFightCapabilities.getEntityPatch(event.getTarget(), LivingEntityPatch.class).playSound(SoundEvents.ITEM_BREAK, 1f, 0, 0);
						EpicFightCapabilities.getEntityPatch(event.getTarget(), LivingEntityPatch.class).playSound(SoundRegistry.CHEESE.get(), 0, 0);
						EpicFightCapabilities.getEntityPatch(event.getTarget(), LivingEntityPatch.class).playSound(SoundRegistry.IMPERATRICE_ANTI_CHEESE.get(), 0, 0);
					}
				}
			}
		});

		container.getExecutor().getEventListener().addEventListener(PlayerEventListener.EventType.DEALT_DAMAGE_EVENT_HURT, EVENT_UUID, event ->
		{
			if (event.getDamageSource().getAnimation() == LumiereSwordAnims.IMPERATRICE_SWORD_FLARIAN_IMPALER)
			{
				event.getPlayerPatch().getOriginal().level().explode(event.getTarget(), event.getTarget().getX(), event.getTarget().getY(), event.getTarget().getZ(), 1.0f, Level.ExplosionInteraction.NONE);
			}

			if (event.getDamageSource().getAnimation() == LumiereSwordAnims.IMPERATRICE_SWORD_FLARESPIN)
			{
				event.getPlayerPatch().getOriginal().setDeltaMovement(0, 2, 0);
			}
		});
		super.onInitiate(container);
	}

	@Override
	public boolean shouldDraw(SkillContainer container)
	{
		return true;
	}

	@Override
	public void drawOnGui(BattleModeGui gui, SkillContainer container, GuiGraphics guiGraphics, float x, float y)
	{
		PoseStack poseStack = guiGraphics.pose();
		poseStack.pushPose();
		poseStack.translate(0, (float)gui.getSlidingProgression(), 0);
		guiGraphics.blit(getSkillTexture(), (int)x, (int)y, 24, 24, 0, 0, 1, 1, 1, 1);
		if (!container.getDataManager().hasData(DatakeyRegistry.TRUE_COMBO_COUNT.get()))
		{
			Integer Heat = container.getDataManager().getDataValue(DatakeyRegistry.TRUE_COMBO_COUNT.get());
			String Heat_Level = String.format("%s", Heat);
			guiGraphics.drawString(gui.getFont(), Heat_Level, x + 4, y + 16, 16777215, true);
		}
		guiGraphics.drawString(gui.getFont(), Integer.toString(container.getStack()), x + 8, y+8, 16777215, true);
		poseStack.popPose();
	}

	@Override
	public boolean canExecute(SkillContainer container)
	{
		return super.canExecute(container);
	}

	@Override
	public void executeOnServer(SkillContainer container, FriendlyByteBuf args)
	{
		if (!container.getServerExecutor().getSkill(SkillSlots.BASIC_ATTACK).hasSkill(OmneriaSkills.FLARE_BLITZ))
		{
			container.getServerExecutor().getSkillCapability().skillContainers[SkillSlots.BASIC_ATTACK.universalOrdinal()].setSkill(OmneriaSkills.FLARE_BLITZ);
			EpicFightNetworkManager.sendToPlayer(new SPChangeSkill(SkillSlots.BASIC_ATTACK, OmneriaSkills.FLARE_BLITZ.toString(), SPChangeSkill.State.ENABLE), container.getServerExecutor().getOriginal());
		}
		if (!container.getServerExecutor().getSkill(BattleArtsSkillSlots.COMBAT_ART).hasSkill(OmneriaSkills.FIRE_ARTS))
		{
			container.getServerExecutor().getSkill(BattleArtsSkillSlots.COMBAT_ART).setSkill(OmneriaSkills.FIRE_ARTS);
			EpicFightNetworkManager.sendToPlayer(new SPChangeSkill(BattleArtsSkillSlots.COMBAT_ART, OmneriaSkills.FIRE_ARTS.toString(), SPChangeSkill.State.ENABLE), container.getServerExecutor().getOriginal());
		}
	}

	private boolean isCheeseFound(DealtDamageEvent.Attack event, boolean cheeseFound)
	{
		for (ItemStack item : event.getTarget().getArmorSlots())
		{
			if (ModList.get().isLoaded(Mekanism.MODID) && item.getItem() instanceof ItemMekaSuitArmor)
			{
				//Trigger Anti-Invincibility Cheese
				item.copyAndClear();
				cheeseFound = true;
			}
			if (ModList.get().isLoaded(DraconicEvolution.MODID) && item.getItem() instanceof ModularChestpiece)
			{
				//Trigger Anti-Invincibility Cheese
				item.copyAndClear();
				cheeseFound = true;
			}
			if (ModList.get().isLoaded(ProjectEAPI.PROJECTE_MODID) && (item.getItem() instanceof DMArmor || item.getItem() instanceof RMArmor || item.getItem() instanceof GemArmorBase))
			{
				//Trigger Anti-Invincibility Cheese
				item.copyAndClear();
				cheeseFound = true;
			}
		}
		return cheeseFound;
	}

	@Override
	public void onRemoved(SkillContainer container)
	{
		super.onRemoved(container);
		container.getExecutor().getEventListener().removeListener(PlayerEventListener.EventType.HURT_EVENT_PRE, EVENT_UUID);
		container.getExecutor().getEventListener().removeListener(PlayerEventListener.EventType.SERVER_ITEM_STOP_EVENT, EVENT_UUID);
		container.getExecutor().getEventListener().removeListener(PlayerEventListener.EventType.MOVEMENT_INPUT_EVENT, EVENT_UUID);
		container.getExecutor().getEventListener().removeListener(PlayerEventListener.EventType.ACTION_EVENT_SERVER, EVENT_UUID);
		container.getExecutor().getEventListener().removeListener(PlayerEventListener.EventType.BASIC_ATTACK_EVENT, EVENT_UUID);
		container.getExecutor().getEventListener().removeListener(PlayerEventListener.EventType.DEALT_DAMAGE_EVENT_ATTACK, EVENT_UUID);
		container.getExecutor().getEventListener().removeListener(PlayerEventListener.EventType.DEALT_DAMAGE_EVENT_HURT, EVENT_UUID);
		container.getExecutor().getEventListener().removeListener(PlayerEventListener.EventType.ACTION_EVENT_SERVER, EVENT_UUID);
	}

	@Override
	public void updateContainer(SkillContainer container)
	{
		if (!container.getExecutor().isLogicalClient())
		{
			if (!container.getExecutor().getOriginal().onGround() && !container.getDataManager().getDataValue(DatakeyRegistry.LEFT_GROUND.get()))
			{
				container.getDataManager().setDataSync(DatakeyRegistry.LEFT_GROUND.get(), true, container.getServerExecutor().getOriginal());
			}
			else if (container.getExecutor().getOriginal().onGround() && container.getDataManager().getDataValue(DatakeyRegistry.LEFT_GROUND.get()))
			{
				container.getDataManager().setDataSync(DatakeyRegistry.LEFT_GROUND.get(), false, container.getServerExecutor().getOriginal());
			}


		}

		super.updateContainer(container);
	}

	public ImperatriceLumiere(Builder<? extends Skill> builder)
	{
		super(builder);
		modifiesAttacks = true;
		jumpBoostPower = 6f;
		criticalHitChance = 1;
		criticalHitDamage = 0.8f;
		immuneDamages.add(DamageTypes.LAVA);
		immuneDamages.add(DamageTypes.FIREBALL);
		immuneDamages.add(DamageTypes.IN_FIRE);
		immuneDamages.add(DamageTypes.ON_FIRE);
		immuneDamages.add(DamageTypes.HOT_FLOOR);
		immuneDamages.add(DamageTypes.FALL);
		immuneDamages.add(DamageTypes.UNATTRIBUTED_FIREBALL);
		unarmedLivingMotions.put(LivingMotions.IDLE, LumiereUnarmedAnims.IMPERATRICE_IDLE);
		unarmedLivingMotions.put(LivingMotions.WALK, LumiereUnarmedAnims.IMPERATRICE_WALK);
	}

	@Override
	public boolean unarmedMoveset()
	{
		return true;
	}
}
