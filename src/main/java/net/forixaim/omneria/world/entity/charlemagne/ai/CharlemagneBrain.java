package net.forixaim.omneria.world.entity.charlemagne.ai;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.logging.LogUtils;
import net.forixaim.omneria.events.advanced_bosses.DamageDealtEvent;
import net.forixaim.omneria.world.entity.charlemagne.Charlemagne;
import net.forixaim.omneria.world.entity.patches.CharlemagnePatch;
import net.forixaim.omneria.world.entity.charlemagne.ai.behaviors.BaseBehavior;
import net.forixaim.omneria.world.entity.charlemagne.ai.behaviors.HostileAttackBehavior;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.phys.AABB;
import yesman.epicfight.api.animation.AnimationManager;
import yesman.epicfight.api.animation.types.AttackAnimation;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.utils.AttackResult;

import java.util.List;
import java.util.Map;


/**
 * The core class that defines most of Charlemagne's AI
 */
public class CharlemagneBrain
{
	private final Charlemagne target;
	public CharlemagnePatch patch;
	public CharlemagneMode mode;
	private LivingEntity nearestMonster;
	private LivingEntity opponent;
	private final Map<Emotion, AnimationManager.AnimationAccessor<? extends StaticAnimation>> emotionState = Maps.newHashMap();
	private Emotion state;
	private final Map<CharlemagneMode, BaseBehavior> handlers = Maps.newHashMap();

	//Behaviors
	public HostileAttackBehavior hostileAttackBehavior;

	//Universal Flags
	private boolean blocking = false;

	private final List<CharlemagneAttackString> charlemagneAttackStrings = Lists.newArrayList(
	);

	private final Map<AttackAnimation, Float> StaminaDamageMap = Maps.newHashMap();

	private int tick = 0;
	private int seconds = 0;

    public void toggleBlock()
	{
		this.blocking = !this.blocking;
	}

	public boolean isBlocking()
	{
		return  this.blocking;
	}

	public CharlemagneBrain(final Charlemagne target, final CharlemagnePatch patch)
	{
		this.target = target;
		this.mode = CharlemagneMode.FRIENDLY;
		this.patch = patch;
		this.state = null;
		hostileAttackBehavior = new HostileAttackBehavior(patch, this, target);
		this.handlers.put(CharlemagneMode.DEFENSE, hostileAttackBehavior);
	}

	public AttackResult handleWhenAttacked(DamageSource damageSource, float amount)
	{
		if (mode.is(CharlemagneMode.DEFENSE)) {
			if (blocking)
			{
				return AttackResult.blocked(amount);
			}
			return AttackResult.missed(amount);
		}

		return AttackResult.of(patch.getEntityState().attackResult(damageSource), amount);
	}

	public CharlemagneMode getMode()
	{
		return mode;
	}

	public void onReceiveAttackConnection(DamageDealtEvent event)
	{
		//Handle
		if (mode.is(CharlemagneMode.DEFENSE))
			hostileAttackBehavior.handleAttackConnection(event);
	}

	public LivingEntity getOpponent() {
		return opponent;
	}

	public LivingEntity getMobOpponent() {
		return nearestMonster;
	}

	public void onAttackAnimationEnd()
	{
		for (CharlemagneAttackString charlemagneAttackString : charlemagneAttackStrings)
		{
			if (charlemagneAttackString.firing)
			{
				charlemagneAttackString.reset();
			}
		}
	}

	protected AABB getTargetSearchArea() {
		return this.target.getBoundingBox().inflate(80.0, 40.0, 80.0);
	}

	public Emotion getState()
	{
		return state;
	}

	public void receiveTickFire()
	{
		tick++;
		if (state == null)
		{
			changeEmotionState(Emotion.NEUTRAL);
		}
		if (mode.is(CharlemagneMode.DEFENSE))
			hostileAttackBehavior.onReceiveHostileAttack(nearestMonster);
		if (mode.is(CharlemagneMode.FRIENDLY))
			friendlyTick();
		if (mode.is(CharlemagneMode.DUELING))
			battleTick();
		if (tick >= 20)
		{
			tick = 0;
			seconds++;
			printDebugList();
		}
		nearestMonster = this.target.level().getNearestEntity(this.target.level().getEntitiesOfClass(Mob.class, this.getTargetSearchArea()), target.defConditions, this.target, this.target.getX(), this.target.getY(), this.target.getZ());
		if (nearestMonster != null && !mode.is(CharlemagneMode.DUELING))
		{
			this.mode = CharlemagneMode.DEFENSE;
		}
		if (seconds >= 60)
		{
			seconds = 0;
		}
	}


	public void changeEmotionState(Emotion newState)
	{
		this.state = newState;
	}

	private void printDebugList()
	{
		if (!target.level().isClientSide)
		{
			LogUtils.getLogger().debug("a second has passed;");
			LogUtils.getLogger().debug("Nearest monster: {}", nearestMonster);
			LogUtils.getLogger().debug("Current Mode: {}", mode);
            //Defense flags
			// LogUtils.getLogger().debug("Backing off: {}", backingOff);
			LogUtils.getLogger().debug("WalkSpeed: {}", target.walkAnimation.speed());
			LogUtils.getLogger().debug("Living Motion: {}", patch.currentLivingMotion);
			LogUtils.getLogger().debug("Emotional State: {}", state);
		}
	}

	public void debugFire()
	{
		this.charlemagneAttackStrings.get(0).fire(patch);
		this.charlemagneAttackStrings.get(0).position++;
	}

	private void battleTick()
	{

	}

	private void friendlyTick()
	{
		if (state != Emotion.NEUTRAL)
			changeEmotionState(Emotion.NEUTRAL);
	}
}
