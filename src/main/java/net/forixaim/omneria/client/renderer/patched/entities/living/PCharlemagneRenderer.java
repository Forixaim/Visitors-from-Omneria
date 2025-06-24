package net.forixaim.omneria.client.renderer.patched.entities.living;

import net.forixaim.omneria.client.renderer.entity.CharlemagneRenderer;
import net.forixaim.omneria.registry.MeshRegistry;
import net.forixaim.omneria.world.entity.charlemagne.Charlemagne;
import net.forixaim.omneria.world.entity.patches.CharlemagnePatch;
import net.forixaim.omneria.client.meshes.CharlemagneMesh;
import net.forixaim.omneria.client.models.entity.mob.CharlemagneModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.EntityType;
import yesman.epicfight.api.client.model.Meshes;
import yesman.epicfight.client.mesh.HumanoidMesh;
import yesman.epicfight.client.renderer.patched.entity.PHumanoidRenderer;

public class PCharlemagneRenderer extends PHumanoidRenderer<Charlemagne, CharlemagnePatch, CharlemagneModel, CharlemagneRenderer, HumanoidMesh>
{
	public PCharlemagneRenderer(EntityRendererProvider.Context context, EntityType<?> entityType)
	{
		super(MeshRegistry.CHARLEMAGNE, context, entityType);
	}

	@Override
	public Meshes.MeshAccessor<HumanoidMesh> getDefaultMesh()
	{
		return MeshRegistry.CHARLEMAGNE;
	}
}
