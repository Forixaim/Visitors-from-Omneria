package net.forixaim.omneria.client.armatures;

import yesman.epicfight.api.animation.Joint;
import yesman.epicfight.model.armature.HumanoidArmature;

import java.util.Map;

public class CharlemagneArmature extends HumanoidArmature
{
	public CharlemagneArmature(String name, int jointNumber, Joint rootJoint, Map<String, Joint> jointMap)
	{
		super(name, jointNumber, rootJoint, jointMap);
	}
}
