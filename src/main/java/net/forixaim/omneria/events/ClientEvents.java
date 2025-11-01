package net.forixaim.omneria.events;

import net.forixaim.omneria.VisitorsOfOmneria;
import net.forixaim.omneria.client.models.entity.projectile.DragonShotModel;
import net.forixaim.omneria.client.particles.DraconicBlastParticle;
import net.forixaim.omneria.client.particles.GenesisAuraParticle;
import net.forixaim.omneria.registry.ParticleRegistry;
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
    }

    @SubscribeEvent
    public static void onParticle(RegisterParticleProvidersEvent event)
    {
        event.registerSpriteSet(ParticleRegistry.GENESIS_AURA.get(), GenesisAuraParticle.Provider::new);
        event.registerSpriteSet(ParticleRegistry.DRACONIC_BLAST_IMPACT.get(), DraconicBlastParticle.Provider::new);
        event.registerSpriteSet(ParticleRegistry.DRACONIC_BLAST_FLASH.get(), DraconicBlastParticle.ProviderSmall::new);

    }
}
