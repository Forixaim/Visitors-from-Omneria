package net.forixaim.vfo.capabilities.weapons;

import net.forixaim.efm_ex.api.events.ExCapMovesetRegistryEvent;
import net.forixaim.efm_ex.api.events.ExCapWeaponRegistryEvent;
import net.forixaim.efm_ex.capabilities.CoreCapability;
import net.forixaim.efm_ex.capabilities.weapon_presets.CoreMovesets;
import net.forixaim.efm_ex.capabilities.weapon_presets.ExCapWeapons;
import net.forixaim.efm_ex.capabilities.weapon_presets.MainConditionals;
import net.forixaim.efm_ex.capabilities.weapon_presets.MovesetMappings;
import net.forixaim.vfo.VisitorsOfOmneria;
import net.forixaim.vfo.capabilities.ex_cap_weapons.OmneriaProviders;
import net.forixaim.vfo.capabilities.ex_cap_weapons.movesets.ImperatriceMovesets;
import net.forixaim.vfo.capabilities.styles.LumiereStyles;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import yesman.epicfight.world.capabilities.item.CapabilityItem;

@Mod.EventBusSubscriber(modid = VisitorsOfOmneria.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class WeaponTypeInjection
{
	@SubscribeEvent
	public static void inject(ExCapMovesetRegistryEvent event)
	{
		event.addProvider(ExCapWeapons.LONGSWORD, OmneriaProviders.IMPERATRICE_SWORD_PROVIDER);
		event.addMoveset(ExCapWeapons.LONGSWORD, LumiereStyles.IMPERATRICE_SWORD, ImperatriceMovesets.IMPERATRICE_SWORD_PRIMARY);
	}
}
