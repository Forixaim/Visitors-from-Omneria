package net.forixaim.omneria.world.entity.charlemagne.ai.behaviors;

import net.forixaim.omneria.world.entity.charlemagne.Charlemagne;
import net.forixaim.omneria.world.entity.patches.CharlemagnePatch;
import net.forixaim.omneria.world.entity.charlemagne.ai.CharlemagneBrain;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import yesman.epicfight.api.utils.math.OpenMatrix4f;
import yesman.epicfight.api.utils.math.Vec3f;

import java.util.Objects;

/**
 * Arguably the most complex behavior in the game, Charlemagne's dueling behavior is a complex system of attacks and feints.
 */
public class CharlemagneDuelingBehavior extends BaseBehavior
{
	//Flags
	private boolean bEncirclement = false;
	private int timer;
	private int cooldown;
	private boolean lR;
	private final CharlemagneBrain brain;
	private final Charlemagne mob;
	private final CharlemagnePatch mobPatch;

	//Movement Flags
	public boolean hyperChase = false;
	public boolean chasing = false;

	public CharlemagneDuelingBehavior(final CharlemagnePatch patch, final CharlemagneBrain brain, final Charlemagne mob)
	{
		this.mob = mob;
		this.mobPatch = patch;
		this.brain = brain;
	}



	public void encircle(LivingEntity entity)
	{
		//Face perpendicular to the vector's direction and move in a circle around the opponent.

		//First rotate perpendicular to the vector's direction.
		double distanceToTarget = mob.distanceToSqr(entity.getX(), entity.getY(), entity.getZ());

		float angle = (float) (Objects.requireNonNull(mob.getAttribute(Attributes.MOVEMENT_SPEED)).getValue() * 8.100000381469727);

		if (Math.sqrt(distanceToTarget) < 10.0) {
			angle = -10.0F;
		} else if (Math.sqrt(distanceToTarget) > 11.0) {
			angle = 10.0F;
		}
		mobPatch.rotateTo(entity, 360, true);
		if (lR)
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
		Vec3 jumpDir = OpenMatrix4f.transform(OpenMatrix4f.createRotatorDeg(0.0F, Vec3f.Y_AXIS), forwardHorizontal.scale(Objects.requireNonNull(mob.getAttribute(Attributes.MOVEMENT_SPEED)).getValue() * 2.5));
		mob.setDeltaMovement(jumpDir.x, mob.getDeltaMovement().y, jumpDir.z);
	}

	public void onTick(Player opponent)
	{

	}

}
