package com.ngt.jopenmetaverse.shared.sim.events;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.ngt.jopenmetaverse.shared.util.JLogger;
import com.ngt.jopenmetaverse.shared.util.Utils;

public class DefaultThreadPoolExecutor implements ThreadPool {
	int poolSize = 20;
    int maxPoolSize = 1000;
    long keepAliveTime = 10;
    ThreadPoolExecutor threadPool;
    ArrayBlockingQueue<Runnable> queue;
 
    public DefaultThreadPoolExecutor()
    {
        this(20, 1000, 10, TimeUnit.SECONDS, 10000);
    }
 
    public DefaultThreadPoolExecutor(int corePoolSize, int maximumPoolSize, 
    		long keepAliveTime, TimeUnit unit, int capacity)
    {
    	this.poolSize =  corePoolSize;
    	this.maxPoolSize = maximumPoolSize;
        this.keepAliveTime = keepAliveTime;
    	queue = new ArrayBlockingQueue<Runnable>(capacity);
    	threadPool = new ThreadPoolExecutor(corePoolSize, maximumPoolSize,
                keepAliveTime, unit, queue);
    }
    
    //FIXME Need to find a way to catch the exception and save the thread..
    public void execute(Runnable task)
    {
    	try{
        threadPool.execute(task);
    	}
    	catch(Exception e)
    	{
    		JLogger.debug("Exception while executing the task: " + Utils.getExceptionStackTraceAsString(e));
    	}
    }
 
    public void execute(Runnable[] tasks)
    {
    	for(Runnable task: tasks)
    		execute(task);
    }    
    
    public void shutdown()
    {
        threadPool.shutdown();
        queue.clear();
        queue = null;
    }

	public int getQueueSize() {
		return queue.size();
	}

	public int getActiveThreadCount() {
		return threadPool.getActiveCount();
	}
}
