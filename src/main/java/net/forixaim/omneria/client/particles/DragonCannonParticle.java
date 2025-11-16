package net.forixaim.omneria.client.particles;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.logging.LogUtils;
import net.forixaim.battle_arts_api.battle_arts_skills.BattleArtsSkillSlots;
import net.forixaim.omneria.client.particles.types.TrackingParticleType;
import net.forixaim.omneria.client.renderer.entity.projectile.DragonCannonRenderer;
import net.forixaim.omneria.skill.DatakeyRegistry;
import net.forixaim.omneria.world.entity.projectiles.DragonCannonBeam;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import yesman.epicfight.api.utils.math.OpenMatrix4f;
import yesman.epicfight.api.utils.math.Vec3f;
import yesman.epicfight.client.world.capabilites.entitypatch.player.LocalPlayerPatch;
import yesman.epicfight.model.armature.HumanoidArmature;
import yesman.epicfight.skill.SkillDataManager;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.EntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;

public class DragonCannonParticle extends TextureSheetParticle
{
    private final Entity entity;


    protected DragonCannonParticle(ClientLevel pLevel, double pX, double pY, double pZ, int entityId) {
        super(pLevel, pX, pY, pZ);
        entity = pLevel.getEntity(entityId);
        this.lifetime = 90;
        this.quadSize = 0.0f;
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public void render(@NotNull VertexConsumer pBuffer, @NotNull Camera pRenderInfo, float pPartialTicks) {
        if (quadSize < 0.5f)
        {
            this.quadSize = 0.01f;
        }
        if (EpicFightCapabilities.getEntityPatch(entity, EntityPatch.class) instanceof LivingEntityPatch<?> livingEntityPatch)
        {
            if (livingEntityPatch.getArmature() instanceof HumanoidArmature ha)
            {
                OpenMatrix4f jointMatrix = livingEntityPatch.getArmature().getBoundTransformFor(livingEntityPatch.getAnimator().getPose(0.0F), ha.handR).mulFront(OpenMatrix4f.createTranslation((float) livingEntityPatch.getOriginal().getX(), (float) livingEntityPatch.getOriginal().getY(), (float) livingEntityPatch.getOriginal().getZ()).mulBack(OpenMatrix4f.createRotatorDeg(180.0F, Vec3f.Y_AXIS).mulBack(livingEntityPatch.getModelMatrix(0.0F))));
                LogUtils.getLogger().debug(jointMatrix.toTranslationVector().toString());
                jointMatrix.translate(new Vec3f(0.0F, 1, 0F));
                this.setPos(jointMatrix.toTranslationVector().x, jointMatrix.toTranslationVector().y, jointMatrix.toTranslationVector().z);
            }
            if (livingEntityPatch instanceof LocalPlayerPatch lpp)
            {
                SkillDataManager dm = lpp.getSkill(BattleArtsSkillSlots.BATTLE_STYLE).getDataManager();
                if (dm.hasData(DatakeyRegistry.BEAM.get()))
                {
                    Entity e = level.getEntity(dm.getDataValue(DatakeyRegistry.BEAM.get()));
                    if (!(e instanceof DragonCannonBeam be && !be.isRemoved() || lifetime < 35))
                    {
                        this.quadSize -= 0.01f;
                        if (this.quadSize <= 0f)
                        {
                            this.remove();
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
            return new GenesisAuraParticle(worldIn, x, y, z, typeIn.getEntityId(), sprites);

        }
    }
}
