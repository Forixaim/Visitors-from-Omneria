package net.forixaim.vfo.capabilities.ex_cap_weapons.movesets;

import net.forixaim.efm_ex.api.events.MoveSetDefinitionRegistryEvent;
import net.forixaim.efm_ex.api.moveset.MoveSet;
import net.forixaim.vfo.VisitorsOfOmneria;
import net.forixaim.vfo.animations.battle_style.imperatrice_lumiere.sword.LumiereSwordAnims;
import net.forixaim.vfo.skill.OmneriaSkills;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import yesman.epicfight.api.animation.LivingMotions;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.gameasset.EpicFightSkills;
import yesman.epicfight.skill.guard.GuardSkill;
import yesman.epicparcool.EpicParCool;
import yesman.epicparcool.ParcoolLivingMotions;

@SuppressWarnings("unchecked")
@Mod.EventBusSubscriber(modid = VisitorsOfOmneria.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ImperatriceMovesets
{
    public static MoveSet.MoveSetBuilder IMPERATRICE_SWORD_PRIMARY;

    @SubscribeEvent
    public static void registerMovesets(MoveSetDefinitionRegistryEvent event)
    {
        event.getMoveSets().put(VisitorsOfOmneria.MOD_ID, ImperatriceMovesets::build);
    }

    public static void build()
    {
        IMPERATRICE_SWORD_PRIMARY = MoveSet.builder()
                .addLivingMotionModifier(LivingMotions.IDLE, LumiereSwordAnims.IMPERATRICE_SWORD_IDLE_SET)
                .addLivingMotionModifier(LivingMotions.CREATIVE_IDLE, LumiereSwordAnims.IMPERATRICE_SWORD_FLY_IDLE)
                .addLivingMotionModifier(LivingMotions.CREATIVE_FLY, LumiereSwordAnims.IMPERATRICE_SWORD_FLY)
                .addLivingMotionModifier(LivingMotions.WALK, LumiereSwordAnims.IMPERATRICE_SWORD_WALK_SET)
                .addLivingMotionModifier(LivingMotions.RUN, LumiereSwordAnims.IMPERATRICE_SWORD_RUN_SET)
                .addLivingMotionModifier(LivingMotions.BLOCK, LumiereSwordAnims.IMPERATRICE_SWORD_GUARD_TRANSITION)
                .addGuardAnimations(EpicFightSkills.GUARD, GuardSkill.BlockType.GUARD, LumiereSwordAnims.IMPERATRICE_SWORD_GUARD_HIT)
                .addGuardAnimations(EpicFightSkills.PARRYING, GuardSkill.BlockType.GUARD, LumiereSwordAnims.IMPERATRICE_SWORD_GUARD_HIT)
                .addGuardAnimations(EpicFightSkills.PARRYING, GuardSkill.BlockType.ADVANCED_GUARD, LumiereSwordAnims.IMPERATRICE_SWORD_PARRY_1)
                .addAutoAttacks(
                LumiereSwordAnims.IMPERATRICE_SWORD_NEUTRAL_ATTACK,
                Animations.LONGSWORD_DASH,
                LumiereSwordAnims.IMPERATRICE_SWORD_SUNRISE
        ).setPassiveSkill(OmneriaSkills.IMPERATRICE_WP);
        if (ModList.get().isLoaded(EpicParCool.MODID))
        {
            IMPERATRICE_SWORD_PRIMARY.addLivingMotionModifier(ParcoolLivingMotions.FAST_RUN, LumiereSwordAnims.IMPERATRICE_SWORD_FAST_RUN_SET);
        }
    }
}
