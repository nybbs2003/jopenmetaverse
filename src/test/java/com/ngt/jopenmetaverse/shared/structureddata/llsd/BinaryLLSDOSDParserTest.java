package com.ngt.jopenmetaverse.shared.structureddata.llsd;

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
import com.ngt.jopenmetaverse.shared.structureddata.OSDString;
import com.ngt.jopenmetaverse.shared.structureddata.OSDType;
import com.ngt.jopenmetaverse.shared.types.UUID;

/// </summary>
public class BinaryLLSDOSDParserTest
{
	//ASCII Bytes
    private static final byte[] binaryHead = "<? llsd/binary ?>".getBytes(Charset.forName("US-ASCII"));

    @Test
    public void HelperFunctions() throws IOException
    {
        String s = "this is a testString so that we can find something from the beginning";
        byte[] sBinary = s.getBytes(Charset.forName("US-ASCII"));
        ByteArrayInputStream stream = new ByteArrayInputStream(sBinary);
        stream.mark(stream.available() + 1);
        boolean result = BinaryLLSDOSDParser.FindString(stream, "this");
        Assert.assertTrue(result);
        Assert.assertEquals(4L, sBinary.length - stream.available());

//        stream.Position = 10L;
//        stream.reset();
//        stream.mark(stream.available() + 1);
        stream.skip(6L);
        result = BinaryLLSDOSDParser.FindString(stream, "teststring");
        Assert.assertTrue(result);
        Assert.assertEquals(20L, sBinary.length - stream.available());

//        stream.Position = 25L;
//        stream.reset();
//        stream.mark(stream.available() + 1);
        stream.skip(5L);
        result = BinaryLLSDOSDParser.FindString(stream, "notfound");
        Assert.assertFalse(result);
        Assert.assertEquals(25L, sBinary.length - stream.available());

//        stream.Position = 60L;
//        stream.reset();
//        stream.mark(stream.available() + 1);
        stream.skip(35L);
        result = BinaryLLSDOSDParser.FindString(stream, "beginningAndMore");
        Assert.assertFalse(result);
        Assert.assertEquals(60L, sBinary.length - stream.available());

        byte[] sFrontWhiteSpace = "   \t\t\n\rtest".getBytes(Charset.forName("US-ASCII"));
        ByteArrayInputStream streamTwo = new ByteArrayInputStream(sFrontWhiteSpace);
        BinaryLLSDOSDParser.SkipWhiteSpace(streamTwo);
        Assert.assertEquals(7L, sFrontWhiteSpace.length - streamTwo.available());

        byte[] sMiddleWhiteSpace = "test \t\t\n\rtest".getBytes(Charset.forName("US-ASCII"));
        ByteArrayInputStream streamThree = new ByteArrayInputStream(sMiddleWhiteSpace);
//        streamThree.Position = 4L;
//        stream.reset();
//        stream.mark(stream.available() + 1);
        streamThree.skip(4L);
        BinaryLLSDOSDParser.SkipWhiteSpace(streamThree);
        Assert.assertEquals(9L, sMiddleWhiteSpace.length - streamThree.available());

        byte[] sNoWhiteSpace = "testtesttest".getBytes(Charset.forName("US-ASCII"));
        ByteArrayInputStream streamFour = new ByteArrayInputStream(sNoWhiteSpace);
        BinaryLLSDOSDParser.SkipWhiteSpace(streamFour);
        Assert.assertEquals(0L, sNoWhiteSpace.length - streamFour.available());

    }

    public static byte[] mergeArrays(byte[] mainArray, byte[] addArray) {
    	byte[] finalArray = new byte[mainArray.length + addArray.length];
        System.arraycopy(mainArray, 0, finalArray, 0, mainArray.length);
        System.arraycopy(addArray, 0, finalArray, mainArray.length, addArray.length);

        return finalArray;
    }
    
    // Testvalues for Undef:
    private static byte[] binaryUndefValue = { 0x21 };
    private static byte[] binaryUndef = mergeArrays(binaryHead, binaryUndefValue);
    
    
    @Test
    public void deserializeUndef() throws IOException, OSDException
    {
        OSD llsdUndef = BinaryLLSDOSDParser.DeserializeLLSDBinary(binaryUndef);
        Assert.assertEquals(OSDType.Unknown, llsdUndef.getType());
    }

    @Test
    public void SerializeUndef() throws Exception
    {
        OSD llsdUndef = new OSD();
        byte[] binaryUndefSerialized = BinaryLLSDOSDParser.SerializeLLSDBinary(llsdUndef);
        Assert.assertArrayEquals(binaryUndef, binaryUndefSerialized);
    }

    private static byte[] binaryTrueValue = { 0x31 };
    private static byte[] binaryTrue = (byte[])mergeArrays(binaryHead, binaryTrueValue);


    private static byte[] binaryFalseValue = { 0x30 };
    private static byte[] binaryFalse = (byte[])mergeArrays(binaryHead, binaryFalseValue);

    @Test
    public void DeserializeBool() throws IOException, OSDException
    {
        OSD llsdTrue = BinaryLLSDOSDParser.DeserializeLLSDBinary(binaryTrue);
        Assert.assertEquals(OSDType.Boolean, llsdTrue.getType());
        Assert.assertTrue(llsdTrue.asBoolean());

        OSD llsdFalse = BinaryLLSDOSDParser.DeserializeLLSDBinary(binaryFalse);
        Assert.assertEquals(OSDType.Boolean, llsdFalse.getType());
        Assert.assertFalse(llsdFalse.asBoolean());
    }

    @Test
    public void SerializeBool() throws Exception
    {
        OSD llsdTrue = OSD.FromBoolean(true);
        byte[] binaryTrueSerialized = BinaryLLSDOSDParser.SerializeLLSDBinary(llsdTrue);
        Assert.assertArrayEquals(binaryTrue, binaryTrueSerialized);

        OSD llsdFalse = OSD.FromBoolean(false);
        byte[] binaryFalseSerialized = BinaryLLSDOSDParser.SerializeLLSDBinary(llsdFalse);
        Assert.assertArrayEquals(binaryFalse, binaryFalseSerialized);
    }

    private static byte[] binaryZeroIntValue = { 0x69, 0x0, 0x0, 0x0, 0x0 };
    private static byte[] binaryZeroInt = (byte[])mergeArrays(binaryHead, binaryZeroIntValue);

    private static byte[] binaryAnIntValue = { (byte)0x69, (byte)0x0, (byte)0x12, (byte)0xd7, (byte)0x9b };
    private static byte[] binaryAnInt = (byte[])mergeArrays(binaryHead, binaryAnIntValue);

    @Test
    public void DeserializeInteger() throws IOException, OSDException
    {
        OSD llsdZeroInteger = BinaryLLSDOSDParser.DeserializeLLSDBinary(binaryZeroInt);
        Assert.assertEquals(OSDType.Integer, llsdZeroInteger.getType());
        Assert.assertEquals(0, llsdZeroInteger.asInteger());


        OSD llsdAnInteger = BinaryLLSDOSDParser.DeserializeLLSDBinary(binaryAnInt);
        Assert.assertEquals(OSDType.Integer, llsdAnInteger.getType());
        Assert.assertEquals(1234843, llsdAnInteger.asInteger());
    }

    @Test
    public void SerializeInteger() throws Exception
    {
        OSD llsdZeroInt = OSD.FromInteger(0);
        byte[] binaryZeroIntSerialized = BinaryLLSDOSDParser.SerializeLLSDBinary(llsdZeroInt);
        Assert.assertArrayEquals(binaryZeroInt, binaryZeroIntSerialized);

        binaryZeroIntSerialized = BinaryLLSDOSDParser.SerializeLLSDBinary(llsdZeroInt, false);
        Assert.assertArrayEquals(binaryZeroIntValue, binaryZeroIntSerialized);

        OSD llsdAnInt = OSD.FromInteger(1234843);
        byte[] binaryAnIntSerialized = BinaryLLSDOSDParser.SerializeLLSDBinary(llsdAnInt);
        Assert.assertArrayEquals(binaryAnInt, binaryAnIntSerialized);

        binaryAnIntSerialized = BinaryLLSDOSDParser.SerializeLLSDBinary(llsdAnInt, false);
        Assert.assertArrayEquals(binaryAnIntValue, binaryAnIntSerialized);
    }

    private static byte[] binaryRealValue = { (byte)0x72, (byte)0x41, (byte)0x2c, (byte)0xec, (byte)0xf6, (byte)0x77, (byte)0xce, (byte)0xd9, (byte)0x17 };
    private static byte[] binaryReal = (byte[])mergeArrays(binaryHead, binaryRealValue);

    @Test
    public void DeserializeReal() throws IOException, OSDException
    {
        OSD llsdReal = BinaryLLSDOSDParser.DeserializeLLSDBinary(binaryReal);
        Assert.assertEquals(OSDType.Real, llsdReal.getType());
        Assert.assertEquals(947835.234d, llsdReal.asReal(), 0);
    }

    @Test
    public void SerializeReal() throws Exception
    {
        OSD llsdReal = OSD.FromReal(947835.234d);
        byte[] binaryRealSerialized = BinaryLLSDOSDParser.SerializeLLSDBinary(llsdReal);
        Assert.assertArrayEquals(binaryReal, binaryRealSerialized);

        binaryRealSerialized = BinaryLLSDOSDParser.SerializeLLSDBinary(llsdReal);
        Assert.assertArrayEquals(binaryReal, binaryRealSerialized);
    }

    private static byte[] binaryAUUIDValue = { (byte)0x75, (byte) 0x97, (byte) 0xf4, (byte)0xae, (byte)0xca, (byte)0x88, (byte)0xa1, (byte)0x42, (byte)0xa1, 
    	(byte)0xb3, (byte)0x85, (byte)0xb9, (byte)0x7b, (byte)0x18, (byte)0xab, (byte)0xb2, (byte)0x55 };
    private static byte[] binaryAUUID = (byte[])mergeArrays(binaryHead, binaryAUUIDValue);

    private static byte[] binaryZeroUUIDValue = { 0x75, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0 };
    private static byte[] binaryZeroUUID = (byte[])mergeArrays(binaryHead, binaryZeroUUIDValue);


    @Test
    public void DeserializeUUID() throws IOException, OSDException
    {
        OSD llsdAUUID = BinaryLLSDOSDParser.DeserializeLLSDBinary(binaryAUUID);
        Assert.assertEquals(OSDType.UUID, llsdAUUID.getType());
        Assert.assertEquals("97f4aeca-88a1-42a1-b385-b97b18abb255", llsdAUUID.asString());

        OSD llsdZeroUUID = BinaryLLSDOSDParser.DeserializeLLSDBinary(binaryZeroUUID);
        Assert.assertEquals(OSDType.UUID, llsdZeroUUID.getType());
        Assert.assertEquals("00000000-0000-0000-0000-000000000000", llsdZeroUUID.asString());

    }

    @Test
    public void SerializeUUID() throws Exception
    {
        OSD llsdAUUID = OSD.FromUUID(new UUID("97f4aeca-88a1-42a1-b385-b97b18abb255"));
        byte[] binaryAUUIDSerialized = BinaryLLSDOSDParser.SerializeLLSDBinary(llsdAUUID);
        Assert.assertArrayEquals(binaryAUUID, binaryAUUIDSerialized);

        binaryAUUIDSerialized = BinaryLLSDOSDParser.SerializeLLSDBinary(llsdAUUID);
        Assert.assertArrayEquals(binaryAUUID, binaryAUUIDSerialized);

        OSD llsdZeroUUID = OSD.FromUUID(new UUID("00000000-0000-0000-0000-000000000000"));
        byte[] binaryZeroUUIDSerialized = BinaryLLSDOSDParser.SerializeLLSDBinary(llsdZeroUUID);
        Assert.assertArrayEquals(binaryZeroUUID, binaryZeroUUIDSerialized);

        binaryZeroUUIDSerialized = BinaryLLSDOSDParser.SerializeLLSDBinary(llsdZeroUUID);
        Assert.assertArrayEquals(binaryZeroUUID, binaryZeroUUIDSerialized);
    }

    private static byte[] binaryBinStringValue = { 0x62, 0x0, 0x0, 0x0, 0x34, // this line is the encoding header
                                    0x74, 0x65, 0x73, 0x74, 0x69, 0x6e, 0x67, 0x20, 0x61, 0x20, 0x73, 
                                    0x69, 0x6d, 0x70, 0x6c, 0x65, 0x20, 0x62, 0x69, 0x6e, 0x61, 0x72, 0x79, 0x20, 0x63, 0x6f,
                                    0x6e, 0x76, 0x65, 0x72, 0x73, 0x69, 0x6f, 0x6e, 0x20, 0x66, 0x6f, 0x72, 0x20, 0x74, 0x68,
                                    0x69, 0x73, 0x20, 0x73, 0x74, 0x72, 0x69, 0x6e, 0x67, 0xa, 0xd };
    private static byte[] binaryBinString = (byte[])mergeArrays(binaryHead, binaryBinStringValue);

    @Test
    public void DeserializeLLSDBinary() throws IOException, OSDException
    {
        OSD llsdBytes = BinaryLLSDOSDParser.DeserializeLLSDBinary(binaryBinString);
        Assert.assertEquals(OSDType.Binary, llsdBytes.getType());
        byte[] contentBinString = { 0x74, 0x65, 0x73, 0x74, 0x69, 0x6e, 0x67, 0x20, 0x61, 0x20, 0x73, 
                                    0x69, 0x6d, 0x70, 0x6c, 0x65, 0x20, 0x62, 0x69, 0x6e, 0x61, 0x72, 0x79, 0x20, 0x63, 0x6f,
                                    0x6e, 0x76, 0x65, 0x72, 0x73, 0x69, 0x6f, 0x6e, 0x20, 0x66, 0x6f, 0x72, 0x20, 0x74, 0x68,
                                    0x69, 0x73, 0x20, 0x73, 0x74, 0x72, 0x69, 0x6e, 0x67, 0xa, 0xd };
        Assert.assertArrayEquals(contentBinString, llsdBytes.asBinary());
    }

    @Test
    public void SerializeLLSDBinary() throws Exception
    {
        byte[] contentBinString = { 0x74, 0x65, 0x73, 0x74, 0x69, 0x6e, 0x67, 0x20, 0x61, 0x20, 0x73, 
                                    0x69, 0x6d, 0x70, 0x6c, 0x65, 0x20, 0x62, 0x69, 0x6e, 0x61, 0x72, 0x79, 0x20, 0x63, 0x6f,
                                    0x6e, 0x76, 0x65, 0x72, 0x73, 0x69, 0x6f, 0x6e, 0x20, 0x66, 0x6f, 0x72, 0x20, 0x74, 0x68,
                                    0x69, 0x73, 0x20, 0x73, 0x74, 0x72, 0x69, 0x6e, 0x67, 0xa, 0xd };
        OSD llsdBinary = OSD.FromBinary(contentBinString);
        byte[] binaryBinarySerialized = BinaryLLSDOSDParser.SerializeLLSDBinary(llsdBinary);
        Assert.assertArrayEquals(binaryBinString, binaryBinarySerialized);
    }

    private static byte[] binaryEmptyStringValue = { 0x73, 0x0, 0x0, 0x0, 0x0 };
    private static byte[] binaryEmptyString = (byte[])mergeArrays(binaryHead, binaryEmptyStringValue);
    private static byte[] binaryLongStringValue = { 0x73, 0x0, 0x0, 0x0, 0x25, 
                                                        0x61, 0x62, 0x63, 0x64, 0x65, 0x66,
                                                        0x67, 0x68, 0x69, 0x6a, 0x6b, 0x6c,
                                                        0x6d, 0x6e, 0x6f, 0x70, 0x71, 0x72,
                                                        0x73, 0x74, 0x75, 0x76, 0x77, 0x78,
                                                        0x79, 0x7a, 0x30, 0x31, 0x32, 0x33,
                                                        0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x30 };
    private static byte[] binaryLongString = (byte[])mergeArrays(binaryHead, binaryLongStringValue);

    @Test
    public void DeserializeString() throws IOException, OSDException
    {
        OSD llsdEmptyString = BinaryLLSDOSDParser.DeserializeLLSDBinary(binaryEmptyString);
        Assert.assertEquals(OSDType.String, llsdEmptyString.getType());
        String contentEmptyString = "";
        Assert.assertEquals(contentEmptyString, llsdEmptyString.asString());

        OSD llsdLongString = BinaryLLSDOSDParser.DeserializeLLSDBinary(binaryLongString);
        Assert.assertEquals(OSDType.String, llsdLongString.getType());
        String contentLongString = "abcdefghijklmnopqrstuvwxyz01234567890";
        Assert.assertEquals(contentLongString, llsdLongString.asString());
    }

    @Test
    public void SerializeString() throws Exception
    {
        OSD llsdString = OSD.FromString("abcdefghijklmnopqrstuvwxyz01234567890");
        byte[] binaryLongStringSerialized = BinaryLLSDOSDParser.SerializeLLSDBinary(llsdString);
        Assert.assertArrayEquals(binaryLongString, binaryLongStringSerialized);

        // A test with some utf8 characters
        String contentAStringXML = "<?xml version='1.0'?> <x>&#x196;&#x214;&#x220;&#x228;&#x246;&#x252;</x>";
        byte[] bytes = contentAStringXML.getBytes(Charset.forName("UTF8"));
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(new ByteArrayInputStream(bytes));
        Element rootNode = doc.getDocumentElement();
        System.out.println("Root element " + doc.getDocumentElement().getNodeName());
        rootNode.normalize();
       
        String contentAString = rootNode.getChildNodes().item(0).getNodeValue();
        System.out.println("Node Value:" + contentAString);
        OSD llsdAString = OSD.FromString(contentAString);
        byte[] binaryAString = BinaryLLSDOSDParser.SerializeLLSDBinary(llsdAString);
        OSD llsdAStringDS = BinaryLLSDOSDParser.DeserializeLLSDBinary(binaryAString);
        Assert.assertEquals(OSDType.String, llsdAStringDS.getType());
        Assert.assertEquals(contentAString, llsdAStringDS.asString());

        // we also test for a 4byte character.
        String xml = "<x>&#x10137;</x>";
        byte[] bytesTwo = xml.getBytes(Charset.forName("UTF8"));
        DocumentBuilderFactory dbf2 = DocumentBuilderFactory.newInstance();
        DocumentBuilder db2 = dbf2.newDocumentBuilder();
        Document doc2 = db2.parse(new ByteArrayInputStream(bytes));
        Element rootNode2 = doc2.getDocumentElement();
        rootNode2.normalize();
        String contentAString2 = rootNode2.getChildNodes().item(0).getNodeValue();

        OSD llsdStringOne = OSD.FromString(contentAString2);
        byte[] binaryAStringOneSerialized = BinaryLLSDOSDParser.SerializeLLSDBinary(llsdStringOne);
        OSD llsdStringOneDS = BinaryLLSDOSDParser.DeserializeLLSDBinary(binaryAStringOneSerialized);
        Assert.assertEquals(OSDType.String, llsdStringOneDS.getType());
        Assert.assertEquals(contentAString2, llsdStringOneDS.asString());

    }

    // Be careful. The current and above mentioned reference implementation has a bug that
    // doesnt allow proper binary Uri encoding.
    // We compare here to a fixed version of Uri encoding
    private static byte[] binaryURIValue = { 0x6c, 0x0, 0x0, 0x0, 0x18, // this line is the encoding header
                                0x68, 0x74, 0x74, 0x70, 0x3a, 0x2f, 0x2f, 0x77, 0x77, 0x77, 0x2e, 0x74,
                                0x65, 0x73, 0x74, 0x75, 0x72, 0x6c, 0x2e, 0x74, 0x65, 0x73, 0x74, 0x2f };
    private static byte[] binaryURI = (byte[])mergeArrays(binaryHead, binaryURIValue);

    @Test
    public void DeserializeURI() throws IOException, OSDException, URISyntaxException
    {
        OSD llsdURI = BinaryLLSDOSDParser.DeserializeLLSDBinary(binaryURI);
        Assert.assertEquals(OSDType.URI, llsdURI.getType());
        URI uri = new URI("http://www.testurl.test/");
        Assert.assertEquals(uri, llsdURI.asUri());

    }

    @Test
    public void SerializeURI() throws Exception
    {
        OSD llsdUri = OSD.FromUri(new URI("http://www.testurl.test/"));
        byte[] binaryURISerialized = BinaryLLSDOSDParser.SerializeLLSDBinary(llsdUri);
        Assert.assertArrayEquals(binaryURI, binaryURISerialized);
    }

    // Here is a problem.
    // The reference implementation does serialize to a local timestamp and not to a universal timestamp,
    // which means, this implementation and the reference implementation only work the same in the universal
    // timezone. Therefore this binaryDateTimeValue is generated in the UTC timezone by the reference
    // implementation.
    private static byte[] binaryDateTimeValue = { 100, 0, 0, (byte) 192, (byte)141, (byte)167, (byte)222, (byte)209, 65 };
    
    private static byte[] binaryDateTime = (byte[])mergeArrays(binaryHead, binaryDateTimeValue);

    @Test
    public void DeserializeDateTime() throws IOException, OSDException
    {
        OSD llsdDateTime = BinaryLLSDOSDParser.DeserializeLLSDBinary(binaryDateTime);
        Assert.assertEquals(OSDType.Date, llsdDateTime.getType());
        
    	Calendar cal = Calendar.getInstance();
    	cal.setTimeZone(TimeZone.getTimeZone("UTC"));
    	cal.set(2008, 0, 1, 20, 10, 31);
        cal.set(Calendar.MILLISECOND, 0);
        Date dt = cal.getTime();
        Date dateLocal = llsdDateTime.asDate();
        
//        double t = 9717.36597327148;        
//    	//System.out.print("\t" + Utils.bytesToHexString(bytes, "consumed bytes\t"));
//        System.out.print(Utils.bytesToHexString(Utils.doubleToBytes(t), "Double Bytes Big Endian"));
//        System.out.print(Utils.bytesToHexString(Utils.doubleToBytesLit(t), "Double Bytes Lit Endian"));
        
//        System.out.println(dt.getTime() + ":" + dateLocal.getTime());
        Assert.assertEquals(dt, dateLocal);
    }

    @Test
    public void SerializeDateTime() throws Exception
    {
    	Calendar cal = Calendar.getInstance();
    	cal.setTimeZone(TimeZone.getTimeZone("UTC"));
    	cal.set(2008, 0, 1, 20, 10, 31);
        cal.set(Calendar.MILLISECOND, 0);
    	Date dt = cal.getTime();
        OSD llsdDate = OSD.FromDate(dt);
        byte[] binaryDateSerialized = BinaryLLSDOSDParser.SerializeLLSDBinary(llsdDate);
//    	System.out.println(Utils.bytesToHexString(binaryDateTime, "Original Time"));
//    	System.out.println(Utils.bytesToHexString(binaryDateSerialized, "Serialized Time"));    	
        Assert.assertArrayEquals(binaryDateTime, binaryDateSerialized);

        // check if a *local* time can be serialized and deserialized
    	cal.set(2009, 12, 30, 8, 25, 10);
        Date dtOne = cal.getTime();
        OSD llsdDateOne = OSD.FromDate(dtOne);
        byte[] binaryDateOneSerialized = BinaryLLSDOSDParser.SerializeLLSDBinary(llsdDateOne);
        OSD llsdDateOneDS = BinaryLLSDOSDParser.DeserializeLLSDBinary(binaryDateOneSerialized);
        Assert.assertEquals(OSDType.Date, llsdDateOneDS.getType());
        Assert.assertEquals(dtOne, llsdDateOneDS.asDate());

    	cal.set(2010, 11, 11, 10, 8, 20);
        Date dtTwo = cal.getTime();
        OSD llsdDateTwo = OSD.FromDate(dtTwo);
        byte[] binaryDateTwoSerialized = BinaryLLSDOSDParser.SerializeLLSDBinary(llsdDateTwo);
        OSD llsdDateTwoDS = BinaryLLSDOSDParser.DeserializeLLSDBinary(binaryDateTwoSerialized);
        Assert.assertEquals(OSDType.Date, llsdDateOneDS.getType());
        Assert.assertEquals(dtTwo, llsdDateTwoDS.asDate());
    }

    // Data for empty array { }
    private static byte[] binaryEmptyArrayValue = { 0x5b, 0x0, 0x0, 0x0, 0x0, 0x5d };
    // Encoding header + num of elements + tail
    private static byte[] binaryEmptyArray = (byte[])mergeArrays(binaryHead, binaryEmptyArrayValue);
    // Data for simple array { 0 }
    private static byte[] binarySimpleArrayValue = { 0x5b, 0x0, 0x0, 0x0, 0x1, // Encoding header + num of elements
                                                         0x69, 0x0, 0x0, 0x0, 0x0, 0x5d };
    private static byte[] binarySimpleArray = (byte[])mergeArrays(binaryHead, binarySimpleArrayValue);

    // Data for simple array { 0, 0 }
    private static byte[] binarySimpleArrayTwoValue = { 0x5b, 0x0, 0x0, 0x0, 0x2, // Encoding header + num of elements
                                                         0x69, 0x0, 0x0, 0x0, 0x0, 
                                                         0x69, 0x0, 0x0, 0x0, 0x0, 0x5d };
    private static byte[] binarySimpleArrayTwo = (byte[])mergeArrays(binaryHead, binarySimpleArrayTwoValue);

    @Test
    public void DeserializeArray() throws IOException, OSDException
    {
        OSD llsdEmptyArray = BinaryLLSDOSDParser.DeserializeLLSDBinary(binaryEmptyArray);
        Assert.assertEquals(OSDType.Array, llsdEmptyArray.getType());
        OSDArray llsdEmptyArrayArray = (OSDArray)llsdEmptyArray;
        Assert.assertEquals(0, llsdEmptyArrayArray.count());


        OSD llsdSimpleArray = BinaryLLSDOSDParser.DeserializeLLSDBinary(binarySimpleArray);
        Assert.assertEquals(OSDType.Array, llsdSimpleArray.getType());
        OSDArray llsdArray = (OSDArray)llsdSimpleArray;
        Assert.assertEquals(OSDType.Integer, llsdArray.get(0).getType());
        Assert.assertEquals(0, llsdArray.get(0).asInteger());


        OSD llsdSimpleArrayTwo = BinaryLLSDOSDParser.DeserializeLLSDBinary(binarySimpleArrayTwo);
        Assert.assertEquals(OSDType.Array, llsdSimpleArrayTwo.getType());
        OSDArray llsdArrayTwo = (OSDArray)llsdSimpleArrayTwo;
        Assert.assertEquals(2, llsdArrayTwo.count());

        Assert.assertEquals(OSDType.Integer, llsdArrayTwo.get(0).getType());
        Assert.assertEquals(0, llsdArrayTwo.get(0).asInteger());
        Assert.assertEquals(OSDType.Integer, llsdArrayTwo.get(1).getType());
        Assert.assertEquals(0, llsdArrayTwo.get(1).asInteger());
    }

    @Test
    public void SerializeArray() throws Exception
    {
        OSDArray llsdEmptyArray = new OSDArray();
        byte[] binaryEmptyArraySerialized = BinaryLLSDOSDParser.SerializeLLSDBinary(llsdEmptyArray);
        Assert.assertArrayEquals(binaryEmptyArray, binaryEmptyArraySerialized);

        binaryEmptyArraySerialized = BinaryLLSDOSDParser.SerializeLLSDBinary(llsdEmptyArray, false);
        Assert.assertArrayEquals(binaryEmptyArrayValue, binaryEmptyArraySerialized);

        OSDArray llsdSimpleArray = new OSDArray();
        llsdSimpleArray.add(OSD.FromInteger(0));
        byte[] binarySimpleArraySerialized = BinaryLLSDOSDParser.SerializeLLSDBinary(llsdSimpleArray);
        Assert.assertArrayEquals(binarySimpleArray, binarySimpleArraySerialized);

        binarySimpleArraySerialized = BinaryLLSDOSDParser.SerializeLLSDBinary(llsdSimpleArray, false);
        Assert.assertArrayEquals(binarySimpleArrayValue, binarySimpleArraySerialized);

        OSDArray llsdSimpleArrayTwo = new OSDArray();
        llsdSimpleArrayTwo.add(OSD.FromInteger(0));
        llsdSimpleArrayTwo.add(OSD.FromInteger(0));
        byte[] binarySimpleArrayTwoSerialized = BinaryLLSDOSDParser.SerializeLLSDBinary(llsdSimpleArrayTwo);
        Assert.assertArrayEquals(binarySimpleArrayTwo, binarySimpleArrayTwoSerialized);

        binarySimpleArrayTwoSerialized = BinaryLLSDOSDParser.SerializeLLSDBinary(llsdSimpleArrayTwo, false);
        Assert.assertArrayEquals(binarySimpleArrayTwoValue, binarySimpleArrayTwoSerialized);
    }

    // Data for empty dictionary { }
    private static byte[] binaryEmptyMapValue = { 0x7b, 0x0, 0x0, 0x0, 0x0, 0x7d };
    private static byte[] binaryEmptyMap = (byte[])mergeArrays(binaryHead, binaryEmptyMapValue);

    // Data for simple dictionary { test = 0 }
    private static byte[] binarySimpleMapValue = { 0x7b, 0x0, 0x0, 0x0, 0x1, // Encoding header + num of elements
                                                    0x6b, 0x0, 0x0, 0x0, 0x4, // 'k' + keylength 
                                                    0x74, 0x65, 0x73, 0x74,  // key 'test' 
                                                    0x69, 0x0, 0x0, 0x0, 0x0, // i + '0'
                                                    0x7d };
    private static byte[] binarySimpleMap = (byte[])mergeArrays(binaryHead, binarySimpleMapValue);

    // Data for simple dictionary { t0st = 241, tes1 = "aha", test = undef }
    private static byte[] binarySimpleMapTwoValue = { 0x7b, 0x0, 0x0, 0x0, 0x3, // Encoding header + num of elements
                             0x6b, 0x0, 0x0, 0x0, 0x4, // 'k' + keylength 
                             0x74, 0x65, 0x73, 0x74,  // key 'test'
                             0x21, // undef
                             0x6b, 0x0, 0x0, 0x0, 0x4, // k + keylength 
                             0x74, 0x65, 0x73, 0x31, // key 'tes1' 
                             0x73, 0x0, 0x0, 0x0, 0x3, // String head + length
                             0x61, 0x68, 0x61, // 'aha' 
                             0x6b, 0x0, 0x0, 0x0, 0x4, // k + keylength 
                             0x74, 0x30, 0x73, 0x74,  // key 't0st'
                             0x69, 0x0, 0x0, 0x0, (byte) 0xf1, // integer 241
                             0x7d };
    private static byte[] binarySimpleMapTwo = (byte[])mergeArrays(binaryHead, binarySimpleMapTwoValue);

    @Test
    public void DeserializeDictionary() throws IOException, OSDException
    {
        OSDMap llsdEmptyMap = (OSDMap)BinaryLLSDOSDParser.DeserializeLLSDBinary(binaryEmptyMap);
        Assert.assertEquals(OSDType.Map, llsdEmptyMap.getType());
        Assert.assertEquals(0, llsdEmptyMap.count());

        OSDMap llsdSimpleMap = (OSDMap)BinaryLLSDOSDParser.DeserializeLLSDBinary(binarySimpleMap);
        Assert.assertEquals(OSDType.Map, llsdSimpleMap.getType());
        Assert.assertEquals(1, llsdSimpleMap.count());
        Assert.assertEquals(OSDType.Integer, llsdSimpleMap.get("test").getType());
        Assert.assertEquals(0, llsdSimpleMap.get("test").asInteger());

        OSDMap llsdSimpleMapTwo = (OSDMap)BinaryLLSDOSDParser.DeserializeLLSDBinary(binarySimpleMapTwo);
        Assert.assertEquals(OSDType.Map, llsdSimpleMapTwo.getType());
        Assert.assertEquals(3, llsdSimpleMapTwo.count());
        Assert.assertEquals(OSDType.Unknown, llsdSimpleMapTwo.get("test").getType());
        Assert.assertEquals(OSDType.String, llsdSimpleMapTwo.get("tes1").getType());
        Assert.assertEquals("aha", llsdSimpleMapTwo.get("tes1").asString());
        Assert.assertEquals(OSDType.Integer, llsdSimpleMapTwo.get("t0st").getType());
        Assert.assertEquals(241, llsdSimpleMapTwo.get("t0st").asInteger());
    }

    @Test
    public void SerializeDictionary() throws Exception
    {
        OSDMap llsdEmptyMap = new OSDMap();
        byte[] binaryEmptyMapSerialized = BinaryLLSDOSDParser.SerializeLLSDBinary(llsdEmptyMap);
        Assert.assertArrayEquals(binaryEmptyMap, binaryEmptyMapSerialized);

        OSDMap llsdSimpleMap = new OSDMap();
        llsdSimpleMap.put("test", OSD.FromInteger(0));
        byte[] binarySimpleMapSerialized = BinaryLLSDOSDParser.SerializeLLSDBinary(llsdSimpleMap);
        Assert.assertArrayEquals(binarySimpleMap, binarySimpleMapSerialized);

        OSDMap llsdSimpleMapTwo = new OSDMap();
        llsdSimpleMapTwo.put("t0st", OSD.FromInteger(241));
        llsdSimpleMapTwo.put("tes1", OSD.FromString("aha"));
        llsdSimpleMapTwo.put("test", new OSD());
        byte[] binarySimpleMapTwoSerialized = BinaryLLSDOSDParser.SerializeLLSDBinary(llsdSimpleMapTwo);

        // We dont compare here to the original serialized value, because, as maps dont preserve order,
        // the original serialized value is not *exactly* the same. Instead we compare to a deserialized
        // version created by this deserializer.
        OSDMap llsdSimpleMapDeserialized = (OSDMap)BinaryLLSDOSDParser.DeserializeLLSDBinary(binarySimpleMapTwoSerialized);
        Assert.assertEquals(OSDType.Map, llsdSimpleMapDeserialized.getType());
        Assert.assertEquals(3, llsdSimpleMapDeserialized.count());
        Assert.assertEquals(OSDType.Integer, llsdSimpleMapDeserialized.get("t0st").getType());
        Assert.assertEquals(241, llsdSimpleMapDeserialized.get("t0st").asInteger());
        Assert.assertEquals(OSDType.String, llsdSimpleMapDeserialized.get("tes1").getType());
        Assert.assertEquals("aha", llsdSimpleMapDeserialized.get("tes1").asString());
        Assert.assertEquals(OSDType.Unknown, llsdSimpleMapDeserialized.get("test").getType());

        // we also test for a 4byte key character.
        String xml = "<x>&#x10137;</x>";        
        byte[] bytes = xml.getBytes(Charset.forName("UTF8"));
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(new ByteArrayInputStream(bytes));
        Element rootNode = doc.getDocumentElement();
        rootNode.normalize();
       
        String content = rootNode.getChildNodes().item(0).getNodeValue();
        
        OSDMap llsdSimpleMapThree = new OSDMap();
        OSD llsdSimpleValue = OSD.FromString(content);
        llsdSimpleMapThree.put(content, llsdSimpleValue);
        Assert.assertEquals(content, llsdSimpleMapThree.get(content).asString());

        byte[] binarySimpleMapThree = BinaryLLSDOSDParser.SerializeLLSDBinary(llsdSimpleMapThree);
        OSDMap llsdSimpleMapThreeDS = (OSDMap)BinaryLLSDOSDParser.DeserializeLLSDBinary(binarySimpleMapThree);
        Assert.assertEquals(OSDType.Map, llsdSimpleMapThreeDS.getType());
        Assert.assertEquals(1, llsdSimpleMapThreeDS.count());
        Assert.assertEquals(content, llsdSimpleMapThreeDS.get(content).asString());

    }

    private static byte[] binaryNestedValue = { 0x5b, 0x0, 0x0, 0x0, 0x3, 
                                        0x7b, 0x0, 0x0, 0x0, 0x2, 
                                        0x6b, 0x0, 0x0, 0x0, 0x4, 
                                        0x74, 0x65, 0x73, 0x74, 
                                        0x73, 0x0, 0x0, 0x0, 0x4, 
                                        0x77, 0x68, 0x61, 0x74, 
                                        0x6b, 0x0, 0x0, 0x0, 0x4, 
                                        0x74, 0x30, 0x73, 
                                        0x74, 0x5b, 0x0, 0x0, 0x0, 0x2,
                                        0x69, 0x0, 0x0, 0x0, 0x1,
                                        0x69, 0x0, 0x0, 0x0, 0x2,
                                        0x5d, 0x7d, 0x69, 0x0, 0x0, 0x0, 
                                        0x7c, 0x69, 0x0, 0x0, 0x3, (byte)0xdb, 
                                       0x5d };
    private static byte[] binaryNested = (byte[])mergeArrays(binaryHead, binaryNestedValue);

    @Test
    public void DeserializeNestedComposite() throws IOException, OSDException
    {
        OSD llsdNested = BinaryLLSDOSDParser.DeserializeLLSDBinary(binaryNested);
        Assert.assertEquals(OSDType.Array, llsdNested.getType());
        OSDArray llsdArray = (OSDArray)llsdNested;
        Assert.assertEquals(3, llsdArray.count());

        OSDMap llsdMap = (OSDMap)llsdArray.get(0);
        Assert.assertEquals(OSDType.Map, llsdMap.getType());
        Assert.assertEquals(2, llsdMap.count());

        OSDArray llsdNestedArray = (OSDArray)llsdMap.get("t0st");
        Assert.assertEquals(OSDType.Array, llsdNestedArray.getType());
        OSDInteger llsdNestedIntOne = (OSDInteger)llsdNestedArray.get(0);
        Assert.assertEquals(OSDType.Integer, llsdNestedIntOne.getType());
        Assert.assertEquals(1, llsdNestedIntOne.asInteger());
        OSDInteger llsdNestedIntTwo = (OSDInteger)llsdNestedArray.get(1);
        Assert.assertEquals(OSDType.Integer, llsdNestedIntTwo.getType());
        Assert.assertEquals(2, llsdNestedIntTwo.asInteger());

        OSDString llsdString = (OSDString)llsdMap.get("test");
        Assert.assertEquals(OSDType.String, llsdString.getType());
        Assert.assertEquals("what", llsdString.asString());

        OSDInteger llsdIntOne = (OSDInteger)llsdArray.get(1);
        Assert.assertEquals(OSDType.Integer, llsdIntOne.getType());
        Assert.assertEquals(124, llsdIntOne.asInteger());
        OSDInteger llsdIntTwo = (OSDInteger)llsdArray.get(2);
        Assert.assertEquals(OSDType.Integer, llsdIntTwo.getType());
        Assert.assertEquals(987, llsdIntTwo.asInteger());

    }

    @Test
    public void SerializeNestedComposite() throws Exception
    {
        OSDArray llsdNested = new OSDArray();
        OSDMap llsdMap = new OSDMap();
        OSDArray llsdArray = new OSDArray();
        llsdArray.add(OSD.FromInteger(1));
        llsdArray.add(OSD.FromInteger(2));
        llsdMap.put("t0st", llsdArray);
        llsdMap.put("test", OSD.FromString("what"));
        llsdNested.add(llsdMap);
        llsdNested.add(OSD.FromInteger(124));
        llsdNested.add(OSD.FromInteger(987));

        byte[] binaryNestedSerialized = BinaryLLSDOSDParser.SerializeLLSDBinary(llsdNested);
        // Because maps don't preserve order, we compare here to a deserialized value. 
        OSDArray llsdNestedDeserialized = (OSDArray)BinaryLLSDOSDParser.DeserializeLLSDBinary(binaryNestedSerialized);
        Assert.assertEquals(OSDType.Array, llsdNestedDeserialized.getType());
        Assert.assertEquals(3, llsdNestedDeserialized.count());

        OSDMap llsdMapDeserialized = (OSDMap)llsdNestedDeserialized.get(0);
        Assert.assertEquals(OSDType.Map, llsdMapDeserialized.getType());
        Assert.assertEquals(2, llsdMapDeserialized.count());
        Assert.assertEquals(OSDType.Array, llsdMapDeserialized.get("t0st").getType());

        OSDArray llsdNestedArray = (OSDArray)llsdMapDeserialized.get("t0st");
        Assert.assertEquals(OSDType.Array, llsdNestedArray.getType());
        Assert.assertEquals(2, llsdNestedArray.count());
        Assert.assertEquals(OSDType.Integer, llsdNestedArray.get(0).getType());
        Assert.assertEquals(1, llsdNestedArray.get(0).asInteger());
        Assert.assertEquals(OSDType.Integer, llsdNestedArray.get(1).getType());
        Assert.assertEquals(2, llsdNestedArray.get(1).asInteger());

        Assert.assertEquals(OSDType.String, llsdMapDeserialized.get("test").getType());
        Assert.assertEquals("what", llsdMapDeserialized.get("test").asString());

        Assert.assertEquals(OSDType.Integer, llsdNestedDeserialized.get(1).getType());
        Assert.assertEquals(124, llsdNestedDeserialized.get(1).asInteger());

        Assert.assertEquals(OSDType.Integer, llsdNestedDeserialized.get(2).getType());
        Assert.assertEquals(987, llsdNestedDeserialized.get(2).asInteger());

    }

    @Test
    public void SerializeLongMessage() throws Exception
    {
        // each 80 chars
        String sOne = "asdklfjasadlfkjaerotiudfgjkhsdklgjhsdklfghasdfklhjasdfkjhasdfkljahsdfjklaasdfkj8";
        String sTwo = "asdfkjlaaweoiugsdfjkhsdfg,.mnasdgfkljhrtuiohfglökajsdfoiwghjkdlaaaaseldkfjgheus9";
        
        OSD stringOne = OSD.FromString( sOne );
        OSD stringTwo = OSD.FromString(sTwo);

        OSDMap llsdMap = new OSDMap();
        llsdMap.put("testOne", stringOne);
        llsdMap.put("testTwo", stringTwo);
        llsdMap.put("testThree", stringOne);
        llsdMap.put("testFour", stringTwo);
        llsdMap.put("testFive", stringOne);
        llsdMap.put("testSix", stringTwo);
        llsdMap.put("testSeven", stringOne);
        llsdMap.put("testEight", stringTwo);
        llsdMap.put("testNine", stringOne);
        llsdMap.put("testTen", stringTwo);
        
        
        byte[] binaryData = BinaryLLSDOSDParser.SerializeLLSDBinary( llsdMap );

        OSDMap llsdMapDS = (OSDMap)BinaryLLSDOSDParser.DeserializeLLSDBinary( binaryData );
        Assert.assertEquals( OSDType.Map, llsdMapDS.getType() );
        Assert.assertEquals( 10, llsdMapDS.count() );
        Assert.assertEquals( sOne, llsdMapDS.get("testOne").asString());
        Assert.assertEquals( sTwo, llsdMapDS.get("testTwo").asString());
        Assert.assertEquals( sOne, llsdMapDS.get("testThree").asString());
        Assert.assertEquals( sTwo, llsdMapDS.get("testFour").asString());
        Assert.assertEquals( sOne, llsdMapDS.get("testFive").asString());
        Assert.assertEquals( sTwo, llsdMapDS.get("testSix").asString());
        Assert.assertEquals( sOne, llsdMapDS.get("testSeven").asString());
        Assert.assertEquals( sTwo, llsdMapDS.get("testEight").asString());
        Assert.assertEquals( sOne, llsdMapDS.get("testNine").asString());
        Assert.assertEquals( sTwo, llsdMapDS.get("testTen").asString());
    }

}
