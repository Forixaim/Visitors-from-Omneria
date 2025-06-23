package net.forixaim.omneria.capabilities.weapons;

import net.forixaim.omneria.VisitorsOfOmneria;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import yesman.epicfight.api.forgeevent.WeaponCapabilityPresetRegistryEvent;
import yesman.epicfight.world.capabilities.item.CapabilityItem;

import java.util.function.Function;

//This is where all the weapon capability presets are implemented
@Mod.EventBusSubscriber(modid = VisitorsOfOmneria.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class PresetRegistry
{
	public static Function<Item, CapabilityItem.Builder> JOYEUSE = item ->
			OmneriaExCapWeapons.ORIGIN_JOYEUSE.export();


	@SubscribeEvent
	public static void Register(WeaponCapabilityPresetRegistryEvent Event)
	{
		Event.getTypeEntry().put(ResourceLocation.fromNamespaceAndPath(VisitorsOfOmneria.MOD_ID, "joyeuse"), JOYEUSE);
	}
}
