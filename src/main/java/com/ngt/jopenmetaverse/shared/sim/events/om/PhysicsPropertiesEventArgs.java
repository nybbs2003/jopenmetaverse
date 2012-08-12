package com.ngt.jopenmetaverse.shared.sim.events.om;

import com.ngt.jopenmetaverse.shared.protocol.primitives.PhysicsProperties;
import com.ngt.jopenmetaverse.shared.sim.Simulator;
import com.ngt.jopenmetaverse.shared.sim.events.EventArgs;

/// <summary>
/// Set when simulator sends us infomation on primitive's physical properties
/// </summary>
public class PhysicsPropertiesEventArgs extends EventArgs
{
    /// <summary>Simulator where the message originated</summary>
    public Simulator Simulator;
    /// <summary>Updated physical properties</summary>
    public PhysicsProperties PhysicsProperties;

    /// <summary>
    /// Constructor
    /// </summary>
    /// <param name="sim">Simulator where the message originated</param>
    /// <param name="props">Updated physical properties</param>
    public PhysicsPropertiesEventArgs(Simulator sim, PhysicsProperties props)
    {
        Simulator = sim;
        PhysicsProperties = props;
    }
}
