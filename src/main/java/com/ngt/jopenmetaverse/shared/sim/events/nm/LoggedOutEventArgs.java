package com.ngt.jopenmetaverse.shared.sim.events.nm;

import java.util.List;

import com.ngt.jopenmetaverse.shared.sim.events.EventArgs;
import com.ngt.jopenmetaverse.shared.types.UUID;

public class LoggedOutEventArgs extends EventArgs 
{
	 private  List<UUID> m_InventoryItems;
     public List<UUID> getInventoryItems() {return m_InventoryItems;};

     public LoggedOutEventArgs(List<UUID> inventoryItems)
     {
         this.m_InventoryItems = inventoryItems;
     }
}
