package com.ngt.jopenmetaverse.shared.sim.events.gm;

import java.util.List;

import com.ngt.jopenmetaverse.shared.sim.Simulator;
import com.ngt.jopenmetaverse.shared.sim.events.EventArgs;
import com.ngt.jopenmetaverse.shared.types.UUID;


public class CoarseLocationUpdateEventArgs extends EventArgs
{
    private  Simulator m_Simulator;
    private  List<UUID> m_NewEntries;
    private  List<UUID> m_RemovedEntries;

    public Simulator getSimulator() {  return m_Simulator; } 
    public List<UUID> getNewEntries() { return m_NewEntries; } 
    public List<UUID> getRemovedEntries() {return m_RemovedEntries; } 

    public CoarseLocationUpdateEventArgs(Simulator simulator, List<UUID> newEntries, List<UUID> removedEntries)
    {
        this.m_Simulator = simulator;
        this.m_NewEntries = newEntries;
        this.m_RemovedEntries = removedEntries;
    }
}
