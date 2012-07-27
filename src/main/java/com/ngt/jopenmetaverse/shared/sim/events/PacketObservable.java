package com.ngt.jopenmetaverse.shared.sim.events;

import java.util.Observable;

public class PacketObservable extends Observable
{
	public void raiseEvent(Object arg)
	{
		setChanged();
		notifyObservers(arg);
	}
}