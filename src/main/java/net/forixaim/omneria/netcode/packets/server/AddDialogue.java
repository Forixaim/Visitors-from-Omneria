package net.forixaim.omneria.netcode.packets.server;

import net.forixaim.omneria.netcode.packets.SimplePacket;
import net.minecraft.ChatFormatting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;

public record AddDialogue(Component name, Component content, boolean broadcast) implements SimplePacket
{
    @Override
    public void encode(FriendlyByteBuf buf)
    {
        buf.writeComponent(name);
        buf.writeComponent(content);
        buf.writeBoolean(broadcast);
    }

    public static AddDialogue decode(FriendlyByteBuf buf)
    {
        return new AddDialogue(buf.readComponent(), buf.readComponent(), buf.readBoolean());
    }

    @Override
    public void execute(@Nullable Player playerEntity)
    {
//        DOTEArchiveManager.addDialog(name, content);
        if (playerEntity != null && broadcast)
        {
            for (Player player : playerEntity.level().players())
            {
                if (player != playerEntity && player.getPosition(1.0f).distanceTo(playerEntity.getPosition(1.0f)) < 10)
                {
                    player.displayClientMessage(Component.literal("[").append(name.copy().withStyle(ChatFormatting.YELLOW)).append(Component.literal("]:")), false);
                    player.displayClientMessage(content, false);
                }
            }
        }
    }
}
