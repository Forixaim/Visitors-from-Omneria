package net.forixaim.omneria.registry;

import net.forixaim.omneria.VisitorsOfOmneria;
import net.forixaim.omneria.world.entity.charlemagne.Charlemagne;
import net.forixaim.omneria.world.entity.projectiles.FlareSlashProjectile;
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
			() -> EntityType.Builder.of(Charlemagne::new, MobCategory.MISC).sized(0.7f, 2.0f).clientTrackingRange(12).build("charlemagne"));
	public static final RegistryObject<EntityType<FlareSlashProjectile>> FLARE_SLASH = ENTITIES.register("flare_slash",
			() -> EntityType.Builder.of(FlareSlashProjectile::new, MobCategory.MISC).sized(0.1f, 0.1f).clientTrackingRange(12).build("flare_slash"));


	public static void Register(IEventBus bus)
	{
		ENTITIES.register(bus);
	}
}
