package net.forixaim.omneria.skill.battle_style.imperatrice_lumiere;

import net.forixaim.omneria.animations.battle_style.imperatrice_lumiere.sword.LumiereSwordAnims;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import yesman.epicfight.client.events.engine.ControlEngine;
import yesman.epicfight.skill.SkillBuilder;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.weaponinnate.WeaponInnateSkill;

public class InfernalDriver extends WeaponInnateSkill
{
    public InfernalDriver(SkillBuilder<? extends WeaponInnateSkill> builder)
    {
        super(builder.setResource(Resource.NONE));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public FriendlyByteBuf gatherArguments(SkillContainer container, ControlEngine controlEngine)
    {
        return ArgumentGatherers.UniversalDirectionalInput(container, null);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public Object getExecutionPacket(SkillContainer container, FriendlyByteBuf args)
    {
        return ArgumentGatherers.DirectionalExecutionPacket(container, args);
    }

    @Override
    public void executeOnServer(SkillContainer container, FriendlyByteBuf args)
    {
        super.executeOnServer(container, args);
        int fw = args.readInt();
        int sw = args.readInt();
        int ud = args.readInt();
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
