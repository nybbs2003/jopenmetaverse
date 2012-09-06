package com.ngt.jopenmetaverse.shared.sim.events.group;

import com.ngt.jopenmetaverse.shared.sim.events.EventArgs;
import com.ngt.jopenmetaverse.shared.types.UUID;

public class GroupDroppedEventArgs extends EventArgs
    {
        private  UUID m_GroupID;
        /// <summary>Get the ID of the group</summary>
        public UUID getGroupID() {return m_GroupID; }

        /// <summary>Construct a new instance of the GroupDroppedEventArgs class</summary>
        /// <param name="groupID">The ID of the group</param>
        public GroupDroppedEventArgs(UUID groupID)
        {
            m_GroupID = groupID;
        }
    }

    /// <summary>Represents a list of active group notices</summary>
    