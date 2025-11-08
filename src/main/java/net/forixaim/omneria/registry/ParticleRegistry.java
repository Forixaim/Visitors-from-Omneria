package net.forixaim.omneria.registry;

import net.forixaim.omneria.VisitorsOfOmneria;
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
    public static final RegistryObject<SimpleParticleType> DRACONIC_BLAST_IMPACT = PARTICLES.register("draconic_blast_impact", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> DRACONIC_BLAST_FLASH = PARTICLES.register("draconic_blast_flash", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> DARK_BANG_EXPLOSION = PARTICLES.register("dark_bang_explosion", () -> new SimpleParticleType(true));

}
