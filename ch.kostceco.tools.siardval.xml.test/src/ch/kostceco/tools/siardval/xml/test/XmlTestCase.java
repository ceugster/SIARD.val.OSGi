package ch.kostceco.tools.siardval.xml.test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.osgi.util.tracker.ServiceTracker;
import org.xml.sax.SAXException;

import ch.kostceco.tools.siardval.xml.api.service.Tree;
import ch.kostceco.tools.siardval.xml.api.service.XmlService;
import ch.kostceco.tools.siardval.xml.impl.internal.Activator;
import ch.kostceco.tools.siardval.zip.api.service.ZipService;

public class XmlTestCase
{
	private String validSiardFile;
	
	private ServiceTracker<XmlService, XmlService> xmlServiceTracker;

	private XmlService xmlService;
	
	private ServiceTracker<ZipService, ZipService> zipServiceTracker;

	private ZipService zipService;
	
	@Before
	public void setUp() throws Exception
	{
		zipServiceTracker = new ServiceTracker<ZipService, ZipService>(Activator.getBundleContext(), ZipService.class, null);
		zipServiceTracker.open();
		zipService = zipServiceTracker.getService();

		xmlServiceTracker = new ServiceTracker<XmlService, XmlService>(Activator.getBundleContext(), XmlService.class, null);
		xmlServiceTracker.open();
		xmlService = xmlServiceTracker.getService();
	
		validSiardFile = "U:/Incubator Projekte/SIARD.val/Test Data/gebaeudeversicherung.siard";
	}

	@After
	public void tearDown() throws Exception
	{
		xmlServiceTracker.close();
		zipServiceTracker.close();
	}

	@Test
	public void testRunningService()
	{
		Assert.assertNotNull(xmlService);
		Assert.assertNotNull(zipService);
	}

	@Test
	public void testExtractSiardVersion()
	{
		Calendar start = GregorianCalendar.getInstance();
		System.out.println("Start extracting siard version: " + DateFormat.getDateTimeInstance().format(start.getTime()));
		try 
		{
			File file = new File(validSiardFile);
			String version = xmlService.getSiardVersion(file);
			Assert.assertTrue("Wrong SIARD-Version: " + version, "1.0".equals(version));
			System.out.println("SIARD-Version: " + version);
		} 
		catch (IOException e) 
		{
			Assert.fail("File could not be read");
		}
		Calendar end = GregorianCalendar.getInstance();
		System.out.println("End extracting siard version: " + DateFormat.getDateTimeInstance().format(end.getTime()));
		System.out.println("Time elapsed: " + (end.getTimeInMillis() - start.getTimeInMillis()) + " Milliseconds.");
		
	}
	
	@Test
	public void testContentTree()
	{
		try 
		{
			InputStream in = zipService.getEntryAsStream(new File(validSiardFile), "header/metadata.xml");
			Tree tree = xmlService.extractContentTree(in);
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (SAXException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
