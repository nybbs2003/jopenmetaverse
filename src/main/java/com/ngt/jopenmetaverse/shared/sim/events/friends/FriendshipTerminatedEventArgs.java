package com.ngt.jopenmetaverse.shared.sim.events.friends;

import com.ngt.jopenmetaverse.shared.sim.events.EventArgs;
import com.ngt.jopenmetaverse.shared.types.UUID;
  /// <summary>Contains data sent when a friend terminates a friendship with us</summary>
    public class FriendshipTerminatedEventArgs extends  EventArgs
    {
        private  UUID m_AgentID;
        private  String m_AgentName;

        /// <summary>Get the ID of the agent that terminated the friendship with us</summary>
        public UUID getAgentID() {return m_AgentID;}
        /// <summary>Get the name of the agent that terminated the friendship with us</summary>
        public String getAgentName() {return m_AgentName;}

        /// <summary>
        /// Construct a new instance of the FrindshipTerminatedEventArgs class
        /// </summary>
        /// <param name="agentID">The ID of the friend who terminated the friendship with us</param>
        /// <param name="agentName">The name of the friend who terminated the friendship with us</param>
        public FriendshipTerminatedEventArgs(UUID agentID, String agentName)
        {
            this.m_AgentID = agentID;
            this.m_AgentName = agentName;
        }
    }