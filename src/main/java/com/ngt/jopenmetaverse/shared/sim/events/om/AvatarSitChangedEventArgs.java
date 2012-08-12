package com.ngt.jopenmetaverse.shared.sim.events.om;

import com.ngt.jopenmetaverse.shared.sim.Avatar;
import com.ngt.jopenmetaverse.shared.sim.Simulator;
import com.ngt.jopenmetaverse.shared.sim.events.EventArgs;

/// <summary>
/// Provides updates sit position data
/// </summary>
public class AvatarSitChangedEventArgs extends EventArgs
{
    private  Simulator m_Simulator;
    private  Avatar m_Avatar;
    //uint
    private  long m_SittingOn;
    //uint
    private  long m_OldSeat;

    /// <summary>Get the simulator the object is located</summary>
    public Simulator getSimulator() {return m_Simulator;}
    /// <summary></summary>
    public Avatar getAvatar() {return m_Avatar;}
    /// <summary></summary>
    public long getSittingOn() {return m_SittingOn;}
    /// <summary></summary>
    public long getOldSeat() {return m_OldSeat;}

    public AvatarSitChangedEventArgs(Simulator simulator, Avatar avatar, long sittingOn, long oldSeat)
    {
        this.m_Simulator = simulator;
        this.m_Avatar = avatar;
        this.m_SittingOn = sittingOn;
        this.m_OldSeat = oldSeat;
    }
}
