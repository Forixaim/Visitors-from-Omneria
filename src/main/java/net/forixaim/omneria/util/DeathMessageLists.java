package net.forixaim.omneria.util;

import net.minecraft.network.chat.Component;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

public class DeathMessageLists
{
    public static List<Component> FPDC_DEATH_MESSAGES = Lists.newArrayList(
            Component.translatable("death.omneria.fpdc_1"),
            Component.translatable("death.omneria.fpdc_2"),
            Component.translatable("death.omneria.fpdc_3")
    );
}
