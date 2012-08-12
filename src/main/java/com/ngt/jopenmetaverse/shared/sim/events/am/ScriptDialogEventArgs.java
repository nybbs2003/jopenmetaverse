package com.ngt.jopenmetaverse.shared.sim.events.am;

import java.util.List;

import com.ngt.jopenmetaverse.shared.sim.events.EventArgs;
import com.ngt.jopenmetaverse.shared.types.UUID;

	/// <summary>Contains the data sent when a primitive opens a dialog with this agent</summary>
	public class ScriptDialogEventArgs extends EventArgs
	{
		private  String m_Message;
		private  String m_ObjectName;
		private  UUID m_ImageID;
		private  UUID m_ObjectID;
		private  String m_FirstName;
		private  String m_LastName;
		private  int m_Channel;
		private  List<String> m_ButtonLabels;
		private  UUID m_OwnerID;

		/// <summary>Get the dialog message</summary>
		public String getMessage() { return m_Message; } 
		/// <summary>Get the name of the object that sent the dialog request</summary>
		public String getObjectName() { return m_ObjectName; } 
		/// <summary>Get the ID of the image to be displayed</summary>
		public UUID getImageID() { return m_ImageID; } 
		/// <summary>Get the ID of the primitive sending the dialog</summary>
		public UUID getObjectID() { return m_ObjectID; } 
		/// <summary>Get the first name of the senders owner</summary>
		public String getFirstName() { return m_FirstName; } 
		/// <summary>Get the last name of the senders owner</summary>
		public String getLastName() { return m_LastName; } 
		/// <summary>Get the communication channel the dialog was sent on, responses
		/// should also send responses on this same channel</summary>
		public int getChannel() { return m_Channel; } 
		/// <summary>Get the String labels containing the options presented in this dialog</summary>
		public List<String> getButtonLabels() { return m_ButtonLabels; } 
		/// <summary>UUID of the scritped object owner</summary>
		public UUID getOwnerID() { return m_OwnerID; } 

		/// <summary>
		/// Construct a new instance of the ScriptDialogEventArgs
		/// </summary>
		/// <param name="message">The dialog message</param>
		/// <param name="objectName">The name of the object that sent the dialog request</param>
		/// <param name="imageID">The ID of the image to be displayed</param>
		/// <param name="objectID">The ID of the primitive sending the dialog</param>
		/// <param name="firstName">The first name of the senders owner</param>
		/// <param name="lastName">The last name of the senders owner</param>
		/// <param name="chatChannel">The communication channel the dialog was sent on</param>
		/// <param name="buttons">The String labels containing the options presented in this dialog</param>
		/// <param name="ownerID">UUID of the scritped object owner</param>
		public ScriptDialogEventArgs(String message, String objectName, UUID imageID,
				UUID objectID, String firstName, String lastName, int chatChannel, List<String> buttons, UUID ownerID)
		{
			this.m_Message = message;
			this.m_ObjectName = objectName;
			this.m_ImageID = imageID;
			this.m_ObjectID = objectID;
			this.m_FirstName = firstName;
			this.m_LastName = lastName;
			this.m_Channel = chatChannel;
			this.m_ButtonLabels = buttons;
			this.m_OwnerID = ownerID;
		}
	}
	
