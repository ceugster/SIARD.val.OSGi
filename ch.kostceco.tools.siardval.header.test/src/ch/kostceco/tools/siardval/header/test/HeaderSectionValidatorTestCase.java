package ch.kostceco.tools.siardval.header.test;

import java.io.File;

import junit.framework.Assert;

import org.eclipse.core.runtime.IStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.osgi.service.log.LogService;
import org.osgi.util.tracker.ServiceTracker;

import ch.kostceco.tools.siardval.header.api.service.HeaderSectionValidator;
import ch.kostceco.tools.siardval.header.impl.internal.Activator;

public class HeaderSectionValidatorTestCase
{
	private String missingXml = "U:\\Incubator Projekte\\SIARD.val\\Test Data\\StepC-1-missing-xml.siard";
	
	private String missingXsd = "U:\\Incubator Projekte\\SIARD.val\\Test Data\\StepC-2-missing-xsd.siard";
	
	private String missingVersionAttributeXml = "U:\\Incubator Projekte\\SIARD.val\\Test Data\\StepC-3-missing-version-attribute.xml.siard";
	
	private String missingVersionAttributeXsd = "U:\\Incubator Projekte\\SIARD.val\\Test Data\\StepC-4-missing-version-attribute-xsd.siard";
	
	private String ok = "U:\\Incubator Projekte\\SIARD.val\\Test Data\\ok.siard";
	
	private ServiceTracker<LogService, LogService> logServiceTracker;
	
	private LogService logService;
	
	private ServiceTracker<HeaderSectionValidator, HeaderSectionValidator> headerSectionValidatorTracker;

	private HeaderSectionValidator headerSectionValidator;
	
	@Before
	public void setUp() throws Exception
	{
		headerSectionValidatorTracker = new ServiceTracker<HeaderSectionValidator, HeaderSectionValidator>(Activator.getBundleContext(), HeaderSectionValidator.class, null);
		headerSectionValidatorTracker.open();
		headerSectionValidator = headerSectionValidatorTracker.getService();

		logServiceTracker = new ServiceTracker<LogService, LogService>(Activator.getBundleContext(), LogService.class, null);
		logServiceTracker.open();
		logService = logServiceTracker.getService();
	}

	@After
	public void tearDown() throws Exception
	{
		logServiceTracker.close();
		headerSectionValidatorTracker.close();
	}

	@Test
	public void testRunningService()
	{
		Assert.assertNotNull(logService);
		Assert.assertNotNull(headerSectionValidator);
	}

	@Test
	public void testMissingXml()
	{
		logService.log(LogService.LOG_INFO, "Expecting missing metadata.xml.");
		File file = new File(missingXml);
		IStatus status = headerSectionValidator.validateMetadata(file);
		Assert.assertFalse(status.getMessage(), status.isOK());
	}
	
	@Test
	public void testMissingXsd()
	{
		logService.log(LogService.LOG_INFO, "Expecting missing metadata.xsd.");
		File file = new File(missingXsd);
		IStatus status = headerSectionValidator.validateSchema(file);
		Assert.assertFalse(status.getMessage(), status.isOK());
	}
	
	@Test
	public void testInvalidXml()
	{
		logService.log(LogService.LOG_INFO, "Expecting invalid metadata.xml (attribute version missing).");
		File file = new File(missingVersionAttributeXml);
		IStatus status = headerSectionValidator.validateMetadata(file);
		Assert.assertFalse(status.getMessage(), status.isOK());
	}
	
	@Test
	public void testInvalidXsd()
	{
		logService.log(LogService.LOG_INFO, "Expecting invalid metadata.xsd (attribute version missing).");
		File file = new File(missingVersionAttributeXsd);
		IStatus status = headerSectionValidator.validateSchema(file);
		Assert.assertFalse(status.getMessage(), status.isOK());
	}
	
	@Test
	public void testOk()
	{
		logService.log(LogService.LOG_INFO, "Expecting valid file.");
		File file = new File(ok);
		IStatus status = headerSectionValidator.validateSchema(file);
		Assert.assertTrue(status.getMessage(), status.isOK());
	}
	
}
