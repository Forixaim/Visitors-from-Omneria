package net.forixaim.vfo.registry;

import net.forixaim.vfo.VisitorsOfOmneria;
import net.forixaim.vfo.item.VisitorsOfOmneriaTiers;
import net.forixaim.vfo.item.weapons.legendary.house_lux.OriginArondight;
import net.forixaim.vfo.item.weapons.legendary.house_lux.OriginExcalibur;
import net.forixaim.vfo.item.weapons.legendary.imperatrice_lumiere.OriginDurindana;
import net.forixaim.vfo.item.weapons.legendary.imperatrice_lumiere.OriginJoyeuse;
import net.minecraft.world.food.Foods;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import yesman.epicfight.world.item.LongswordItem;

public class ItemRegistry
{
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, VisitorsOfOmneria.MOD_ID);
	public static final RegistryObject<Item> ORIGIN_EXCALIBUR = ITEMS.register("origin_excalibur", OriginExcalibur::new);
	public static final RegistryObject<Item> ORIGIN_ARONDIGHT = ITEMS.register("origin_arondight", OriginArondight::new);
	public static final RegistryObject<Item> ORIGIN_JOYEUSE = ITEMS.register("origin_joyeuse", OriginJoyeuse::new);
	public static final RegistryObject<Item> ORIGIN_DURINDANA = ITEMS.register("origin_durindana", OriginDurindana::new);

	public static final RegistryObject<Item> BAGUETTE = ITEMS.register("baguette", () -> new LongswordItem(new Item.Properties().durability(0).defaultDurability(0).food(Foods.COOKED_BEEF), VisitorsOfOmneriaTiers.BAGUETTE));

}
