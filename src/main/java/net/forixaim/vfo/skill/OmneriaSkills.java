package net.forixaim.vfo.skill;

import net.forixaim.bs_api.battle_arts_skills.active.combat_arts.CombatArt;
import net.forixaim.bs_api.battle_arts_skills.battle_style.BattleStyle;
import net.forixaim.efm_ex.skill.ExCapWeaponPassive;
import net.forixaim.vfo.VisitorsOfOmneria;
import net.forixaim.vfo.registry.CreativeTabRegistry;
import net.forixaim.vfo.skill.battle_style.imperatrice_lumiere.*;
import net.forixaim.vfo.skill.battle_style.imperatrice_lumiere.active.FireArts;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import yesman.epicfight.api.forgeevent.SkillBuildEvent;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillCategories;
import yesman.epicfight.skill.weaponinnate.WeaponInnateSkill;

@Mod.EventBusSubscriber(modid = VisitorsOfOmneria.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class OmneriaSkills
{
	public static Skill IMPERATRICE_LUMIERE;
	public static Skill FLARE_BLITZ;
	public static Skill INFERNAL_DRIVER;
	public static Skill FIRE_ARTS;
	public static Skill IMPERATRICE_WP;


	@SubscribeEvent
	public static void BuildSkillEvent(SkillBuildEvent OnBuild)
	{
		SkillBuildEvent.ModRegistryWorker registryWorker = OnBuild.createRegistryWorker(VisitorsOfOmneria.MOD_ID);
		INFERNAL_DRIVER = registryWorker.build("infernal_driver", InfernalDriver::new, WeaponInnateSkill.createWeaponInnateBuilder());
			IMPERATRICE_LUMIERE = registryWorker.build("imperatrice_lumiere", ImperatriceLumiere::new, BattleStyle.CreateBattleStyle().setCreativeTab(CreativeTabRegistry.VISITORS_OF_OMNERIA.get()));
			FLARE_BLITZ = registryWorker.build("flare_blitz", FlareBlitz::new, FlareBlitz.createImperatriceAttackSet().setCreativeTab(CreativeTabRegistry.VISITORS_OF_OMNERIA.get()));
			IMPERATRICE_WP = registryWorker.build("imperatrice_wp", ImperatriceWP::new, ExCapWeaponPassive.createBuilder().setCategory(SkillCategories.WEAPON_PASSIVE).setResource(Skill.Resource.NONE));
			FIRE_ARTS = registryWorker.build("fire_arts", FireArts::new, CombatArt.createCombatArt().setResource(Skill.Resource.COOLDOWN));
	}
}
