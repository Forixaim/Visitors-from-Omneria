package net.forixaim.omneria.colliders;

import net.forixaim.omneria.VisitorsOfOmneria;
import net.minecraft.resources.ResourceLocation;
import yesman.epicfight.api.collider.Collider;
import yesman.epicfight.api.collider.MultiOBBCollider;
import yesman.epicfight.api.collider.OBBCollider;

import static yesman.epicfight.gameasset.ColliderPreset.registerCollider;

public class GenesisWyrmColliders
{
    private static ResourceLocation reg(String name)
    {
        return ResourceLocation.fromNamespaceAndPath(VisitorsOfOmneria.MOD_ID, name);
    }

    public static final Collider GW_PULL = registerCollider(reg("gw_pull"), new OBBCollider(1, 1, 12, 0, 1, -12));

    public static final Collider GW_CLAW = registerCollider(reg("gw_claw"), new MultiOBBCollider(5,
            0.3D, 0.6D, 0.3D, 0.0D, 0.3D, -0.0D
    ));

    public static final Collider GW_DRAGON_CANNON = registerCollider(reg("gw_dragon_cannon"), new MultiOBBCollider(5,
            0.3D, 14D, 0.3D, 0.0D, 7D, -0.0D
    ));

    public static final Collider GW_CIRCLE_CLAW = registerCollider(reg("gw_circle_claw"), new MultiOBBCollider(5,
            1.5D, 0.5D, 1.5D, 0.0D, 1D, -0.0D
    ));

    public static final Collider GW_CIRCLE_CLAW_BURST = registerCollider(reg("gw_circle_claw_burst"), new MultiOBBCollider(5,
            1.7D, 0.5D, 1.7D, 0.0D, 1D, -0.0D
    ));

    public static final Collider GW_CLAW_CLEAVE = registerCollider(reg("gw_claw_cleave"), new MultiOBBCollider(5,
            1.7D, 0.5D, 1D, 0.0D, 1D, -1D
    ));
}


