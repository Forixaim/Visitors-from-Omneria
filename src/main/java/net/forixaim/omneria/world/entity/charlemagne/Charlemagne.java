package net.forixaim.omneria.world.entity.charlemagne;

import com.google.common.collect.Lists;
import com.mojang.logging.LogUtils;
import mekanism.common.Mekanism;
import mekanism.common.capabilities.Capabilities;
import net.forixaim.omneria.registry.ItemRegistry;
import net.forixaim.omneria.world.entity.charlemagne.ai.CharlemagneMode;
import net.forixaim.omneria.world.entity.patches.CharlemagnePatch;
import net.forixaim.omneria.world.entity.special_tags.IRadiationImmune;
import net.forixaim.omneria.world.entity.types.AbstractFriendlyNPC;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.fml.ModList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Charlemagne extends AbstractFriendlyNPC implements IRadiationImmune
{
	public CharlemagnePatch patch;
	private final SimpleContainer charlemagneInventory = new SimpleContainer(8);
	//For debugging purposes, the entity will be set to a stationary armor stand.
	public final TargetingConditions defConditions = TargetingConditions.forCombat().range(this.getAttributeValue(Attributes.FOLLOW_RANGE)).selector(pred -> pred instanceof Enemy);
	private static final List<MobEffect> onlyAffectedEffects = Lists.newArrayList(
			MobEffects.ABSORPTION,
			MobEffects.DAMAGE_BOOST,
			MobEffects.DAMAGE_RESISTANCE,
			MobEffects.REGENERATION,
			MobEffects.HEAL,
			MobEffects.MOVEMENT_SPEED
	);

	public Charlemagne(EntityType<? extends AbstractFriendlyNPC> p_21683_, Level p_21684_)
	{
		super(p_21683_, p_21684_);
		this.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(ItemRegistry.ORIGIN_JOYEUSE.get()));
		//Mod Checks

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
	protected @NotNull InteractionResult mobInteract(@NotNull Player p_21472_, @NotNull InteractionHand p_21473_)
	{
		LogUtils.getLogger().debug("Interacted");
		if (p_21472_.getItemInHand(InteractionHand.MAIN_HAND).is(Items.DEBUG_STICK) && !this.level().isClientSide)
			this.patch.brain.debugFire();
		return InteractionResult.SUCCESS;
	}
}
