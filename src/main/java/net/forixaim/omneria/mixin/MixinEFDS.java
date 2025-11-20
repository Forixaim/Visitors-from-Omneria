package net.forixaim.omneria.mixin;

import net.forixaim.omneria.combat.OmneriaDamageTypes;
import net.forixaim.omneria.util.DeathMessageLists;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import yesman.epicfight.world.damagesource.EpicFightDamageSource;

@Mixin(DamageSource.class)
public class MixinEFDS {
    @Inject(method = "getLocalizedDeathMessage", at = @At("HEAD"), cancellable = true)
    public void getDeath(LivingEntity pLivingEntity, CallbackInfoReturnable<Component> cir)
    {
        if (((DamageSource)(Object)this) instanceof EpicFightDamageSource epicFightDamageSource)
        {
            if (epicFightDamageSource.is(OmneriaDamageTypes.FP_DRAGON_CANNON))
            {
                RandomSource rng = RandomSource.create();
                cir.setReturnValue(DeathMessageLists.FPDC_DEATH_MESSAGES.get(rng.nextInt(DeathMessageLists.FPDC_DEATH_MESSAGES.size())));
            }
        }
    }
}
