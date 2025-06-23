package net.forixaim.omneria.world.entity.charlemagne.ai;

import net.forixaim.omneria.util.Tree;
import net.forixaim.omneria.world.entity.patches.CharlemagnePatch;
import net.minecraft.util.RandomSource;
import org.apache.commons.compress.utils.Lists;

import java.util.List;

public class CharlemagneAttackString
{
	public final List<List<CharlemagneAttack>> Attacks = Lists.newArrayList();
	public final Tree<CharlemagneAttack> attacks;
	public boolean connected;
	public boolean firing;
	public int position;

	public CharlemagneAttackString(CharlemagneAttack set)
	{
		attacks = newTreeBuilder(Tree.Node.createNode(set)).build();
		this.connected = false;
		this.firing = false;
		this.position = 0;
	}

	//Creates a new tree builder that will be used to build the tree
	private Tree.TreeBuilder<CharlemagneAttack> newTreeBuilder(Tree.Node<CharlemagneAttack> root)
	{
		return new Tree.TreeBuilder<>(root);
	}

	public void fire(CharlemagnePatch attacker)
	{
		RandomSource source = attacker.getOriginal().getRandom();
		//All traversal logic is done here
		Tree.Node<CharlemagneAttack> node = attacks.getRoot();
		while (node != null)
		{
			if (node.isRoot())
			{
				//Immediately fire the attack and traverse to the next node
				node.getData().Fire(attacker);
				node = node.getChild();
			}
			else
			{
				//Randomly decide whether to fire the attack and traverse to the child node or traverse to the sibling node
				if (source.nextBoolean())
				{
					node.getData().Fire(attacker);
					node = node.getChild();
				}
				else
				{
					//However this will throw an exception if the sibling node is null, check if the sibling node is null
					if (node.getSibling() == null)
					{
						//If the sibling node is null fire the attack and traverse to the child node
						node.getData().Fire(attacker);
						node = node.getChild();
					}
					else
					{
						//If the sibling node is not null traverse to the sibling node
						node = node.getSibling();
					}
				}
			}
		}
	}

	public void reset()
	{
		position = 0;
		firing = false;
	}
}
