package net.forixaim.omneria.capabilities.styles;

import yesman.epicfight.world.capabilities.item.Style;

public enum LumiereStyles implements Style
{
	IMPERATRICE_SWORD(false),
	FORIXAIM_SWORD(false),
	BLAZE_KICK_ART(false),
	FLARE_FALCON(false),
	EMPRESS_COMBINATION(false);

	final boolean OffHandUse;
	final int id;
	LumiereStyles(boolean OffHandUse)
	{
		this.id = ENUM_MANAGER.assign(this);
		this.OffHandUse = OffHandUse;
	}

	@Override
	public int universalOrdinal()
	{
		return this.id;
	}

	@Override
	public boolean canUseOffhand()
	{
		return this.OffHandUse;
	}
}
