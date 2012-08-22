package com.ngt.jopenmetaverse.shared.sim.events;

import java.util.concurrent.atomic.AtomicInteger;

import junit.framework.Assert;

import org.junit.Test;

import com.ngt.jopenmetaverse.shared.util.JLogger;
import com.ngt.jopenmetaverse.shared.util.PlatformUtils;
import com.ngt.jopenmetaverse.shared.util.Utils;

public class ThreadPoolFactoryTests {

	@Test
	public void executeParallelTests()
	{
		final AtomicInteger taskNum1 = new AtomicInteger(30);
		int capacity1 = 5;
		Runnable[] tasks1 = new Runnable[taskNum1.get()];
		for(int i = 0; i < taskNum1.get(); i++)
		{
			tasks1[i] = new Runnable(){
				public void run() {
					try{
					PlatformUtils.sleep(5000);
					taskNum1.decrementAndGet();
					Object a = null;
					a.getClass();
					}
			    	catch(Exception e)
			    	{
			    		JLogger.warn("Exception while running the task: \n" + Utils.getExceptionStackTraceAsString(e));
			    	}
				}
			};
		}
		
		try {
			ThreadPoolFactory.executeParallel(tasks1, capacity1);
		} catch (Exception e) {
			Assert.fail(Utils.getExceptionStackTraceAsString(e));
		}
		
		Assert.assertEquals(0, taskNum1.get());
	}
}
