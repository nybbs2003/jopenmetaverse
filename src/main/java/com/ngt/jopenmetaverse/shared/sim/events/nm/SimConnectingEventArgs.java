package com.ngt.jopenmetaverse.shared.sim.events.nm;

import com.ngt.jopenmetaverse.shared.sim.Simulator;
import com.ngt.jopenmetaverse.shared.sim.events.EventArgs;

public class SimConnectingEventArgs extends EventArgs
{
	private Simulator m_Simulator;
    private boolean m_Cancel;

    public Simulator getSimulator() { return m_Simulator; }

    public boolean getCancel()
    {
        return m_Cancel;
    }
    public void setCancel(boolean value)
    {
        m_Cancel = value; 
    }

    public SimConnectingEventArgs(Simulator simulator)
    {
        this.m_Simulator = simulator;
        this.m_Cancel = false;
    }
}
