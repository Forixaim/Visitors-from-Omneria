package net.forixaim.omneria.mixin.optional;

import com.brandon3055.draconicevolution.entity.GuardianCrystalEntity;
import com.brandon3055.draconicevolution.entity.guardian.DraconicGuardianEntity;
import com.brandon3055.draconicevolution.entity.guardian.control.LaserBeamPhase;
import com.brandon3055.draconicevolution.init.DEDamage;
import net.forixaim.battle_arts_api.battle_arts_skills.BattleArtsSkillSlots;
import net.forixaim.battle_arts_api.battle_arts_skills.CoreAPIDataKeys;
import net.forixaim.battle_arts_api.battle_arts_skills.battle_style.BattleStyle;
import net.forixaim.omneria.animations.battle_style.genesis_wyrm.GenesisWyrmAnimations;
import net.forixaim.omneria.registry.ParticleRegistry;
import net.forixaim.omneria.registry.SoundRegistry;
import net.forixaim.omneria.skill.DatakeyRegistry;
import net.forixaim.omneria.skill.OmneriaSkills;
import net.forixaim.omneria.util.ParticleUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;

import java.util.List;

@Mixin(LaserBeamPhase.class)
public class MixinDragonLaserBeamPhase
{
    @Shadow(remap = false)
    private Player attackTarget;


    @Shadow(remap = false)
    private int laserTime;

    @Shadow(remap = false)
    private int maxLaserTime;

    @Inject(method = "serverTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/damagesource/CombatTracker;recordDamage(Lnet/minecraft/world/damagesource/DamageSource;F)V", shift = At.Shift.AFTER), remap = false, cancellable = true)
    public void serverTick(CallbackInfo ci)
    {
        if (attackTarget != null && EpicFightCapabilities.getEntityPatch(attackTarget, PlayerPatch.class) instanceof ServerPlayerPatch serverPlayerPatch)
        {
            if (serverPlayerPatch.getSkill(BattleArtsSkillSlots.BATTLE_STYLE).hasSkill(OmneriaSkills.GENESIS_WYRM))
            {
                this.laserTime += maxLaserTime;
                LaserBeamPhase self = (LaserBeamPhase)(Object)this;
                ((PhaseAccessor)self).getGuardian().hurt(DEDamage.guardianLaser(serverPlayerPatch.getOriginal().serverLevel(), serverPlayerPatch.getOriginal()), 500);
                serverPlayerPatch.playSound(SoundRegistry.FPDC_EXPLOSION.get(),  1.0F, 1.0F);
                ParticleUtil.sendAlwaysVisibleParticles(serverPlayerPatch.getOriginal().serverLevel(), ParticleRegistry.DRACONIC_EXPLOSION.get(), serverPlayerPatch.getOriginal().getX(), serverPlayerPatch.getOriginal().getY(), serverPlayerPatch.getOriginal().getZ(), 1, 0, 0, 0, 0);
                AABB destruction = AABB.ofSize(serverPlayerPatch.getOriginal().position(), 100, 100, 100);
                List<Entity> entities = serverPlayerPatch.getOriginal().serverLevel().getEntities(serverPlayerPatch.getOriginal(), destruction);
                if (!entities.isEmpty())
                {
                    entities.forEach(entity -> {
                        if (entity instanceof GuardianCrystalEntity)
                        {
                            entity.kill();
                        }
                    });
                }
                if (serverPlayerPatch.getSkill(BattleArtsSkillSlots.BATTLE_STYLE).getDataManager().hasData(CoreAPIDataKeys.METER_FILL.get()) && serverPlayerPatch.getSkill(BattleArtsSkillSlots.BATTLE_STYLE).getSkill() instanceof BattleStyle battleStyle)
                {
                    serverPlayerPatch.getSkill(BattleArtsSkillSlots.BATTLE_STYLE).getDataManager().setDataSync(CoreAPIDataKeys.METER_FILL.get(), 1000f);
                }

                if (serverPlayerPatch.getSkill(BattleArtsSkillSlots.BATTLE_STYLE).getDataManager().hasData(DatakeyRegistry.TWILIGHT.get()) && !serverPlayerPatch.getSkill(BattleArtsSkillSlots.BATTLE_STYLE).getDataManager().getDataValue(DatakeyRegistry.TWILIGHT.get()))
                {
                    serverPlayerPatch.playSound(SoundRegistry.POWER_UP.get(),  1.0F, 1.0F);
                    serverPlayerPatch.getSkill(BattleArtsSkillSlots.BATTLE_STYLE).getDataManager().setDataSync(DatakeyRegistry.TWILIGHT.get(), true);

                }
                ci.cancel();
            }
        }
    }

    @Inject(method = "serverTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z"), remap = false, cancellable = true)
    public void reflectLaser(CallbackInfo ci)
    {
        LaserBeamPhase self = (LaserBeamPhase)(Object)this;

        if (attackTarget != null && EpicFightCapabilities.getEntityPatch(attackTarget, PlayerPatch.class) instanceof ServerPlayerPatch serverPlayerPatch) {
            SkillContainer bs = serverPlayerPatch.getSkill(BattleArtsSkillSlots.BATTLE_STYLE);
            if (bs.hasSkill(OmneriaSkills.GENESIS_WYRM)) {
                if (bs.getDataManager().hasData(DatakeyRegistry.TWILIGHT.get())) {
                    if (bs.getDataManager().getDataValue(DatakeyRegistry.TWILIGHT.get())) {
                        laserTime += maxLaserTime;
                        serverPlayerPatch.playAnimationSynchronized(GenesisWyrmAnimations.REFLECTION, 0);
                        ((PhaseAccessor)self).getGuardian().hurt(DEDamage.guardianLaser(serverPlayerPatch.getOriginal().serverLevel(), serverPlayerPatch.getOriginal()), 50);
                        ci.cancel();
                    }
                }
            }
        }
    }
}
