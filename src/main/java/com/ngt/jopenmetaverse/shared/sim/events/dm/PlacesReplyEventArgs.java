package com.ngt.jopenmetaverse.shared.sim.events.dm;

import java.util.List;

import com.ngt.jopenmetaverse.shared.sim.DirectoryManager;
import com.ngt.jopenmetaverse.shared.sim.events.EventArgs;
import com.ngt.jopenmetaverse.shared.types.UUID;

/// <summary>Contains the "Event" list data returned from the data server</summary>
public class PlacesReplyEventArgs extends EventArgs
{
	private UUID m_QueryID;
	/// <summary>The ID returned by <see cref="DirectoryManager.StartPlacesSearch"/></summary>
	public UUID getQueryID() { return m_QueryID; } 

	private List<DirectoryManager.PlacesSearchData> m_MatchedPlaces;

	/// <summary>A list of "Places" returned by the data server</summary>
	public List<DirectoryManager.PlacesSearchData> getMatchedPlaces() { return m_MatchedPlaces; } 

	/// <summary>Construct a new instance of PlacesReplyEventArgs class</summary>
	/// <param name="queryID">The ID of the query returned by the data server. 
	/// This will correlate to the ID returned by the <see cref="StartPlacesSearch"/> method</param>
	/// <param name="matchedPlaces">A list containing the "Places" returned by the data server query</param>
	public PlacesReplyEventArgs(UUID queryID, List<DirectoryManager.PlacesSearchData> matchedPlaces)
	{
		this.m_QueryID = queryID;
		this.m_MatchedPlaces = matchedPlaces;
	}
}
