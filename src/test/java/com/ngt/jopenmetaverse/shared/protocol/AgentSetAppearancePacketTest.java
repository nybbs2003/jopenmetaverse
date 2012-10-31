/**
 * A library to interact with Virtual Worlds such as OpenSim
 * Copyright (C) 2012  Jitendra Chauhan, Email: jitendra.chauhan@gmail.com
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License,
 * or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation,
 * Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package com.ngt.jopenmetaverse.shared.protocol;

import java.io.File;
import java.net.URL;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.ngt.jopenmetaverse.shared.protocol.primitives.TextureEntry;
import com.ngt.jopenmetaverse.shared.structureddata.llsd.JsonLLSDOSDParser;
import com.ngt.jopenmetaverse.shared.util.FileUtils;
import com.ngt.jopenmetaverse.shared.util.JLogger;
import com.ngt.jopenmetaverse.shared.util.Utils;

public class AgentSetAppearancePacketTest {
	
	URL fileLocation;
	
	@Before
	public void setup()
	{
		fileLocation =  getClass().getClassLoader().getResource("data/files/packets/binary/");
	}
	
	@Test
	public void fromBytesTests()
	{
		try
		{
			File[] files = FileUtils.getFileList(fileLocation.getPath(), false);
			for(File f : files)
			{
			
				if(matchPacket(AgentSetAppearancePacket.class.getSimpleName(), f.getName()))
				{
					byte[] data = FileUtils.readBytes(f);
//					
//				AgentSetAppearancePacket pkt 
//					=  (AgentSetAppearancePacket) getClass(f.getName()).newInstance();
					byte[] buffer = null;
					int buflen = 0;
					if( (data[0] & ~Helpers.MSG_ZEROCODED) !=0)
					{
						buffer = new byte[Packet.MTU]; 
						buflen = Helpers.ZeroEncode(data, data.length, buffer);
					}
					else
					{
						buffer = data;
						buflen = buffer.length;
					}
					
					int[] packetEnd = new int[]{buflen - 1};
					
					AgentSetAppearancePacket appear = (AgentSetAppearancePacket) Packet.BuildPacket(buffer, packetEnd, ((buffer[0] & Helpers.MSG_ZEROCODED) != 0) ? new byte[8192] : null);
					
                    byte[] visualparams = new byte[appear.VisualParam.length];
                    for (int i = 0; i < appear.VisualParam.length; i++)
                        visualparams[i] = appear.VisualParam[i].ParamValue;

                    TextureEntry te = null;
                    System.out.println(Utils.bytesToHexDebugString(appear.ObjectData.TextureEntry, ""));	                    

                    if (appear.ObjectData.TextureEntry.length > 1)
                    {
                        te = new TextureEntry(appear.ObjectData.TextureEntry, 0, appear.ObjectData.TextureEntry.length);
                        byte[] serializedTextureEntry = JsonLLSDOSDParser.SerializeLLSDJsonBytes(te.GetOSD());
                        System.out.println(Utils.bytesToString(serializedTextureEntry));
                    }
				}
				else
					JLogger.debug("Ignorning file " + f.getName());
			}
			
		}
		catch(Exception e)
		{
			Assert.fail(Utils.getExceptionStackTraceAsString(e));
		}
	}
	
	
	private boolean matchPacket(String className, String filename) 
	{
		JLogger.debug(className + ":" + filename + " " + filename.toLowerCase().contains(className.toLowerCase()));
		return filename.toLowerCase().contains(className.toLowerCase());
//			filename.split("_");
//			Class<?> theClass  = Class.forName(filename.split("_")[0]);
//			return theClass;
	
	}
}
