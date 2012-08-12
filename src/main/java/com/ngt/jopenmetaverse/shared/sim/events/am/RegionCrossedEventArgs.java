package com.ngt.jopenmetaverse.shared.sim.events.am;

import com.ngt.jopenmetaverse.shared.sim.Simulator;
import com.ngt.jopenmetaverse.shared.sim.events.EventArgs;

/// <summary>Data sent to your agent when it crosses region boundaries</summary>
public class RegionCrossedEventArgs extends EventArgs
{
	private  Simulator m_OldSimulator;
	private  Simulator m_NewSimulator;

	/// <summary>Get the simulator your agent just left</summary>
	public Simulator getOldSimulator() {return m_OldSimulator;}
	/// <summary>Get the simulator your agent is now in</summary>
	public Simulator getNewSimulator() {return m_NewSimulator;}

	/// <summary>
	/// Construct a new instance of the RegionCrossedEventArgs class
	/// </summary>
	/// <param name="oldSim">The simulator your agent just left</param>
	/// <param name="newSim">The simulator your agent is now in</param>
	public RegionCrossedEventArgs(Simulator oldSim, Simulator newSim)
	{
		this.m_OldSimulator = oldSim;
		this.m_NewSimulator = newSim;
	}
}
