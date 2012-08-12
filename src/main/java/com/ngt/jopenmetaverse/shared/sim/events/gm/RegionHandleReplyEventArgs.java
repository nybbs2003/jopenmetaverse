package com.ngt.jopenmetaverse.shared.sim.events.gm;

import java.math.BigInteger;

import com.ngt.jopenmetaverse.shared.sim.events.EventArgs;
import com.ngt.jopenmetaverse.shared.types.UUID;


public class RegionHandleReplyEventArgs extends EventArgs
{
    private UUID m_RegionID;
    //ulong
    private BigInteger m_RegionHandle;

    public UUID getRegionID() { return m_RegionID; } 
    public BigInteger getRegionHandle() { return m_RegionHandle; } 

    public RegionHandleReplyEventArgs(UUID regionID, BigInteger regionHandle)
    {
        this.m_RegionID = regionID;
        this.m_RegionHandle = regionHandle;
    }
}
