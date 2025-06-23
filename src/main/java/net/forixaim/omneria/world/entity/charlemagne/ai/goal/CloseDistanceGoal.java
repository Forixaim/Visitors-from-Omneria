package net.forixaim.omneria.world.entity.charlemagne.ai.goal;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;

public class CloseDistanceGoal extends TargetGoal
{
	public CloseDistanceGoal(Mob pMob, boolean pMustSee)
	{
		super(pMob, pMustSee);
	}

	@Override
	public boolean canUse()
	{
		return false;
	}
}
