package ch.kostceco.tools.siardval.header.api.service;

import java.io.File;

import org.eclipse.core.runtime.IStatus;


public interface HeaderSectionValidator
{
	IStatus validateMetadata(File file);
	
	IStatus validateSchema(File file);
	
	IStatus validateSchemaAgainstExternalSchema(File file);
}
