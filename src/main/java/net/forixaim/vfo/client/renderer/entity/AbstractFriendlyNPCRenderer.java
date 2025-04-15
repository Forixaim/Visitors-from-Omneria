package net.forixaim.vfo.client.renderer.entity;

import net.forixaim.vfo.client.models.entity.mob.AbstractFriendlyNPCModel;
import net.forixaim.vfo.world.entity.types.AbstractFriendlyNPC;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class AbstractFriendlyNPCRenderer<T extends AbstractFriendlyNPC, M extends AbstractFriendlyNPCModel<T>> extends HumanoidMobRenderer<T, M>
{
	private final ResourceLocation texture;

	public AbstractFriendlyNPCRenderer(EntityRendererProvider.Context pContext, M pModel, M pInnerModel, M pOuterModel, ResourceLocation pTexture) {
		super(pContext, pModel, 0.5F);
		this.addLayer(new HumanoidArmorLayer<>(this, pInnerModel, pOuterModel, pContext.getModelManager()));
		this.texture = pTexture;
	}

	@Override
	public @NotNull ResourceLocation getTextureLocation(T t)
	{
		return this.texture;
	}
}
