package net.forixaim.omneria.capabilities.weapons;

import net.forixaim.ex_cap.api.events.ExCapMovesetRegistryEvent;
import net.forixaim.ex_cap.capabilities.weapon_presets.ExCapWeapons;
import net.forixaim.omneria.VisitorsOfOmneria;
import net.forixaim.omneria.capabilities.ex_cap_weapons.OmneriaProviders;
import net.forixaim.omneria.capabilities.ex_cap_weapons.movesets.ImperatriceMovesets;
import net.forixaim.omneria.capabilities.styles.LumiereStyles;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = VisitorsOfOmneria.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class WeaponTypeInjection
{
	@SubscribeEvent
	public static void inject(ExCapMovesetRegistryEvent event)
	{
		event.addProvider(ExCapWeapons.BOKKEN, OmneriaProviders.IMPERATRICE_SWORD_PROVIDER, OmneriaProviders.CHARLEMAGNE_UNCONDITIONAL);
		event.addMoveset(ExCapWeapons.BOKKEN, LumiereStyles.IMPERATRICE_SWORD, ImperatriceMovesets.IMPERATRICE_SWORD_PRIMARY);
		event.addProvider(ExCapWeapons.LONGSWORD, OmneriaProviders.IMPERATRICE_SWORD_PROVIDER, OmneriaProviders.CHARLEMAGNE_UNCONDITIONAL);
		event.addMoveset(ExCapWeapons.LONGSWORD, LumiereStyles.IMPERATRICE_SWORD, ImperatriceMovesets.IMPERATRICE_SWORD_PRIMARY);
	}
}
