package net.forixaim.omneria.client.ui.screens;

import net.forixaim.omneria.VisitorsOfOmneria;
import net.forixaim.omneria.client.ui.screens.components.dialogue.AnswerComponent;
import net.forixaim.omneria.client.ui.screens.components.dialogue.OptionComponent;
import net.forixaim.omneria.netcode.NetworkHandler;
import net.forixaim.omneria.netcode.PacketHandler;
import net.forixaim.omneria.netcode.packets.server.AddDialogue;
import net.forixaim.omneria.netcode.packets.server.NPCInteraction;
import net.forixaim.omneria.world.entity.types.plugins.SoundBasedDialogueNPC;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DialogueScreen extends Screen
{
    public static final ResourceLocation MY_BACKGROUND_LOCATION = ResourceLocation.fromNamespaceAndPath(VisitorsOfOmneria.MOD_ID, "textures/gui/background.png");
    protected final AnswerComponent dialogueAnswer;
    protected final Entity entity;
    public final int typewriterInterval;
    private int typewriterTimer = 0;
    EntityType<?> entityType;

    public DialogueScreen(Entity entity, EntityType<?> entityType)
    {
        super(entity.getDisplayName());
        typewriterInterval = 4;
        this.dialogueAnswer = new AnswerComponent(this.buildDialogueAnswerName(entity.getDisplayName().copy().withStyle(ChatFormatting.YELLOW)).append(": "));
        this.entity = entity;
        this.entityType = entityType;
    }

    /**
     * 在这里实现对话逻辑调用
     */
    @Override
    protected void init()
    {
        positionDialogue();//不填的话用builder创造出来的对话框第一个对话会错误显示
    }

    public void setupDialogueChoices(List<OptionComponent> options)
    {
        this.clearWidgets();
        for (OptionComponent option : options)
        {
            this.addRenderableWidget(option);
        }
        this.positionDialogue();
    }

    /**
     * Repositions the Valkyrie Queen's dialogue answer and the player's dialogue choices based on the amount of choices.
     */
    protected void positionDialogue()
    {
        // Dialogue answer.
        this.dialogueAnswer.reposition(this.width, this.height * 5 / 4);//相较于天堂的下移了一点
        // Dialogue choices.
        int lineNumber = this.dialogueAnswer.height / 12 + 1;
        for (Renderable renderable : this.renderables)
        {
            if (renderable instanceof OptionComponent option)
            {
                option.setX(this.width / 2 - option.getWidth() / 2);
                option.setY(this.height / 2 * 5 / 4 + 12 * lineNumber);//调低一点
                lineNumber++;
            }
        }
    }

    /**
     * 顺便发包同步记录，以及全服广播对话
     * Sets what message to display for a dialogue answer.
     *
     * @param component The message {@link Component}.
     */
    protected void setDialogueAnswer(Component component)
    {
        NetworkHandler.sendToServer(NetworkHandler.PACKET_HANDLER, new AddDialogue(entity.getDisplayName(), component, true));
        this.dialogueAnswer.updateTypewriterDialogue(component);
    }

    public MutableComponent buildDialogueAnswerName(Component component)
    {
        return Component.literal("[").append(component.copy().withStyle(ChatFormatting.YELLOW)).append("]");
    }
    protected void finishChat(long interactionID)
    {
        NetworkHandler.sendToServer(NetworkHandler.PACKET_HANDLER, new NPCInteraction(this.entity.getId(), interactionID));
        super.onClose();
    }

    protected void interactionAction(long interactionID)
    {
        NetworkHandler.sendToServer(NetworkHandler.PACKET_HANDLER, new NPCInteraction(this.entity.getId(), interactionID));
    }

    /**
     * 发包但不关闭窗口
     */
    protected void execute(long interactionID)
    {
         NetworkHandler.sendToServer(NetworkHandler.PACKET_HANDLER, new NPCInteraction(this.entity.getId(), interactionID));
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks)
    {
        this.renderBackground(guiGraphics);
        //guiGraphics.blit(MY_BACKGROUND_LOCATION, this.width/2 - 214/2, this.height/2 - 252/2, 0, 0, 214, 252);

        if (typewriterTimer < 0)
        {
            if (entity instanceof SoundBasedDialogueNPC dialogueNPC)
            {
                this.dialogueAnswer.updateTypewriterDialogue(dialogueNPC.getSound());
            }
            else
            {
                this.dialogueAnswer.updateTypewriterDialogue();
            }
            positionDialogue();
            typewriterTimer = typewriterInterval;
        } else
        {
            typewriterTimer--;
        }

        this.dialogueAnswer.render(guiGraphics);

        //如果回答还没显示完则不渲染选项
        for (Renderable renderable : this.renderables)
        {
            if (renderable instanceof OptionComponent && !dialogueAnswer.shouldRenderOption())
            {
                continue;
            }
            renderable.render(guiGraphics, mouseX, mouseY, partialTicks);
        }
    }

    /**
     * [CODE COPY] - {@link Screen#renderBackground(GuiGraphics)}.<br><br>
     * Remove code for dark gradient and dirt background.
     */
    @Override
    public void renderBackground(@NotNull GuiGraphics guiGraphics)
    {
        if (this.getMinecraft().level != null)
        {
//            guiGraphics.blit(MY_BACKGROUND_LOCATION, 0, 0, 0, 0.0F, 0.0F, this.width, this.height, 214, 252);
            MinecraftForge.EVENT_BUS.post(new ScreenEvent.BackgroundRendered(this, guiGraphics));
        }
    }

    @Override
    public void resize(@NotNull Minecraft minecraft, int width, int height)
    {
        this.width = width;
        this.height = height;
        this.positionDialogue();
    }

    @Override
    public boolean isPauseScreen()
    {
        return false;
    }

    @Override
    public void onClose()
    {
        this.finishChat(Long.MIN_VALUE);
    }
}
