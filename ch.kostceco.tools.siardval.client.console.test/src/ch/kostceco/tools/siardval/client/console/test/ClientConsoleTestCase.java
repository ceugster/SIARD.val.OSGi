package ch.kostceco.tools.siardval.client.console.test;

import junit.framework.Assert;

import org.eclipse.osgi.framework.console.CommandProvider;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.osgi.util.tracker.ServiceTracker;

import ch.kostceco.tools.siardval.client.console.internal.Activator;

public class ClientConsoleTestCase 
{
	private ServiceTracker<CommandProvider, CommandProvider> commandProviderTracker;

	private CommandProvider commandProvider;
	
	@Before
	public void setUp() throws Exception
	{
		commandProviderTracker = new ServiceTracker<CommandProvider, CommandProvider>(Activator.getBundleContext(), CommandProvider.class, null);
		commandProviderTracker.open();
		
		commandProvider = commandProviderTracker.getService();
	}

	@After
	public void tearDown() throws Exception
	{
		commandProviderTracker.close();
	}

	@Test
	public void testRunningService()
	{
		Assert.assertNotNull(commandProvider);
	}

}
