package com.ngt.jopenmetaverse.shared.sim.events.am;

import com.ngt.jopenmetaverse.shared.sim.events.EventArgs;
import com.ngt.jopenmetaverse.shared.types.UUID;

/// <summary>Contains the data sent when a primitive sends a request 
	/// to an agent to open the specified URL</summary>
	public class LoadUrlEventArgs extends EventArgs
	{
		private  String m_ObjectName;
		private  UUID m_ObjectID;
		private  UUID m_OwnerID;
		private  boolean m_OwnerIsGroup;
		private  String m_Message;
		private  String m_URL;

		/// <summary>Get the name of the object sending the request</summary>
		public String getObjectName() { return m_ObjectName; } 
		/// <summary>Get the ID of the object sending the request</summary>
		public UUID getObjectID() { return m_ObjectID; } 
		/// <summary>Get the ID of the owner of the object sending the request</summary>
		public UUID getOwnerID() { return m_OwnerID; } 
		/// <summary>True if the object is owned by a group</summary>
		public boolean getOwnerIsGroup() { return m_OwnerIsGroup; } 
		/// <summary>Get the message sent with the request</summary>
		public String getMessage() { return m_Message; } 
		/// <summary>Get the URL the object sent</summary>
		public String getURL() { return m_URL; } 

		/// <summary>
		/// Construct a new instance of the LoadUrlEventArgs
		/// </summary>
		/// <param name="objectName">The name of the object sending the request</param>
		/// <param name="objectID">The ID of the object sending the request</param>
		/// <param name="ownerID">The ID of the owner of the object sending the request</param>
		/// <param name="ownerIsGroup">True if the object is owned by a group</param>
		/// <param name="message">The message sent with the request</param>
		/// <param name="URL">The URL the object sent</param>
		public LoadUrlEventArgs(String objectName, UUID objectID, UUID ownerID, boolean ownerIsGroup, String message, String URL)
		{
			this.m_ObjectName = objectName;
			this.m_ObjectID = objectID;
			this.m_OwnerID = ownerID;
			this.m_OwnerIsGroup = ownerIsGroup;
			this.m_Message = message;
			this.m_URL = URL;
		}
	}

