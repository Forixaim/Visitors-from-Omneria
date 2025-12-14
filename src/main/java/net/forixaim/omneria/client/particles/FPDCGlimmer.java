package net.forixaim.omneria.client.particles;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.forixaim.battle_arts_api.battle_arts_skills.BattleArtsSkillSlots;
import net.forixaim.omneria.client.particles.types.TrackingParticleType;
import net.forixaim.omneria.registry.ParticleRegistry;
import net.forixaim.omneria.skill.DatakeyRegistry;
import net.forixaim.omneria.world.entity.projectiles.DragonCannonBeam;
import net.forixaim.omneria.world.entity.projectiles.FullPowerDragonCannonBeam;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import yesman.epicfight.api.utils.math.OpenMatrix4f;
import yesman.epicfight.api.utils.math.Vec3f;
import yesman.epicfight.client.world.capabilites.entitypatch.player.LocalPlayerPatch;
import yesman.epicfight.model.armature.HumanoidArmature;
import yesman.epicfight.skill.SkillDataManager;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.EntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;

public class FPDCGlimmer extends TextureSheetParticle
{
    private final Entity entity;


    protected FPDCGlimmer(ClientLevel pLevel, double pX, double pY, double pZ, int entityId, SpriteSet sprites) {
        super(pLevel, pX, pY, pZ);
        entity = pLevel.getEntity(entityId);
        this.lifetime = 90;
        this.quadSize = 0.0f;
        this.alpha = 0.0f;
        this.setSpriteFromAge(sprites);
        if (EpicFightCapabilities.getEntityPatch(entity, EntityPatch.class) instanceof LivingEntityPatch<?> livingEntityPatch) {
            if (livingEntityPatch.getArmature() instanceof HumanoidArmature ha) {
                OpenMatrix4f jointMatrix = livingEntityPatch.getArmature().getBoundTransformFor(livingEntityPatch.getAnimator().getPose(0.0F), ha.handR).mulFront(OpenMatrix4f.createTranslation((float) livingEntityPatch.getOriginal().getX(), (float) livingEntityPatch.getOriginal().getY(), (float) livingEntityPatch.getOriginal().getZ()).mulBack(OpenMatrix4f.createRotatorDeg(180.0F, Vec3f.Y_AXIS).mulBack(livingEntityPatch.getModelMatrix(0.0F))));
                jointMatrix.translate(new Vec3f(0.0F, 0.75, 0F));
                pLevel.addParticle(ParticleRegistry.DRACONIC_BLAST_FLASH.get(), jointMatrix.toTranslationVector().x, jointMatrix.toTranslationVector().y, jointMatrix.toTranslationVector().z, 0, 0, 0);
            }
        }
    }

    @Override
    public void tick() {
        super.tick();
        this.roll += 1f;
        this.oRoll = roll;
    }

    @Override
    public void render(@NotNull VertexConsumer pBuffer, @NotNull Camera pRenderInfo, float pPartialTicks) {
        super.render(pBuffer, pRenderInfo, pPartialTicks);
        if (quadSize < 4f && age < 35)
        {
            this.quadSize += 1f;
        }
        if (alpha != 1f && age < 35)
            alpha += 0.1f;
        if (EpicFightCapabilities.getEntityPatch(entity, EntityPatch.class) instanceof LivingEntityPatch<?> livingEntityPatch)
        {
            if (livingEntityPatch instanceof LocalPlayerPatch lpp)
            {
                SkillDataManager dm = lpp.getSkill(BattleArtsSkillSlots.BATTLE_STYLE).getDataManager();
                if (dm.hasData(DatakeyRegistry.BEAM.get()))
                {
                    Entity e = level.getEntity(dm.getDataValue(DatakeyRegistry.BEAM.get()));
                    boolean pred = !((e instanceof DragonCannonBeam be && !be.isRemoved()) || age < 35);
                    if (pred)
                    {
                        remove();
                    }
                    else if (e instanceof FullPowerDragonCannonBeam)
                    {
                        if (livingEntityPatch.getArmature() instanceof HumanoidArmature ha) {
                            OpenMatrix4f jointMatrix = livingEntityPatch.getArmature().getBoundTransformFor(livingEntityPatch.getAnimator().getPose(0.0F), ha.toolR).mulFront(OpenMatrix4f.createTranslation((float) livingEntityPatch.getOriginal().getX(), (float) livingEntityPatch.getOriginal().getY(), (float) livingEntityPatch.getOriginal().getZ()).mulBack(OpenMatrix4f.createRotatorDeg(180.0F, Vec3f.Y_AXIS).mulBack(livingEntityPatch.getModelMatrix(0.0F))));
                            jointMatrix.translate(new Vec3f(0.0F, -2, 0F));
                            Vec3 translationVector = jointMatrix.toTranslationVector().toDoubleVector();
                            this.setPos(translationVector.x(), translationVector.y(), translationVector.z());
                        }
                    }
                    else
                    {
                        if (livingEntityPatch.getArmature() instanceof HumanoidArmature ha && this.age < 35) {
                            OpenMatrix4f jointMatrix = livingEntityPatch.getArmature().getBoundTransformFor(livingEntityPatch.getAnimator().getPose(0.0F), ha.toolR).mulFront(OpenMatrix4f.createTranslation((float) livingEntityPatch.getOriginal().getX(), (float) livingEntityPatch.getOriginal().getY(), (float) livingEntityPatch.getOriginal().getZ()).mulBack(OpenMatrix4f.createRotatorDeg(180.0F, Vec3f.Y_AXIS).mulBack(livingEntityPatch.getModelMatrix(0.0F))));
                            jointMatrix.translate(new Vec3f(0.0F, -0.3f, 0F));
                            Vec3 translationVector = jointMatrix.toTranslationVector().toDoubleVector();
                            this.setPos(translationVector.x(), translationVector.y(), translationVector.z());
                        }

                    }
                }

            }
        }
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_LIT;
    }

    public record Provider(SpriteSet sprites) implements ParticleProvider<TrackingParticleType> {

        @Override
        public Particle createParticle(@NotNull TrackingParticleType typeIn, @NotNull ClientLevel worldIn,
                                       double x, double y, double z,
                                       double xSpeed, double ySpeed, double zSpeed) {
            return new FPDCGlimmer(worldIn, x, y, z, typeIn.getEntityId(), sprites);

        }
    }
}
