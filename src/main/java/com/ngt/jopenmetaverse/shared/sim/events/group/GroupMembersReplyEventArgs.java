package com.ngt.jopenmetaverse.shared.sim.events.group;

import java.util.Map;
import com.ngt.jopenmetaverse.shared.sim.GroupManager.GroupMember;
import com.ngt.jopenmetaverse.shared.sim.events.EventArgs;
import com.ngt.jopenmetaverse.shared.types.UUID;

  /// <summary>Represents the members of a group</summary>
    public class GroupMembersReplyEventArgs extends EventArgs
    {
        private UUID m_RequestID;
        private  UUID m_GroupID;
        private Map<UUID, GroupMember> m_Members;

        /// <summary>Get the ID as returned by the request to correlate
        /// this result set and the request</summary>
        public UUID getRequestID() {return m_RequestID; }
        /// <summary>Get the ID of the group</summary>
        public UUID getGroupID() {return m_GroupID; }
        /// <summary>Get the dictionary of members</summary>
        public Map<UUID, GroupMember> getMembers() {return m_Members; }

        /// <summary>
        /// Construct a new instance of the GroupMembersReplyEventArgs class
        /// </summary>
        /// <param name="requestID">The ID of the request</param>
        /// <param name="groupID">The ID of the group</param>
        /// <param name="members">The membership list of the group</param>
        public GroupMembersReplyEventArgs(UUID requestID, UUID groupID, Map<UUID, GroupMember> members)
        {
            this.m_RequestID = requestID;
            this.m_GroupID = groupID;
            this.m_Members = members;
        }
    }
    
    /// <summary>Represents the roles associated with a group</summary>
    