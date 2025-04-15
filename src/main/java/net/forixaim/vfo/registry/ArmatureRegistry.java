package net.forixaim.vfo.registry;

import net.forixaim.vfo.VisitorsOfOmneria;
import net.forixaim.vfo.client.armatures.FlareSlashArmature;
import net.minecraftforge.fml.common.Mod;
import yesman.epicfight.gameasset.Armatures;

@Mod.EventBusSubscriber(modid = VisitorsOfOmneria.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ArmatureRegistry
{
    public static Armatures.ArmatureAccessor<FlareSlashArmature> FLARE_SLASH = Armatures.ArmatureAccessor.create(VisitorsOfOmneria.MOD_ID, "entity/flare_slash", FlareSlashArmature::new);
}
