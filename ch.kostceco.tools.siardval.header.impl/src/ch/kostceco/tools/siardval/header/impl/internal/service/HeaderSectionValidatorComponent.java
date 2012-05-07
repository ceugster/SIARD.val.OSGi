package ch.kostceco.tools.siardval.header.impl.internal.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.zip.ZipException;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.osgi.service.log.LogService;

import ch.kostceco.tools.siardval.checksum.api.service.ChecksumValidator;
import ch.kostceco.tools.siardval.header.api.service.HeaderSectionValidator;
import ch.kostceco.tools.siardval.header.impl.internal.Activator;
import ch.kostceco.tools.siardval.xml.api.service.XmlService;
import ch.kostceco.tools.siardval.zip.api.service.ZipService;

public class HeaderSectionValidatorComponent implements HeaderSectionValidator
{
	private LogService logService;
	
	private ZipService zipService;
	
	private XmlService xmlService;
	
	private ChecksumValidator checksumValidator;
	
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

	protected void bindXmlService(XmlService service)
	{
		this.xmlService = service;
	}

	protected void unbindXmlService(XmlService service)
	{
		this.xmlService = null;
	}

	protected void bindChecksumValidator(ChecksumValidator validator)
	{
		this.checksumValidator = validator;
	}

	protected void unbindChecksumValidator(ChecksumValidator validator)
	{
		this.checksumValidator = null;
	}

	@Override
	public IStatus validateMetadata(File file)
	{
		IStatus status = Status.OK_STATUS;
		try 
		{
			InputStream in = zipService.getEntryAsStream(file, "header/metadata.xml");
			URL url = this.getClass().getResource("/META-INF/metadata.xsd");
			status = xmlService.validate(in, url);
			if (!status.isOK())
			{
				logService.log(LogService.LOG_ERROR, status.getMessage());
			}
		} 
		catch (ZipException e) 
		{
			status = new Status(IStatus.ERROR, Activator.getBundleContext().getBundle().getSymbolicName(), "Entry metadata.xml not found in header directory of " + file.getAbsolutePath() + ".", e);
			logService.log(LogService.LOG_ERROR, status.getMessage());
		} 
		catch (IOException e) 
		{
			status = new Status(IStatus.ERROR, Activator.getBundleContext().getBundle().getSymbolicName(), "Error opening file " + file.getAbsolutePath() + ".", e);
			logService.log(LogService.LOG_ERROR, status.getMessage());
		}
		return status;
	}

	@Override
	public IStatus validateSchema(File file) 
	{
		IStatus status = Status.OK_STATUS;
		
		InputStream inputStream = null;
		try 
		{
			inputStream = zipService.getEntryAsStream(file, "header/metadata.xsd");
			String siardDigest = checksumValidator.getDigest(inputStream, "MD-5");
			inputStream.close();
			inputStream = this.getClass().getResourceAsStream("/META-INF/metadata.xsd");
			String internalDigest = checksumValidator.getDigest(inputStream, "MD-5");
			inputStream.close();
			if (siardDigest.equals(internalDigest))
			{
				logService.log(LogService.LOG_INFO, "Valid metadata.xsd found.");
			}
			else
			{
				status = new Status(IStatus.ERROR, Activator.getBundleContext().getBundle().getSymbolicName(), "Invalid metadata.xsd found using checksum.");
				logService.log(LogService.LOG_ERROR, status.getMessage());
			}
		} 
		catch (ZipException e) 
		{
			status = new Status(IStatus.ERROR, Activator.getBundleContext().getBundle().getSymbolicName(), "Entry metadata.xsd not found in header section of file " + file.getAbsolutePath() + ".", e);
			logService.log(LogService.LOG_ERROR, status.getMessage());
		} 
		catch (IOException e) 
		{
			status = new Status(IStatus.ERROR, Activator.getBundleContext().getBundle().getSymbolicName(), "Error opening file " + file.getAbsolutePath() + ".", e);
			logService.log(LogService.LOG_ERROR, status.getMessage());
		}
		finally
		{
			if (inputStream != null)
			{
				try
				{
					inputStream.close();
				}
				catch (IOException e)
				{
					// Do nothing
				}
			}
		}
		return status;
	}

	@Override
	public IStatus validateSchemaAgainstExternalSchema(File file) {
		// TODO Auto-generated method stub
		return null;
	}
}
