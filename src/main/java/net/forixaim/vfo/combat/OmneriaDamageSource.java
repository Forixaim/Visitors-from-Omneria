package net.forixaim.vfo.combat;

import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import yesman.epicfight.api.animation.AnimationManager;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.utils.math.ValueModifier;
import yesman.epicfight.world.damagesource.EpicFightDamageSource;
import yesman.epicfight.world.damagesource.ExtraDamageInstance;
import yesman.epicfight.world.damagesource.StunType;

import java.util.Objects;

public class OmneriaDamageSource extends EpicFightDamageSource
{
    public OmneriaDamageSource(DamageSource damageSource)
    {
        super(damageSource);
    }

    public OmneriaDamageSource(Holder<DamageType> damageType, @Nullable Entity directEntity, @Nullable Entity causingEntity, @Nullable Vec3 initialPosition)
    {
        super(damageType, directEntity, causingEntity, initialPosition);
    }


    @Override
    public @NotNull Component getLocalizedDeathMessage(@NotNull LivingEntity pLivingEntity)
    {
        String $$1 = "death.attack." + this.type().msgId();
        if (this.getEntity() == null && this.getDirectEntity() == null) {
            LivingEntity $$5 = pLivingEntity.getKillCredit();
            String $$6 = $$1 + ".player";
            return $$5 != null ? Component.translatable($$6, pLivingEntity.getDisplayName(), $$5.getDisplayName()) : Component.translatable($$1, new Object[]{pLivingEntity.getDisplayName()});
        } else {
            Component $$2 = this.getEntity() == null ? Objects.requireNonNull(this.getDirectEntity()).getDisplayName() : this.getEntity().getDisplayName();
            Entity var6 = this.getEntity();
            ItemStack var10000;
            if (var6 instanceof LivingEntity) {
                LivingEntity $$3 = (LivingEntity)var6;
                var10000 = $$3.getMainHandItem();
            } else {
                var10000 = ItemStack.EMPTY;
            }

            ItemStack $$4 = var10000;
            return !$$4.isEmpty() && $$4.hasCustomHoverName() ? Component.translatable($$1 + ".item", pLivingEntity.getDisplayName(), $$2, $$4.getDisplayName()) : Component.translatable($$1, new Object[]{pLivingEntity.getDisplayName(), $$2});
        }
    }

    @Override
    public OmneriaDamageSource setAnimation(AnimationManager.AnimationAccessor<? extends StaticAnimation> animation)
    {
        return (OmneriaDamageSource) super.setAnimation(animation);
    }

    @Override
    public OmneriaDamageSource setDamageModifier(ValueModifier damageModifier)
    {
        return (OmneriaDamageSource)super.setDamageModifier(damageModifier);
    }

    @Override
    public OmneriaDamageSource setImpact(float f)
    {
        return (OmneriaDamageSource)super.setImpact(f);
    }

    @Override
    public OmneriaDamageSource setArmorNegation(float f)
    {
        return (OmneriaDamageSource)super.setArmorNegation(f);
    }

    @Override
    public OmneriaDamageSource setHurtItem(ItemStack hurtItem)
    {
        return (OmneriaDamageSource)super.setHurtItem(hurtItem);
    }

    @Override
    public OmneriaDamageSource setInitialPosition(Vec3 initialPosition)
    {
        return (OmneriaDamageSource)super.setInitialPosition(initialPosition);
    }

    @Override
    public OmneriaDamageSource setStunType(StunType stunType)
    {
        return (OmneriaDamageSource)super.setStunType(stunType);
    }

    @Override
    public OmneriaDamageSource addExtraDamage(ExtraDamageInstance extraDamage)
    {
        return (OmneriaDamageSource)super.addExtraDamage(extraDamage);
    }

    @Override
    public OmneriaDamageSource addRuntimeTag(TagKey<DamageType> type)
    {
        return (OmneriaDamageSource)super.addRuntimeTag(type);
    }

    @Override
    public OmneriaDamageSource addRuntimeTag(ResourceKey<DamageType> type)
    {
        return (OmneriaDamageSource)super.addRuntimeTag(type);
    }
}
