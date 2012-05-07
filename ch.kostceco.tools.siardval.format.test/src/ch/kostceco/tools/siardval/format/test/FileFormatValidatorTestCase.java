package ch.kostceco.tools.siardval.format.test;

import java.io.File;

import junit.framework.Assert;

import org.eclipse.core.runtime.IStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.osgi.util.tracker.ServiceTracker;

import ch.kostceco.tools.siardval.format.impl.internal.Activator;
import ch.kostceco.tools.siardval.service.api.ValidationStepService;

public class FileFormatValidatorTestCase
{
	private String compressed = "U:\\Incubator Projekte\\SIARD.val\\Test Data\\StepA-1-compressed.siard";
	
	private String encrypted = "U:\\Incubator Projekte\\SIARD.val\\Test Data\\StepA-2-encrypted.siard";
	
	private String ok = "U:\\Incubator Projekte\\SIARD.val\\Test Data\\ok.siard";
	
	private ServiceTracker<ValidationStepService, ValidationStepService> validationStepServiceTracker;

	private ValidationStepService validationStepService;
	
	@Before
	public void setUp() throws Exception
	{
		validationStepServiceTracker = new ServiceTracker<ValidationStepService, ValidationStepService>(Activator.getBundleContext(), ValidationStepService.class, null);
		validationStepServiceTracker.open();
		validationStepService = validationStepServiceTracker.getService();
	}

	@After
	public void tearDown() throws Exception
	{
		validationStepServiceTracker.close();
	}

	@Test
	public void testRunningService()
	{
		Assert.assertNotNull(validationStepService);
	}

	@Test
	public void testCompressed()
	{
		File file = new File(compressed);
		IStatus status = validationStepService.validate(file);
		Assert.assertFalse(status.getMessage(), status.isOK());
	}

	@Test
	public void testEncrypted()
	{
		File file = new File(encrypted);
		IStatus status = validationStepService.validate(file);
		Assert.assertFalse(status.getMessage(), status.isOK());
	}

	@Test
	public void testOk()
	{
		File file = new File(ok);
		IStatus status = validationStepService.validate(file);
		Assert.assertTrue(status.getMessage(), status.isOK());
	}

}
