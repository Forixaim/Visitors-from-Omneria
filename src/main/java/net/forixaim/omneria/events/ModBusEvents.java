package net.forixaim.omneria.events;

import net.forixaim.omneria.VisitorsOfOmneria;
import net.forixaim.omneria.registry.EntityRegistry;
import net.forixaim.omneria.world.entity.charlemagne.Charlemagne;
import net.forixaim.omneria.world.entity.patches.CharlemagnePatch;
import net.forixaim.omneria.client.renderer.patched.entities.living.PCharlemagneRenderer;
import net.forixaim.omneria.world.entity.patches.DarkBangPatch;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import yesman.epicfight.api.client.forgeevent.PatchedRenderersEvent;
import yesman.epicfight.api.forgeevent.EntityPatchRegistryEvent;
import yesman.epicfight.world.damagesource.EpicFightDamageSource;


public class ModBusEvents
{
	@Mod.EventBusSubscriber(modid = VisitorsOfOmneria.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
	public static class CommonEvents
	{
		@SubscribeEvent
		public static void onEntityPatchRegister(EntityPatchRegistryEvent event)
		{
			event.getTypeEntry().put(EntityRegistry.CHARLEMAGNE.get(), entity -> CharlemagnePatch::new);
            event.getTypeEntry().put(EntityRegistry.DARK_BANG.get(), entity -> DarkBangPatch::new);
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
		public static void onPatchRenderRegister(PatchedRenderersEvent.Add event)
		{
			event.addPatchedEntityRenderer(EntityRegistry.CHARLEMAGNE.get(), entityType -> new PCharlemagneRenderer(event.getContext(), entityType));
		}
	}

}
