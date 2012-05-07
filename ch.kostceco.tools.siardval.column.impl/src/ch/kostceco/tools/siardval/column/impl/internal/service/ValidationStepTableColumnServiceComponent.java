package ch.kostceco.tools.siardval.column.impl.internal.service;

import java.io.File;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import ch.kostceco.tools.siardval.column.api.service.TableColumnValidator;
import ch.kostceco.tools.siardval.service.api.AbstractValidationStepService;

public class ValidationStepTableColumnServiceComponent extends AbstractValidationStepService
{
	private TableColumnValidator tableColumnValidator;
	
	protected void bindTableColumnValidator(TableColumnValidator validator)
	{
		this.tableColumnValidator = validator;
	}
	
	protected void unbindTableColumnValidator(TableColumnValidator validator)
	{
		this.tableColumnValidator = null;
	}
	
	@Override
	public IStatus validate(File file)
	{
		//TODO
		tableColumnValidator.toString();
		return Status.OK_STATUS;
	}
}
