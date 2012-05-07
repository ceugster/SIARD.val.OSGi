package ch.kostceco.tools.siardval.row.impl.internal.service;

import java.io.File;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import ch.kostceco.tools.siardval.row.api.service.TableRowValidator;
import ch.kostceco.tools.siardval.service.api.AbstractValidationStepService;

public class ValidationStepTableRowServiceComponent extends AbstractValidationStepService
{
	private TableRowValidator tableRowValidator;
	
	protected void bindTableRowValidator(TableRowValidator validator)
	{
		this.tableRowValidator = validator;
	}
	
	protected void unbindTableRowValidator(TableRowValidator validator)
	{
		this.tableRowValidator = null;
	}
	
	@Override
	public IStatus validate(File file)
	{
		//TODO
		return Status.OK_STATUS;
	}
}
