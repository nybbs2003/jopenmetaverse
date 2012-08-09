package com.ngt.jopenmetaverse.shared.util;

import java.math.BigInteger;
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
	
	@Test
	public void uintToBytesTests()
	{
		long a1 = 763435512;
		byte[] a1bytes = Utils.uintToBytes(a1);
		Assert.assertEquals(new BigInteger(a1bytes).longValue(), a1);
		System.out.println(new BigInteger(a1bytes).longValue() + " HEX: " 
		+ Utils.bytesToHexDebugString(a1bytes, "") + " " + Utils.bytesToHexDebugString(new BigInteger("763435512").toByteArray(), ""));
		long a2 = 4162421037L;
		byte[] a2bytes = Utils.uintToBytes(a2);
		System.out.println(" HEX: " + Utils.bytesToHexDebugString(a2bytes, ""));
		Assert.assertEquals(a2 & 0x0000000000000000ffffffffffffffffL, new BigInteger(a2bytes).longValue());
	}

}
