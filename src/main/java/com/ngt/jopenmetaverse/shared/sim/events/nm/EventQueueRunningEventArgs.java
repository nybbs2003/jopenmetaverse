package com.ngt.jopenmetaverse.shared.sim.events.nm;

import com.ngt.jopenmetaverse.shared.sim.Simulator;

public class EventQueueRunningEventArgs 
{
	private  Simulator m_Simulator;

    public Simulator getSimulator() { return m_Simulator; } 

    public EventQueueRunningEventArgs(Simulator simulator)
    {
        this.m_Simulator = simulator;
    }
}
