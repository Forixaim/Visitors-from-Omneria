package net.forixaim.vfo.mixin;

import net.forixaim.vfo.world.entity.charlemagne.CharlemagnePatch;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yesman.epicfight.api.animation.types.AttackAnimation;
import yesman.epicfight.api.animation.types.DynamicAnimation;
import yesman.epicfight.api.asset.AssetAccessor;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;

@Mixin(value = AttackAnimation.class)
public abstract class MixinAttackAnimation
{
	@Inject(method = "end", at = @At("RETURN"), remap = false)
	public void nope(LivingEntityPatch<?> entitypatch, AssetAccessor<? extends DynamicAnimation> nextAnimation, boolean isEnd, CallbackInfo ci)
	{
		if (entitypatch instanceof CharlemagnePatch charlemagnePatch && isEnd)
		{
			charlemagnePatch.fireAttackAnimEndEvent();
		}
	}
}
