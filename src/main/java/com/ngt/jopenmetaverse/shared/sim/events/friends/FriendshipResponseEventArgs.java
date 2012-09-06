package com.ngt.jopenmetaverse.shared.sim.events.friends;

import com.ngt.jopenmetaverse.shared.sim.events.EventArgs;
import com.ngt.jopenmetaverse.shared.types.UUID;

/// <summary>A response containing the results of our request to form a friendship with another agent</summary>
    public class FriendshipResponseEventArgs extends  EventArgs
    {
        private  UUID m_AgentID;
        private  String m_AgentName;
        private  boolean m_Accepted;

        /// <summary>Get the ID of the agent we requested a friendship with</summary>
        public UUID getAgentID() {return m_AgentID;}
        /// <summary>Get the name of the agent we requested a friendship with</summary>
        public String getAgentName() {return m_AgentName;}
        /// <summary>true if the agent accepted our friendship offer</summary>
        public boolean getAccepted() {return m_Accepted;}

        /// <summary>
        /// Construct a new instance of the FriendShipResponseEventArgs class
        /// </summary>
        /// <param name="agentID">The ID of the agent we requested a friendship with</param>
        /// <param name="agentName">The name of the agent we requested a friendship with</param>
        /// <param name="accepted">true if the agent accepted our friendship offer</param>
        public FriendshipResponseEventArgs(UUID agentID, String agentName, boolean accepted)
        {
            this.m_AgentID = agentID;
            this.m_AgentName = agentName;
            this.m_Accepted = accepted;
        }
    }