package ch.kostceco.tools.siardval.directory.impl.internal.service;

import java.io.File;

import org.eclipse.core.runtime.IStatus;
import org.osgi.service.log.LogService;

import ch.kostceco.tools.siardval.directory.api.service.DirectoryStructureValidator;
import ch.kostceco.tools.siardval.zip.api.service.ZipService;

public class DirectoryStructureValidatorComponent implements DirectoryStructureValidator
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
	public IStatus checkPrimaryStructure(File file) 
	{
		return zipService.checkTopLevelFolders(file);
	}
}
