package ch.kostceco.tools.siardval.content.impl.internal.service;

import java.io.File;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import ch.kostceco.tools.siardval.content.api.service.ContentValidator;
import ch.kostceco.tools.siardval.service.api.AbstractValidationStepService;

public class ValidationStepContentServiceComponent extends AbstractValidationStepService
{
	private ContentValidator contentValidator;
	
	protected void bindContentValidator(ContentValidator validator)
	{
		this.contentValidator = validator;
	}

	protected void unbindContentValidator(ContentValidator validator)
	{
		this.contentValidator = null;
	}

	@Override
	public IStatus validate(File file)
	{
		//TODO
		contentValidator.toString();
		return Status.OK_STATUS;
	}
}
