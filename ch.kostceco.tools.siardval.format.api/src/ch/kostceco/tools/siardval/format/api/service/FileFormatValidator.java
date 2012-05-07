package ch.kostceco.tools.siardval.format.api.service;

import java.io.File;

import org.eclipse.core.runtime.IStatus;

public interface FileFormatValidator
{
	/**
	 * Check if file is a directory.
	 * 
	 * @param file
	 * @return
	 */
	IStatus checkDirectory(File file);

	/**
	 * Check if user may read the selected file.
	 * 
	 * @param file
	 * @return
	 */
	IStatus checkReadable(File file);

	/**
	 * Check integrity
	 * 
	 * @param file
	 * @return
	 */
	IStatus checkIntegrity(File file);
	
	/**
	 * Check compression (file must not be compressed(
	 * 
	 * @param file
	 * @return
	 */
	IStatus checkCompression(File file);

	/**
	 * Check file encryption (must not be encrypted)
	 * @param file
	 * @return
	 */
	IStatus checkEncryption(File file);
	
	/**
	 * Check file extension
	 * 
	 * @param file
	 * @return
	 */
	IStatus checkExtension(File file);
}
