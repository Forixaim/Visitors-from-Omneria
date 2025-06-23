package net.forixaim.omneria.item.weapons.legendary.house_lux;

import net.forixaim.omneria.VisitorsOfOmneria;
import net.forixaim.omneria.item.OmneriaRarities;
import net.forixaim.omneria.item.VisitorsOfOmneriaTiers;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import yesman.epicfight.world.item.WeaponItem;

import javax.annotation.Nullable;
import java.util.List;

public class OriginExcalibur extends WeaponItem
{
	public OriginExcalibur()
	{
		super(VisitorsOfOmneriaTiers.ORIGIN_EXCALIBUR, 9, -3f, new Properties().durability(0).defaultDurability(0).rarity(OmneriaRarities.HELIOLUX_KING).fireResistant());
	}

	@Override
	public void appendHoverText(@NotNull ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, @NotNull TooltipFlag flagIn) {
		tooltip.add(Component.literal(""));
		tooltip.add(Component.translatable("item." + VisitorsOfOmneria.MOD_ID + ".origin_excalibur.tooltip").withStyle(ChatFormatting.YELLOW));
	}
}
