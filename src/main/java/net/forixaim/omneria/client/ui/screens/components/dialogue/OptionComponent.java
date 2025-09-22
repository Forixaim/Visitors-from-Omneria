package net.forixaim.omneria.client.ui.screens.components.dialogue;

import net.forixaim.omneria.netcode.NetworkHandler;
import net.forixaim.omneria.netcode.packets.server.AddDialogue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

/**
 * A button widget that allows the player to select a line of dialogue to say to an NPC.
 */
public class OptionComponent extends Button {

    private boolean broadcast;

    public OptionComponent(MutableComponent message, Button.OnPress onPress) {
        super(Button.builder(appendBrackets(message), onPress).pos(0, 0).size(0, 12).createNarration(DEFAULT_NARRATION));
        this.width = Minecraft.getInstance().font.width(this.getMessage()) + 2;
        this.broadcast = false;
    }

    public OptionComponent(MutableComponent message, Button.OnPress onPress, boolean broadcast) {
        this(message, onPress);
        this.broadcast = broadcast;
    }

    /**
     * 添加到对话记录并全服广播
     */
    @Override
    public void onPress() {
        super.onPress();
        if(Minecraft.getInstance().player != null){
            NetworkHandler.sendToServer(NetworkHandler.PACKET_HANDLER, new AddDialogue(Minecraft.getInstance().player.getDisplayName(), getMessage(), broadcast));
        }
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        guiGraphics.fillGradient(this.getX(), this.getY(), this.getX() + this.width, this.getY() + this.height, 0x66000000, 0x66000000);
        guiGraphics.drawString(Minecraft.getInstance().font, this.getMessage(), this.getX() + 1, this.getY() + 1, this.isHovered() ? 0xFFFF55: 0xFFFFFF);
    }

    public static MutableComponent appendBrackets(MutableComponent component) {
        return Component.literal("[").append(component).append("]");
    }
}