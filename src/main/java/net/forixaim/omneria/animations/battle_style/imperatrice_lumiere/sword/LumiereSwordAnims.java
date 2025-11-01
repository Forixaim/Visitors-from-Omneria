package net.forixaim.omneria.animations.battle_style.imperatrice_lumiere.sword;

import com.mojang.logging.LogUtils;
import net.forixaim.battle_arts_api.battle_arts_skills.BattleArtsSkillSlots;
import net.forixaim.omneria.animations.types.*;
import net.forixaim.omneria.colliders.LumiereColliders;
import net.forixaim.omneria.registry.SoundRegistry;
import net.forixaim.omneria.skill.DatakeyRegistry;
import net.forixaim.omneria.world.entity.patches.CharlemagnePatch;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import yesman.epicfight.api.animation.AnimationManager;
import yesman.epicfight.api.animation.property.AnimationEvent;
import yesman.epicfight.api.animation.property.AnimationProperty;
import yesman.epicfight.api.animation.property.MoveCoordFunctions;
import yesman.epicfight.api.animation.types.*;
import yesman.epicfight.api.utils.TimePairList;
import yesman.epicfight.api.utils.math.ValueModifier;
import yesman.epicfight.api.utils.math.Vec3f;
import yesman.epicfight.client.world.capabilites.entitypatch.player.LocalPlayerPatch;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.gameasset.Armatures;
import yesman.epicfight.gameasset.ColliderPreset;
import yesman.epicfight.gameasset.EpicFightSounds;
import yesman.epicfight.particle.EpicFightParticles;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.damagesource.StunType;

import java.util.List;
import java.util.function.Function;

/**
 * Module containing living motions.
 */
public class LumiereSwordAnims
{
	public static AnimationManager.AnimationAccessor<SelectiveAnimation> IMPERATRICE_SWORD_IDLE_SET;
	public static AnimationManager.AnimationAccessor<StaticAnimation> IMPERATRICE_SWORD_IDLE;
	public static AnimationManager.AnimationAccessor<DodgeAnimation> IMPERATRICE_SWORD_TRAILBLAZE_LEFT;
	public static AnimationManager.AnimationAccessor<DodgeAnimation> IMPERATRICE_SWORD_TRAILBLAZE_RIGHT;
	public static AnimationManager.AnimationAccessor<DodgeAnimation> IMPERATRICE_SWORD_TRAILBLAZE_FORWARD;
	public static AnimationManager.AnimationAccessor<DodgeAnimation> IMPERATRICE_SWORD_TRAILBLAZE_BACKWARD;
	public static AnimationManager.AnimationAccessor<DodgeAnimation> IMPERATRICE_SWORD_TRAILBLAZE_VANISH;
	public static AnimationManager.AnimationAccessor<DodgeAnimation> IMPERATRICE_SWORD_TRAILBLAZE_UP;


	public static AnimationManager.AnimationAccessor<StaticAnimation> IMPERATRICE_SWORD_FALL_NEUTRAL;
	public static AnimationManager.AnimationAccessor<StaticAnimation> IMPERATRICE_SWORD_FALL_FORWARD;
	public static AnimationManager.AnimationAccessor<StaticAnimation> IMPERATRICE_SWORD_FALL_FORWARD_SPRINT;
	public static AnimationManager.AnimationAccessor<GuardTransitionAnimation> IMPERATRICE_SWORD_GUARD_TRANSITION;
	public static AnimationManager.AnimationAccessor<StaticAnimation> IMPERATRICE_SWORD_GUARD;
	public static AnimationManager.AnimationAccessor<StaticAnimation> IMPERATRICE_SWORD_GUARD_OUT;
	public static AnimationManager.AnimationAccessor<GuardAnimation> IMPERATRICE_SWORD_GUARD_HIT;

	public static AnimationManager.AnimationAccessor<StaticAnimation> IMPERATRICE_SWORD_FLY_IDLE;

	public static AnimationManager.AnimationAccessor<SelectiveAnimation> IMPERATRICE_SWORD_WALK_SET;
	public static AnimationManager.AnimationAccessor<MovementAnimation> IMPERATRICE_SWORD_WALK;
	public static AnimationManager.AnimationAccessor<MovementAnimation> IMPERATRICE_SWORD_WALK_BACK;

	public static AnimationManager.AnimationAccessor<SelectiveAnimation> IMPERATRICE_SWORD_RUN_SET;
	public static AnimationManager.AnimationAccessor<MovementAnimation> IMPERATRICE_SWORD_RUN;

	public static AnimationManager.AnimationAccessor<SelectiveAnimation> IMPERATRICE_SWORD_FAST_RUN_SET;
	public static AnimationManager.AnimationAccessor<MovementAnimation> IMPERATRICE_SWORD_FULL_SPRINT;

	public static AnimationManager.AnimationAccessor<SelectiveAnimation> IMPERATRICE_SWORD_FLY;
	public static AnimationManager.AnimationAccessor<MovementAnimation> IMPERATRICE_SWORD_FLY_FORWARD;
	public static AnimationManager.AnimationAccessor<MovementAnimation> IMPERATRICE_SWORD_FLY_BACK;
	public static AnimationManager.AnimationAccessor<MovementAnimation> IMPERATRICE_SWORD_SUPERDASH;
	public static AnimationManager.AnimationAccessor<LongHitAnimation> IMPERATRICE_SWORD_LAND;

	public static AnimationManager.AnimationAccessor<SelectiveAnimation> IMPERATRICE_SWORD_JUMP;
	public static AnimationManager.AnimationAccessor<JumpAnimation> IMPERATRICE_SWORD_JUMP_NEUTRAL;
	public static AnimationManager.AnimationAccessor<JumpAnimation> IMPERATRICE_SWORD_JUMP_FORWARD;
	public static AnimationManager.AnimationAccessor<JumpAnimation> IMPERATRICE_SWORD_JUMP_BACK;
	public static AnimationManager.AnimationAccessor<OmneriaAttackAnimation> IMPERATRICE_SWORD_FLARIAN_IMPALER;
	public static AnimationManager.AnimationAccessor<ActionAnimation> IMPERATRICE_SWORD_HOMING_JUMP;


	public static AnimationManager.AnimationAccessor<GuardAnimation> IMPERATRICE_SWORD_PARRY_1;

	public static AnimationManager.AnimationAccessor<OmneriaAttackAnimation> IMPERATRICE_SWORD_FLARESPIN;
	public static AnimationManager.AnimationAccessor<OmneriaAttackAnimation> IMPERATRICE_SWORD_SOLAR_FLARE;
	public static AnimationManager.AnimationAccessor<OmneriaAttackAnimation> IMPERATRICE_SWORD_SOLAR_DRIVE;

	public static AnimationManager.AnimationAccessor<OmneriaAttackAnimation> IMPERATRICE_SWORD_NEUTRAL_ATTACK;
	public static AnimationManager.AnimationAccessor<OmneriaAttackAnimation> IMPERATRICE_SWORD_CROUCH_ATTACK;

	public static AnimationManager.AnimationAccessor<OmneriaAttackAnimation> IMPERATRICE_SWORD_NEUTRAL_ATTACK_ALT;
	public static AnimationManager.AnimationAccessor<OmneriaAttackAnimation> IMPERATRICE_SWORD_LEFT_ATTACK;
	public static AnimationManager.AnimationAccessor<OmneriaAttackAnimation> IMPERATRICE_SWORD_RIGHT_ATTACK;
	public static AnimationManager.AnimationAccessor<OmneriaAttackAnimation> IMPERATRICE_SWORD_BACK_ATTACK;
	public static AnimationManager.AnimationAccessor<OmneriaAttackAnimation> IMPERATRICE_SWORD_BACK_ATTACK_ALT;
	public static AnimationManager.AnimationAccessor<OmneriaAttackAnimation> IMPERATRICE_SWORD_FRONT_ATTACK;
	public static AnimationManager.AnimationAccessor<OmneriaAttackAnimation> IMPERATRICE_SWORD_FRONT_ATTACK_ALT;

	public static AnimationManager.AnimationAccessor<OmneriaAttackAnimation> IMPERATRICE_SWORD_FLAREDASH;
	public static AnimationManager.AnimationAccessor<OmneriaAttackAnimation> IMPERATRICE_SWORD_FLAREDASH_CHARLEMAGNE;


	public static AnimationManager.AnimationAccessor<OmneriaAerialAttackAnimation> IMPERATRICE_SWORD_SUNRISE;
	public static AnimationManager.AnimationAccessor<OmneriaAttackAnimation> IMPERATRICE_SWORD_BLAZING_SUNRISE;



	private static Function<LivingEntityPatch<?>, Integer> debugAnim = livingEntityPatch -> {
		if (livingEntityPatch instanceof PlayerPatch<?> playerPatch)
		{
			if (!playerPatch.getOriginal().onGround())
			{
				return 2;
			}
		}
		Vec3 view = livingEntityPatch.getOriginal().getViewVector(1.0F);
		Vec3 move = livingEntityPatch.getOriginal().getDeltaMovement();
		double dot = view.dot(move);
		return dot < (double)0.0F ? 1 : 0;
	};

	private static Function<LivingEntityPatch<?>, Integer> debug2Anim = livingEntityPatch ->
	{
		if (livingEntityPatch instanceof PlayerPatch<?> playerPatch)
		{
			if (!playerPatch.getOriginal().onGround())
				return 1;
		}
		return 0;
	};

	public static void reg(AnimationManager.AnimationBuilder event)
	{
		LumiereUnarmedAnims.reg(event);

		IMPERATRICE_SWORD_IDLE = event.nextAccessor("battle_style/legendary/imperatrice_lumiere/sword/idle", access ->
				new StaticAnimation(0.3f, true, access, Armatures.BIPED));

		IMPERATRICE_SWORD_TRAILBLAZE_LEFT = event.nextAccessor("battle_style/legendary/imperatrice_lumiere/sword/trailblaze_left", access ->
				new DodgeAnimation(0.0f, 0.15f, access, 0.4f, 1.4f, Armatures.BIPED)
						.addEvents(AnimationEvent.InTimeEvent.create(0.05f, Animations.ReusableSources.PLAY_SOUND, AnimationEvent.Side.CLIENT).params(SoundRegistry.IMPERATRICE_SPOT_DODGE.get()), AnimationEvent.InTimeEvent.create(
								0.05f, (livingEntityPatch, assetAccessor, animationParameters) ->
								{
									LivingEntity entity = livingEntityPatch.getOriginal();
									entity.level().addParticle(EpicFightParticles.WHITE_AFTERIMAGE.get(), entity.getX(), entity.getY(), entity.getZ(), Double.longBitsToDouble(entity.getId()), 0.0F, 0.0F);
									RandomSource random = entity.getRandom();
									double x = entity.getX() + (random.nextDouble() - random.nextDouble()) * (double)2.0F;
									double y = entity.getY();
									double z = entity.getZ() + (random.nextDouble() - random.nextDouble()) * (double)2.0F;
									entity.level().addParticle(ParticleTypes.EXPLOSION, x, y, z, random.nextDouble() * 0.005, 0.0F, 0.0F);
								}, AnimationEvent.Side.CLIENT
						)));

		IMPERATRICE_SWORD_TRAILBLAZE_UP = event.nextAccessor("battle_style/legendary/imperatrice_lumiere/sword/trailblaze_up", access ->
				new DodgeAnimation(0.0f, 0.15f, access, 0.4f, 1.4f, Armatures.BIPED)
						.addProperty(AnimationProperty.ActionAnimationProperty.MOVE_VERTICAL, true)
						.addProperty(AnimationProperty.ActionAnimationProperty.STOP_MOVEMENT, false)
						.addEvents(AnimationEvent.InTimeEvent.create(0.05f, Animations.ReusableSources.PLAY_SOUND, AnimationEvent.Side.CLIENT).params(SoundRegistry.IMPERATRICE_SPOT_DODGE.get()), AnimationEvent.InTimeEvent.create(
								0.05f, (livingEntityPatch, assetAccessor, animationParameters) ->
								{
									LivingEntity entity = livingEntityPatch.getOriginal();
									entity.level().addParticle(EpicFightParticles.WHITE_AFTERIMAGE.get(), entity.getX(), entity.getY(), entity.getZ(), Double.longBitsToDouble(entity.getId()), 0.0F, 0.0F);
									RandomSource random = entity.getRandom();
									double x = entity.getX() + (random.nextDouble() - random.nextDouble()) * (double)2.0F;
									double y = entity.getY();
									double z = entity.getZ() + (random.nextDouble() - random.nextDouble()) * (double)2.0F;
									entity.level().addParticle(ParticleTypes.EXPLOSION, x, y, z, random.nextDouble() * 0.005, 0.0F, 0.0F);
								}, AnimationEvent.Side.CLIENT
						)));

		IMPERATRICE_SWORD_TRAILBLAZE_RIGHT = event.nextAccessor("battle_style/legendary/imperatrice_lumiere/sword/trailblaze_right", access ->
				new DodgeAnimation(0.0f, 0.15f, access, 0.4f, 1.4f, Armatures.BIPED)
						.addEvents(AnimationEvent.InTimeEvent.create(0.05f, Animations.ReusableSources.PLAY_SOUND, AnimationEvent.Side.CLIENT).params(SoundRegistry.IMPERATRICE_SPOT_DODGE.get())
								, AnimationEvent.InTimeEvent.create(
										0.05f, (livingEntityPatch, assetAccessor, animationParameters) ->
										{
											LivingEntity entity = livingEntityPatch.getOriginal();
											entity.level().addParticle(EpicFightParticles.WHITE_AFTERIMAGE.get(), entity.getX(), entity.getY(), entity.getZ(), Double.longBitsToDouble(entity.getId()), 0.0F, 0.0F);
											RandomSource random = entity.getRandom();
											double x = entity.getX() + (random.nextDouble() - random.nextDouble()) * (double)2.0F;
											double y = entity.getY();
											double z = entity.getZ() + (random.nextDouble() - random.nextDouble()) * (double)2.0F;
											entity.level().addParticle(ParticleTypes.EXPLOSION, x, y, z, random.nextDouble() * 0.005, 0.0F, 0.0F);
										}, AnimationEvent.Side.CLIENT
								)));

		IMPERATRICE_SWORD_TRAILBLAZE_FORWARD = event.nextAccessor("battle_style/legendary/imperatrice_lumiere/sword/trailblaze_forward", access ->
				new DodgeAnimation(0.0f, 0.15f, access, 0.4f, 1.4f, Armatures.BIPED)
						.addEvents(AnimationEvent.InTimeEvent.create(0.05f, Animations.ReusableSources.PLAY_SOUND, AnimationEvent.Side.CLIENT).params(SoundRegistry.IMPERATRICE_SPOT_DODGE.get())
								, AnimationEvent.InTimeEvent.create(
										0.05f, (livingEntityPatch, assetAccessor, animationParameters) ->
										{
											LivingEntity entity = livingEntityPatch.getOriginal();
											entity.level().addParticle(EpicFightParticles.WHITE_AFTERIMAGE.get(), entity.getX(), entity.getY(), entity.getZ(), Double.longBitsToDouble(entity.getId()), 0.0F, 0.0F);
											RandomSource random = entity.getRandom();
											double x = entity.getX() + (random.nextDouble() - random.nextDouble()) * (double)2.0F;
											double y = entity.getY();
											double z = entity.getZ() + (random.nextDouble() - random.nextDouble()) * (double)2.0F;
											entity.level().addParticle(ParticleTypes.EXPLOSION, x, y, z, random.nextDouble() * 0.005, 0.0F, 0.0F);
										}, AnimationEvent.Side.CLIENT
								)));

		IMPERATRICE_SWORD_TRAILBLAZE_VANISH = event.nextAccessor("battle_style/legendary/imperatrice_lumiere/sword/trailblaze_vanish", access ->
				new DodgeAnimation(0.0f, 0.15f, access, 0.4f, 1.4f, Armatures.BIPED)
						.addEvents(AnimationEvent.InTimeEvent.create(0.05f, Animations.ReusableSources.PLAY_SOUND, AnimationEvent.Side.CLIENT).params(SoundRegistry.IMPERATRICE_SPOT_DODGE.get())
								, AnimationEvent.InTimeEvent.create(
										0.05f, (livingEntityPatch, assetAccessor, animationParameters) ->
										{
											LivingEntity entity = livingEntityPatch.getOriginal();
											entity.level().addParticle(EpicFightParticles.WHITE_AFTERIMAGE.get(), entity.getX(), entity.getY(), entity.getZ(), Double.longBitsToDouble(entity.getId()), 0.0F, 0.0F);
											RandomSource random = entity.getRandom();
											double x = entity.getX() + (random.nextDouble() - random.nextDouble()) * (double)2.0F;
											double y = entity.getY();
											double z = entity.getZ() + (random.nextDouble() - random.nextDouble()) * (double)2.0F;
											entity.level().addParticle(ParticleTypes.EXPLOSION, x, y, z, random.nextDouble() * 0.005, 0.0F, 0.0F);
										}, AnimationEvent.Side.CLIENT
								), AnimationEvent.InTimeEvent.create(0.15f, (livingEntityPatch, assetAccessor, animationParameters) ->
								{
									if (livingEntityPatch.getTarget() != null)
									{
										LivingEntity opponent = livingEntityPatch.getTarget();
										Vec3 opponentPos = opponent.position();
										Vec3 lookVec = opponent.getLookAngle().normalize();
										Vec3 behindPos = opponentPos.subtract(lookVec.scale(2f));
										livingEntityPatch.getOriginal().teleportTo((ServerLevel) opponent.level(), behindPos.x, opponentPos.y, behindPos.z, RelativeMovement.ALL, opponent.yHeadRot, livingEntityPatch.getOriginal().getViewXRot(1.0f));
										Vec3 toOpponent = opponentPos.subtract(behindPos);
										double yaw = Math.toDegrees(Math.atan2(-toOpponent.x, -toOpponent.z));
										double pitch = Math.toDegrees(-Math.atan2(toOpponent.y, Math.sqrt(toOpponent.x * toOpponent.x + toOpponent.z * toOpponent.z)));
										livingEntityPatch.getOriginal().setYRot((float)yaw);
										livingEntityPatch.getOriginal().setXRot((float)pitch);
										if (livingEntityPatch instanceof LocalPlayerPatch)
										{
											Minecraft mc = Minecraft.getInstance();
											if (mc.player != null) {
												mc.player.setYRot((float)yaw);
												mc.player.setXRot((float)pitch);
												mc.player.yRotO = (float)yaw;
												mc.player.xRotO = (float)pitch;
											}
										}
									}
								}, AnimationEvent.Side.SERVER)));


		IMPERATRICE_SWORD_TRAILBLAZE_BACKWARD = event.nextAccessor("battle_style/legendary/imperatrice_lumiere/sword/trailblaze_backward", access ->
				new DodgeAnimation(0.0f, 0.15f, access, 0.4f, 1.4f, Armatures.BIPED)
						.addEvents(AnimationEvent.InTimeEvent.create(0.05f, Animations.ReusableSources.PLAY_SOUND, AnimationEvent.Side.CLIENT).params(SoundRegistry.IMPERATRICE_SPOT_DODGE.get())
								, AnimationEvent.InTimeEvent.create(
										0.05f, (livingEntityPatch, assetAccessor, animationParameters) ->
										{
											LivingEntity entity = livingEntityPatch.getOriginal();
											entity.level().addParticle(EpicFightParticles.WHITE_AFTERIMAGE.get(), entity.getX(), entity.getY(), entity.getZ(), Double.longBitsToDouble(entity.getId()), 0.0F, 0.0F);
											RandomSource random = entity.getRandom();
											double x = entity.getX() + (random.nextDouble() - random.nextDouble()) * (double)2.0F;
											double y = entity.getY();
											double z = entity.getZ() + (random.nextDouble() - random.nextDouble()) * (double)2.0F;
											entity.level().addParticle(ParticleTypes.EXPLOSION, x, y, z, random.nextDouble() * 0.005, 0.0F, 0.0F);
										}, AnimationEvent.Side.CLIENT
								)));

		IMPERATRICE_SWORD_GUARD = event.nextAccessor("battle_style/legendary/imperatrice_lumiere/sword/guard", access ->
				new StaticAnimation(true, access, Armatures.BIPED));

		IMPERATRICE_SWORD_GUARD_OUT = event.nextAccessor("battle_style/legendary/imperatrice_lumiere/sword/guard_transition_out", access ->
				new StaticAnimation(0f, false, access, Armatures.BIPED)
						.addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (dynamicAnimation, livingEntityPatch, v, v1, v2) -> 2f)
						.addEvents(
								AnimationEvent.InTimeEvent.create(0.1f, Animations.ReusableSources.PLAY_SOUND, AnimationEvent.Side.CLIENT).params(EpicFightSounds.WHOOSH_ROD.get()),
								AnimationEvent.InTimeEvent.create(0.3f, Animations.ReusableSources.PLAY_SOUND, AnimationEvent.Side.CLIENT).params(EpicFightSounds.WHOOSH_ROD.get()),
								AnimationEvent.InTimeEvent.create(0.5f, Animations.ReusableSources.PLAY_SOUND, AnimationEvent.Side.CLIENT).params(EpicFightSounds.WHOOSH_ROD.get()),
								AnimationEvent.InTimeEvent.create(0.7f, Animations.ReusableSources.PLAY_SOUND, AnimationEvent.Side.CLIENT).params(EpicFightSounds.WHOOSH_ROD.get())
						));

		IMPERATRICE_SWORD_GUARD_TRANSITION = event.nextAccessor("battle_style/legendary/imperatrice_lumiere/sword/guard_transition", access ->
				new GuardTransitionAnimation(access, Armatures.BIPED, IMPERATRICE_SWORD_GUARD)
						.addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (dynamicAnimation, livingEntityPatch, v, v1, v2) -> 2f)
						.addEvents(
								AnimationEvent.InTimeEvent.create(0.1f, Animations.ReusableSources.PLAY_SOUND, AnimationEvent.Side.CLIENT).params(EpicFightSounds.WHOOSH_ROD.get()),
								AnimationEvent.InTimeEvent.create(0.3f, Animations.ReusableSources.PLAY_SOUND, AnimationEvent.Side.CLIENT).params(EpicFightSounds.WHOOSH_ROD.get()),
								AnimationEvent.InTimeEvent.create(0.5f, Animations.ReusableSources.PLAY_SOUND, AnimationEvent.Side.CLIENT).params(EpicFightSounds.WHOOSH_ROD.get()),
								AnimationEvent.InTimeEvent.create(0.7f, Animations.ReusableSources.PLAY_SOUND, AnimationEvent.Side.CLIENT).params(EpicFightSounds.WHOOSH_ROD.get())
						));

		IMPERATRICE_SWORD_GUARD_HIT = event.nextAccessor("battle_style/legendary/imperatrice_lumiere/sword/guard_hit", access ->
				new GuardAnimation(0f, 0.6f, access, Armatures.BIPED));

		IMPERATRICE_SWORD_LAND = event.nextAccessor("battle_style/legendary/imperatrice_lumiere/sword/land", access ->
				new LongHitAnimation(0.3f, access, Armatures.BIPED));

		IMPERATRICE_SWORD_FLARIAN_IMPALER = event.nextAccessor(
				"battle_style/legendary/imperatrice_lumiere/sword/flarian_impaler",
				access ->
						new OmneriaAttackAnimation(0.2f, 0.0f, 1.1f, 1.3f, 2f, LumiereColliders.IMPERATRICE_INFERNAL_IMPALE, Armatures.BIPED.get().rootJoint, access, Armatures.BIPED)
								.addProperty(BattleArtsAttackPhaseProperties.KNOCKBACK_ANGLE, 30.0)
								.addProperty(BattleArtsAttackPhaseProperties.KNOCKBACK_POWER, 2.0)
								.addEvents(AnimationEvent.InTimeEvent.create(1.2f, (livingEntityPatch, assetAccessor, animationParameters) ->
								{
								}, AnimationEvent.Side.BOTH))
								.addState(EntityState.CAN_SKILL_EXECUTION, false));

		IMPERATRICE_SWORD_BLAZING_SUNRISE = event.nextAccessor("battle_style/legendary/imperatrice_lumiere/sword/blazing_sunrise", access ->
				new OmneriaAttackAnimation(0.4f, 0.1f, 0.5f, 0.9f, 2f, ColliderPreset.BATTOJUTSU_DASH, Armatures.BIPED.get().rootJoint, access, Armatures.BIPED)
						.addProperty(AnimationProperty.AttackPhaseProperty.MAX_STRIKES_MODIFIER, ValueModifier.adder(10))
						.addProperty(BattleArtsAttackPhaseProperties.KNOCKBACK_ANGLE, 90.0)
						.addProperty(BattleArtsAttackPhaseProperties.KNOCKBACK_POWER, 1.0)
						.addProperty(AnimationProperty.ActionAnimationProperty.COORD_SET_BEGIN, MoveCoordFunctions.RAW_COORD_WITH_X_ROT)
						.addProperty(AnimationProperty.ActionAnimationProperty.COORD_SET_TICK, null)
						.addProperty(AnimationProperty.ActionAnimationProperty.MOVE_VERTICAL, true)
						.addProperty(AnimationProperty.ActionAnimationProperty.NO_GRAVITY_TIME, TimePairList.create(0.15F, 0.85F))
						.addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, Animations.ReusableSources.CONSTANT_ONE)
						.addProperty(AnimationProperty.StaticAnimationProperty.POSE_MODIFIER, Animations.ReusableSources.ROOT_X_MODIFIER)
						.addEvents(AnimationProperty.StaticAnimationProperty.ON_END_EVENTS, AnimationEvent.SimpleEvent.create(Animations.ReusableSources.RESTORE_BOUNDING_BOX, AnimationEvent.Side.BOTH))
						.addEvents(AnimationProperty.StaticAnimationProperty.TICK_EVENTS, AnimationEvent.SimpleEvent.create(Animations.ReusableSources.RESIZE_BOUNDING_BOX, AnimationEvent.Side.BOTH).params(EntityDimensions.scalable(0.6F, 1.0F)))
						.addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (dynamicAnimation, livingEntityPatch, speed, prevElapsedTime, elapsedTime) ->
						{
							if (elapsedTime > 0.6f)
								return 1.0f;
							else return 2.0f;
						})
						.addState(EntityState.CAN_SKILL_EXECUTION, false)
						.addState(EntityState.CAN_BASIC_ATTACK, false));


		IMPERATRICE_SWORD_FALL_NEUTRAL = event.nextAccessor("battle_style/legendary/imperatrice_lumiere/sword/fall_neutral", access ->
				new StaticAnimation(0.3f, true, access, Armatures.BIPED));
		IMPERATRICE_SWORD_FALL_FORWARD = event.nextAccessor("battle_style/legendary/imperatrice_lumiere/sword/fall_forward", access ->
				new StaticAnimation(0.3f, true, access, Armatures.BIPED));

		IMPERATRICE_SWORD_FALL_FORWARD_SPRINT = event.nextAccessor("battle_style/legendary/imperatrice_lumiere/sword/fall_forward_sprint", access ->
				new StaticAnimation(0.3f, true, access, Armatures.BIPED));

		IMPERATRICE_SWORD_IDLE_SET = event.nextAccessor("battle_style/legendary/imperatrice_lumiere/sword/idle_set", access ->
				new SelectiveAnimation(livingEntityPatch ->
				{
					if (livingEntityPatch instanceof PlayerPatch<?> playerPatch)
					{
						if (!playerPatch.getOriginal().onGround())
							return 1;
					}
					return 0;
				}, access, IMPERATRICE_SWORD_IDLE, IMPERATRICE_SWORD_FALL_NEUTRAL));

		IMPERATRICE_SWORD_RUN_SET = event.nextAccessor("battle_style/legendary/imperatrice_lumiere/sword/run_set", access ->
				new SelectiveAnimation(debug2Anim, access, IMPERATRICE_SWORD_RUN, IMPERATRICE_SWORD_FALL_FORWARD_SPRINT));


		IMPERATRICE_SWORD_SUNRISE = event.nextAccessor("battle_style/legendary/imperatrice_lumiere/sword/airslash", access ->
				new OmneriaAerialAttackAnimation(0.4f, 0.0f, 0.4f, 0.6f, 1.5f, false, null, Armatures.BIPED.get().toolR, access, Armatures.BIPED)
						.addProperty(BattleArtsAttackPhaseProperties.KNOCKBACK_ANGLE, 90.0)
						.addProperty(BattleArtsAttackPhaseProperties.KNOCKBACK_POWER, 1.3)
						.addProperty(AnimationProperty.AttackPhaseProperty.HIT_SOUND, SoundRegistry.IMPERATRICE_HIT_FINISHER.get()).addProperty(AnimationProperty.ActionAnimationProperty.STOP_MOVEMENT, false)
						.addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (dynamicAnimation, livingEntityPatch, speed, prevElapsedTime, elapsedTime) ->
						{
							if (elapsedTime > 0.6f)
								return 1.0f;
							else return 2.0f;
						})
						.addState(EntityState.CAN_SKILL_EXECUTION, false));

		IMPERATRICE_SWORD_HOMING_JUMP = event.nextAccessor("battle_style/legendary/imperatrice_lumiere/sword/superdash/homing_jump", access ->
				new ActionAnimation(0.2f, access, Armatures.BIPED)
						.addProperty(AnimationProperty.ActionAnimationProperty.MOVE_VERTICAL, true)
						.addProperty(AnimationProperty.ActionAnimationProperty.NO_GRAVITY_TIME, TimePairList.create(0.0f, 0.75f))
						.addState(EntityState.CAN_SKILL_EXECUTION, false)
						.addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (dynamicAnimation, livingEntityPatch, speed, prevElapsedTime, elapsedTime) ->
						{
							if (elapsedTime >= 0.2F && elapsedTime < 0.25F && (livingEntityPatch.getTarget() != null || livingEntityPatch instanceof CharlemagnePatch charlemagnePatch && charlemagnePatch.brain.getMobOpponent() != null)) {
								float dpx = (float) livingEntityPatch.getOriginal().getX();
								float dpy = (float) livingEntityPatch.getOriginal().getY();
								float dpz = (float) livingEntityPatch.getOriginal().getZ();

								for(BlockState block = livingEntityPatch.getOriginal().level().getBlockState(new BlockPos.MutableBlockPos(dpx, dpy, dpz)); (block.getBlock() instanceof BushBlock || block.isAir()) && !block.is(Blocks.VOID_AIR); block = livingEntityPatch.getOriginal().level().getBlockState(new BlockPos.MutableBlockPos(dpx, dpy, dpz))) {
									--dpy;
								}

								AABB box = AABB.ofSize(livingEntityPatch.getOriginal().getPosition(1.0F), 1.0F, 1.0F, 1.0F);
								List<Entity> list = (livingEntityPatch.getOriginal()).level().getEntities(livingEntityPatch.getOriginal(), box);
								float distanceToGround = (float)Math.max(Math.abs(livingEntityPatch.getOriginal().getY() - (double)dpy) - (double)1.0F, 0.0F);
								LivingEntity livingentity = livingEntityPatch.getOriginal();
								Vec3 direction;

								if (livingEntityPatch instanceof CharlemagnePatch charlemagnePatch)
								{
									direction = new Vec3(0, Math.abs(charlemagnePatch.getOriginal().getY() - charlemagnePatch.brain.getMobOpponent().getY()), 0);
									if (Math.abs(charlemagnePatch.getOriginal().getY() - charlemagnePatch.brain.getMobOpponent().getY()) > 0.5) {
										livingentity.move(MoverType.SELF, direction);
										return 0.025F;
									} else {
										return speed * 1.5f;
									}
								}
								else
								{
									direction = distanceTo(livingEntityPatch.getTarget(), livingEntityPatch).normalize().scale(4 - (2 * elapsedTime));
									if (Math.abs(livingEntityPatch.getOriginal().getY() - livingEntityPatch.getTarget().getY()) > 0.5) {
										livingentity.move(MoverType.SELF, direction);
										return 0.025F;
									} else {
										return speed * 1.5f;
									}
								}


							} else {
								return speed * 1.5f;
							}
						})
						.addEvents(AnimationEvent.InTimeEvent.create(0.35f, Animations.ReusableSources.PLAY_SOUND, AnimationEvent.Side.CLIENT).params(EpicFightSounds.ROCKET_JUMP.get())));


		IMPERATRICE_SWORD_FLAREDASH = event.nextAccessor("battle_style/legendary/imperatrice_lumiere/sword/superdash/flaredash", access ->
				new OmneriaAttackAnimation(0.2f, 0.6f, 0.75f, 0.85f, 1.9f, ColliderPreset.BATTOJUTSU_DASH, Armatures.BIPED.get().rootJoint, access, Armatures.BIPED)
						.addProperty(BattleArtsAttackPhaseProperties.KNOCKBACK_ANGLE, -40d)
						.addProperty(BattleArtsAttackPhaseProperties.KNOCKBACK_POWER, 1d)
						.addProperty(AnimationProperty.AttackAnimationProperty.FIXED_MOVE_DISTANCE, true)
						.addProperty(AnimationProperty.ActionAnimationProperty.MOVE_VERTICAL, true)
						.addProperty(AnimationProperty.ActionAnimationProperty.NO_GRAVITY_TIME, TimePairList.create(0.0f, 0.75f))
						.addState(EntityState.CAN_SKILL_EXECUTION, false)
						.addProperty(AnimationProperty.StaticAnimationProperty.POSE_MODIFIER, Animations.ReusableSources.ROOT_X_MODIFIER)
						.addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (dynamicAnimation, livingEntityPatch, speed, prevElapsedTime, elapsedTime) ->
						{
							if (elapsedTime < 0.7F)
							{
								Vec3 dir = new  Vec3(0.0D, 0.1D, 0.0D);
								livingEntityPatch.getOriginal().move(MoverType.SELF, dir);

							}
							if (elapsedTime >= 0.7F && elapsedTime < 0.75F && (livingEntityPatch.getTarget() != null)) {
								float dpx = (float) livingEntityPatch.getOriginal().getX();
								float dpy = (float) livingEntityPatch.getOriginal().getY();
								float dpz = (float) livingEntityPatch.getOriginal().getZ();



								for(BlockState block = livingEntityPatch.getOriginal().level().getBlockState(new BlockPos.MutableBlockPos(dpx, dpy, dpz)); (block.getBlock() instanceof BushBlock || block.isAir()) && !block.is(Blocks.VOID_AIR); block = livingEntityPatch.getOriginal().level().getBlockState(new BlockPos.MutableBlockPos(dpx, dpy, dpz))) {
									--dpy;
								}

								AABB box = AABB.ofSize(livingEntityPatch.getOriginal().getPosition(1.0F), 1.0F, 1.0F, 1.0F);
								List<Entity> list = (livingEntityPatch.getOriginal()).level().getEntities(livingEntityPatch.getOriginal(), box);
                                LivingEntity livingentity = livingEntityPatch.getOriginal();
								Vec3 direction = distanceTo(livingEntityPatch.getTarget(), livingEntityPatch).add(0, livingEntityPatch.getTarget().getBoundingBox().getYsize() / 2, 0).normalize().scale(4 - (2 * elapsedTime));


								if (!livingentity.onGround() && list.isEmpty()) {
									livingentity.move(MoverType.SELF, direction);
									return 0.025F;
								} else {
									return speed * 1.5f;
								}
							} else {
								return speed * 1.5f;
							}
						})
						.addEvents(AnimationProperty.StaticAnimationProperty.ON_END_EVENTS, AnimationEvent.SimpleEvent.create(Animations.ReusableSources.RESTORE_BOUNDING_BOX, AnimationEvent.Side.BOTH))
						.addEvents(AnimationProperty.StaticAnimationProperty.TICK_EVENTS, AnimationEvent.SimpleEvent.create(Animations.ReusableSources.RESIZE_BOUNDING_BOX, AnimationEvent.Side.BOTH).params(EntityDimensions.scalable(0.6F, 1.0F))
								,AnimationEvent.InTimeEvent.create(0.7f, Animations.ReusableSources.PLAY_SOUND, AnimationEvent.Side.CLIENT).params(EpicFightSounds.ROCKET_JUMP.get())
								,AnimationEvent.InTimeEvent.create(0.75f, Animations.ReusableSources.FRACTURE_GROUND_SIMPLE, AnimationEvent.Side.SERVER).params(new Vec3f(0.0F, -0.24F, -2.0F), Armatures.BIPED.get().rootJoint, 1.2, 1F)));

		IMPERATRICE_SWORD_FLAREDASH_CHARLEMAGNE = event.nextAccessor("battle_style/legendary/imperatrice_lumiere/sword/superdash/flaredash_charlemagne", access ->
				new OmneriaAttackAnimation(0.2f, 0.6f, 0.75f, 0.85f, 1.9f, ColliderPreset.BATTOJUTSU_DASH, Armatures.BIPED.get().rootJoint, access, Armatures.BIPED)
						.addProperty(BattleArtsAttackPhaseProperties.KNOCKBACK_ANGLE, -40d)
						.addProperty(BattleArtsAttackPhaseProperties.KNOCKBACK_POWER, 1d)
						.addProperty(AnimationProperty.AttackAnimationProperty.FIXED_MOVE_DISTANCE, true)
						.addProperty(AnimationProperty.ActionAnimationProperty.MOVE_VERTICAL, true)
						.addProperty(AnimationProperty.ActionAnimationProperty.NO_GRAVITY_TIME, TimePairList.create(0.0f, 0.75f))
						.addState(EntityState.CAN_SKILL_EXECUTION, false)
						.addProperty(AnimationProperty.StaticAnimationProperty.POSE_MODIFIER, Animations.ReusableSources.ROOT_X_MODIFIER)
						.addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (dynamicAnimation, livingEntityPatch, speed, prevElapsedTime, elapsedTime) ->
						{
							if (elapsedTime < 0.7F)
							{
								Vec3 dir = new  Vec3(0.0D, 0.1D, 0.0D);
								livingEntityPatch.getOriginal().move(MoverType.SELF, dir);

							}
							if (elapsedTime >= 0.7F && elapsedTime < 0.75F && (((CharlemagnePatch)livingEntityPatch).brain.getMobOpponent() != null)) {
								float dpx = (float) livingEntityPatch.getOriginal().getX();
								float dpy = (float) livingEntityPatch.getOriginal().getY();
								float dpz = (float) livingEntityPatch.getOriginal().getZ();



								for(BlockState block = livingEntityPatch.getOriginal().level().getBlockState(new BlockPos.MutableBlockPos(dpx, dpy, dpz)); (block.getBlock() instanceof BushBlock || block.isAir()) && !block.is(Blocks.VOID_AIR); block = livingEntityPatch.getOriginal().level().getBlockState(new BlockPos.MutableBlockPos(dpx, dpy, dpz))) {
									--dpy;
								}

								AABB box = AABB.ofSize(livingEntityPatch.getOriginal().getPosition(1.0F), 1.0F, 1.0F, 1.0F);
								List<Entity> list = (livingEntityPatch.getOriginal()).level().getEntities(livingEntityPatch.getOriginal(), box);
								LivingEntity livingentity = livingEntityPatch.getOriginal();
								Vec3 direction = distanceToChar((CharlemagnePatch)livingEntityPatch).add(0, livingEntityPatch.getTarget().getBoundingBox().getYsize() / 2, 0).normalize().scale(4 - (2 * elapsedTime));
								LogUtils.getLogger().debug(direction.toString());

								if (!livingentity.onGround() && list.isEmpty()) {
									livingentity.move(MoverType.SELF, direction);
									return 0.025F;
								} else {
									return speed * 1.5f;
								}
							} else {
								return speed * 1.5f;
							}
						})
						.addEvents(AnimationProperty.StaticAnimationProperty.ON_END_EVENTS, AnimationEvent.SimpleEvent.create(Animations.ReusableSources.RESTORE_BOUNDING_BOX, AnimationEvent.Side.BOTH),
								AnimationEvent.SimpleEvent.create((livingEntityPatch, assetAccessor, animationParameters) ->
                                        ((CharlemagnePatch)livingEntityPatch).brain.hostileAttackBehavior.hyperDash = false, AnimationEvent.Side.SERVER))
						.addEvents(AnimationProperty.StaticAnimationProperty.TICK_EVENTS, AnimationEvent.SimpleEvent.create(Animations.ReusableSources.RESIZE_BOUNDING_BOX, AnimationEvent.Side.BOTH).params(EntityDimensions.scalable(0.6F, 1.0F))
								,AnimationEvent.InTimeEvent.create(0.7f, Animations.ReusableSources.PLAY_SOUND, AnimationEvent.Side.CLIENT).params(EpicFightSounds.ROCKET_JUMP.get())
								,AnimationEvent.InTimeEvent.create(0.75f, Animations.ReusableSources.FRACTURE_GROUND_SIMPLE, AnimationEvent.Side.SERVER).params(new Vec3f(0.0F, -0.24F, -2.0F), Armatures.BIPED.get().rootJoint, 1.2, 1F)));

		IMPERATRICE_SWORD_WALK_SET = event.nextAccessor("battle_style/legendary/imperatrice_lumiere/sword/walk_set", access ->
				new SelectiveAnimation(debugAnim, access, IMPERATRICE_SWORD_WALK, IMPERATRICE_SWORD_WALK_BACK,IMPERATRICE_SWORD_FALL_NEUTRAL));

		IMPERATRICE_SWORD_FAST_RUN_SET = event.nextAccessor("battle_style/legendary/imperatrice_lumiere/sword/fast_run_set", access ->
				new SelectiveAnimation(livingEntityPatch ->
				{
					if (livingEntityPatch instanceof PlayerPatch<?> playerPatch)
					{
						if (!playerPatch.getOriginal().onGround())
							return 1;
					}
					return 0;
				}, access, IMPERATRICE_SWORD_FULL_SPRINT, IMPERATRICE_SWORD_FALL_FORWARD_SPRINT));


		IMPERATRICE_SWORD_JUMP_NEUTRAL = event.nextAccessor("battle_style/legendary/imperatrice_lumiere/sword/jump_neutral", access ->
				new JumpAnimation(0.083f, false, access, Armatures.BIPED)
						.addEvents(AnimationEvent.InTimeEvent.create(0.15f, (livingEntityPatch, assetAccessor, animationParameters) -> {
							if (livingEntityPatch instanceof LocalPlayerPatch localPlayerPatch && localPlayerPatch.getSkill(BattleArtsSkillSlots.BATTLE_STYLE).getDataManager().hasData(DatakeyRegistry.JUMPING.get()))
							{
								localPlayerPatch.getOriginal().jumpFromGround();
								localPlayerPatch.getSkill(BattleArtsSkillSlots.BATTLE_STYLE).getDataManager().setDataSync(DatakeyRegistry.JUMPING.get(), false);
							}
						}, AnimationEvent.Side.CLIENT))
						.addState(EntityState.MOVEMENT_LOCKED, false));

		IMPERATRICE_SWORD_JUMP_FORWARD = event.nextAccessor("battle_style/legendary/imperatrice_lumiere/sword/jump_forward_sprint", access ->
				new JumpAnimation(0.083f, false, access, Armatures.BIPED)
						.addEvents(AnimationEvent.InTimeEvent.create(0.15f, (livingEntityPatch, assetAccessor, animationParameters) -> {
							if (livingEntityPatch instanceof LocalPlayerPatch localPlayerPatch && localPlayerPatch.getSkill(BattleArtsSkillSlots.BATTLE_STYLE).getDataManager().hasData(DatakeyRegistry.JUMPING.get()))
							{
								localPlayerPatch.getOriginal().jumpFromGround();
								localPlayerPatch.getSkill(BattleArtsSkillSlots.BATTLE_STYLE).getDataManager().setDataSync(DatakeyRegistry.JUMPING.get(), false);
							}
						}, AnimationEvent.Side.CLIENT))
						.addState(EntityState.MOVEMENT_LOCKED, false));



		IMPERATRICE_SWORD_JUMP_BACK = event.nextAccessor("battle_style/legendary/imperatrice_lumiere/sword/jump_back", access ->
				new JumpAnimation(0.083f, false, access, Armatures.BIPED)
						.addEvents(AnimationEvent.InTimeEvent.create(0.15f, (livingEntityPatch, assetAccessor, animationParameters) -> {
							if (livingEntityPatch instanceof LocalPlayerPatch localPlayerPatch && localPlayerPatch.getSkill(BattleArtsSkillSlots.BATTLE_STYLE).getDataManager().hasData(DatakeyRegistry.JUMPING.get()))
							{
								localPlayerPatch.getOriginal().jumpFromGround();
								localPlayerPatch.getSkill(BattleArtsSkillSlots.BATTLE_STYLE).getDataManager().setDataSync(DatakeyRegistry.JUMPING.get(), false);
							}
						}, AnimationEvent.Side.CLIENT))
						.addState(EntityState.MOVEMENT_LOCKED, false));

		IMPERATRICE_SWORD_JUMP = event.nextAccessor("battle_style/legendary/imperatrice_lumiere/sword/jump", access ->
				new SelectiveAnimation(livingEntityPatch -> 0, access, IMPERATRICE_SWORD_JUMP_NEUTRAL));

		IMPERATRICE_SWORD_FLY_IDLE = event.nextAccessor("battle_style/legendary/imperatrice_lumiere/sword/flying_idle", access ->
				new StaticAnimation(0.3f, true, access, Armatures.BIPED));

		IMPERATRICE_SWORD_WALK = event.nextAccessor("battle_style/legendary/imperatrice_lumiere/sword/walk", access ->
				new MovementAnimation(0.1f, true, access, Armatures.BIPED));

		IMPERATRICE_SWORD_WALK_BACK = event.nextAccessor("battle_style/legendary/imperatrice_lumiere/sword/walk_back", access ->
				new MovementAnimation(0.1f, true, access, Armatures.BIPED));

		IMPERATRICE_SWORD_PARRY_1 = event.nextAccessor("battle_style/legendary/imperatrice_lumiere/sword/guard_parry_1", access ->
				new GuardAnimation(0.1f, 0.5f, access, Armatures.BIPED));

		IMPERATRICE_SWORD_RUN = event.nextAccessor("battle_style/legendary/imperatrice_lumiere/sword/run", access ->
				new MovementAnimation(0.1f, true, access, Armatures.BIPED));

		IMPERATRICE_SWORD_FULL_SPRINT = event.nextAccessor("battle_style/legendary/imperatrice_lumiere/sword/fast_run", access ->
				new MovementAnimation(0.1f, true, access, Armatures.BIPED));

		IMPERATRICE_SWORD_SUPERDASH = event.nextAccessor("battle_style/legendary/imperatrice_lumiere/sword/flying_superdash", access ->
				new MovementAnimation(0.15f, true, access, Armatures.BIPED)
						.addProperty(AnimationProperty.StaticAnimationProperty.POSE_MODIFIER, Animations.ReusableSources.FLYING_CORRECTION));

		IMPERATRICE_SWORD_FLY_FORWARD = event.nextAccessor("battle_style/legendary/imperatrice_lumiere/sword/flying_forward", access ->
				new MovementAnimation(0.15f, true, access, Armatures.BIPED)
						.addProperty(AnimationProperty.StaticAnimationProperty.POSE_MODIFIER, Animations.ReusableSources.FLYING_CORRECTION));

		IMPERATRICE_SWORD_FLY_BACK = event.nextAccessor("battle_style/legendary/imperatrice_lumiere/sword/flying_back", access ->
				new MovementAnimation(0.15f, true, access, Armatures.BIPED)
						.addProperty(AnimationProperty.StaticAnimationProperty.POSE_MODIFIER, Animations.ReusableSources.FLYING_CORRECTION2));

		IMPERATRICE_SWORD_FLY = event.nextAccessor("battle_style/legendary/imperatrice_lumiere/sword/flying", access ->
		new SelectiveAnimation(
				livingEntityPatch ->
				{
					Vec3 view = livingEntityPatch.getOriginal().getViewVector(1.0F);
					Vec3 move = livingEntityPatch.getOriginal().getDeltaMovement();
					double dot = view.dot(move);
					if (livingEntityPatch.getOriginal().isSprinting())
					{
						return 2;
					}
					return dot < (double)0.0F ? 1 : 0;
				}, access, IMPERATRICE_SWORD_FLY_FORWARD, IMPERATRICE_SWORD_FLY_BACK, IMPERATRICE_SWORD_SUPERDASH

		));

		IMPERATRICE_SWORD_NEUTRAL_ATTACK = event.nextAccessor("battle_style/legendary/imperatrice_lumiere/sword/jab", access ->
				new OmneriaAttackAnimation(0.2f, 0, 0.2f, 0.3f, 1.5f, null, Armatures.BIPED.get().toolR, access, Armatures.BIPED)
						.addProperty(AnimationProperty.AttackPhaseProperty.HIT_SOUND, SoundRegistry.IMPERATRICE_HIT_S.get())
						.addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.HOLD)
						.addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (a,b,c,d,e) -> c * 1.2f)
						.addEvents(AnimationEvent.SimpleEvent.create(
								(livingEntityPatch, assetAccessor, animationParameters) ->
								{
									if (!livingEntityPatch.getOriginal().onGround())
									{
										assetAccessor.get().addProperty(AnimationProperty.ActionAnimationProperty.NO_GRAVITY_TIME, TimePairList.create(0, 1.5f));
									}
								}, AnimationEvent.Side.SERVER
						)));

		IMPERATRICE_SWORD_FLARESPIN = event.nextAccessor("battle_style/legendary/imperatrice_lumiere/sword/flarespin/flarespin", access ->
				new OmneriaAttackAnimation(0.2f, access, Armatures.BIPED,
						new OmneriaAttackAnimation.Phase(0.0f, 0.0f, 0.05f, 0.15f, 0.15f, 0.15f, Armatures.BIPED.get().rootJoint, LumiereColliders.IMPERATRICE_NEUTRAL_AERIAL),
						new OmneriaAttackAnimation.Phase(0.15f, 0.0f, 0.25f, 0.35f, 0.35f, 0.35f, Armatures.BIPED.get().rootJoint, LumiereColliders.IMPERATRICE_NEUTRAL_AERIAL),
						new OmneriaAttackAnimation.Phase(0.35f, 0.0f, 0.045f, 0.55f, 0.55f, 0.55f, Armatures.BIPED.get().rootJoint, LumiereColliders.IMPERATRICE_NEUTRAL_AERIAL),
						new OmneriaAttackAnimation.Phase(0.55f, 0.0f, 0.65f, 0.75f, 1f, 1f, Armatures.BIPED.get().rootJoint, LumiereColliders.IMPERATRICE_NEUTRAL_AERIAL))
						.addProperty(AnimationProperty.AttackPhaseProperty.HIT_SOUND, SoundRegistry.IMPERATRICE_HIT_S.get())
						.addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.HOLD)
						.addProperty(AnimationProperty.AttackPhaseProperty.HIT_SOUND, SoundRegistry.IMPERATRICE_HIT_S.get(), 1)
						.addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.HOLD, 1)
						.addProperty(AnimationProperty.AttackPhaseProperty.HIT_SOUND, SoundRegistry.IMPERATRICE_HIT_S.get(), 2)
						.addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.HOLD, 2)
						.addProperty(AnimationProperty.AttackPhaseProperty.HIT_SOUND, SoundRegistry.IMPERATRICE_HIT_M.get(), 3)
						.addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.LONG, 3)
						.addProperty(AnimationProperty.AttackAnimationProperty.EXTRA_COLLIDERS, 2)
						.addProperty(AnimationProperty.ActionAnimationProperty.NO_GRAVITY_TIME, TimePairList.create(0, 1))
						.addState(EntityState.CAN_SKILL_EXECUTION, false)
						.addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (a,b,c,d,e) -> 1f));

		IMPERATRICE_SWORD_CROUCH_ATTACK = event.nextAccessor("battle_style/legendary/imperatrice_lumiere/sword/crouch", access ->
				new OmneriaAttackAnimation(0.2f, 0, 0.45f, 0.6f, 1.5f, null, Armatures.BIPED.get().toolR, access, Armatures.BIPED)
						.addProperty(AnimationProperty.AttackPhaseProperty.HIT_SOUND, SoundRegistry.IMPERATRICE_HIT_S.get())
						.addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.HOLD)
						.addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (a,b,c,d,e) -> 2f));

		IMPERATRICE_SWORD_NEUTRAL_ATTACK_ALT = event.nextAccessor("battle_style/legendary/imperatrice_lumiere/sword/jab_alt", access ->
				new OmneriaAttackAnimation(0.2f, 0, 0.2f, 0.3f, 1.5f, null, Armatures.BIPED.get().toolR, access, Armatures.BIPED)
						.addProperty(AnimationProperty.AttackPhaseProperty.HIT_SOUND, SoundRegistry.IMPERATRICE_HIT_S.get())
						.addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.HOLD)
						.addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (a,b,c,d,e) -> c * 1.2f)
						.addEvents(AnimationEvent.SimpleEvent.create(
								(livingEntityPatch, assetAccessor, animationParameters) ->
								{
									if (!livingEntityPatch.getOriginal().onGround())
									{
										assetAccessor.get().addProperty(AnimationProperty.ActionAnimationProperty.NO_GRAVITY_TIME, TimePairList.create(0, 1.5f));
									}
								}, AnimationEvent.Side.SERVER
						)));

		IMPERATRICE_SWORD_LEFT_ATTACK = event.nextAccessor("battle_style/legendary/imperatrice_lumiere/sword/left", access ->
				new OmneriaAttackAnimation(0.2f, 0, 0.6f, 0.7f, 1.5f, null, Armatures.BIPED.get().toolR, access, Armatures.BIPED)
						.addProperty(AnimationProperty.AttackPhaseProperty.HIT_SOUND, SoundRegistry.IMPERATRICE_HIT_S.get())
						.addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.HOLD)
						.addProperty(AnimationProperty.AttackAnimationProperty.EXTRA_COLLIDERS, 2)
						.addEvents(AnimationEvent.SimpleEvent.create(
								(livingEntityPatch, assetAccessor, animationParameters) ->
								{
									if (!livingEntityPatch.getOriginal().onGround())
									{
										assetAccessor.get().addProperty(AnimationProperty.ActionAnimationProperty.NO_GRAVITY_TIME, TimePairList.create(0, 1.5f));
									}
								}, AnimationEvent.Side.SERVER
						))
						.addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (a,b,c,d,e) -> 2f));

		IMPERATRICE_SWORD_RIGHT_ATTACK = event.nextAccessor("battle_style/legendary/imperatrice_lumiere/sword/right", access ->
				new OmneriaAttackAnimation(0.2f, 0, 0.3f, 0.4f, 1.5f, null, Armatures.BIPED.get().toolR, access, Armatures.BIPED)
						.addProperty(AnimationProperty.AttackPhaseProperty.HIT_SOUND, SoundRegistry.IMPERATRICE_HIT_S.get())
						.addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.HOLD)
						.addProperty(AnimationProperty.AttackAnimationProperty.EXTRA_COLLIDERS, 2)
						.addEvents(AnimationEvent.SimpleEvent.create(
								(livingEntityPatch, assetAccessor, animationParameters) ->
								{
									if (!livingEntityPatch.getOriginal().onGround())
									{
										assetAccessor.get().addProperty(AnimationProperty.ActionAnimationProperty.NO_GRAVITY_TIME, TimePairList.create(0, 1.5f));
									}
								}, AnimationEvent.Side.SERVER
						))
						.addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (a,b,c,d,e) -> 2f));

		IMPERATRICE_SWORD_BACK_ATTACK = event.nextAccessor("battle_style/legendary/imperatrice_lumiere/sword/back", access ->
				new OmneriaAttackAnimation(0.2f, 0, 0.5f, 0.6f, 1.5f, null, Armatures.BIPED.get().toolR, access, Armatures.BIPED)
						.addProperty(AnimationProperty.AttackPhaseProperty.HIT_SOUND, SoundRegistry.IMPERATRICE_HIT_S.get())
						.addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.HOLD)
						.addEvents(AnimationEvent.SimpleEvent.create(
								(livingEntityPatch, assetAccessor, animationParameters) ->
								{
									if (!livingEntityPatch.getOriginal().onGround())
									{
										assetAccessor.get().addProperty(AnimationProperty.ActionAnimationProperty.NO_GRAVITY_TIME, TimePairList.create(0, 1.5f));
									}
								}, AnimationEvent.Side.SERVER
						))
						.addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (a,b,c,d,e) -> 2f));

		IMPERATRICE_SWORD_SOLAR_FLARE = event.nextAccessor("battle_style/legendary/imperatrice_lumiere/sword/solar_flare", access ->
				new OmneriaAttackAnimation(0.2f, 0, 0.2f, 0.3f, 1.5f, null, Armatures.BIPED.get().toolR, access, Armatures.BIPED)
						.addProperty(AnimationProperty.AttackPhaseProperty.HIT_SOUND, SoundRegistry.IMPERATRICE_HIT_FINISHER.get())
						.addProperty(BattleArtsAttackPhaseProperties.KNOCKBACK_ANGLE, 70.0)
						.addProperty(BattleArtsAttackPhaseProperties.KNOCKBACK_POWER, 1.0)
						.addState(EntityState.CAN_SKILL_EXECUTION, false)
						.addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (a,b,c,d,e) -> 1f));

		IMPERATRICE_SWORD_SOLAR_DRIVE = event.nextAccessor("battle_style/legendary/imperatrice_lumiere/sword/sabre_drive", access ->
				new OmneriaAttackAnimation(0.2f, 0, 0.4f, 0.5f, 1.5f, null, Armatures.BIPED.get().toolR, access, Armatures.BIPED)
						.addProperty(AnimationProperty.AttackPhaseProperty.HIT_SOUND, SoundRegistry.IMPERATRICE_HIT_FINISHER.get())
						.addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.LONG)
						.addState(EntityState.CAN_SKILL_EXECUTION, false)
						.addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (a,b,c,d,e) -> 1f));

		IMPERATRICE_SWORD_BACK_ATTACK_ALT = event.nextAccessor("battle_style/legendary/imperatrice_lumiere/sword/back_alt", access ->
				new OmneriaAttackAnimation(0.1f, 0, 0.3f, 0.5f, 1.5f, null, Armatures.BIPED.get().toolR, access, Armatures.BIPED)
						.addProperty(AnimationProperty.AttackPhaseProperty.HIT_SOUND, SoundRegistry.IMPERATRICE_HIT_S.get())
						.addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.HOLD)
						.addEvents(AnimationEvent.SimpleEvent.create(
								(livingEntityPatch, assetAccessor, animationParameters) ->
								{
									if (!livingEntityPatch.getOriginal().onGround())
									{
										assetAccessor.get().addProperty(AnimationProperty.ActionAnimationProperty.NO_GRAVITY_TIME, TimePairList.create(0, 1.5f));
									}
								}, AnimationEvent.Side.SERVER
						))
						.addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (a,b,c,d,e) -> 2f));

		IMPERATRICE_SWORD_FRONT_ATTACK = event.nextAccessor("battle_style/legendary/imperatrice_lumiere/sword/front", access ->
				new OmneriaAttackAnimation(0.1f, 0, 0.3f, 0.4f, 1.5f, null, Armatures.BIPED.get().toolR, access, Armatures.BIPED)
						.addProperty(AnimationProperty.AttackPhaseProperty.HIT_SOUND, SoundRegistry.IMPERATRICE_HIT_S.get())
						.addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.HOLD)
						.addEvents(AnimationEvent.SimpleEvent.create(
								(livingEntityPatch, assetAccessor, animationParameters) ->
								{
									if (!livingEntityPatch.getOriginal().onGround())
									{
										assetAccessor.get().addProperty(AnimationProperty.ActionAnimationProperty.NO_GRAVITY_TIME, TimePairList.create(0, 1.5f));
									}
								}, AnimationEvent.Side.SERVER
						))
						.addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (a,b,c,d,e) -> 1f));

		IMPERATRICE_SWORD_FRONT_ATTACK_ALT = event.nextAccessor("battle_style/legendary/imperatrice_lumiere/sword/front_alt", access ->
				new OmneriaAttackAnimation(0.1f, 0, 0.3f, 0.4f, 1.5f, null, Armatures.BIPED.get().toolR, access, Armatures.BIPED)
						.addProperty(AnimationProperty.AttackPhaseProperty.HIT_SOUND, SoundRegistry.IMPERATRICE_HIT_S.get())
						.addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.HOLD)
						.addEvents(AnimationEvent.SimpleEvent.create(
								(livingEntityPatch, assetAccessor, animationParameters) ->
								{
									if (!livingEntityPatch.getOriginal().onGround())
									{
										assetAccessor.get().addProperty(AnimationProperty.ActionAnimationProperty.NO_GRAVITY_TIME, TimePairList.create(0, 1.5f));
									}
								}, AnimationEvent.Side.SERVER
						))
						.addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (a,b,c,d,e) -> 1f));

	}
	private static Vec3 distanceTo(LivingEntity opponent, LivingEntityPatch<?> attackerPatch)
	{
		return opponent.position().subtract(attackerPatch.getOriginal().position());
	}

	private static Vec3 distanceToChar(CharlemagnePatch charactermagnePatch)
	{
		return charactermagnePatch.getTarget().position().subtract(charactermagnePatch.getOriginal().position());
	}
}
