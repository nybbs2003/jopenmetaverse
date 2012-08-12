package com.ngt.jopenmetaverse.shared.sim.events.am;

import com.ngt.jopenmetaverse.shared.sim.AvatarManager.AgentDisplayName;
import com.ngt.jopenmetaverse.shared.sim.events.EventArgs;

/// <summary>Event arguments with the result of setting display name operation</summary>
	public class SetDisplayNameReplyEventArgs extends EventArgs
	{
		private  int m_Status;
		private  String m_Reason;
		private  AgentDisplayName m_DisplayName;

		/// <summary>Status code, 200 indicates settign display name was successful</summary>
		public int getStatus() {return m_Status;}

		/// <summary>Textual description of the status</summary>
		public String getReason() {return m_Reason;}

		/// <summary>Details of the newly set display name</summary>
		public AgentDisplayName getDisplayName() {return m_DisplayName;}

		/// <summary>Default constructor</summary>
		public SetDisplayNameReplyEventArgs(int status, String reason, AgentDisplayName displayName)
		{
			m_Status = status;
			m_Reason = reason;
			m_DisplayName = displayName;
		}
	}
