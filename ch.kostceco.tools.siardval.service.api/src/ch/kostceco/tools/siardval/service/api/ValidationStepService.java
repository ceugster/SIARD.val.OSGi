package ch.kostceco.tools.siardval.service.api;

import java.io.File;

import org.eclipse.core.runtime.IStatus;

public interface ValidationStepService extends Comparable<ValidationStepService>
{
	IStatus validate(File file);
	
	Integer getServiceRanking();
}
