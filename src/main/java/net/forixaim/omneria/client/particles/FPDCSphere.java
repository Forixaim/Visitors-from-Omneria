package net.forixaim.omneria.client.particles;

import com.mojang.blaze3d.vertex.*;
import net.forixaim.battle_arts_api.battle_arts_skills.BattleArtsSkillSlots;
import net.forixaim.omneria.client.particles.types.TrackingParticleType;
import net.forixaim.omneria.registry.MeshRegistry;
import net.forixaim.omneria.registry.ParticleRegistry;
import net.forixaim.omneria.skill.DatakeyRegistry;
import net.forixaim.omneria.world.entity.projectiles.FullPowerDragonCannonBeam;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;
import yesman.epicfight.api.client.model.ClassicMesh;
import yesman.epicfight.api.client.model.Mesh;
import yesman.epicfight.api.client.model.Meshes;
import yesman.epicfight.api.utils.math.OpenMatrix4f;
import yesman.epicfight.api.utils.math.QuaternionUtils;
import yesman.epicfight.api.utils.math.Vec3f;
import yesman.epicfight.client.particle.CustomModelParticle;
import yesman.epicfight.client.particle.EpicFightParticleRenderTypes;
import yesman.epicfight.client.world.capabilites.entitypatch.player.LocalPlayerPatch;
import yesman.epicfight.model.armature.HumanoidArmature;
import yesman.epicfight.skill.SkillDataManager;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.EntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;

public class FPDCSphere extends CustomModelParticle<ClassicMesh>
{
    private final Entity entity;
    private int progression = 0;

    protected FPDCSphere(ClientLevel pLevel, double pX, double pY, double pZ, int entityId) {
        super(pLevel, pX, pY, pZ, 0, 0, 0, MeshRegistry.SPHERE);
        this.zd = 0;
        this.xd = 0;
        this.yd = 0;
        this.gCol = 0.0f;
        this.rCol = 0.941f;
        entity = pLevel.getEntity(entityId);
        this.lifetime = 90;

        this.scale = 0;
        if (EpicFightCapabilities.getEntityPatch(entity, EntityPatch.class) instanceof LivingEntityPatch<?> livingEntityPatch) {
            if (livingEntityPatch.getArmature() instanceof HumanoidArmature ha) {
                OpenMatrix4f jointMatrix = livingEntityPatch.getArmature().getBoundTransformFor(livingEntityPatch.getAnimator().getPose(0.0F), ha.handR).mulFront(OpenMatrix4f.createTranslation((float) livingEntityPatch.getOriginal().getX(), (float) livingEntityPatch.getOriginal().getY(), (float) livingEntityPatch.getOriginal().getZ()).mulBack(OpenMatrix4f.createRotatorDeg(180.0F, Vec3f.Y_AXIS).mulBack(livingEntityPatch.getModelMatrix(0.0F))));
                jointMatrix.translate(new Vec3f(0.0F, 1.5, 0F));
                pLevel.addParticle(ParticleRegistry.DRACONIC_BLAST_FLASH.get(), jointMatrix.toTranslationVector().x, jointMatrix.toTranslationVector().y, jointMatrix.toTranslationVector().z, 0, 0, 0);
            }
        }
        if (EpicFightCapabilities.getEntityPatch(entity, EntityPatch.class) instanceof LivingEntityPatch<?> livingEntityPatch) {
            if (livingEntityPatch.getArmature() instanceof HumanoidArmature ha) {
                OpenMatrix4f jointMatrix = livingEntityPatch.getArmature().getBoundTransformFor(livingEntityPatch.getAnimator().getPose(0.0F), ha.handR).mulFront(OpenMatrix4f.createTranslation((float) livingEntityPatch.getOriginal().getX(), (float) livingEntityPatch.getOriginal().getY(), (float) livingEntityPatch.getOriginal().getZ()).mulBack(OpenMatrix4f.createRotatorDeg(180.0F, Vec3f.Y_AXIS).mulBack(livingEntityPatch.getModelMatrix(0.0F))));
                jointMatrix.translate(new Vec3f(0.0F, 2, 0F));
                Vec3 translationVector = jointMatrix.toTranslationVector().toDoubleVector();
                this.setPos(translationVector.x(), translationVector.y(), translationVector.z());
            }
        }

    }

    @Override
    public void tick() {
        super.tick();
        if (scale < 1.8 && age < 35)
        {
            scale += 0.2f;
        }
        if (EpicFightCapabilities.getEntityPatch(entity, EntityPatch.class) instanceof LivingEntityPatch<?> livingEntityPatch) {
            if (livingEntityPatch.getArmature() instanceof HumanoidArmature ha) {
                OpenMatrix4f jointMatrix = livingEntityPatch.getArmature().getBoundTransformFor(livingEntityPatch.getAnimator().getPose(0.0F), ha.handR).mulFront(OpenMatrix4f.createTranslation((float) livingEntityPatch.getOriginal().getX(), (float) livingEntityPatch.getOriginal().getY(), (float) livingEntityPatch.getOriginal().getZ()).mulBack(OpenMatrix4f.createRotatorDeg(180.0F, Vec3f.Y_AXIS).mulBack(livingEntityPatch.getModelMatrix(0.0F))));
                jointMatrix.translate(new Vec3f(0.0F, 2, 0F));
                Vec3 translationVector = jointMatrix.toTranslationVector().toDoubleVector();
                this.setPos(translationVector.x(), translationVector.y(), translationVector.z());
            }
        }
        if (EpicFightCapabilities.getEntityPatch(entity, EntityPatch.class) instanceof LivingEntityPatch<?> livingEntityPatch)
        {
            if (livingEntityPatch instanceof LocalPlayerPatch lpp)
            {
                SkillDataManager dm = lpp.getSkill(BattleArtsSkillSlots.BATTLE_STYLE).getDataManager();
                if (dm.hasData(DatakeyRegistry.BEAM.get()))
                {
                    Entity e = level.getEntity(dm.getDataValue(DatakeyRegistry.BEAM.get()));
                    boolean pred = !((e instanceof FullPowerDragonCannonBeam be && !be.isRemoved()) || age < 35);
                    if (pred)
                    {
                        scale = Mth.cos((progression/10f) * Mth.PI * 0.5f) * 1.8F;
                        progression++;
                        if (scale <= 0f)
                        {
                            this.remove();
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void setupPoseStack(PoseStack poseStack, Camera camera, float partialTicks) {


        Vec3 cameraPosition = camera.getPosition();
        float x = (float)(this.x - cameraPosition.x());
        float y = (float)(this.y - cameraPosition.y());
        float z = (float)(this.z - cameraPosition.z());
        poseStack.translate(x, y, z);
        Quaternionf rotation = new Quaternionf(0.0F, 0.0F, 0.0F, 1.0F);
        float roll = Mth.lerp(partialTicks, this.oRoll, this.roll);
        float pitch = Mth.lerp(partialTicks, this.pitchO, this.pitch);
        float yaw = Mth.lerp(partialTicks, this.yawO, this.yaw);
        rotation.mul(QuaternionUtils.YP.rotationDegrees(180.0F - yaw));
        rotation.mul(QuaternionUtils.XP.rotationDegrees(pitch));
        rotation.mul(QuaternionUtils.ZP.rotationDegrees(roll));
        poseStack.mulPose(rotation);
        float scale = Mth.lerp(partialTicks, this.scaleO, this.scale);
        poseStack.scale(scale, scale, scale);
    }

    @Override
    public void render(@NotNull VertexConsumer pBuffer, @NotNull Camera pRenderInfo, float pPartialTicks) {
        PoseStack poseStack = new PoseStack();
        poseStack.pushPose();
        this.setupPoseStack(poseStack, pRenderInfo, pPartialTicks);
        this.prepareDraw(poseStack, pPartialTicks);
        this.particleMeshProvider.get().draw(poseStack, pBuffer, Mesh.DrawingFunction.POSITION_TEX_COLOR_LIGHTMAP, this.getLightColor(pPartialTicks), this.rCol, this.gCol, this.bCol, this.alpha, OverlayTexture.NO_OVERLAY);
        this.revert(poseStack);
        poseStack.popPose();
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return EpicFightParticleRenderTypes.TRANSLUCENT_GLOWING;
    }

    public record Provider(SpriteSet sprites) implements ParticleProvider<TrackingParticleType> {

        @Override
        public Particle createParticle(@NotNull TrackingParticleType typeIn, @NotNull ClientLevel worldIn,
                                       double x, double y, double z,
                                       double xSpeed, double ySpeed, double zSpeed) {
            return new FPDCSphere(worldIn, x, y, z, typeIn.getEntityId());

        }
    }
}
