package com.ngt.jopenmetaverse.shared.sim;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import junit.framework.Assert;

import com.ngt.jopenmetaverse.shared.sim.events.ThreadPool;
import com.ngt.jopenmetaverse.shared.sim.events.ThreadPoolFactory;

public class UDPServer {

	private  ThreadPool threadPool = ThreadPoolFactory.getThreadPool();

	public void start(final int port) throws Exception
	{
		threadPool.execute(new Runnable()
		{
			public void run()
			{
				pingpong(port);
			}
		});
	}
	
	private void pingpong(int listenport)
	{
		try
		{
//			System.out.println("Starting UDP server on port:" + listenport);
		DatagramSocket serverSocket = new DatagramSocket(listenport);
		byte[] receiveData = new byte[65355];
		byte[] sendData = new byte[65355];
		while(true)
		{
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
//			System.out.println("Going to receive Data");
			serverSocket.receive(receivePacket);
			String sentence = new String( receivePacket.getData());
//			System.out.println("RECEIVED of length" + receivePacket.getLength() + " " + sentence);
			InetAddress IPAddress = receivePacket.getAddress();
			int port = receivePacket.getPort();
//			String capitalizedSentence = sentence.toUpperCase();
			
			sendData = sentence.getBytes();
			DatagramPacket sendPacket =
					new DatagramPacket(sendData, receivePacket.getLength(), IPAddress, port);
			serverSocket.send(sendPacket);
		}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			Assert.fail();
		}
	}
}