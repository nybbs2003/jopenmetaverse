package com.ngt.jopenmetaverse.shared.sim.events.group;

import com.ngt.jopenmetaverse.shared.sim.events.EventArgs;
import java.util.Map;
import com.ngt.jopenmetaverse.shared.sim.GroupManager.GroupTitle;
import com.ngt.jopenmetaverse.shared.types.UUID;

public class GroupTitlesReplyEventArgs extends EventArgs
    {
        private  UUID m_RequestID;
        private  UUID m_GroupID;
        private Map<UUID, GroupTitle> m_Titles;

        /// <summary>Get the ID as returned by the request to correlate
        /// this result set and the request</summary>
        public UUID getRequestID() {return m_RequestID; }
        /// <summary>Get the ID of the group</summary>
        public UUID getGroupID() {return m_GroupID; }
        /// <summary>Get the titles</summary>
        public Map<UUID, GroupTitle> getTitles() {return m_Titles; }

        /// <summary>Construct a new instance of the GroupTitlesReplyEventArgs class</summary>
        /// <param name="requestID">The ID as returned by the request to correlate
        /// this result set and the request</param>
        /// <param name="groupID">The ID of the group</param>
        /// <param name="titles">The titles</param>
        public GroupTitlesReplyEventArgs(UUID requestID, UUID groupID, Map<UUID, GroupTitle> titles)
        {
            this.m_RequestID = requestID;
            this.m_GroupID = groupID;
            this.m_Titles = titles;
        }
    }

    /// <summary>Represents the summary data for a group</summary>
    