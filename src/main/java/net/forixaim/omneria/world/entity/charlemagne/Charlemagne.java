package net.forixaim.omneria.world.entity.charlemagne;

import com.google.common.collect.Lists;
import com.mojang.logging.LogUtils;
import mekanism.common.Mekanism;
import mekanism.common.capabilities.Capabilities;
import net.forixaim.omneria.VisitorsOfOmneria;
import net.forixaim.omneria.client.ui.screens.DialogueBuilder;
import net.forixaim.omneria.registry.SoundRegistry;
import net.forixaim.omneria.world.entity.charlemagne.ai.CharlemagneBrain;
import net.forixaim.omneria.world.entity.charlemagne.ai.CharlemagneMode;
import net.forixaim.omneria.world.entity.charlemagne.ai.goal.PlayerConversationGoal;
import net.forixaim.omneria.world.entity.patches.CharlemagnePatch;
import net.forixaim.omneria.world.entity.special_tags.IRadiationImmune;
import net.forixaim.omneria.world.entity.types.AbstractFriendlyNPC;
import net.forixaim.omneria.world.entity.types.plugins.DialogueNPC;
import net.forixaim.omneria.world.entity.types.plugins.SoundBasedDialogueNPC;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.fml.ModList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Charlemagne extends AbstractFriendlyNPC implements IRadiationImmune, SoundBasedDialogueNPC
{

	public CharlemagnePatch patch;
	public CharlemagneBrain moddedBrain;
	public Player conversingPlayer;
	public double xCloakO;
	public double yCloakO;
	public double zCloakO;
	public double xCloak;
	public double yCloak;
	public double zCloak;
	private final SimpleContainer charlemagneInventory = new SimpleContainer(8);
	//For debugging purposes, the entity will be set to a stationary armor stand.
	public final TargetingConditions defConditions = TargetingConditions.forCombat().range(this.getAttributeValue(Attributes.FOLLOW_RANGE)).selector(pred -> pred instanceof Enemy);
	public final TargetingConditions playerConditions = TargetingConditions.forNonCombat().range(this.getAttributeValue(Attributes.FOLLOW_RANGE)).selector(pred -> pred instanceof Player);

	private static final List<MobEffect> onlyAffectedEffects = Lists.newArrayList(
			MobEffects.ABSORPTION,
			MobEffects.DAMAGE_BOOST,
			MobEffects.DAMAGE_RESISTANCE,
			MobEffects.REGENERATION,
			MobEffects.HEAL,
			MobEffects.MOVEMENT_SPEED
	);

	void followPath(List<Vec3> path, int index, Entity entity, double speed) {
		Vec3 target = path.get(index);
		Vec3 dir = target.subtract(entity.position());
		Vec3 velocity = dir.normalize().scale(speed);
		entity.setDeltaMovement(velocity.x, entity.getDeltaMovement().y, velocity.z);
	}

	public ResourceLocation getCapeTexture()
	{
		return ResourceLocation.fromNamespaceAndPath(VisitorsOfOmneria.MOD_ID, "textures/entity/charlemagne_cape.png");
	}

	private Set<Vec3> testBounds(Level level, Vec3 start)
	{
		Set<Vec3> obstacles = new HashSet<>();
		int radius = 20; // example range
		for (int x = -radius; x <= radius; x++) {
			for (int y = -2; y <= 2; y++) {
				for (int z = -radius; z <= radius; z++) {
					BlockPos pos = new BlockPos((int) (start.x + x), (int) (start.y + y), (int) (start.z + z));
					if (!isWalkable(pos, level)) {
						obstacles.add(Vec3.atLowerCornerOf(pos));
					}
				}
			}
		}
		return obstacles;
	}

	boolean isWalkable(BlockPos pos, Level world) {
		BlockState state = world.getBlockState(pos);
		return state.isAir() || state.getCollisionShape(world, pos).isEmpty();
	}

	public Charlemagne(EntityType<? extends AbstractFriendlyNPC> p_21683_, Level p_21684_)
	{
		super(p_21683_, p_21684_);
	}

	@Override
	public boolean canBeAffected(MobEffectInstance p_70687_1_) {
		return p_70687_1_.getEffect() != MobEffects.WITHER && super.canBeAffected(p_70687_1_);
	}

	@Override
	public boolean fireImmune()
	{
		return true;
	}

	@Override
	public boolean canBeLeashed(@NotNull Player pPlayer)
	{
		return false;
	}

	@Override
	public boolean canBeCollidedWith()
	{
		return true;
	}

	@Override
	public boolean isPushable()
	{
		return false;
	}

	@Override
	public boolean canStandOnFluid(FluidState p_204042_)
	{
		return p_204042_.is(Fluids.LAVA) || p_204042_.is(Fluids.FLOWING_LAVA);
	}



	@Override
	public void kill()
	{
		super.kill();
	}

	@Override
	protected void registerGoals()
	{
		goalSelector.addGoal(0, new PlayerConversationGoal<>(this));
		goalSelector.addGoal(0, new FloatGoal(this));
	}

	public static AttributeSupplier.Builder createAttributes()
	{
		return createLivingAttributes()
				.add(Attributes.MAX_HEALTH, 2500)
				.add(Attributes.MOVEMENT_SPEED, 0.25D)
				.add(Attributes.ARMOR, 20)
				.add(Attributes.ARMOR_TOUGHNESS, 20)
				.add(Attributes.ATTACK_KNOCKBACK, 2)
				.add(Attributes.KNOCKBACK_RESISTANCE, 1f)
				.add(Attributes.ATTACK_DAMAGE, 10)
				.add(Attributes.FOLLOW_RANGE, 80.0)
				.add(ForgeMod.STEP_HEIGHT_ADDITION.get(), 1.5);
	}

	@Override
	public boolean isInvulnerableTo(@NotNull DamageSource p_20122_)
	{
		if ((this.patch.brain != null && (this.patch.brain.getMode().is(CharlemagneMode.FRIENDLY) || this.patch.brain.getMode().is(CharlemagneMode.DEFENSE))) && !p_20122_.is(DamageTypes.GENERIC_KILL))
			return true;
		if (ModList.get().isLoaded(Mekanism.MODID))
			this.getCapability(Capabilities.RADIATION_ENTITY).ifPresent(rad -> rad.set(0));

		return super.isInvulnerableTo(p_20122_);
	}

	//Gains immunity to some effects.
	@Override
	public boolean addEffect(@NotNull MobEffectInstance pEffectInstance, @Nullable Entity pEntity)
	{
		for (MobEffect effect : onlyAffectedEffects)
		{
			if (pEffectInstance.getEffect() == effect)
				return super.addEffect(pEffectInstance, pEntity);
		}
		return false;
	}

	@Override
	public void tick() {
		super.tick();
		this.moveCloak();
	}

	private void moveCloak() {
		this.xCloakO = this.xCloak;
		this.yCloakO = this.yCloak;
		this.zCloakO = this.zCloak;
		double d0 = this.getX() - this.xCloak;
		double d1 = this.getY() - this.yCloak;
		double d2 = this.getZ() - this.zCloak;
		double d3 = 10.0F;
		if (d0 > (double)10.0F) {
			this.xCloak = this.getX();
			this.xCloakO = this.xCloak;
		}

		if (d2 > (double)10.0F) {
			this.zCloak = this.getZ();
			this.zCloakO = this.zCloak;
		}

		if (d1 > (double)10.0F) {
			this.yCloak = this.getY();
			this.yCloakO = this.yCloak;
		}

		if (d0 < (double)-10.0F) {
			this.xCloak = this.getX();
			this.xCloakO = this.xCloak;
		}

		if (d2 < (double)-10.0F) {
			this.zCloak = this.getZ();
			this.zCloakO = this.zCloak;
		}

		if (d1 < (double)-10.0F) {
			this.yCloak = this.getY();
			this.yCloakO = this.yCloak;
		}

		this.xCloak += d0 * (double)0.25F;
		this.zCloak += d2 * (double)0.25F;
		this.yCloak += d1 * (double)0.25F;
	}

	@Override
	protected @NotNull InteractionResult mobInteract(@NotNull Player p_21472_, @NotNull InteractionHand p_21473_)
	{
		LogUtils.getLogger().debug("Interacted");
		if (!this.level().isClientSide)
			this.patch.brain.handleInteractionServer((ServerPlayer) p_21472_);
		return InteractionResult.sidedSuccess(level().isClientSide);
	}

	@Override
	public void openDialogueScreen(CompoundTag senderData)
	{
		DialogueBuilder builder = new DialogueBuilder(this);

		builder.start(0)
				.addChoice(0, 1)
				.addChoice(1, 2)
				.addChoice(2, 3)
				.addFinalChoice(9, 1L);

		if(!builder.isEmpty()){
			Minecraft.getInstance().setScreen(builder.build());
		}
	}

	@Override
	public void handleNpcInteraction(Player player, long interactionID)
	{
		setConversingPlayer(null);
	}

	@Override
	public void setConversingPlayer(@Nullable Player player)
	{
		this.conversingPlayer = player;
	}

	@Override
	public @Nullable Player getConversingPlayer()
	{
		return conversingPlayer;
	}

	@Override
	public @NotNull SoundEvent getSound()
	{
		return SoundRegistry.SANS.get();
	}
}
