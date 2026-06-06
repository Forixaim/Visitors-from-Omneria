package net.forixaim.omneria.registry;

import net.forixaim.omneria.VisitorsOfOmneria;
import net.forixaim.omneria.world.entity.charlemagne.Charlemagne;
import net.forixaim.omneria.world.entity.projectiles.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;


public class EntityRegistry
{
	private static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, VisitorsOfOmneria.MOD_ID);
    
	public static final DeferredHolder<EntityType<?>,EntityType<DragonShotProjectile>> DRAGON_SHOT = ENTITIES.register("dragon_shot",
			() -> EntityType.Builder.of(DragonShotProjectile::new, MobCategory.MISC).sized(0.5f, 0.5f).clientTrackingRange(12).build("dragon_shot"));
    public static final DeferredHolder<EntityType<?>, EntityType<DarkBangProjectile>> DARK_BANG = ENTITIES.register("dark_bang",
            () -> EntityType.Builder.of(DarkBangProjectile::new, MobCategory.MISC).sized(1f, 1f).clientTrackingRange(12).build("dark_bang"));
    public static final DeferredHolder<EntityType<?>, EntityType<DragonCannonBeam>> DRAGON_CANNON = ENTITIES.register("dragon_cannon",
            () -> EntityType.Builder.of(DragonCannonBeam::new, MobCategory.MISC).sized(1.2f, 1.2f).clientTrackingRange(64).build("dragon_cannon"));
    public static final DeferredHolder<EntityType<?>, EntityType<FullPowerDragonCannonBeam>> FULL_POWER_DRAGON_CANNON = ENTITIES.register("full_power_dragon_cannon",
            () -> EntityType.Builder.of(FullPowerDragonCannonBeam::new, MobCategory.MISC).sized(3f, 3f).clientTrackingRange(512).build("full_power_dragon_cannon"));

    public static final DeferredHolder<EntityType<?>, EntityType<DarkBangProjectile>> DRAGON_PULSE = ENTITIES.register("dragon_pulse",
            () -> EntityType.Builder.of(DarkBangProjectile::new, MobCategory.MISC).sized(2f, 2f).clientTrackingRange(12).build("dragon_pulse"));
	public static void Register(IEventBus bus)
	{
		ENTITIES.register(bus);
	}
}
