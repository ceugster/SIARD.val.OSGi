package ch.kostceco.tools.siardval.zip.api.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.eclipse.core.runtime.IStatus;

public interface ZipService
{
	/**
	 * Returns the entry specified by path of the designated file as an input stream.
	 * Throws an IOException if the file could not be opened.
	 * 
	 * The calling client is responsible to close the input stream.
	 * 
	 * @param file
	 * @param path
	 * @return
	 * @throws IOException
	 */
	InputStream getEntryAsStream(File file, String path) throws IOException;

	/**
	 * Returns an URL to the entry specified by path of the designated file.
	 * 
	 * @param file
	 * @param path
	 * @return
	 * @throws IOException
	 */
	URL getEntryAsUrl(File file, String path) throws IOException;
	
	/**
	 * Check compression of the file.
	 * 
	 * @param file
	 * @return
	 */
	IStatus checkCompression(File file);

	/**
	 * Check integrity of the file. Performs a deep check opening and reading each entry of the file.
	 * 
	 * @param file
	 * @return
	 */
	IStatus checkIntegrity(File file);

	/**
	 * Check integrity of the file, depending on the method specified.
	 * 
	 * @param file
	 * @param method
	 * @return
	 */
	IStatus checkIntegrity(File file, Method method);

	/**
	 * Check encryption of the specified file.
	 * 
	 * @param file
	 * @return
	 */
	IStatus checkEncryption(File file);

	/**
	 * Returns the entry specified by path as a file.
	 * 
	 * @param file
	 * @param path
	 * @return
	 * @throws IOException
	 */
	File getEntryAsFile(File file, String path) throws IOException;

	/**
	 * Check the file extension. Valid extensions can be retrieved from the component context properties.
	 * Property name is "siard.extensions".
	 * 
	 * @param file
	 * @return
	 */
	IStatus checkExtension(File file);
	
	/**
	 * Check top level structure of siard file.
	 * 
	 * @param file
	 * @return
	 */
	IStatus checkTopLevelFolders(File file);
	
	/**
	 * Integrity check methods.
	 * 
	 * There are two methods available:
	 * 
	 * Method.CHECK_FILE just tries to open the file.
	 * Method.CHECK_ENTRIES opens and reads each entry of the file to the end to ensure that the file is not corrupted.
	 * 
	 * @author ceugster
	 *
	 */
	public enum Method
	{
		CHECK_FILE, CHECK_ENTRIES;
	}

}
