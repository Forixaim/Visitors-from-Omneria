package net.forixaim.omneria.combat;

import com.google.common.collect.Lists;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import yesman.epicfight.world.damagesource.EpicFightDamageSource;

import java.util.List;
import java.util.Map;

public class OmneriaDamageSource extends EpicFightDamageSource
{
    private static final Map<ResourceKey<DamageType>, List<Component>> deathMessages = Map.ofEntries(
            Map.entry(OmneriaDamageTypes.ENERGY_BLAST,
                    Lists.newArrayList(
                            Component.translatable("death.omneria.energy_blast_1"),
                            Component.translatable("death.omneria.energy_blast_2"),
                            Component.translatable("death.omneria.energy_blast_3")
                    )),
            Map.entry(OmneriaDamageTypes.DRAGON_CLAW,
                    Lists.newArrayList(
                            Component.translatable("death.omneria.dragon_claw_1"),
                            Component.translatable("death.omneria.dragon_claw_2"),
                            Component.translatable("death.omneria.dragon_claw_3")
                    )),
            Map.entry(OmneriaDamageTypes.DRAGON_KICK,
                    Lists.newArrayList(
                            Component.translatable("death.omneria.dragon_kick_1"),
                            Component.translatable("death.omneria.dragon_kick_2"),
                            Component.translatable("death.omneria.dragon_kick_3")
                    ))
    );

    public static OmneriaDamageSource fromEFDS(EpicFightDamageSource source)
    {
        return new OmneriaDamageSource(source);
    }

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
        return super.getLocalizedDeathMessage(pLivingEntity);
    }
}
