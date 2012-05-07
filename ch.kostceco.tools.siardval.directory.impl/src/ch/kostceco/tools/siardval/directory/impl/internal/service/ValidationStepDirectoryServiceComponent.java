package ch.kostceco.tools.siardval.directory.impl.internal.service;

import java.io.File;

import org.eclipse.core.runtime.IStatus;

import ch.kostceco.tools.siardval.directory.api.service.DirectoryStructureValidator;
import ch.kostceco.tools.siardval.service.api.AbstractValidationStepService;

public class ValidationStepDirectoryServiceComponent extends AbstractValidationStepService
{
	private DirectoryStructureValidator directoryStructureValidator;
	
	protected void bindDirectoryStructureValidator(DirectoryStructureValidator validator)
	{
		this.directoryStructureValidator = validator;
	}

	protected void unbindDirectoryStructureValidator(DirectoryStructureValidator validator)
	{
		this.directoryStructureValidator = null;
	}

	@Override
	public IStatus validate(File file)
	{
		return directoryStructureValidator.checkPrimaryStructure(file);
	}
}
