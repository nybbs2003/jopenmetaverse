package com.ngt.jopenmetaverse.shared.sim.events.am;

import com.ngt.jopenmetaverse.shared.sim.AgentManager.ScriptControlChange;
import com.ngt.jopenmetaverse.shared.sim.events.EventArgs;

/// <summary>Data sent by a script requesting to take or release specified controls to your agent</summary>
public class ScriptControlEventArgs extends EventArgs
{
	private  ScriptControlChange m_Controls;
	private  boolean m_Pass;
	private  boolean m_Take;

	/// <summary>Get the controls the script is attempting to take or release to the agent</summary>
	public ScriptControlChange getControls() {return m_Controls;}
	/// <summary>True if the script is passing controls back to the agent</summary>
	public boolean getPass() {return m_Pass;}
	/// <summary>True if the script is requesting controls be released to the script</summary>
	public boolean getTake() {return m_Take;}

	/// <summary>
	/// Construct a new instance of the ScriptControlEventArgs class
	/// </summary>
	/// <param name="controls">The controls the script is attempting to take or release to the agent</param>
	/// <param name="pass">True if the script is passing controls back to the agent</param>
	/// <param name="take">True if the script is requesting controls be released to the script</param>
	public ScriptControlEventArgs(ScriptControlChange controls, boolean pass, boolean take)
	{
		m_Controls = controls;
		m_Pass = pass;
		m_Take = take;
	}
}

