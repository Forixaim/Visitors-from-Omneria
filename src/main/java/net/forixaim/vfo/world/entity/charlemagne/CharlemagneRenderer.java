package net.forixaim.vfo.world.entity.charlemagne;

import com.mojang.blaze3d.vertex.PoseStack;
import net.forixaim.vfo.VisitorsOfOmneria;
import net.forixaim.vfo.world.entity.charlemagne.model.CharlemagneModel;
import net.forixaim.vfo.world.entity.types.AbstractFriendlyNPCRenderer;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class CharlemagneRenderer extends AbstractFriendlyNPCRenderer<Charlemagne, CharlemagneModel>
{

	public CharlemagneRenderer(EntityRendererProvider.Context context)
	{
		this(context, ModelLayers.PLAYER_SLIM, ModelLayers.PLAYER_SLIM_INNER_ARMOR, ModelLayers.PLAYER_SLIM_OUTER_ARMOR, new ResourceLocation(VisitorsOfOmneria.MOD_ID, "textures/entity/charlemagne.png"));
	}

	public CharlemagneRenderer(EntityRendererProvider.Context pContext, ModelLayerLocation pZombieLayer, ModelLayerLocation pInnerArmor, ModelLayerLocation pOuterArmor, ResourceLocation location) {
		super(pContext, new CharlemagneModel(pContext.bakeLayer(pZombieLayer)), new CharlemagneModel(pContext.bakeLayer(pInnerArmor)), new CharlemagneModel(pContext.bakeLayer(pOuterArmor)), location);
	}

	@Override
	public @NotNull ResourceLocation getTextureLocation(@NotNull Charlemagne charlemagne)
	{
		return new ResourceLocation(VisitorsOfOmneria.MOD_ID, "textures/entity/charlemagne.png");
	}

	@Override
	public void render(@NotNull Charlemagne p_115455_, float p_115456_, float p_115457_, @NotNull PoseStack p_115458_, @NotNull MultiBufferSource p_115459_, int p_115460_)
	{
		super.render(p_115455_, p_115456_, p_115457_, p_115458_, p_115459_, p_115460_);
	}
}
