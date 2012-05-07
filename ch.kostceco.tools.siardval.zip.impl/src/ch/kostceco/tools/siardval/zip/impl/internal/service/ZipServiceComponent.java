package ch.kostceco.tools.siardval.zip.impl.internal.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipException;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.log.LogService;

import ch.kostceco.tools.siardval.zip.api.service.ZipService;
import ch.kostceco.tools.siardval.zip.impl.internal.Activator;
import de.schlichtherle.truezip.zip.ZipEntry;
import de.schlichtherle.truezip.zip.ZipFile;

public class ZipServiceComponent implements ZipService
{
	public static final String PROPERTY_KEY_SIARD_EXTENSIONS = "siard.extensions";
	
	public static final String PROPERTY_KEY_TOP_LEVEL_FOLDERS = "top.level.folders";
	
	private ComponentContext componentContext;
	
	private LogService logService;
	
	protected void bindLogService(LogService service)
	{
		this.logService = service;
	}

	protected void unbindLogService(LogService service)
	{
		this.logService = null;
	}

	protected void activate(ComponentContext componentContext)
	{
		this.componentContext = componentContext;
	}
	
	protected void deactivate(ComponentContext componentContext)
	{
		this.componentContext = null;
	}

	@Override
	public InputStream getEntryAsStream(File file, String path) throws IOException, ZipException
	{
		ZipFile zipFile = new ZipFile(file);
		ZipEntry entry = zipFile.getEntry(path);
		if (entry == null)
		{
			throw new ZipException("Entry " + path + " does not exist.");
		}
		return zipFile.getInputStream(entry);
	}

	/**
	 * Returns the entry specified by path as an URL. The entry is read and written to
	 * a temporary folder in the file system (as a default the temp file of the user is 
	 * used if available or the system's temp folder. 
	 * 
	 * The client is responsible for the life cycle of the underlying file of the generated URL.
	 * 
	 * @param file
	 * @param path
	 * @return
	 * @throws IOException
	 */
	@Override
	public URL getEntryAsUrl(File file, String path) throws IOException
	{
		return getEntryAsFile(file, path).toURI().toURL();
	}

	@Override
	public IStatus checkCompression(File file)
	{
		IStatus status = Status.OK_STATUS;
		try
		{
			int errors = 0;
			logService.log(LogService.LOG_INFO, "Checking file " + file.getAbsolutePath() + " for compression methods");
			ZipFile zipFile = new ZipFile(file);
			try
			{
				Enumeration<? extends ZipEntry> entries = zipFile.entries();
				while (entries.hasMoreElements())
				{
					ZipEntry entry = entries.nextElement();
					if (entry.getMethod() == ZipEntry.STORED)
					{
						logService.log(LogService.LOG_INFO, "Checking entry " + entry.getName() + ": " + Method.getMethod(entry.getMethod()).toString());
					}
					else
					{
						if (status.isOK())
						{
							status = new Status(IStatus.CANCEL, Activator.getBundleContext().getBundle().getSymbolicName(), "File " + file.getAbsolutePath() + " is compressed (see log file for details).");
						}
						errors++;
						logService.log(LogService.LOG_ERROR, "Checking entry " + entry.getName() + ": " + Method.getMethod(entry.getMethod()).toString());
					}
				}
			}
			finally
			{
				zipFile.close();
				if (status.isOK())
				{
					logService.log(LogService.LOG_INFO, "Compression check found no errors.");
				}
				else
				{
					logService.log(LogService.LOG_INFO, "Compression check found " + errors + " errors.");
				}
			}
		}
		catch (IOException e)
		{
			status = new Status(IStatus.ERROR, Activator.getBundleContext().getBundle().getSymbolicName(), "Error opening file " + file.getAbsolutePath());
		}
		return status;
	}

	/**
	 * Checks if file is a valid zip file. This method equals to checkZip(file, Method.HEADER_CHECK)
	 * 
	 * @param file
	 * @return
	 */
	@Override
	public IStatus checkIntegrity(File file)
	{
		return checkIntegrity(file, ZipService.Method.CHECK_ENTRIES);
	}

	@Override
	public IStatus checkIntegrity(File file, ZipService.Method method)
	{
		IStatus status = Status.OK_STATUS;
		switch (method)
		{
		case CHECK_FILE:
		{
			status = checkFile(file);
			break;
		}
		case CHECK_ENTRIES:
		{
			status = checkEntries(file);
			break;
		}
		default:
		{
			status = checkEntries(file);
			break;
		}
		}
		return status;
	}

	private IStatus checkFile(File file)
	{
		logService.log(LogService.LOG_INFO, "Checking integrity (readability) of file " + file.getAbsolutePath() + " (HEADER_CHECK)");
		IStatus status = Status.OK_STATUS;
	    try 
	    {
	        ZipFile zipFile = new ZipFile(file);
	        zipFile.close();
			logService.log(LogService.LOG_INFO, "Integrity successfully checked.");
	    } 
	    catch (ZipException e) 
	    {
			status = new Status(IStatus.ERROR, Activator.getBundleContext().getBundle().getSymbolicName(), "Read error occured.", e);
			logService.log(LogService.LOG_ERROR, status.getMessage());
	    } 
	    catch (IOException e) 
	    {
			status = new Status(IStatus.ERROR, Activator.getBundleContext().getBundle().getSymbolicName(), "Error opening file " + file.getAbsolutePath() + ".", e);
			logService.log(LogService.LOG_ERROR, status.getMessage());
	    }
	    return status;
	}

	/**
	 * Perform a full check of entries. Open each entry and read whole stream in
	 * 
	 * @param file
	 * @return
	 */
	private IStatus checkEntries(File file)
	{
		IStatus status = Status.OK_STATUS;
	    try 
	    {
	    	int errors = 0;
			logService.log(LogService.LOG_INFO, "Check integrity (readability) of file " + file.getName() + " (FULL_CHECK).");
	        ZipFile zipFile = new ZipFile(file);
	        Enumeration<? extends ZipEntry> entries = zipFile.entries();
	        while (entries.hasMoreElements())
	        {
	        	ZipEntry entry = entries.nextElement();
	        	InputStream in = null;
        		if (entry.isDirectory())
        		{
        			logService.log(LogService.LOG_INFO, "Entry " + entry.getName() + " is directory.");
        		}
        		else
        		{
    	        	try
    	        	{
    	        		int size = 0;
		        		in = zipFile.getInputStream(entry);
			        	while (in.available() > 0)
			        	{
			        		byte[] b = new byte[in.available()];
			        		size += in.read(b, 0, in.available());
			        	}
	        			logService.log(LogService.LOG_INFO, "Entry " + entry.getName() + " checked: size " + size + " bytes.");
	        		}
    	        	catch(IOException e)
    	        	{
    	        		errors++;
	        			logService.log(LogService.LOG_ERROR, "Entry " + entry.getName() + " checked: error occured.");
    	        	}
    	        	finally
    	        	{
    	        		if (in != null)
    	        		{
    	    	        	in.close();
    	        		}
    	        	}
	        	}
	        }
	        zipFile.close();
			if (status.isOK())
			{
				logService.log(LogService.LOG_INFO, "Encryption check found no errors.");
			}
			else
			{
				logService.log(LogService.LOG_INFO, "Encryption check found " + errors + " errors.");
			}
	    } 
	    catch (ZipException e) 
	    {
			status = new Status(IStatus.ERROR, Activator.getBundleContext().getBundle().getSymbolicName(), "Read error occured.", e);
			logService.log(LogService.LOG_ERROR, status.getMessage());
	    } 
	    catch (IOException e) 
	    {
			status = new Status(IStatus.ERROR, Activator.getBundleContext().getBundle().getSymbolicName(), "Error opening " + file.getAbsolutePath() + ".", e);
			logService.log(LogService.LOG_ERROR, status.getMessage());
	    } 
	    return status;
	}

	@Override
	public IStatus checkEncryption(File file)
	{
		IStatus status = Status.OK_STATUS;
		try
		{
			int errors = 0;
			logService.log(LogService.LOG_INFO, "Check encryption of file " + file.getName() + ".");
			ZipFile zipFile = new ZipFile(file);
			try
			{
				Enumeration<? extends ZipEntry> entries = zipFile.entries();
				while (entries.hasMoreElements())
				{
					ZipEntry entry = entries.nextElement();
					if (entry.isDirectory())
					{
						logService.log(LogService.LOG_INFO, "Entry " + entry.getName() + " is directory.");
					}
					else if (entry.isEncrypted())
					{
						if (status.isOK())
						{
							status = new Status(IStatus.CANCEL, Activator.getBundleContext().getBundle().getSymbolicName(), "File " + file.getAbsolutePath() + " is encrypted (see log file for details).");
						}
						errors++;
						logService.log(LogService.LOG_ERROR, "Entry " + entry.getName() + " is encrypted.");
					}
					else
					{
						logService.log(LogService.LOG_INFO, "Entry " + entry.getName() + " is not encrypted.");
					}
				}
			}
			finally
			{
				zipFile.close();
				if (status.isOK())
				{
					logService.log(LogService.LOG_INFO, "Encryption check found no errors.");
				}
				else
				{
					logService.log(LogService.LOG_INFO, "Encryption check found " + errors + " errors.");
				}
			}
		}
		catch (IOException e)
		{
			status = new Status(IStatus.ERROR, Activator.getBundleContext().getBundle().getSymbolicName(), "Error opening file " + file.getAbsolutePath() + ".", e);
		}
		return status;
	}

	/**
	 * Returns the entry specified by path as a file. The entry is read and written to
	 * a temporary folder in the file system (as a default the temp file of the user is 
	 * used if available or the system's temp folder. 
	 * 
	 * The client is responsible for the life cycle of the generated file.
	 * 
	 * @param file
	 * @param path
	 * @return
	 * @throws IOException
	 */
	@Override
	public File getEntryAsFile(File file, String path) throws IOException
	{
		InputStream in = this.getEntryAsStream(file, path);
		File tmp = File.createTempFile("tmp_", ".siard");
		OutputStream out = new FileOutputStream(tmp);
		try
		{
			byte[] b = new byte[in.available()];
			while (in.available() > 0)
			{
				int size = in.read(b, 0, in.available());
				out.write(b, 0, size);
			}
		}
		finally
		{
			out.close();
			in.close();
		}
		return tmp;
	}

	public enum Method
	{
		STORED, DEFLATED, BZIP2, UNKNOWN;
		
		public static Method getMethod(int method)
		{
			switch(method)
			{
			case ZipEntry.STORED:
			{
				return STORED;
			}
			case ZipEntry.DEFLATED:
			{
				return DEFLATED;
			}
			case ZipEntry.BZIP2:
			{
				return BZIP2;
			}
			default:
			{
				return UNKNOWN;
			}
			}
		}
	}

	@Override
	public IStatus checkExtension(File file) 
	{
		logService.log(LogService.LOG_ERROR, "Check file extension.");

		String[] segments = file.getName().split("[.]");
		IStatus status = Status.OK_STATUS;
		Object object = componentContext.getProperties().get(PROPERTY_KEY_SIARD_EXTENSIONS);
		if (object instanceof String)
		{
			String extension = (String) object;
			if (!file.getName().endsWith(extension))
			{
				status = new Status(IStatus.ERROR, Activator.getBundleContext().getBundle().getSymbolicName(), "Invalid file extension found: " + segments[segments.length - 1] + " instead of " + extension);
				logService.log(LogService.LOG_ERROR, status.getMessage());
			}
		}
		else if (object instanceof String[])
		{
			String[] extensions = (String[]) object;
			status = new Status(IStatus.ERROR, Activator.getBundleContext().getBundle().getSymbolicName(), "Invalid file extension found: " + segments[segments.length - 1] + " instead one of " + extensions);
			for (String extension : extensions)
			{
				if (file.getName().endsWith(extension))
				{
					status = Status.OK_STATUS;
					break;
				}
			}
			if (!status.isOK())
			{
				logService.log(LogService.LOG_ERROR, status.getMessage());
			}
		}
		return status;
	}

	@Override
	public IStatus checkTopLevelFolders(File file) 
	{
		IStatus status = Status.OK_STATUS;
		Map<String, String> topLevelFolders = new HashMap<String, String>(); 
		try
		{
			ZipFile zipFile = new ZipFile(file);
			Enumeration<? extends ZipEntry> entries = zipFile.entries();
			while (entries.hasMoreElements())
			{
				ZipEntry entry = entries.nextElement();
				String[] segments = entry.getName().split("/");
				if (segments.length == 1)
				{
					topLevelFolders.put(entry.getName().replace("/", ""), entry.getName());
				}
			}
			Map<String, String> validTopLevelFolders = getValidTopLevelFolders();
			Collection<String> validFolders = validTopLevelFolders.values();
			for (String validFolder : validFolders)
			{
				String value = topLevelFolders.get(validFolder);
				if (value == null)
				{
					status = new Status(IStatus.CANCEL, Activator.getBundleContext().getBundle().getSymbolicName(), "Top level entry '" + validFolder + "' missing.");
					logService.log(LogService.LOG_ERROR, status.getMessage());
				}
				else if (!value.endsWith("/"))
				{
					status = new Status(IStatus.CANCEL, Activator.getBundleContext().getBundle().getSymbolicName(), "Top level entry '" + validFolder + "' is not a directory.");
					logService.log(LogService.LOG_ERROR, status.getMessage());
				}
				else
				{
					logService.log(LogService.LOG_INFO, "Top level entry '" + validFolder + "' exists.");
				}
			}
			/*
			 * Check for files and folders not expected
			 */
			Set<String> folders = topLevelFolders.keySet();
			for (String folder : folders)
			{
				String value = validTopLevelFolders.get(folder);
				if (value == null)
				{
					if (folder.endsWith("/"))
					{
						status = new Status(IStatus.ERROR, Activator.getBundleContext().getBundle().getSymbolicName(), "Invalid top level directory entry '" + folder + "' found.");
						logService.log(LogService.LOG_ERROR, status.getMessage());
					}
					else
					{
						status = new Status(IStatus.ERROR, Activator.getBundleContext().getBundle().getSymbolicName(), "Invalid top level file entry '" + folder + "' found.");
						logService.log(LogService.LOG_ERROR, status.getMessage());
					}
				}
			}
		} 
		catch (IOException e) 
		{
			status = new Status(IStatus.CANCEL, Activator.getBundleContext().getBundle().getSymbolicName(), "File + " + file.getAbsolutePath() + " could not be opened.");
			logService.log(LogService.LOG_ERROR, status.getMessage());
		}
		return status;
	}

	private Map<String, String> getValidTopLevelFolders()
	{
		Map<String, String> validFolders = new HashMap<String, String>();
		Object properties = componentContext.getProperties().get(PROPERTY_KEY_TOP_LEVEL_FOLDERS);
		if (properties instanceof String)
		{
			String folder = (String) properties;
			validFolders.put(folder, folder);
		}
		if (properties instanceof String[])
		{
			String[] folders = (String[]) properties;
			for (String folder : folders)
			{
				validFolders.put(folder, folder);
			}
		}
		return validFolders;
	}
}
