package net.forixaim.omneria.client.particles;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import yesman.epicfight.api.animation.AnimationManager;
import yesman.epicfight.api.animation.Joint;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.asset.AssetAccessor;
import yesman.epicfight.api.client.animation.property.ClientAnimationProperties;
import yesman.epicfight.api.client.animation.property.TrailInfo;
import yesman.epicfight.client.ClientEngine;
import yesman.epicfight.client.particle.AnimationTrailParticle;
import yesman.epicfight.client.renderer.patched.item.RenderItemBase;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.capabilities.item.CapabilityItem;

import java.util.List;
import java.util.Optional;

public class BillboardAnimationTrailParticle extends AnimationTrailParticle
{
    protected BillboardAnimationTrailParticle(ClientLevel level, LivingEntityPatch<?> owner, Joint joint, AssetAccessor<? extends StaticAnimation> animation, TrailInfo trailInfo) {
        super(level, owner, joint, animation, trailInfo);
    }

    @Override
    public void render(VertexConsumer vertexConsumer, Camera camera, float partialTick) {
        if (this.trailEdges.isEmpty()) return;
        PoseStack poseStack = new PoseStack();
        int light = this.getLightColor(partialTick);
        this.setupPoseStack(poseStack, camera, partialTick);
        Matrix4f matrix4f = poseStack.last().pose();
        int edges = this.trailEdges.size() - 1;
        boolean startFade = this.trailEdges.get(0).lifetime == 1;
        boolean endFade = this.trailEdges.get(edges).lifetime == this.trailInfo.trailLifetime();
        float startEdge = (startFade ? (float)(this.trailInfo.interpolateCount() * 2) * partialTick : 0.0F) + this.startEdgeCorrection;
        float endEdge = endFade ? Math.min((float)edges - (float)(this.trailInfo.interpolateCount() * 2) * (1.0F - partialTick), (float)(edges - 1)) : (float)(edges - 1);
        float interval = 1.0F / (endEdge - startEdge);
        float fading = 1.0F;
        if (this.shouldRemove) {
            if (TrailInfo.isValidTime(this.trailInfo.fadeTime())) {
                fading = (float)(this.lifetime - this.age) / (float)this.trailInfo.trailLifetime();
            } else {
                fading = Mth.clamp(((float)(this.lifetime - this.age) + (1.0F - partialTick)) / (float)this.trailInfo.trailLifetime(), 0.0F, 1.0F);
            }
        }
        float partialStartEdge = interval * (startEdge % 1.0F);
        float from = -partialStartEdge;
        float to = -partialStartEdge + interval;
        Vec3 camPos = camera.getPosition();
        for (int i = (int)startEdge; i < (int)endEdge + 1; ++i) {
            TrailEdge e1 = this.trailEdges.get(i);
            TrailEdge e2 = this.trailEdges.get(i + 1);
            Vec3 p1 = e1.start;
            Vec3 p2 = e2.start;
            Vec3 center = p1.add(p2).scale(0.5);
            Vec3 toCam = camPos.subtract(center).normalize();
            Vec3 dir = p2.subtract(p1).normalize();
            float width = (float) p1.distanceTo(p2);
            Vec3 right = dir.cross(toCam).normalize().scale(width * 0.5f);
            Vec3 q1 = p1.add(right);
            Vec3 q2 = p1.subtract(right);
            Vec3 q3 = p2.subtract(right);
            Vec3 q4 = p2.add(right);
            Vector4f pos1 = new Vector4f((float)q1.x, (float)q1.y, (float)q1.z, 1);
            Vector4f pos2 = new Vector4f((float)q2.x, (float)q2.y, (float)q2.z, 1);
            Vector4f pos3 = new Vector4f((float)q3.x, (float)q3.y, (float)q3.z, 1);
            Vector4f pos4 = new Vector4f((float)q4.x, (float)q4.y, (float)q4.z, 1);
            pos1.mul(matrix4f);
            pos2.mul(matrix4f);
            pos3.mul(matrix4f);
            pos4.mul(matrix4f);
            float alphaFrom = Mth.clamp(from, 0.0F, 1.0F);
            float alphaTo = Mth.clamp(to, 0.0F, 1.0F);
            vertexConsumer.vertex(pos1.x(), pos1.y(), pos1.z()).uv(from, 1.0F).color(this.rCol, this.gCol, this.bCol, this.alpha * alphaFrom * fading).uv2(light).endVertex();
            vertexConsumer.vertex(pos2.x(), pos2.y(), pos2.z()).uv(from, 0.0F).color(this.rCol, this.gCol, this.bCol, this.alpha * alphaFrom * fading).uv2(light).endVertex();
            vertexConsumer.vertex(pos3.x(), pos3.y(), pos3.z()).uv(to, 0.0F).color(this.rCol, this.gCol, this.bCol, this.alpha * alphaTo * fading).uv2(light).endVertex();
            vertexConsumer.vertex(pos4.x(), pos4.y(), pos4.z()).uv(to, 1.0F).color(this.rCol, this.gCol, this.bCol, this.alpha * alphaTo * fading).uv2(light).endVertex();
            from += interval;
            to += interval;
        }
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        public Particle createParticle(@NotNull SimpleParticleType typeIn, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            int eid = (int)Double.doubleToRawLongBits(x);
            int animid = (int)Double.doubleToRawLongBits(z);
            int jointId = (int)Double.doubleToRawLongBits(xSpeed);
            int idx = (int)Double.doubleToRawLongBits(ySpeed);
            Entity entity = level.getEntity(eid);
            if (entity == null) {
                return null;
            } else {
                LivingEntityPatch<?> entitypatch = (LivingEntityPatch) EpicFightCapabilities.getEntityPatch(entity, LivingEntityPatch.class);
                if (entitypatch == null) {
                    return null;
                } else {
                    AnimationManager.AnimationAccessor<? extends StaticAnimation> animation = AnimationManager.byId(animid);
                    if (animation == null) {
                        return null;
                    } else {
                        Optional<List<TrailInfo>> trailInfo = animation.get().getProperty(ClientAnimationProperties.TRAIL_EFFECT);
                        if (trailInfo.isEmpty()) {
                            return null;
                        } else {
                            TrailInfo result = (TrailInfo)((List)trailInfo.get()).get(idx);
                            if (result.hand() != null) {
                                ItemStack stack = entitypatch.getOriginal().getItemInHand(result.hand());
                                RenderItemBase renderItemBase = ClientEngine.getInstance().renderEngine.getItemRenderer(stack);
                                if (renderItemBase != null && renderItemBase.trailInfo() != null) {
                                    result = renderItemBase.trailInfo().overwrite(result);
                                }
                            }

                            result = entitypatch.getEntityDecorations().getModifiedTrailInfo(result, result.hand() == null ? CapabilityItem.EMPTY : entitypatch.getAdvancedHoldingItemCapability(result.hand()));
                            return result.playable() ? new BillboardAnimationTrailParticle(level, entitypatch, entitypatch.getArmature().searchJointById(jointId), animation, result) : null;
                        }
                    }
                }
            }
        }
    }
}
