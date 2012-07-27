package com.ngt.jopenmetaverse.shared.structureddata;

import com.ngt.jopenmetaverse.shared.util.Utils;
import org.apache.commons.codec.binary.Base64;


/// <summary>
/// 
/// </summary>
public final class OSDBinary extends OSD
{
  private byte[] value;

   @Override
  public OSDType getType()
   {
  	return type;
  }
  
  public OSDBinary()
  {
	  super();
	  type =  OSDType.Binary;
  }
   
  public OSDBinary(byte[] value)
  {
	  this();
      if (value != null)
          this.value = value;
      else
          this.value = Utils.EmptyBytes;
  }

  public OSDBinary(int value)
  {
	  this();
      this.value = new byte[]
      {
          (byte)((value >> 24) % 256),
          (byte)((value >> 16) % 256),
          (byte)((value >> 8) % 256),
          (byte)(value % 256)
      };
  }

  
  public OSDBinary(long value)
  {
	  this();
      this.value = new byte[]
      {
          (byte)((value >> 56) % 256),
          (byte)((value >> 48) % 256),
          (byte)((value >> 40) % 256),
          (byte)((value >> 32) % 256),
          (byte)((value >> 24) % 256),
          (byte)((value >> 16) % 256),
          (byte)((value >> 8) % 256),
          (byte)(value % 256)
      };
  }

  public  String asString() { return Base64.encodeBase64String(value); }
  public  byte[] asBinary() { return value; }

  @Override
  public int asInteger()
  {
      return (int) (
          (value[0] << 24) +
          (value[1] << 16) +
          (value[2] << 8) +
          (value[3] << 0));
  }

  @Override
  public  long asLong()
  {
      return (long)(
          ((long)value[0] << 56) +
          ((long)value[1] << 48) +
          ((long)value[2] << 40) +
          ((long)value[3] << 32) +
          ((long)value[4] << 24) +
          ((long)value[5] << 16) +
          ((long)value[6] << 8) +
          ((long)value[7] << 0));
  }

  @Override
  public  String toString()
  {
      return Utils.bytesToHexString(value, null);
  }
}