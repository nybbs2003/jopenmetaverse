package com.ngt.jopenmetaverse.shared.sim.events;

import java.util.Observable;

public class EventObservable<T> extends Observable
{
	public void raiseEvent(T arg)
	{
		setChanged();
		notifyObservers(arg);
	}
}