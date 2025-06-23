package net.forixaim.omneria.world.entity.patches;

import net.forixaim.omneria.world.entity.types.AbstractFriendlyNPC;
import yesman.epicfight.api.animation.Animator;
import yesman.epicfight.api.animation.LivingMotions;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.world.capabilities.entitypatch.Faction;
import yesman.epicfight.world.capabilities.entitypatch.HumanoidMobPatch;
import yesman.epicfight.world.capabilities.entitypatch.mob.ZombiePatch;

public class FriendlyHumanoidNPCPatch<T extends AbstractFriendlyNPC> extends HumanoidMobPatch<T>
{

	public FriendlyHumanoidNPCPatch(Faction faction)
	{
		super(faction);
	}

	@Override
	public void initAnimator(Animator animator)
	{
		animator.addLivingAnimation(LivingMotions.IDLE, Animations.BIPED_IDLE);
	}

	@Override
	public void updateMotion(boolean b)
	{
		super.commonMobUpdateMotion(b);
	}
}
