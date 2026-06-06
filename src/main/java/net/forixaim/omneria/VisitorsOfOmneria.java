package net.forixaim.omneria;


import com.google.common.collect.Lists;
import net.forixaim.omneria.capabilities.weapons.OmneriaExCapWeapons;
import net.forixaim.omneria.client.renderer.entity.projectile.DarkBangRenderer;
import net.forixaim.omneria.client.renderer.entity.projectile.DragonCannonRenderer;
import net.forixaim.omneria.client.renderer.entity.projectile.DragonShotRenderer;
import net.forixaim.omneria.client.renderer.entity.projectile.FPDragonCannonRenderer;
import net.forixaim.omneria.netcode.PacketHandler;
import net.forixaim.omneria.registry.ArmatureRegistry;
import net.forixaim.omneria.registry.EntityRegistry;
import net.forixaim.omneria.registry.ParticleRegistry;
import net.forixaim.omneria.registry.SoundRegistry;
import net.forixaim.omneria.skill.DatakeyRegistry;
import net.forixaim.omneria.client.renderer.entity.CharlemagneRenderer;
import net.forixaim.omneria.world.entity.FacialLivingMotions;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.CommonColors;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import yesman.epicfight.api.animation.LivingMotion;
import yesman.epicfight.gameasset.Armatures;
import yesman.epicfight.main.EpicFightExtensions;

import static net.forixaim.omneria.registry.CreativeTabRegistry.CREATIVE_MODE_TABS;
import static net.forixaim.omneria.registry.CreativeTabRegistry.VISITORS_OF_OMNERIA;
import static net.forixaim.omneria.registry.ItemRegistry.ITEMS;


// The value here should match an entry in the META-INF/mods.toml file
@Mod(VisitorsOfOmneria.MOD_ID)
public class VisitorsOfOmneria
{
	// Define mod id in a common place for everything to reference
	public static final String MOD_ID = "omneria";

    public VisitorsOfOmneria(IEventBus modEventBus, ModContainer container)
	{
		ITEMS.register(modEventBus);
		CREATIVE_MODE_TABS.register(modEventBus);
		EntityRegistry.Register(modEventBus);
		DatakeyRegistry.DATA_KEYS.register(modEventBus);
		SoundRegistry.SOUNDS.register(modEventBus);
        ParticleRegistry.PARTICLES.register(modEventBus);
        OmneriaExCapWeapons.EX_CAP_WEAPONS.register(modEventBus);
		modEventBus.addListener(this::commonSetup);
		modEventBus.addListener(this::clientSetup);
		MinecraftForge.EVENT_BUS.register(this);
		LivingMotion.ENUM_MANAGER.registerEnumCls(MOD_ID, FacialLivingMotions.class);
		container.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
		container.registerExtensionPoint(EpicFightExtensions.class, () -> new EpicFightExtensions(VISITORS_OF_OMNERIA));
	}

	private static void registerEntityTypes()
	{
		Armatures.registerEntityTypeArmature(EntityRegistry.CHARLEMAGNE.get(), ArmatureRegistry.CHARLEMAGNE);
	}


	private void commonSetup(final FMLCommonSetupEvent event)
	{
		event.enqueueWork(PacketHandler::register);
		event.enqueueWork(VisitorsOfOmneria::registerEntityTypes);
        event.enqueueWork(this::registerStuff);
	}

    private void registerStuff()
    {
        if (ModList.get().isLoaded(Loader.MODID))
        {
            LegendaryTooltipsConfig.INSTANCE.addFrameDefinition(ResourceLocation.fromNamespaceAndPath(VisitorsOfOmneria.MOD_ID, "textures/gui/genesis_wyrm_rarity.png")
            , 1, () -> ColorUtil.combineRGB(255, 0, 255), () -> ColorUtil.combineRGB(102, 0, 169), () -> CommonColors.BLACK, 2, Lists.newArrayList());
        }
    }

	private void clientSetup(final FMLClientSetupEvent event)
	{
		EntityRenderers.register(EntityRegistry.CHARLEMAGNE.get(), CharlemagneRenderer::new);
		EntityRenderers.register(EntityRegistry.DRAGON_SHOT.get(), DragonShotRenderer::new);
        EntityRenderers.register(EntityRegistry.DRAGON_CANNON.get(), DragonCannonRenderer::new);
        EntityRenderers.register(EntityRegistry.FULL_POWER_DRAGON_CANNON.get(), FPDragonCannonRenderer::new);

        EntityRenderers.register(EntityRegistry.DARK_BANG.get(), DarkBangRenderer::new);

	}
}
