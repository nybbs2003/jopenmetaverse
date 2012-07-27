package com.ngt.jopenmetaverse.shared.structureddata;

import java.net.URI;
import com.ngt.jopenmetaverse.shared.util.Utils;


/// <summary>
/// 
/// </summary>
public final class OSDUri extends OSD
{
    private URI value;

     @Override
    public OSDType getType()
    {
    	return type;
    }

     public OSDUri()
     {
    	 super();
    	 type  = OSDType.URI;
     }
     
    public OSDUri(URI value)
    {
    	this();
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