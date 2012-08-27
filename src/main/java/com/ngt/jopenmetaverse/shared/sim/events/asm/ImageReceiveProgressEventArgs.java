package com.ngt.jopenmetaverse.shared.sim.events.asm;

import com.ngt.jopenmetaverse.shared.sim.events.EventArgs;
import com.ngt.jopenmetaverse.shared.types.UUID;

// <summary>Provides data for ImageReceiveProgress event</summary>
public class ImageReceiveProgressEventArgs extends EventArgs
{
    private UUID m_ImageID;
    private int m_Received;
    private int m_Total;

    /// <summary>UUID of the image that is in progress</summary>
    public UUID getImageID() {return m_ImageID;} 

    /// <summary>Number of bytes received so far</summary>
    public int getReceived() {return m_Received;} 

    /// <summary>Image size in bytes</summary>
    public int getTotal() {return m_Total;} 

    public ImageReceiveProgressEventArgs(UUID imageID, int received, int total)
    {
        this.m_ImageID = imageID;
        this.m_Received = received;
        this.m_Total = total;
    }
}