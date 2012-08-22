package com.ngt.jopenmetaverse.shared.sim.events;

import java.util.Timer;
import java.util.TimerTask;

public class EventTimer {

	private Timer timer;
	private TimerTask task;
	private long delay;
	
	public EventTimer(TimerTask task)
	{
		timer = new Timer();
		this.task=task;
	}

	public void schedule(long delay)
	{
		timer.schedule(task, delay);
	}

	public void schedule(long delay, int interval)
	{
		timer.schedule(task, delay, interval);
	}
	
	public void cancel()
	{
		timer.cancel();
	}
	
	public void reschedule(long delay)
	{
		cancel();
		schedule(delay);
	}
	
	public void reschedule(long delay, int interval)
	{
		cancel();
		schedule(delay, interval);
	}
}
