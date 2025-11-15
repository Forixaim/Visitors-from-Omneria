package net.forixaim.omneria.world.entity.projectiles;

import com.mojang.logging.LogUtils;
import net.forixaim.omneria.client.particles.types.BeamParticleType;
import net.forixaim.omneria.registry.ParticleRegistry;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class DragonCannonBeam extends SimpleEnergyProjectile {

    private Vec3 originBeam = null;

    public DragonCannonBeam(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.lifetime = 40;
    }

    public void setOrigin(Vec3 origin) {
        this.originBeam = new Vec3(origin.x, origin.y, origin.z);
    }

    @Override
    public void tick() {
        super.tick();
        if (originBeam != null)
        {
            if (!this.level().isClientSide && !this.isRemoved())
            {
                ((ServerLevel)this.level()).sendParticles(new BeamParticleType(2f, ParticleRegistry.SQUARE_LASER.get(), 255, 0, 255, 255, position().x, position().y, position().z), originBeam.x, originBeam.y, originBeam.z, 1, 0, 0, 0, 0);
            }
        }
    }


}
