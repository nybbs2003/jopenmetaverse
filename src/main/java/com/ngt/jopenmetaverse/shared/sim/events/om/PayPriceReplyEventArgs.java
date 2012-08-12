package com.ngt.jopenmetaverse.shared.sim.events.om;

import com.ngt.jopenmetaverse.shared.sim.Simulator;
import com.ngt.jopenmetaverse.shared.sim.events.EventArgs;
import com.ngt.jopenmetaverse.shared.types.UUID;

/// <summary>
/// 
/// </summary>
public class PayPriceReplyEventArgs extends EventArgs
{
    private  Simulator m_Simulator;
    private  UUID m_ObjectID;
    private  int m_DefaultPrice;
    private  int[] m_ButtonPrices;

    /// <summary>Get the simulator the object is located</summary>
    public Simulator getSimulator() {return m_Simulator;}
    /// <summary></summary>
    public UUID getObjectID() {return m_ObjectID;}
    /// <summary></summary>
    public int getDefaultPrice() {return m_DefaultPrice;}
    /// <summary></summary>
    public int[] getButtonPrices() {return m_ButtonPrices;}

    public PayPriceReplyEventArgs(Simulator simulator, UUID objectID, int defaultPrice, int[] buttonPrices)
    {
        this.m_Simulator = simulator;
        this.m_ObjectID = objectID;
        this.m_DefaultPrice = defaultPrice;
        this.m_ButtonPrices = buttonPrices;
    }
}

