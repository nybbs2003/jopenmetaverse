package com.ngt.jopenmetaverse.shared.structureddata;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import com.ngt.jopenmetaverse.shared.util.Utils;

/// <summary>
/// 
/// </summary>
public final class OSDDate extends OSD
{
  private Date value;

   public  final OSDType type =  OSDType.Date;

  public OSDType getType()
  {
  	return type;
  }
  
  public OSDDate(Date value)
  {
      this.value = value;
  }

  public  String asString()
  {
//      String format;
//      Calendar calendar = Calendar.getInstance();
//      calendar.setTime(value);
//      
//      if (calendar.get(Calendar.MILLISECOND) > 0)
//          format = "yyyy-MM-ddTHH:mm:ss.ffZ";
//      else
//          format = "yyyy-MM-ddTHH:mm:ssZ";
//      SimpleDateFormat formatter = new SimpleDateFormat(format);
      DateTimeFormatter formatter = ISODateTimeFormat.dateTime().withZoneUTC();
//      formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
      return formatter.print(new DateTime(value));
  }

  public  int asInteger()
  {
      return (int) (value.getTime()/1000);
  }

  public  long asLong()
  {
      return (long) value.getTime()/1000;
  }

  public  byte[] asBinary()
  {
	  //TODO Why we need to return Little endian form of double byte array
      return Utils.doubleToBytesLit(1.0*(value.getTime()/1000));
  }

  public  Date asDate() { return value; }
  public  String toString() { return asString(); }
}