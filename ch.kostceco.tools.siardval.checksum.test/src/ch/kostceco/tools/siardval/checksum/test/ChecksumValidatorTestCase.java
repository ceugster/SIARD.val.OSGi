package ch.kostceco.tools.siardval.checksum.test;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.osgi.util.tracker.ServiceTracker;

import ch.kostceco.tools.siardval.checksum.api.service.ChecksumValidator;
import ch.kostceco.tools.siardval.checksum.impl.internal.Activator;
import ch.kostceco.tools.siardval.service.api.ValidationStepService;


public class ChecksumValidatorTestCase
{
	private ServiceTracker<ValidationStepService, ValidationStepService> validationStepServiceTracker;

	private ServiceTracker<ChecksumValidator, ChecksumValidator> checksumValidatorTracker;

	private ValidationStepService validationStepService;
	
	private ChecksumValidator checksumValidator;
	
	@Before
	public void setUp() throws Exception
	{
		validationStepServiceTracker = new ServiceTracker<ValidationStepService, ValidationStepService>(Activator.getBundleContext(), ValidationStepService.class, null);
		validationStepServiceTracker.open();
		validationStepService = validationStepServiceTracker.getService();

		checksumValidatorTracker = new ServiceTracker<ChecksumValidator, ChecksumValidator>(Activator.getBundleContext(), ChecksumValidator.class, null);
		checksumValidatorTracker.open();
		checksumValidator = checksumValidatorTracker.getService();
	}

	@After
	public void tearDown() throws Exception
	{
		checksumValidatorTracker.close();
		validationStepServiceTracker.close();
	}

	@Test
	public void testRunningServices()
	{
		Assert.assertNotNull(checksumValidator);
		Assert.assertNotNull(validationStepService);
	}

}
