package net.forixaim.omneria.animations;

import net.forixaim.omneria.VisitorsOfOmneria;
import net.forixaim.omneria.animations.battle_style.genesis_wyrm.GenesisWyrmAnimations;
import net.forixaim.omneria.animations.battle_style.imperatrice_lumiere.sword.LumiereSwordAnims;
import net.forixaim.omneria.animations.entity.CharlemagneAnimations;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import yesman.epicfight.api.animation.AnimationManager;

@Mod.EventBusSubscriber(modid = VisitorsOfOmneria.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class AnimationRegistry
{
    @SubscribeEvent
    public static void registerAnims(AnimationManager.AnimationRegistryEvent event)
    {
        event.newBuilder(VisitorsOfOmneria.MOD_ID, AnimationRegistry::globalRegister);
    }

    public static void globalRegister(AnimationManager.AnimationBuilder builder)
    {
        LumiereSwordAnims.reg(builder);
        CharlemagneAnimations.build(builder);
        GenesisWyrmAnimations.build(builder);
    }
}
