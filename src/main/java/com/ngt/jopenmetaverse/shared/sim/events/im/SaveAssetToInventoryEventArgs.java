package com.ngt.jopenmetaverse.shared.sim.events.im;

import com.ngt.jopenmetaverse.shared.sim.events.EventArgs;
import com.ngt.jopenmetaverse.shared.types.UUID;


public class SaveAssetToInventoryEventArgs extends EventArgs
{
	private UUID m_ItemID;
	private UUID m_NewAssetID;

	public UUID getItemID() {return m_ItemID;}
	public UUID getNewAssetID() {return m_NewAssetID;}

	public SaveAssetToInventoryEventArgs(UUID itemID, UUID newAssetID)
	{
		this.m_ItemID = itemID;
		this.m_NewAssetID = newAssetID;
	}
}
