package com.ngt.jopenmetaverse.shared.sim.events.dm;

import java.util.List;

import com.ngt.jopenmetaverse.shared.sim.DirectoryManager;
import com.ngt.jopenmetaverse.shared.sim.events.EventArgs;

/// <summary>Contains the land sales data returned from the data server</summary>
public class DirLandReplyEventArgs extends EventArgs
{
	private List<DirectoryManager.DirectoryParcel> m_DirParcels;

	/// <summary>A list containing land forsale data returned by the data server</summary>
	public List<DirectoryManager.DirectoryParcel> getDirParcels() { return m_DirParcels; } 

	/// <summary>Construct a new instance of the DirLandReplyEventArgs class</summary>
	/// <param name="dirParcels">A list of parcels for sale returned by the data server</param>
	public DirLandReplyEventArgs(List<DirectoryManager.DirectoryParcel> dirParcels)
	{
		this.m_DirParcels = dirParcels;
	}
}
