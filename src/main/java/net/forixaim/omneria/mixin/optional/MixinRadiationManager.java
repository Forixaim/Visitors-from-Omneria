package net.forixaim.omneria.mixin.optional;


import mekanism.common.lib.radiation.RadiationManager;
import net.forixaim.omneria.world.entity.special_tags.IRadiationImmune;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = RadiationManager.class)
public class MixinRadiationManager
{
	@Inject(method = "getRadiationResistance", at = @At("RETURN"), cancellable = true, remap = false)
	private void addImmuneMobs(LivingEntity entity, CallbackInfoReturnable<Double> cir)
	{
		if (entity instanceof IRadiationImmune)
		{
			cir.setReturnValue(1.0);
		}
	}
}
