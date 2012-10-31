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
package com.ngt.jopenmetaverse.shared.sim;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import com.ngt.jopenmetaverse.shared.sim.buffers.UDPPacketBuffer;
import com.ngt.jopenmetaverse.shared.sim.events.AutoResetEvent;
import com.ngt.jopenmetaverse.shared.util.JLogger;
import com.ngt.jopenmetaverse.shared.util.Utils;

public class UDPBaseTest 
{	
	URL fileLocation;
	@Before
	public void setup() throws Exception
	{
		fileLocation =  getClass().getClassLoader().getResource("data/files/json");
	}

	@Test
	public void UDPServerBasicTest()
	{
		int serverPort = 9786;
		startUDPServer(serverPort);

		UDPPacketBuffer[] bufArray = new UDPPacketBuffer[1];
		InetSocketAddress saddress;
		AutoResetEvent onPacketReceive = new AutoResetEvent(false); 
		try {
			saddress = new InetSocketAddress(Inet4Address.getByName("127.0.0.1"), serverPort);
			UDPBase udp = createUDPBase(saddress, bufArray, onPacketReceive);
			udp.Start();
			byte[] data = Utils.stringToBytes("This is ping message");

			sendAndReceiveData(udp, saddress, bufArray, data, data.length);
			if(!onPacketReceive.waitOne(5000))
			{
				Assert.fail("No Packet Recieved from UDP Server");
			}
		} catch (Exception e) {
			Assert.fail();
			e.printStackTrace();
		}
	}


	@Test
	public void UDPServerMultiDataTest()
	{
		int serverPort = 9787;
		startUDPServer(serverPort);

		UDPPacketBuffer[] bufArray = new UDPPacketBuffer[1];
		InetSocketAddress saddress;
		try {
			AutoResetEvent onPacketReceive = new AutoResetEvent(false); 
			saddress = new InetSocketAddress(Inet4Address.getByName("127.0.0.1"), serverPort);
			UDPBase udp = createUDPBase(saddress, bufArray, onPacketReceive);
			udp.Start();

			File[] files = getFileList(fileLocation.getPath());

			for(File f: files)
			{
				JLogger.debug("Reading from File: " + f.getAbsolutePath());
				byte[] data = getFileBytes(f);
				sendAndReceiveData(udp, saddress, bufArray, data, data.length);
				if(!onPacketReceive.waitOne(5000))
				{
					Assert.fail("No Packet Recieved from UDP Server");
				}			
			}

		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	private File[] getFileList(String dirname)
	{
		JLogger.debug("Try to traverse the directory" + dirname);
		List<File> files = new ArrayList<File>(); 
		File file = new File(dirname); 

		if(file.isDirectory())
		{
			System.out.println("Directory is  " + dirname);
			String str[] = file.list();
			for( int i = 0; i < str.length; i++)
			{
				File f=new File(dirname + "/" + str[i]);
				if(f.isDirectory()){
					System.out.println(str[i] + " is a directory");
				}
				else
				{
					files.add(f);
					System.out.println(str[i] + " is a file");
				}
			}
		}
		return files.toArray(new File[0]);
	}


	private void sendAndReceiveData(UDPBase udp, InetSocketAddress saddress, 
			UDPPacketBuffer[] bufArray, byte[] data, int length)
	{
		UDPPacketBuffer buf = new UDPPacketBuffer(saddress, length);
		Utils.arraycopy(data, 0, buf.getData(), 0, length);
		buf.setDataLength(length);
		JLogger.debug("Sending data to UDP server");
		bufArray[0] = buf;
		udp.AsyncBeginSend(buf);	
	}


	private void startUDPServer(int serverPort)
	{
		UDPServer server = new UDPServer();

		//Start the UDP Server
		try 
		{
			JLogger.debug("Starting UDP server on port: " + serverPort);
			server.start(serverPort);

		} catch (Exception e) {
			Assert.fail();
			e.printStackTrace();
		}
	}


	private UDPBase createUDPBase(InetSocketAddress server, 
			final UDPPacketBuffer[] origBuf, final AutoResetEvent onPacketReceive)
	{
		UDPBase udpBase = new UDPBase(server)
		{
			@Override
			protected void PacketReceived(UDPPacketBuffer buffer) 
			{
				try {					
					JLogger.debug("Data recieved from server: " + Utils.bytesToString(ArrayUtils.subarray(buffer.getData(), 0, buffer.getDataLength())));
					Assert.assertArrayEquals(origBuf[0].getData(), ArrayUtils.subarray(buffer.getData(), 0, buffer.getDataLength()));
				} catch (UnsupportedEncodingException e) {
					Assert.fail();
					e.printStackTrace();
				}
				onPacketReceive.set();
			}

			@Override
			protected void PacketSent(UDPPacketBuffer buffer, int bytesSent) 
			{
				try {
					JLogger.debug("Data Sent to  server: " + Utils.bytesToString(buffer.getData()));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				Assert.assertArrayEquals(origBuf[0].getData(), buffer.getData());
			}
		};

		return udpBase;
	}	

//	private void sleep(int milisec)
//	{
//		try {
//			JLogger.debug("Waiting for UDP Packet.. Going to sleep");
//			Thread.sleep(milisec);
////			Assert.fail("No Message Receive from UDP Server");
//		} catch (InterruptedException e) {
//			JLogger.debug("Coming out of sleep...");
//			e.printStackTrace();
//		}
//	}

	public static byte[] getFileBytes(File file) throws IOException {
		ByteArrayOutputStream ous = null;
		InputStream ios = null;
		try {
			byte[] buffer = new byte[4096];
			ous = new ByteArrayOutputStream();
			ios = new FileInputStream(file);
			int read = 0;
			while ((read = ios.read(buffer)) != -1)
				ous.write(buffer, 0, read);
		} finally {
			try {
				if (ous != null)
					ous.close();
			} catch (IOException e) {
				// swallow, since not that important
			}
			try {
				if (ios != null)
					ios.close();
			} catch (IOException e) {
				// swallow, since not that important
			}
		}
		return ous.toByteArray();
	}

}
