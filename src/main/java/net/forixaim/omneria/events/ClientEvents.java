package net.forixaim.omneria.events;

import net.forixaim.omneria.VisitorsOfOmneria;
import net.forixaim.omneria.client.models.entity.projectile.DragonCannonModel;
import net.forixaim.omneria.client.models.entity.projectile.DragonShotModel;
import net.forixaim.omneria.client.particles.*;
import net.forixaim.omneria.registry.ParticleRegistry;
import net.forixaim.omneria.world.entity.projectiles.DragonCannonBeam;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = VisitorsOfOmneria.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEvents
{
    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event)
    {
        event.registerLayerDefinition(DragonShotModel.LAYER_LOCATION, DragonShotModel::createBodyLayer);
        event.registerLayerDefinition(DragonCannonModel.LAYER_LOCATION, DragonCannonModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void onParticle(RegisterParticleProvidersEvent event)
    {
        event.registerSpriteSet(ParticleRegistry.GENESIS_AURA.get(), GenesisAuraParticle.Provider::new);
        event.registerSpriteSet(ParticleRegistry.DRACONIC_BLAST_IMPACT.get(), DraconicBlastParticle.Provider::new);
        event.registerSpriteSet(ParticleRegistry.DRACONIC_BLAST_FLASH.get(), DraconicBlastParticle.ProviderSmall::new);
        event.registerSpriteSet(ParticleRegistry.DARK_BANG_EXPLOSION.get(), DraconicBlastParticle.ProviderLarge::new);
        event.registerSpecial(ParticleRegistry.OMNERIA_PROJECTILE_TRAIL.get(), new OmneriaProjectileTrailParticle.Provider());
        event.registerSpecial(ParticleRegistry.DRAGON_CANNON_BEAM.get(), new DragonCannonParticle.Provider());
        event.registerSpecial(ParticleRegistry.SQUARE_LASER.get(), new SquareLaserParticle.Provider());

    }
}
