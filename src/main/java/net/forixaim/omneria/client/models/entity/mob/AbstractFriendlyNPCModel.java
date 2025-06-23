package net.forixaim.omneria.client.models.entity.mob;

import net.forixaim.omneria.world.entity.types.AbstractFriendlyNPC;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;

public abstract class AbstractFriendlyNPCModel<T extends AbstractFriendlyNPC> extends HumanoidModel<T>
{
	public AbstractFriendlyNPCModel(ModelPart pRoot)
	{
		super(pRoot);
	}
}
