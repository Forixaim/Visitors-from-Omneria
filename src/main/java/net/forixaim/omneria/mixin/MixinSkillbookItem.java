package net.forixaim.omneria.mixin;

import net.forixaim.omneria.item.OmneriaRarities;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import yesman.epicfight.world.item.SkillBookItem;

@Mixin(SkillBookItem.class)
public class MixinSkillbookItem extends Item {

    public MixinSkillbookItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public @NotNull Rarity getRarity(@NotNull ItemStack pStack) {
        if (pStack.getOrCreateTag().contains("skill") && pStack.getOrCreateTag().getString("skill").equals("omneria:imperatrice_lumiere")) {
            return OmneriaRarities.IMPERATRICE_LUMIERE;
        }
        if (pStack.getOrCreateTag().contains("skill") && pStack.getOrCreateTag().getString("skill").equals("omneria:genesis_wyrm")) {
            return OmneriaRarities.GENESIS_WYRM;
        }

        return super.getRarity(pStack);
    }
}
