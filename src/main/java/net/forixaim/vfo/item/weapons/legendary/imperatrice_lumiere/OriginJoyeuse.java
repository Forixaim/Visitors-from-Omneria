package net.forixaim.vfo.item.weapons.legendary.imperatrice_lumiere;

import net.forixaim.vfo.VisitorsOfOmneria;
import net.forixaim.vfo.item.OmneriaRarities;
import net.forixaim.vfo.item.VisitorsOfOmneriaTiers;
import net.forixaim.vfo.item.weapons.legendary.GeckoLibLegendaryWeapon;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import yesman.epicfight.world.item.WeaponItem;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

public class OriginJoyeuse extends GeckoLibLegendaryWeapon
{
	public OriginJoyeuse()
	{
		super(VisitorsOfOmneriaTiers.ORIGIN_JOYEUSE, 5, -3f, new Properties().rarity(OmneriaRarities.IMPERATRICE_LUMIERE));
	}

	@Override
	public void appendHoverText(@NotNull ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, @NotNull TooltipFlag flagIn) {
		tooltip.add(Component.literal(""));
		tooltip.add(Component.translatable("item." + VisitorsOfOmneria.MOD_ID + ".origin_joyeuse.tooltip").withStyle(ChatFormatting.DARK_RED));
	}
}
