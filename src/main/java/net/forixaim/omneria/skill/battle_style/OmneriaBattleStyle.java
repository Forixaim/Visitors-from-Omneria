package net.forixaim.omneria.skill.battle_style;

import net.forixaim.bs_api.battle_arts_skills.battle_style.BattleStyle;
import net.forixaim.omneria.skill.DatakeyRegistry;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;

public class OmneriaBattleStyle extends BattleStyle
{

    public OmneriaBattleStyle(Builder<?> builder)
    {
        super(builder);
    }

    @Override
    public void updateContainer(SkillContainer container)
    {
        super.updateContainer(container);
        if (!container.getExecutor().isLogicalClient())
        {
            LivingEntityPatch<?> entityPatch = EpicFightCapabilities.getEntityPatch(container.getServerExecutor().getOriginal().getLastHurtMob(), LivingEntityPatch.class);
            if ((entityPatch != null && (!entityPatch.isStunned() || entityPatch.getOriginal().isDeadOrDying()) && container.getDataManager().hasData(DatakeyRegistry.TRUE_COMBO_COUNT.get())))
            {
                container.getDataManager().setDataSync(DatakeyRegistry.TRUE_COMBO_COUNT.get(), 0, container.getServerExecutor().getOriginal());
            }
        }
    }
}
