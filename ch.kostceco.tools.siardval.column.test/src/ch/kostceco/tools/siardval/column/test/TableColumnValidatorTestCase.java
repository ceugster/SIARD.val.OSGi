package ch.kostceco.tools.siardval.column.test;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.osgi.util.tracker.ServiceTracker;

import ch.kostceco.tools.siardval.column.api.service.TableColumnValidator;
import ch.kostceco.tools.siardval.column.impl.internal.Activator;
import ch.kostceco.tools.siardval.service.api.ValidationStepService;

public class TableColumnValidatorTestCase
{
	private ServiceTracker<TableColumnValidator, TableColumnValidator> tableColumnValidatorTracker;

	private TableColumnValidator tableColumnValidator;
	
	private ServiceTracker<ValidationStepService, ValidationStepService> validationStepServiceTracker;

	private ValidationStepService validationStepService;
	
	@Before
	public void setUp() throws Exception
	{
		tableColumnValidatorTracker = new ServiceTracker<TableColumnValidator, TableColumnValidator>(Activator.getBundleContext(), TableColumnValidator.class, null);
		tableColumnValidatorTracker.open();
		tableColumnValidator = tableColumnValidatorTracker.getService();

		validationStepServiceTracker = new ServiceTracker<ValidationStepService, ValidationStepService>(Activator.getBundleContext(), ValidationStepService.class, null);
		validationStepServiceTracker.open();
		validationStepService = validationStepServiceTracker.getService();
	}

	@After
	public void tearDown() throws Exception
	{
		validationStepServiceTracker.close();
		tableColumnValidatorTracker.close();
	}

	@Test
	public void testRunningService()
	{
		Assert.assertNotNull(validationStepService);
		Assert.assertNotNull(tableColumnValidator);
	}

}
