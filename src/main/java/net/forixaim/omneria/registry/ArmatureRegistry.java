package net.forixaim.omneria.registry;

import net.forixaim.omneria.VisitorsOfOmneria;
import net.forixaim.omneria.client.armatures.CharlemagneArmature;
import net.forixaim.omneria.client.armatures.FlareSlashArmature;
import net.minecraftforge.fml.common.Mod;
import yesman.epicfight.gameasset.Armatures;
import yesman.epicfight.model.armature.HumanoidArmature;

@Mod.EventBusSubscriber(modid = VisitorsOfOmneria.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ArmatureRegistry
{
    public static Armatures.ArmatureAccessor<CharlemagneArmature> CHARLEMAGNE = Armatures.ArmatureAccessor.create(VisitorsOfOmneria.MOD_ID, "entity/charlemagne", CharlemagneArmature::new);
    public static Armatures.ArmatureAccessor<FlareSlashArmature> FLARE_SLASH = Armatures.ArmatureAccessor.create(VisitorsOfOmneria.MOD_ID, "entity/flare_slash", FlareSlashArmature::new);
}
