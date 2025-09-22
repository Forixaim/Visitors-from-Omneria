package net.forixaim.omneria.mixin;

import net.forixaim.omneria.registry.MeshRegistry;
import net.forixaim.omneria.special.SpecialPlayers;
import net.minecraft.client.player.AbstractClientPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import yesman.epicfight.api.asset.AssetAccessor;
import yesman.epicfight.client.mesh.HumanoidMesh;
import yesman.epicfight.client.renderer.patched.entity.PPlayerRenderer;
import yesman.epicfight.client.world.capabilites.entitypatch.player.AbstractClientPlayerPatch;

@Mixin(PPlayerRenderer.class)
public class MixinPPlayerRenderer
{


    @Inject(method = "getMeshProvider(Lyesman/epicfight/client/world/capabilites/entitypatch/player/AbstractClientPlayerPatch;)Lyesman/epicfight/api/asset/AssetAccessor;", at = @At("RETURN"), remap = false, cancellable = true)
    public void getMeshProvider(AbstractClientPlayerPatch<AbstractClientPlayer> entitypatch, CallbackInfoReturnable<AssetAccessor<HumanoidMesh>> cir)
    {
    }
}
