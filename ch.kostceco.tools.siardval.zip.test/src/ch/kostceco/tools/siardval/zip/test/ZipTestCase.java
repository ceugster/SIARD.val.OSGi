package ch.kostceco.tools.siardval.zip.test;

import java.io.File;

import junit.framework.Assert;

import org.eclipse.core.runtime.IStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.osgi.util.tracker.ServiceTracker;

import ch.kostceco.tools.siardval.zip.api.service.ZipService;
import ch.kostceco.tools.siardval.zip.impl.internal.Activator;

public class ZipTestCase
{
	private String validSiardFile = "U:/Incubator Projekte/SIARD.val/Test Data/gebaeudeversicherung.siard";
	
	private ServiceTracker<ZipService, ZipService> zipServiceTracker;

	private ZipService zipService;
	
	@Before
	public void setUp() throws Exception
	{
		zipServiceTracker = new ServiceTracker<ZipService, ZipService>(Activator.getBundleContext(), ZipService.class, null);
		zipServiceTracker.open();
		
		zipService = zipServiceTracker.getService();
	}

	@After
	public void tearDown() throws Exception
	{
		zipServiceTracker.close();
	}

	@Test
	public void testRunningService()
	{
		Assert.assertNotNull(zipService);
	}

	@Test
	public void testExtension()
	{
		File file = new File(validSiardFile);
		IStatus status = zipService.checkExtension(file);
		Assert.assertTrue(status.getMessage(), status.isOK());
	}

}
