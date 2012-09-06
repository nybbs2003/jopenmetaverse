package com.ngt.jopenmetaverse.shared.sim.events.group;

import java.util.List;
import com.ngt.jopenmetaverse.shared.sim.GroupManager.GroupNoticesListEntry;
import com.ngt.jopenmetaverse.shared.sim.events.EventArgs;
import com.ngt.jopenmetaverse.shared.types.UUID;

public class GroupNoticesListReplyEventArgs extends EventArgs
    {
        private  UUID m_GroupID;
        private  List<GroupNoticesListEntry> m_Notices;

        /// <summary>Get the ID of the group</summary>
        public UUID getGroupID() {return m_GroupID; }
        /// <summary>Get the notices list</summary>
        public List<GroupNoticesListEntry> getNotices() {return m_Notices; }

        /// <summary>Construct a new instance of the GroupNoticesListReplyEventArgs class</summary>
        /// <param name="groupID">The ID of the group</param>
        /// <param name="notices">The list containing active notices</param>
        public GroupNoticesListReplyEventArgs(UUID groupID, List<GroupNoticesListEntry> notices)
        {
            m_GroupID = groupID;
            m_Notices = notices;
        }
    }
    
    /// <summary>Represents the profile of a group</summary>
    