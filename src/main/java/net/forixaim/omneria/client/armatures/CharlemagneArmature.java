package net.forixaim.omneria.client.armatures;

import yesman.epicfight.api.animation.Joint;
import yesman.epicfight.model.armature.HumanoidArmature;

import java.util.Map;

public class CharlemagneArmature extends HumanoidArmature
{
	public final Joint eyeL;
	public final Joint eyeR;
	public final Joint eyebrowL;
	public final Joint eyebrowR;
	public final Joint eyelidL;
	public final Joint eyelidR;
	public CharlemagneArmature(String name, int jointNumber, Joint rootJoint, Map<String, Joint> jointMap)
	{
		super(name, jointNumber, rootJoint, jointMap);
		this.eyeL= this.getOrLogException(jointMap, "Eye_L");
		this.eyeR = this.getOrLogException(jointMap, "Eye_R");
		this.eyelidR = this.getOrLogException(jointMap, "Eyelid_R");
		this.eyelidL = this.getOrLogException(jointMap, "Eyelid_L");
		this.eyebrowL = this.getOrLogException(jointMap, "Eyebrow_L");
		this.eyebrowR = this.getOrLogException(jointMap, "Eyebrow_R");
	}

	public Joint leftEye()
	{
		return this.eyeL;
	}

	public Joint rightEye()
	{
		return this.eyeR;
	}
}
