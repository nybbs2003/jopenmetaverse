package com.ngt.jopenmetaverse.shared.sim.events.asm;

import com.ngt.jopenmetaverse.shared.sim.events.EventArgs;

public class XferReceivedEventArgs extends EventArgs
{
    private XferDownload m_Xfer;

    /// <summary>Xfer data</summary>
    public XferDownload getXfer() { return m_Xfer; } 

    public XferReceivedEventArgs(XferDownload xfer)
    {
        this.m_Xfer = xfer;
    }
}
