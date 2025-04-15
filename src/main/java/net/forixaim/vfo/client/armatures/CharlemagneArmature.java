package net.forixaim.vfo.client.armatures;

import yesman.epicfight.api.animation.Joint;
import yesman.epicfight.model.armature.HumanoidArmature;

import java.util.Map;

public class CharlemagneArmature extends HumanoidArmature
{
	public final Joint eyebrowL;
	public final Joint eyebrowR;
	public final Joint eyelidL;
	public final Joint eyelidR;
	public final Joint bottomLidL;
	public final Joint bottomLidR;
	public final Joint eyeL;
	public final Joint eyeR;


	public CharlemagneArmature(String name, int jointNumber, Joint rootJoint, Map<String, Joint> jointMap)
	{
		super(name, jointNumber, rootJoint, jointMap);
		this.eyebrowL = this.getOrLogException(jointMap, "Eyebrow_L");
		this.eyebrowR = this.getOrLogException(jointMap, "Eyebrow_R");
		this.eyelidL = this.getOrLogException(jointMap, "Eyelid_L");
		this.eyelidR = this.getOrLogException(jointMap, "Eyelid_R");
		this.eyeL = this.getOrLogException(jointMap, "Eye_L");
		this.eyeR = this.getOrLogException(jointMap, "Eye_R");
		this.bottomLidL = this.getOrLogException(jointMap, "BottomLid_L");
		this.bottomLidR = this.getOrLogException(jointMap, "BottomLid_R");
	}
}
