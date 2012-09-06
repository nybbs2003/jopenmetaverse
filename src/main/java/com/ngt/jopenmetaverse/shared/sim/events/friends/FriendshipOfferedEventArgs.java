package com.ngt.jopenmetaverse.shared.sim.events.friends;

import com.ngt.jopenmetaverse.shared.sim.events.EventArgs;
import com.ngt.jopenmetaverse.shared.types.UUID;
   /// <summary>Sent when another agent requests a friendship with our agent</summary>
    public class FriendshipOfferedEventArgs extends  EventArgs
    {
        private  UUID m_AgentID;
        private  String m_AgentName;
        private  UUID m_SessionID;

        /// <summary>Get the ID of the agent requesting friendship</summary>
        public UUID getAgentID() {return m_AgentID;}
        /// <summary>Get the name of the agent requesting friendship</summary>
        public String getAgentName() {return m_AgentName;}
        /// <summary>Get the ID of the session, used in accepting or declining the 
        /// friendship offer</summary>
        public UUID getSessionID() {return m_SessionID;}

        /// <summary>
        /// Construct a new instance of the FriendshipOfferedEventArgs class
        /// </summary>
        /// <param name="agentID">The ID of the agent requesting friendship</param>
        /// <param name="agentName">The name of the agent requesting friendship</param>
        /// <param name="imSessionID">The ID of the session, used in accepting or declining the 
        /// friendship offer</param>
        public FriendshipOfferedEventArgs(UUID agentID, String agentName, UUID imSessionID)
        {
            this.m_AgentID = agentID;
            this.m_AgentName = agentName;
            this.m_SessionID = imSessionID;
        }
    }