package com.ngt.jopenmetaverse.shared.structureddata;


/// <summary>
/// 
/// </summary>
public final class OSDBoolean extends OSD
{
  private boolean value;

  private static byte[] trueBinary = { 0x31 };
  private static byte[] falseBinary = { 0x30 };

  final public  OSDType type = getType();

  public OSDBoolean(boolean value)
  {
      this.value = value;
  }

  @Override
  public OSDType getType()
  {
	  return OSDType.Boolean;
  }
  
  @Override
  public  boolean asBoolean() { return value; }
  
  @Override
  public  int asInteger() { return value ? 1 : 0; }
  
  @Override
  public  double asReal() { return value ? 1d : 0d; }
  
  @Override
  public  String asString() { return value ? "1" : "0"; }
  
  @Override
  public  byte[] asBinary() { return value ? trueBinary : falseBinary; }

  @Override
  public  String toString() { return asString(); }
}