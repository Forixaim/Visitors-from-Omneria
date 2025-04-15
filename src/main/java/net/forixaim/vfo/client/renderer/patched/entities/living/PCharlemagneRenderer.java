package net.forixaim.vfo.client.renderer.patched.entities.living;

import net.forixaim.vfo.client.renderer.entity.CharlemagneRenderer;
import net.forixaim.vfo.registry.MeshRegistry;
import net.forixaim.vfo.world.entity.charlemagne.Charlemagne;
import net.forixaim.vfo.world.entity.patches.CharlemagnePatch;
import net.forixaim.vfo.client.meshes.CharlemagneMesh;
import net.forixaim.vfo.client.models.entity.mob.CharlemagneModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.EntityType;
import yesman.epicfight.api.client.model.Meshes;
import yesman.epicfight.client.renderer.patched.entity.PHumanoidRenderer;

public class PCharlemagneRenderer extends PHumanoidRenderer<Charlemagne, CharlemagnePatch, CharlemagneModel, CharlemagneRenderer, CharlemagneMesh>
{
	public PCharlemagneRenderer(EntityRendererProvider.Context context, EntityType<?> entityType)
	{
		super(MeshRegistry.CHARLEMAGNE, context, entityType);
	}

	@Override
	public Meshes.MeshAccessor<CharlemagneMesh> getDefaultMesh()
	{
		return MeshRegistry.CHARLEMAGNE;
	}
}
