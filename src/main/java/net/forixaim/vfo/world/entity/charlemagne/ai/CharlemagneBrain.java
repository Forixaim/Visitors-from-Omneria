package net.forixaim.vfo.world.entity.charlemagne.ai;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.logging.LogUtils;
import net.forixaim.vfo.events.advanced_bosses.DamageDealtEvent;
import net.forixaim.vfo.world.entity.charlemagne.Charlemagne;
import net.forixaim.vfo.world.entity.charlemagne.CharlemagneMode;
import net.forixaim.vfo.world.entity.charlemagne.CharlemagnePatch;
import net.forixaim.vfo.world.entity.charlemagne.ai.behaviors.BaseBehavior;
import net.forixaim.vfo.world.entity.charlemagne.ai.behaviors.HostileAttackBehavior;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
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

	private final List<CharlemagneAttackString> charlemagneAttackStrings = Lists.newArrayList(
	);

	private final Map<AttackAnimation, Float> StaminaDamageMap = Maps.newHashMap();

	private int tick = 0;
	private int seconds = 0;

	//Defense flags
	private boolean backingOff = false;

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
		if (mode.is(CharlemagneMode.DEFENSE))
			return AttackResult.missed(amount);

		return AttackResult.of(patch.getEntityState().attackResult(damageSource), amount);
	}

	public CharlemagneMode getMode()
	{
		return mode;
	}

	public void onReceiveAttackConnection(DamageDealtEvent event)
	{
		//Handle
		handlers.get(mode).handleAttackConnection(event);
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
		return this.target.getBoundingBox().inflate(80.0, 4.0, 80.0);
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
			//Check for any enemy mobs nearby unless in defense mode;
			nearestMonster = this.target.level().getNearestEntity(this.target.level().getEntitiesOfClass(ArmorStand.class, this.getTargetSearchArea()), target.DefCond, this.target, this.target.getX(), this.target.getY(), this.target.getZ());
			if (nearestMonster != null && !mode.is(CharlemagneMode.DUELING))
			{
				this.mode = CharlemagneMode.DEFENSE;
			}
			printDebugList();
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
			LogUtils.getLogger().debug("Backing off: {}", backingOff);
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
