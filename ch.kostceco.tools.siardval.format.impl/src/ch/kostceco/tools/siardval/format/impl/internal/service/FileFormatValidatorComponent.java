package ch.kostceco.tools.siardval.format.impl.internal.service;

import java.io.File;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.osgi.service.log.LogService;

import ch.kostceco.tools.siardval.format.api.service.FileFormatValidator;
import ch.kostceco.tools.siardval.format.impl.internal.Activator;
import ch.kostceco.tools.siardval.zip.api.service.ZipService;

public class FileFormatValidatorComponent implements FileFormatValidator
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

	@Override
	public IStatus checkDirectory(File file) 
	{
		logService.log(LogService.LOG_INFO, "Check if file is directory.");
		IStatus status = Status.OK_STATUS;
		if (file.isDirectory())
		{
			status = new Status(IStatus.ERROR, Activator.getBundleContext().getBundle().getSymbolicName(), "Specified file is a directory.");
			logService.log(LogService.LOG_ERROR, status.getMessage());
		}
		else
		{
			logService.log(LogService.LOG_INFO, "File ist not a directory.");
		}
		return status;
	}

	@Override
	public IStatus checkReadable(File file) 
	{
		logService.log(LogService.LOG_INFO, "Check if file can be read.");
		IStatus status = Status.OK_STATUS;
		if (!file.canRead())
		{
			status = new Status(IStatus.ERROR, Activator.getBundleContext().getBundle().getSymbolicName(), "Specified file cannot be read.");
			logService.log(LogService.LOG_ERROR, status.getMessage());
		}
		else
		{
			logService.log(LogService.LOG_INFO, "File can be read.");
		}
		return status;
	}

	public IStatus checkIntegrity(File file)
	{
		return zipService.checkIntegrity(file);
	}
	
	public IStatus checkCompression(File file)
	{
		return zipService.checkCompression(file);
	}
	
	public IStatus checkEncryption(File file)
	{
		return zipService.checkEncryption(file);
	}
	
	public IStatus checkExtension(File file)
	{
		return zipService.checkExtension(file);
	}
}
