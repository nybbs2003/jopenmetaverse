package com.ngt.jopenmetaverse.shared.sim.events.am;

import com.ngt.jopenmetaverse.shared.sim.events.EventArgs;

/// <summary>Data sent by the simulator containing urgent messages</summary>
public class AlertMessageEventArgs extends EventArgs
{
	private  String m_Message;

	/// <summary>Get the alert message</summary>
	public String getMessage() {return m_Message;}

	/// <summary>
	/// Construct a new instance of the AlertMessageEventArgs class
	/// </summary>
	/// <param name="message">The alert message</param>
	public AlertMessageEventArgs(String message)
	{
		this.m_Message = message;
	}
}
