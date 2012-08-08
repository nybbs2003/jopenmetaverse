package com.ngt.jopenmetaverse.shared.sim.events;

import java.math.BigInteger;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import junit.framework.Assert;

import org.junit.Test;

import com.ngt.jopenmetaverse.shared.protocol.PacketType;
import com.ngt.jopenmetaverse.shared.sim.GridClient;
import com.ngt.jopenmetaverse.shared.sim.Settings;
import com.ngt.jopenmetaverse.shared.sim.Simulator;

public class PacketEventDictionaryTest {
	
	private final class SimpleTestObserver implements Observer
	{
		public int called = 0;
		
		public void update(Observable o, Object arg) {
				called += 1;
		}
		
	}
	
	/*
	 * Test for Asynchronously creating events. In this case, PacketEventDictionary will start a new thread
	 * for every event is fired
	 * 
	 */
	@Test
	public void createEventAsyncTests() throws InterruptedException
	{
		createAndRaisePacketEventDictionary(1, true, 5000);
		createAndRaisePacketEventDictionary(50, true, 5000);
		createAndRaisePacketEventDictionary(10000, true, 5000);
	}

	@Test
	public void createEventTests() throws InterruptedException
	{
		createAndRaisePacketEventDictionary(1, false, 0);
		createAndRaisePacketEventDictionary(50, false, 0);
		createAndRaisePacketEventDictionary(10000, false, 0);
	}
	
	@Test
	public void deleteEventTests() throws InterruptedException
	{
		//TODO Need to implement
//		createAndRaisePacketEventDictionary(1, false, 0);
//		createAndRaisePacketEventDictionary(50, false, 0);
//		createAndRaisePacketEventDictionary(10000, false, 0);		
	}	
	
	
	private void createAndRaisePacketEventDictionary(int observerListNum, boolean isAsync, int timeout)
	{
		GridClient gd = new GridClient();
		Simulator sim = new Simulator(gd, new InetSocketAddress(Settings.BIND_ADDR, 4444), new BigInteger("9999"));
		
		/* Add few Observers and raise event*/
		List<SimpleTestObserver> observerList1 = new ArrayList<SimpleTestObserver>();
		PacketEventDictionary pvd2 = new PacketEventDictionary(gd);
		for(int i = 0; i < observerListNum; i ++)
		{
			SimpleTestObserver o = new SimpleTestObserver();
			observerList1.add(o);
			pvd2.RegisterEvent(PacketType.AcceptFriendship, o, isAsync);
		}
		
		pvd2.RaiseEvent(PacketType.AcceptFriendship, null, sim);
		
		if(isAsync)
			sleep(timeout);
		
		for(SimpleTestObserver o: observerList1)
		{
			Assert.assertTrue(o.called ==1);
		}
	}
	
	private void sleep(int timeout)
	{
		try {
			Thread.sleep(timeout);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}
