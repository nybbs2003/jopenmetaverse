package com.ngt.jopenmetaverse.shared.sim.events;

import java.util.Observable;
import java.util.Observer;


public abstract class EventObserver<T> implements Observer
{	
	public abstract void handleEvent(Observable o, T arg);

	public void update(Observable o, Object arg) {
		T t = (T)arg;
		handleEvent(o, t);
	}
}