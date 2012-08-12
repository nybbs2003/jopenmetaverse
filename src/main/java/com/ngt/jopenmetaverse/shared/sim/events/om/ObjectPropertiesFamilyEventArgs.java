package com.ngt.jopenmetaverse.shared.sim.events.om;

import com.ngt.jopenmetaverse.shared.protocol.primitives.ObjectProperties;
import com.ngt.jopenmetaverse.shared.sim.ObjectManager.ReportType;
import com.ngt.jopenmetaverse.shared.sim.Simulator;
import com.ngt.jopenmetaverse.shared.sim.events.EventArgs;


/// <summary>Provides additional primitive data, permissions and sale info for the <see cref="ObjectManager.ObjectPropertiesFamily"/> event</summary>
/// <remarks><para>The <see cref="ObjectManager.ObjectPropertiesFamily"/> event occurs when the simulator sends
/// an <see cref="ObjectPropertiesPacket"/> containing additional details for a Primitive, Foliage data or Attachment. This includes
/// Permissions, Sale info, and other basic details on an object</para>
/// <para>The <see cref="ObjectManager.ObjectProperties"/> event is also raised when a <see cref="ObjectManager.RequestObjectPropertiesFamily"/> request is
/// made, the viewer equivalent is hovering the mouse cursor over an object</para>
/// </remarks>    
public class ObjectPropertiesFamilyEventArgs extends EventArgs
{
    private  Simulator m_Simulator;
    private  ObjectProperties m_Properties;
    private  ReportType m_Type;

    /// <summary>Get the simulator the object is located</summary>
    public Simulator getSimulator() {return m_Simulator;}
    /// <summary></summary>
    public ObjectProperties getProperties() {return m_Properties;}
    /// <summary></summary>
    public ReportType getType() {return m_Type;}

    public ObjectPropertiesFamilyEventArgs(Simulator simulator, ObjectProperties props, ReportType type)
    {
        this.m_Simulator = simulator;
        this.m_Properties = props;
        this.m_Type = type;
    }
}

