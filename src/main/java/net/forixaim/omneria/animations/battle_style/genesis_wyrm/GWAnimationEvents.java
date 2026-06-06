package net.forixaim.omneria.animations.battle_style.genesis_wyrm;

import net.forixaim.omneria.registry.EntityRegistry;
import net.forixaim.omneria.registry.SoundRegistry;
import net.forixaim.omneria.world.entity.projectiles.DarkBangProjectile;
import net.forixaim.omneria.world.entity.projectiles.DragonPulse;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.Vec3;
import yesman.epicfight.api.animation.property.AnimationParameters;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.asset.AssetAccessor;
import yesman.epicfight.api.utils.math.OpenMatrix4f;
import yesman.epicfight.api.utils.math.Vec3f;
import yesman.epicfight.model.armature.HumanoidArmature;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;

public class GWAnimationEvents
{
    public static void FIRE_DRAGON_PULSE(LivingEntityPatch<?> livingEntityPatch, AssetAccessor<? extends StaticAnimation> accessor, AnimationParameters<Object, Object, Object,Object,Object,Object,Object,Object,Object,Object> parameters) {
//        float ang = (float) ((livingEntityPatch.getYRot() + 90) / 180 * Math.PI);
//
//        Vec3 position = new Vec3(livingEntityPatch.getOriginal().getLookAngle().x, 0, livingEntityPatch.getOriginal().getLookAngle().z).normalize().scale(1.5);
//        Vec3 shootVec = new Vec3(Math.cos(ang), 0, Math.sin(ang));
//        Vec3 shootPos = livingEntityPatch.getOriginal().position().add(0, livingEntityPatch.getOriginal().getEyeHeight() - 0.5, 0).add(position);
//
//
//        DragonPulse projectile = EntityRegistry.DRAGON_PULSE.get().create(livingEntityPatch.getOriginal().level());
//
//        if (projectile != null) {
//
//            projectile.setPos(shootPos);
//            projectile.shoot(shootVec.x(), 0, shootVec.z(), 4.2f, 0);
//            projectile.setDamageSource(livingEntityPatch.getDamageSource(accessor.get().getRealAnimation(), InteractionHand.MAIN_HAND));
//            if (livingEntityPatch.getArmature() instanceof HumanoidArmature ha) {
//                Vec3 lv = livingEntityPatch.getOriginal().getLookAngle();
//                OpenMatrix4f jointMatrix = livingEntityPatch.getArmature().getBoundTransformFor(livingEntityPatch.getAnimator().getPose(0.0F), ha.handR).mulFront(OpenMatrix4f.createTranslation((float) livingEntityPatch.getOriginal().getX(), (float) livingEntityPatch.getOriginal().getY(), (float) livingEntityPatch.getOriginal().getZ()).mulBack(OpenMatrix4f.createRotatorDeg(180.0F, Vec3f.Y_AXIS).mulBack(livingEntityPatch.getModelMatrix(0.0F))));
//                jointMatrix.translate(0, 0.5f, 0);
//                projectile.setPosRaw(jointMatrix.toTranslationVector().x, jointMatrix.toTranslationVector().y, jointMatrix.toTranslationVector().z);
//                projectile.shoot(lv.x, lv.y, lv.z, 4.2f, 0);
//
//            }
//            projectile.setOwner(livingEntityPatch.getOriginal());
//            livingEntityPatch.playSound(SoundRegistry.HEAVY_BLAST.get(), 0, 0);
//            livingEntityPatch.getOriginal().level().addFreshEntity(projectile);
//
//        }
    }
}
