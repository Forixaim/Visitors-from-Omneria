package net.forixaim.omneria.mixin.optional;

import com.brandon3055.draconicevolution.entity.guardian.DraconicGuardianEntity;
import com.brandon3055.draconicevolution.entity.guardian.control.Phase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Phase.class)
public interface PhaseAccessor
{
    @Accessor(value = "guardian", remap = false)
    DraconicGuardianEntity getGuardian();
}
