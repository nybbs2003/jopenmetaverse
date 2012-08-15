package com.ngt.jopenmetaverse.shared.sim.events.im;

import com.ngt.jopenmetaverse.shared.sim.events.EventArgs;
import com.ngt.jopenmetaverse.shared.sim.inventory.InventoryBase;

public class InventoryObjectAddedEventArgs extends EventArgs
{
	private InventoryBase m_Obj;

	public InventoryBase getObj() 
	{ return m_Obj;}

	public InventoryObjectAddedEventArgs(InventoryBase obj)
	{
		this.m_Obj = obj;
	}
}
