package net.forixaim.omneria.util;

import com.google.gson.JsonElement;
import org.apache.commons.compress.utils.Lists;

import java.util.List;

/**
 * This data structure is for storing attack strings when traversing through it.
 * This tree will be using the LC-RS approach
 * @param <T> Any object to traverse through
 */
public class Tree<T>
{
	private Node<T> root;

	public Tree(TreeBuilder<T> builder) {
		this.root = builder.root;
	}

	//Creates a new tree builder that will be used to build the tree
	public static TreeBuilder<?> newTreeBuilder(Node<?> root)
	{
		return new TreeBuilder<>(root);
	}

	public Node<T> getRoot()
	{
		return root;
	}



	public static class Node<T>
	{
		private final T data;
		private Node<T> child;
		private Node<T> sibling;
		private boolean isRoot;

		public Node(T data)
		{
			this.data = data;
		}

		public T getData()
		{
			return data;
		}

		public boolean isRoot()
		{
			return isRoot;
		}

		public Node<T> getChild()
		{
			return child;
		}

		public Node<T> getSibling()
		{
			return sibling;
		}

		public static <T> Node<T> createNode(T data)
		{
			return new Node<>(data);
		}
	}

	public static class TreeBuilder<T>
	{
		private Node<T> root;
		private final List<Node<T>> nodes = Lists.newArrayList();
		JsonElement jsonElement;

		public TreeBuilder<T> setRoot(Node<T> root)
		{
			this.root = root;
			this.root.isRoot = true;
			return this;
		}

		public TreeBuilder(Node<T> root)
		{
			this.root = root;
		}

		public TreeBuilder<T> addNode(Node<T> node, Node<T> relation, NodeAddCondition condition)
		{
			if (condition == NodeAddCondition.AS_CHILD)
			{
				if (relation.child == null)
				{
					relation.child = node;
				}
				else
				{
					//If the child already exists, traverse to the child and add the node as a sibling
					addNode(node, relation.child, NodeAddCondition.AS_SIBLING);
				}
			}
			else if (condition == NodeAddCondition.AS_SIBLING)
			{
				if (relation.sibling == null)
				{
					relation.sibling = node;
				}
				else
				{
					//Recursively traverse through the siblings until the end is reached
					addNode(node, relation.sibling, NodeAddCondition.AS_SIBLING);
				}
			}
			return this;
		}

		public Tree<T> build()
		{
			return new Tree<>(this);
		}
	}
}
