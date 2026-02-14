package net.forixaim.omneria.world.entity.projectiles;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;

public class DragonPulse extends SimpleEnergyProjectile {
    protected int lifetime = 120;

    public DragonPulse(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }


}
