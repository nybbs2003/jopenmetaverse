package com.ngt.jopenmetaverse.shared.util;

import java.security.NoSuchAlgorithmException;

import org.junit.Assert;
import org.junit.Test;

public class UtilsTest {

	@Test
	public void MD5PasswordTest() {
		try 
		{
			Assert.assertTrue(Utils.MD5("Hello World!").equals("ed076287532e86365e841e92bfc50d8c"));
			Assert.assertTrue(Utils.MD5("").equals("d41d8cd98f00b204e9800998ecf8427e"));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

}
