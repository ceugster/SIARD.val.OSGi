package ch.kostceco.tools.siardval.row.test;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.osgi.util.tracker.ServiceTracker;

import ch.kostceco.tools.siardval.row.impl.internal.Activator;
import ch.kostceco.tools.siardval.service.api.ValidationStepService;

public class TableRowValidatorTestCase
{
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

}
