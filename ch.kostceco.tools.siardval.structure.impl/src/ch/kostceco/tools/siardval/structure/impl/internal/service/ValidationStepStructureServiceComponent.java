package ch.kostceco.tools.siardval.structure.impl.internal.service;

import java.io.File;

import org.eclipse.core.runtime.IStatus;

import ch.kostceco.tools.siardval.service.api.AbstractValidationStepService;
import ch.kostceco.tools.siardval.structure.api.service.StructureValidator;

public class ValidationStepStructureServiceComponent extends AbstractValidationStepService
{
	private StructureValidator structureValidator;
	
	protected void bindStructureValidator(StructureValidator validator)
	{
		this.structureValidator = validator;
	}
	
	protected void unbindStructureValidator(StructureValidator validator)
	{
		this.structureValidator = null;
	}
	
	@Override
	public IStatus validate(File file)
	{
		// TODO Auto-generated method stub
		return null;
	}

}
