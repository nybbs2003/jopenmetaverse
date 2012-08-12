package com.ngt.jopenmetaverse.shared.sim.events.om;

import com.ngt.jopenmetaverse.shared.protocol.primitives.Primitive;
import com.ngt.jopenmetaverse.shared.sim.ObjectManager.ObjectMovementUpdate;
import com.ngt.jopenmetaverse.shared.sim.Simulator;
import com.ngt.jopenmetaverse.shared.sim.events.EventArgs;


/// <summary>Provides primitive data containing updated location, velocity, rotation, textures for the <see cref="ObjectManager.TerseObjectUpdate"/> event</summary>
/// <remarks><para>The <see cref="ObjectManager.TerseObjectUpdate"/> event occurs when the simulator sends updated location, velocity, rotation, etc</para>        
/// </remarks>
public class TerseObjectUpdateEventArgs extends EventArgs
{
    private  Simulator m_Simulator;
    private  Primitive m_Prim;
    private  ObjectMovementUpdate m_Update;
    //ushort
    private  int m_TimeDilation;

    /// <summary>Get the simulator the object is located</summary>
    public Simulator getSimulator() {return m_Simulator;}
    /// <summary>Get the primitive details</summary>
    public Primitive getPrim() {return m_Prim;}
    /// <summary></summary>
    public ObjectMovementUpdate getUpdate() {return m_Update;}
    /// <summary></summary>
    //ushort
    public int getTimeDilation() {return m_TimeDilation;}

    public TerseObjectUpdateEventArgs(Simulator simulator, Primitive prim, ObjectMovementUpdate update, int timeDilation)
    {
        this.m_Simulator = simulator;
        this.m_Prim = prim;
        this.m_Update = update;
        this.m_TimeDilation = timeDilation;
    }
}
