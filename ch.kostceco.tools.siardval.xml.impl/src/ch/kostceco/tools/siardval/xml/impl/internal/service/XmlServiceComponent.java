package ch.kostceco.tools.siardval.xml.impl.internal.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.log.LogService;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import ch.kostceco.tools.siardval.xml.api.service.Tree;
import ch.kostceco.tools.siardval.xml.api.service.XmlService;
import ch.kostceco.tools.siardval.xml.impl.internal.Activator;
import ch.kostceco.tools.siardval.xml.impl.internal.MetadataContentHandler;
import ch.kostceco.tools.siardval.xml.impl.internal.ValidationErrorHandler;
import ch.kostceco.tools.siardval.zip.api.service.ZipService;

public class XmlServiceComponent implements XmlService
{
	private LogService logService;

	private ZipService zipService;
	
	protected void bindLogService(LogService service)
	{
		this.logService = service;
	}

	protected void unbindLogService(LogService service)
	{
		this.logService = null;
	}

	protected void bindZipService(ZipService service)
	{
		this.zipService = service;
	}

	protected void unbindZipService(ZipService service)
	{
		this.zipService = null;
	}

	protected void activate(ComponentContext componentContext)
	{
	}
	
	protected void deactivate(ComponentContext componentContext)
	{
	}

	@Override
	public IStatus validate(InputStream in, File schemaLocation)
	{
		IStatus status = Status.OK_STATUS;
		try 
		{
			URL url = schemaLocation.toURI().toURL();
			status = validate(in, url);
		} 
		catch (MalformedURLException e) 
		{
			status = new Status(IStatus.ERROR, Activator.getBundleContext().getBundle().getSymbolicName(), e.getMessage(), e);
		}
		return status;
	}

	@Override
	public IStatus validate(InputStream in, URL schemaLocation)
	{
		IStatus status = Status.OK_STATUS;
		try
		{
			SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
			Schema schema = factory.newSchema(schemaLocation);
			Validator validator = schema.newValidator();
			ValidationErrorHandler errorHandler = new ValidationErrorHandler(logService);
			validator.setErrorHandler(errorHandler);
			Source source = new StreamSource(in);
			validator.validate(source);
			status = errorHandler.getStatus();
		} 
		catch (IOException e)
		{
			status = new Status(IStatus.ERROR, Activator.getBundleContext().getBundle().getSymbolicName(), e.getMessage(), e);
		} 
		catch (SAXException e) 
		{
			status = new Status(IStatus.ERROR, Activator.getBundleContext().getBundle().getSymbolicName(), e.getMessage(), e);
		}
		return status;
	}

	@Override
	public IStatus validateSchema(URL schemaLocation)
	{
		IStatus status = Status.OK_STATUS;
		try
		{
			SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
			ValidationErrorHandler errorHandler = new ValidationErrorHandler(logService);
			factory.setErrorHandler(errorHandler);
			factory.newSchema(schemaLocation);
			status = errorHandler.getStatus();
		} 
		catch (SAXException e)
		{
			status = new Status(IStatus.ERROR, Activator.getBundleContext().getBundle().getSymbolicName(), "Validierungsfehler", e);
		}
		return status;
	}

	@Override
	public IStatus validateSchema(File schemaLocation)
	{
		IStatus status = Status.OK_STATUS;
		try 
		{
			URL url = schemaLocation.toURI().toURL();
			status = validateSchema(url);
		} 
		catch (MalformedURLException e) 
		{
			status = new Status(IStatus.ERROR, Activator.getBundleContext().getBundle().getSymbolicName(), e.getMessage(), e);
		}
		return status;
	}

	@Override
	public String getSiardVersion(File file) throws IOException
	{
		InputStream in = zipService.getEntryAsStream(file, "header/metadata.xml");
		Version version = new Version();
		InputSource source = new InputSource(in);
		try 
		{
			XMLReader reader = XMLReaderFactory.createXMLReader();
			reader.setContentHandler(new SiardVersionHandler(version));
			reader.parse(source);
		} 
		catch (SAXException e) 
		{
			return null;
		}
		return version.getVersion();
	}

	@Override
	public Tree extractContentTree(InputStream in) throws IOException, SAXException
	{
		InputSource source = new InputSource(in);
		MetadataContentHandler contentHandler = new MetadataContentHandler();
		XMLReader reader = XMLReaderFactory.createXMLReader();
		reader.setContentHandler(contentHandler);
		reader.parse(source);
		return contentHandler.getRootTree();
	}
	
	public class Version
	{
		private String version;
		
		public void setVersion(String version)
		{
			this.version = version;
		}
		
		public String getVersion()
		{
			return this.version;
		}
	}

}
