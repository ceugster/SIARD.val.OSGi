package ch.kostceco.tools.siardval.content.test;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.osgi.util.tracker.ServiceTracker;

import ch.kostceco.tools.siardval.content.api.service.ContentValidator;
import ch.kostceco.tools.siardval.content.impl.internal.Activator;
import ch.kostceco.tools.siardval.service.api.ValidationStepService;

public class ContentValidatorTestCase
{
	private ServiceTracker<ContentValidator, ContentValidator> contentValidatorTracker;

	private ContentValidator contentValidator;
	
	private ServiceTracker<ValidationStepService, ValidationStepService> validationStepServiceTracker;

	private ValidationStepService validationStepService;
	
	@Before
	public void setUp() throws Exception
	{
		contentValidatorTracker = new ServiceTracker<ContentValidator, ContentValidator>(Activator.getBundleContext(), ContentValidator.class, null);
		contentValidatorTracker.open();
		contentValidator = contentValidatorTracker.getService();

		validationStepServiceTracker = new ServiceTracker<ValidationStepService, ValidationStepService>(Activator.getBundleContext(), ValidationStepService.class, null);
		validationStepServiceTracker.open();
		validationStepService = validationStepServiceTracker.getService();
	}

	@After
	public void tearDown() throws Exception
	{
		validationStepServiceTracker.close();
		contentValidatorTracker.close();
	}

	@Test
	public void testRunningService()
	{
		Assert.assertNotNull(validationStepService);
		Assert.assertNotNull(contentValidator);
	}

}
