package net.forixaim.vfo.client.meshes;

import org.jetbrains.annotations.Nullable;
import yesman.epicfight.api.client.model.Mesh;
import yesman.epicfight.api.client.model.MeshPartDefinition;
import yesman.epicfight.api.client.model.SkinnedMesh;
import yesman.epicfight.api.client.model.SkinnedMeshVertexBuilder;
import yesman.epicfight.client.mesh.HumanoidMesh;

import java.util.List;
import java.util.Map;

public class FlareSlashMesh extends SkinnedMesh
{
    public final SkinnedMeshPart projectile;
    public FlareSlashMesh(Map<String, Number[]> arrayMap, Map<MeshPartDefinition, List<SkinnedMeshVertexBuilder>> parts, SkinnedMesh parent, Mesh.RenderProperties properties)
    {
        super(arrayMap, parts, parent, properties);
        projectile = this.getOrLogException(this.parts, "projectile");
    }
}
