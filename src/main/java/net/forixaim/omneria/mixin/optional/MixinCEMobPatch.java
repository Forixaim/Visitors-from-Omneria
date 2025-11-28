package net.forixaim.omneria.mixin.optional;

import net.forixaim.battle_arts_api.battle_arts_skills.BattleArtsSkillSlots;
import net.forixaim.omneria.skill.battle_style.IIgnoresCEStunImmunity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.phys.Vec3;
import net.shelmarow.combat_evolution.ai.CEHumanoidPatch;
import net.shelmarow.combat_evolution.effect.CEMobEffects;
import net.shelmarow.combat_evolution.iml.ILivingEntityData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yesman.epicfight.particle.EpicFightParticles;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.EntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.damagesource.EpicFightDamageSource;
import yesman.epicfight.world.damagesource.StunType;
import yesman.epicfight.world.effect.EpicFightMobEffects;
import yesman.epicfight.world.entity.ai.attribute.EpicFightAttributes;

@Mixin(CEHumanoidPatch.class)
public class MixinCEMobPatch {

    @Inject(method = "onCommonHurt", at = @At("HEAD"), remap = false, cancellable = true)
    public void onCommonHurt(DamageSource source, CallbackInfo ci){
        CEHumanoidPatch patch = (CEHumanoidPatch)(Object)this;
        if (EpicFightCapabilities.getEntityPatch(source.getEntity(), EntityPatch.class) instanceof PlayerPatch<?> playerPatch)
        {
            if (playerPatch.getSkill(BattleArtsSkillSlots.BATTLE_STYLE).getSkill() instanceof IIgnoresCEStunImmunity)
            {
                if (!patch.getOriginal().hasEffect(EpicFightMobEffects.STUN_IMMUNITY.get()) && !patch.getOriginal().hasEffect(CEMobEffects.FULL_STUN_IMMUNITY.get())) {
                    ILivingEntityData entityData = (ILivingEntityData)this;
                    EpicFightDamageSource epicFightDamageSource = source instanceof EpicFightDamageSource efds ? efds : null;
                    float impact = 0.1F;
                    if (epicFightDamageSource != null) {
                        impact = epicFightDamageSource.calculateImpact();
                    }

                    float stamina = entityData.combat_evolution$getStamina(patch.getOriginal());
                    if (stamina > impact) {
                        entityData.combat_evolution$setStamina(patch.getOriginal(), stamina - impact);
                    } else {
                        float maxStamina = 15.0F;
                        if (patch.getOriginal().getAttribute(EpicFightAttributes.MAX_STAMINA.get()) != null) {
                            maxStamina = (float) patch.getOriginal().getAttributeValue(EpicFightAttributes.MAX_STAMINA.get());
                        }

                        patch.applyStun(StunType.NEUTRALIZE, 0.0F);
                        entityData.combat_evolution$setStamina(patch.getOriginal(), maxStamina);
                        patch.getOriginal().forceAddEffect(new MobEffectInstance(CEMobEffects.FULL_STUN_IMMUNITY.get(), 80), patch.getOriginal());
                        Vec3 eyePosition = patch.getOriginal().getEyePosition();
                        Vec3 viewVec = patch.getOriginal().getLookAngle().scale(2.0F);
                        Vec3 pos = new Vec3(eyePosition.x + viewVec.x, eyePosition.y + viewVec.y, eyePosition.z + viewVec.z);
                        patch.getOriginal().level().addParticle(EpicFightParticles.NEUTRALIZE.get(), pos.x, pos.y, pos.z, 0.0F, 0.0F, 0.0F);
                        patch.playGuardBreakSound();
                    }
                }
                ci.cancel();
            }
        }
    }
}
