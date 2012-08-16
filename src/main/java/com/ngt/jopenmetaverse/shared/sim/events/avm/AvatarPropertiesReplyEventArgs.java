package com.ngt.jopenmetaverse.shared.sim.events.avm;

import com.ngt.jopenmetaverse.shared.sim.Avatar;
import com.ngt.jopenmetaverse.shared.sim.events.EventArgs;
import com.ngt.jopenmetaverse.shared.types.UUID;
/// <summary>The properties of an agent</summary>
public class AvatarPropertiesReplyEventArgs extends EventArgs
{
    private UUID m_AvatarID;
    private Avatar.AvatarProperties m_Properties;

    /// <summary>Get the ID of the agent</summary>
    public UUID getAvatarID() {return m_AvatarID; }
    public Avatar.AvatarProperties getProperties() {return m_Properties; }

    public AvatarPropertiesReplyEventArgs(UUID avatarID, Avatar.AvatarProperties properties)
    {
        this.m_AvatarID = avatarID;
        this.m_Properties = properties;
    }
}