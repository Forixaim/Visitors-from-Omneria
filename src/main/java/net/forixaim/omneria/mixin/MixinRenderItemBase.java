package net.forixaim.omneria.mixin;

import com.mojang.logging.LogUtils;
import net.forixaim.omneria.world.entity.patches.CharlemagnePatch;
import net.minecraft.world.InteractionHand;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import yesman.epicfight.api.animation.Joint;
import yesman.epicfight.api.utils.math.OpenMatrix4f;
import yesman.epicfight.client.renderer.patched.item.RenderItemBase;
import yesman.epicfight.model.armature.types.ToolHolderArmature;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;

import java.util.Map;

//Primarily for debugging purposes, methods are not going to be changed.
@Mixin(RenderItemBase.class)
public class MixinRenderItemBase {

    @Mutable
    @Shadow(remap = false) @Final private boolean alwaysInHand;

    @Shadow(remap = false) @Final protected Map<String, OpenMatrix4f> mainhandCorrectionTransforms;

    @Shadow(remap = false) @Final public OpenMatrix4f transformHolder;

    @Shadow(remap = false) @Final protected Map<String, OpenMatrix4f> offhandCorrectionTransforms;

    @Shadow(remap = false) @Final protected static Map<String, OpenMatrix4f> GLOBAL_MAINHAND_ITEM_TRANSFORMS;

    @Shadow(remap = false) @Final protected static Map<String, OpenMatrix4f> GLOBAL_OFFHAND_ITEM_TRANSFORMS;

   @Inject(method = "getCorrectionMatrix", at = @At("HEAD"), remap = false)
   public void injectMatrix(LivingEntityPatch<?> entitypatch, InteractionHand hand, OpenMatrix4f[] poses, CallbackInfoReturnable<OpenMatrix4f> cir)
   {
       if (entitypatch instanceof CharlemagnePatch)
       {
           this.alwaysInHand = true;
       }
   }

}
