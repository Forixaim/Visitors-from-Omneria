package net.forixaim.omneria.skill;

import net.forixaim.battle_arts_api.battle_arts_skills.mana_arts.ManaArt;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillBuilder;
import yesman.epicfight.skill.SkillContainer;

public class CosmicFocus extends ManaArt
{
    private float range;

    public CosmicFocus(SkillBuilder<? extends Skill> builder)
    {
        super(builder);
    }

    @Override
    public void setParams(CompoundTag parameters)
    {
        super.setParams(parameters);
        range = parameters.getFloat("range");
    }

    @Override
    public void executeOnServer(SkillContainer container, FriendlyByteBuf args)
    {
        super.executeOnServer(container, args);
        if (container.getDataManager().getDataValue(DatakeyRegistry.FOCUSED_TARGET.get()) != -1 && container.getExecutor().getTarget() == null)
        {
            container.getDataManager().setDataSync(DatakeyRegistry.FOCUSED_TARGET.get(), -1);
        }
        else
        {
            container.getDataManager().setDataSync(DatakeyRegistry.FOCUSED_TARGET.get(), container.getExecutor().getTarget().getId());
        }
    }

    @Override
    public void updateContainer(SkillContainer container)
    {
        super.updateContainer(container);
        if (!container.getExecutor().isLogicalClient() && container.getDataManager().getDataValue(DatakeyRegistry.FOCUSED_TARGET.get()) != -1)
        {
            Entity opponent = container.getServerExecutor().getOriginal().serverLevel().getEntity(container.getDataManager().getDataValue(DatakeyRegistry.FOCUSED_TARGET.get()));
            if (opponent == null || opponent.level() != container.getServerExecutor().getOriginal().level() || (opponent instanceof LivingEntity livingEntity && !livingEntity.isAlive()) || (range > 0 && container.getExecutor().getOriginal().distanceTo(opponent) > range))
            {
                 container.getDataManager().setDataSync(DatakeyRegistry.FOCUSED_TARGET.get(), -1);
            }
        }
    }
}
