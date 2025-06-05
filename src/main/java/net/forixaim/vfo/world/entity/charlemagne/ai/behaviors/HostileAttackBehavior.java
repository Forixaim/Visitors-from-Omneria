package net.forixaim.vfo.world.entity.charlemagne.ai.behaviors;

import net.forixaim.vfo.events.advanced_bosses.DamageDealtEvent;
import net.forixaim.vfo.world.entity.charlemagne.Charlemagne;
import net.forixaim.vfo.world.entity.charlemagne.ai.CharlemagneMode;
import net.forixaim.vfo.world.entity.patches.CharlemagnePatch;
import net.forixaim.vfo.world.entity.charlemagne.ai.CharlemagneBrain;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import yesman.epicfight.api.utils.AttackResult;
import yesman.epicfight.api.utils.math.MathUtils;
import yesman.epicfight.api.utils.math.OpenMatrix4f;
import yesman.epicfight.api.utils.math.Vec3f;

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
	private boolean hyperChase = false;
	private float dist = 4;
	
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
			return;
		}
		if (shouldCloseIn)
		{
			opLastPosition = copyPosition(opponent.position());
		}
		shouldCloseIn = distanceToPoint(opponent, opLastPosition) > 5 && mob.getNavigation().isInProgress();

		if (mob.distanceTo(opponent) <= dist && hyperChase)
		{
			hyperChase = false;
		}
	}

	private void handleResponse(LivingEntity opponent)
	{
		if (shouldCloseIn)
		{
			closeIn(opponent, dist, dist * 2);
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
		mob.setDeltaMovement(distanceTo(opponent).normalize().scale(1));
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
			//Rush up and grab
			onStopEncirclement(true);
		}
	}

	private void backOff(LivingEntity entity)
	{
		mobPatch.rotateTo(entity, 360, true);
		Vec3 forwardHorizontal = Vec3.directionFromRotation(new Vec2(0.0F, -mob.getYHeadRot()));
		Vec3 jumpDir = OpenMatrix4f.transform(OpenMatrix4f.createRotatorDeg(0.0F, Vec3f.Y_AXIS), forwardHorizontal.scale(Objects.requireNonNull(mob.getAttribute(Attributes.MOVEMENT_SPEED)).getValue() * 0.6));
		mob.setDeltaMovement(jumpDir.x, mob.getDeltaMovement().y, jumpDir.z);
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

	}

	@Override
	public AttackResult handleDamageTaken(DamageSource source, float amount)
	{
		return AttackResult.missed(amount);
	}
}
