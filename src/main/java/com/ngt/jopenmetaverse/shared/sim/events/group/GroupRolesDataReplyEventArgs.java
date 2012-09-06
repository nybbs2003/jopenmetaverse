package com.ngt.jopenmetaverse.shared.sim.events.group;

import java.util.Map;
import com.ngt.jopenmetaverse.shared.sim.GroupManager.GroupRole;
import com.ngt.jopenmetaverse.shared.sim.events.EventArgs;
import com.ngt.jopenmetaverse.shared.types.UUID;

public class GroupRolesDataReplyEventArgs extends EventArgs
    {
        private  UUID m_RequestID;
        private  UUID m_GroupID;
        private Map<UUID, GroupRole> m_Roles;

        /// <summary>Get the ID as returned by the request to correlate
        /// this result set and the request</summary>
        public UUID getRequestID() {return m_RequestID; }
        /// <summary>Get the ID of the group</summary>
        public UUID getGroupID() {return m_GroupID; }
        /// <summary>Get the dictionary containing the roles</summary>
        public Map<UUID, GroupRole> getRoles() {return m_Roles; }

        /// <summary>Construct a new instance of the GroupRolesDataReplyEventArgs class</summary>
        /// <param name="requestID">The ID as returned by the request to correlate
        /// this result set and the request</param>
        /// <param name="groupID">The ID of the group</param>
        /// <param name="roles">The dictionary containing the roles</param>
        public GroupRolesDataReplyEventArgs(UUID requestID, UUID groupID, Map<UUID, GroupRole> roles)
        {
            this.m_RequestID = requestID;
            this.m_GroupID = groupID;
            this.m_Roles = roles;
        }
    }

    /// <summary>Represents the Role to Member mappings for a group</summary>
    