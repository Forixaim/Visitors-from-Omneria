package net.forixaim.vfo.world.entity.charlemagne.ai;

import com.google.common.collect.Lists;
import net.forixaim.vfo.world.entity.charlemagne.CharlemagnePatch;
import yesman.epicfight.api.animation.AnimationManager;
import yesman.epicfight.api.animation.types.AttackAnimation;
import yesman.epicfight.api.animation.types.StaticAnimation;

import java.util.List;

public class CharlemagneAttack
{
	private final AnimationManager.AnimationAccessor<? extends AttackAnimation> attackAnimation;

	//Primarily for multi-hit attacks
	private List<AttackAnimation.Phase> attacks = Lists.newArrayList();

	/**
	 * Range: The magnitude of the vector generated when putting the end of a h
	 */
	private final float range;
	private boolean feint;

	private CharlemagneAttack(AnimationManager.AnimationAccessor<? extends AttackAnimation> attackAnimation, float range)
	{
		this.attackAnimation = attackAnimation;
		this.attacks.addAll(List.of(attackAnimation.get().phases));
		this.range = range;
	}

	public float getRange()
	{
		return range;
	}

	public static CharlemagneAttack createAttack(AnimationManager.AnimationAccessor<? extends AttackAnimation> animation, float range)
	{
		return new CharlemagneAttack(animation, range);
	}

	public float getSuspectConfidence()
	{
		return range;
	}

	public void Fire(CharlemagnePatch attacker)
	{
		attacker.playAnimationSynchronized(attackAnimation, 0);
	}

	public void Feint(CharlemagnePatch attacker)
	{
		attacker.playAnimationSynchronized(attackAnimation, 2);
	}
}
