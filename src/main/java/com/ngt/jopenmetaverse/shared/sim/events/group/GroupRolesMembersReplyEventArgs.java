package com.ngt.jopenmetaverse.shared.sim.events.group;

import java.util.List;
import com.ngt.jopenmetaverse.shared.sim.events.EventArgs;
import com.ngt.jopenmetaverse.shared.types.UUID;

public class GroupRolesMembersReplyEventArgs extends EventArgs
    {
        private  UUID m_RequestID;
        private  UUID m_GroupID;
        private  List<KeyValuePair<UUID, UUID>> m_RolesMembers;

        /// <summary>Get the ID as returned by the request to correlate
        /// this result set and the request</summary>
        public UUID getRequestID() {return m_RequestID; }
        /// <summary>Get the ID of the group</summary>
        public UUID getGroupID() {return m_GroupID; }
        /// <summary>Get the member to roles map</summary>
        public List<KeyValuePair<UUID, UUID>> getRolesMembers() {return m_RolesMembers; }

        /// <summary>Construct a new instance of the GroupRolesMembersReplyEventArgs class</summary>
        /// <param name="requestID">The ID as returned by the request to correlate
        /// this result set and the request</param>
        /// <param name="groupID">The ID of the group</param>
        /// <param name="rolesMembers">The member to roles map</param>
        public GroupRolesMembersReplyEventArgs(UUID requestID, UUID groupID, List<KeyValuePair<UUID, UUID>> groupRoleMemberCache)
        {
            this.m_RequestID = requestID;
            this.m_GroupID = groupID;
            this.m_RolesMembers = groupRoleMemberCache;
        }
    }

    /// <summary>Represents the titles for a group</summary>
    