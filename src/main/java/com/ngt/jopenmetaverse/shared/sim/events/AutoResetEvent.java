package com.ngt.jopenmetaverse.shared.sim.events;

public final class AutoResetEvent {
	private final Object monitor = new Object();
	  private volatile boolean open = false;

	  public AutoResetEvent(boolean open) {
	    this.open = open;
	  }

	  public boolean waitOne() throws InterruptedException {
		  return waitOne(0);
	  }

	  public boolean waitOne(long timeout) throws InterruptedException {
		  boolean signalled = false;
		    synchronized (monitor) {
		      while (open == false) { 
		        monitor.wait(timeout);
		        signalled = open;
		      }
		      open = false; // close for other
		    }
		    return signalled;
		  }
	  
	  public void set() {
	    synchronized (monitor) {
	      open = true;
	      monitor.notify(); // open one
	    }
	  }

	  public void reset() {//close stop
	    open = false;
	  }
}
