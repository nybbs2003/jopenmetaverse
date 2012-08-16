package com.ngt.jopenmetaverse.shared.sim.events.avm;

import java.util.List;

import com.ngt.jopenmetaverse.shared.sim.AvatarManager.AvatarGroup;
import com.ngt.jopenmetaverse.shared.sim.events.EventArgs;
import com.ngt.jopenmetaverse.shared.types.UUID;
public class AvatarGroupsReplyEventArgs extends EventArgs
{
    private UUID m_AvatarID;
    private List<AvatarGroup> m_Groups;

    /// <summary>Get the ID of the agent</summary>
    public UUID getAvatarID() {return m_AvatarID; }
    public List<AvatarGroup> getGroups() {return m_Groups; }

    public AvatarGroupsReplyEventArgs(UUID avatarID, List<AvatarGroup> avatarGroups)
    {
        this.m_AvatarID = avatarID;
        this.m_Groups = avatarGroups;
    }
}