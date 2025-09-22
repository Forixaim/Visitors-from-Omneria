package net.forixaim.omneria.netcode.packets.client;

import net.forixaim.omneria.netcode.packets.SimplePacket;
import net.forixaim.omneria.world.entity.types.plugins.DialogueNPC;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;

public record NPCDialogue(int id, CompoundTag tag) implements SimplePacket
{
    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(this.id());
        buf.writeNbt(this.tag());
    }

    public static NPCDialogue decode(FriendlyByteBuf buf) {
        int id = buf.readInt();
        CompoundTag tag = buf.readNbt();
        return new NPCDialogue(id,tag);
    }

    @Override
    public void execute(Player playerEntity) {
        if (Minecraft.getInstance().player != null && Minecraft.getInstance().level != null) {
            if (Minecraft.getInstance().level.getEntity(this.id()) instanceof DialogueNPC npc) {
                npc.setConversingPlayer(playerEntity);
                npc.openDialogueScreen(this.tag());
            }
        }
    }
}
