package net.forixaim.omneria.world.entity.charlemagne.ai;

public enum CharlemagneMode
{
	FRIENDLY,
	DUELING,
	DEFENSE;

	public boolean is(CharlemagneMode mode)
	{
		return this == mode;
	}
}
