package net.forixaim.omneria.world.entity.patches;

import com.google.common.collect.Lists;
import com.yesman.epicparcool.EpicParCool;
import com.yesman.epicparcool.ParcoolLivingMotions;
import net.forixaim.omneria.animations.entity.CharlemagneAnimations;
import net.forixaim.omneria.events.advanced_bosses.DamageDealtEvent;
import net.forixaim.omneria.world.entity.FacialLivingMotions;
import net.forixaim.omneria.world.entity.charlemagne.Charlemagne;
import net.forixaim.omneria.world.entity.charlemagne.ai.CharlemagneAttackString;
import net.forixaim.omneria.world.entity.charlemagne.ai.CharlemagneBrain;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.ModList;
import yesman.epicfight.api.animation.Animator;
import yesman.epicfight.api.animation.LivingMotions;
import yesman.epicfight.api.client.model.Meshes;
import yesman.epicfight.api.client.model.SoftBodyTranslatable;
import yesman.epicfight.api.client.physics.cloth.ClothColliderPresets;
import yesman.epicfight.api.client.physics.cloth.ClothSimulatable;
import yesman.epicfight.api.client.physics.cloth.ClothSimulator;
import yesman.epicfight.api.physics.PhysicsSimulator;
import yesman.epicfight.api.physics.SimulationTypes;
import yesman.epicfight.api.utils.AttackResult;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.gameasset.Armatures;
import yesman.epicfight.world.capabilities.entitypatch.Factions;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SuppressWarnings("unchecked")
public class CharlemagnePatch extends FriendlyHumanoidNPCPatch<Charlemagne> implements ClothSimulatable
{

	private static final UUID CloseGapUUID = UUID.fromString("eb18c5eb-19bf-4a71-9398-b49d9b510217");
	private static final List<CharlemagneAttackString> bossAttackString = Lists.newArrayList();

	public CharlemagneBrain brain;
	private final ClothSimulator clothSimulator = new ClothSimulator();


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
		if (entityIn.level().isClientSide())
			initDefaultCape(this);
		entityIn.patch = this;
		super.onConstructed(entityIn);
		brain = new CharlemagneBrain(entityIn, this);
		entityIn.moddedBrain = brain;
	}


	@OnlyIn(Dist.CLIENT)
	public static void initDefaultCape(CharlemagnePatch playerpatch) {
		SoftBodyTranslatable.TRACKING_SIMULATION_SUBJECTS.add(playerpatch);
		playerpatch.getClothSimulator().runWhen(
				ClothSimulator.PLAYER_CLOAK
				, Meshes.CAPE_DEFAULT
				, ClothSimulator.ClothObjectBuilder.create()
						.parentJoint(Armatures.BIPED.get().torso)
						.putAll(ClothColliderPresets.BIPED)
				, () -> !playerpatch.getOriginal().isInvisible()
		);
	}

	@Override
	public void initAnimator(Animator animator)
	{
		animator.addLivingAnimation(LivingMotions.IDLE, CharlemagneAnimations.IDLE);
		animator.addLivingAnimation(LivingMotions.WALK, Animations.BIPED_WALK);
		animator.addLivingAnimation(LivingMotions.RUN, Animations.BIPED_RUN);
		animator.addLivingAnimation(LivingMotions.DEATH, Animations.BIPED_DEATH);
		animator.addLivingAnimation(FacialLivingMotions.CHARLEMAGNE_NEUTRAL, CharlemagneAnimations.FACE_NEUTRAL);
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
				if (ModList.get().isLoaded(EpicParCool.MODID) && this.getOriginal().walkAnimation.speed() > 1.2f)
					this.currentLivingMotion = ParcoolLivingMotions.FAST_RUN;
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
		else if (this.brain.isBlocking())
		{
			this.currentLivingMotion = LivingMotions.BLOCK;
		}
		else
		{
			this.currentLivingMotion = LivingMotions.FALL;
		}

        this.currentCompositeMotion = FacialLivingMotions.CHARLEMAGNE_NEUTRAL;
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

	@Override
	public ClothSimulator getClothSimulator() {
		return this.clothSimulator;
	}

	@Override
	public Vec3 getAccurateCloakLocation(float partialFrame) {
		if (partialFrame < 0.0F) {
			partialFrame = 1.0F - partialFrame;

			double x = Mth.lerp(partialFrame, this.original.xCloakO, this.original.xCloakO) - Mth.lerp(partialFrame, this.original.xo, this.original.xo);
			double y = Mth.lerp(partialFrame, this.original.yCloakO, this.original.yCloakO) - Mth.lerp(partialFrame, this.original.yo, this.original.yo);
			double z = Mth.lerp(partialFrame, this.original.zCloakO, this.original.zCloakO) - Mth.lerp(partialFrame, this.original.zo, this.original.zo);

			return new Vec3(x, y, z);
		} else {
			double x = Mth.lerp(partialFrame, this.original.xCloakO, this.original.xCloak) - Mth.lerp(partialFrame, this.original.xo, this.original.getX());
			double y = Mth.lerp(partialFrame, this.original.yCloakO, this.original.yCloak) - Mth.lerp(partialFrame, this.original.yo, this.original.getY());
			double z = Mth.lerp(partialFrame, this.original.zCloakO, this.original.zCloak) - Mth.lerp(partialFrame, this.original.zo, this.original.getZ());

			return new Vec3(x, y, z);
		}
	}

	@Override
	public Vec3 getAccuratePartialLocation(float partialFrame) {
		if (partialFrame < 0.0F) {
			partialFrame = 1.0F + partialFrame;

			double x = Mth.lerp(partialFrame, this.getOriginal().xOld, this.original.xOld);
			double y = Mth.lerp(partialFrame, this.original.yOld, this.original.yOld);
			double z = Mth.lerp(partialFrame, this.original.zOld, this.original.zOld);

			return new Vec3(x, y, z);
		} else {
			double x = Mth.lerp(partialFrame, this.original.xOld, this.original.getX());
			double y = Mth.lerp(partialFrame, this.original.yOld, this.original.getY());
			double z = Mth.lerp(partialFrame, this.original.zOld, this.original.getZ());

			return new Vec3(x, y, z);
		}
	}

	@Override
	public Vec3 getObjectVelocity() {
		return new Vec3(this.original.getX() - this.original.xOld, this.original.getY() - this.original.yOld, this.original.getZ() - this.original.zOld);
	}

	@Override
	public float getAccurateYRot(float partialFrame) {
		if (partialFrame < 0.0F) {
			partialFrame = 1.0F + partialFrame;

			return Mth.rotLerp(partialFrame, this.getYRotO(), this.getYRotO());
		} else {
			return Mth.rotLerp(partialFrame, this.getYRotO(), this.getYRot());
		}
	}

	@Override
	public float getYRotDelta(float partialFrame) {
		if (partialFrame < 0.0F) {
			partialFrame = 1.0F + partialFrame;

			return Mth.rotLerp(partialFrame, this.getYRotO(), this.getYRotO()) - this.getYRotO();
		} else {
			return Mth.rotLerp(partialFrame, this.getYRotO(), this.getYRot()) - this.getYRotO();
		}
	}

	@Override
	public boolean invalid() {
		return this.original.isRemoved();
	}

	@Override
	public float getScale() {
		return 1f;
	}

	@Override
	public Animator getSimulatableAnimator() {
		return this.animator;
	}

	@Override
	public float getGravity() {
		return this.getOriginal().isUnderWater() ? 0.98F : 9.8F;
	}

	@Override
	public <SIM extends PhysicsSimulator<?, ?, ?, ?, ?>> Optional<SIM> getSimulator(SimulationTypes<?, ?, ?, ?, ?, SIM> simulationType) {
		if (simulationType == SimulationTypes.CLOTH) {
			return Optional.of((SIM)this.clothSimulator);
		}

		return Optional.empty();
	}
}
