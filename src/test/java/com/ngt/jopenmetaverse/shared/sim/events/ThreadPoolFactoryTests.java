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
