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
package com.ngt.jopenmetaverse.shared.protocol;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import com.ngt.jopenmetaverse.shared.structureddata.OSD;
import com.ngt.jopenmetaverse.shared.structureddata.OSDArray;
import com.ngt.jopenmetaverse.shared.structureddata.OSDException;
import com.ngt.jopenmetaverse.shared.structureddata.OSDInteger;
import com.ngt.jopenmetaverse.shared.structureddata.OSDMap;
import com.ngt.jopenmetaverse.shared.structureddata.OSDParser;
import com.ngt.jopenmetaverse.shared.structureddata.OSDString;
import com.ngt.jopenmetaverse.shared.structureddata.OSDType;
import com.ngt.jopenmetaverse.shared.structureddata.llsd.NotationalLLSDOSDParser;
import com.ngt.jopenmetaverse.shared.types.Quaternion;
import com.ngt.jopenmetaverse.shared.types.UUID;
import com.ngt.jopenmetaverse.shared.types.Vector3;

/// </summary>
public class TypeTest
{
	@Test
	public void UUIDs()
	{
		// Creation
		UUID a = new UUID();
		byte[] bytes = a.GetBytes();
		for (int i = 0; i < 16; i++)
			Assert.assertTrue(bytes[i] == 0x00);

		// Comparison
		a = new UUID(new byte[] { 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0A,
				0x0B, 0x0C, 0x0D, 0x0E, 0x0F, (byte) 0xFF, (byte) 0xFF }, 0);
		UUID b = new UUID(new byte[] { 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0A,
				0x0B, 0x0C, 0x0D, 0x0E, 0x0F }, 0);

		Assert.assertTrue("UUID comparison operator failed, " + a.toString() + " should equal " + 
				b.toString(), a.equals(b));

		// From String
		a = new UUID(new byte[] { 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0A,
				0x0B, 0x0C, 0x0D, 0x0E, 0x0F }, 0);
		String zeroonetwo = "00010203-0405-0607-0809-0a0b0c0d0e0f";
		b = new UUID(zeroonetwo);

		Assert.assertTrue("UUID hyphenated String constructor failed, should have " + a.toString() + 
				" but we got " + b.toString(), a.equals(b));

		// ToString()            
		Assert.assertTrue(a.equals(b));                        
		Assert.assertTrue(a.equals(UUID.Parse(zeroonetwo)));

		// TODO: CRC test
	}

	@Test
	public void Vector3ApproxEquals()
	{
		Vector3 a = new Vector3(1f, 0f, 0f);
		Vector3 b = new Vector3(0f, 0f, 0f);

		Assert.assertFalse("ApproxEquals failed (1)", a.approxEquals(b, 0.9f));
		Assert.assertTrue("ApproxEquals failed (2)", a.approxEquals(b, 1.0f));

		a = new Vector3(-1f, 0f, 0f);
		b = new Vector3(1f, 0f, 0f);

		Assert.assertFalse("ApproxEquals failed (3)", a.approxEquals(b, 1.9f));
		Assert.assertTrue("ApproxEquals failed (4)", a.approxEquals(b, 2.0f));

		a = new Vector3(0f, -1f, 0f);
		b = new Vector3(0f, -1.1f, 0f);

		Assert.assertFalse("ApproxEquals failed (5)", a.approxEquals(b, 0.09f));
		Assert.assertTrue("ApproxEquals failed (6)", a.approxEquals(b, 0.11f));

		a = new Vector3(0f, 0f, 0.00001f);
		b = new Vector3(0f, 0f, 0f);

		Assert.assertFalse("ApproxEquals failed (6)", b.approxEquals(a, Float.MIN_VALUE));
		Assert.assertTrue("ApproxEquals failed (7)", b.approxEquals(a, 0.0001f));
	}

	@Test
	public void Quaternions()
	{
		Quaternion a = new Quaternion(1, 0, 0, 0);
		Quaternion b = new Quaternion(1, 0, 0, 0);

		Assert.assertTrue("Quaternion comparison operator failed", a.equals(b));

		Quaternion expected = new Quaternion(0, 0, 0, -1);
		Quaternion result = Quaternion.multiply(a , b);

		Assert.assertTrue( a.toString() + " * " + b.toString() + " produced " + result.toString() +
				" instead of " + expected.toString(), result.equals(expected));

		a = new Quaternion(1, 0, 0, 0);
		b = new Quaternion(0, 1, 0, 0);
		expected = new Quaternion(0, 0, 1, 0);
		result = Quaternion.multiply(a , b);

		Assert.assertTrue(a.toString() + " * " + b.toString() + " produced " + result.toString() +
				" instead of " + expected.toString(), result.equals(expected));

		a = new Quaternion(0, 0, 1, 0);
		b = new Quaternion(0, 1, 0, 0);
		expected = new Quaternion(-1, 0, 0, 0);
		result = Quaternion.multiply(a , b);

		Assert.assertTrue(a.toString() + " * " + b.toString() + " produced " + result.toString() +
				" instead of " + expected.toString(), result.equals(expected));
	}

//	@Test
//	public void VectorQuaternionMath()
//	{
//	    // Convert a vector to a quaternion and back
//	    Vector3 a = new Vector3(1f, 0.5f, 0.75f);
//	    Quaternion b = a.ToQuaternion();
//	    Vector3 c;
//	    b.GetEulerAngles(out c.X, out c.Y, out c.Z);
//
//	    Assert.assertTrue(c.toString() + " does not equal " + a.toString(), a == c);
//	}

	@Test
	public void FloatsToTerseStrings()
	{
		float f = 1.20f;
		String a = "";
		String b = "1.2";

		a = Helpers.FloatToTerseString(f);
		Assert.assertTrue(f + " converted to " + a + ", expecting " + b, a.equals(b));

		f = 24.00f;
		b = "24";

		a = Helpers.FloatToTerseString(f);
		Assert.assertTrue(f + " converted to " + a + ", expecting " + b, a.equals(b));

		f = -0.59f;
		b = "-.59";

		a = Helpers.FloatToTerseString(f);
		Assert.assertTrue(f + " converted to " + a + ", expecting " + b, a.equals(b));

		f = 0.59f;
		b = ".59";

		a = Helpers.FloatToTerseString(f);
		Assert.assertTrue(f + " converted to " + a + ", expecting " + b, a.equals(b));
	}

	@Test
	public void BitUnpacking()
	{
		byte[] data = new byte[] { (byte)0x80, 0x00, 0x0F, 0x50, (byte)0x83, 0x7D };
		BitPack bitpacker = new BitPack(data, 0);

		int b = bitpacker.UnpackBits(1);
		Assert.assertTrue("Unpacked " + b + " instead of 1", b == 1);

		b = bitpacker.UnpackBits(1);
		Assert.assertTrue("Unpacked " + b + " instead of 0", b == 0);

		bitpacker = new BitPack(data, 2);

		b = bitpacker.UnpackBits(4);
		Assert.assertTrue("Unpacked " + b + " instead of 0", b == 0);

		b = bitpacker.UnpackBits(8);
		Assert.assertTrue("Unpacked " + b + " instead of 0xF5", b == 0xF5);

		b = bitpacker.UnpackBits(4);
		Assert.assertTrue("Unpacked " + b + " instead of 0", b == 0);

		b = bitpacker.UnpackBits(10);
		Assert.assertTrue("Unpacked " + b + " instead of 0x0183", b == 0x0183);
	}

	@Test
	public void BitPacking()
	{
		byte[] packedBytes = new byte[12];
		BitPack bitpacker = new BitPack(packedBytes, 0);

		bitpacker.PackBits(0x0ABBCCDD, 32);
		bitpacker.PackBits(25, 5);
		bitpacker.PackFloat(123.321f);
		bitpacker.PackBits(1000, 16);

		bitpacker = new BitPack(packedBytes, 0);

		int b = bitpacker.UnpackBits(32);
		Assert.assertTrue("Unpacked " + b + " instead of 2864434397", b == 0x0ABBCCDD);

		b = bitpacker.UnpackBits(5);
		Assert.assertTrue("Unpacked " + b + " instead of 25", b == 25);

		float f = bitpacker.UnpackFloat();
		Assert.assertTrue("Unpacked " + f + " instead of 123.321", f == 123.321f);

		b = bitpacker.UnpackBits(16);
		Assert.assertTrue("Unpacked " + b + " instead of 1000", b == 1000);

		packedBytes = new byte[1];
		bitpacker = new BitPack(packedBytes, 0);
		bitpacker.PackBit(true);

		bitpacker = new BitPack(packedBytes, 0);
		b = bitpacker.UnpackBits(1);
		Assert.assertTrue("Unpacked " + b + " instead of 1", b == 1);

		packedBytes = new byte[] { Byte.MAX_VALUE };
		bitpacker = new BitPack(packedBytes, 0);
		bitpacker.PackBit(false);

		bitpacker = new BitPack(packedBytes, 0);
		b = bitpacker.UnpackBits(1);
		Assert.assertTrue("Unpacked " + b + " instead of 0", b == 0);
	}

	//TODO Uncomment following test case
	@Test
	public void LLSDTerseParsing() throws OSDException, IOException
	{
		String testOne = "[r0.99967899999999998428,r-0.025334599999999998787,r0]";
		String testTwo = "[[r1,r1,r1],r0]";
		String testThree = "{'region_handle':[r255232, r256512], 'position':[r33.6, r33.71, r43.13], 'look_at':[r34.6, r33.71, r43.13]}";

		OSD obj = NotationalLLSDOSDParser.DeserializeLLSDNotation(testOne);
		
		Assert.assertTrue("Expected SDArray, got " + obj.getType().toString(), obj instanceof OSDArray);
		OSDArray array = (OSDArray)obj;
		Assert.assertTrue("Expected three contained objects, got " + array.count(), array.count() == 3);
		Assert.assertTrue("Unexpected value for first real " + array.get(0).asReal(), array.get(0).asReal() > 0.9d && array.get(0).asReal() < 1.0d);
		Assert.assertTrue("Unexpected value for second real " + array.get(1).asReal(), array.get(1).asReal() < 0.0d && array.get(1).asReal() > -0.03d);
		Assert.assertTrue("Unexpected value for third real " + array.get(2).asReal(), array.get(2).asReal() == 0.0d);

		obj = NotationalLLSDOSDParser.DeserializeLLSDNotation(testTwo);
		Assert.assertTrue("Expected SDArray, got " + obj.getType().toString(), obj instanceof OSDArray);
		array = (OSDArray)obj;
		Assert.assertTrue("Expected two contained objects, got " + array.count(), array.count() == 2);
		Assert.assertTrue("Unexpected value for real " + array.get(1).asReal(), array.get(1).asReal() == 0.0d);
		obj = array.get(0);
		Assert.assertTrue("Expected ArrayList, got " + obj.getType().toString(), obj instanceof OSDArray);
		array = (OSDArray)obj;
		Assert.assertTrue("Unexpected value(s) for nested array: " + array.get(0).asReal() + ", " + array.get(1).asReal() + ", " +
				array.get(2).asReal(), array.get(0).asReal() == 1.0d && array.get(1).asReal() == 1.0d && array.get(2).asReal() == 1.0d);

		obj = NotationalLLSDOSDParser.DeserializeLLSDNotation(testThree);
		Assert.assertTrue("Expected LLSDMap, got " + obj.getType().toString(), obj instanceof OSDMap);
		OSDMap hashtable = (OSDMap)obj;
		Assert.assertTrue("Expected three contained objects, got " + hashtable.count(), hashtable.count() == 3);
		Assert.assertTrue(hashtable.get("region_handle") instanceof OSDArray);
		Assert.assertTrue(((OSDArray)hashtable.get("region_handle")).count() == 2);
		Assert.assertTrue(hashtable.get("position") instanceof OSDArray);
		Assert.assertTrue(((OSDArray)hashtable.get("position")).count() == 3);
		Assert.assertTrue(hashtable.get("look_at") instanceof OSDArray);
		Assert.assertTrue(((OSDArray)hashtable.get("look_at")).count() == 3);
	}
}
