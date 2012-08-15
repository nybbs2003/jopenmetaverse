package com.ngt.jopenmetaverse.shared.sim.events.im;

import com.ngt.jopenmetaverse.shared.sim.events.EventArgs;
import com.ngt.jopenmetaverse.shared.types.UUID;

public class FindObjectByPathReplyEventArgs extends EventArgs
{
	private String m_Path;
	private UUID m_InventoryObjectID;

	public String getPath() {return m_Path;}
	public UUID getInventoryObjectID() {return m_InventoryObjectID;}

	public FindObjectByPathReplyEventArgs(String path, UUID inventoryObjectID)
	{
		this.m_Path = path;
		this.m_InventoryObjectID = inventoryObjectID;
	}
}
