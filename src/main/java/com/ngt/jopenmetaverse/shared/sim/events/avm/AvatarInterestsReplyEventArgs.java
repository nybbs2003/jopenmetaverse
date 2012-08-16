package com.ngt.jopenmetaverse.shared.sim.events.avm;

import com.ngt.jopenmetaverse.shared.sim.Avatar;
import com.ngt.jopenmetaverse.shared.sim.events.EventArgs;
import com.ngt.jopenmetaverse.shared.types.UUID;

/// <summary>Represents the interests from the profile of an agent</summary>
public class AvatarInterestsReplyEventArgs extends EventArgs
{
    private UUID m_AvatarID;
    private Avatar.Interests m_Interests;

    /// <summary>Get the ID of the agent</summary>
    public UUID getAvatarID() {return m_AvatarID; }
    public Avatar.Interests getInterests() {return m_Interests; }

    public AvatarInterestsReplyEventArgs(UUID avatarID, Avatar.Interests interests)
    {
        this.m_AvatarID = avatarID;
        this.m_Interests = interests;
    }
}
