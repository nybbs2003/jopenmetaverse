package com.ngt.jopenmetaverse.shared.sim.events.dm;

import java.util.List;

import com.ngt.jopenmetaverse.shared.sim.DirectoryManager;
import com.ngt.jopenmetaverse.shared.sim.events.EventArgs;
import com.ngt.jopenmetaverse.shared.types.UUID;

/// <summary>Contains the people data returned from the data server</summary>
public class DirPeopleReplyEventArgs extends EventArgs
{
	private UUID m_QueryID;
	/// <summary>The ID returned by <see cref="DirectoryManager.StartPeopleSearch"/></summary>
	public UUID getQueryID() { return m_QueryID; } 

	private List<DirectoryManager.AgentSearchData> m_MatchedPeople;

	/// <summary>A list containing People data returned by the data server</summary>
	public List<DirectoryManager.AgentSearchData> getMatchedPeople() { return m_MatchedPeople; } 

	/// <summary>Construct a new instance of the DirPeopleReplyEventArgs class</summary>
	/// <param name="queryID">The ID of the query returned by the data server. 
	/// This will correlate to the ID returned by the <see cref="StartPeopleSearch"/> method</param>
	/// <param name="matchedPeople">A list of people data returned by the data server</param>
	public DirPeopleReplyEventArgs(UUID queryID, List<DirectoryManager.AgentSearchData> matchedPeople)
	{
		this.m_QueryID = queryID;
		this.m_MatchedPeople = matchedPeople;
	}
}
