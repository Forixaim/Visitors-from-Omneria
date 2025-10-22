package net.forixaim.omneria.capabilities.weapons;

import net.forixaim.ex_cap.capabilities.ExCapWeapon;
import net.forixaim.ex_cap.capabilities.weapon_presets.ExCapWeapons;
import net.forixaim.omneria.VisitorsOfOmneria;
import net.forixaim.omneria.colliders.LumiereColliders;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import yesman.epicfight.gameasset.EpicFightSounds;

public class OmneriaExCapWeapons
{
    public static final DeferredRegister<ExCapWeapon> EX_CAP_WEAPONS = DeferredRegister.create(ExCapWeapons.REGISTRY_KEY, VisitorsOfOmneria.MOD_ID);

    public static RegistryObject<ExCapWeapon> ORIGIN_JOYEUSE = EX_CAP_WEAPONS.register("origin_joyeuse", () -> ExCapWeapon.quickStart(
            builder -> builder.category(OmneriaCategories.ORIGIN_JOYEUSE)
                    .collider(LumiereColliders.JOYEUSE)
                    .hitSound(EpicFightSounds.BLADE_HIT.get())
                    .swingSound(EpicFightSounds.WHOOSH.get())
    , 1, 1, 1));

}
