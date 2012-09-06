package com.ngt.jopenmetaverse.shared.sim.events.group;


import com.ngt.jopenmetaverse.shared.sim.Simulator;
import com.ngt.jopenmetaverse.shared.sim.events.EventArgs;
import com.ngt.jopenmetaverse.shared.types.UUID;

public class GroupInvitationEventArgs extends EventArgs
    {
        private  UUID m_FromAgentID;
        private  String m_FromAgentName;
        private  String m_Message;
        private  Simulator m_Simulator;
        private boolean m_accept;
        
        /// <summary>The ID of the Avatar sending the group invitation</summary>
        public UUID getAgentID() {return m_FromAgentID; }
        /// <summary>The name of the Avatar sending the group invitation</summary>
        public String getFromName() {return m_FromAgentName; }
        /// <summary>A message containing the request information which includes
        /// the name of the group, the groups charter and the fee to join details</summary>
        public String getMessage() {return m_Message; }
        /// <summary>The Simulator</summary>
        public Simulator getSimulator() {return m_Simulator; }

        /// <summary>Set to true to accept invitation, false to decline</summary>
        public boolean getAccept() { return m_accept; }
        public void setAccept(boolean value) { m_accept = value; }

        
        public GroupInvitationEventArgs(Simulator simulator, UUID agentID, String agentName, String message)
        {
            this.m_Simulator = simulator;
            this.m_FromAgentID = agentID;
            this.m_FromAgentName = agentName;
            this.m_Message = message;
        }
    }
