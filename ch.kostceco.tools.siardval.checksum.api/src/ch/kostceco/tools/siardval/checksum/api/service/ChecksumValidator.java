package ch.kostceco.tools.siardval.checksum.api.service;

import java.io.InputStream;

public interface ChecksumValidator
{
	String getDigest(InputStream in, String algorithm);
	
	String[] getAvailableAlgorithms();
}
