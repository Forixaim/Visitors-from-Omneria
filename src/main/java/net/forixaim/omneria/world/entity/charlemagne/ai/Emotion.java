package net.forixaim.omneria.world.entity.charlemagne.ai;

public enum Emotion
{
	NEUTRAL,
	SERIOUS;

	public boolean is(Emotion mode)
	{
		return this == mode;
	}
}
