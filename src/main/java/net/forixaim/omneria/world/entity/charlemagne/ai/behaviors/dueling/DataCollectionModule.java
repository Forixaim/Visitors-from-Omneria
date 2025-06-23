package net.forixaim.omneria.world.entity.charlemagne.ai.behaviors.dueling;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

/**
 * This module collects data from the opponent player and sends data to the Logic Module
 */
public class DataCollectionModule
{
	private LivingEntity livingEntity;

	private Vec3 position1, position2;
	private boolean position = true;

	public void setLivingEntity(LivingEntity livingEntity)
	{
		this.livingEntity = livingEntity;
	}

	public Vec3 getVelocity()
	{
		if (position1 == null || position2 == null)
		{
			return null;
		}
		return position2.subtract(position1);
	}

	private double pythagoranTheorem(double a, double b)
	{
		return Math.sqrt(Math.pow(a, 2) * Math.pow(b, 2));
	}

	public double getVelocityMagnitude()
	{
		return pythagoranTheorem(getVelocity().x(), pythagoranTheorem(getVelocity().y(), getVelocity().z()));
	}

	public void update()
	{
		if (position)
		{
			position1 = livingEntity.position();
		}
		else
		{
			position2 = livingEntity.position();
		}
		position = !position;
	}
}
