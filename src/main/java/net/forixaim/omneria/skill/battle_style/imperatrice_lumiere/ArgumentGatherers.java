package net.forixaim.omneria.skill.battle_style.imperatrice_lumiere;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import yesman.epicfight.api.client.input.InputManager;
import yesman.epicfight.api.client.input.action.InputAction;
import yesman.epicfight.network.client.CPSkillRequest;
import yesman.epicfight.skill.SkillContainer;
import com.mojang.datafixers.util.Pair;

import java.util.Map;
import java.util.Objects;

public class ArgumentGatherers
{
	public static void UniversalDirectionalInput(CompoundTag tag)
	{
        Options opt = Minecraft.getInstance().options;

        // Define the keys, their mappings, and their "active" values
        Map<String, Pair<KeyMapping, Integer>> inputMap = Map.of(
                "forward",  Pair.of(opt.keyUp, 1),
                "backward", Pair.of(opt.keyDown, -1),
                "left",     Pair.of(opt.keyLeft, 1),
                "right",    Pair.of(opt.keyRight, -1),
                "down",     Pair.of(opt.keyShift, -1),
                "up",       Pair.of(opt.keyJump, 1)
        );

        inputMap.forEach((name, data) -> {
            boolean active = InputManager.isActionActive(Objects.requireNonNull(InputAction.fromKeyMapping(data.getFirst())));
            tag.putInt(name, active ? data.getSecond() : 0);
        });
    }

	public static CustomPacketPayload DirectionalExecutionPacket(SkillContainer container, CompoundTag args)
	{
        CompoundTag result = new CompoundTag();
        result.putInt("front_back", args.getInt("forward") + args.getInt("backward"));
        result.putInt("left_right", args.getInt("left") + args.getInt("horizon"));
        result.putInt("up_down", args.getInt("up") + args.getInt("down"));
        return new CPSkillRequest(container.getSlot(), result);
	}
}
