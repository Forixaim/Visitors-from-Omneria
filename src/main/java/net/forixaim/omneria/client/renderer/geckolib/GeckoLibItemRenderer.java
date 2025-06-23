package net.forixaim.omneria.client.renderer.geckolib;

import net.forixaim.omneria.item.weapons.legendary.GeckoLibLegendaryWeapon;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class GeckoLibItemRenderer extends GeoItemRenderer<GeckoLibLegendaryWeapon>
{
    public GeckoLibItemRenderer()
    {
        super(new GeckoLibModel());
    }
}
