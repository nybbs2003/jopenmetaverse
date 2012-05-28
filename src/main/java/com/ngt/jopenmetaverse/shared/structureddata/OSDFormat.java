package com.ngt.jopenmetaverse.shared.structureddata;

public enum OSDFormat
{
    Xml (0),
    Json (1),
    Binary (2);
    
    private int index;
    
    OSDFormat(int index)
    {
    	this.index = index;
    }
    
    public int getIndex()
    {
    	return index;
    }
}