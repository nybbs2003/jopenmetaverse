package com.ngt.jopenmetaverse.shared.sim.events.am;

import java.util.EnumSet;

import com.ngt.jopenmetaverse.shared.sim.Simulator;
import com.ngt.jopenmetaverse.shared.sim.AgentManager.ScriptPermission;
import com.ngt.jopenmetaverse.shared.sim.events.EventArgs;
import com.ngt.jopenmetaverse.shared.types.UUID;

/// <summary>Contains the data sent when a primitive requests debit or other permissions
	/// requesting a YES or NO answer</summary>
	public class ScriptQuestionEventArgs extends EventArgs
	{
		private  Simulator m_Simulator;
		private  UUID m_TaskID;
		private  UUID m_ItemID;
		private  String m_ObjectName;
		private  String m_ObjectOwnerName;
		private  EnumSet<ScriptPermission> m_Questions;

		/// <summary>Get the simulator containing the object sending the request</summary>
		public Simulator getSimulator() { return m_Simulator; } 
		/// <summary>Get the ID of the script making the request</summary>
		public UUID getTaskID() { return m_TaskID; } 
		/// <summary>Get the ID of the primitive containing the script making the request</summary>
		public UUID getItemID() { return m_ItemID; } 
		/// <summary>Get the name of the primitive making the request</summary>
		public String getObjectName() { return m_ObjectName; } 
		/// <summary>Get the name of the owner of the object making the request</summary>
		public String getObjectOwnerName() {return m_ObjectOwnerName; } 
		/// <summary>Get the permissions being requested</summary>
		public EnumSet<ScriptPermission> getQuestions() { return m_Questions; } 

		/// <summary>
		/// Construct a new instance of the ScriptQuestionEventArgs
		/// </summary>
		/// <param name="simulator">The simulator containing the object sending the request</param>
		/// <param name="taskID">The ID of the script making the request</param>
		/// <param name="itemID">The ID of the primitive containing the script making the request</param>
		/// <param name="objectName">The name of the primitive making the request</param>
		/// <param name="objectOwner">The name of the owner of the object making the request</param>
		/// <param name="questions">The permissions being requested</param>
		public ScriptQuestionEventArgs(Simulator simulator, UUID taskID, UUID itemID, String objectName, String objectOwner, EnumSet<ScriptPermission> questions)
		{
			this.m_Simulator = simulator;
			this.m_TaskID = taskID;
			this.m_ItemID = itemID;
			this.m_ObjectName = objectName;
			this.m_ObjectOwnerName = objectOwner;
			this.m_Questions = questions;
		}

	}

