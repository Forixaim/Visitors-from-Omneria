package net.forixaim.omneria.skill;

import net.forixaim.battle_arts_api.battle_arts_skills.BattleArtsSkillCategories;
import net.forixaim.battle_arts_api.battle_arts_skills.active.burst_arts.BurstArt;
import net.forixaim.battle_arts_api.battle_arts_skills.active.combat_arts.CombatArt;
import net.forixaim.battle_arts_api.battle_arts_skills.battle_style.BattleStyle;
import net.forixaim.battle_arts_api.battle_arts_skills.mana_arts.ManaArt;
import net.forixaim.omneria.VisitorsOfOmneria;
import net.forixaim.omneria.animations.battle_style.imperatrice_lumiere.sword.LumiereSwordAnims;
import net.forixaim.omneria.registry.CreativeTabRegistry;
import net.forixaim.omneria.skill.battle_style.genesis_wyrm.*;
import net.forixaim.omneria.skill.battle_style.imperatrice_lumiere.*;
import net.forixaim.omneria.skill.battle_style.imperatrice_lumiere.active.FireArts;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import yesman.epicfight.api.forgeevent.SkillBuildEvent;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillCategories;
import yesman.epicfight.skill.dodge.DodgeSkill;
import yesman.epicfight.skill.dodge.StepSkill;
import yesman.epicfight.skill.guard.GuardSkill;
import yesman.epicfight.skill.weaponinnate.WeaponInnateSkill;

@Mod.EventBusSubscriber(modid = VisitorsOfOmneria.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class OmneriaSkills
{
	public static Skill IMPERATRICE_LUMIERE;
	public static Skill GENESIS_WYRM;
    public static Skill INITIAL_FORCE;
	public static Skill FLARE_BLITZ;
	public static Skill INFERNAL_DRIVER;
	public static Skill FIRE_ARTS;
	public static Skill IMPERATRICE_WP;
	public static Skill TRAILBLAZE;
    public static Skill PRIMORDIAL_BARRIER;
    public static Skill TWILIGHT;
    public static Skill DARK_ARTS;
    public static Skill COSMIC_FOCUS;
    public static Skill COSMIC_CHASER;


    @SubscribeEvent
	public static void BuildSkillEvent(SkillBuildEvent OnBuild)
	{
		SkillBuildEvent.ModRegistryWorker registryWorker = OnBuild.createRegistryWorker(VisitorsOfOmneria.MOD_ID);
		GENESIS_WYRM = registryWorker.build("genesis_wyrm", GenesisWyrm::new, BattleStyle.CreateBattleStyle());
		INFERNAL_DRIVER = registryWorker.build("infernal_driver", InfernalDriver::new, WeaponInnateSkill.createWeaponInnateBuilder());
		TRAILBLAZE = registryWorker.build("trailblaze", Trailblaze::new, DodgeSkill.createDodgeBuilder().setAnimations(Animations.BIPED_STEP_FORWARD, Animations.BIPED_STEP_BACKWARD, LumiereSwordAnims.IMPERATRICE_SWORD_TRAILBLAZE_LEFT, LumiereSwordAnims.IMPERATRICE_SWORD_TRAILBLAZE_RIGHT));
		IMPERATRICE_LUMIERE = registryWorker.build("imperatrice_lumiere", ImperatriceLumiere::new, BattleStyle.CreateBattleStyle().setCreativeTab(CreativeTabRegistry.VISITORS_OF_OMNERIA.get()));
		FLARE_BLITZ = registryWorker.build("flare_blitz", FlareBlitz::new, FlareBlitz.createImperatriceAttackSet().setCreativeTab(CreativeTabRegistry.VISITORS_OF_OMNERIA.get()));
		IMPERATRICE_WP = registryWorker.build("imperatrice_wp", ImperatriceWP::new, Skill.createBuilder().setCategory(SkillCategories.WEAPON_PASSIVE).setResource(Skill.Resource.NONE));
		FIRE_ARTS = registryWorker.build("fire_arts", FireArts::new, CombatArt.createCombatArt().setResource(Skill.Resource.COOLDOWN));
	    PRIMORDIAL_BARRIER = registryWorker.build("primordial_barrier", PrimordialBarrier::new, GuardSkill.createGuardBuilder());
        INITIAL_FORCE = registryWorker.build("initial_force", InitialForce::new, WeaponInnateSkill.createWeaponInnateBuilder().setResource(Skill.Resource.NONE));
        TWILIGHT = registryWorker.build("twilight", Twilight::new, BurstArt.createBurstArt().setResource(Skill.Resource.NONE));
        DARK_ARTS = registryWorker.build("dark_arts", DarkArts::new, DarkArts.createCombatArt().setResource(Skill.Resource.NONE));
        COSMIC_FOCUS = registryWorker.build("cosmic_focus", CosmicFocus::new, Skill.createBuilder().setResource(Skill.Resource.NONE).setCategory(BattleArtsSkillCategories.MANA_ART));
        COSMIC_CHASER = registryWorker.build("cosmic_chaser", CosmicChaser::new, Skill.createMoverBuilder().setResource(Skill.Resource.NONE));
    }
}
