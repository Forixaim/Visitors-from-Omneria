package net.forixaim.vfo.skill.battle_style.imperatrice_lumiere;

import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.Input;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.Mth;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import yesman.epicfight.client.events.engine.ControllEngine;
import yesman.epicfight.client.world.capabilites.entitypatch.player.LocalPlayerPatch;
import yesman.epicfight.network.client.CPExecuteSkill;
import yesman.epicfight.skill.Skill;

public class ArgumentGatherers
{
	public static FriendlyByteBuf UniversalDirectionalInput(LocalPlayerPatch playerPatch, ControllEngine engine)
	{
		Input input = playerPatch.getOriginal().input;
		float pulse = Mth.clamp(0.3F + EnchantmentHelper.getSneakingSpeedBonus(playerPatch.getOriginal()), 0.0F, 1.0F);
		input.tick(false, pulse);

		int forward = ControllEngine.isKeyDown(Minecraft.getInstance().options.keyUp) ? 1 : 0;
		int backward = ControllEngine.isKeyDown(Minecraft.getInstance().options.keyDown) ? -1 : 0;
		int left = ControllEngine.isKeyDown(Minecraft.getInstance().options.keyLeft) ? 1 : 0;
		int right = ControllEngine.isKeyDown(Minecraft.getInstance().options.keyRight) ? -1 : 0;
		int down = ControllEngine.isKeyDown(Minecraft.getInstance().options.keyShift) ? -1 : 0;
		int up = ControllEngine.isKeyDown(Minecraft.getInstance().options.keyJump) ? 1 : 0;

		FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
		buf.writeInt(forward);
		buf.writeInt(backward);
		buf.writeInt(left);
		buf.writeInt(right);
		buf.writeInt(down);
		buf.writeInt(up);

		return buf;
	}

	public static Object DirectionalExecutionPacket(LocalPlayerPatch playerPatch, FriendlyByteBuf args, Skill skill)
	{
		int forward = args.readInt();
		int backward = args.readInt();
		int left = args.readInt();
		int right = args.readInt();
		int down = args.readInt();
		int up = args.readInt();
		int vertic = forward + backward;
		int horizon = left + right;
		int upDown = up + down;


		CPExecuteSkill packet = new CPExecuteSkill(playerPatch.getSkill(skill).getSlotId());
		packet.getBuffer().writeInt(Integer.compare(vertic, 0));
		packet.getBuffer().writeInt(Integer.compare(horizon, 0));
		packet.getBuffer().writeInt(Integer.compare(down, 0));

		return packet;
	}
}
