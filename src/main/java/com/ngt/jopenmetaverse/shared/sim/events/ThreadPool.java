package com.ngt.jopenmetaverse.shared.sim.events;

public interface ThreadPool {
    public void execute(Runnable task);
    public void execute(Runnable[] tasks);
    public int getQueueSize();
    public int getActiveThreadCount();
    public void shutdown();
}
