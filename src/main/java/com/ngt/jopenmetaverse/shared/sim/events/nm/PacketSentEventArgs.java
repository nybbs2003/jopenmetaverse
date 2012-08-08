package com.ngt.jopenmetaverse.shared.sim.events.nm;

import com.ngt.jopenmetaverse.shared.sim.Simulator;
import com.ngt.jopenmetaverse.shared.sim.events.EventArgs;

public class PacketSentEventArgs extends EventArgs  
{
	private  byte[] m_Data;
    private  int m_SentBytes;
    private Simulator m_Simulator;

    public byte[] getData() { return m_Data; } 
    public int getSentBytes() { return m_SentBytes; } 
    public Simulator getSimulator() { return m_Simulator; } 

    public PacketSentEventArgs(byte[] data, int bytesSent, Simulator simulator)
    {
        this.m_Data = data;
        this.m_SentBytes = bytesSent;
        this.m_Simulator = simulator;
    }
}
