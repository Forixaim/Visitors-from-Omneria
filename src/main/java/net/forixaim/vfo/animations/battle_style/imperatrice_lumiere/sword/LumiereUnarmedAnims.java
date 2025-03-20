package net.forixaim.vfo.animations.battle_style.imperatrice_lumiere.sword;

import net.forixaim.bs_api.battle_arts_skills.BattleArtsSkillSlots;
import net.forixaim.vfo.VisitorsOfOmneria;
import net.forixaim.vfo.animations.types.DebuggableSelectiveAnimation;
import net.forixaim.vfo.animations.types.GuardTransitionAnimation;
import net.forixaim.vfo.animations.types.JumpAnimation;
import net.forixaim.vfo.registry.SoundRegistry;
import net.forixaim.vfo.skill.DatakeyRegistry;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import yesman.epicfight.api.animation.AnimationManager;
import yesman.epicfight.api.animation.property.AnimationEvent;
import yesman.epicfight.api.animation.property.AnimationProperty;
import yesman.epicfight.api.animation.types.*;
import yesman.epicfight.client.world.capabilites.entitypatch.player.LocalPlayerPatch;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.gameasset.Armatures;
import yesman.epicfight.gameasset.EpicFightSounds;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.damagesource.StunType;

import java.util.function.Function;

/**
 * Module containing living motions.
 */
public class LumiereUnarmedAnims
{
	public static AnimationManager.AnimationAccessor<StaticAnimation> IMPERATRICE_IDLE;
	public static AnimationManager.AnimationAccessor<MovementAnimation> IMPERATRICE_WALK;

	public static void reg(AnimationManager.AnimationBuilder event)
	{
		IMPERATRICE_IDLE = event.nextAccessor("battle_style/legendary/imperatrice_lumiere/idle", access ->
				new StaticAnimation(0.3f, true, access, Armatures.BIPED));

		IMPERATRICE_WALK = event.nextAccessor("battle_style/legendary/imperatrice_lumiere/walk", access ->
				new MovementAnimation(0.1f, true, access, Armatures.BIPED));
	}
}
