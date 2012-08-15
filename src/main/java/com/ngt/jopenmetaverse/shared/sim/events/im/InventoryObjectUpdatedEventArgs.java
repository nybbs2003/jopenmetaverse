package com.ngt.jopenmetaverse.shared.sim.events.im;

import com.ngt.jopenmetaverse.shared.sim.events.EventArgs;
import com.ngt.jopenmetaverse.shared.sim.inventory.InventoryBase;

public class InventoryObjectUpdatedEventArgs extends EventArgs
{
    private InventoryBase m_OldObject;
    private InventoryBase m_NewObject;

    public InventoryBase getOldObject() 
{ return m_OldObject;}
        
    public InventoryBase getNewObject() 
{ return m_NewObject;}
 

    public InventoryObjectUpdatedEventArgs(InventoryBase oldObject, InventoryBase newObject)
    {
        this.m_OldObject = oldObject;
        this.m_NewObject = newObject;
    }
}
