package com.ngt.jopenmetaverse.shared.structureddata;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import com.ngt.jopenmetaverse.shared.structureddata.llsd.BinaryLLSDOSDParser;
import com.ngt.jopenmetaverse.shared.util.Utils;


public class OSDParser
{
    static final String LLSD_BINARY_HEADER = "<? llsd/binary ?>";
    static final String LLSD_XML_HEADER = "<llsd>";
    static final String LLSD_XML_ALT_HEADER = "<?xml";
    static final String LLSD_XML_ALT2_HEADER = "<? llsd/xml ?>";

    //TODO need to implement
    public static OSD Deserialize(byte[] data) throws IOException, OSDException
    {
        String header = Utils.bytesToString(data, 0, data.length >= 17 ? 17 : data.length, "US-ASCII");

        if (header.startsWith(LLSD_BINARY_HEADER))
        {
            return BinaryLLSDOSDParser.DeserializeLLSDBinary(data);
        }
        else if (header.startsWith(LLSD_XML_HEADER) ||
            header.startsWith(LLSD_XML_ALT_HEADER) ||
            header.startsWith(LLSD_XML_ALT2_HEADER))
        {
        	//TODO Need to implement following
//            return DeserializeLLSDXml(data);
        	return null;
        }
        else
        {
        	//TODO need to implement following
//            return DeserializeJson(Encoding.UTF8.GetString(data));
        	return null;

        }
    }

    public static OSD Deserialize(String data) throws UnsupportedEncodingException, IOException, OSDException
    {
        if (data.startsWith(LLSD_BINARY_HEADER, 0))
        {
            return BinaryLLSDOSDParser.DeserializeLLSDBinary(data.getBytes("UTF8"));
        }
        else if (data.startsWith(LLSD_XML_HEADER, 0) ||
            data.startsWith(LLSD_XML_ALT_HEADER, 0) ||
            data.startsWith(LLSD_XML_ALT2_HEADER, 0))
        {
        	//TODO need to implement following
//            return DeserializeLLSDXml(data);
        	return null;
        }
        else
        {
        	//TODO need to implement following
//            return DeserializeJson(data);
        	return null;
        }
    }

    public static OSD Deserialize(InputStream stream) throws OSDException, IOException
    {
        if (stream.markSupported())
        {
            byte[] headerData = new byte[14];
            stream.mark(15);
            stream.read(headerData, 0, 14);
            stream.reset();
            String header = new String(headerData, "US-ASCII");
//            String header = Encoding.ASCII.GetString(headerData);

            if (header.startsWith(LLSD_BINARY_HEADER))
                return BinaryLLSDOSDParser.DeserializeLLSDBinary(stream);
            else if (header.startsWith(LLSD_XML_HEADER) || header.startsWith(LLSD_XML_ALT_HEADER) || header.startsWith(LLSD_XML_ALT2_HEADER))
            {
//                return DeserializeLLSDXml(stream);
            	//TODO need to implement following
            	return null;
            }
            else
            {
//                return DeserializeJson(stream);
            	//TODO need to implement following
            	return null;
            }
        }
        else
        {
            throw new OSDException("Cannot deserialize structured data from unseekable streams");
        }
    }
}