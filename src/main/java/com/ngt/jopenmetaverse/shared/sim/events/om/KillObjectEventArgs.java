package com.ngt.jopenmetaverse.shared.sim.events.om;

import com.ngt.jopenmetaverse.shared.sim.Simulator;
import com.ngt.jopenmetaverse.shared.sim.events.EventArgs;

/// <summary>Provides notification when an Avatar, Object or Attachment is DeRezzed or moves out of the avatars view for the 
/// <see cref="ObjectManager.KillObject"/> event</summary>
public class KillObjectEventArgs extends EventArgs
{
    private  Simulator m_Simulator;
    //uint
    private  long m_ObjectLocalID;

    /// <summary>Get the simulator the object is located</summary>
    public Simulator getSimulator() {return m_Simulator;}
    /// <summary>The LocalID of the object</summary>
    public long getObjectLocalID() {return m_ObjectLocalID;}

    public KillObjectEventArgs(Simulator simulator, long objectID)
    {
        this.m_Simulator = simulator;
        this.m_ObjectLocalID = objectID;
    }
}
