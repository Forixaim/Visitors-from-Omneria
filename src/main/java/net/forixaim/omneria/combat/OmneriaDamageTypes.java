package net.forixaim.omneria.combat;

import net.forixaim.omneria.VisitorsOfOmneria;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.LivingEntity;

public class OmneriaDamageTypes
{

	public static final ResourceKey<DamageType> DRAGON_CLAW = createOriginal("dragon_claw");
	public static final ResourceKey<DamageType> DRAGON_KICK = createOriginal("dragon_kick");
	public static final ResourceKey<DamageType> ENERGY_BLAST = createOriginal("energy_blast");
    public static final TagKey<DamageType> GRAB = createTag("grab");

	public static Holder<DamageType> getHolder(LivingEntity entity, ResourceKey<DamageType> damageTypeKey)
	{
		return entity.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(damageTypeKey);

	}

	private static ResourceKey<DamageType> createOriginal(final String pName)
	{
		return ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(VisitorsOfOmneria.MOD_ID, pName));
	}

    private static TagKey<DamageType> createTag(final String pName)
    {
        return TagKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(VisitorsOfOmneria.MOD_ID, pName));
    }
}
