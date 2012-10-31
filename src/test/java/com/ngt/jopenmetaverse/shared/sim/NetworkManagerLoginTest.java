/**
 * A library to interact with Virtual Worlds such as OpenSim
 * Copyright (C) 2012  Jitendra Chauhan, Email: jitendra.chauhan@gmail.com
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License,
 * or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation,
 * Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
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