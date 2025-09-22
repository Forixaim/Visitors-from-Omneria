package net.forixaim.omneria.world.entity.types.plugins;

import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;

import java.util.Map;

public interface VoicedDialogueNPC extends DialogueNPC
{
    Map<Component, SoundEvent> getVoicelines();
}
