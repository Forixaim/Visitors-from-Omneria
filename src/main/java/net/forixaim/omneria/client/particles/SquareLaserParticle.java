package net.forixaim.omneria.client.particles;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.forixaim.omneria.client.particles.types.BeamParticleType;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import yesman.epicfight.api.client.model.ClassicMesh;
import yesman.epicfight.api.client.model.Meshes;
import yesman.epicfight.api.utils.math.QuaternionUtils;
import yesman.epicfight.client.particle.CustomModelParticle;
import yesman.epicfight.client.particle.EpicFightParticleRenderTypes;


@OnlyIn(Dist.CLIENT)
public class SquareLaserParticle extends CustomModelParticle<ClassicMesh> {
	private final float length;
	private final float xRot;
	private final float yRot;
    private final float width;
	private final double speed;

	public SquareLaserParticle(ClientLevel level, double x, double y, double z, double toX, double toY, double toZ, float width, float r, float g, float b, float a, double speed) {
		super(level, x, y, z, 0, 0, 0, Meshes.LASER);
		this.lifetime = 12;
		this.width = width;
        this.rCol = r;
        this.gCol = g;
        this.bCol = b;
        this.alpha = a;
		this.speed = speed;

        double xLength = toX - x;
		double yLength = toY - y;
		double zLength = toZ - z;
		double horizontalDistance = (float)Math.sqrt(xLength * xLength + zLength * zLength);
		this.length = (float)Math.sqrt(xLength * xLength + yLength * yLength + zLength * zLength);
		this.yRot = (float)(Math.atan2(zLength, xLength) * (180D / Math.PI)) + 90.0F + 180.0F;
		this.xRot = (float)(Math.atan2(yLength, horizontalDistance) * (180D / Math.PI));
		
		this.setBoundingBox(new AABB(x, y, z, toX, toY, toZ));
	}

    @Override
    public void render(VertexConsumer vertexConsumer, Camera camera, float partialTicks) {
        super.render(vertexConsumer, camera, partialTicks);
    }

    @Override
	protected void setupPoseStack(PoseStack poseStack, Camera camera, float partialTick) {
		poseStack.pushPose();
		Vec3 cameraPosition = camera.getPosition();
		float x = (float)(Mth.lerp(partialTick, this.xo, this.x) - cameraPosition.x());
		float y = (float)(Mth.lerp(partialTick, this.yo, this.y) - cameraPosition.y());
		float z = (float)(Mth.lerp(partialTick, this.zo, this.z) - cameraPosition.z());
		poseStack.translate(x, y, z);
		poseStack.mulPose(QuaternionUtils.YP.rotationDegrees(180.0F - this.yRot));
		poseStack.mulPose(QuaternionUtils.XP.rotationDegrees(this.xRot));
		
		float progression = (this.age + partialTick) / (this.lifetime + 1);
		float scale = Mth.cos((progression * (float)Math.PI)/2);

		poseStack.scale(width * scale, width * scale, (float) ((float) (this.length / Math.E) + (progression * speed)));
	}
	
	@Override
	public @NotNull ParticleRenderType getRenderType() {
		return EpicFightParticleRenderTypes.TRANSLUCENT_GLOWING;
	}
	
	@OnlyIn(Dist.CLIENT)
	public static class Provider implements ParticleProvider<BeamParticleType> {
		@Override
		public Particle createParticle(@NotNull BeamParticleType typeIn, @NotNull ClientLevel level, double startX, double startY, double startZ, double endX, double endY, double endZ) {
			Vec3 endPos = typeIn.getEndPos();
            return new SquareLaserParticle(level, startX, startY, startZ, endPos.x, endPos.y, endPos.z, typeIn.getWidth(), typeIn.getRed(), typeIn.getGreen(), typeIn.getBlue(), typeIn.getAlpha(), typeIn.getSpeed());
		}
	}
}