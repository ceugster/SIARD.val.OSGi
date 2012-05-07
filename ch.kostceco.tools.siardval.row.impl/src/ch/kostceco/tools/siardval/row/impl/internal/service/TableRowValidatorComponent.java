package ch.kostceco.tools.siardval.row.impl.internal.service;

import org.osgi.service.log.LogService;

import ch.kostceco.tools.siardval.row.api.service.TableRowValidator;

public class TableRowValidatorComponent implements TableRowValidator
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
