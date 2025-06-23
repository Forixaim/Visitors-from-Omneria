package net.forixaim.omneria.client.meshes;

import org.jetbrains.annotations.Nullable;
import yesman.epicfight.api.client.model.Mesh;
import yesman.epicfight.api.client.model.MeshPartDefinition;
import yesman.epicfight.api.client.model.SkinnedMesh;
import yesman.epicfight.api.client.model.VertexBuilder;
import yesman.epicfight.client.mesh.HumanoidMesh;

import java.util.List;
import java.util.Map;

public class FlareSlashMesh extends SkinnedMesh
{
    public FlareSlashMesh(@Nullable Map<String, Number[]> arrayMap, @Nullable Map<MeshPartDefinition, List<VertexBuilder>> partBuilders, @Nullable SkinnedMesh parent, RenderProperties properties)
    {
        super(arrayMap, partBuilders, parent, properties);
    }
}
