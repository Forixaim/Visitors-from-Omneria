package net.forixaim.omneria.world.entity.charlemagne.ai.behaviors;

import net.forixaim.omneria.events.advanced_bosses.DamageDealtEvent;
import net.minecraft.world.damagesource.DamageSource;
import yesman.epicfight.api.utils.AttackResult;

/**
 * This class contains all the base event handlers and stuff.
 */
public abstract class BaseBehavior
{
	public void handleAttackConnection(DamageDealtEvent event)
	{
		//To be implemented by the child class, this method is only called to prevent null pointer exceptions.
		return;
	}
	public AttackResult handleDamageTaken(DamageSource source, float amount)
	{
		//To be implemented by the child class, this method is only called to prevent null pointer exceptions.
		//By default, return a success result.
		return AttackResult.success(amount);
	}
}
