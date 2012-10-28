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
package com.ngt.jopenmetaverse.shared.structureddata.llsd;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.Map;
import org.junit.Assert;
import org.junit.Test;
import com.ngt.jopenmetaverse.shared.structureddata.OSD;
import com.ngt.jopenmetaverse.shared.structureddata.OSDArray;
import com.ngt.jopenmetaverse.shared.structureddata.OSDBinary;
import com.ngt.jopenmetaverse.shared.structureddata.OSDBoolean;
import com.ngt.jopenmetaverse.shared.structureddata.OSDDate;
import com.ngt.jopenmetaverse.shared.structureddata.OSDInteger;
import com.ngt.jopenmetaverse.shared.structureddata.OSDMap;
import com.ngt.jopenmetaverse.shared.structureddata.OSDReal;
import com.ngt.jopenmetaverse.shared.structureddata.OSDString;
import com.ngt.jopenmetaverse.shared.structureddata.OSDType;
import com.ngt.jopenmetaverse.shared.structureddata.OSDUUID;
import com.ngt.jopenmetaverse.shared.structureddata.OSDUri;
import com.ngt.jopenmetaverse.shared.types.UUID;
import com.ngt.jopenmetaverse.shared.util.Utils;

/// </summary>
public class JsonLLSDOSDParserTest
{
	
	private void testSerialization(String testSD) throws Exception
	{
		byte[] bytes = Utils.stringToBytes(testSD);
		OSD sourceSD =JsonLLSDOSDParser.DeserializeLLSDJson(bytes);
		
		String targetSSD = JsonLLSDOSDParser.SerializeLLSDJsonString(sourceSD);
		System.out.println(targetSSD);
		bytes = Utils.stringToBytes(targetSSD);
		OSD targetSD =JsonLLSDOSDParser.DeserializeLLSDJson(targetSSD);
		TestHelper.compareOSD(sourceSD, targetSD);
	}
	
	@Test
	public void	SeserializeLLSDSample() throws Exception
	{
		String testSD = "[ \n" + 
				"{ \n" + 
				"\"region_id\": \"67153d5b-3659-afb4-8510-adda2c034649\", \n" + 
				"\"scale\": \"one minute\", \n" + 
				"\"simulator statistics\": { \n" + 
				"\"time dilation\": 0.9878624, \n" + 
				"\"sim fps\": 44.38898, \n" + 
//				"\"agent updates per second\": NaN, \n" + 
				"\"total task count\": 4.0, \n" + 
				"\"active task count\": 0.0, \n" + 
				"\"pending uploads\": 0.0001096525 \n" + 
				"}\n" + 
				"}\n" + 
				"]";
		
		testSerialization(testSD);
	}
	
	@Test
	public void SeserializeLLSDReals() throws Exception
	{
		String testSD ="[ \n" +
				"[ \n" +
				"44.38898, \n" +
//				"<real>nan</real> \n" +
				"4.0, \n" +
				"-13.333, \n" +
				"0.0 \n" +
				"], \n" +
				"]";
		testSerialization(testSD);
	}
	
	@Test
	public void SeserializeLLSDStrings() throws Exception
	{
		String testSD = "[ \n" +
				"[ \n" +
				"\"Kissling\", \n" +
				"\"Attack ships on fire off the shoulder of Orion\", \n" +
				"\"&lt; &gt; &amp; &apos; &quot;\", \n" +
				"\"\", \n" +
				"\"    \" \n" +
				"] \n" +
				"]";
		testSerialization(testSD);
	}
	
	@Test
	public void SeserializeLLSDIntegers() throws Exception
	{
		String testSD ="[ \n" +
				"[ \n" +
				"2147483647, \n" +
				"-2147483648, \n" +
				"0, \n" +
				"013, \n" +
				"0 \n" +
				"] \n" +
				"]";
		testSerialization(testSD);
	}
	
	@Test
	public void SeserializeLLSDUUID() throws Exception
	{

		String testSD = "[ \n" +
				"[ \n" +
				"\"d7f4aeca-88f1-42a1-b385-b9db18abb255\",\n" +
				"\"00000000-0000-0000-0000-000000000000\" \n" +
				"] \n" +
				"]";
		testSerialization(testSD);
	}
	
	@Test
	public void SeserializeLLSDDates() throws Exception
	{

		String testSD = "[ \n" +
				"[ \n" +
				"\"2006-02-01T14:29:53Z\", \n" +
				"\"1999-01-01T00:00:00Z\" \n" +
				"] \n" +
				"]";
		testSerialization(testSD);
	}
	
	@Test
	public void SeserializeLLSDBoolean() throws Exception
	{
		String testSD = "[ \n" +
				"[ \n" +
				"true, \n" +
				"false \n" +
				"] \n" +
				"]";	
		testSerialization(testSD);
	}	
	@Test
	public void SeserializeLLSDBinary() throws Exception
	{
		String str1 = "This is test String 1";
		byte[] bytes1 = Utils.stringToBytes(str1);
		String str2 = "This is test String 2";
		byte[] bytes2 = Utils.stringToBytes(str2);


		String testSD = "[ \n" +
				"[ \n" +
				bytesToJsonArrayString(bytes1) + ", \n" +
				bytesToJsonArrayString(bytes2) + "\n" +
				"] \n" +
				"]";
		testSerialization(testSD);
	}

	@Test
	public void SeserializeLLSDUndef() throws Exception
	{
		String testSD = "[ \n" +
				"null \n" +
				"]";

		testSerialization(testSD);
	}
	@Test
	public void SeserializeLLSDURI() throws Exception
	{
		String testSD = "[ \n" +
				"[ \n" +
				"\"http://sim956.agni.lindenlab.com:12035/runtime/agents\" \n" +
				//							"\"\" \n" +
				"] \n" +
				"]";

		testSerialization(testSD);
	}
	@Test
	public void SeserializeLLSDNestedContainers() throws Exception
	{
		String testSD =	"[ \n" +
				"[ \n" +
				"{ \n" +
				"\"Map One\": \n" +
				"{ \n" +
				"\"Array One\": \n" +
				"[ \n" +
				"1.0, \n" +
				"2.0 \n" +
				"] \n" +
				"}\n" +
				"},\n" +
				"[ \n" +
				"\"A\", \n" +
				"\"B\", \n" +
				"[ \n" +
				"1.0, \n" +
				"4.0, \n" +
				"9.0 \n" +
				"] \n" +
				"] \n" +
				"] \n" +
				"]";
		testSerialization(testSD);
	}
	
	
	@Test
	public void DeserializeLLSDSample()
	{
		OSD theSD = null;
		OSDMap map = null;
		OSD tempSD = null;
		OSDUUID tempUUID = null;
		OSDString tempStr = null;
		OSDReal tempReal = null;

		String testSD = "[ \n" + 
				"{ \n" + 
				"\"region_id\": \"67153d5b-3659-afb4-8510-adda2c034649\", \n" + 
				"\"scale\": \"one minute\", \n" + 
				"\"simulator statistics\": { \n" + 
				"\"time dilation\": 0.9878624, \n" + 
				"\"sim fps\": 44.38898, \n" + 
//				"\"agent updates per second\": NaN, \n" + 
				"\"total task count\": 4.0, \n" + 
				"\"active task count\": 0.0, \n" + 
				"\"pending uploads\": 0.0001096525 \n" + 
				"}\n" + 
				"}\n" + 
				"]";

		//Deserialize the string
		System.out.println(testSD);
		byte[] bytes = Utils.stringToBytes(testSD);
		theSD =JsonLLSDOSDParser.DeserializeLLSDJson(bytes);

		//Confirm the contents
		Assert.assertNotNull(theSD);
		Assert.assertTrue(theSD instanceof OSDMap);
		System.out.println(theSD.getType().toString());
		Assert.assertTrue(theSD.getType() == OSDType.Map);
		map = (OSDMap)theSD;

		tempSD = map.get("region_id");
		Assert.assertNotNull(tempSD);
		Assert.assertTrue(tempSD instanceof OSDUUID);
		Assert.assertTrue(tempSD.getType() == OSDType.UUID);
		tempUUID = (OSDUUID)tempSD;
		Assert.assertEquals(new UUID("67153d5b-3659-afb4-8510-adda2c034649"), tempUUID.asUUID());

		tempSD = map.get("scale");
		Assert.assertNotNull(tempSD);
		Assert.assertTrue(tempSD instanceof OSDString);
		Assert.assertTrue(tempSD.getType() == OSDType.String);
		tempStr = (OSDString)tempSD;
		Assert.assertEquals("one minute", tempStr.asString());

		tempSD = map.get("simulator statistics");
		Assert.assertNotNull(tempSD);
		Assert.assertTrue(tempSD instanceof OSDMap);
		Assert.assertTrue(tempSD.getType() == OSDType.Map);
		map = (OSDMap)tempSD;

		tempSD = map.get("time dilation");
		Assert.assertNotNull(tempSD);
		Assert.assertTrue(tempSD instanceof OSDReal);
		Assert.assertTrue(tempSD.getType() == OSDType.Real);
		tempReal = (OSDReal)tempSD;

		Assert.assertEquals(0.9878624d, tempReal.asReal(), 0);
		//TODO - figure out any relevant rounding variability for 64 bit reals
		tempSD = map.get("sim fps");
		Assert.assertNotNull(tempSD);
		Assert.assertTrue(tempSD instanceof OSDReal);
		Assert.assertTrue(tempSD.getType() == OSDType.Real);
		tempReal = (OSDReal)tempSD;
		Assert.assertEquals(44.38898d, tempReal.asReal(), 0);

//		tempSD = map.get("agent updates per second");
//		Assert.assertNotNull(tempSD);
//		Assert.assertTrue(tempSD instanceof OSDReal);
//		Assert.assertTrue(tempSD.getType() == OSDType.Real);
//		tempReal = (OSDReal)tempSD;
//		Assert.assertEquals(Double.NaN, tempSD.asReal(), 0);

		tempSD = map.get("total task count");
		Assert.assertNotNull(tempSD);
		Assert.assertTrue(tempSD instanceof OSDReal);
		Assert.assertTrue(tempSD.getType() == OSDType.Real);
		tempReal = (OSDReal)tempSD;
		Assert.assertEquals(4.0d, tempReal.asReal(), 0);

		tempSD = map.get("active task count");
		Assert.assertNotNull(tempSD);
		Assert.assertTrue(tempSD instanceof OSDReal);
		Assert.assertTrue(tempSD.getType() == OSDType.Real);
		tempReal = (OSDReal)tempSD;
		Assert.assertEquals(0.0d, tempReal.asReal(), 0);

		tempSD = map.get("pending uploads");
		Assert.assertNotNull(tempSD);
		Assert.assertTrue(tempSD instanceof OSDReal);
		Assert.assertTrue(tempSD.getType() == OSDType.Real);
		tempReal = (OSDReal)tempSD;
		Assert.assertEquals(0.0001096525d, tempReal.asReal(), 0);

	}

	/// <summary>
	/// Test that various Real representations are parsed correctly.
	/// </summary>
	@Test
	public void DeserializeReals()
	{
		OSD theSD = null;
		OSDArray array = null;
		OSDReal tempReal = null;

		String testSD ="[ \n" +
				"[ \n" +
				"44.38898, \n" +
//				"<real>nan</real> \n" +
				"4.0, \n" +
				"-13.333, \n" +
				"0.0 \n" +
				"], \n" +
				"]";
		//Deserialize the string
		byte[] bytes = Utils.stringToBytes(testSD);
		theSD =JsonLLSDOSDParser.DeserializeLLSDJson(bytes);

		Assert.assertTrue(theSD instanceof OSDArray);
		array = (OSDArray)theSD;

		Assert.assertEquals(OSDType.Real, array.get(0).getType());
		tempReal = (OSDReal)array.get(0);
		Assert.assertEquals(44.38898d, tempReal.asReal(), 0);

//		Assert.assertEquals(OSDType.Real, array.get(1).getType());
//		tempReal = (OSDReal)array.get(1);
//		Assert.assertEquals(Double.NaN, tempReal.asReal(), 0);

		Assert.assertEquals(OSDType.Real, array.get(1).getType());
		tempReal = (OSDReal)array.get(1);
		Assert.assertEquals(4.0d, tempReal.asReal(), 0);

		Assert.assertEquals(OSDType.Real, array.get(2).getType());
		tempReal = (OSDReal)array.get(2);
		Assert.assertEquals(-13.333d, tempReal.asReal(), 0);

		Assert.assertEquals(OSDType.Real, array.get(3).getType());
		tempReal = (OSDReal)array.get(3);
		Assert.assertEquals(0d, tempReal.asReal(), 0);
	}

	/// <summary>
	/// Test that various String representations are parsed correctly.
	/// </summary>
	@Test
	public void DeserializeStrings()
	{
		OSD theSD = null;
		OSDArray array = null;
		OSDString tempStr = null;

		String testSD = "[ \n" +
				"[ \n" +
				"\"Kissling\", \n" +
				"\"Attack ships on fire off the shoulder of Orion\", \n" +
				"\"&lt; &gt; &amp; &apos; &quot;\", \n" +
				"\"\", \n" +
				"\"    \" \n" +
				"] \n" +
				"]";
		//Deserialize the string
		byte[] bytes = Utils.stringToBytes(testSD);
		theSD =JsonLLSDOSDParser.DeserializeLLSDJson(bytes);

		Assert.assertTrue(theSD instanceof OSDArray);
		array = (OSDArray)theSD;

		Assert.assertEquals(OSDType.String, array.get(0).getType());
		tempStr = (OSDString)array.get(0);
		Assert.assertEquals("Kissling", tempStr.asString());

		Assert.assertEquals(OSDType.String, array.get(1).getType());
		tempStr = (OSDString)array.get(1);
		Assert.assertEquals("Attack ships on fire off the shoulder of Orion", tempStr.asString());

		Assert.assertEquals(OSDType.String, array.get(2).getType());
		tempStr = (OSDString)array.get(2);
		Assert.assertEquals("&lt; &gt; &amp; &apos; &quot;", tempStr.asString());

		Assert.assertEquals(OSDType.String, array.get(3).getType());
		tempStr = (OSDString)array.get(3);
		Assert.assertEquals("", tempStr.asString());

		Assert.assertEquals(OSDType.String, array.get(4).getType());
		tempStr = (OSDString)array.get(4);
		Assert.assertEquals("    ", tempStr.asString());
		
	}

	/// <summary>
	/// Test that various Integer representations are parsed correctly.
	/// These tests currently only test for values within the range of a
	/// 32 bit signed integer, even though the SD specification says
	/// the type instanceof a 64 bit signed integer, because LLSInteger instanceof currently
	/// implemented using int, a.k.a. Int32.  Not testing Int64 range until
	/// it's understood if there was a design reason for the Int32.
	/// </summary>
	@Test
	public void DeserializeIntegers()
	{
		OSD theSD = null;
		OSDArray array = null;
		OSDInteger tempInt = null;

		String testSD ="[ \n" +
				"[ \n" +
				"2147483647, \n" +
				"-2147483648, \n" +
				"0, \n" +
				"013, \n" +
				"0 \n" +
				"] \n" +
				"]";
		//Deserialize the string
		byte[] bytes = Utils.stringToBytes(testSD);
		theSD =JsonLLSDOSDParser.DeserializeLLSDJson(bytes);

		System.out.println("=========" + theSD.getType().toString());
		
		Assert.assertTrue(theSD instanceof OSDArray);
		array = (OSDArray)theSD;

		Assert.assertEquals(OSDType.Integer, array.get(0).getType());
		tempInt = (OSDInteger)array.get(0);
		Assert.assertEquals(2147483647, tempInt.asInteger());

		Assert.assertEquals(OSDType.Integer, array.get(1).getType());
		tempInt = (OSDInteger)array.get(1);
		Assert.assertEquals(-2147483648, tempInt.asInteger());

		Assert.assertEquals(OSDType.Integer, array.get(2).getType());
		tempInt = (OSDInteger)array.get(2);
		Assert.assertEquals(0, tempInt.asInteger());

		Assert.assertEquals(OSDType.Integer, array.get(3).getType());
		tempInt = (OSDInteger)array.get(3);
		Assert.assertEquals(13, tempInt.asInteger());

		Assert.assertEquals(OSDType.Integer, array.get(4).getType());
		tempInt = (OSDInteger)array.get(4);
		Assert.assertEquals(0, tempInt.asInteger());
	}

	/// <summary>
	/// Test that various UUID representations are parsed correctly.
	/// </summary>
	@Test
	public void DeserializeUUID()
	{
		OSD theSD = null;
		OSDArray array = null;
		OSDUUID tempUUID = null;

		String testSD = "[ \n" +
				"[ \n" +
				"\"d7f4aeca-88f1-42a1-b385-b9db18abb255\",\n" +
				"\"00000000-0000-0000-0000-000000000000\" \n" +
				"] \n" +
				"]";
		//Deserialize the string
		byte[] bytes = Utils.stringToBytes(testSD);
		theSD =JsonLLSDOSDParser.DeserializeLLSDJson(bytes);

		Assert.assertTrue(theSD instanceof OSDArray);
		array = (OSDArray)theSD;

		Assert.assertEquals(OSDType.UUID, array.get(0).getType());
		tempUUID = (OSDUUID)array.get(0);
		Assert.assertEquals(new UUID("d7f4aeca-88f1-42a1-b385-b9db18abb255"), tempUUID.asUUID());

		Assert.assertEquals(OSDType.UUID, array.get(1).getType());
		tempUUID = (OSDUUID)array.get(1);
		Assert.assertEquals(UUID.Zero, tempUUID.asUUID());
	}

	/// <summary>
	/// Test that various date representations are parsed correctly.
	/// </summary>
	@Test
	public void DeserializeDates()
	{
		OSD theSD = null;
		OSDArray array = null;
		OSDDate tempDate = null;
		Date[] testDate = new Date[1];

		String testSD = "[ \n" +
				"[ \n" +
				"\"2006-02-01T14:29:53Z\", \n" +
				"\"1999-01-01T00:00:00Z\" \n" +
				"] \n" +
				"]";
		//Deserialize the string
		byte[] bytes = Utils.stringToBytes(testSD);
		theSD =JsonLLSDOSDParser.DeserializeLLSDJson(bytes);

		Assert.assertTrue(theSD instanceof OSDArray);
		array = (OSDArray)theSD;

		Assert.assertEquals(OSDType.Date, array.get(0).getType());
		tempDate = (OSDDate)array.get(0);
		Utils.tryParseDate("2006-02-01T14:29:53Z", testDate);
		Assert.assertEquals(testDate[0], tempDate.asDate());

		Assert.assertEquals(OSDType.Date, array.get(1).getType());
		tempDate = (OSDDate)array.get(1);
		Utils.tryParseDate("1999-01-01T00:00:00Z", testDate);
		Assert.assertEquals(testDate[0], tempDate.asDate());

//		Assert.assertEquals(OSDType.Date, array.get(2).getType());
//		tempDate = (OSDDate)array.get(2);
//		Assert.assertEquals(Utils.Epoch, tempDate.asDate());
	}

	/// <summary>
	/// Test that various Boolean representations are parsed correctly.
	/// </summary>
	@Test
	public void DeserializeBoolean()
	{
		OSD theSD = null;
		OSDArray array = null;
		OSDBoolean tempBool = null;

		String testSD = "[ \n" +
				"[ \n" +
				"true, \n" +
				"false \n" +
				"] \n" +
				"]";
		//Deserialize the string
		byte[] bytes = Utils.stringToBytes(testSD);
		theSD =JsonLLSDOSDParser.DeserializeLLSDJson(bytes);

		Assert.assertTrue(theSD instanceof OSDArray);
		array = (OSDArray)theSD;

		Assert.assertEquals(OSDType.Boolean, array.get(0).getType());
		tempBool = (OSDBoolean)array.get(0);
		Assert.assertEquals(true, tempBool.asBoolean());

		Assert.assertEquals(OSDType.Boolean, array.get(1).getType());
		tempBool = (OSDBoolean)array.get(1);
		Assert.assertEquals(false, tempBool.asBoolean());
	}

	public String bytesToJsonArrayString(byte[] bytes)
	{
		String str = "[";
		for(int i=0; i < bytes.length; i ++)
		{
			str += "" + Utils.ubyteToInt(bytes[i]);
			if(i < bytes.length - 1)
			str += ",";
		}
		str += "]";
		return str;
	}
					
	/// <summary>
	/// Test that binary elements are parsed correctly.
	/// </summary>
	@Test
	public void DeserializeBinary()
	{
		OSD theSD = null;
		OSDArray array = null;
		OSDBinary tempBinary = null;

		String str1 = "This is test String 1";
		byte[] bytes1 = Utils.stringToBytes(str1);
		String str2 = "This is test String 2";
		byte[] bytes2 = Utils.stringToBytes(str2);
		
		
		String testSD = "[ \n" +
				"[ \n" +
				bytesToJsonArrayString(bytes1) + ", \n" +
				bytesToJsonArrayString(bytes2) + "\n" +
				"] \n" +
				"]";

		System.out.println(testSD);
		
		//Deserialize the string
		byte[] bytes = Utils.stringToBytes(testSD);
		theSD =JsonLLSDOSDParser.DeserializeLLSDJson(bytes);

		Assert.assertTrue(theSD instanceof OSDArray);
		array = (OSDArray)theSD;

		Assert.assertEquals(OSDType.Binary, array.get(0).getType());
		tempBinary = (OSDBinary)array.get(0);
		byte[] testData1 = bytes1;
		TestHelper.TestBinary(tempBinary, testData1);

		Assert.assertEquals(OSDType.Binary, array.get(1).getType());
		tempBinary = (OSDBinary)array.get(1);
		byte[] testData2 = bytes2;
		TestHelper.TestBinary(tempBinary, testData2);
	}

	/// <summary>
	/// Test that undefened elements are parsed correctly.
	/// Currently this just checks that there instanceof no error since undefined has no
	/// value and there instanceof no SD child class for Undefined elements - the
	/// current implementation generates an instance of SD
	/// </summary>
	@Test
	public void DeserializeUndef()
	{
		OSD theSD = null;

		String testSD = "[ \n" +
				"null \n" +
				"]";
		//Deserialize the string
		byte[] bytes = Utils.stringToBytes(testSD);
		theSD =JsonLLSDOSDParser.DeserializeLLSDJson(bytes);

		Assert.assertTrue(theSD instanceof OSD);
	}

	/// <summary>
	/// Test that various URI representations are parsed correctly.
	/// </summary>
	@Test
	public void DeserializeURI() throws URISyntaxException
	{
		OSD theSD = null;
		OSDArray array = null;
		OSDUri tempURI = null;

		String testSD = "[ \n" +
				"[ \n" +
				"\"http://sim956.agni.lindenlab.com:12035/runtime/agents\" \n" +
//				"\"\" \n" +
				"] \n" +
				"]";
		//Deserialize the string
		byte[] bytes = Utils.stringToBytes(testSD);
		theSD =JsonLLSDOSDParser.DeserializeLLSDJson(bytes);

		Assert.assertTrue(theSD instanceof OSDArray);
		array = (OSDArray)theSD;

		Assert.assertEquals(OSDType.URI, array.get(0).getType());
		tempURI = (OSDUri)array.get(0);
		URI testURI = new URI("http://sim956.agni.lindenlab.com:12035/runtime/agents");
		Assert.assertEquals(testURI, tempURI.asUri());

//		Assert.assertEquals(OSDType.URI, array.get(1).getType());
//		tempURI = (OSDUri)array.get(1);
//		Assert.assertEquals("", tempURI.asUri().toString());
	}

	/// <summary>
	/// Test some nested containers.  This instanceof not a very deep or complicated SD graph
	/// but it should reveal basic nesting issues.
	/// </summary>
	@Test
	public void DeserializeNestedContainers()
	{
		OSD theSD = null;
		OSDArray array = null;
		OSDMap map = null;
		OSD tempSD = null;

		String testSD =	"[ \n" +
				"[ \n" +
					"{ \n" +
						"\"Map One\": \n" +
							"{ \n" +
								"\"Array One\": \n" +
									"[ \n" +
										"1.0, \n" +
										"2.0 \n" +
									"] \n" +
							"}\n" +
					"},\n" +
					"[ \n" +
						"\"A\", \n" +
						"\"B\", \n" +
						"[ \n" +
							"1.0, \n" +
							"4.0, \n" +
							"9.0 \n" +
						"] \n" +
					"] \n" +
					"] \n" +
				"]";
		//Deserialize the string
		byte[] bytes = Utils.stringToBytes(testSD);
		theSD =JsonLLSDOSDParser.DeserializeLLSDJson(bytes);

		Assert.assertTrue(theSD instanceof OSDArray);
		array = (OSDArray)theSD;
		Assert.assertEquals(2, array.count());

		//The first element of top level array, a map
		Assert.assertEquals(OSDType.Map, array.get(0).getType());
		map = (OSDMap)array.get(0);
		//First nested map
		tempSD = map.get("Map One");
		Assert.assertNotNull(tempSD);
		Assert.assertEquals(OSDType.Map, tempSD.getType());
		map = (OSDMap)tempSD;
		//First nested array
		tempSD = map.get("Array One");
		Assert.assertNotNull(tempSD);
		Assert.assertEquals(OSDType.Array, tempSD.getType());
		array = (OSDArray)tempSD;
		Assert.assertEquals(2, array.count());

		array = (OSDArray)theSD;
		//Second element of top level array, an array
		tempSD = array.get(1);
		Assert.assertEquals(OSDType.Array, tempSD.getType());
		array = (OSDArray)tempSD;
		Assert.assertEquals(3, array.count());
		//Nested array
		tempSD = array.get(2);
		Assert.assertEquals(OSDType.Array, tempSD.getType());
		array = (OSDArray)tempSD;
		Assert.assertEquals(3, array.count());
	}

	private static class TestHelper
	{
		/// <summary>
		/// Asserts that the contents of the SDBinary match the values and length
		/// of the supplied byte array
		/// </summary>
		/// <param name="inBinary"></param>
		/// <param name="inExpected"></param>
		public static void TestBinary(OSDBinary inBinary, byte[] inExpected)
		{
			byte[] binary = inBinary.asBinary();
			Assert.assertEquals(inExpected.length, binary.length);
			for (int i = 0; i < inExpected.length; i++)
			{
				if (inExpected[i] != binary[i])
				{
					Assert.fail("Expected " + Byte.toString(inExpected[i]) + " at position " + i +
							" but saw " + Byte.toString(binary[i]));
				}
			}
		}
		
		
	     public static void compareOSD(OSD source, OSD target)
	        {
	    	 	if(! source.getType().equals(target.getType()))
	    	 	{
	    	 		Assert.fail(source.getType().toString() + 
	    	 				" AND " + target.getType().toString() + " are not equal");
	    	 	}
	    	 	
	            switch (source.getType())
	            {
	                case Unknown:
	                	
	                    break;
	                case Boolean:
	                	Assert.assertEquals(((OSDBoolean)source).asBoolean(), ((OSDBoolean)target).asBoolean());  
	                    break;
	                case Integer:
	                	Assert.assertEquals(((OSDInteger)source).asInteger(), ((OSDInteger)target).asInteger());  
	                    
	                    break;
	                case Real:
	                	Assert.assertEquals(((OSDReal)source).asReal(), ((OSDReal)target).asReal(), 0);  

	                    break;
	                case String:
	                	Assert.assertEquals(((OSDString)source).asString(), ((OSDString)target).asString());  

	                    break;
	                case UUID:
	                	Assert.assertEquals(((OSDUUID)source).asUUID(), ((OSDUUID)target).asUUID());  
	                    
	                    break;
	                case Date:
	                	Assert.assertEquals(((OSDDate)source).asDate(), ((OSDDate)target).asDate());  

	                    break;
	                case URI:
	                	Assert.assertEquals(((OSDUri)source).asUri().toString(), ((OSDUri)target).asUri().toString());  
	                    
	                    break;
	                case Binary:
	                	Assert.assertArrayEquals(((OSDBinary)source).asBinary(), ((OSDBinary)target).asBinary());  
	                   
	                    
	                    break;
	                case Map:
	                    OSDMap smap = (OSDMap)source;
	                    OSDMap tmap = (OSDMap)target;
	                    	                    
	                    for(Map.Entry<String, OSD> entry: smap.entrySet())
	                    {
	                    	Assert.assertTrue(tmap.containsKey(entry.getKey()));
	                    	compareOSD(entry.getValue(), tmap.get(entry.getKey()));

	     	            }                    
	                    break;
	                case Array:
	                    OSDArray sarray = (OSDArray)source;
	                    OSDArray tarray = (OSDArray)target;
	                    	                    
	                    for (int i = 0; i < sarray.count(); i++)
	                    {
	                    	Assert.assertNotNull(tarray.get(i));
	                    	compareOSD(sarray.get(i), tarray.get(i));
	                    }
	                    break;
	            }
	        }
		
	}
}
