package net.forixaim.omneria.item.weapons.legendary;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tier;
import yesman.epicfight.world.item.WeaponItem;

public class LegendaryWeapon extends WeaponItem {
    public LegendaryWeapon(Tier tier, int damageIn, float speedIn, Item.Properties props) {
        super(tier, damageIn, speedIn, props.durability(0).defaultDurability(0).fireResistant());
    }
}
