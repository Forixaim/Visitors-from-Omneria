package net.forixaim.omneria.animations;

import com.mojang.logging.LogUtils;
import net.forixaim.battle_arts_api.battle_arts_skills.BattleArtsSkillSlots;
import net.forixaim.battle_arts_api.battle_arts_skills.battle_style.BattleStyle;
import net.forixaim.omneria.registry.EntityRegistry;
import net.forixaim.omneria.registry.ParticleRegistry;
import net.forixaim.omneria.registry.SoundRegistry;
import net.forixaim.omneria.world.entity.projectiles.DragonShotProjectile;
import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import yesman.epicfight.api.animation.Joint;
import yesman.epicfight.api.animation.property.AnimationEvent;
import yesman.epicfight.api.animation.property.AnimationParameters;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.asset.AssetAccessor;
import yesman.epicfight.api.utils.math.OpenMatrix4f;
import yesman.epicfight.api.utils.math.Vec3f;
import yesman.epicfight.gameasset.EpicFightSounds;
import yesman.epicfight.model.armature.HumanoidArmature;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;

public class ReusableEvents
{
    public static void executeDelayedJump(ServerPlayerPatch player) {
        if (!player.getOriginal().onGround()) return;

        player.getOriginal().jumpFromGround();
    }

    public static void FIRE_DRAGON_SHOT(LivingEntityPatch<?> livingEntityPatch, AssetAccessor<? extends StaticAnimation> assetAccessor, AnimationParameters<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> animationParameters)
    {
        float ang = (float) ((livingEntityPatch.getYRot()+90)/180 * Math.PI);

        Vec3 position = new Vec3(livingEntityPatch.getOriginal().getLookAngle().x, 0, livingEntityPatch.getOriginal().getLookAngle().z).normalize().scale(1.5);
        Vec3 shootVec = new Vec3(Math.cos(ang), 0 , Math.sin(ang));
        Vec3 shootPos = livingEntityPatch.getOriginal().position().add(0, livingEntityPatch.getOriginal().getEyeHeight() - 0.5, 0).add(position);


        DragonShotProjectile projectile = EntityRegistry.DRAGON_SHOT.get().create(livingEntityPatch.getOriginal().level());

        if (projectile != null)
        {

            projectile.setPos(shootPos);
            projectile.shoot(shootVec.x(), 0, shootVec.z(), 4.2f, 0);

            if (livingEntityPatch.getArmature() instanceof HumanoidArmature && animationParameters.first() instanceof Joint joint && animationParameters.second() instanceof Float floatValue)
            {
                Vec3 lv = livingEntityPatch.getOriginal().getLookAngle();
                OpenMatrix4f jointMatrix = livingEntityPatch.getArmature().getBoundTransformFor(livingEntityPatch.getAnimator().getPose(0.0F), joint).mulFront(OpenMatrix4f.createTranslation((float) livingEntityPatch.getOriginal().getX(), (float) livingEntityPatch.getOriginal().getY(), (float) livingEntityPatch.getOriginal().getZ()).mulBack(OpenMatrix4f.createRotatorDeg(180.0F, Vec3f.Y_AXIS).mulBack(livingEntityPatch.getModelMatrix(0.0F))));
                jointMatrix.translate(0, 0.5f, 0);
                projectile.setPosRaw(jointMatrix.toTranslationVector().x, jointMatrix.toTranslationVector().y, jointMatrix.toTranslationVector().z);
                projectile.shoot(lv.x, lv.y, lv.z, 5, 0);
                if (!livingEntityPatch.isLogicalClient())
                {
                    ((ServerLevel)livingEntityPatch.getOriginal().level()).sendParticles(ParticleRegistry.DRACONIC_BLAST_FLASH.get(), jointMatrix.toTranslationVector().x, jointMatrix.toTranslationVector().y, jointMatrix.toTranslationVector().z, 1, 0, 0, 0, 0);
                }
            }
            projectile.setOwner(livingEntityPatch.getOriginal());
            livingEntityPatch.getOriginal().level().addFreshEntity(projectile);
            if (!livingEntityPatch.isLogicalClient())
                livingEntityPatch.playSound(SoundRegistry.BLAST.get(), 0.5f, 20.0f, 20.0f);
        }
    }
    public static AnimationEvent.E2<Joint, Float> FIRE_DRAGON_SHOT = (livingEntityPatch, assetAccessor, animationParameters) ->
    {

    };
}
