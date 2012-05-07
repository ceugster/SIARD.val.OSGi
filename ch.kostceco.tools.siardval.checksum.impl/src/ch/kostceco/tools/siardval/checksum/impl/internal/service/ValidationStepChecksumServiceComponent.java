package ch.kostceco.tools.siardval.checksum.impl.internal.service;

import java.io.File;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import ch.kostceco.tools.siardval.checksum.api.service.ChecksumValidator;
import ch.kostceco.tools.siardval.service.api.AbstractValidationStepService;

public class ValidationStepChecksumServiceComponent extends AbstractValidationStepService
{
	private ChecksumValidator checksumValidator;
	
	protected void bindChecksumValidator(ChecksumValidator validator)
	{
		this.checksumValidator = validator;
	}
	
	protected void unbindChecksumValidator(ChecksumValidator validator)
	{
		this.checksumValidator = null;
	}
	
	@Override
	public IStatus validate(File file)
	{
		// TODO
		checksumValidator.getAvailableAlgorithms();
		return Status.OK_STATUS;
	}
}
