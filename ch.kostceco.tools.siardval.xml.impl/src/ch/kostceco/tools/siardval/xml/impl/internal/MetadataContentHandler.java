package ch.kostceco.tools.siardval.xml.impl.internal;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import ch.kostceco.tools.siardval.xml.api.service.Tree;

public class MetadataContentHandler extends DefaultHandler implements ContentHandler
{
	private Tree root = new Tree(null, "content");
	
	private Tree schemaTree;
	
	private Tree tableTree;
	
	private String currentParent;
	
	private StringBuilder characters;
	
	public Tree getRootTree()
	{
		return root;
	}
	
	@Override
	public void ignorableWhitespace(char[] ch, int start, int length)
			throws SAXException 
	{
		super.ignorableWhitespace(ch, start, length);
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException 
	{
		characters.append(new String(ch, start, length));
	}

	@Override
	public void endDocument() throws SAXException 
	{
		super.endDocument();
	}

	@Override
	public void endElement(String namespaceUri, String localName, String qualifiedName)
			throws SAXException 
	{
		if (namespaceUri.equals("http://www.bar.admin.ch/xmlns/siard/1.0/metadata.xsd"))
		{
			String name = characters.toString().replaceAll("\\W", "");
			if (this.currentParent != null && this.currentParent.equals("schema"))
			{
				if (localName.equals("folder"))
				{
					this.schemaTree = new Tree(this.root, name);
					this.root.addChild(this.schemaTree);
				}
			}
			else if (this.currentParent != null && this.currentParent.equals("table"))
			{
				if (localName.equals("folder"))
				{
					this.schemaTree.addChild(new Tree(this.root, name));
				}
			}
		}
	}

	@Override
	public void startDocument() throws SAXException 
	{
		super.startDocument();
	}

	@Override
	public void startElement(String namespaceUri, String localName, String qualifiedName,
			Attributes attributes) throws SAXException 
	{
		if (namespaceUri.equals("http://www.bar.admin.ch/xmlns/siard/1.0/metadata.xsd"))
		{
			characters = new StringBuilder();
			if (localName == "schema")
			{
				this.currentParent = localName;
			}
			else if (localName.equals("table"))
			{
				this.currentParent = localName;
			}
			else
			{
				this.currentParent = null;
			}
		}
	}

}
