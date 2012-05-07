package ch.kostceco.tools.siardval.header.impl.internal.service;

import java.io.File;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import ch.kostceco.tools.siardval.header.api.service.HeaderSectionValidator;
import ch.kostceco.tools.siardval.service.api.AbstractValidationStepService;

public class HeaderSectionValidatorServiceComponent extends AbstractValidationStepService
{
	private HeaderSectionValidator headerSectionValidator;
	
	protected void bindHeaderSectionValidator(HeaderSectionValidator validator)
	{
		this.headerSectionValidator = validator;
	}

	protected void unbindHeaderSectionValidator(HeaderSectionValidator validator)
	{
		this.headerSectionValidator = null;
	}

	@Override
	public IStatus validate(File file)
	{
		IStatus status = Status.OK_STATUS;
		status = headerSectionValidator.validateSchema(file);
		if (!status.isOK())
		{
			status = headerSectionValidator.validateSchemaAgainstExternalSchema(file);
		}
		if (status.isOK())
		{
			status = headerSectionValidator.validateMetadata(file);
		}
		return status;
	}
}
