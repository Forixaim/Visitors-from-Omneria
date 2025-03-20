package net.forixaim.vfo.registry;

import net.forixaim.vfo.VisitorsOfOmneria;
import net.forixaim.vfo.world.entity.charlemagne.model.CharlemagneMesh;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import yesman.epicfight.api.client.model.Meshes;

@Mod.EventBusSubscriber(modid = VisitorsOfOmneria.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class MeshRegistry
{
	public static Meshes.MeshAccessor<CharlemagneMesh> CHARLEMAGNE = Meshes.MeshAccessor.create(VisitorsOfOmneria.MOD_ID, "entity/charlemagne", jsonAssetLoader -> jsonAssetLoader.loadSkinnedMesh(CharlemagneMesh::new));

}
