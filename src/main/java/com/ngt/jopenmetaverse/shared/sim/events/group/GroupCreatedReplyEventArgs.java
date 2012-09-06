package com.ngt.jopenmetaverse.shared.sim.events.group;


import com.ngt.jopenmetaverse.shared.sim.events.EventArgs;
import com.ngt.jopenmetaverse.shared.types.UUID;

public class GroupCreatedReplyEventArgs extends EventArgs
    {
        private  UUID m_GroupID;
        private  boolean m_Success;
        private  String m_Message;

        /// <summary>Get the ID of the group</summary>
        public UUID getGroupID() {return m_GroupID; }
        /// <summary>true of the  group was created successfully</summary>
        public boolean getSuccess() {return m_Success; }
        /// <summary>A String containing the message</summary>
        public String getMessage() {return m_Message; }

        /// <summary>Construct a new instance of the GroupCreatedReplyEventArgs class</summary>
        /// <param name="groupID">The ID of the group</param>
        /// <param name="success">the success or faulure of the request</param>
        /// <param name="messsage">A String containing additional information</param>
        public GroupCreatedReplyEventArgs(UUID groupID, boolean success, String messsage)
        {
            this.m_GroupID = groupID;
            this.m_Success = success;
            this.m_Message = messsage;
        }
    }
    
    /// <summary>Represents a response to a request</summary>
    