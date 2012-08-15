package com.ngt.jopenmetaverse.shared.sim.events.asm;

import com.ngt.jopenmetaverse.shared.types.Enums;
import com.ngt.jopenmetaverse.shared.types.UUID;
import com.ngt.jopenmetaverse.shared.util.Utils;

/// <summary>
/// 
/// </summary>
public class Transfer
{
    public UUID ID;
    public int Size;
    public byte[] AssetData = Utils.EmptyBytes;
    //int
    public long Transferred;
    public boolean Success;
    public Enums.AssetType AssetType;

    //int
    private long transferStart;

    /// <summary>Number of milliseconds passed since the last transfer
    /// packet was received</summary>
    public long getTimeSinceLastPacket()
    {
        return Utils.getUnixTime() - transferStart; 
    }
    public void setTimeSinceLastPacket(long value)
    {
        transferStart = Utils.getUnixTime() + value; 
    }

    public Transfer()
    {
        AssetData = Utils.EmptyBytes;
        transferStart = Utils.getUnixTime();
    }
}