package net.forixaim.omneria.world.entity.charlemagne.ai.behaviors;

import com.google.common.collect.Lists;
import com.mojang.logging.LogUtils;
import net.forixaim.omneria.animations.battle_style.imperatrice_lumiere.sword.LumiereSwordAnims;
import net.forixaim.omneria.events.advanced_bosses.DamageDealtEvent;
import net.forixaim.omneria.world.entity.charlemagne.Charlemagne;
import net.forixaim.omneria.world.entity.charlemagne.ai.CharlemagneMode;
import net.forixaim.omneria.world.entity.patches.CharlemagnePatch;
import net.forixaim.omneria.world.entity.charlemagne.ai.CharlemagneBrain;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import yesman.epicfight.api.animation.AnimationManager;
import yesman.epicfight.api.animation.types.AttackAnimation;
import yesman.epicfight.api.utils.AttackResult;
import yesman.epicfight.api.utils.math.MathUtils;
import yesman.epicfight.api.utils.math.OpenMatrix4f;
import yesman.epicfight.api.utils.math.Vec3f;
import yesman.epicfight.data.conditions.entity.TargetInEyeHeight;

import java.util.List;
import java.util.Objects;

public class HostileAttackBehavior extends BaseBehavior
{
	//Timers (MC Runs at 20 TPS)
	private int encirclementTimer;
	private int cooldown;

	//Opponent Watches
	Vec3 opLastPosition;

	//Movement Flags
	private boolean bEncirclement = false;
	private boolean encirclementDirectionLR = true;
	private boolean shouldCloseIn = false;
	private boolean tooClose = false;
	private boolean above = false;
	private float dist = 3;
	private TargetInEyeHeight predicate = new TargetInEyeHeight();
	private boolean comboing = false;
	private int ticksSinceLastHit = 0;
	private int combo = 0;
	public boolean hyperDash = false;

	List<AnimationManager.AnimationAccessor<? extends AttackAnimation>> BASE_MOB_COMBO = Lists.newArrayList(
			LumiereSwordAnims.IMPERATRICE_SWORD_NEUTRAL_ATTACK,
			LumiereSwordAnims.IMPERATRICE_SWORD_NEUTRAL_ATTACK_ALT,
			LumiereSwordAnims.IMPERATRICE_SWORD_BACK_ATTACK,
			LumiereSwordAnims.IMPERATRICE_SWORD_FRONT_ATTACK
	);

	
	//Important Values
	private final CharlemagneBrain brain;
	private final Charlemagne mob;
	private final CharlemagnePatch mobPatch;
	private Vec3 targetPoint;

	public boolean chasing = false;

	public boolean interruptedCircle = false;



	public HostileAttackBehavior(final CharlemagnePatch patch, final CharlemagneBrain brain, final Charlemagne mob)
	{
		this.mob = mob;
		this.mobPatch = patch;
		this.brain = brain;
	}
	//Logic
	private void handleLogic(LivingEntity opponent)
	{
		if (opLastPosition == null)
		{
			shouldCloseIn = true;
		}
		if (!mobPatch.getEntityState().attacking())
		{
			ticksSinceLastHit++;
		}
		if (ticksSinceLastHit >= 30)
		{
			comboing = false;
			combo = 0;
		}
		if (shouldCloseIn)
		{
			opLastPosition = copyPosition(opponent.position());
		}
		above = distanceTo(opponent).y() > 2;
		shouldCloseIn = mob.distanceTo(opponent) > dist;
		tooClose = mob.distanceTo(opponent) < dist - 1;
	}



	private boolean withinEyeHeight(LivingEntity target)
	{
		double veticalDistance = Math.abs(mob.getY() - target.getY());
		return veticalDistance < (double)mob.getEyeHeight() - 0.2;
	}

	private void handleResponse(LivingEntity opponent)
	{
		if (comboing && mobPatch.getEntityState().canBasicAttack() && opponent.isAlive() && mob.distanceTo(opponent) <= dist && withinEyeHeight(opponent))
		{
			if (!opponent.isAlive())
			{
				comboing = false;
				combo = 0;
			}
			if (combo >= BASE_MOB_COMBO.size())
			{
				comboing = false;
			}
			attack(opponent);
		}
		if (above && lateralDistance(opponent) > 10 && mobPatch.getEntityState().canBasicAttack() && !hyperDash)
		{
			hyperDash = true;
			mobPatch.rotateTo(opponent, 90, true);
			mobPatch.playAnimationSynchronized(LumiereSwordAnims.IMPERATRICE_SWORD_FLAREDASH_CHARLEMAGNE, 0);
		}
		if (shouldCloseIn && mobPatch.getEntityState().canBasicAttack())
		{
			closeIn(opponent, dist, dist * 2);
		}
		if (tooClose && mobPatch.getEntityState().canBasicAttack())
		{
			brain.toggleBlock();
			backOff(opponent);
		}
		else if (brain.isBlocking())
		{
			brain.toggleBlock();
		}
		if (!shouldCloseIn && !tooClose && mobPatch.getEntityState().canBasicAttack() && withinEyeHeight(opponent))
		{
			combo = 0;
			attack(opponent);
		}
	}

	private double lateralDistance(LivingEntity opponent)
	{
		Vec3 lateralOpponent = new Vec3(opponent.getX(), mob.getY(), opponent.getZ());
		return mob.distanceToSqr(lateralOpponent);
	}

	private void attack(LivingEntity opponent)
	{
		try {
			AnimationManager.AnimationAccessor<? extends AttackAnimation> attack = BASE_MOB_COMBO.get(combo);
			combo++;
			if (attack != null)
			{
				mobPatch.rotateTo(opponent, 360, true);
				mobPatch.playAnimationSynchronized(attack, 0);
			}
		}
		catch (final Exception e) {
			LogUtils.getLogger().warn("bruh");
		}
	}

	private Vec3 copyPosition(Vec3 toCopy)
	{
		return new Vec3(toCopy.toVector3f());
	}

	private float distanceToPoint(LivingEntity entity, Vec3 targetPoint)
	{
		return (float) Math.sqrt(entity.distanceToSqr(targetPoint));
	}

	//Movement Patterns
	private void closeIn(LivingEntity opponent, float distance, float fastChaseThreshold)
	{
		mobPatch.rotateTo(opponent, 90f, true);
		final Vec3 lateralMovement = new Vec3(distanceTo(opponent).x(), 0, distanceTo(opponent).z()).normalize().scale(0.4);
		final Vec3 finalMovement = new Vec3(lateralMovement.x(), mob.getDeltaMovement().y(), lateralMovement.z());

		mob.setDeltaMovement(finalMovement);
	}

	private Vec3 distanceTo(LivingEntity opponent)
	{
		return opponent.position().subtract(mob.position());
	}

	private void encircle(Vec3 pos, LivingEntity entity)
	{
		double distanceToTarget = mob.distanceToSqr(pos.x(), pos.y(), pos.z());
		float angle = (float) (Objects.requireNonNull(mob.getAttribute(Attributes.MOVEMENT_SPEED)).getValue() * 8.100000381469727 * 2);
		if (Math.sqrt(distanceToTarget) < 4) {
			angle = -2.0F;
		} else if (Math.sqrt(distanceToTarget) > 5) {
			angle = 2.0F;
		}
		mobPatch.rotateTo((float) MathUtils.getYRotOfVector(pos.subtract(mob.position())), 360, true);
		if (encirclementDirectionLR)
		{
			mobPatch.rotateTo(mobPatch.getYRot() - 90.0F + angle, 360.0F, true);
		}
		else
		{
			mobPatch.rotateTo(mobPatch.getYRot() + 90.0F - angle, 360.0F, true);
		}
		//Move forward
		mob.getLookControl().setLookAt(entity);
		Vec3 forwardHorizontal = Vec3.directionFromRotation(new Vec2(0.0F, mob.getYHeadRot()));
		Vec3 jumpDir = OpenMatrix4f.transform(OpenMatrix4f.createRotatorDeg(0.0F, Vec3f.Y_AXIS), forwardHorizontal.scale(Objects.requireNonNull(mob.getAttribute(Attributes.MOVEMENT_SPEED)).getValue() * 0.6));
		mob.setDeltaMovement(jumpDir.x, mob.getDeltaMovement().y, jumpDir.z);
		encirclementTimer--;
		if (Math.sqrt(entity.distanceToSqr(pos)) > Math.sqrt(distanceToTarget))
		{
			onStopEncirclement(true);
		}
	}

	private void backOff(LivingEntity entity)
	{
		mobPatch.rotateTo(entity, 90f, true);
		final Vec3 movementVector = new Vec3(distanceTo(entity).x(), 0, distanceTo(entity).z());

		mob.setDeltaMovement(movementVector.normalize().scale(-0.15));
	}


	//Events
	private void onStopEncirclement(Boolean interrupted)
	{
		bEncirclement = false;
		encirclementTimer = 0;
		interruptedCircle = interrupted;
	}

	private void startEncirclement(LivingEntity opponent)
	{
		//Ticks
		encirclementTimer = 200;
		bEncirclement = true;
		cooldown = 100;
		targetPoint = new Vec3(opponent.position().toVector3f());
		encirclementDirectionLR = mob.getRandom().nextBoolean();
	}

	//Actions
	private void resetAll()
	{
		chasing = false;
		bEncirclement = false;
		opLastPosition = null;
		encirclementTimer = 0;
		cooldown = 0;
	}
	/**
	 * This function is called every tick
	 * @param opponent the opponent to fight.
	 */
	public void onReceiveHostileAttack(LivingEntity opponent)
	{
		//Primary Ticking Function
		if (opponent == null || !opponent.isAlive())
		{
			brain.mode = CharlemagneMode.FRIENDLY;
			resetAll();
			return;
		}
		handleLogic(opponent);
		handleResponse(opponent);
	}

	@Override
	public void handleAttackConnection(DamageDealtEvent event)
	{
		comboing = true;
		ticksSinceLastHit = 0;
	}

	@Override
	public AttackResult handleDamageTaken(DamageSource source, float amount)
	{
		return AttackResult.missed(amount);
	}
}
