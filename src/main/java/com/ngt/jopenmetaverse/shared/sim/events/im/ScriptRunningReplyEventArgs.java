package com.ngt.jopenmetaverse.shared.sim.events.im;

import com.ngt.jopenmetaverse.shared.sim.events.EventArgs;
import com.ngt.jopenmetaverse.shared.types.UUID;

public class ScriptRunningReplyEventArgs extends EventArgs
{
	private UUID m_ObjectID;
	private UUID m_ScriptID;
	private boolean m_IsMono;
	private boolean m_IsRunning;

	public UUID getObjectID() {return m_ObjectID;}

	public UUID getScriptID() {return m_ScriptID;}
	public boolean getIsMono() {return m_IsMono;}
	public boolean getIsRunning() {return m_IsRunning;}

	public ScriptRunningReplyEventArgs(UUID objectID, UUID sctriptID, boolean isMono, boolean isRunning)
	{
		this.m_ObjectID = objectID;
		this.m_ScriptID = sctriptID;
		this.m_IsMono = isMono;
		this.m_IsRunning = isRunning;
	}
}
