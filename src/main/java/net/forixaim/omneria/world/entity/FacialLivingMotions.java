package net.forixaim.omneria.world.entity;

import yesman.epicfight.api.animation.LivingMotion;

public enum FacialLivingMotions implements LivingMotion
{
    CHARLEMAGNE_NEUTRAL;

    final int id;

    FacialLivingMotions()
    {
        this.id = ENUM_MANAGER.assign(this);
    }

    @Override
    public int universalOrdinal()
    {
        return 0;
    }
}
