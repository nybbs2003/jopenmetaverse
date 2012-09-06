package com.ngt.jopenmetaverse.shared.sim.events.group;


import com.ngt.jopenmetaverse.shared.sim.events.EventArgs;
import com.ngt.jopenmetaverse.shared.types.UUID;

public class GroupOperationEventArgs extends EventArgs
    {
        private  UUID m_GroupID;
        private  boolean m_Success;

        /// <summary>Get the ID of the group</summary>
        public UUID getGroupID() {return m_GroupID; }
        /// <summary>true of the request was successful</summary>
        public boolean getSuccess() {return m_Success; }

        /// <summary>Construct a new instance of the GroupOperationEventArgs class</summary>
        /// <param name="groupID">The ID of the group</param>
        /// <param name="success">true of the request was successful</param>
        public GroupOperationEventArgs(UUID groupID, boolean success)
        {
            this.m_GroupID = groupID;
            this.m_Success = success;
        }
    }
    
    /// <summary>Represents your agent leaving a group</summary>
    