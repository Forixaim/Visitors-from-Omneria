package net.forixaim.omneria.registry;

import net.forixaim.omneria.VisitorsOfOmneria;
import net.forixaim.omneria.item.VisitorsOfOmneriaTiers;
import net.forixaim.omneria.item.weapons.legendary.house_lux.OriginArondight;
import net.forixaim.omneria.item.weapons.legendary.house_lux.OriginExcalibur;
import net.forixaim.omneria.item.weapons.legendary.imperatrice_lumiere.OriginDurindana;
import net.forixaim.omneria.item.weapons.legendary.imperatrice_lumiere.OriginJoyeuse;
import net.minecraft.world.food.Foods;
import net.minecraft.world.item.Item;

import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import yesman.epicfight.world.item.LongswordItem;

public class ItemRegistry
{
	public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(VisitorsOfOmneria.MOD_ID);
	public static final DeferredItem<Item> ORIGIN_EXCALIBUR = ITEMS.register("origin_excalibur", OriginExcalibur::new);
	public static final DeferredItem<Item> ORIGIN_ARONDIGHT = ITEMS.register("origin_arondight", OriginArondight::new);
	public static final DeferredItem<Item> ORIGIN_JOYEUSE = ITEMS.register("origin_joyeuse", OriginJoyeuse::new);
	public static final DeferredItem<Item> ORIGIN_DURINDANA = ITEMS.register("origin_durindana", OriginDurindana::new);
}
