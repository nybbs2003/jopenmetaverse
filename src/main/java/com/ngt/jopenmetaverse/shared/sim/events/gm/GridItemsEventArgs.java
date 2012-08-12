package com.ngt.jopenmetaverse.shared.sim.events.gm;

import java.util.List;

import com.ngt.jopenmetaverse.shared.sim.GridManager.GridItemType;
import com.ngt.jopenmetaverse.shared.sim.GridManager.MapItem;
import com.ngt.jopenmetaverse.shared.sim.events.EventArgs;

public class GridItemsEventArgs extends EventArgs
{
    private  GridItemType m_Type;
    private  List<MapItem> m_Items;

    public GridItemType getType() { return m_Type; } 
    public List<MapItem> getItems() { return m_Items; } 

    public GridItemsEventArgs(GridItemType type, List<MapItem> items)
    {
        this.m_Type = type;
        this.m_Items = items;
    }
}
