package com.ngt.jopenmetaverse.shared.structureddata;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.Charset;

import com.ngt.jopenmetaverse.shared.util.Utils;


/// <summary>
/// 
/// </summary>
public final class OSDUri extends OSD
{
    private URI value;

     public  final OSDType type  = OSDType.URI;

     @Override
    public OSDType getType()
    {
    	return type;
    }
    
    public OSDUri(URI value)
    {
        this.value = value;
    }

    public  String asString()
    {
        if (value != null)
        {
            return value.toString();

//        	try {
//				return URLDecoder.decode(value.toString(), "UTF8");
//			} catch (UnsupportedEncodingException e) {
//				return "";
//			}
            
//            if (value.isAbsolute())
//                return value.toString();
//            else
//                return value.toString();
        }
        return "";
    }

    @Override
    public  URI asUri() { return value; }
    @Override
    public  byte[] asBinary() { return Utils.stringToBytes(asString()); }
    @Override
    public  String toString() { return asString(); }
}