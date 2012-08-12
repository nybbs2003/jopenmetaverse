package com.ngt.jopenmetaverse.shared.sim.events.am;

import com.ngt.jopenmetaverse.shared.sim.Simulator;
import com.ngt.jopenmetaverse.shared.sim.AgentManager.InstantMessage;
import com.ngt.jopenmetaverse.shared.sim.events.EventArgs;

/// <summary>The date received from an ImprovedInstantMessage</summary>
public class InstantMessageEventArgs extends EventArgs
{
	private  InstantMessage m_IM;
	private  Simulator m_Simulator;

	/// <summary>Get the InstantMessage object</summary>
	public InstantMessage getIM() { return m_IM; } 
	/// <summary>Get the simulator where the InstantMessage origniated</summary>
	public Simulator getSimulator() { return m_Simulator; } 

	/// <summary>
	/// Construct a new instance of the InstantMessageEventArgs object
	/// </summary>
	/// <param name="im">the InstantMessage object</param>
	/// <param name="simulator">the simulator where the InstantMessage origniated</param>
	public InstantMessageEventArgs(InstantMessage im, Simulator simulator)
	{
		this.m_IM = im;
		this.m_Simulator = simulator;
	}
}
