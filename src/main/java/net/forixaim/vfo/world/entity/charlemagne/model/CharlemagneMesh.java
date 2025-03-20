package net.forixaim.vfo.world.entity.charlemagne.model;

import yesman.epicfight.api.client.model.*;
import yesman.epicfight.client.mesh.HumanoidMesh;

import java.util.List;
import java.util.Map;

public class CharlemagneMesh extends HumanoidMesh
{
	public CharlemagneMesh(Map<String, Number[]> arrayMap, Map<MeshPartDefinition, List<SkinnedMeshVertexBuilder>> parts, SkinnedMesh parent, Mesh.RenderProperties properties)
	{
		super(arrayMap, parts, parent, properties);
	}
}
