package com.ngt.jopenmetaverse.shared.sim.events.gm;

import com.ngt.jopenmetaverse.shared.sim.GridManager.GridRegion;
import com.ngt.jopenmetaverse.shared.sim.events.EventArgs;


public class GridRegionEventArgs extends EventArgs
{
    private GridRegion m_Region;
    public GridRegion getRegion() {  return m_Region; } 

    public GridRegionEventArgs(GridRegion region)
    {
        this.m_Region = region;
    }
}
