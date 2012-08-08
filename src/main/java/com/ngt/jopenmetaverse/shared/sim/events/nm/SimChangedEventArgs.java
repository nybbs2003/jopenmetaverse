package com.ngt.jopenmetaverse.shared.sim.events.nm;

import com.ngt.jopenmetaverse.shared.sim.Simulator;

public class SimChangedEventArgs 
{
	 private Simulator m_PreviousSimulator;

     public Simulator getPreviousSimulator() { return m_PreviousSimulator; } 

     public SimChangedEventArgs(Simulator previousSimulator)
     {
         this.m_PreviousSimulator = previousSimulator;
     }
}
