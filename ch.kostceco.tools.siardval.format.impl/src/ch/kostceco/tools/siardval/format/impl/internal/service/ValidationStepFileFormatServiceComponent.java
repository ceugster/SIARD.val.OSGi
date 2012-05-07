package ch.kostceco.tools.siardval.format.impl.internal.service;

import java.io.File;

import org.eclipse.core.runtime.IStatus;

import ch.kostceco.tools.siardval.format.api.service.FileFormatValidator;
import ch.kostceco.tools.siardval.service.api.AbstractValidationStepService;

public class ValidationStepFileFormatServiceComponent extends AbstractValidationStepService
{
	private FileFormatValidator fileFormatValidator;
	
	protected void bindFileFormatValidator(FileFormatValidator validator)
	{
		this.fileFormatValidator = validator;
	}

	protected void unbindFileFormatValidator(FileFormatValidator validator)
	{
		this.fileFormatValidator = null;
	}

	@Override
	public IStatus validate(File file)
	{
		IStatus status = fileFormatValidator.checkReadable(file);
		if (status.isOK())
		{
			status = fileFormatValidator.checkDirectory(file);
			if (status.isOK())
			{
				status = fileFormatValidator.checkIntegrity(file);
				if (status.isOK())
				{
					status = fileFormatValidator.checkEncryption(file);
					if (status.isOK())
					{
						status = fileFormatValidator.checkCompression(file);
						if (status.isOK())
						{
							status = fileFormatValidator.checkExtension(file);
						}
					}
				}
			}
		}
		return status;
	}

}
