package ch.kostceco.tools.siardval.checksum.impl.internal.service;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.Security;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.osgi.service.component.ComponentContext;

import ch.kostceco.tools.siardval.checksum.api.service.ChecksumValidator;

public class ChecksumValidatorComponent implements ChecksumValidator
{
	protected void activate(ComponentContext componentContext)
	{
	}
	
	protected void deactivate(ComponentContext componentContext)
	{
	}
	
	@Override
	public String getDigest(InputStream in, String algorithm) 
	{
		StringBuilder sb = new StringBuilder();
		try
		{
			MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
			messageDigest.reset();
			byte[] b = new byte[in.available()];
			while (in.available() > 0)
			{
				int len = in.read(b);
				messageDigest.update(b, 0, len);
			}
			byte[] digest = messageDigest.digest();
			for (int i = 0; i < digest.length; i++)
			{
				sb.append(Integer.toString((digest[i] & 0xff) + 0x100, 16).substring(1));
			}
		} 
		catch (NoSuchAlgorithmException e)
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		return sb.toString();
	}

	@Override
	public String[] getAvailableAlgorithms() 
	{
		String serviceType = "MessageDigest";
	    Set<String> result = new HashSet<String>();
	    Provider[] providers = Security.getProviders();
	    for (int i = 0; i < providers.length; i++) 
	    {
	        Set<Object> keys = providers[i].keySet();
	        for (Iterator<Object> it = keys.iterator(); it.hasNext(); ) 
	        {
	            String key = (String)it.next();
	            key = key.split(" ")[0];

	            if (key.startsWith(serviceType+".")) 
	            {
	                result.add(key.substring(serviceType.length()+1));
	            } 
	            else if (key.startsWith("Alg.Alias."+serviceType+".")) 
	            {
	                // This is an alias
	                result.add(key.substring(serviceType.length()+11));
	            }
	        }
	    }
	    return result.toArray(new String[result.size()]);
	}
}
