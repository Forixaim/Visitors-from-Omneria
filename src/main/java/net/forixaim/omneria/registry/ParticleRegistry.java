package net.forixaim.omneria.registry;

import net.forixaim.omneria.VisitorsOfOmneria;
import net.forixaim.omneria.client.particles.types.BeamParticleType;
import net.forixaim.omneria.client.particles.types.TrackingParticleType;
import net.forixaim.omneria.client.particles.types.TrailParticleType;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ParticleRegistry
{
    public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(Registries.PARTICLE_TYPE, VisitorsOfOmneria.MOD_ID);

    public static final DeferredHolder<ParticleType<?>, TrackingParticleType> GENESIS_AURA = PARTICLES.register("genesis_aura", () -> new TrackingParticleType(true));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> DRACONIC_BLAST_IMPACT = PARTICLES.register("draconic_blast_impact", () -> new SimpleParticleType(true));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> DRACONIC_BLAST_FLASH = PARTICLES.register("draconic_blast_flash", () -> new SimpleParticleType(true));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> DARK_BANG_EXPLOSION = PARTICLES.register("dark_bang_explosion", () -> new SimpleParticleType(true));
    public static final DeferredHolder<ParticleType<?>, TrailParticleType> OMNERIA_PROJECTILE_TRAIL = PARTICLES.register("omneria_project_trail", () -> new TrailParticleType(true));
    public static final DeferredHolder<ParticleType<?>, TrackingParticleType> DRAGON_CANNON_CHARGE = PARTICLES.register("dragon_cannon_charge", () -> new TrackingParticleType(true));
    public static final DeferredHolder<ParticleType<?>, TrackingParticleType> FULL_POWER_DRAGON_CANNON_CHARGE = PARTICLES.register("full_power_dragon_cannon_charge", () -> new TrackingParticleType(true));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> DRACONIC_EXPLOSION = PARTICLES.register("draconic_explosion", () -> new SimpleParticleType(true));
    public static final DeferredHolder<ParticleType<?>, TrackingParticleType> FULL_POWER_DRAGON_CANNON_GLIMMER = PARTICLES.register("dragon_cannon_glimmer", () -> new TrackingParticleType(true));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> BILLBOARD_TRAIL = PARTICLES.register("billboard_trail", () -> new SimpleParticleType(true));

    public static final DeferredHolder<ParticleType<?>, BeamParticleType> SQUARE_LASER = PARTICLES.register("square_laser", () -> new BeamParticleType(true));
    public static final DeferredHolder<ParticleType<?>, BeamParticleType> DRAGON_CANNON_LASER = PARTICLES.register("dragon_cannon_laser", () -> new BeamParticleType(true));


}
