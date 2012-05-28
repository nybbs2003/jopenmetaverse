package com.ngt.jopenmetaverse.shared.structureddata.llsd;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import com.ngt.jopenmetaverse.shared.structureddata.OSD;
import com.ngt.jopenmetaverse.shared.structureddata.OSDArray;
import com.ngt.jopenmetaverse.shared.structureddata.OSDException;
import com.ngt.jopenmetaverse.shared.structureddata.OSDInteger;
import com.ngt.jopenmetaverse.shared.structureddata.OSDMap;
import com.ngt.jopenmetaverse.shared.structureddata.OSDParser;
import com.ngt.jopenmetaverse.shared.structureddata.OSDString;
import com.ngt.jopenmetaverse.shared.structureddata.OSDType;
import com.ngt.jopenmetaverse.shared.types.UUID;
import com.ngt.jopenmetaverse.shared.util.Utils;

/// </summary>
public class NotationalLLSDOSDParserTest
{
	@Test
	        public void HelperFunctions() throws IOException
	        {
	            StringReader reader = new StringReader("test1tast2test3");

	            char[] charsOne = { 't', 'e', 's', 't' };
	            int resultOne = NotationalLLSDOSDParser.BufferCharactersEqual(reader, charsOne, 0);
	            Assert.assertEquals(charsOne.length, resultOne);

	            char[] charsTwo = { '1', 't', 'e' };
	            int resultTwo = NotationalLLSDOSDParser.BufferCharactersEqual(reader, charsTwo, 0);
	            Assert.assertEquals(2, resultTwo);

	            char[] charsThree = { 'a', 's', 't', '2', 't', 'e', 's' };
	            int resultThree = NotationalLLSDOSDParser.BufferCharactersEqual(reader, charsThree, 1);
	            Assert.assertEquals(1, resultThree);

	            int resultFour = NotationalLLSDOSDParser.BufferCharactersEqual(reader, charsThree, 0);
	            Assert.assertEquals(charsThree.length, resultFour);

	            char[] charsFive = { 't', '3', 'a', 'a' };
	            int resultFive = NotationalLLSDOSDParser.BufferCharactersEqual(reader, charsFive, 0);
	            Assert.assertEquals(2, resultFive);


	        }

	        @Test
	        public void DeserializeUndef() throws OSDException, IOException
	        {
	            String s = "!";
	            OSD llsd = NotationalLLSDOSDParser.DeserializeLLSDNotation(s);
	            Assert.assertEquals(OSDType.Unknown, llsd.getType());
	        }

	        @Test
	        public void SerializeUndef() throws IOException, OSDException
	        {
	            OSD llsd = new OSD();
	            String s = NotationalLLSDOSDParser.SerializeLLSDNotation(llsd);

	            OSD llsdDS = NotationalLLSDOSDParser.DeserializeLLSDNotation(s);
	            Assert.assertEquals(OSDType.Unknown, llsdDS.getType());
	        }

	        @Test
	        public void DeserializeBoolean() throws OSDException, IOException
	        {
	            String t = "true";
	            OSD llsdT = NotationalLLSDOSDParser.DeserializeLLSDNotation(t);
	            Assert.assertEquals(OSDType.Boolean, llsdT.getType());
	            Assert.assertTrue(llsdT.asBoolean());

	            String tTwo = "t";
	            OSD llsdTTwo = NotationalLLSDOSDParser.DeserializeLLSDNotation(tTwo);
	            Assert.assertEquals(OSDType.Boolean, llsdTTwo.getType());
	            Assert.assertTrue(llsdTTwo.asBoolean());

	            String tThree = "TRUE";
	            OSD llsdTThree = NotationalLLSDOSDParser.DeserializeLLSDNotation(tThree);
	            Assert.assertEquals(OSDType.Boolean, llsdTThree.getType());
	            Assert.assertTrue(llsdTThree.asBoolean());

	            String tFour = "T";
	            OSD llsdTFour = NotationalLLSDOSDParser.DeserializeLLSDNotation(tFour);
	            Assert.assertEquals(OSDType.Boolean, llsdTFour.getType());
	            Assert.assertTrue(llsdTFour.asBoolean());

	            String tFive = "1";
	            OSD llsdTFive = NotationalLLSDOSDParser.DeserializeLLSDNotation(tFive);
	            Assert.assertEquals(OSDType.Boolean, llsdTFive.getType());
	            Assert.assertTrue(llsdTFive.asBoolean());

	            String f = "false";
	            OSD llsdF = NotationalLLSDOSDParser.DeserializeLLSDNotation(f);
	            Assert.assertEquals(OSDType.Boolean, llsdF.getType());
	            Assert.assertFalse(llsdF.asBoolean());

	            String fTwo = "f";
	            OSD llsdFTwo = NotationalLLSDOSDParser.DeserializeLLSDNotation(fTwo);
	            Assert.assertEquals(OSDType.Boolean, llsdFTwo.getType());
	            Assert.assertFalse(llsdFTwo.asBoolean());

	            String fThree = "FALSE";
	            OSD llsdFThree = NotationalLLSDOSDParser.DeserializeLLSDNotation(fThree);
	            Assert.assertEquals(OSDType.Boolean, llsdFThree.getType());
	            Assert.assertFalse(llsdFThree.asBoolean());

	            String fFour = "F";
	            OSD llsdFFour = NotationalLLSDOSDParser.DeserializeLLSDNotation(fFour);
	            Assert.assertEquals(OSDType.Boolean, llsdFFour.getType());
	            Assert.assertFalse(llsdFFour.asBoolean());

	            String fFive = "0";
	            OSD llsdFFive = NotationalLLSDOSDParser.DeserializeLLSDNotation(fFive);
	            Assert.assertEquals(OSDType.Boolean, llsdFFive.getType());
	            Assert.assertFalse(llsdFFive.asBoolean());
	        }

	        @Test
	        public void SerializeBoolean() throws IOException, OSDException
	        {
	            OSD llsdTrue = OSD.FromBoolean(true);
	            String sTrue = NotationalLLSDOSDParser.SerializeLLSDNotation(llsdTrue);
	            OSD llsdTrueDS = NotationalLLSDOSDParser.DeserializeLLSDNotation(sTrue);
	            Assert.assertEquals(OSDType.Boolean, llsdTrueDS.getType());
	            Assert.assertTrue(llsdTrueDS.asBoolean());

	            OSD llsdFalse = OSD.FromBoolean(false);
	            String sFalse = NotationalLLSDOSDParser.SerializeLLSDNotation(llsdFalse);
	            OSD llsdFalseDS = NotationalLLSDOSDParser.DeserializeLLSDNotation(sFalse);
	            Assert.assertEquals(OSDType.Boolean, llsdFalseDS.getType());
	            Assert.assertFalse(llsdFalseDS.asBoolean());
	        }

	        @Test
	        public void DeserializeInteger() throws OSDException, IOException
	        {
	            String integerOne = "i12319423";
	            OSD llsdOne = NotationalLLSDOSDParser.DeserializeLLSDNotation(integerOne);
	            Assert.assertEquals(OSDType.Integer, llsdOne.getType());
	            Assert.assertEquals(12319423, llsdOne.asInteger());

	            String integerTwo = "i-489234";
	            OSD llsdTwo = NotationalLLSDOSDParser.DeserializeLLSDNotation(integerTwo);
	            Assert.assertEquals(OSDType.Integer, llsdTwo.getType());
	            Assert.assertEquals(-489234, llsdTwo.asInteger());
	        }

	        @Test
	        public void SerializeInteger() throws IOException, OSDException
	        {
	            OSD llsdOne = OSD.FromInteger(12319423);
	            String sOne = NotationalLLSDOSDParser.SerializeLLSDNotation(llsdOne);
	            OSD llsdOneDS = NotationalLLSDOSDParser.DeserializeLLSDNotation(sOne);
	            Assert.assertEquals(OSDType.Integer, llsdOneDS.getType());
	            Assert.assertEquals(12319423, llsdOne.asInteger());

	            OSD llsdTwo = OSD.FromInteger(-71892034);
	            String sTwo = NotationalLLSDOSDParser.SerializeLLSDNotation(llsdTwo);
	            OSD llsdTwoDS = NotationalLLSDOSDParser.DeserializeLLSDNotation(sTwo);
	            Assert.assertEquals(OSDType.Integer, llsdTwoDS.getType());
	            Assert.assertEquals(-71892034, llsdTwoDS.asInteger());
	        }

	        @Test
	        public void DeserializeReal() throws OSDException, IOException
	        {
	            String realOne = "r1123412345.465711";
	            OSD llsdOne = NotationalLLSDOSDParser.DeserializeLLSDNotation(realOne);
	            Assert.assertEquals(OSDType.Real, llsdOne.getType());
	            Assert.assertEquals(1123412345.465711d, llsdOne.asReal(), 0);

	            String realTwo = "r-11234684.923411";
	            OSD llsdTwo = NotationalLLSDOSDParser.DeserializeLLSDNotation(realTwo);
	            Assert.assertEquals(OSDType.Real, llsdTwo.getType());
	            Assert.assertEquals(-11234684.923411d, llsdTwo.asReal(), 0);

	            String realThree = "r1";
	            OSD llsdThree = NotationalLLSDOSDParser.DeserializeLLSDNotation(realThree);
	            Assert.assertEquals(OSDType.Real, llsdThree.getType());
	            Assert.assertEquals(1d, llsdThree.asReal(), 0);

	            String realFour = "r2.0193899999999998204e-06";
	            OSD llsdFour = NotationalLLSDOSDParser.DeserializeLLSDNotation(realFour);
	            Assert.assertEquals(OSDType.Real, llsdFour.getType());
	            Assert.assertEquals(2.0193899999999998204e-06d, llsdFour.asReal(), 0);

	            String realFive = "r0";
	            OSD llsdFive = NotationalLLSDOSDParser.DeserializeLLSDNotation(realFive);
	            Assert.assertEquals(OSDType.Real, llsdFive.getType());
	            Assert.assertEquals(0d, llsdFive.asReal(), 0);
	        }

	        @Test
	        public void SerializeReal() throws IOException, OSDException
	        {
	            OSD llsdOne = OSD.FromReal(12987234.723847d);
	            String sOne = NotationalLLSDOSDParser.SerializeLLSDNotation(llsdOne);
	            OSD llsdOneDS = NotationalLLSDOSDParser.DeserializeLLSDNotation(sOne);
	            Assert.assertEquals(OSDType.Real, llsdOneDS.getType());
	            Assert.assertEquals(12987234.723847d, llsdOneDS.asReal(), 0);

	            OSD llsdTwo = OSD.FromReal(-32347892.234234d);
	            String sTwo = NotationalLLSDOSDParser.SerializeLLSDNotation(llsdTwo);
	            OSD llsdTwoDS = NotationalLLSDOSDParser.DeserializeLLSDNotation(sTwo);
	            Assert.assertEquals(OSDType.Real, llsdTwoDS.getType());
	            Assert.assertEquals(-32347892.234234d, llsdTwoDS.asReal(), 0);

	            OSD llsdThree = OSD.FromReal( Double.MAX_VALUE );
	            String sThree = NotationalLLSDOSDParser.SerializeLLSDNotation( llsdThree );
	            OSD llsdThreeDS = NotationalLLSDOSDParser.DeserializeLLSDNotation( sThree );
	            Assert.assertEquals( OSDType.Real, llsdThreeDS.getType() );
	            Assert.assertEquals( Double.MAX_VALUE, llsdThreeDS.asReal(), 0);
	        
	            OSD llsdFour = OSD.FromReal(Double.MIN_VALUE);
	            String sFour = NotationalLLSDOSDParser.SerializeLLSDNotation(llsdFour);
	            OSD llsdFourDS = NotationalLLSDOSDParser.DeserializeLLSDNotation(sFour);
	            Assert.assertEquals(OSDType.Real, llsdFourDS.getType());
	            Assert.assertEquals(Double.MIN_VALUE, llsdFourDS.asReal(), 0);

	            OSD llsdFive = OSD.FromReal(-1.1123123E+50d);
	            String sFive = NotationalLLSDOSDParser.SerializeLLSDNotation(llsdFive);
	            OSD llsdFiveDS = NotationalLLSDOSDParser.DeserializeLLSDNotation(sFive);
	            Assert.assertEquals(OSDType.Real, llsdFiveDS.getType());
	            Assert.assertEquals(-1.1123123E+50d, llsdFiveDS.asReal(), 0);

	            OSD llsdSix = OSD.FromReal(2.0193899999999998204e-06);
	            String sSix = NotationalLLSDOSDParser.SerializeLLSDNotation(llsdSix);
	            OSD llsdSixDS = NotationalLLSDOSDParser.DeserializeLLSDNotation(sSix);
	            Assert.assertEquals(OSDType.Real, llsdSixDS.getType());
	            Assert.assertEquals(2.0193899999999998204e-06, llsdSixDS.asReal(), 0);
	        }

	        @Test
	        public void DeserializeUUID() throws OSDException, IOException
	        {
	            String uuidOne = "u97f4aeca-88a1-42a1-b385-b97b18abb255";
	            OSD llsdOne = NotationalLLSDOSDParser.DeserializeLLSDNotation(uuidOne);
	            Assert.assertEquals(OSDType.UUID, llsdOne.getType());
	            Assert.assertEquals("97f4aeca-88a1-42a1-b385-b97b18abb255", llsdOne.asString());

	            String uuidTwo = "u00000000-0000-0000-0000-000000000000";
	            OSD llsdTwo = NotationalLLSDOSDParser.DeserializeLLSDNotation(uuidTwo);
	            Assert.assertEquals(OSDType.UUID, llsdTwo.getType());
	            Assert.assertEquals("00000000-0000-0000-0000-000000000000", llsdTwo.asString());
	        }

	        @Test
	        public void SerializeUUID() throws IOException, OSDException
	        {
	            OSD llsdOne = OSD.FromUUID(new UUID("97f4aeca-88a1-42a1-b385-b97b18abb255"));
	            String sOne = NotationalLLSDOSDParser.SerializeLLSDNotation(llsdOne);
	            OSD llsdOneDS = NotationalLLSDOSDParser.DeserializeLLSDNotation(sOne);
	            Assert.assertEquals(OSDType.UUID, llsdOneDS.getType());
	            Assert.assertEquals("97f4aeca-88a1-42a1-b385-b97b18abb255", llsdOneDS.asString());

	            OSD llsdTwo = OSD.FromUUID(new UUID("00000000-0000-0000-0000-000000000000"));
	            String sTwo = NotationalLLSDOSDParser.SerializeLLSDNotation(llsdTwo);
	            OSD llsdTwoDS = NotationalLLSDOSDParser.DeserializeLLSDNotation(sTwo);
	            Assert.assertEquals(OSDType.UUID, llsdTwoDS.getType());
	            Assert.assertEquals("00000000-0000-0000-0000-000000000000", llsdTwoDS.asString());
	        }

	        public void DeserializeString() throws OSDException, IOException
	        {
	            String sOne = "''";
	            OSD llsdOne = NotationalLLSDOSDParser.DeserializeLLSDNotation(sOne);
	            Assert.assertEquals(OSDType.String, llsdOne.getType());
	            Assert.assertEquals("", llsdOne.asString());

	            // This is double escaping. Once for the encoding, and once for csharp.  
	            String sTwo = "'test\\'\"test'";
	            OSD llsdTwo = NotationalLLSDOSDParser.DeserializeLLSDNotation(sTwo);
	            Assert.assertEquals(OSDType.String, llsdTwo.getType());
	            Assert.assertEquals("test'\"test", llsdTwo.asString());

	            // "test \\lest"
	            char[] cThree = { (char)0x27, (char)0x74, (char)0x65, (char)0x73, (char)0x74, (char)0x20, (char)0x5c,
	                                (char)0x5c, (char)0x6c, (char)0x65, (char)0x73, (char)0x74, (char)0x27 };
	            String sThree = new String(cThree);

	            OSD llsdThree = NotationalLLSDOSDParser.DeserializeLLSDNotation(sThree);
	            Assert.assertEquals(OSDType.String, llsdThree.getType());
	            Assert.assertEquals("test \\lest", llsdThree.asString());

	            String sFour = "'aa\t la'";
	            OSD llsdFour = NotationalLLSDOSDParser.DeserializeLLSDNotation(sFour);
	            Assert.assertEquals(OSDType.String, llsdFour.getType());
	            Assert.assertEquals("aa\t la", llsdFour.asString());

	            char[] cFive = { (char)0x27, (char)0x5c, (char)0x5c, (char)0x27 };
	            String sFive = new String(cFive);
	            OSD llsdFive = NotationalLLSDOSDParser.DeserializeLLSDNotation(sFive);
	            Assert.assertEquals(OSDType.String, llsdFive.getType());
	            Assert.assertEquals("\\", llsdFive.asString());


	            String sSix = "s(10)\"1234567890\"";
	            OSD llsdSix = NotationalLLSDOSDParser.DeserializeLLSDNotation(sSix);
	            Assert.assertEquals(OSDType.String, llsdSix.getType());
	            Assert.assertEquals("1234567890", llsdSix.asString());

	            String sSeven = "s(5)\"\\\\\\\\\\\"";
	            OSD llsdSeven = NotationalLLSDOSDParser.DeserializeLLSDNotation(sSeven);
	            Assert.assertEquals(OSDType.String, llsdSeven.getType());
	            Assert.assertEquals("\\\\\\\\\\", llsdSeven.asString());

	            String sEight = "\"aouAOUhsdjklfghskldjfghqeiurtzwieortzaslxfjkgh\"";
	            OSD llsdEight = NotationalLLSDOSDParser.DeserializeLLSDNotation(sEight);
	            Assert.assertEquals(OSDType.String, llsdEight.getType());
	            Assert.assertEquals("aouAOUhsdjklfghskldjfghqeiurtzwieortzaslxfjkgh", llsdEight.asString());



	        }

	        public void DoSomeStringSerializingActionsAndAsserts(String s) throws IOException, OSDException
	        {
	            OSD llsdOne = OSD.FromString(s);
	            String sOne = NotationalLLSDOSDParser.SerializeLLSDNotation(llsdOne);
	            OSD llsdOneDS = NotationalLLSDOSDParser.DeserializeLLSDNotation(sOne);
	            Assert.assertEquals(OSDType.String, llsdOne.getType());
	            Assert.assertEquals(s, llsdOneDS.asString());
	        }


	        @Test
	        public void SerializeString() throws ParserConfigurationException, IOException, OSDException, SAXException
	        {
	            DoSomeStringSerializingActionsAndAsserts("");

	            DoSomeStringSerializingActionsAndAsserts("\\");

	            DoSomeStringSerializingActionsAndAsserts("\"\"");

	            DoSomeStringSerializingActionsAndAsserts("ÄÖÜäöü-these-should-be-some-german-umlauts");

	            DoSomeStringSerializingActionsAndAsserts("\t\n\r");

	            DoSomeStringSerializingActionsAndAsserts("asdkjfhaksldjfhalskdjfhaklsjdfhaklsjdhjgzqeuiowrtzserghsldfg" +
	                                                      "asdlkfhqeiortzsdkfjghslkdrjtzsoidklghuisoehiguhsierughaishdl" +
	                                                      "asdfkjhueiorthsgsdkfughaslkdfjshldkfjghsldkjghsldkfghsdklghs" +
	                                                      "wopeighisdjfghklasdfjghsdklfgjhsdklfgjshdlfkgjshdlfkgjshdlfk");

	            DoSomeStringSerializingActionsAndAsserts("all is N\"\\'othing and n'oting is all");

	            DoSomeStringSerializingActionsAndAsserts("very\"british is this.");

	            // We test here also for 4byte characters
	            String xml = "<x>&#x10137;</x>";
	            byte[] bytes = xml.getBytes(Charset.forName("UTF8"));
	            
	            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	            DocumentBuilder db = dbf.newDocumentBuilder();
	            Document doc = db.parse(new ByteArrayInputStream(bytes));
	            Element rootNode = doc.getDocumentElement();
	            System.out.println("Root element " + doc.getDocumentElement().getNodeName());
	            rootNode.normalize();
	           
	            String content = rootNode.getChildNodes().item(0).getNodeValue();
	            
//	            XmlTextReader xtr = new XmlTextReader(new MemoryStream(bytes, false));
//	            xtr.Read();
//	            xtr.Read();
//	            String content = xtr.ReadString();

	            DoSomeStringSerializingActionsAndAsserts(content);

	        }

	        @Test
	        public void DeserializeURI() throws OSDException, IOException
	        {
	            String sUriOne = "l\"http://test.com/test test>\\\"/&yes\"";
	            OSD llsdOne = NotationalLLSDOSDParser.DeserializeLLSDNotation(sUriOne);
	            Assert.assertEquals(OSDType.URI, llsdOne.getType());
	            Assert.assertEquals("http://test.com/test%20test%3E%22/&yes", llsdOne.asString());

	            String sUriTwo = "l\"test/test/test?test=1&toast=2\"";
	            OSD llsdTwo = NotationalLLSDOSDParser.DeserializeLLSDNotation(sUriTwo);
	            Assert.assertEquals(OSDType.URI, llsdTwo.getType());
	            Assert.assertEquals("test/test/test?test=1&toast=2", llsdTwo.asString());
	        }

	        @Test
	        public void SerializeURI() throws URISyntaxException, OSDException, IOException
	        {
	        	URI uriOne[] = new URI[1];
	   
	            Utils.tryParseUri("http://test.org/test test>\\\"/&yes\"", uriOne);
	            OSD llsdOne = OSD.FromUri(uriOne[0]);
	            String sUriOne = NotationalLLSDOSDParser.SerializeLLSDNotation(llsdOne);
	            System.out.println("sUriOne: " + sUriOne);
	            OSD llsdOneDS = NotationalLLSDOSDParser.DeserializeLLSDNotation(sUriOne);
	            Assert.assertEquals(OSDType.URI, llsdOneDS.getType());
	            Assert.assertEquals(uriOne[0], llsdOneDS.asUri());

	        	URI uriTwo[] = new URI[1];

	        	Utils.tryParseUri("test/test/near/the/end?test=1", uriTwo);
	            OSD llsdTwo = OSD.FromUri(uriTwo[0]);
	            String sUriTwo = NotationalLLSDOSDParser.SerializeLLSDNotation(llsdTwo);
	            OSD llsdTwoDS = NotationalLLSDOSDParser.DeserializeLLSDNotation(sUriTwo);
	            Assert.assertEquals(OSDType.URI, llsdTwoDS.getType());
	            Assert.assertEquals(uriTwo[0], llsdTwoDS.asUri());
	        }

	        @Test
	        public void DeserializeDate() throws OSDException, IOException
	        {
	            String sDateOne = "d\"2007-12-31T20:49:10Z\"";
	            OSD llsdOne = NotationalLLSDOSDParser.DeserializeLLSDNotation(sDateOne);
	            Assert.assertEquals(OSDType.Date, llsdOne.getType());
	            
	           	Calendar cal = Calendar.getInstance();
	        	cal.setTimeZone(TimeZone.getTimeZone("UTC"));
	            cal.set(Calendar.MILLISECOND, 0);
	            
	            cal.set(2007, 11, 31, 20, 49, 10);
	            Date dt = cal.getTime();
	            Date dtDS = llsdOne.asDate();
	            Assert.assertEquals(dt, dtDS);
	        }

	        @Test
	        public void SerializeDate() throws IOException, OSDException
	        {
	           	Calendar cal = Calendar.getInstance();
	        	cal.setTimeZone(TimeZone.getTimeZone("UTC"));
	            cal.set(Calendar.MILLISECOND, 0);
	            
	            cal.set(2005, 7, 10, 11, 23, 4);
	            Date dtOne = cal.getTime(); 
	            OSD llsdOne = OSD.FromDate(dtOne);
	            String sDtOne = NotationalLLSDOSDParser.SerializeLLSDNotation(llsdOne);
	            OSD llsdOneDS = NotationalLLSDOSDParser.DeserializeLLSDNotation(sDtOne);
	            Assert.assertEquals(OSDType.Date, llsdOneDS.getType());
	            Date dtOneDS = llsdOneDS.asDate();
	            Assert.assertEquals(dtOne, dtOneDS);

	            cal.set(2010, 9, 11, 23, 00, 10);
	            cal.set(Calendar.MILLISECOND, 100);
	            Date dtTwo = cal.getTime();
	            
	            OSD llsdTwo = OSD.FromDate(dtTwo);
	            String sDtTwo = NotationalLLSDOSDParser.SerializeLLSDNotation(llsdTwo);
	            OSD llsdTwoDS = NotationalLLSDOSDParser.DeserializeLLSDNotation(sDtTwo);
	            Assert.assertEquals(OSDType.Date, llsdTwoDS.getType());
	            Date dtTwoDS = llsdTwoDS.asDate();
	            System.out.println(dtTwo.getTime() + " : " + dtTwoDS.getTime());
	            Assert.assertEquals(dtTwo, dtTwoDS);

	            // check if a *local* time can be serialized and deserialized
	            cal.set(2009, 12, 30, 8, 25, 10);
	            cal.set(Calendar.MILLISECOND, 0);
	            
	            Date dtThree = cal.getTime();
	            OSD llsdDateThree = OSD.FromDate(dtThree);
	            String sDateThreeSerialized = NotationalLLSDOSDParser.SerializeLLSDNotation(llsdDateThree);
	            OSD llsdDateThreeDS = NotationalLLSDOSDParser.DeserializeLLSDNotation(sDateThreeSerialized);
	            Assert.assertEquals(OSDType.Date, llsdDateThreeDS.getType());
	            Assert.assertEquals(dtThree, llsdDateThreeDS.asDate());
	        }

	        @Test
	        public void SerializeBinary() throws OSDException, IOException
	        {
	            byte[] binary = { 0x0, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0b,
	                                0x0b, 0x0c, 0x0d, 0x0e, 0x0f };

	            OSD llsdBinary = OSD.FromBinary(binary);
	            String sBinarySerialized = NotationalLLSDOSDParser.SerializeLLSDNotation(llsdBinary);
	            OSD llsdBinaryDS = NotationalLLSDOSDParser.DeserializeLLSDNotation(sBinarySerialized);
	            Assert.assertEquals(OSDType.Binary, llsdBinaryDS.getType());
	            Assert.assertArrayEquals(binary, llsdBinaryDS.asBinary());
	        }

	        @Test
	        public void DeserializeArray() throws OSDException, IOException
	        {
	            String sArrayOne = "[]";
	            OSDArray llsdArrayOne = (OSDArray)NotationalLLSDOSDParser.DeserializeLLSDNotation(sArrayOne);
	            Assert.assertEquals(OSDType.Array, llsdArrayOne.getType());
	            Assert.assertEquals(0, llsdArrayOne.count());

	            String sArrayTwo = "[ i0 ]";
	            OSDArray llsdArrayTwo = (OSDArray)NotationalLLSDOSDParser.DeserializeLLSDNotation(sArrayTwo);
	            Assert.assertEquals(OSDType.Array, llsdArrayTwo.getType());
	            Assert.assertEquals(1, llsdArrayTwo.count());
	            OSDInteger llsdIntOne = (OSDInteger)llsdArrayTwo.get(0);
	            Assert.assertEquals(OSDType.Integer, llsdIntOne.getType());
	            Assert.assertEquals(0, llsdIntOne.asInteger());

	            String sArrayThree = "[ i0, i1 ]";
	            OSDArray llsdArrayThree = (OSDArray)NotationalLLSDOSDParser.DeserializeLLSDNotation(sArrayThree);
	            Assert.assertEquals(OSDType.Array, llsdArrayThree.getType());
	            Assert.assertEquals(2, llsdArrayThree.count());
	            OSDInteger llsdIntTwo = (OSDInteger)llsdArrayThree.get(0);
	            Assert.assertEquals(OSDType.Integer, llsdIntTwo.getType());
	            Assert.assertEquals(0, llsdIntTwo.asInteger());
	            OSDInteger llsdIntThree = (OSDInteger)llsdArrayThree.get(1);
	            Assert.assertEquals(OSDType.Integer, llsdIntThree.getType());
	            Assert.assertEquals(1, llsdIntThree.asInteger());

	            String sArrayFour = " [ \"testtest\", \"aha\",t,f,i1, r1.2, [ i1] ] ";
//	        	 System.out.println("Start Testing ...");
	            OSDArray llsdArrayFour = (OSDArray)NotationalLLSDOSDParser.DeserializeLLSDNotation(sArrayFour);
//	        	 System.out.println("Ending Testing ...");
	            Assert.assertEquals(OSDType.Array, llsdArrayFour.getType());
	            Assert.assertEquals(7, llsdArrayFour.count());
	            Assert.assertEquals("testtest", llsdArrayFour.get(0).asString());
	            Assert.assertEquals("aha", llsdArrayFour.get(1).asString());
	            Assert.assertTrue(llsdArrayFour.get(2).asBoolean());
	            Assert.assertFalse(llsdArrayFour.get(3).asBoolean());
	            Assert.assertEquals(1, llsdArrayFour.get(4).asInteger());
	            Assert.assertEquals(1.2d, llsdArrayFour.get(5).asReal(), 0);
	            Assert.assertEquals(OSDType.Array, llsdArrayFour.get(6).getType());
	            OSDArray llsdArrayFive = (OSDArray)llsdArrayFour.get(6);
	            Assert.assertEquals(1, llsdArrayFive.get(0).asInteger());

	        }

	        @Test
	        public void SerializeArray() throws IOException, OSDException
	        {
	            OSDArray llsdOne = new OSDArray();
	            String sOne = NotationalLLSDOSDParser.SerializeLLSDNotation(llsdOne);
	            OSDArray llsdOneDS = (OSDArray)NotationalLLSDOSDParser.DeserializeLLSDNotation(sOne);
	            Assert.assertEquals(OSDType.Array, llsdOneDS.getType());
	            Assert.assertEquals(0, llsdOneDS.count());

	            OSD llsdTwo = OSD.FromInteger(123234);
	            OSD llsdThree = OSD.FromString("asedkfjhaqweiurohzasdf");
	            OSDArray llsdFour = new OSDArray();
	            llsdFour.add(llsdTwo);
	            llsdFour.add(llsdThree);

	            llsdOne.add(llsdTwo);
	            llsdOne.add(llsdThree);
	            llsdOne.add(llsdFour);

	            String sFive = NotationalLLSDOSDParser.SerializeLLSDNotation(llsdOne);
	            OSDArray llsdFive = (OSDArray)NotationalLLSDOSDParser.DeserializeLLSDNotation(sFive);
	            Assert.assertEquals(OSDType.Array, llsdFive.getType());
	            Assert.assertEquals(3, llsdFive.count());
	            Assert.assertEquals(OSDType.Integer, llsdFive.get(0).getType());
	            Assert.assertEquals(123234, llsdFive.get(0).asInteger());
	            Assert.assertEquals(OSDType.String, llsdFive.get(1).getType());
	            Assert.assertEquals("asedkfjhaqweiurohzasdf", llsdFive.get(1).asString());

	            OSDArray llsdSix = (OSDArray)llsdFive.get(2);
	            Assert.assertEquals(OSDType.Array, llsdSix.getType());
	            Assert.assertEquals(2, llsdSix.count());
	            Assert.assertEquals(OSDType.Integer, llsdSix.get(0).getType());
	            Assert.assertEquals(123234, llsdSix.get(0).asInteger());
	            Assert.assertEquals(OSDType.String, llsdSix.get(1).getType());
	            Assert.assertEquals("asedkfjhaqweiurohzasdf", llsdSix.get(1).asString());
	        }

	        @Test
	        public void DeserializeMap() throws OSDException, IOException
	        {
	            String sMapOne = " { } ";
	            OSDMap llsdMapOne = (OSDMap)NotationalLLSDOSDParser.DeserializeLLSDNotation(sMapOne);
	            Assert.assertEquals(OSDType.Map, llsdMapOne.getType());
	            Assert.assertEquals(0, llsdMapOne.count());

	            String sMapTwo = " { \"test\":i2 } ";
	            OSDMap llsdMapTwo = (OSDMap)NotationalLLSDOSDParser.DeserializeLLSDNotation(sMapTwo);
	            Assert.assertEquals(OSDType.Map, llsdMapTwo.getType());
	            Assert.assertEquals(1, llsdMapTwo.count());
	            Assert.assertEquals(OSDType.Integer, llsdMapTwo.get("test").getType());
	            Assert.assertEquals(2, llsdMapTwo.get("test").asInteger());

	            String sMapThree = " { 'test':\"testtesttest\", 'aha':\"muahahaha\" , \"anywhere\":! } ";
	            OSDMap llsdMapThree = (OSDMap)NotationalLLSDOSDParser.DeserializeLLSDNotation(sMapThree);
	            Assert.assertEquals(OSDType.Map, llsdMapThree.getType());
	            Assert.assertEquals(3, llsdMapThree.count());
	            Assert.assertEquals(OSDType.String, llsdMapThree.get("test").getType());
	            Assert.assertEquals("testtesttest", llsdMapThree.get("test").asString());
	            Assert.assertEquals(OSDType.String, llsdMapThree.get("test").getType());
	            Assert.assertEquals("muahahaha", llsdMapThree.get("aha").asString());
	            Assert.assertEquals(OSDType.Unknown, llsdMapThree.get("self").getType());

	            String sMapFour = " { 'test' : { 'test' : i1, 't0st' : r2.5 }, 'tist' : \"hello world!\", 'tast' : \"last\" } ";
	            OSDMap llsdMapFour = (OSDMap)NotationalLLSDOSDParser.DeserializeLLSDNotation(sMapFour);
	            Assert.assertEquals(OSDType.Map, llsdMapFour.getType());
	            Assert.assertEquals(3, llsdMapFour.count());
	            Assert.assertEquals("hello world!", llsdMapFour.get("tist").asString());
	            Assert.assertEquals("last", llsdMapFour.get("tast").asString());
	            OSDMap llsdMapFive = (OSDMap)llsdMapFour.get("test");
	            Assert.assertEquals(OSDType.Map, llsdMapFive.getType());
	            Assert.assertEquals(2, llsdMapFive.count());
	            Assert.assertEquals(OSDType.Integer, llsdMapFive.get("test").getType());
	            Assert.assertEquals(1, llsdMapFive.get("test").asInteger());
	            Assert.assertEquals(OSDType.Real, llsdMapFive.get("t0st").getType());
	            Assert.assertEquals(2.5d, llsdMapFive.get("t0st").asReal(), 0);

	        }

	        @Test
	        public void SerializeMap() throws SAXException, IOException, OSDException, ParserConfigurationException
	        {
	            OSDMap llsdOne = new OSDMap();
	            String sOne = NotationalLLSDOSDParser.SerializeLLSDNotation(llsdOne);
	            OSDMap llsdOneDS = (OSDMap)NotationalLLSDOSDParser.DeserializeLLSDNotation(sOne);
	            Assert.assertEquals(OSDType.Map, llsdOneDS.getType());
	            Assert.assertEquals(0, llsdOneDS.count());

	            OSD llsdTwo = OSD.FromInteger(123234);
	            OSD llsdThree = OSD.FromString("asedkfjhaqweiurohzasdf");
	            OSDMap llsdFour = new OSDMap();
	            llsdFour.put("test0", llsdTwo);
	            llsdFour.put("test1", llsdThree);

	            llsdOne.put("test0", llsdTwo);
	            llsdOne.put("test1", llsdThree);
	            llsdOne.put("test2", llsdFour);

	            String sFive = NotationalLLSDOSDParser.SerializeLLSDNotation(llsdOne);
	            OSDMap llsdFive = (OSDMap)NotationalLLSDOSDParser.DeserializeLLSDNotation(sFive);
	            Assert.assertEquals(OSDType.Map, llsdFive.getType());
	            Assert.assertEquals(3, llsdFive.count());
	            Assert.assertEquals(OSDType.Integer, llsdFive.get("test0").getType());
	            Assert.assertEquals(123234, llsdFive.get("test0").asInteger());
	            Assert.assertEquals(OSDType.String, llsdFive.get("test1").getType());
	            Assert.assertEquals("asedkfjhaqweiurohzasdf", llsdFive.get("test1").asString());

	            OSDMap llsdSix = (OSDMap)llsdFive.get("test2");
	            Assert.assertEquals(OSDType.Map, llsdSix.getType());
	            Assert.assertEquals(2, llsdSix.count());
	            Assert.assertEquals(OSDType.Integer, llsdSix.get("test0").getType());
	            Assert.assertEquals(123234, llsdSix.get("test0").asInteger());
	            Assert.assertEquals(OSDType.String, llsdSix.get("test1").getType());
	            Assert.assertEquals("asedkfjhaqweiurohzasdf", llsdSix.get("test1").asString());

	            // We test here also for 4byte characters as map keys
	            String xml = "<x>&#x10137;</x>";
	            byte[] bytes = xml.getBytes(Charset.forName("UTF8"));
	            
	            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	            DocumentBuilder db = dbf.newDocumentBuilder();
	            Document doc = db.parse(new ByteArrayInputStream(bytes));
	            Element rootNode = doc.getDocumentElement();
	            System.out.println("Root element " + doc.getDocumentElement().getNodeName());
	            rootNode.normalize();
	           
	            String content = rootNode.getChildNodes().item(0).getNodeValue();

	            OSDMap llsdSeven = new OSDMap();
	            llsdSeven.put(content, OSD.FromString(content));
	            String sSeven = NotationalLLSDOSDParser.SerializeLLSDNotation(llsdSeven);
	            OSDMap llsdSevenDS = (OSDMap)NotationalLLSDOSDParser.DeserializeLLSDNotation(sSeven);
	            Assert.assertEquals(OSDType.Map, llsdSevenDS.getType());
	            Assert.assertEquals(1, llsdSevenDS.count());
	            Assert.assertEquals(content, llsdSevenDS.get(content).asString());
	        }

	        @Test
	        public void DeserializeRealWorldExamples() throws OSDException, IOException
	        {
	            String realWorldExample = "	[ \n" + 
	  "{'destination':'http://secondlife.com'}, \n" +  
	  "{'version':i1}, \n" +
	  "{\n" + 
	    "'agent_id':u3c115e51-04f4-523c-9fa6-98aff1034730, \n" +  
	    "'session_id':u2c585cec-038c-40b0-b42e-a25ebab4d132, \n" + 
	    "'circuit_code':i1075, \n" + 
	    "'first_name':'Phoenix', \n" + 
	    "'last_name':'Linden', \n" + 
	    "'position':[r70.9247,r254.378,r38.7304], \n" + 
	    "'look_at':[r-0.043753,r-0.999042,r0], \n" + 
	    "'granters':[ua2e76fcd-9360-4f6d-a924-000000000003], \n" + 
	    "'attachment_data': \n" + 
	    "[\n" + 
	      "{\n" + 
	        "'attachment_point':i2, \n" + 
	        "'item_id':ud6852c11-a74e-309a-0462-50533f1ef9b3, \n" + 
	        "'asset_id':uc69b29b1-8944-58ae-a7c5-2ca7b23e22fb \n" + 
	      "},\n" + 
	      "{\n" + 
	        "'attachment_point':i10, \n" + 
	        "'item_id':uff852c22-a74e-309a-0462-50533f1ef900, \n" + 
	        "'asset_id':u5868dd20-c25a-47bd-8b4c-dedc99ef9479 \n" + 
	      "}\n" + 
	    "]\n" + 
	  "}\n" + 
	"]\n";
	            // We dont do full testing here. We are fine if a few values are right
	            // and the parser doesnt throw an exception
	            OSDArray llsdArray = (OSDArray)NotationalLLSDOSDParser.DeserializeLLSDNotation(realWorldExample);
	            Assert.assertEquals(OSDType.Array, llsdArray.getType());
	            Assert.assertEquals(3, llsdArray.count());

	            OSDMap llsdMapOne = (OSDMap)llsdArray.get(0);
	            Assert.assertEquals(OSDType.Map, llsdMapOne.getType());
	            Assert.assertEquals("http://secondlife.com", llsdMapOne.get("destination").asString());

	            OSDMap llsdMapTwo = (OSDMap)llsdArray.get(1);
	            Assert.assertEquals(OSDType.Map, llsdMapTwo.getType());
	            Assert.assertEquals(OSDType.Integer, llsdMapTwo.get("version").getType());
	            Assert.assertEquals(1, llsdMapTwo.get("version").asInteger());

	            OSDMap llsdMapThree = (OSDMap)llsdArray.get(2);
	            Assert.assertEquals(OSDType.UUID, llsdMapThree.get("session_id").getType());
	            Assert.assertEquals("2c585cec-038c-40b0-b42e-a25ebab4d132", llsdMapThree.get("session_id").asString());
	            Assert.assertEquals(OSDType.UUID, llsdMapThree.get("agent_id").getType());
	            Assert.assertEquals("3c115e51-04f4-523c-9fa6-98aff1034730", llsdMapThree.get("agent_id").asString());

	        }

	        @Test
	        public void SerializeFormattedTest() throws IOException, OSDException
	        {
	            // This is not a real test. Instead look at the console.out tab for how formatted notation looks like.
	            OSDArray llsdArray = new OSDArray();
	            OSD llsdOne = OSD.FromInteger(1);
	            OSD llsdTwo = OSD.FromInteger(1);
	            llsdArray.add(llsdOne);
	            llsdArray.add(llsdTwo);
	                        
	            OSDMap llsdMap = new OSDMap();
	            OSD llsdThree = OSD.FromInteger(2);
	            llsdMap.put("test1", llsdThree);
	            OSD llsdFour = OSD.FromInteger(2);
	            llsdMap.put("test2", llsdFour);

	            llsdArray.add(llsdMap);
	            
	            OSDArray llsdArrayTwo = new OSDArray();
	            OSD llsdFive = OSD.FromString("asdflkhjasdhj");
	            OSD llsdSix = OSD.FromString("asdkfhasjkldfghsd");
	            llsdArrayTwo.add(llsdFive);
	            llsdArrayTwo.add(llsdSix);

	            llsdMap.put("test3",  llsdArrayTwo);

	            String sThree = NotationalLLSDOSDParser.SerializeLLSDNotationFormatted(llsdArray);

	            // we also try to parse this... and look a little at the results 
	            OSDArray llsdSeven = (OSDArray)NotationalLLSDOSDParser.DeserializeLLSDNotation(sThree);
	            Assert.assertEquals(OSDType.Array, llsdSeven.getType());
	            Assert.assertEquals(3, llsdSeven.count());
	            Assert.assertEquals(OSDType.Integer, llsdSeven.get(0).getType());
	            Assert.assertEquals(1, llsdSeven.get(0).asInteger());
	            Assert.assertEquals(OSDType.Integer, llsdSeven.get(1).getType());
	            Assert.assertEquals(1, llsdSeven.get(1).asInteger());

	            Assert.assertEquals(OSDType.Map, llsdSeven.get(2).getType());
	            // thats enough for now.            
	        }
	    }
