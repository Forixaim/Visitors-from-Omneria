package net.forixaim.vfo.animations.types;

import net.forixaim.bs_api.battle_arts_skills.BattleArtsSkillSlots;
import net.forixaim.efm_ex.capabilities.CoreCapability;
import net.forixaim.vfo.skill.DatakeyRegistry;
import net.minecraft.server.level.ServerPlayer;
import yesman.epicfight.api.animation.AnimationManager;
import yesman.epicfight.api.animation.types.DynamicAnimation;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.asset.AssetAccessor;
import yesman.epicfight.api.model.Armature;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.item.CapabilityItem;

public class JumpAnimation extends StaticAnimation
{

    public JumpAnimation(boolean isRepeat, AnimationManager.AnimationAccessor<? extends StaticAnimation> accessor, AssetAccessor<? extends Armature> armature) {
        super(0.15F, isRepeat, accessor, armature);
    }

    public JumpAnimation(float transitionTime, boolean isRepeat, AnimationManager.AnimationAccessor<? extends StaticAnimation> accessor, AssetAccessor<? extends Armature> armature) {
        super(transitionTime, isRepeat, accessor, armature);
    }

    @Override
    public void end(LivingEntityPatch<?> entitypatch, AssetAccessor<? extends DynamicAnimation> nextAnimation, boolean isEnd)
    {
        if (entitypatch instanceof PlayerPatch<?> playerPatch && !playerPatch.isLogicalClient())
        {
            if (playerPatch.getSkill(BattleArtsSkillSlots.BATTLE_STYLE).getDataManager().hasData(DatakeyRegistry.JUMPING.get()))
            {
                playerPatch.getSkill(BattleArtsSkillSlots.BATTLE_STYLE).getDataManager().setDataSync(DatakeyRegistry.JUMPING.get(), false, (ServerPlayer) playerPatch.getOriginal());
            }
        }
        super.end(entitypatch, nextAnimation, isEnd);

    }
}
