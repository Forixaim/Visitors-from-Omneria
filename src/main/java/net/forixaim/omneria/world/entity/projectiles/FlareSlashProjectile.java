package net.forixaim.omneria.world.entity.projectiles;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;

public class FlareSlashProjectile extends Projectile
{
    protected int lifetime = 80;
    protected Vec3 deceleration = null;
    protected double decelerationConstant = 0.2;
    protected float damage = 1;
    protected int maxStrikes = 1;

    public FlareSlashProjectile(EntityType<? extends Projectile> pEntityType, Level pLevel)
    {
        super(pEntityType, pLevel);
    }

    @Override
    public void tick()
    {
        super.tick();

        Vec3 originalVec = this.getDeltaMovement();
        if (deceleration == null)
        {
            deceleration = originalVec.multiply(decelerationConstant, decelerationConstant, decelerationConstant);
        }

        HitResult hitresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
        if (hitresult.getType() != HitResult.Type.MISS && !ForgeEventFactory.onProjectileImpact(this, hitresult)) {
            this.onHit(hitresult);
        }

        double d7;
        double d2;
        double d3;
        if (magnitude(originalVec) > 0.0)
        {
            Vec3 moveVec = originalVec.subtract(deceleration);
            double d5 = moveVec.x;
            double d6 = moveVec.y;
            double d1 = moveVec.z;
            d7 = this.getX() + d5;
            d2 = this.getY() + d6;
            d3 = this.getZ() + d1;
        }
        else
        {
            d7 = this.getX() + 0;
            d2 = this.getY() + 0;
            d3 = this.getZ() + 0;
        }
        this.setPos(d7, d2, d3);
        lifetime--;
        if (lifetime <= 0)
        {
            this.discard();
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult pResult)
    {

        this.discard();
    }

    private double magnitude(Vec3 vec)
    {
        return Math.sqrt(vec.x * vec.x + vec.y * vec.y + vec.z * vec.z);
    }

    @Override
    protected void defineSynchedData()
    {

    }
}
