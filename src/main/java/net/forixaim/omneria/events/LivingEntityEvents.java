package net.forixaim.omneria.events;

import net.forixaim.battle_arts_api.battle_arts_skills.BattleArtsSkillSlots;
import net.forixaim.omneria.VisitorsOfOmneria;
import net.forixaim.omneria.events.advanced_bosses.DamageDealtEvent;
import net.forixaim.omneria.skill.battle_style.OmneriaBattleStyle;
import net.forixaim.omneria.skill.battle_style.properties.FlyingEnabled;
import net.forixaim.omneria.world.entity.patches.CharlemagnePatch;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.GameType;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import yesman.epicfight.api.animation.AnimationManager;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.client.world.capabilites.entitypatch.player.LocalPlayerPatch;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.projectile.ProjectilePatch;
import yesman.epicfight.world.damagesource.EpicFightDamageSource;
import yesman.epicfight.world.damagesource.EpicFightDamageTypeTags;
import yesman.epicfight.world.damagesource.EpicFightDamageTypeTags;

@Mod.EventBusSubscriber(modid = VisitorsOfOmneria.MOD_ID)
public class LivingEntityEvents
{
	@SubscribeEvent
	public static void onPlayerGamemodeChange(PlayerEvent.PlayerChangeGameModeEvent event)
	{
		if (event.getNewGameMode() == GameType.SURVIVAL || event.getNewGameMode() == GameType.ADVENTURE)
		{
			PlayerPatch<?> playerPatch = EpicFightCapabilities.getEntityPatch(event.getEntity(), PlayerPatch.class);
			if (playerPatch.getSkill(BattleArtsSkillSlots.BATTLE_STYLE).getSkill() instanceof FlyingEnabled)
			{
				event.getEntity().getAbilities().mayfly = true;
			}
		}
	}

	@SubscribeEvent
	public static void onJump(LivingEvent.LivingJumpEvent event)
	{
		LivingEntity entity = event.getEntity();
		if (EpicFightCapabilities.getEntityPatch(entity, LivingEntityPatch.class) instanceof PlayerPatch<?> livingEntityPatch && livingEntityPatch.isEpicFightMode())
		{
			if (livingEntityPatch.getSkill(BattleArtsSkillSlots.BATTLE_STYLE).getSkill() instanceof OmneriaBattleStyle obs)
			{
				AnimationManager.AnimationAccessor<? extends StaticAnimation> jump = obs.getJump(livingEntityPatch.getSkill(BattleArtsSkillSlots.BATTLE_STYLE));
				if (jump != null && entity.onGround())
				{
					entity.setDeltaMovement(entity.getDeltaMovement().subtract(0, entity.getDeltaMovement().y, 0));
					livingEntityPatch.playAnimationSynchronized(jump, 0);
				}
			}
		}
	}


    @SubscribeEvent
    public static void onDeath(LivingDeathEvent event)
    {
        event.getEntity().sendSystemMessage(event.getSource().getLocalizedDeathMessage(event.getEntity()));
    }

	@SubscribeEvent
	public static void onLivingEntityHurt(LivingHurtEvent event)
	{
		EpicFightDamageSource epicFightDamageSource = null;
		Entity trueSource = event.getSource().getEntity();

		if (trueSource != null)
		{
			LivingEntityPatch<?> attackerEntityPatch = EpicFightCapabilities.getEntityPatch(trueSource, LivingEntityPatch.class);

			if (event.getSource() instanceof EpicFightDamageSource instance)
			{
				epicFightDamageSource = instance;
			} else if (attackerEntityPatch != null)
			{
				epicFightDamageSource = attackerEntityPatch.getEpicFightDamageSource();
			}

			if (epicFightDamageSource != null && !epicFightDamageSource.is(EpicFightDamageTypeTags.GUARD_PUNCTURE))
			{
				LivingEntity hitEntity = event.getEntity();

				if (attackerEntityPatch instanceof CharlemagnePatch charlemagnePatch)
				{
					DamageDealtEvent DamageDealtEvent = new DamageDealtEvent(charlemagnePatch, hitEntity, epicFightDamageSource, event);
					charlemagnePatch.fireDamageDealtEvent(DamageDealtEvent);
				}
			}
		}
	}
}
