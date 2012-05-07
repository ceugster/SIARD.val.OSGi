package ch.kostceco.tools.siardval.xml.impl.internal;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.osgi.service.log.LogService;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;


public class ValidationErrorHandler implements ErrorHandler 
{
	private LogService logService;
	
	IStatus status = Status.OK_STATUS;
	
	public ValidationErrorHandler(LogService logService)
	{
		this.logService = logService;
	}
	
	@Override
	public void error(SAXParseException e) throws SAXException 
	{
		if (status.isOK() || status.getSeverity() == IStatus.ERROR)
		{
			status = new Status(IStatus.CANCEL, Activator.getBundleContext().getBundle().getSymbolicName(), "", e);
		}
		logService.log(LogService.LOG_ERROR, "Validation error on line " + e.getLineNumber() + " column " + e.getColumnNumber() + ": " + e.getMessage());
	}

	@Override
	public void fatalError(SAXParseException e) throws SAXException 
	{
		if (status.isOK() || status.getSeverity() == IStatus.ERROR)
		{
			status = new Status(IStatus.CANCEL, Activator.getBundleContext().getBundle().getSymbolicName(), "", e);
		}
		logService.log(LogService.LOG_ERROR, "Validation error on line " + e.getLineNumber() + " column " + e.getColumnNumber() + ": " + e.getMessage());
	}

	@Override
	public void warning(SAXParseException e) throws SAXException 
	{
		if (status.isOK())
		{
			status = new Status(IStatus.ERROR, Activator.getBundleContext().getBundle().getSymbolicName(), "", e);
		}
		logService.log(LogService.LOG_WARNING, "Validation error on line " + e.getLineNumber() + " column " + e.getColumnNumber() + ": " + e.getMessage());
	}
	
	public IStatus getStatus()
	{
		return this.status;
	}

}
