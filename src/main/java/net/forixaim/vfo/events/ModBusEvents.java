package net.forixaim.vfo.events;

import net.forixaim.vfo.VisitorsOfOmneria;
import net.forixaim.vfo.registry.EntityRegistry;
import net.forixaim.vfo.registry.ModelLayers;
import net.forixaim.vfo.world.entity.charlemagne.Charlemagne;
import net.forixaim.vfo.world.entity.charlemagne.CharlemagneModel;
import net.forixaim.vfo.world.entity.charlemagne.CharlemagnePatch;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import yesman.epicfight.api.forgeevent.EntityPatchRegistryEvent;


public class ModBusEvents
{
	@Mod.EventBusSubscriber(modid = VisitorsOfOmneria.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
	public static class CommonEvents
	{
		@SubscribeEvent
		public static void onEntityPatchRegister(EntityPatchRegistryEvent event)
		{
			event.getTypeEntry().put(EntityRegistry.CHARLEMAGNE.get(), entity -> CharlemagnePatch::new);
		}

		@SubscribeEvent
		public static void onAttributeRegister(EntityAttributeCreationEvent event)
		{
			event.put(EntityRegistry.CHARLEMAGNE.get(), Charlemagne.createAttributes().build());
		}
	}

	@Mod.EventBusSubscriber(modid = VisitorsOfOmneria.MOD_ID, value = {Dist.CLIENT}, bus = Mod.EventBusSubscriber.Bus.MOD)
	public static class ClientEvents
	{
		@SubscribeEvent
		public static void registerLayer(EntityRenderersEvent.RegisterLayerDefinitions event) {
			event.registerLayerDefinition(ModelLayers.CHARLEMAGNE_LAYER, CharlemagneModel::createBodyLayer);
		}
	}

}
