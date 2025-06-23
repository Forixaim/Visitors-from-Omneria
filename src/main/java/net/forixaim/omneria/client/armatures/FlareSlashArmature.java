package net.forixaim.omneria.client.armatures;

import yesman.epicfight.api.animation.Joint;
import yesman.epicfight.api.model.Armature;

import java.util.Map;

public class FlareSlashArmature extends Armature
{
    public FlareSlashArmature(String name, int jointNumber, Joint rootJoint, Map<String, Joint> jointMap)
    {
        super(name, jointNumber, rootJoint, jointMap);
    }
}
