package net.forixaim.omneria.registry;

import net.forixaim.omneria.VisitorsOfOmneria;
import net.forixaim.omneria.client.meshes.CharlemagneMesh;
import net.forixaim.omneria.client.meshes.FlareSlashMesh;
import yesman.epicfight.api.client.model.ClassicMesh;
import yesman.epicfight.api.client.model.Meshes;
import yesman.epicfight.client.mesh.HumanoidMesh;

public class MeshRegistry
{
	public static Meshes.MeshAccessor<HumanoidMesh> CHARLEMAGNE = Meshes.MeshAccessor.create(VisitorsOfOmneria.MOD_ID, "entity/charlemagne", jsonAssetLoader -> jsonAssetLoader.loadSkinnedMesh(CharlemagneMesh::new));
	public static Meshes.MeshAccessor<FlareSlashMesh> FLARE_SLASH = Meshes.MeshAccessor.create(VisitorsOfOmneria.MOD_ID, "entity/flare_slash", jsonAssetLoader -> jsonAssetLoader.loadSkinnedMesh(FlareSlashMesh::new));
    public static Meshes.MeshAccessor<ClassicMesh> SQUARE_LASER = Meshes.MeshAccessor.create(VisitorsOfOmneria.MOD_ID, "particle/square_laser", jsonAssetLoader -> jsonAssetLoader.loadClassicMesh(ClassicMesh::new));
    public static Meshes.MeshAccessor<ClassicMesh> SPHERE = Meshes.MeshAccessor.create(VisitorsOfOmneria.MOD_ID, "particle/sphere", jsonAssetLoader -> jsonAssetLoader.loadClassicMesh(ClassicMesh::new));

}
