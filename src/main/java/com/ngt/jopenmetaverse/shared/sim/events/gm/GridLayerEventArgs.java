package com.ngt.jopenmetaverse.shared.sim.events.gm;

import com.ngt.jopenmetaverse.shared.sim.GridManager.GridLayer;
import com.ngt.jopenmetaverse.shared.sim.events.EventArgs;

public class GridLayerEventArgs extends EventArgs
{
    private GridLayer m_Layer;

    public GridLayer getLayer() { return m_Layer; } 

    public GridLayerEventArgs(GridLayer layer)
    {
        this.m_Layer = layer;
    }
}