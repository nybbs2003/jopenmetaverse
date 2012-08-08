package com.ngt.jopenmetaverse.shared.sim.events;

public class ThreadPoolFactory {
	private static ThreadPool threadPool = new DefaultThreadPoolExecutor();
	
	public static ThreadPool getThreadPool()
	{
		return threadPool;
	}
}
