package com.ngt.jopenmetaverse.shared.sim.events.am;

import com.ngt.jopenmetaverse.shared.sim.events.EventArgs;
import com.ngt.jopenmetaverse.shared.types.UUID;

/// <summary>Data sent when an agent exits a chat session your agent is currently participating in</summary>
public class ChatSessionMemberLeftEventArgs extends EventArgs
{
	private  UUID m_SessionID;
	private  UUID m_AgentID;

	/// <summary>Get the ID of the chat session</summary>
	public UUID getSessionID() {return m_SessionID;}
	/// <summary>Get the ID of the agent that left</summary>
	public UUID getAgentID() {return m_AgentID;}

	/// <summary>
	/// Construct a new instance of the ChatSessionMemberLeftEventArgs object
	/// </summary>
	/// <param name="sessionID">The ID of the chat session</param>
	/// <param name="agentID">The ID of the Agent that left</param>
	public ChatSessionMemberLeftEventArgs(UUID sessionID, UUID agentID)
	{
		this.m_SessionID = sessionID;
		this.m_AgentID = agentID;
	}
}
