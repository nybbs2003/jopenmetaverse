package com.ngt.jopenmetaverse.shared.sim.events.nm;

import com.ngt.jopenmetaverse.shared.sim.Simulator;
import com.ngt.jopenmetaverse.shared.sim.events.EventArgs;

public class SimConnectedEventArgs extends EventArgs
{
	 private  Simulator m_Simulator;
     public Simulator getSimulator() { return m_Simulator; } 

     public SimConnectedEventArgs(Simulator simulator)
     {
         this.m_Simulator = simulator;
     }
}
