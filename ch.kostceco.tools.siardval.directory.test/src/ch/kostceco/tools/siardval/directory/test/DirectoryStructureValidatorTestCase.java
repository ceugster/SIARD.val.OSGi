package ch.kostceco.tools.siardval.directory.test;

import java.io.File;

import junit.framework.Assert;

import org.eclipse.core.runtime.IStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.osgi.service.log.LogService;
import org.osgi.util.tracker.ServiceTracker;

import ch.kostceco.tools.siardval.directory.impl.internal.Activator;
import ch.kostceco.tools.siardval.service.api.ValidationStepService;

public class DirectoryStructureValidatorTestCase
{
	private String additionalFile = "U:\\Incubator Projekte\\SIARD.val\\Test Data\\StepB-1-additional-file.siard";
	
	private String additionalFolder = "U:\\Incubator Projekte\\SIARD.val\\Test Data\\StepB-2-addition-folder.siard";
	
	private String missingHeader = "U:\\Incubator Projekte\\SIARD.val\\Test Data\\StepB-3-missing-header.siard";
	
	private String missingContent = "U:\\Incubator Projekte\\SIARD.val\\Test Data\\StepB-4-missing-content.siard";
	
	private String ok = "U:\\Incubator Projekte\\SIARD.val\\Test Data\\ok.siard";
	
	private ServiceTracker<LogService, LogService> logServiceTracker;
	
	private LogService logService;
	
	private ServiceTracker<ValidationStepService, ValidationStepService> validationStepServiceTracker;

	private ValidationStepService validationStepService;
	
	@Before
	public void setUp() throws Exception
	{
		validationStepServiceTracker = new ServiceTracker<ValidationStepService, ValidationStepService>(Activator.getBundleContext(), ValidationStepService.class, null);
		validationStepServiceTracker.open();
		validationStepService = validationStepServiceTracker.getService();

		logServiceTracker = new ServiceTracker<LogService, LogService>(Activator.getBundleContext(), LogService.class, null);
		logServiceTracker.open();
		logService = logServiceTracker.getService();
	}

	@After
	public void tearDown() throws Exception
	{
		logServiceTracker.close();
		validationStepServiceTracker.close();
	}

	@Test
	public void test()
	{
		Assert.assertNotNull(validationStepService);
		Assert.assertNotNull(logService);
	}

	@Test
	public void testAdditionalFile()
	{
		logService.log(LogService.LOG_INFO, "Expecting additional file in root directory.");
		File file = new File(additionalFile);
		IStatus status = validationStepService.validate(file);
		Assert.assertFalse(status.getMessage(), status.isOK());
		
	}

	@Test
	public void testAdditionalFolder()
	{
		logService.log(LogService.LOG_INFO, "Expecting additional directory in root directory.");
		File file = new File(additionalFolder);
		IStatus status = validationStepService.validate(file);
		Assert.assertFalse(status.getMessage(), status.isOK());
		
	}

	@Test
	public void testMissingHeader()
	{
		logService.log(LogService.LOG_INFO, "Expecting missing header directory.");
		File file = new File(missingHeader);
		IStatus status = validationStepService.validate(file);
		Assert.assertFalse(status.getMessage(), status.isOK());
		
	}

	@Test
	public void testMissingContent()
	{
		logService.log(LogService.LOG_INFO, "Expecting missing content directory.");
		File file = new File(missingContent);
		IStatus status = validationStepService.validate(file);
		Assert.assertFalse(status.getMessage(), status.isOK());
		
	}

	@Test
	public void testOk()
	{
		logService.log(LogService.LOG_INFO, "Expecting no errors.");
		File file = new File(ok);
		IStatus status = validationStepService.validate(file);
		Assert.assertTrue(status.getMessage(), status.isOK());
		
	}
}
