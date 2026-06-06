package net.forixaim.omneria.registry;

import net.forixaim.omneria.VisitorsOfOmneria;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import yesman.epicfight.registry.entries.EpicFightCreativeTabs;


public class CreativeTabRegistry
{
	public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, VisitorsOfOmneria.MOD_ID);


	public static final DeferredHolder<CreativeModeTab, CreativeModeTab> VISITORS_OF_OMNERIA = CREATIVE_MODE_TABS.register("visitors_of_omneria", () -> CreativeModeTab.builder()
			.title(Component.translatable("itemGroup.omneria.visitors_of_omneria").withStyle(ChatFormatting.DARK_PURPLE))
			.icon(() -> new ItemStack(ItemRegistry.ORIGIN_JOYEUSE.get()))
			.withTabsBefore(EpicFightCreativeTabs.ITEMS.getId()).hideTitle()
			.withBackgroundLocation(ResourceLocation.fromNamespaceAndPath(VisitorsOfOmneria.MOD_ID, "textures/gui/visitors_of_omneria.png"))
			.displayItems((params, output) -> ItemRegistry.ITEMS.getEntries().forEach(item -> {
				if (item == ItemRegistry.ORIGIN_JOYEUSE)
					output.accept(item.get());
				if (item == ItemRegistry.ORIGIN_EXCALIBUR)
					output.accept(item.get());
			}))
			.build());
}
