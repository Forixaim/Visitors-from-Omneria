package net.forixaim.omneria.capabilities.weapons;

import yesman.epicfight.world.capabilities.item.WeaponCategory;

public enum OmneriaCategories implements WeaponCategory
{
	ORIGIN_EXCALIBUR,
	DERIVED_EXCALIBUR,
	ORIGIN_JOYEUSE,
	DERIVED_JOYEUSE;
	final int id;
	OmneriaCategories() {
		this.id = WeaponCategory.ENUM_MANAGER.assign(this);
	}

	@Override
	public int universalOrdinal()
	{
		return this.id;
	}
}
