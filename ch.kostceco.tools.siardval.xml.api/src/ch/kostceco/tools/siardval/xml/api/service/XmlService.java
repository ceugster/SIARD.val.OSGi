package ch.kostceco.tools.siardval.xml.api.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.eclipse.core.runtime.IStatus;
import org.xml.sax.SAXException;


public interface XmlService
{
	/**
	 * Validates the given input stream against the given schema in schema location.
	 * 
	 * @param in
	 * @param schemaLocation
	 * @return
	 */
	IStatus validate(InputStream in, File schemaLocation);

	/**
	 * Validates the given input stream against the given schema in schema location.
	 * 
	 * @param in
	 * @param schemaLocation
	 * @return
	 */
	IStatus validate(InputStream in, URL schemaLocation);

	/**
	 * Validates the given URL containing an xsd file against xml schema.
	 * 
	 * @param in
	 * @param schemaLocation
	 * @return
	 */
	IStatus validateSchema(URL schemaLocation);

	/**
	 * Validates the given file pointing to an xsd file against xml schema.
	 * 
	 * @param in
	 * @param schemaLocation
	 * @return
	 */
	IStatus validateSchema(File schemaLocation);

	/**
	 * Returns the siard version of the provided input stream. Throws an IOException if the file cannot be opened.
	 * 
	 * @param in
	 * @return
	 * @throws IOException
	 */
	String getSiardVersion(File file) throws IOException;

	/**
	 * Returns the content tree of metadata.xml stream. Root tree is level content
	 * 
	 * @param file
	 * @return
	 */
	Tree extractContentTree(InputStream in) throws IOException, SAXException;

}
