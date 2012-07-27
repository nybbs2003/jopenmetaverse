package com.ngt.jopenmetaverse.shared.sim.events;

import com.ngt.jopenmetaverse.shared.protocol.Packet;
import com.ngt.jopenmetaverse.shared.sim.Simulator;

    public class PacketReceivedEventArgs extends EventArgs
    {
        private final Packet m_Packet;
        private final Simulator m_Simulator;

        public Packet getPacket() 
        { return m_Packet; } 
        public Simulator getSimulator() { return m_Simulator; } 

        public PacketReceivedEventArgs(Packet packet, Simulator simulator)
        {
        	super();
            this.m_Packet = packet;
            this.m_Simulator = simulator;
        }
    }