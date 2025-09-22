package net.forixaim.omneria.netcode.packets.server;

import net.forixaim.omneria.netcode.packets.SimplePacket;
import net.forixaim.omneria.world.entity.types.plugins.DialogueNPC;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;

public record NPCInteraction(int entityID, long interactionID) implements SimplePacket
{
    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(this.entityID());
        buf.writeLong(this.interactionID());
    }

    public static NPCInteraction decode(FriendlyByteBuf buf) {
        return new NPCInteraction(buf.readInt(), buf.readByte());
    }

    @Override
    public void execute(@Nullable Player playerEntity) {
        if (playerEntity != null && playerEntity.getServer() != null && playerEntity.level().getEntity(this.entityID()) instanceof DialogueNPC npc) {
            npc.handleNpcInteraction(playerEntity, this.interactionID());
        }
    }
}
