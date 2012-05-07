package ch.kostceco.tools.siardval.directory.api.service;

import java.io.File;

import org.eclipse.core.runtime.IStatus;

public interface DirectoryStructureValidator
{
	/**
	 * Check the top level structure of the file.
	 * 
	 * @param file
	 * @return
	 */
	IStatus checkPrimaryStructure(File file);
}
