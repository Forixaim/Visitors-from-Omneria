package net.forixaim.omneria.capabilities.weapons;

import net.forixaim.efm_ex.EpicFightEXCapability;
import net.forixaim.efm_ex.capabilities.CoreCapability;
import net.forixaim.omneria.colliders.LumiereColliders;
import yesman.epicfight.gameasset.EpicFightSounds;

public class OmneriaExCapWeapons
{
    public static CoreCapability ORIGIN_JOYEUSE = CoreCapability.quickStart(
            builder -> builder.category(OmneriaCategories.ORIGIN_JOYEUSE)
                    .collider(LumiereColliders.JOYEUSE)
                    .hitSound(EpicFightSounds.BLADE_HIT.get())
                    .swingSound(EpicFightSounds.WHOOSH.get())
    , 1, 1, 1);

}
