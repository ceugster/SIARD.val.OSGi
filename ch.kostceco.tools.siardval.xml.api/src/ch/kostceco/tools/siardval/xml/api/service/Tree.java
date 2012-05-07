package ch.kostceco.tools.siardval.xml.api.service;

import java.util.HashMap;
import java.util.Map;

public class Tree 
{
	private String name;
	
	private Tree parent;
	
	private Map<String, Tree> children = new HashMap<String, Tree>();

	public Tree(Tree parent, String name)
	{
		this.parent = parent;
		this.name = name;
	}
	
	public void addChild(Tree tree)
	{
		children.put(tree.getName(), tree);
	}
	
	public Tree getChild(String name)
	{
		return children.get(name);
	}
	
	public Tree[] getChildren()
	{
		return children.values().toArray(new Tree[0]);
	}
	
	public String getName()
	{
		return name;
	}
	
	public Tree getParent()
	{
		return parent;
	}
}
