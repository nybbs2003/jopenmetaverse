package com.ngt.jopenmetaverse.shared.structureddata;

import com.ngt.jopenmetaverse.shared.util.Utils;


/// <summary>
/// 
/// </summary>
public final class OSDInteger extends OSD
{
    private int value;

    public  OSDType type = OSDType.Integer; 

    public OSDInteger(int value)
    {
        this.value = value;
    }

    @Override
    public OSDType getType()
    { 
    	return OSDType.Integer;	
    }
    
    @Override
    public  boolean asBoolean() { return value != 0; }
    
    @Override
    public  int asInteger() { return value; }
//    public  uint asUInteger() { return (uint)value; }
    
    @Override
    public  long asLong() { return value; }
//    public  ulong asULong() { return (ulong)value; }
    
    @Override
    public  double asReal() { return (double)value; }
    
    @Override
    public  String asString() { return Integer.toString(value); }
    
    @Override
    public  byte[] asBinary() { return Utils.intToBytes(value); }
    
    @Override
    public  String toString() { return asString(); }
}