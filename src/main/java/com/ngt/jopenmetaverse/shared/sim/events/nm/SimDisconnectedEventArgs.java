package com.ngt.jopenmetaverse.shared.sim.events.nm;

import com.ngt.jopenmetaverse.shared.sim.NetworkManager;
import com.ngt.jopenmetaverse.shared.sim.Simulator;
import com.ngt.jopenmetaverse.shared.sim.events.EventArgs;


public class SimDisconnectedEventArgs extends EventArgs
{
	 private Simulator m_Simulator;
     private NetworkManager.DisconnectType m_Reason;

     public Simulator getSimulator() { return m_Simulator; } 
     public NetworkManager.DisconnectType getReason() { return m_Reason; }

     public SimDisconnectedEventArgs(Simulator simulator, NetworkManager.DisconnectType reason)
     {
         this.m_Simulator = simulator;
         this.m_Reason = reason;
     }
}
