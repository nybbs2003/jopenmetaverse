package com.ngt.jopenmetaverse.shared.sim.events;

import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import com.ngt.jopenmetaverse.shared.util.JLogger;

public class ThreadPoolFactory {
	private static ThreadPool threadPool = new DefaultThreadPoolExecutor();
	
	public static ThreadPool getThreadPool()
	{
		return threadPool;
	}
	
	public static ThreadPool getNewInstance(int corePoolSize, int maximumPoolSize, 
    		long keepAliveTime, TimeUnit unit, int capacity)
	{
		return new DefaultThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, 
				unit, capacity);
	}	
	
	public static void executeParallel(Runnable[] tasks, int capacity) throws InterruptedException
	{
		//FIXME a dirty implement of executing 
		final ThreadPool pool = new DefaultThreadPoolExecutor(capacity, capacity, 10, 
				TimeUnit.SECONDS, tasks.length);
		final AutoResetEvent event = new AutoResetEvent(false); 
		final EventTimer timer = new EventTimer(new TimerTask(){
			@Override
			public void run() {
				JLogger.debug("Waiting for parallel tasks to get finished: Queue Size: " + pool.getQueueSize());
				if(pool.getActiveThreadCount() ==0 && pool.getQueueSize() ==0 )
				{
					pool.shutdown();
					this.cancel();
					JLogger.debug("All the tasks got finished...");
					event.set();
				}
			}
		});
		JLogger.debug("Start executing the tasks...");		
		pool.execute(tasks);
		timer.schedule(1000, 1000);
		event.waitOne();
		JLogger.debug("Execute Parallel returing...");
	}
}
