package com.ngt.jopenmetaverse.shared.structureddata;

import com.ngt.jopenmetaverse.shared.util.Utils;


/// <summary>
/// 
/// </summary>
public final class OSDReal extends OSD
{
    private double value;

    public final OSDType type = OSDType.Real;

    public OSDReal(double value)
    {
        this.value = value;
    }

    public OSDType getType()
    {
    	return type;
    }
    
    @Override
    public  boolean asBoolean() { return (!Double.isNaN(value) && value != 0d); }
    
    @Override
    public  int asInteger()
    {
        if (Double.isNaN(value))
            return 0;
        if (value > (double)Integer.MAX_VALUE)
            return Integer.MAX_VALUE;
        if (value < (double)Integer.MIN_VALUE)
            return Integer.MIN_VALUE;
        return (int)Math.round(value);
    }

//    public  uint AsUInteger()
//    {
//        if (Double.IsNaN(value))
//            return 0;
//        if (value > (double)UInt32.MaxValue)
//            return UInt32.MaxValue;
//        if (value < (double)UInt32.MinValue)
//            return UInt32.MinValue;
//        return (uint)Math.Round(value);
//    }

    @Override
    public  long asLong()
    {
        if (Double.isNaN(value))
            return 0;
        if (value > (double)Long.MAX_VALUE)
            return Long.MAX_VALUE;
        if (value < (double)Long.MIN_VALUE)
            return Long.MIN_VALUE;
        return (long)Math.round(value);
    }

//    public  ulong AsULong()
//    {
//        if (Double.IsNaN(value))
//            return 0;
//        if (value > (double)UInt64.MaxValue)
//            return Int32.MaxValue;
//        if (value < (double)UInt64.MinValue)
//            return UInt64.MinValue;
//        return (ulong)Math.Round(value);
//    }

    @Override
    public  double asReal() { return value; }
    // "r" ensures the value will correctly round-trip back through Double.TryParse
    @Override
    public  String asString() { return Double.toString(value); }
    @Override
    public  byte[] asBinary() { return Utils.doubleToBytes(value); }
    @Override
    public  String toString() { return asString(); }
}