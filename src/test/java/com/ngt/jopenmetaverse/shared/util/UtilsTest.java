package com.ngt.jopenmetaverse.shared.util;

import java.io.UnsupportedEncodingException;
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
		System.out.println(" HEX: " + Utils.bytesToHexDebugString(Utils.uintToBytes(Utils.bytesToUInt(a2bytes)), ""));
		Assert.assertEquals(a2, Utils.bytesToUInt(a2bytes));
	}
	
	
	@Test 
	public void ubyteToIntTests()
	{
		byte a1 = -5;
		int i1 = Utils.ubyteToInt(a1);
		System.out.println(Utils.bytesToHexDebugString(new byte[]{a1}, ""));
		Assert.assertEquals(251, i1);
	}
	
	@Test
	public void longToUintTests()
	{
		long[] uints1 = new long[2];
		Utils.longToUInts(1099511628032000L, uints1);
		Assert.assertEquals(1099511628032000L >> 32, uints1[0]);
		Assert.assertEquals(1099511628032000L & 0x00000000ffffffffL, uints1[1]);
//		System.out.println(String.format("%d --> %d --> %s --> %s", 1099511628032000L >> 32, 
//				1099511628032000L & 0x00000000ffffffffL, 
//				Utils.bytesToHexDebugString(Utils.int64ToBytes(uints1[0]), "")
//				,Utils.bytesToHexDebugString(Utils.int64ToBytes(uints1[1]), "")));
	}
	
	@Test
	public void stringtoBytesTests()
	{
		System.out.println(String.format("22.660995 get converted to \n%s", 
				Utils.bytesToHexDebugString(Utils.stringToBytes(Double.toString(22.660995)), "")));
		System.out.println(String.format("20.1538619995117 gets converted to\n%s", 
				Utils.bytesToHexDebugString(Utils.stringToBytes("20.1538619995117"), "")));
	}
	
	@Test
	public void stringToBytesWithTrailingNullByteTests()
	{
		System.out.println(String.format("22.660995 get converted to \n%s", 
				Utils.bytesToHexDebugString(Utils.stringToBytesWithTrailingNullByte(Double.toString(22.660995)), "")));
		System.out.println(String.format("20.1538619995117 gets converted to\n%s", 
				Utils.bytesToHexDebugString(Utils.stringToBytesWithTrailingNullByte("20.1538619995117"), "")));
		
		Assert.assertEquals(Utils.bytesToHexDebugString(Utils.stringToBytesWithTrailingNullByte(Double.toString(22.660995)), ""), 
				"32 32 2E 36 36 30 39 39 35 00");
	}
	
	
	@Test
	public void bytesToStringWithTrailingNullByteTests()
	{
		try {
			byte[] bytes1 = new byte[]{0x32, 0x32, 0x2E, 0x36, 0x36, 0x30, 0x39, 0x39, 0x35, 0x00};
			String string1 = "22.660995";
			Assert.assertEquals(Utils.bytesToString(bytes1), string1);			
			
		} catch (UnsupportedEncodingException e) {
			Assert.fail("Failed with exception" + Utils.getExceptionStackTraceAsString(e));
			e.printStackTrace();
		} 
	}	
	
}
