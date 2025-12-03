package net.forixaim.omneria.animations.types;

import yesman.epicfight.api.animation.types.EntityState;

public class OmneriaEntityStates
{
    public static final EntityState.StateFactor<Boolean> CAN_BE_PUSHED = new EntityState.StateFactor<>("pushable", true);
    public static final EntityState.StateFactor<Boolean> CAN_JUMP = new EntityState.StateFactor<>("jumpable", true);

}
