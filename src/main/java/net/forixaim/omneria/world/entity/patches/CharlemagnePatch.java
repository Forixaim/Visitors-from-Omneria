package net.forixaim.omneria.world.entity.patches;

import com.google.common.collect.Lists;
import net.forixaim.omneria.events.advanced_bosses.DamageDealtEvent;
import net.forixaim.omneria.world.entity.charlemagne.Charlemagne;
import net.forixaim.omneria.world.entity.charlemagne.ai.CharlemagneAttackString;
import net.forixaim.omneria.world.entity.charlemagne.ai.CharlemagneBrain;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraftforge.event.entity.living.LivingEvent;
import yesman.epicfight.api.animation.Animator;
import yesman.epicfight.api.animation.LivingMotions;
import yesman.epicfight.api.utils.AttackResult;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.world.capabilities.entitypatch.Faction;
import yesman.epicfight.world.capabilities.entitypatch.Factions;

import java.util.List;
import java.util.UUID;

public class CharlemagnePatch extends FriendlyHumanoidNPCPatch<Charlemagne>
{

	private static final UUID CloseGapUUID = UUID.fromString("eb18c5eb-19bf-4a71-9398-b49d9b510217");
	private static final List<CharlemagneAttackString> bossAttackString = Lists.newArrayList();

	public CharlemagneBrain brain;

	public CharlemagnePatch()
	{
		super(Factions.NEUTRAL);
	}

	@Override
	public boolean isLastAttackSuccess()
	{
		return super.isLastAttackSuccess();
	}

	public void fireDamageDealtEvent(DamageDealtEvent event)
	{
		//Give all control to the internal brain class.
		brain.onReceiveAttackConnection(event);
	}

	public void fireAttackAnimEndEvent()
	{
		brain.onAttackAnimationEnd();
	}

	@Override
	public void onConstructed(Charlemagne entityIn)
	{
		entityIn.patch = this;
		super.onConstructed(entityIn);
		brain = new CharlemagneBrain(entityIn, this);
	}

	@Override
	public void initAnimator(Animator animator)
	{
		animator.addLivingAnimation(LivingMotions.IDLE, Animations.BIPED_IDLE);
		animator.addLivingAnimation(LivingMotions.WALK, Animations.BIPED_WALK);
		animator.addLivingAnimation(LivingMotions.RUN, Animations.BIPED_RUN);
	}

	@Override
	public void updateMotion(boolean b)
	{
		if (this.original.getHealth() <= 0.0F)
		{
			this.currentLivingMotion = LivingMotions.DEATH;
		}
		else if (this.state.inaction() && b)
		{
			this.currentLivingMotion = LivingMotions.INACTION;
		}
		else if (this.original.getVehicle() != null)
		{
			this.currentLivingMotion = LivingMotions.MOUNT;
		}
		else if (!(this.original.getDeltaMovement().y < -0.550000011920929) && !this.isAirborneState())
		{
			if (this.getOriginal().walkAnimation.speed() > 0.01f)
			{
				if (this.getOriginal().walkAnimation.speed() > 0.8f)
					this.currentLivingMotion = LivingMotions.RUN;
				else
					this.currentLivingMotion = LivingMotions.WALK;
			}
			else
			{
				this.currentLivingMotion = LivingMotions.IDLE;
			}
		}
		else
		{
			this.currentLivingMotion = LivingMotions.FALL;
		}

		this.currentCompositeMotion = this.currentLivingMotion;
	}

	@Override
	public void tick(LivingEvent.LivingTickEvent event)
	{
		super.tick(event);
		if (brain != null && !this.isLogicalClient())
			brain.receiveTickFire();
	}

	@Override
	public AttackResult tryHurt(DamageSource damageSource, float amount)
	{
		return brain.handleWhenAttacked(damageSource, amount);

	}
}
