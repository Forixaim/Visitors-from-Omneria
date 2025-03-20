package net.forixaim.vfo.skill;

import net.forixaim.vfo.VisitorsOfOmneria;
import net.forixaim.vfo.skill.battle_style.imperatrice_lumiere.FlareBlitz;
import net.forixaim.vfo.skill.battle_style.imperatrice_lumiere.ImperatriceLumiere;
import net.forixaim.vfo.skill.battle_style.imperatrice_lumiere.LumiereMovements;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import yesman.epicfight.api.utils.PacketBufferCodec;
import yesman.epicfight.main.EpicFightMod;
import yesman.epicfight.skill.SkillDataKey;

public class DatakeyRegistry
{
	public static final DeferredRegister<SkillDataKey<?>> DATA_KEYS = DeferredRegister.create(new ResourceLocation(EpicFightMod.MODID, "skill_data_keys"), VisitorsOfOmneria.MOD_ID);

	public static final RegistryObject<SkillDataKey<Boolean>> HIT = DATA_KEYS.register("hit", () ->
			SkillDataKey.createSkillDataKey(
					PacketBufferCodec.BOOLEAN,
					false,
					true,
					FlareBlitz.class
			)
	);

	public static final RegistryObject<SkillDataKey<Integer>> PREV_ANIM = DATA_KEYS.register("prev_anim", () ->
			SkillDataKey.createSkillDataKey(
					PacketBufferCodec.INTEGER,
					-1,
					true,
					FlareBlitz.class
			)
	);

	public static final RegistryObject<SkillDataKey<Integer>> BLAZE_COMBO = DATA_KEYS.register("blaze_combo", () ->
			SkillDataKey.createSkillDataKey(
					PacketBufferCodec.INTEGER,
					0,
					false,
					FlareBlitz.class
			)
	);



	public static final RegistryObject<SkillDataKey<Boolean>> LEFT_GROUND = DATA_KEYS.register(
			"left_ground", () -> SkillDataKey.createSkillDataKey(
					PacketBufferCodec.BOOLEAN,
					false,
					true,
					ImperatriceLumiere.class
			)
	);

	public static final RegistryObject<SkillDataKey<Boolean>> GUARDING = DATA_KEYS.register(
			"guarding", () -> SkillDataKey.createSkillDataKey(
					PacketBufferCodec.BOOLEAN,
					false,
					true,
					ImperatriceLumiere.class
			)
	);

	public static final RegistryObject<SkillDataKey<Boolean>> JUMPING = DATA_KEYS.register(
			"jumping", () -> SkillDataKey.createSkillDataKey(
					PacketBufferCodec.BOOLEAN,
					false,
					true,
					ImperatriceLumiere.class
			)
	);
}
