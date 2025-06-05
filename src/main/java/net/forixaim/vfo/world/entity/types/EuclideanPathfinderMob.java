package net.forixaim.vfo.world.entity.types;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.Level;

public class EuclideanPathfinderMob extends PathfinderMob
{
    protected EuclideanPathfinderMob(EntityType<? extends PathfinderMob> pEntityType, Level pLevel)
    {
        super(pEntityType, pLevel);
    }


}
