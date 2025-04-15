package net.forixaim.vfo;

import net.forixaim.vfo.registry.EntityRegistry;
import net.forixaim.vfo.registry.SoundRegistry;
import net.forixaim.vfo.skill.DatakeyRegistry;
import net.forixaim.vfo.client.renderer.entity.CharlemagneRenderer;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import yesman.epicfight.main.EpicFightExtensions;

import static net.forixaim.vfo.registry.CreativeTabRegistry.CREATIVE_MODE_TABS;
import static net.forixaim.vfo.registry.CreativeTabRegistry.VISITORS_OF_OMNERIA;
import static net.forixaim.vfo.registry.ItemRegistry.ITEMS;


// The value here should match an entry in the META-INF/mods.toml file
@Mod(VisitorsOfOmneria.MOD_ID)
public class VisitorsOfOmneria
{
	// Define mod id in a common place for everything to reference
	public static final String MOD_ID = "omneria";


	public VisitorsOfOmneria()
	{

		IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
		ITEMS.register(modEventBus);
		CREATIVE_MODE_TABS.register(modEventBus);
		EntityRegistry.Register(modEventBus);
		DatakeyRegistry.DATA_KEYS.register(modEventBus);
		SoundRegistry.SOUNDS.register(modEventBus);
		modEventBus.addListener(this::commonSetup);
		modEventBus.addListener(this::clientSetup);
		MinecraftForge.EVENT_BUS.register(this);
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
		ModLoadingContext.get().registerExtensionPoint(EpicFightExtensions.class, () ->
				new EpicFightExtensions(VISITORS_OF_OMNERIA.get()));
	}

	private void commonSetup(final FMLCommonSetupEvent event)
	{
	}

	private void clientSetup(final FMLClientSetupEvent event)
	{
		EntityRenderers.register(EntityRegistry.CHARLEMAGNE.get(), CharlemagneRenderer::new);
	}
}
