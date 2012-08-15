package com.ngt.jopenmetaverse.shared.sim.events.im;

import com.ngt.jopenmetaverse.shared.sim.events.EventArgs;
import com.ngt.jopenmetaverse.shared.types.UUID;

public class FolderUpdatedEventArgs extends EventArgs
{
	private UUID m_FolderID;
	public UUID getFolderID() {return m_FolderID;}
	private boolean m_Success;
	public boolean getSuccess() {return m_Success;}

	public FolderUpdatedEventArgs(UUID folderID, boolean success)
	{
		this.m_FolderID = folderID;
		this.m_Success = success;
	}
}
