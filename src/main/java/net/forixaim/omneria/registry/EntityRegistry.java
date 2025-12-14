package net.forixaim.omneria.registry;

import net.forixaim.omneria.VisitorsOfOmneria;
import net.forixaim.omneria.world.entity.charlemagne.Charlemagne;
import net.forixaim.omneria.world.entity.projectiles.*;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EntityRegistry
{
	private static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, VisitorsOfOmneria.MOD_ID);

	public static final RegistryObject<EntityType<Charlemagne>> CHARLEMAGNE = ENTITIES.register("charlemagne",
			() -> EntityType.Builder.of(Charlemagne::new, MobCategory.MISC).sized(0.6f, 1.8f).clientTrackingRange(12).build("charlemagne"));
	public static final RegistryObject<EntityType<FlareSlashProjectile>> FLARE_SLASH = ENTITIES.register("flare_slash",
			() -> EntityType.Builder.of(FlareSlashProjectile::new, MobCategory.MISC).sized(0.1f, 0.1f).clientTrackingRange(12).build("flare_slash"));
	public static final RegistryObject<EntityType<DragonShotProjectile>> DRAGON_SHOT = ENTITIES.register("dragon_shot",
			() -> EntityType.Builder.of(DragonShotProjectile::new, MobCategory.MISC).sized(0.5f, 0.5f).clientTrackingRange(12).build("dragon_shot"));
    public static final RegistryObject<EntityType<DarkBangProjectile>> DARK_BANG = ENTITIES.register("dark_bang",
            () -> EntityType.Builder.of(DarkBangProjectile::new, MobCategory.MISC).sized(1f, 1f).clientTrackingRange(12).build("dark_bang"));
    public static final RegistryObject<EntityType<DragonCannonBeam>> DRAGON_CANNON = ENTITIES.register("dragon_cannon",
            () -> EntityType.Builder.of(DragonCannonBeam::new, MobCategory.MISC).sized(1.2f, 1.2f).clientTrackingRange(64).build("dragon_cannon"));
    public static final RegistryObject<EntityType<FullPowerDragonCannonBeam>> FULL_POWER_DRAGON_CANNON = ENTITIES.register("full_power_dragon_cannon",
            () -> EntityType.Builder.of(FullPowerDragonCannonBeam::new, MobCategory.MISC).sized(3f, 3f).clientTrackingRange(512).build("full_power_dragon_cannon"));


	public static void Register(IEventBus bus)
	{
		ENTITIES.register(bus);
	}
}
