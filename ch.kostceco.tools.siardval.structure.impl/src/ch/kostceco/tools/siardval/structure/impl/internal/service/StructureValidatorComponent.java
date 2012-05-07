package ch.kostceco.tools.siardval.structure.impl.internal.service;

import org.osgi.service.log.LogService;

import ch.kostceco.tools.siardval.structure.api.service.StructureValidator;

public class StructureValidatorComponent implements StructureValidator
{
	private LogService logService;
	
	protected void bindLogService(LogService service)
	{
		this.logService = service;
	}
	
	protected void unbindLogService(LogService service)
	{
		this.logService = null;
	}
}
