package com.ngt.jopenmetaverse.shared.sim.events.im;

import com.ngt.jopenmetaverse.shared.sim.events.EventArgs;
import com.ngt.jopenmetaverse.shared.types.UUID;

public class TaskInventoryReplyEventArgs extends EventArgs
{
	private UUID m_ItemID;
	//Int16
	private short m_Serial;
	private String m_AssetFilename;

	public UUID getItemID() {return m_ItemID;}
	public short getSerial() {return m_Serial;}
	public String getAssetFilename() {return m_AssetFilename;}

	public TaskInventoryReplyEventArgs(UUID itemID, short serial, String assetFilename)
	{
		this.m_ItemID = itemID;
		this.m_Serial = serial;
		this.m_AssetFilename = assetFilename;
	}
}
