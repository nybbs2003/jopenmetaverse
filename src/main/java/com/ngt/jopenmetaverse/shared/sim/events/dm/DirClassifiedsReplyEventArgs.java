package com.ngt.jopenmetaverse.shared.sim.events.dm;

import java.util.List;

import com.ngt.jopenmetaverse.shared.sim.DirectoryManager;
import com.ngt.jopenmetaverse.shared.sim.events.EventArgs;

/// <summary>Contains the classified data returned from the data server</summary>
public class DirClassifiedsReplyEventArgs extends EventArgs
{
	private List<DirectoryManager.Classified> m_Classifieds;
	/// <summary>A list containing Classified Ads returned by the data server</summary>
	public List<DirectoryManager.Classified> getClassifieds() { return m_Classifieds; } 

	/// <summary>Construct a new instance of the DirClassifiedsReplyEventArgs class</summary>
	/// <param name="classifieds">A list of classified ad data returned from the data server</param>
	public DirClassifiedsReplyEventArgs(List<DirectoryManager.Classified> classifieds)
	{
		this.m_Classifieds = classifieds;
	}
}
