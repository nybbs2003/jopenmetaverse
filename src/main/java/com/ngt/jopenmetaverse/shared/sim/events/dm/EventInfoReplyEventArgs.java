package com.ngt.jopenmetaverse.shared.sim.events.dm;

import com.ngt.jopenmetaverse.shared.sim.DirectoryManager;
import com.ngt.jopenmetaverse.shared.sim.events.EventArgs;

/// <summary>Contains the Event data returned from the data server from an EventInfoRequest</summary>
public class EventInfoReplyEventArgs extends EventArgs
{
	private DirectoryManager.EventInfo m_MatchedEvent;

	/// <summary>
	/// A single EventInfo object containing the details of an event
	/// </summary>
	public DirectoryManager.EventInfo getMatchedEvent() { return m_MatchedEvent; } 

	/// <summary>Construct a new instance of the EventInfoReplyEventArgs class</summary>
	/// <param name="matchedEvent">A single EventInfo object containing the details of an event</param>
	public EventInfoReplyEventArgs(DirectoryManager.EventInfo matchedEvent)
	{
		this.m_MatchedEvent = matchedEvent;
	}        
}
