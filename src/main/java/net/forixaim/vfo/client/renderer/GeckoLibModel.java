package net.forixaim.vfo.client.renderer;

import net.forixaim.vfo.VisitorsOfOmneria;
import net.forixaim.vfo.item.weapons.legendary.GeckoLibLegendaryWeapon;
import net.forixaim.vfo.item.weapons.legendary.LegendaryWeapon;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import software.bernie.geckolib.model.GeoModel;

import java.util.Objects;

public class GeckoLibModel extends GeoModel<GeckoLibLegendaryWeapon>
{
    @Override
    public ResourceLocation getModelResource(GeckoLibLegendaryWeapon animatable) {
        return new ResourceLocation(VisitorsOfOmneria.MOD_ID, "models/item/geo/" + getWeaponName(animatable) + ".geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(GeckoLibLegendaryWeapon animatable) {
        return new ResourceLocation(VisitorsOfOmneria.MOD_ID, "textures/item/" + getWeaponName(animatable) + ".png");
    }

    @Override
    public ResourceLocation getAnimationResource(GeckoLibLegendaryWeapon animatable) {
        return new ResourceLocation(VisitorsOfOmneria.MOD_ID, "geo/animations" + getWeaponName(animatable) + ".animation.json");
    }

    private String getWeaponName(GeckoLibLegendaryWeapon weapon) {
        ResourceLocation name = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(weapon));
        return name.getPath();
    }
}
