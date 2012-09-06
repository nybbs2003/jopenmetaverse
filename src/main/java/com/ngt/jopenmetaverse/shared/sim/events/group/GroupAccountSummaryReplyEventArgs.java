package com.ngt.jopenmetaverse.shared.sim.events.group;

import com.ngt.jopenmetaverse.shared.sim.GroupManager.GroupAccountSummary;
import com.ngt.jopenmetaverse.shared.sim.events.EventArgs;
import com.ngt.jopenmetaverse.shared.types.UUID;

public class GroupAccountSummaryReplyEventArgs extends EventArgs
    {
        private  UUID m_GroupID;
        private  GroupAccountSummary m_Summary;

        /// <summary>Get the ID of the group</summary>
        public UUID getGroupID() {return m_GroupID; }
        /// <summary>Get the summary data</summary>
        public GroupAccountSummary getSummary() {return m_Summary; }

        /// <summary>Construct a new instance of the GroupAccountSummaryReplyEventArgs class</summary>
        /// <param name="groupID">The ID of the group</param>
        /// <param name="summary">The summary data</param>
        public GroupAccountSummaryReplyEventArgs(UUID groupID, GroupAccountSummary summary)
        {
            this.m_GroupID = groupID;
            this.m_Summary = summary;
        }
    }
    
    /// <summary>A response to a group create request</summary>
    