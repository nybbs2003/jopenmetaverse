package com.ngt.jopenmetaverse.shared.sim.events.avm;

import com.ngt.jopenmetaverse.shared.sim.AvatarManager.AgentDisplayName;
import com.ngt.jopenmetaverse.shared.sim.events.EventArgs;

/// <summary>
/// Event args class for display name notification messages
/// </summary>
public class DisplayNameUpdateEventArgs extends EventArgs
{
    private String oldDisplayName;
    private AgentDisplayName displayName;

    public String getOldDisplayName() {return oldDisplayName;}
    public AgentDisplayName getDisplayName() {return displayName;}

    public DisplayNameUpdateEventArgs(String oldDisplayName, AgentDisplayName displayName)
    {
        this.oldDisplayName = oldDisplayName;
        this.displayName = displayName;
    }
}