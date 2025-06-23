package net.forixaim.omneria.events.advanced_bosses;

import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.damagesource.EpicFightDamageSource;

public class DamageDealtEvent
{
	private final LivingEntityPatch<?> AttackingNPC;
	private final LivingEntity HurtEntity;
	private final EpicFightDamageSource DamageSource;
	private final LivingHurtEvent Event;

	public DamageDealtEvent(LivingEntityPatch<?> pAttackingNPC, LivingEntity pHurtEntity, EpicFightDamageSource pDamageSource, LivingHurtEvent event)
	{
		this.AttackingNPC = pAttackingNPC;
		this.HurtEntity = pHurtEntity;
		this.Event = event;
		this.DamageSource = pDamageSource;
	}

	public LivingEntity getHurtEntity()
	{
		return HurtEntity;
	}

	public EpicFightDamageSource getDamageSource()
	{
		return DamageSource;
	}

	public LivingHurtEvent getEvent()
	{
		return Event;
	}

	public LivingEntityPatch<?> getAttackingNPC()
	{
		return AttackingNPC;
	}
}
