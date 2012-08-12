package com.ngt.jopenmetaverse.shared.sim.events.om;

import com.ngt.jopenmetaverse.shared.protocol.primitives.ObjectProperties;
import com.ngt.jopenmetaverse.shared.protocol.primitives.Primitive;
import com.ngt.jopenmetaverse.shared.sim.Simulator;
import com.ngt.jopenmetaverse.shared.sim.events.EventArgs;


/// <summary>Provides additional primitive data for the <see cref="ObjectManager.ObjectPropertiesUpdated"/> event</summary>
/// <remarks><para>The <see cref="ObjectManager.ObjectPropertiesUpdated"/> event occurs when the simulator sends
/// an <see cref="ObjectPropertiesPacket"/> containing additional details for a Primitive or Foliage data that is currently
/// being tracked in the <see cref="Simulator.ObjectsPrimitives"/> dictionary</para>
/// <para>The <see cref="ObjectManager.ObjectPropertiesUpdated"/> event is also raised when a <see cref="ObjectManager.SelectObject"/> request is
/// made and <see cref="Settings.OBJECT_TRACKING"/> is enabled</para>    
/// </remarks>    
public class ObjectPropertiesUpdatedEventArgs extends EventArgs
{

    private  Simulator m_Simulator;
    private  Primitive m_Prim;
    private  ObjectProperties m_Properties;

    /// <summary>Get the simulator the object is located</summary>
    public Simulator getSimulator() {return m_Simulator;}
    /// <summary>Get the primitive details</summary>
    public Primitive getPrim() {return m_Prim;}
    /// <summary>Get the primitive properties</summary>
    public ObjectProperties getProperties() {return m_Properties;}

    /// <summary>
    /// Construct a new instance of the ObjectPropertiesUpdatedEvenrArgs class
    /// </summary>                
    /// <param name="simulator">The simulator the object is located</param>
    /// <param name="prim">The Primitive</param>
    /// <param name="props">The primitive Properties</param>
    public ObjectPropertiesUpdatedEventArgs(Simulator simulator, Primitive prim, ObjectProperties props)
    {
        this.m_Simulator = simulator;
        this.m_Prim = prim;
        this.m_Properties = props;
    }
}
