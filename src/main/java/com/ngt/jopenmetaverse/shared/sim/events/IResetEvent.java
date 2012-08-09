package com.ngt.jopenmetaverse.shared.sim.events;


public interface IResetEvent {
	public void set();
	
	public void reset();
	
	public void waitOne() throws InterruptedException;
	
	public boolean waitOne(int timeout) throws InterruptedException;
	
	public boolean isSignalled();
}
