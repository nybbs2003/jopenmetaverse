package com.ngt.jopenmetaverse.shared.sim;

import java.security.MessageDigest;
import junit.framework.Assert;
import org.junit.Test;

import com.ngt.jopenmetaverse.shared.util.JLogger;

public class NetworkManagerLoginTest {
	
	@Test
	public void LoginTest()
	{
		try{			
		GridClient client = new GridClient();
		NetworkManager networkManager = client.network;
		networkManager.Login("jitendra", "chauhan81", "jchauhan", "Opera", "last", "1.0");
		sleep(120000);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			Assert.fail();
		}
		
	}
	
	public static String digest(String text) throws Exception
	{
	   MessageDigest md = MessageDigest.getInstance("MD5");
	   byte[] md5hash = new byte[32];
	   md.update(text.getBytes(), 0, text.length());
	   md5hash = md.digest();
	   return convertToHex(md5hash);
	}
	 
	private static String convertToHex(byte[] b) {
	   StringBuilder result = new StringBuilder(32);
	   for (int i = 0; i < b.length; i++) {
	      result.append(
	         Integer.toString( ( b[i] & 0xff ) + 0x100, 16).substring( 1 ));
	   }
	   return result.toString();
	}

	private static void sleep(int timeout)
	{
		try {
			Thread.sleep(timeout);
		} catch (InterruptedException e) {
//			e.printStackTrace();
			JLogger.info(e.getMessage());
		}
	}
	
}