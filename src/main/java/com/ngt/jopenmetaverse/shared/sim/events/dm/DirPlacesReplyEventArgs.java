package com.ngt.jopenmetaverse.shared.sim.events.dm;

import java.util.List;

import com.ngt.jopenmetaverse.shared.sim.DirectoryManager;
import com.ngt.jopenmetaverse.shared.sim.events.EventArgs;
import com.ngt.jopenmetaverse.shared.types.UUID;

/// <summary>Contains the places data returned from the data server</summary>
public class DirPlacesReplyEventArgs extends EventArgs
{
	private  UUID m_QueryID;
	/// <summary>The ID returned by <see cref="DirectoryManager.StartDirPlacesSearch"/></summary>
	public UUID getQueryID() { return m_QueryID; } 

	private List<DirectoryManager.DirectoryParcel> m_MatchedParcels;

	/// <summary>A list containing Places data returned by the data server</summary>
	public List<DirectoryManager.DirectoryParcel> getMatchedParcels() { return m_MatchedParcels; } 

	/// <summary>Construct a new instance of the DirPlacesReplyEventArgs class</summary>
	/// <param name="queryID">The ID of the query returned by the data server. 
	/// This will correlate to the ID returned by the <see cref="StartDirPlacesSearch"/> method</param>
	/// <param name="matchedParcels">A list containing land data returned by the data server</param>
	public DirPlacesReplyEventArgs(UUID queryID, List<DirectoryManager.DirectoryParcel> matchedParcels)
	{
		this.m_QueryID = queryID;
		this.m_MatchedParcels = matchedParcels;
	}
}
