package net.forixaim.omneria.mixin;

import net.forixaim.omneria.animations.types.OmneriaEntityStates;
import net.forixaim.omneria.combat.OmneriaDamageTypes;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.EntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.damagesource.EpicFightDamageSource;

@Mixin(LivingEntity.class)
public class MixinLivingEntity {
    @Inject(method = "isPushable", at = @At("RETURN"), cancellable = true)
    private void isPushable(CallbackInfoReturnable<Boolean> cir) {
        if (EpicFightCapabilities.getEntityPatch((LivingEntity)(Object)(this), EntityPatch.class) instanceof LivingEntityPatch<?> livingEntityPatch)
        {
            cir.setReturnValue(livingEntityPatch.getEntityState().getState(OmneriaEntityStates.CAN_BE_PUSHED));
        }
    }
}
