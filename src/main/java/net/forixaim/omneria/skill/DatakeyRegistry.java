package net.forixaim.omneria.skill;

import net.forixaim.omneria.VisitorsOfOmneria;
import net.forixaim.omneria.skill.battle_style.genesis_wyrm.GenesisWyrm;
import net.forixaim.omneria.skill.battle_style.genesis_wyrm.InitialForce;
import net.forixaim.omneria.skill.battle_style.imperatrice_lumiere.ImperatriceLumiere;
import net.forixaim.omneria.skill.battle_style.imperatrice_lumiere.active.FireArts;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import yesman.epicfight.api.utils.PacketBufferCodec;
import yesman.epicfight.main.EpicFightMod;
import yesman.epicfight.skill.SkillDataKey;

public class DatakeyRegistry
{
	public static final DeferredRegister<SkillDataKey<?>> DATA_KEYS = DeferredRegister.create(ResourceLocation.fromNamespaceAndPath(EpicFightMod.MODID, "skill_data_keys"), VisitorsOfOmneria.MOD_ID);

	public static final RegistryObject<SkillDataKey<Boolean>> HIT = DATA_KEYS.register("hit", () ->
			SkillDataKey.createSkillDataKey(
					PacketBufferCodec.BOOLEAN,
					false,
					true,
					FlareBlitz.class
			)
	);

    public static final RegistryObject<SkillDataKey<Boolean>> ULT_HELD = DATA_KEYS.register("ult_held", () ->
            SkillDataKey.createSkillDataKey(
                    PacketBufferCodec.BOOLEAN,
                    false,
                    true,
                    GenesisWyrm.class
            )
    );


    public static final RegistryObject<SkillDataKey<Integer>> DODGE_ANIM = DATA_KEYS.register("dodge_anim", () ->
            SkillDataKey.createSkillDataKey(
                    PacketBufferCodec.INTEGER,
                    -1,
                    true,
                    GenesisWyrm.class
            )
    );

    public static final RegistryObject<SkillDataKey<Boolean>> SHIFT = DATA_KEYS.register("shift", () ->
            SkillDataKey.createSkillDataKey(
                    PacketBufferCodec.BOOLEAN,
                    false,
                    true,
                    GenesisWyrm.class
            )
    );

    public static final RegistryObject<SkillDataKey<Boolean>> TWILIGHT = DATA_KEYS.register("twilight", () ->
            SkillDataKey.createSkillDataKey(
                    PacketBufferCodec.BOOLEAN,
                    false,
                    true,
                    GenesisWyrm.class
            )
    );

	public static final RegistryObject<SkillDataKey<Boolean>> RIGHT_CLICKED = DATA_KEYS.register("right_clicked", () ->
			SkillDataKey.createSkillDataKey(
					PacketBufferCodec.BOOLEAN,
					false,
					true,
					GenesisWyrm.class
			)
	);

	public static final RegistryObject<SkillDataKey<Integer>> FOCUSED_TARGET = DATA_KEYS.register("focused_target", () ->
			SkillDataKey.createSkillDataKey(PacketBufferCodec.INTEGER, -1, true, CosmicFocus.class));

    public static final RegistryObject<SkillDataKey<Integer>> OPPONENT = DATA_KEYS.register("opponent", () ->
            SkillDataKey.createSkillDataKey(
                    PacketBufferCodec.INTEGER,
                    -1,
                    true,
                    GenesisWyrm.class
            )
    );

    public static final RegistryObject<SkillDataKey<Integer>> BEAM = DATA_KEYS.register("beam", () ->
            SkillDataKey.createSkillDataKey(
                    PacketBufferCodec.INTEGER,
                    -1,
                    true,
                    GenesisWyrm.class
            )
    );

    public static final RegistryObject<SkillDataKey<Boolean>> PROJECTILE = DATA_KEYS.register("projectile", () ->
            SkillDataKey.createSkillDataKey(
                    PacketBufferCodec.BOOLEAN,
                    false,
                    true,
                    GenesisWyrm.class
            )
    );

    public static final RegistryObject<SkillDataKey<Boolean>> MOUSE3 = DATA_KEYS.register("mouse3", () ->
            SkillDataKey.createSkillDataKey(
                    PacketBufferCodec.BOOLEAN,
                    false,
                    true,
                    GenesisWyrm.class
            )
    );

    public static final RegistryObject<SkillDataKey<Integer>> REFLECT_WINDOW = DATA_KEYS.register("reflect_window", () ->
            SkillDataKey.createSkillDataKey(PacketBufferCodec.INTEGER, 0, true, GenesisWyrm.class));

    public static final RegistryObject<SkillDataKey<Integer>> COUNTER_WINDOW = DATA_KEYS.register("counter_window", () ->
            SkillDataKey.createSkillDataKey(PacketBufferCodec.INTEGER, 0, true, GenesisWyrm.class));

	public static final RegistryObject<SkillDataKey<Integer>> PREV_ANIM = DATA_KEYS.register("prev_anim", () ->
			SkillDataKey.createSkillDataKey(
					PacketBufferCodec.INTEGER,
					-1,
					true,
					FlareBlitz.class
			)
	);

	public static final RegistryObject<SkillDataKey<Integer>> OMNERIA_COMBO = DATA_KEYS.register("omneria_combo", () ->
			SkillDataKey.createSkillDataKey(
					PacketBufferCodec.INTEGER,
					0,
					false,
					FlareBlitz.class
			)
	);

    public static final RegistryObject<SkillDataKey<Integer>> INITIAL_FORCE_COMBO = DATA_KEYS.register("initial_force_combo", () ->
            SkillDataKey.createSkillDataKey(
                    PacketBufferCodec.INTEGER,
                    0,
                    false,
                    InitialForce.class
            )
    );

	public static final RegistryObject<SkillDataKey<Integer>> OMNERIA_COMBO_GENESIS_WYRM = DATA_KEYS.register("omneria_combo_gwm", () ->
			SkillDataKey.createSkillDataKey(
					PacketBufferCodec.INTEGER,
					0,
					false,
					GenesisWyrm.class
			)
	);

	public static final RegistryObject<SkillDataKey<Integer>> OMNERIA_COMBO_GENESIS_WYRM_BLAST = DATA_KEYS.register("omneria_combo_gwm_blast", () ->
			SkillDataKey.createSkillDataKey(
					PacketBufferCodec.INTEGER,
					0,
					false,
					GenesisWyrm.class
			)
	);

	public static final RegistryObject<SkillDataKey<Integer>> OMNERIA_COMBO_GENESIS_WYRM_LEGS = DATA_KEYS.register("omneria_combo_gwm_legs", () ->
			SkillDataKey.createSkillDataKey(
					PacketBufferCodec.INTEGER,
					0,
					false,
					GenesisWyrm.class
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

	public static final RegistryObject<SkillDataKey<Boolean>> FLARESPIN = DATA_KEYS.register(
			"flarespin", () -> SkillDataKey.createSkillDataKey(
					PacketBufferCodec.BOOLEAN,
					true,
					true,
					FireArts.class
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

	public static final RegistryObject<SkillDataKey<Integer>> TRUE_COMBO_COUNT = DATA_KEYS.register(
			"true_combo_count", () -> SkillDataKey.createSkillDataKey(
					PacketBufferCodec.INTEGER,
					0,
					true,
					ImperatriceLumiere.class
			)
	);
}
