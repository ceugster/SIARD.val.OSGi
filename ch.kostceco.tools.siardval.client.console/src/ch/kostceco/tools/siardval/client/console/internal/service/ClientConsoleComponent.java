package ch.kostceco.tools.siardval.client.console.internal.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.osgi.framework.console.CommandInterpreter;
import org.eclipse.osgi.framework.console.CommandProvider;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.log.LogService;

import ch.kostceco.tools.siardval.service.api.ValidationStepService;

public class ClientConsoleComponent implements CommandProvider 
{
	private LogService logService;

	private List<ValidationStepService> validatorServices = new ArrayList<ValidationStepService>();

	protected void bindLogService(LogService service)
	{
		this.logService = service;
	}
	
	protected void unbindLogService(LogService service)
	{
		this.logService = null;
	}

	protected void bindValidatorService(ValidationStepService service)
	{
		this.validatorServices.add(service);
	}
	
	protected void unbindValidatorService(ValidationStepService service)
	{
		this.validatorServices.remove(service);
	}
	
	protected void activate(ComponentContext context)
	{
		logService.log(LogService.LOG_INFO, "Ready");
	}
	
	protected void deactivate(ComponentContext context)
	{
		logService.log(LogService.LOG_INFO, "Not ready");
	}
	
	public void _validate(CommandInterpreter commandInterpreter)
	{
		String	path = commandInterpreter.nextArgument();
		if (path == null)
		{
			logService.log(LogService.LOG_ERROR, "Invalid input: path to siard file required.");
			commandInterpreter.println("Usage: validate <path>\n");
			return;
		}
		
		File file = new File(path);
		if (!file.exists())
		{
			logService.log(LogService.LOG_ERROR, "Invalid input: path to siard file must exist.");
			commandInterpreter.println("path " + path + " does not exist.");
			return;
		}

		ValidationStepService[] services = validatorServices.toArray(new ValidationStepService[0]);
		Arrays.sort(services);
		for (ValidationStepService service : services)
		{
			service.validate(file);
		}
		commandInterpreter.println();
		commandInterpreter.println("Ready");
	}
	
	@Override
	public String getHelp() 
	{
		return new StringBuilder("Usage: validate [options] [path]\n\n")
		.append("Options:\n").toString();
	}

}
