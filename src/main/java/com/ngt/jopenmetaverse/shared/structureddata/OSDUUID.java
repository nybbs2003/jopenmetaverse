package com.ngt.jopenmetaverse.shared.structureddata;

import com.ngt.jopenmetaverse.shared.types.UUID;


// <summary>
/// 
/// </summary>
public final class OSDUUID extends OSD
{
    private UUID value;
    public  final OSDType type = OSDType.UUID; 
    public OSDUUID(UUID value)
    {
        this.value = value;
    }

    @Override
    public OSDType getType()
    {
    	return type;
    }
    
    @Override
    public  boolean asBoolean() { return (value == UUID.Zero) ? false : true; }
    @Override
    public  String asString() { return value.toString(); }
    @Override
    public  UUID asUUID() { return value; }
    @Override
    public  byte[] asBinary() { return value.GetBytes(); }
    @Override
    public  String toString() { return asString(); }
}