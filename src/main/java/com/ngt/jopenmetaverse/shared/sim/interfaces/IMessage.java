package com.ngt.jopenmetaverse.shared.sim.interfaces;

import com.ngt.jopenmetaverse.shared.structureddata.OSDMap;

/// <summary>
/// Interface requirements for Messaging system
/// </summary>
public interface IMessage {
        OSDMap Serialize();
        void Deserialize(OSDMap map) throws Exception;
}
