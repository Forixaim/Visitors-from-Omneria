package net.forixaim.vfo.item.weapons.legendary;

import net.forixaim.vfo.client.renderer.geckolib.GeckoLibItemRenderer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tier;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;
import yesman.epicfight.world.item.WeaponItem;

import java.util.function.Consumer;

public class GeckoLibLegendaryWeapon extends WeaponItem implements GeoItem
{
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public GeckoLibLegendaryWeapon(Tier tier, int damageIn, float speedIn, Item.Properties props) {
        super(tier, damageIn, speedIn, props.durability(0).defaultDurability(0).fireResistant());
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private GeckoLibItemRenderer renderer;

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (this.renderer == null)
                    this.renderer = new GeckoLibItemRenderer();

                return this.renderer;
            }
        });
    }
}
