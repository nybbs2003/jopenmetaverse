package com.ngt.jopenmetaverse.shared.sim.events.dm;

import java.util.List;

import com.ngt.jopenmetaverse.shared.sim.DirectoryManager;
import com.ngt.jopenmetaverse.shared.sim.events.EventArgs;
import com.ngt.jopenmetaverse.shared.types.UUID;

/// <summary>Contains the "Event" detail data returned from the data server</summary>
public class DirEventsReplyEventArgs extends EventArgs
{
	private UUID m_QueryID;
	/// <summary>The ID returned by <see cref="DirectoryManager.StartEventsSearch"/></summary>
	public UUID getQueryID() { return m_QueryID; } 

	private List<DirectoryManager.EventsSearchData> m_matchedEvents;

	/// <summary>A list of "Events" returned by the data server</summary>
	public List<DirectoryManager.EventsSearchData> getMatchedEvents() {  return m_matchedEvents; } 

	/// <summary>Construct a new instance of the DirEventsReplyEventArgs class</summary>
	/// <param name="queryID">The ID of the query returned by the data server. 
	/// This will correlate to the ID returned by the <see cref="StartEventsSearch"/> method</param>
	/// <param name="matchedEvents">A list containing the "Events" returned by the search query</param>
	public DirEventsReplyEventArgs(UUID queryID, List<DirectoryManager.EventsSearchData> matchedEvents)
	{
		this.m_QueryID = queryID;
		this.m_matchedEvents = matchedEvents;
	}
}
