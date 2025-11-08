package net.forixaim.omneria.skill.battle_style.imperatrice_lumiere.active;

import net.forixaim.battle_arts_api.battle_arts_skills.active.combat_arts.CombatArt;
import net.forixaim.omneria.animations.battle_style.imperatrice_lumiere.sword.LumiereSwordAnims;
import net.forixaim.omneria.skill.DatakeyRegistry;
import net.forixaim.omneria.skill.battle_style.imperatrice_lumiere.ArgumentGatherers;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fml.ModList;
import yesman.epicfight.client.events.engine.ControlEngine;
import yesman.epicfight.skill.SkillBuilder;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import com.yesman.epicparcool.EpicParCool;
import com.yesman.epicparcool.ParcoolLivingMotions;

import java.util.UUID;

public class FireArts extends CombatArt
{
    private static final UUID EVENT_UUID = UUID.fromString("ab4576e8-7fe4-4c32-a83c-f7eea4d256db");
    public FireArts(SkillBuilder<? extends CombatArt> builder)
    {
        super(builder);
        allowedWeapons.add(CapabilityItem.WeaponCategories.LONGSWORD);
    }

    @Override
    public FriendlyByteBuf gatherArguments(SkillContainer container, ControlEngine controlEngine)
    {
        return ArgumentGatherers.UniversalDirectionalInput(container, controlEngine);
    }

    @Override
    public Object getExecutionPacket(SkillContainer container, FriendlyByteBuf args)
    {
        return ArgumentGatherers.DirectionalExecutionPacket(container, args);
    }

    @Override
    public boolean canExecute(SkillContainer container)
    {
        return super.canExecute(container);
    }

    @Override
    public void executeOnServer(SkillContainer container, FriendlyByteBuf args)
    {
        super.executeOnServer(container, args);
        int fw = args.readInt();
        int sw = args.readInt();
        int ud = args.readInt();

        if (ud == -1 || (ModList.get().isLoaded(EpicParCool.MODID) && container.getServerExecutor().getCurrentLivingMotion().isSame(ParcoolLivingMotions.FAST_RUN)))
        {
            if (container.getServerExecutor().getTarget() != null)
                container.getServerExecutor().playAnimationSynchronized(LumiereSwordAnims.IMPERATRICE_SWORD_FLAREDASH, 0);

        }
        else
        {
            if (!container.getExecutor().getOriginal().onGround() && container.getDataManager().getDataValue(DatakeyRegistry.FLARESPIN.get()))
            {
                container.getDataManager().setDataSync(DatakeyRegistry.FLARESPIN.get(), false);
                container.getServerExecutor().playAnimationSynchronized(LumiereSwordAnims.IMPERATRICE_SWORD_FLARESPIN, 0);
            }
            else
                container.getServerExecutor().playAnimationSynchronized(LumiereSwordAnims.IMPERATRICE_SWORD_FLARIAN_IMPALER, 0);
        }
    }

    @Override
    public void updateContainer(SkillContainer container)
    {
        if (container.getExecutor().getOriginal().onGround() && !container.getExecutor().isLogicalClient())
        {
            container.getDataManager().setDataSync(DatakeyRegistry.FLARESPIN.get(), true);
        }
        super.updateContainer(container);
    }
}
