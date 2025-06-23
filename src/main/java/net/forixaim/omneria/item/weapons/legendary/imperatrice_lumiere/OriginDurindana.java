package net.forixaim.omneria.item.weapons.legendary.imperatrice_lumiere;

import net.forixaim.omneria.VisitorsOfOmneria;
import net.forixaim.omneria.item.OmneriaRarities;
import net.forixaim.omneria.item.VisitorsOfOmneriaTiers;
import net.forixaim.omneria.item.weapons.legendary.LegendaryWeapon;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

public class OriginDurindana extends LegendaryWeapon
{
	public OriginDurindana()
	{
		super(VisitorsOfOmneriaTiers.ORIGIN_JOYEUSE, -1, -2.8f, new Properties().rarity(OmneriaRarities.IMPERATRICE_LUMIERE));
	}

	@Override
	public void appendHoverText(@NotNull ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, @NotNull TooltipFlag flagIn) {
		tooltip.add(Component.literal(""));
		tooltip.add(Component.translatable("item." + VisitorsOfOmneria.MOD_ID + ".origin_durindana.tooltip").withStyle(ChatFormatting.DARK_RED));
	}
}
