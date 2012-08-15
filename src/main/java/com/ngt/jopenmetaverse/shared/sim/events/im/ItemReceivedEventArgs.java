package com.ngt.jopenmetaverse.shared.sim.events.im;

import com.ngt.jopenmetaverse.shared.sim.events.EventArgs;
import com.ngt.jopenmetaverse.shared.sim.inventory.InventoryItem;

public class ItemReceivedEventArgs extends EventArgs
{
	private InventoryItem m_Item;

	public InventoryItem getItem() {return m_Item;}

	public ItemReceivedEventArgs(InventoryItem item)
	{
		this.m_Item = item;
	}
}
