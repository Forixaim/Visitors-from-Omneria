package net.forixaim.omneria.world.entity.types.plugins;

import net.minecraft.sounds.SoundEvent;
import org.jetbrains.annotations.NotNull;

public interface SoundBasedDialogueNPC extends DialogueNPC
{
    @NotNull SoundEvent getSound();

}
