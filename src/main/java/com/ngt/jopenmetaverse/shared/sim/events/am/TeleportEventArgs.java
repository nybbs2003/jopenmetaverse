package com.ngt.jopenmetaverse.shared.sim.events.am;

import java.util.EnumSet;

import com.ngt.jopenmetaverse.shared.sim.AgentManager.TeleportFlags;
import com.ngt.jopenmetaverse.shared.sim.AgentManager.TeleportStatus;
import com.ngt.jopenmetaverse.shared.sim.events.EventArgs;


// String message, TeleportStatus status, TeleportFlags flags
public class TeleportEventArgs extends EventArgs
{
	private  String m_Message;
	private  TeleportStatus m_Status;
	private  EnumSet<TeleportFlags> m_Flags;

	public String getMessage() {return m_Message;}
	public TeleportStatus getStatus() {return m_Status;}
	public EnumSet<TeleportFlags> getFlags() {return m_Flags;}

	public TeleportEventArgs(String message, TeleportStatus status, EnumSet<TeleportFlags> flags)
	{
		this.m_Message = message;
		this.m_Status = status;
		this.m_Flags = flags;
	}
}