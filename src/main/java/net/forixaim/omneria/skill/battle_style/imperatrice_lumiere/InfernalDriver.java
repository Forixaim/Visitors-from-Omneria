package net.forixaim.omneria.skill.battle_style.imperatrice_lumiere;

import net.forixaim.omneria.animations.battle_style.imperatrice_lumiere.sword.LumiereSwordAnims;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import yesman.epicfight.client.events.engine.ControlEngine;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.weaponinnate.WeaponInnateSkill;

public class InfernalDriver extends WeaponInnateSkill
{
    public InfernalDriver(Builder<?> builder)
    {
        super(builder.setResource(Resource.NONE));
    }

    @Override
    public void gatherArguments(SkillContainer container, ControlEngine controlEngine, CompoundTag arguments) {
        ArgumentGatherers.UniversalDirectionalInput(arguments);
    }

    @Override
    public CustomPacketPayload getExecutionPacket(SkillContainer container, CompoundTag args)
    {
        return ArgumentGatherers.DirectionalExecutionPacket(container, args);
    }

    @Override
    public void executeOnServer(SkillContainer container, CompoundTag args)
    {
        super.executeOnServer(container, args);
        int fw = args.getInt("front_back");
        if (fw == -1)
        {
            container.getServerExecutor().playAnimationSynchronized(LumiereSwordAnims.IMPERATRICE_SWORD_SOLAR_FLARE, 0);
        }
        else
        {
            container.getServerExecutor().playAnimationSynchronized(LumiereSwordAnims.IMPERATRICE_SWORD_SOLAR_DRIVE, 0);
        }
    }
}
