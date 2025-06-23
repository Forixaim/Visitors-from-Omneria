package net.forixaim.omneria.animations.battle_style.imperatrice_lumiere.sword;

import yesman.epicfight.api.animation.AnimationManager;
import yesman.epicfight.api.animation.types.*;
import yesman.epicfight.gameasset.Armatures;

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
