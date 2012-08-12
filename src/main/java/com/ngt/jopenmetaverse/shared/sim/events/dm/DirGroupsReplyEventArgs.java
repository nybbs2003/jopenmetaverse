package com.ngt.jopenmetaverse.shared.sim.events.dm;

import java.util.List;

import com.ngt.jopenmetaverse.shared.sim.DirectoryManager;
import com.ngt.jopenmetaverse.shared.sim.events.EventArgs;
import com.ngt.jopenmetaverse.shared.types.UUID;

/// <summary>Contains the group data returned from the data server</summary>
public class DirGroupsReplyEventArgs extends EventArgs
{
	private UUID m_QueryID;
	/// <summary>The ID returned by <see cref="DirectoryManager.StartGroupSearch"/></summary>
	public UUID getQueryID() { return m_QueryID; } 

	private List<DirectoryManager.GroupSearchData> m_matchedGroups;

	/// <summary>A list containing Groups data returned by the data server</summary>
	public List<DirectoryManager.GroupSearchData> getMatchedGroups() { return m_matchedGroups; } 

	/// <summary>Construct a new instance of the DirGroupsReplyEventArgs class</summary>
	/// <param name="queryID">The ID of the query returned by the data server. 
	/// This will correlate to the ID returned by the <see cref="StartGroupSearch"/> method</param>
	/// <param name="matchedGroups">A list of groups data returned by the data server</param>
	public DirGroupsReplyEventArgs(UUID queryID, List<DirectoryManager.GroupSearchData> matchedGroups)
	{
		this.m_QueryID = queryID;
		this.m_matchedGroups = matchedGroups;
	}
}
