package net.forixaim.omneria.registry;

import net.forixaim.omneria.VisitorsOfOmneria;
import net.forixaim.omneria.client.particles.types.TrackingParticleOptions;
import net.forixaim.omneria.client.particles.types.TrackingParticleType;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ParticleRegistry
{
    public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(Registries.PARTICLE_TYPE, VisitorsOfOmneria.MOD_ID);

    public static final RegistryObject<TrackingParticleType> GENESIS_AURA = PARTICLES.register("genesis_aura", () -> new TrackingParticleType(true));

}
