package ch.kostceco.tools.siardval.xml.impl.internal.service;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import ch.kostceco.tools.siardval.xml.impl.internal.service.XmlServiceComponent.Version;

public class SiardVersionHandler extends DefaultHandler 
{
	private Version version;
	
	public SiardVersionHandler(Version version)
	{
		this.version = version;
	}
	
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException 
	{
		if (localName.equals("siardArchive"))
		{
			this.version.setVersion(attributes.getValue("version"));
		}
	}

}
