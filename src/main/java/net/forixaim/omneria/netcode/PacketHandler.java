package net.forixaim.omneria.netcode;

import net.forixaim.omneria.VisitorsOfOmneria;
import net.forixaim.omneria.netcode.packets.SimplePacket;
import net.forixaim.omneria.netcode.packets.client.NPCDialogue;
import net.forixaim.omneria.netcode.packets.server.AddDialogue;
import net.forixaim.omneria.netcode.packets.server.NPCInteraction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.Function;

public class PacketHandler
{


    private static int index;

    public static synchronized void register()
    {
        register(NPCDialogue.class, NPCDialogue::decode);
        register(NPCInteraction.class, NPCInteraction::decode);
        register(AddDialogue.class, AddDialogue::decode);
    }

    private static <MSG extends SimplePacket> void register(final Class<MSG> packet, Function<FriendlyByteBuf, MSG> decoder) {
        NetworkHandler.PACKET_HANDLER.messageBuilder(packet, index++).encoder(SimplePacket::encode).decoder(decoder).consumerMainThread(SimplePacket::handle).add();
    }
}
