package net.forixaim.omneria.world.entity.types;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.npc.Npc;
import net.minecraft.world.level.Level;

public class AbstractFriendlyNPC extends PathfinderMob implements Npc
{

	protected AbstractFriendlyNPC(EntityType<? extends PathfinderMob> p_21683_, Level p_21684_)
	{
		super(p_21683_, p_21684_);
	}
}
