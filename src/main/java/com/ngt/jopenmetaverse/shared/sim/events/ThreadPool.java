package com.ngt.jopenmetaverse.shared.sim.events;

public interface ThreadPool {
    public void execute(Runnable task);
    public int getQueueSize();
    public int getActiveThreadCount();
    public void shutdown();
}
