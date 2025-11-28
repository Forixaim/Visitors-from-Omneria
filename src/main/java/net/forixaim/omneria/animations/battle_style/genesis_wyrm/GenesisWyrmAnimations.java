package net.forixaim.omneria.animations.battle_style.genesis_wyrm;

import com.mojang.logging.LogUtils;
import net.forixaim.battle_arts_api.battle_arts_skills.BattleArtsSkillSlots;
import net.forixaim.omneria.animations.ReusableEvents;
import net.forixaim.omneria.animations.types.BattleArtsAttackPhaseProperties;
import net.forixaim.omneria.animations.types.OmneriaAttackAnimation;
import net.forixaim.omneria.animations.types.OmneriaEntityStates;
import net.forixaim.omneria.animations.types.OmneriaGrabAnimation;
import net.forixaim.omneria.client.particles.types.TrackingParticleType;
import net.forixaim.omneria.colliders.GenesisWyrmColliders;
import net.forixaim.omneria.combat.OmneriaDamageTypes;
import net.forixaim.omneria.registry.EntityRegistry;
import net.forixaim.omneria.registry.ParticleRegistry;
import net.forixaim.omneria.registry.SoundRegistry;
import net.forixaim.omneria.skill.DatakeyRegistry;
import net.forixaim.omneria.world.entity.projectiles.DarkBangProjectile;
import net.forixaim.omneria.world.entity.projectiles.DragonCannonBeam;
import net.forixaim.omneria.world.entity.projectiles.FullPowerDragonCannonBeam;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import yesman.epicfight.api.animation.AnimationManager;
import yesman.epicfight.api.animation.JointTransform;
import yesman.epicfight.api.animation.property.AnimationEvent;
import yesman.epicfight.api.animation.property.AnimationProperty;
import yesman.epicfight.api.animation.property.MoveCoordFunctions;
import yesman.epicfight.api.animation.types.*;
import yesman.epicfight.api.utils.TimePairList;
import yesman.epicfight.api.utils.math.OpenMatrix4f;
import yesman.epicfight.api.utils.math.QuaternionUtils;
import yesman.epicfight.api.utils.math.ValueModifier;
import yesman.epicfight.api.utils.math.Vec3f;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.gameasset.Armatures;
import yesman.epicfight.gameasset.ColliderPreset;
import yesman.epicfight.gameasset.EpicFightSounds;
import yesman.epicfight.model.armature.HumanoidArmature;
import yesman.epicfight.particle.EpicFightParticles;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;
import yesman.epicfight.world.damagesource.EpicFightDamageSource;
import yesman.epicfight.world.damagesource.EpicFightDamageTypeTags;
import yesman.epicfight.world.damagesource.StunType;

import java.util.List;
import java.util.Set;

public class GenesisWyrmAnimations
{
    public static AnimationManager.AnimationAccessor<StaticAnimation> IDLE;
    public static AnimationManager.AnimationAccessor<StaticAnimation> IDLE_INJURED;
    public static AnimationManager.AnimationAccessor<StaticAnimation> IDLE_EXHAUSTED;

    public static AnimationManager.AnimationAccessor<SelectiveAnimation> IDLE_SET;

    public static AnimationManager.AnimationAccessor<MovementAnimation> WALK;
    public static AnimationManager.AnimationAccessor<MovementAnimation> RUN;

    public static AnimationManager.AnimationAccessor<MovementAnimation> WALK_BACK;
    public static AnimationManager.AnimationAccessor<SelectiveAnimation> WALK_SET;


    public static AnimationManager.AnimationAccessor<StaticAnimation> GUARD;
    public static AnimationManager.AnimationAccessor<StaticAnimation> CROUCH;

    public static AnimationManager.AnimationAccessor<GuardAnimation> GUARD_HIT;
    public static AnimationManager.AnimationAccessor<GuardAnimation> REFLECTION;

    public static AnimationManager.AnimationAccessor<BasicAttackAnimation> AUTO1;
    public static AnimationManager.AnimationAccessor<BasicAttackAnimation> AUTO2;
    public static AnimationManager.AnimationAccessor<OmneriaAttackAnimation> AUTO3;
    public static AnimationManager.AnimationAccessor<OmneriaAttackAnimation> AUTO4;
    public static AnimationManager.AnimationAccessor<OmneriaAttackAnimation> AUTO5;
    public static AnimationManager.AnimationAccessor<OmneriaAttackAnimation> MELEE_COUNTER;
    public static AnimationManager.AnimationAccessor<OmneriaAttackAnimation> RANGED_COUNTER;
    public static AnimationManager.AnimationAccessor<OmneriaAttackAnimation> DRAGON_RUSH_ATTEMPT;
    public static AnimationManager.AnimationAccessor<OmneriaAttackAnimation> DRAGON_RUSH_ATTACK;
    public static AnimationManager.AnimationAccessor<OmneriaAttackAnimation> MOONLIGHT_FINISH;




    public static AnimationManager.AnimationAccessor<InvincibleAnimation> TWILIGHT_ACTIVATION;
    public static AnimationManager.AnimationAccessor<InvincibleAnimation> DRAGON_CANNON;
    public static AnimationManager.AnimationAccessor<InvincibleAnimation> FULL_POWER_DRAGON_CANNON;


    public static AnimationManager.AnimationAccessor<OmneriaAttackAnimation> COSMIC_CHASER;


    public static AnimationManager.AnimationAccessor<OmneriaAttackAnimation> LEG_AUTO1;
    public static AnimationManager.AnimationAccessor<OmneriaAttackAnimation> LEG_AUTO2;

    public static AnimationManager.AnimationAccessor<BasicAttackAnimation> FOCUS_AUTO1;
    public static AnimationManager.AnimationAccessor<BasicAttackAnimation> FOCUS_AUTO2;

    public static AnimationManager.AnimationAccessor<OmneriaAttackAnimation> HEAVY_AUTO1;
    public static AnimationManager.AnimationAccessor<OmneriaAttackAnimation> HEAVY_AUTO2;
    public static AnimationManager.AnimationAccessor<OmneriaAttackAnimation> HEAVY_AUTO3;
    public static AnimationManager.AnimationAccessor<OmneriaAttackAnimation> DARK_UPPER;
    public static AnimationManager.AnimationAccessor<OmneriaAttackAnimation> DRAGON_UPPERCUT;
    public static AnimationManager.AnimationAccessor<InvincibleAnimation> DRACONIC_DODGE1;
    public static AnimationManager.AnimationAccessor<InvincibleAnimation> DRACONIC_DODGE2;
    public static AnimationManager.AnimationAccessor<InvincibleAnimation> DRACONIC_DODGE3;



    public static AnimationManager.AnimationAccessor<OmneriaAttackAnimation> UMBRAL_HAMMER;
    public static AnimationManager.AnimationAccessor<OmneriaGrabAnimation> DRAGON_THROW_TRY;
    public static AnimationManager.AnimationAccessor<OmneriaAttackAnimation> DRAGON_THROW;
    public static AnimationManager.AnimationAccessor<LongHitAnimation> DRAGON_THROW_VICTIM_BIPED;



    public static AnimationManager.AnimationAccessor<BasicAttackAnimation> BLAST_AUTO1;
    public static AnimationManager.AnimationAccessor<BasicAttackAnimation> BLAST_AUTO2;

    public static AnimationManager.AnimationAccessor<OmneriaAttackAnimation> DARK_BANG;

    public static final AnimationProperty.PoseModifier LOCK_POSE = (self, pose, entitypatch, time, partialTicks) -> {
        JointTransform chest = pose.orElseEmpty("Root");};

    public static void build(AnimationManager.AnimationBuilder builder)
    {
        IDLE = builder.nextAccessor("battle_style/legendary/genesis_wyrm/idle", access -> new StaticAnimation(0.1f, true, access, Armatures.BIPED));
        IDLE_INJURED = builder.nextAccessor("battle_style/legendary/genesis_wyrm/idle_injured", access -> new StaticAnimation(0.1f, true, access, Armatures.BIPED));
        IDLE_EXHAUSTED = builder.nextAccessor("battle_style/legendary/genesis_wyrm/idle_exhausted", access -> new StaticAnimation(0.1f, true, access, Armatures.BIPED));

        IDLE_SET = builder.nextAccessor("battle_style/legendary/genesis_wyrm/idle_set", access -> new SelectiveAnimation(
                patch -> {
                    float percentage = patch.getOriginal().getHealth() / patch.getOriginal().getMaxHealth();
                    if (percentage < 0.5f)
                    {
                        if (percentage < 0.25f)
                            return 2;
                        return 1;
                    }
                    return 0;
                }, access, IDLE, IDLE_INJURED, IDLE_EXHAUSTED
        ));



        TWILIGHT_ACTIVATION = builder.nextAccessor("battle_style/legendary/genesis_wyrm/twilight_activation", access ->
                new InvincibleAnimation(0.05f, access, Armatures.BIPED)
                        .addEvents(AnimationEvent.InTimeEvent.create(0.6f, (livingEntityPatch, assetAccessor, animationParameters) ->
                        {
                            if (livingEntityPatch instanceof ServerPlayerPatch serverPlayerPatch)
                            {
                                if (serverPlayerPatch.getSkill(BattleArtsSkillSlots.BATTLE_STYLE).getDataManager().hasData(DatakeyRegistry.TWILIGHT.get()))
                                {
                                    serverPlayerPatch.getSkill(BattleArtsSkillSlots.BATTLE_STYLE).getDataManager().setDataSync(DatakeyRegistry.TWILIGHT.get(), true);
                                    serverPlayerPatch.playSound(SoundRegistry.POWER_UP.get(), 50, 0, 0);
                                }
                            }
                        }, AnimationEvent.Side.SERVER)));

        DRACONIC_DODGE1 = builder.nextAccessor("battle_style/legendary/genesis_wyrm/instinct_dodge1", access ->
                new InvincibleAnimation(0.1f, access, Armatures.BIPED));

        DRACONIC_DODGE2 = builder.nextAccessor("battle_style/legendary/genesis_wyrm/instinct_dodge2", access ->
                new InvincibleAnimation(0.1f, access, Armatures.BIPED));

        DRACONIC_DODGE3 = builder.nextAccessor("battle_style/legendary/genesis_wyrm/instinct_dodge3", access ->
                new InvincibleAnimation(0.1f, access, Armatures.BIPED));



        DARK_UPPER = builder.nextAccessor("battle_style/legendary/genesis_wyrm/dark_upper", access -> new OmneriaAttackAnimation(
                0.1f, 0.0f, 0.1f, 0.2f, 1f, ColliderPreset.BATTOJUTSU_DASH, Armatures.BIPED.get().rootJoint, access, Armatures.BIPED
        ).addProperty(BattleArtsAttackPhaseProperties.KNOCKBACK_POWER, 0.7d).addProperty(AnimationProperty.AttackPhaseProperty.HIT_SOUND, SoundRegistry.IMPERATRICE_PUNCH_IMPACT_M.get()).addProperty(BattleArtsAttackPhaseProperties.KNOCKBACK_ANGLE, 60d)
                .addProperty(AnimationProperty.AttackPhaseProperty.IMPACT_MODIFIER, ValueModifier.setter(5))
                .addProperty(BattleArtsAttackPhaseProperties.HITSTUN_TICKS, 5)
                .addProperty(BattleArtsAttackPhaseProperties.ENDLAG_TICKS, 4)
                .addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, Animations.ReusableSources.CONSTANT_ONE));

        DRAGON_UPPERCUT = builder.nextAccessor("battle_style/legendary/genesis_wyrm/dragon_uppercut", access -> new OmneriaAttackAnimation(
                0.1f, 0.0f, 0.2f, 0.35f, 0.75f, ColliderPreset.BATTOJUTSU_DASH, Armatures.BIPED.get().rootJoint, access, Armatures.BIPED
        ).addProperty(BattleArtsAttackPhaseProperties.KNOCKBACK_POWER, 1.2d).addProperty(AnimationProperty.AttackPhaseProperty.HIT_SOUND, SoundRegistry.HEAVY_BLOW.get()).addProperty(BattleArtsAttackPhaseProperties.KNOCKBACK_ANGLE, 60d)
                .addProperty(AnimationProperty.AttackPhaseProperty.IMPACT_MODIFIER, ValueModifier.setter(5))
                .addProperty(BattleArtsAttackPhaseProperties.HITSTUN_TICKS, 12)
                .addProperty(BattleArtsAttackPhaseProperties.ENDLAG_TICKS, 4)

                .addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, Animations.ReusableSources.CONSTANT_ONE));

        DRAGON_RUSH_ATTEMPT = builder.nextAccessor("battle_style/legendary/genesis_wyrm/dragon_rush_attempt", access -> new OmneriaAttackAnimation(
                0.1f, 0.0f, 0.2f, 0.25f, 1f, ColliderPreset.BATTOJUTSU_DASH, Armatures.BIPED.get().rootJoint, access, Armatures.BIPED
        ).addProperty(BattleArtsAttackPhaseProperties.KNOCKBACK_POWER, 6d).addProperty(BattleArtsAttackPhaseProperties.KNOCKBACK_ANGLE, 0d)
                .addProperty(AnimationProperty.AttackPhaseProperty.IMPACT_MODIFIER, ValueModifier.setter(5))
                .addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, Animations.ReusableSources.CONSTANT_ONE));
        DRAGON_RUSH_ATTACK = builder.nextAccessor("battle_style/legendary/genesis_wyrm/dragon_rush_attack", access -> new OmneriaAttackAnimation(
                0.1f, access, Armatures.BIPED,
                new AttackAnimation.Phase(0.0f, 0.0f, 0.2f, 0.3f, 0.7f, 0.7f, Armatures.BIPED.get().rootJoint, ColliderPreset.BATTOJUTSU_DASH).addProperty(BattleArtsAttackPhaseProperties.KNOCKBACK_POWER, 1.7d).addProperty(BattleArtsAttackPhaseProperties.KNOCKBACK_ANGLE, 77d),
                new AttackAnimation.Phase(0.7f, 0.0f, 0.8f, 0.85f, 0.85f, 0.85f, Armatures.BIPED.get().rootJoint, ColliderPreset.BATTOJUTSU_DASH),
                new AttackAnimation.Phase(0.85f, 0.0f, 0.85f, 0.9f, 0.9f, 0.9f, Armatures.BIPED.get().rootJoint, ColliderPreset.BATTOJUTSU_DASH),
                new AttackAnimation.Phase(0.9f, 0.0f, 0.9f, 0.95f, 0.95f, 0.95f, Armatures.BIPED.get().rootJoint, ColliderPreset.BATTOJUTSU_DASH),
                new AttackAnimation.Phase(0.95f, 0.0f, 0.95f, 1f, 1f, 1f, Armatures.BIPED.get().rootJoint, ColliderPreset.BATTOJUTSU_DASH),
                new AttackAnimation.Phase(1f, 0.0f, 1f, 1.05f, 1.05f, 1.05f, Armatures.BIPED.get().rootJoint, ColliderPreset.BATTOJUTSU_DASH),
                new AttackAnimation.Phase(1.05f, 0.0f, 1.05f, 1.1f, 1.1f, 1.1f, Armatures.BIPED.get().rootJoint, ColliderPreset.BATTOJUTSU_DASH),
                new AttackAnimation.Phase(1.2f, 0.0f, 1.45f, 1.5f, 2f, 2f, Armatures.BIPED.get().rootJoint, ColliderPreset.BATTOJUTSU_DASH).addProperty(BattleArtsAttackPhaseProperties.KNOCKBACK_POWER, 3d).addProperty(BattleArtsAttackPhaseProperties.KNOCKBACK_ANGLE, -90d)
        )
                .addProperty(AnimationProperty.AttackPhaseProperty.IMPACT_MODIFIER, ValueModifier.setter(1))
                .addProperty(AnimationProperty.ActionAnimationProperty.MOVE_VERTICAL, true)
                .addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (dynamicAnimation, livingEntityPatch, v, v1, v2) ->
                {
                    if (v2 > 0.0f && v2 < 0.2f)
                    {
                        return 0.5f;
                    }
                    else
                    {
                        return 1f;
                    }
                }));


        MOONLIGHT_FINISH = builder.nextAccessor("battle_style/legendary/genesis_wyrm/moonlight_finish", access -> new OmneriaAttackAnimation(
                0.1f, 0.0f, 0.4f, 0.65f, 1f, ColliderPreset.FIST, Armatures.BIPED.get().handL, access, Armatures.BIPED
        ).addProperty(BattleArtsAttackPhaseProperties.KNOCKBACK_POWER, 1.0).addProperty(BattleArtsAttackPhaseProperties.HITSTUN_TICKS, 12).addProperty(AnimationProperty.AttackPhaseProperty.HIT_SOUND, SoundRegistry.HEAVY_BLOW.get()).addProperty(BattleArtsAttackPhaseProperties.KNOCKBACK_ANGLE, 0d)
                .addProperty(AnimationProperty.AttackPhaseProperty.IMPACT_MODIFIER, ValueModifier.setter(5))
                .addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, Animations.ReusableSources.CONSTANT_ONE));

        DRAGON_CANNON = builder.nextAccessor("battle_style/legendary/genesis_wyrm/dragon_cannon", access ->
                new InvincibleAnimation(0.05f, access, Armatures.BIPED)
                        .addProperty(AnimationProperty.AttackAnimationProperty.STOP_MOVEMENT, true)
                        .addState(EntityState.CAN_SKILL_EXECUTION, false)
                        .addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (dynamicAnimation, livingEntityPatch, v, v1, v2) ->
                        {
                            if (livingEntityPatch instanceof PlayerPatch<?> playerPatch && playerPatch.getSkill(BattleArtsSkillSlots.BATTLE_STYLE).getDataManager().hasData(DatakeyRegistry.BEAM.get()) && playerPatch.getSkill(BattleArtsSkillSlots.BATTLE_STYLE).getDataManager().getDataValue(DatakeyRegistry.BEAM.get()) > -1)
                            {
                                Entity beam = playerPatch.getOriginal().level().getEntity(playerPatch.getSkill(BattleArtsSkillSlots.BATTLE_STYLE).getDataManager().getDataValue(DatakeyRegistry.BEAM.get()));
                                if (beam instanceof DragonCannonBeam trueBeam && !trueBeam.isRemoved())
                                {
                                    return 0;
                                }
                            }
                            return 1;
                        })
                        .addEvents(AnimationEvent.InTimeEvent.create(0.1f, (livingEntityPatch, assetAccessor, animationParameters) -> {
                            livingEntityPatch.playSound(SoundRegistry.DARK_BANG_MANIFEST.get(),  1, 0, 0);
                            ((ServerLevel)livingEntityPatch.getOriginal().level()).sendParticles(new TrackingParticleType(livingEntityPatch.getOriginal().getId(), ParticleRegistry.DRAGON_CANNON_CHARGE.get()), livingEntityPatch.getOriginal().getX(), livingEntityPatch.getOriginal().getY(), livingEntityPatch.getOriginal().getZ(), 1, 0, 0, 0, 0);
                            }, AnimationEvent.Side.SERVER), AnimationEvent.InTimeEvent.create(0.45f, (livingEntityPatch, assetAccessor, animationParameters) ->
                        {
                            float ang = (float) ((livingEntityPatch.getYRot()+90)/180 * Math.PI);

                            Vec3 position = new Vec3(livingEntityPatch.getOriginal().getLookAngle().x, 0, livingEntityPatch.getOriginal().getLookAngle().z).normalize().scale(1.5);
                            Vec3 shootVec = new Vec3(Math.cos(ang), 0 , Math.sin(ang));
                            Vec3 shootPos = livingEntityPatch.getOriginal().position().add(0, livingEntityPatch.getOriginal().getEyeHeight() - 0.5, 0).add(position);


                            DragonCannonBeam projectile = EntityRegistry.DRAGON_CANNON.get().create(livingEntityPatch.getOriginal().level());

                            if (projectile != null) {

                                projectile.setPos(shootPos);
                                projectile.shoot(shootVec.x(), 0, shootVec.z(), 4f, 0);
                                projectile.setDamageSource(new EpicFightDamageSource(projectile.level().damageSources().generic()));
                                if (livingEntityPatch.getArmature() instanceof HumanoidArmature ha) {
                                    OpenMatrix4f jointMatrix = livingEntityPatch.getArmature().getBoundTransformFor(livingEntityPatch.getAnimator().getPose(0.0F), ha.handR).mulFront(OpenMatrix4f.createTranslation((float) livingEntityPatch.getOriginal().getX(), (float) livingEntityPatch.getOriginal().getY(), (float) livingEntityPatch.getOriginal().getZ()).mulBack(OpenMatrix4f.createRotatorDeg(180.0F, Vec3f.Y_AXIS).mulBack(livingEntityPatch.getModelMatrix(0.0F))));
                                    LogUtils.getLogger().debug(jointMatrix.toTranslationVector().toString());
                                    jointMatrix.translate(new Vec3f(0.0F, 1, 0F));
                                    projectile.setOrigin(jointMatrix.toTranslationVector().toDoubleVector());
                                    projectile.setPosRaw(jointMatrix.toTranslationVector().x, jointMatrix.toTranslationVector().y, jointMatrix.toTranslationVector().z);
                                }
                                projectile.setOwner(livingEntityPatch.getOriginal());
                                livingEntityPatch.playSound(SoundRegistry.HEAVY_BLAST.get(), 0, 0);
                                livingEntityPatch.getOriginal().level().addFreshEntity(projectile);
                                if (livingEntityPatch instanceof ServerPlayerPatch serverPlayerPatch)
                                {
                                    serverPlayerPatch.getSkill(BattleArtsSkillSlots.BATTLE_STYLE).getDataManager().setDataSync(DatakeyRegistry.BEAM.get(), projectile.getId());
                                }
                            }
                        }, AnimationEvent.Side.SERVER)));



        FULL_POWER_DRAGON_CANNON = builder.nextAccessor("battle_style/legendary/genesis_wyrm/full_power_dragon_cannon", access ->
                new InvincibleAnimation(0.05f, access, Armatures.BIPED)
                        .addProperty(AnimationProperty.AttackAnimationProperty.STOP_MOVEMENT, true)
                        .addProperty(AnimationProperty.ActionAnimationProperty.NO_GRAVITY_TIME, TimePairList.create(0.0f, 2.0f))
                        .addProperty(AnimationProperty.StaticAnimationProperty.POSE_MODIFIER, Animations.ReusableSources.COMBO_ATTACK_DIRECTION_MODIFIER)
                        .addState(EntityState.CAN_SKILL_EXECUTION, false)
                        .addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (dynamicAnimation, livingEntityPatch, v, v1, v2) ->
                        {
                            if (livingEntityPatch instanceof PlayerPatch<?> playerPatch && playerPatch.getSkill(BattleArtsSkillSlots.BATTLE_STYLE).getDataManager().hasData(DatakeyRegistry.BEAM.get()) && playerPatch.getSkill(BattleArtsSkillSlots.BATTLE_STYLE).getDataManager().getDataValue(DatakeyRegistry.BEAM.get()) > -1)
                            {
                                Entity beam = playerPatch.getOriginal().level().getEntity(playerPatch.getSkill(BattleArtsSkillSlots.BATTLE_STYLE).getDataManager().getDataValue(DatakeyRegistry.BEAM.get()));
                                if (beam instanceof FullPowerDragonCannonBeam trueBeam && !trueBeam.isRemoved())
                                {
                                    return 0;
                                }
                            }
                            return 1;
                        })
                        .addEvents(AnimationEvent.InTimeEvent.create(0.4f, (livingEntityPatch, assetAccessor, animationParameters) -> {
                            livingEntityPatch.playSound(SoundRegistry.DARK_BANG_MANIFEST.get(),  1, 0, 0);
                            livingEntityPatch.playSound(SoundRegistry.FPDC_CHARGE_BEGIN.get(),  1, 0, 0);

                            livingEntityPatch.playSound(SoundRegistry.CANNON_CHARGE.get(),  1, 0, 0);

                            ((ServerLevel)livingEntityPatch.getOriginal().level()).sendParticles(new TrackingParticleType(livingEntityPatch.getOriginal().getId(), ParticleRegistry.FULL_POWER_DRAGON_CANNON_CHARGE.get()), livingEntityPatch.getOriginal().getX(), livingEntityPatch.getOriginal().getY(), livingEntityPatch.getOriginal().getZ(), 1, 0, 0, 0, 0);
                        }, AnimationEvent.Side.SERVER), AnimationEvent.InTimeEvent.create(1.55f, (livingEntityPatch, assetAccessor, animationParameters) ->
                        {
                            float ang = (float) ((livingEntityPatch.getYRot()+90)/180 * Math.PI);

                            Vec3 position = new Vec3(livingEntityPatch.getOriginal().getLookAngle().x, 0, livingEntityPatch.getOriginal().getLookAngle().z).normalize().scale(1.5);
                            Vec3 la = livingEntityPatch.getOriginal().getLookAngle().normalize();
                            Vec3 shootVec = new Vec3(la.x, Math.max(0, la.y), la.z);

                            if (!livingEntityPatch.getOriginal().onGround())
                            {
                                shootVec = shootVec.add(0, la.y, 0);
                            }
                            Vec3 shootPos = livingEntityPatch.getOriginal().position().add(0, livingEntityPatch.getOriginal().getEyeHeight() - 0.5, 0).add(position);


                            FullPowerDragonCannonBeam projectile = EntityRegistry.FULL_POWER_DRAGON_CANNON.get().create(livingEntityPatch.getOriginal().level());

                            if (projectile != null) {

                                projectile.setPos(shootPos);
                                projectile.shoot(Mth.cos(ang), shootVec.y(), Mth.sin(ang), 4f, 0);
                                if (livingEntityPatch.getArmature() instanceof HumanoidArmature ha) {
                                    OpenMatrix4f jointMatrix = livingEntityPatch.getArmature().getBoundTransformFor(livingEntityPatch.getAnimator().getPose(0.0F), ha.toolR).mulFront(OpenMatrix4f.createTranslation((float) livingEntityPatch.getOriginal().getX(), (float) livingEntityPatch.getOriginal().getY(), (float) livingEntityPatch.getOriginal().getZ()).mulBack(OpenMatrix4f.createRotatorDeg(180.0F, Vec3f.Y_AXIS).mulBack(livingEntityPatch.getModelMatrix(0.0F))));
                                    LogUtils.getLogger().debug(jointMatrix.toTranslationVector().toString());
                                    jointMatrix.translate(new Vec3f(0.0F, -2, 0F));
                                    ((ServerLevel)projectile.level()).sendParticles(ParticleRegistry.DARK_BANG_EXPLOSION.get(), jointMatrix.toTranslationVector().x, jointMatrix.toTranslationVector().y, jointMatrix.toTranslationVector().z, 1 ,0, 0, 0, 0);
                                    projectile.setOrigin(jointMatrix.toTranslationVector().toDoubleVector());
                                    projectile.setPosRaw(jointMatrix.toTranslationVector().x, jointMatrix.toTranslationVector().y, jointMatrix.toTranslationVector().z);
                                }
                                projectile.setOwner(livingEntityPatch.getOriginal());
                                livingEntityPatch.playSound(SoundRegistry.HEAVY_BLAST.get(), -5, -5);
                                livingEntityPatch.getOriginal().level().addFreshEntity(projectile);
                                if (livingEntityPatch instanceof ServerPlayerPatch serverPlayerPatch)
                                {
                                    serverPlayerPatch.getSkill(BattleArtsSkillSlots.BATTLE_STYLE).getDataManager().setDataSync(DatakeyRegistry.BEAM.get(), projectile.getId());
                                }
                            }
                        }, AnimationEvent.Side.SERVER)));

        COSMIC_CHASER = builder.nextAccessor("battle_style/legendary/genesis_wyrm/timeless_chase", access ->
                new OmneriaAttackAnimation(0.05f, access, Armatures.BIPED,
                        new AttackAnimation.Phase(0.25f, 0.0f, 0.25f, 0.3f, 0.5f, 0.5f, Armatures.BIPED.get().rootJoint, ColliderPreset.BIPED_BODY_COLLIDER).addProperty(BattleArtsAttackPhaseProperties.KNOCKBACK_ANGLE, 15.0).addProperty(BattleArtsAttackPhaseProperties.KNOCKBACK_POWER, 3.0).addProperty(AnimationProperty.AttackPhaseProperty.HIT_SOUND, SoundRegistry.IMPERATRICE_PUNCH_IMPACT_M.get()))
                        .addProperty(AnimationProperty.ActionAnimationProperty.MOVE_VERTICAL, true)
                        .addProperty(AnimationProperty.ActionAnimationProperty.NO_GRAVITY_TIME, TimePairList.create(0.0f, 1.0f))
                        .addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (dynamicAnimation, livingEntityPatch, speed, prevElapsedTime, elapsedTime) ->
                        {
                            if (dynamicAnimation instanceof StaticAnimation staticAnimation)
                            {
                                if (elapsedTime >= 0.25f && elapsedTime <= 0.5f)
                                    staticAnimation.addProperty(AnimationProperty.StaticAnimationProperty.POSE_MODIFIER, Animations.ReusableSources.ROOT_X_MODIFIER);
                                else
                                    staticAnimation.removeProperty(AnimationProperty.StaticAnimationProperty.POSE_MODIFIER);
                            }
                            LivingEntity self = livingEntityPatch.getOriginal();
                            if (elapsedTime < 0.25f)
                            {
                                return 1;
                            }
                            else if (elapsedTime >= 0.25f && elapsedTime <= 0.5f)
                            {

                                Vec3 targetPoint;
                                AABB scanBox = AABB.ofSize(self.position(), 4, 4, 4);
                                List<Entity> entities = self.level().getEntities(self, scanBox);
                                if (livingEntityPatch instanceof PlayerPatch<?> playerPatch)
                                {
                                    if (playerPatch.getSkill(BattleArtsSkillSlots.MANA_ART).getDataManager().hasData(DatakeyRegistry.FOCUSED_TARGET.get()))
                                    {
                                        Entity opponent = playerPatch.getOriginal().level().getEntity(playerPatch.getSkill(BattleArtsSkillSlots.MANA_ART).getDataManager().getDataValue(DatakeyRegistry.FOCUSED_TARGET.get()));
                                        if (opponent != null)
                                        {
                                            targetPoint = opponent.position();
                                            if (entities.isEmpty())
                                            {
                                                Vec3 velocity = targetPoint.subtract(self.position()).normalize().scale(4f);
                                                self.setDeltaMovement(velocity);
                                                self.move(MoverType.SELF, new Vec3(0, self.getDeltaMovement().y(), 0));

                                            }
                                            else {

                                                self.setDeltaMovement(Vec3.ZERO);
                                            }
                                            return !entities.isEmpty() ? 1f : 0.025f;
                                        }
                                    }
                                    self.setDeltaMovement(self.getDeltaMovement().normalize());
                                    self.move(MoverType.SELF, self.getLookAngle().normalize().scale(2f));
                                    return 1f;
                                }
                                else
                                {
                                    self.setDeltaMovement(self.getLookAngle().normalize());
                                    self.move(MoverType.SELF, self.getLookAngle().normalize().scale(2f));
                                    return 1;
                                }
                            }
                            else
                            {
                                return 1;
                            }
                        }));

        WALK = builder.nextAccessor("battle_style/legendary/genesis_wyrm/walk", access -> new MovementAnimation(0.1f, true, access, Armatures.BIPED)
                .addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (dynamicAnimation, livingEntityPatch, v, v1, v2) ->
                        v * 2f));

        RUN = builder.nextAccessor("battle_style/legendary/genesis_wyrm/run", access -> new MovementAnimation(0.2f, true, access, Armatures.BIPED)
                .addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (dynamicAnimation, livingEntityPatch, v, v1, v2) ->
                        v));
        WALK_BACK = builder.nextAccessor("battle_style/legendary/genesis_wyrm/walk_back", access -> new MovementAnimation(0.1f, true, access, Armatures.BIPED)
                .addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (dynamicAnimation, livingEntityPatch, v, v1, v2) ->
                        v * 2f));

        WALK_SET = builder.nextAccessor("battle_style/legendary/genesis_wyrm/walk_set", access -> new SelectiveAnimation(
                patch -> {
                    Vec3 look = patch.getOriginal().getLookAngle();
                    Vec3 motion = patch.getOriginal().getDeltaMovement();
                    double d = look.normalize().dot(motion.normalize());
                    if (d > 0.5)
                        return 0;
                    return 1;
                }, access, WALK, WALK_BACK
        ));

        GUARD = builder.nextAccessor("battle_style/legendary/genesis_wyrm/guard", access -> new StaticAnimation(0.1f, true, access, Armatures.BIPED));
        CROUCH = builder.nextAccessor("battle_style/legendary/genesis_wyrm/crouch", access -> new StaticAnimation(0.1f, true, access, Armatures.BIPED));


        GUARD_HIT = builder.nextAccessor("battle_style/legendary/genesis_wyrm/guard_hit", access -> new GuardAnimation(0.1f, access, Armatures.BIPED));
        REFLECTION = builder.nextAccessor("battle_style/legendary/genesis_wyrm/reflect", access -> new GuardAnimation(0.1f, access, Armatures.BIPED)
                .addProperty(AnimationProperty.StaticAnimationProperty.FIXED_HEAD_ROTATION, true));

        AUTO1 = builder.nextAccessor("battle_style/legendary/genesis_wyrm/auto1", access -> new BasicAttackAnimation(
                0.1f, 0.0f, 0.1f, 0.2f, 0.2f, GenesisWyrmColliders.GW_CLAW, Armatures.BIPED.get().handL, access, Armatures.BIPED
        ).addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.HOLD)
                .addProperty(AnimationProperty.AttackPhaseProperty.SOURCE_TAG, Set.of(DamageTypeTags.BYPASSES_RESISTANCE))
                .addProperty(AnimationProperty.AttackPhaseProperty.SWING_SOUND, EpicFightSounds.SWORDMASTER_SWING.get())
                .addProperty(AnimationProperty.AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLADE_HIT.get())
                .addProperty(AnimationProperty.AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLADE)
                .addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, Animations.ReusableSources.CONSTANT_ONE));

        AUTO2 = builder.nextAccessor("battle_style/legendary/genesis_wyrm/auto2", access -> new BasicAttackAnimation(
                0.1f, 0.0f, 0.1f, 0.2f, 0.2f, GenesisWyrmColliders.GW_CLAW, Armatures.BIPED.get().handR, access, Armatures.BIPED
        ).addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.HOLD)
                .addProperty(AnimationProperty.AttackPhaseProperty.SWING_SOUND, EpicFightSounds.SWORDMASTER_SWING.get())
                .addProperty(AnimationProperty.AttackPhaseProperty.SOURCE_TAG, Set.of(DamageTypeTags.BYPASSES_RESISTANCE))

                .addProperty(AnimationProperty.AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLADE_HIT.get())
                .addProperty(AnimationProperty.AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLADE)
                .addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, Animations.ReusableSources.CONSTANT_ONE));

        AUTO3 = builder.nextAccessor("battle_style/legendary/genesis_wyrm/auto3", access -> new OmneriaAttackAnimation(
                0.1f,access, Armatures.BIPED,
                new AttackAnimation.Phase(0.0f, 0.0f, 0.1f, 0.15f, 0.15f, 0.15f, Armatures.BIPED.get().rootJoint, GenesisWyrmColliders.GW_CIRCLE_CLAW)
                        .addProperty(AnimationProperty.AttackPhaseProperty.SOURCE_TAG, Set.of(DamageTypeTags.BYPASSES_RESISTANCE))
                ,
                new AttackAnimation.Phase(0.15f, 0.0f, 0.15f, 0.2f, 0.2f, 0.4f, Armatures.BIPED.get().rootJoint, GenesisWyrmColliders.GW_CIRCLE_CLAW)
                        .addProperty(AnimationProperty.AttackPhaseProperty.SWING_SOUND, EpicFightSounds.SWORDMASTER_SWING.get())
                        .addProperty(AnimationProperty.AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLADE_HIT.get())
                        .addProperty(AnimationProperty.AttackPhaseProperty.SOURCE_TAG, Set.of(DamageTypeTags.BYPASSES_RESISTANCE))


        ).addProperty(BattleArtsAttackPhaseProperties.KNOCKBACK_ANGLE, 25d)
                .addProperty(BattleArtsAttackPhaseProperties.KNOCKBACK_POWER, 3.0d)
                .addProperty(AnimationProperty.AttackPhaseProperty.SWING_SOUND, EpicFightSounds.SWORDMASTER_SWING.get())
                .addProperty(AnimationProperty.AttackPhaseProperty.SOURCE_TAG, Set.of(DamageTypeTags.BYPASSES_RESISTANCE))

                .addProperty(AnimationProperty.AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLADE_HIT.get())
                .addProperty(AnimationProperty.AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLADE)
                .addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.HOLD)
                .addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, Animations.ReusableSources.CONSTANT_ONE));

        AUTO4 = builder.nextAccessor("battle_style/legendary/genesis_wyrm/auto4", access -> new OmneriaAttackAnimation(
                0.1f,access, Armatures.BIPED,
                new AttackAnimation.Phase(0.0f, 0.0f, 0.1f, 0.15f, 0.15f, 0.15f, Armatures.BIPED.get().rootJoint, GenesisWyrmColliders.GW_CIRCLE_CLAW_BURST)
                        .addProperty(AnimationProperty.AttackPhaseProperty.SOURCE_TAG, Set.of(DamageTypeTags.BYPASSES_RESISTANCE))
                ,
                new AttackAnimation.Phase(0.15f, 0.0f, 0.15f, 0.2f, 0.4f, 0.4f, Armatures.BIPED.get().rootJoint, GenesisWyrmColliders.GW_CIRCLE_CLAW_BURST)
                        .addProperty(AnimationProperty.AttackPhaseProperty.SWING_SOUND, EpicFightSounds.SWORDMASTER_SWING.get())
                        .addProperty(AnimationProperty.AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLADE_HIT.get())
                        .addProperty(AnimationProperty.AttackPhaseProperty.SOURCE_TAG, Set.of(DamageTypeTags.BYPASSES_RESISTANCE))


        ).addProperty(BattleArtsAttackPhaseProperties.KNOCKBACK_ANGLE, 25d, 1)
                .addProperty(BattleArtsAttackPhaseProperties.KNOCKBACK_POWER, 3.0d, 1)
                .addProperty(AnimationProperty.AttackPhaseProperty.SWING_SOUND, EpicFightSounds.SWORDMASTER_SWING.get())
                .addProperty(AnimationProperty.AttackPhaseProperty.SOURCE_TAG, Set.of(DamageTypeTags.BYPASSES_RESISTANCE))

                .addProperty(AnimationProperty.AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLADE_HIT.get())
                .addProperty(AnimationProperty.AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLADE)
                .addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.HOLD)
                .addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, Animations.ReusableSources.CONSTANT_ONE));

        MELEE_COUNTER = builder.nextAccessor("battle_style/legendary/genesis_wyrm/melee_counter", access -> new OmneriaAttackAnimation(
                0.1f, 0.0f, 0.05f, 0.2f, 0.5f, GenesisWyrmColliders.GW_CLAW, Armatures.BIPED.get().handR, access, Armatures.BIPED
        )
                .addProperty(AnimationProperty.AttackPhaseProperty.IMPACT_MODIFIER, ValueModifier.setter(2))
                .addProperty(BattleArtsAttackPhaseProperties.KNOCKBACK_ANGLE, 5d)
                .addProperty(BattleArtsAttackPhaseProperties.KNOCKBACK_POWER, 1.2)
                .addProperty(AnimationProperty.AttackPhaseProperty.SOURCE_TAG, Set.of(DamageTypeTags.BYPASSES_RESISTANCE))
                .addProperty(AnimationProperty.AttackPhaseProperty.SWING_SOUND, EpicFightSounds.SWORDMASTER_SWING.get())
                .addProperty(AnimationProperty.AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLADE_HIT.get())
                .addProperty(AnimationProperty.AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLADE)
                .addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, Animations.ReusableSources.CONSTANT_ONE));

        RANGED_COUNTER = builder.nextAccessor("battle_style/legendary/genesis_wyrm/ranged_counter", access -> new OmneriaAttackAnimation(
                0.1f, 0.3f, 0.6f, 0.7f, 1.5f, GenesisWyrmColliders.GW_CLAW_CLEAVE, Armatures.BIPED.get().rootJoint, access, Armatures.BIPED
        )
                .addProperty(AnimationProperty.AttackPhaseProperty.IMPACT_MODIFIER, ValueModifier.setter(2))
                .addProperty(BattleArtsAttackPhaseProperties.KNOCKBACK_ANGLE, 35d)
                .addProperty(BattleArtsAttackPhaseProperties.KNOCKBACK_POWER, 1.4)
                .addProperty(AnimationProperty.AttackPhaseProperty.SOURCE_TAG, Set.of(DamageTypeTags.BYPASSES_RESISTANCE))
                .addProperty(AnimationProperty.AttackPhaseProperty.SWING_SOUND, EpicFightSounds.WHOOSH_BIG.get())
                .addProperty(AnimationProperty.AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLUNT_HIT_HARD.get())
                .addProperty(AnimationProperty.AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLADE)
                .addProperty(AnimationProperty.AttackAnimationProperty.FIXED_MOVE_DISTANCE, true)
                .addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, Animations.ReusableSources.CONSTANT_ONE)
                .addEvents(
                        AnimationEvent.InTimeEvent.create(0.2f, Animations.ReusableSources.PLAY_SOUND, AnimationEvent.Side.CLIENT).params(EpicFightSounds.WHOOSH_ROD.get()),
                        AnimationEvent.InTimeEvent.create(0.3f, Animations.ReusableSources.PLAY_SOUND, AnimationEvent.Side.CLIENT).params(EpicFightSounds.WHOOSH_ROD.get()),
                        AnimationEvent.InTimeEvent.create(0.4f, Animations.ReusableSources.PLAY_SOUND, AnimationEvent.Side.CLIENT).params(EpicFightSounds.WHOOSH_ROD.get()),
                        AnimationEvent.InTimeEvent.create(0.5f, Animations.ReusableSources.PLAY_SOUND, AnimationEvent.Side.CLIENT).params(EpicFightSounds.WHOOSH_ROD.get())
                ));

        AUTO5 = builder.nextAccessor("battle_style/legendary/genesis_wyrm/auto5", access -> new OmneriaAttackAnimation(
                0.1f, 0.0f, 0.1f, 0.2f, 1f, GenesisWyrmColliders.GW_CLAW_CLEAVE, Armatures.BIPED.get().rootJoint, access, Armatures.BIPED
        )
                .addProperty(BattleArtsAttackPhaseProperties.KNOCKBACK_ANGLE, 45d)
                .addProperty(BattleArtsAttackPhaseProperties.KNOCKBACK_POWER, 3.0d)
                .addProperty(AnimationProperty.AttackPhaseProperty.IMPACT_MODIFIER, ValueModifier.setter(4))
                .addProperty(AnimationProperty.AttackPhaseProperty.SOURCE_TAG, Set.of(DamageTypeTags.BYPASSES_RESISTANCE))

                .addProperty(AnimationProperty.AttackPhaseProperty.SWING_SOUND, EpicFightSounds.WHOOSH_SHARP.get())
                .addProperty(AnimationProperty.AttackPhaseProperty.HIT_SOUND, SoundRegistry.IMPERATRICE_HIT_FINISHER.get())
                .addProperty(AnimationProperty.AttackPhaseProperty.PARTICLE, EpicFightParticles.HIT_BLADE)
                .addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, Animations.ReusableSources.CONSTANT_ONE));

        LEG_AUTO1 = builder.nextAccessor("battle_style/legendary/genesis_wyrm/leg_auto1", access -> new OmneriaAttackAnimation(
                0.0f, access, Armatures.BIPED,
                new AttackAnimation.Phase(0.0f, 0.0f, 0.05f, 0.1f, 0.1f, 0.1f, Armatures.BIPED.get().legR, ColliderPreset.FIST),
                new AttackAnimation.Phase(0.1f, 0.0f, 0.15f, 0.2f, 0.2f, 0.2f, Armatures.BIPED.get().legR, ColliderPreset.FIST),
                new AttackAnimation.Phase(0.2f, 0.0f, 0.25f, 0.3f, 0.3f, 0.3f, Armatures.BIPED.get().legR, ColliderPreset.FIST),
                new AttackAnimation.Phase(0.3f, 0.0f, 0.35f, 0.4f, 0.4f, 0.4f, Armatures.BIPED.get().legR, ColliderPreset.FIST),
                new AttackAnimation.Phase(0.4f, 0.0f, 0.45f, 0.5f, 0.6f, 1.0f, Armatures.BIPED.get().legR, ColliderPreset.FIST)
                        .addProperty(BattleArtsAttackPhaseProperties.KNOCKBACK_ANGLE, 25d)
                        .addProperty(BattleArtsAttackPhaseProperties.KNOCKBACK_POWER, 3.0d)
        ).addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, Animations.ReusableSources.CONSTANT_ONE));

        LEG_AUTO2 = builder.nextAccessor("battle_style/legendary/genesis_wyrm/leg_auto2", access -> new OmneriaAttackAnimation(
                0.1f, 0.0f, 0.1f, 0.2f, 0.4f, ColliderPreset.FIST, Armatures.BIPED.get().legL, access, Armatures.BIPED
        ).addProperty(BattleArtsAttackPhaseProperties.KNOCKBACK_ANGLE, 25d)
                .addProperty(BattleArtsAttackPhaseProperties.KNOCKBACK_POWER, 3.0d)
                .addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.NEUTRALIZE)
                .addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, Animations.ReusableSources.CONSTANT_ONE));

        BLAST_AUTO1 = builder.nextAccessor("battle_style/legendary/genesis_wyrm/blast_auto1", access -> new BasicAttackAnimation(
                0.1f, 0.0f, 0.0f, 0.35f, 0.5f, ColliderPreset.FIST, Armatures.BIPED.get().handR, access, Armatures.BIPED
        ).addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.HOLD)
                .addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, Animations.ReusableSources.CONSTANT_ONE)
                .addEvents(AnimationEvent.InTimeEvent.create(0.35f, ReusableEvents::FIRE_DRAGON_SHOT, AnimationEvent.Side.BOTH).params(Armatures.BIPED.get().handR, 0.35f)));



        BLAST_AUTO2 = builder.nextAccessor("battle_style/legendary/genesis_wyrm/blast_auto2", access -> new BasicAttackAnimation(
                0.1f, 0.0f, 0.0f, 0.05f, 0.15f, ColliderPreset.FIST, Armatures.BIPED.get().handR, access, Armatures.BIPED
        ).addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.HOLD)
                .addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, Animations.ReusableSources.CONSTANT_ONE)
                .addEvents(AnimationEvent.InTimeEvent.create(0.0f, ReusableEvents::FIRE_DRAGON_SHOT, AnimationEvent.Side.BOTH).params(Armatures.BIPED.get().handR, 0.0f)));

        HEAVY_AUTO1 = builder.nextAccessor("battle_style/legendary/genesis_wyrm/heavy_auto1", access -> new OmneriaAttackAnimation(
                0.1f, 0.0f, 0.15f, 0.35f, 0.7f, ColliderPreset.FIST, Armatures.BIPED.get().handL, access, Armatures.BIPED
        ).addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.HOLD)
                .addProperty(BattleArtsAttackPhaseProperties.HITSTUN_TICKS, 3)
                .addProperty(BattleArtsAttackPhaseProperties.ENDLAG_TICKS, 2)
                .addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.25f))
                .addProperty(AnimationProperty.AttackPhaseProperty.IMPACT_MODIFIER, ValueModifier.setter(2))
                .addProperty(AnimationProperty.AttackPhaseProperty.HIT_SOUND, SoundRegistry.IMPERATRICE_PUNCH_IMPACT_M.get())
                .addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (dynamicAnimation, livingEntityPatch, v, v1, v2) -> 1f));

        HEAVY_AUTO2 = builder.nextAccessor("battle_style/legendary/genesis_wyrm/heavy_auto2", access -> new OmneriaAttackAnimation(
                0.05f, 0.0f, 0.15f, 0.3f, 1f, ColliderPreset.FIST, Armatures.BIPED.get().handR, access, Armatures.BIPED
        ).addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.HOLD)
                .addProperty(BattleArtsAttackPhaseProperties.HITSTUN_TICKS, 3)
                .addProperty(BattleArtsAttackPhaseProperties.ENDLAG_TICKS, 2)
                .addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.25f))
                .addProperty(AnimationProperty.AttackPhaseProperty.IMPACT_MODIFIER, ValueModifier.setter(2))
                .addProperty(AnimationProperty.AttackPhaseProperty.HIT_SOUND, SoundRegistry.IMPERATRICE_PUNCH_IMPACT_M.get())
                .addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (dynamicAnimation, livingEntityPatch, v, v1, v2) -> 1f));

        HEAVY_AUTO3 = builder.nextAccessor("battle_style/legendary/genesis_wyrm/heavy_auto3", access -> new OmneriaAttackAnimation(
                0.05f, 0.0f, 0.1f, 0.25f, 1.65f, ColliderPreset.FIST, Armatures.BIPED.get().legL, access, Armatures.BIPED
        ).addProperty(BattleArtsAttackPhaseProperties.KNOCKBACK_ANGLE, 70d)
                .addProperty(AnimationProperty.AttackPhaseProperty.DAMAGE_MODIFIER, ValueModifier.multiplier(0.25f))
                .addProperty(AnimationProperty.AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLUNT_HIT_HARD.get())
                .addProperty(BattleArtsAttackPhaseProperties.KNOCKBACK_POWER, 1d)
                .addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (dynamicAnimation, livingEntityPatch, v, v1, v2) -> 1f));

        DARK_BANG = builder.nextAccessor("battle_style/legendary/genesis_wyrm/dark_bang", access -> new OmneriaAttackAnimation(
                0.05f, 1.0f, 0.1f, 0.25f, 2f, ColliderPreset.FIST, Armatures.BIPED.get().handR, access, Armatures.BIPED
        ).addProperty(BattleArtsAttackPhaseProperties.KNOCKBACK_ANGLE, 70d)
                .addProperty(AnimationProperty.AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLUNT_HIT_HARD.get())
                .addProperty(BattleArtsAttackPhaseProperties.KNOCKBACK_POWER, 1d)
                .addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (dynamicAnimation, livingEntityPatch, v, v1, v2) -> 1f)
                .addEvents(AnimationEvent.InTimeEvent.create(0.6f, (livingEntityPatch, assetAccessor, animationParameters) ->
                {

                    if (!livingEntityPatch.isLogicalClient()) {
                        if (livingEntityPatch.getArmature() instanceof HumanoidArmature ha) {
                            OpenMatrix4f jointMatrix = livingEntityPatch.getArmature().getBoundTransformFor(livingEntityPatch.getAnimator().getPose(0.0F), ha.handR).mulFront(OpenMatrix4f.createTranslation((float) livingEntityPatch.getOriginal().getX(), (float) livingEntityPatch.getOriginal().getY(), (float) livingEntityPatch.getOriginal().getZ()).mulBack(OpenMatrix4f.createRotatorDeg(180.0F, Vec3f.Y_AXIS).mulBack(livingEntityPatch.getModelMatrix(0.0F))));
                            LogUtils.getLogger().debug(jointMatrix.toTranslationVector().toString());
                            ((ServerLevel) livingEntityPatch.getOriginal().level()).sendParticles(ParticleRegistry.DRACONIC_BLAST_FLASH.get(), jointMatrix.toTranslationVector().x, jointMatrix.toTranslationVector().y, jointMatrix.toTranslationVector().z, 1, 0, 0, 0, 0);
                        }
                        livingEntityPatch.playSound(SoundRegistry.DARK_BANG_MANIFEST.get(), 1f, 20.0f, 20.0f);
                        livingEntityPatch.playSound(SoundRegistry.DARK_BANG_CHARGE.get(), 1f, 20.0f, 20.0f);

                    }
                }, AnimationEvent.Side.SERVER), AnimationEvent.InTimeEvent.create(1.0f, (livingEntityPatch, assetAccessor, animationParameters) ->
                {
                    float ang = (float) ((livingEntityPatch.getYRot()+90)/180 * Math.PI);

                    Vec3 position = new Vec3(livingEntityPatch.getOriginal().getLookAngle().x, 0, livingEntityPatch.getOriginal().getLookAngle().z).normalize().scale(1.5);
                    Vec3 shootVec = new Vec3(Math.cos(ang), 0 , Math.sin(ang));
                    Vec3 shootPos = livingEntityPatch.getOriginal().position().add(0, livingEntityPatch.getOriginal().getEyeHeight() - 0.5, 0).add(position);


                    DarkBangProjectile projectile = EntityRegistry.DARK_BANG.get().create(livingEntityPatch.getOriginal().level());

                    if (projectile != null) {

                        projectile.setPos(shootPos);
                        projectile.shoot(shootVec.x(), 0, shootVec.z(), 4.2f, 0);
                        projectile.setCountdown(0);
                        projectile.setDamageSource(livingEntityPatch.getDamageSource(access, InteractionHand.MAIN_HAND));
                        if (livingEntityPatch.getArmature() instanceof HumanoidArmature ha) {
                            Vec3 lv = livingEntityPatch.getOriginal().getLookAngle();
                            OpenMatrix4f jointMatrix = livingEntityPatch.getArmature().getBoundTransformFor(livingEntityPatch.getAnimator().getPose(0.0F), ha.handR).mulFront(OpenMatrix4f.createTranslation((float) livingEntityPatch.getOriginal().getX(), (float) livingEntityPatch.getOriginal().getY(), (float) livingEntityPatch.getOriginal().getZ()).mulBack(OpenMatrix4f.createRotatorDeg(180.0F, Vec3f.Y_AXIS).mulBack(livingEntityPatch.getModelMatrix(0.0F))));
                            jointMatrix.translate(0, 0.5f, 0);
                            projectile.setPosRaw(jointMatrix.toTranslationVector().x, jointMatrix.toTranslationVector().y, jointMatrix.toTranslationVector().z);
                            projectile.shoot(lv.x, lv.y, lv.z, 4.2f, 0);

                        }
                        projectile.setOwner(livingEntityPatch.getOriginal());
                        livingEntityPatch.playSound(SoundRegistry.HEAVY_BLAST.get(), 0, 0);
                        livingEntityPatch.getOriginal().level().addFreshEntity(projectile);

                    }
                }, AnimationEvent.Side.SERVER))
                .addState(EntityState.CAN_SKILL_EXECUTION, false));

        UMBRAL_HAMMER = builder.nextAccessor("battle_style/legendary/genesis_wyrm/umbral_hammer", access -> new OmneriaAttackAnimation(
                0.05f, 0.0f, 0.2f, 0.35f, 1.65f, ColliderPreset.BATTOJUTSU_DASH, Armatures.BIPED.get().rootJoint, access, Armatures.BIPED
        ).addProperty(BattleArtsAttackPhaseProperties.KNOCKBACK_ANGLE, 35d)
                .addProperty(AnimationProperty.AttackPhaseProperty.HIT_SOUND, EpicFightSounds.BLUNT_HIT_HARD.get())
                .addProperty(BattleArtsAttackPhaseProperties.KNOCKBACK_POWER, 1d)
                .addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (dynamicAnimation, livingEntityPatch, speed, prevElapsedTime, elapsedTime) ->
                {
                    if (elapsedTime >= 0.1F && elapsedTime < 0.2F) {
                        float dpx = (float) livingEntityPatch.getOriginal().getX();
                        float dpy = (float) livingEntityPatch.getOriginal().getY();
                        float dpz = (float) livingEntityPatch.getOriginal().getZ();

                        for(BlockState block = livingEntityPatch.getOriginal().level().getBlockState(new BlockPos.MutableBlockPos(dpx, dpy, dpz)); (block.getBlock() instanceof BushBlock || block.isAir()) && !block.is(Blocks.VOID_AIR); block = livingEntityPatch.getOriginal().level().getBlockState(new BlockPos.MutableBlockPos(dpx, dpy, dpz))) {
                            --dpy;
                        }

                        LivingEntity livingentity = livingEntityPatch.getOriginal();
                        Vec3f direction = new Vec3f(4F, -0F, 0.0F);
                        OpenMatrix4f rotation = new OpenMatrix4f().rotate(-(float)Math.toRadians(livingEntityPatch.getOriginal().yBodyRotO + 90.0F), new Vec3f(0.0F, 1.0F, 0.0F));
                        OpenMatrix4f.transform3v(rotation, direction, direction);
                        AABB box = AABB.ofSize(livingentity.getPosition(1.0F), 3.0F, 2.0F, 3.0F);
                        List<Entity> entities = livingentity.level().getEntities(livingentity, box);
                        if (entities.isEmpty()) {
                            livingentity.move(MoverType.SELF, direction.toDoubleVector());

                        }
                        if (!livingentity.level().isClientSide())
                        {
                            ((ServerLevel)livingentity.level()).sendParticles(ParticleRegistry.DRACONIC_BLAST_IMPACT.get(), livingentity.getX(), livingentity.getY(), livingentity.getZ(), 1, 0, 0, 0, 0);
                        }
                        return 1;
                    } else {
                        return 1;
                    }
                }));

        DRAGON_THROW = builder.nextAccessor("battle_style/legendary/genesis_wyrm/dragon_throw_attack", access ->
                new OmneriaAttackAnimation(0.0f, 0.0f, 1.2f, 1.6f, 2.0f, GenesisWyrmColliders.GW_CIRCLE_CLAW_BURST, Armatures.BIPED.get().rootJoint, access, Armatures.BIPED)
                        .addProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, StunType.NONE)
                        .addState(EntityState.ATTACKING, true)
                        .addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (dynamicAnimation, livingEntityPatch, v, v1, v2) -> 1f)
                        .addEvents(AnimationEvent.InTimeEvent.create(0.2f, Animations.ReusableSources.PLAY_SOUND, AnimationEvent.Side.SERVER).params(EpicFightSounds.WHOOSH_ROD.get()),
                                AnimationEvent.InTimeEvent.create(0.4f, Animations.ReusableSources.PLAY_SOUND, AnimationEvent.Side.SERVER).params(EpicFightSounds.WHOOSH_ROD.get()),
                                AnimationEvent.InTimeEvent.create(0.6f, Animations.ReusableSources.PLAY_SOUND, AnimationEvent.Side.SERVER).params(EpicFightSounds.WHOOSH_ROD.get()),
                                AnimationEvent.InTimeEvent.create(0.8f, Animations.ReusableSources.PLAY_SOUND, AnimationEvent.Side.SERVER).params(EpicFightSounds.WHOOSH_ROD.get()),
                                AnimationEvent.InTimeEvent.create(0.95f, Animations.ReusableSources.PLAY_SOUND, AnimationEvent.Side.SERVER).params(EpicFightSounds.WHOOSH_ROD.get()),
                                AnimationEvent.InTimeEvent.create(1.05f, Animations.ReusableSources.PLAY_SOUND, AnimationEvent.Side.SERVER).params(EpicFightSounds.WHOOSH_ROD.get()),
                                AnimationEvent.InTimeEvent.create(1.15f, Animations.ReusableSources.PLAY_SOUND, AnimationEvent.Side.SERVER).params(EpicFightSounds.WHOOSH_ROD.get()),
                                AnimationEvent.InTimeEvent.create(1.25f, Animations.ReusableSources.PLAY_SOUND, AnimationEvent.Side.SERVER).params(EpicFightSounds.WHOOSH_ROD.get()),
                                AnimationEvent.InTimeEvent.create(1.35f, Animations.ReusableSources.PLAY_SOUND, AnimationEvent.Side.SERVER).params(EpicFightSounds.WHOOSH_ROD.get()),
                                AnimationEvent.InTimeEvent.create(1.45f, Animations.ReusableSources.PLAY_SOUND, AnimationEvent.Side.SERVER).params(EpicFightSounds.WHOOSH_ROD.get()),
                                AnimationEvent.InTimeEvent.create(1.55f, Animations.ReusableSources.PLAY_SOUND, AnimationEvent.Side.SERVER).params(EpicFightSounds.WHOOSH_ROD.get())));



        DRAGON_THROW_VICTIM_BIPED = builder.nextAccessor("battle_style/legendary/genesis_wyrm/victim/dragon_throw_stun", access -> new LongHitAnimation(
                0.2f, access, Armatures.BIPED
        ).addProperty(AnimationProperty.ActionAnimationProperty.MOVE_VERTICAL, true)
                .addProperty(AnimationProperty.ActionAnimationProperty.ActionAnimationProperty.MOVE_TIME, TimePairList.create(1.4f, 2.0f))
                .addProperty(AnimationProperty.ActionAnimationProperty.IS_DEATH_ANIMATION, true)
                .addProperty(AnimationProperty.ActionAnimationProperty.REMOVE_DELTA_MOVEMENT, true)
                .addProperty(AnimationProperty.StaticAnimationProperty.NO_PHYSICS, true)
                .addState(OmneriaEntityStates.CAN_BE_PUSHED, false)
                .addState(EntityState.ATTACKING, true));

        FOCUS_AUTO1 = builder.nextAccessor("battle_style/legendary/genesis_wyrm/focused_auto1", access -> new BasicAttackAnimation(
                0.05f, 0.05f, 0.05f, 0.05f, ColliderPreset.FIST, Armatures.BIPED.get().handL, access, Armatures.BIPED
        ).addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (dynamicAnimation, livingEntityPatch, v, v1, v2) -> 1));

        FOCUS_AUTO2 = builder.nextAccessor("battle_style/legendary/genesis_wyrm/focused_auto2", access -> new BasicAttackAnimation(
                0.05f, 0.05f, 0.05f, 0.05f, ColliderPreset.FIST, Armatures.BIPED.get().handR, access, Armatures.BIPED
        ).addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (dynamicAnimation, livingEntityPatch, v, v1, v2) -> 1));

        DRAGON_THROW_TRY = builder.nextAccessor("battle_style/legendary/genesis_wyrm/dragon_throw_try", access ->
                new OmneriaGrabAnimation(0.2f, 0.0f, 0.4f, 0.5f, 1.0f, ColliderPreset.BIPED_BODY_COLLIDER, Armatures.BIPED.get().rootJoint, access, Armatures.BIPED, DRAGON_THROW_VICTIM_BIPED)

                        .addProperty(AnimationProperty.AttackPhaseProperty.SOURCE_TAG, Set.of(OmneriaDamageTypes.GRAB, EpicFightDamageTypeTags.UNBLOCKALBE))
                        .addProperty(AnimationProperty.ActionAnimationProperty.COORD_START_KEYFRAME_INDEX, 1)
                .addProperty(AnimationProperty.ActionAnimationProperty.NO_GRAVITY_TIME, TimePairList.create(0.15F, 0.35F))
                .addProperty(AnimationProperty.ActionAnimationProperty.DEST_LOCATION_PROVIDER, MoveCoordFunctions.SYNCHED_TARGET_ENTITY_LOCATION_VARIABLE)
                        .addProperty(AnimationProperty.StaticAnimationProperty.PLAY_SPEED_MODIFIER, (dynamicAnimation, livingEntityPatch, v, v1, v2) -> 1f));
    }

    public static final AnimationProperty.PoseModifier VELOCITY_X_MODIFIER = (self, pose, entitypatch, time, partialTicks) -> {
        Vec3 d = entitypatch.getOriginal().getDeltaMovement();
        float pitch = (float) -Math.toDegrees(Math.atan2(d.y, Math.sqrt(d.x * d.x + d.z * d.z)));
        LogUtils.getLogger().debug("pitch: " + pitch);
        JointTransform chest = pose.orElseEmpty("Root");
        chest.frontResult(JointTransform.rotation(QuaternionUtils.XP.rotationDegrees(-pitch)), OpenMatrix4f::mulAsOriginInverse);
    };

    private Vec3 rotateVector(Vec3 v, double degreesPerTick) {
        double radians = Math.toRadians(degreesPerTick);
        double cos = Math.cos(radians);
        double sin = Math.sin(radians);
        double x = v.x * cos - v.z * sin;
        double z = v.x * sin + v.z * cos;
        return new Vec3(x, v.y, z);
    }
}
