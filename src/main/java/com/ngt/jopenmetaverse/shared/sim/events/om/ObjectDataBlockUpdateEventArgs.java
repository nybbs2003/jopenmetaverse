package com.ngt.jopenmetaverse.shared.sim.events.om;

import com.ngt.jopenmetaverse.shared.protocol.NameValue;
import com.ngt.jopenmetaverse.shared.protocol.ObjectUpdatePacket;
import com.ngt.jopenmetaverse.shared.protocol.primitives.ConstructionData;
import com.ngt.jopenmetaverse.shared.protocol.primitives.Primitive;
import com.ngt.jopenmetaverse.shared.sim.ObjectManager.ObjectMovementUpdate;
import com.ngt.jopenmetaverse.shared.sim.Simulator;
import com.ngt.jopenmetaverse.shared.sim.events.EventArgs;

/// <summary>
/// 
/// </summary>
public class ObjectDataBlockUpdateEventArgs extends EventArgs
{
    private  Simulator m_Simulator;
    private  Primitive m_Prim;
    private  ConstructionData m_ConstructionData;
    private  ObjectUpdatePacket.ObjectDataBlock m_Block;
    private  ObjectMovementUpdate m_Update;
    private  NameValue[] m_NameValues;

    /// <summary>Get the simulator the object is located</summary>
    public Simulator getSimulator() {return m_Simulator;}
    /// <summary>Get the primitive details</summary>
    public Primitive getPrim() {return m_Prim;}
    /// <summary></summary>
    public ConstructionData getConstructionData() {return m_ConstructionData;}
    /// <summary></summary>
    public ObjectUpdatePacket.ObjectDataBlock getBlock() {return m_Block;}
    /// <summary></summary>
    public ObjectMovementUpdate getUpdate() {return m_Update;}
    /// <summary></summary>
    public NameValue[] getNameValues() {return m_NameValues;}

    public ObjectDataBlockUpdateEventArgs(Simulator simulator, Primitive prim, ConstructionData constructionData,
        ObjectUpdatePacket.ObjectDataBlock block, ObjectMovementUpdate objectupdate, NameValue[] nameValues)
    {
        this.m_Simulator = simulator;
        this.m_Prim = prim;
        this.m_ConstructionData = constructionData;
        this.m_Block = block;
        this.m_Update = objectupdate;
        this.m_NameValues = nameValues;
    }
}
