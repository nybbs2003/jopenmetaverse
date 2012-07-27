package com.ngt.jopenmetaverse.shared.sim.events;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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
    
    public void execute(Runnable task)
    {
        threadPool.execute(task);
    }
 
    @Override
    public void shutdown()
    {
        threadPool.shutdown();
    }

	@Override
	public int getQueueSize() {
		return queue.size();
	}

	@Override
	public int getActiveThreadCount() {
		return threadPool.getActiveCount();
	}
}
