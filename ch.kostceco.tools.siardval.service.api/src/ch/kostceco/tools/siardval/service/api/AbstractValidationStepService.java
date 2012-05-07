package ch.kostceco.tools.siardval.service.api;

import org.osgi.framework.Constants;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.log.LogService;

public abstract class AbstractValidationStepService implements ValidationStepService
{
	protected ComponentContext componentContext;
	
	protected LogService logService;
	
	protected void bindLogService(LogService service)
	{
		this.logService = service;
	}

	protected void unbindLogService(LogService service)
	{
		this.logService = null;
	}

	protected void activate(ComponentContext componentContext)
	{
		this.componentContext = componentContext;
	}
	
	protected void deactivate(ComponentContext componentContext)
	{
		this.componentContext = null;
	}
	
	@Override
	public int compareTo(ValidationStepService other) 
	{
		return other.getServiceRanking().compareTo(this.getServiceRanking());
	}

	@Override
	public Integer getServiceRanking() 
	{
		Object ranking = this.componentContext.getProperties().get(Constants.SERVICE_RANKING);
		return ranking instanceof Integer ? (Integer) ranking : Integer.valueOf(0);
	}


}
