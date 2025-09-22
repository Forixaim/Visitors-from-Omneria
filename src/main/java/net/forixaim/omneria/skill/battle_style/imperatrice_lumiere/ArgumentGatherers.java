package net.forixaim.omneria.skill.battle_style.imperatrice_lumiere;

import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.Input;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.Mth;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import yesman.epicfight.api.animation.types.AirSlashAnimation;
import yesman.epicfight.client.events.engine.ControlEngine;
import yesman.epicfight.network.client.CPSkillRequest;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillContainer;

public class ArgumentGatherers
{
	public static FriendlyByteBuf UniversalDirectionalInput(SkillContainer container, ControlEngine engine)
	{
		Input input = container.getClientExecutor().getOriginal().input;
		float pulse = Mth.clamp(0.3F + EnchantmentHelper.getSneakingSpeedBonus(container.getExecutor().getOriginal()), 0.0F, 1.0F);
		input.tick(false, pulse);

		int forward = ControlEngine.isKeyDown(Minecraft.getInstance().options.keyUp) ? 1 : 0;
		int backward = ControlEngine.isKeyDown(Minecraft.getInstance().options.keyDown) ? -1 : 0;
		int left = ControlEngine.isKeyDown(Minecraft.getInstance().options.keyLeft) ? 1 : 0;
		int right = ControlEngine.isKeyDown(Minecraft.getInstance().options.keyRight) ? -1 : 0;
		int down = ControlEngine.isKeyDown(Minecraft.getInstance().options.keyShift) ? -1 : 0;
		int up = ControlEngine.isKeyDown(Minecraft.getInstance().options.keyJump) ? 1 : 0;

		FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
		buf.writeInt(forward);
		buf.writeInt(backward);
		buf.writeInt(left);
		buf.writeInt(right);
		buf.writeInt(down);
		buf.writeInt(up);

		return buf;
	}

	public static Object DirectionalExecutionPacket(SkillContainer container, FriendlyByteBuf args, Skill skill)
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

		CPSkillRequest packet = new CPSkillRequest(container.getSlot());
		packet.getBuffer().writeInt(Integer.compare(vertic, 0));
		packet.getBuffer().writeInt(Integer.compare(horizon, 0));
		packet.getBuffer().writeInt(Integer.compare(upDown, 0));

		return packet;
	}
}
