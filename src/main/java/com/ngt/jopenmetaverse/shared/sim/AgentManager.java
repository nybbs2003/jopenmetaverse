package com.ngt.jopenmetaverse.shared.sim;

import java.math.BigInteger;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;

import com.ngt.jopenmetaverse.shared.cap.http.CapsHttpClient;
import com.ngt.jopenmetaverse.shared.cap.http.CapsHttpRequestCompletedArg;
import com.ngt.jopenmetaverse.shared.protocol.ActivateGesturesPacket;
import com.ngt.jopenmetaverse.shared.protocol.AgentAnimationPacket;
import com.ngt.jopenmetaverse.shared.protocol.AgentRequestSitPacket;
import com.ngt.jopenmetaverse.shared.protocol.AgentSitPacket;
import com.ngt.jopenmetaverse.shared.protocol.AgentUpdatePacket;
import com.ngt.jopenmetaverse.shared.protocol.ChatFromViewerPacket;
import com.ngt.jopenmetaverse.shared.protocol.CompleteAgentMovementPacket;
import com.ngt.jopenmetaverse.shared.protocol.DeactivateGesturesPacket;
import com.ngt.jopenmetaverse.shared.protocol.GenericMessagePacket;
import com.ngt.jopenmetaverse.shared.protocol.Helpers;
import com.ngt.jopenmetaverse.shared.protocol.ImprovedInstantMessagePacket;
import com.ngt.jopenmetaverse.shared.protocol.MoneyBalanceRequestPacket;
import com.ngt.jopenmetaverse.shared.protocol.MoneyTransferRequestPacket;
import com.ngt.jopenmetaverse.shared.protocol.ObjectDeGrabPacket;
import com.ngt.jopenmetaverse.shared.protocol.ObjectGrabPacket;
import com.ngt.jopenmetaverse.shared.protocol.ObjectGrabUpdatePacket;
import com.ngt.jopenmetaverse.shared.protocol.RetrieveInstantMessagesPacket;
import com.ngt.jopenmetaverse.shared.protocol.ScriptDialogReplyPacket;
import com.ngt.jopenmetaverse.shared.protocol.SetAlwaysRunPacket;
import com.ngt.jopenmetaverse.shared.protocol.TeleportLandmarkRequestPacket;
import com.ngt.jopenmetaverse.shared.protocol.ViewerEffectPacket;
import com.ngt.jopenmetaverse.shared.protocol.primitives.Primitive;
import com.ngt.jopenmetaverse.shared.sim.Avatar.ProfileFlags;
import com.ngt.jopenmetaverse.shared.sim.AvatarManager.AgentDisplayName;
import com.ngt.jopenmetaverse.shared.sim.GroupManager.ChatSessionMember;
import com.ngt.jopenmetaverse.shared.sim.GroupManager.GroupPowers;
import com.ngt.jopenmetaverse.shared.sim.ParcelManager.LandingType;
import com.ngt.jopenmetaverse.shared.sim.asset.AssetGesture;
import com.ngt.jopenmetaverse.shared.sim.events.EventArgs;
import com.ngt.jopenmetaverse.shared.sim.events.EventObserver;
import com.ngt.jopenmetaverse.shared.sim.events.ManualResetEvent;
import com.ngt.jopenmetaverse.shared.sim.events.nm.DisconnectedEventArgs;
import com.ngt.jopenmetaverse.shared.sim.events.nm.SimDisconnectedEventArgs;
import com.ngt.jopenmetaverse.shared.sim.login.LoginProgressEventArgs;
import com.ngt.jopenmetaverse.shared.sim.login.LoginResponseCallbackArg;
import com.ngt.jopenmetaverse.shared.sim.login.LoginResponseData;
import com.ngt.jopenmetaverse.shared.sim.login.LoginStatus;
import com.ngt.jopenmetaverse.shared.sim.message.LindenMessages.ChatSessionAcceptInvitation;
import com.ngt.jopenmetaverse.shared.sim.message.LindenMessages.ChatSessionRequestStartConference;
import com.ngt.jopenmetaverse.shared.structureddata.OSD;
import com.ngt.jopenmetaverse.shared.structureddata.OSDFormat;
import com.ngt.jopenmetaverse.shared.types.Color4;
import com.ngt.jopenmetaverse.shared.types.Enums.SaleType;
import com.ngt.jopenmetaverse.shared.types.Quaternion;
import com.ngt.jopenmetaverse.shared.types.UUID;
import com.ngt.jopenmetaverse.shared.types.Vector3;
import com.ngt.jopenmetaverse.shared.types.Vector3d;
import com.ngt.jopenmetaverse.shared.types.Vector4;
import com.ngt.jopenmetaverse.shared.util.JLogger;
import com.ngt.jopenmetaverse.shared.util.Utils;

public class AgentManager {

	/*Patch up code*/

	//region AgentUpdate Constants

	private final static int CONTROL_AT_POS_INDEX = 0;
	private final static int CONTROL_AT_NEG_INDEX = 1;
	private final static int CONTROL_LEFT_POS_INDEX = 2;
	private final static int CONTROL_LEFT_NEG_INDEX = 3;
	private final static int CONTROL_UP_POS_INDEX = 4;
	private final static int CONTROL_UP_NEG_INDEX = 5;
	private final static int CONTROL_PITCH_POS_INDEX = 6;
	private final static int CONTROL_PITCH_NEG_INDEX = 7;
	private final static int CONTROL_YAW_POS_INDEX = 8;
	private final static int CONTROL_YAW_NEG_INDEX = 9;
	private final static int CONTROL_FAST_AT_INDEX = 10;
	private final static int CONTROL_FAST_LEFT_INDEX = 11;
	private final static int CONTROL_FAST_UP_INDEX = 12;
	private final static int CONTROL_FLY_INDEX = 13;
	private final static int CONTROL_STOP_INDEX = 14;
	private final static int CONTROL_FINISH_ANIM_INDEX = 15;
	private final static int CONTROL_STAND_UP_INDEX = 16;
	private final static int CONTROL_SIT_ON_GROUND_INDEX = 17;
	private final static int CONTROL_MOUSELOOK_INDEX = 18;
	private final static int CONTROL_NUDGE_AT_POS_INDEX = 19;
	private final static int CONTROL_NUDGE_AT_NEG_INDEX = 20;
	private final static int CONTROL_NUDGE_LEFT_POS_INDEX = 21;
	private final static int CONTROL_NUDGE_LEFT_NEG_INDEX = 22;
	private final static int CONTROL_NUDGE_UP_POS_INDEX = 23;
	private final static int CONTROL_NUDGE_UP_NEG_INDEX = 24;
	private final static int CONTROL_TURN_LEFT_INDEX = 25;
	private final static int CONTROL_TURN_RIGHT_INDEX = 26;
	private final static int CONTROL_AWAY_INDEX = 27;
	private final static int CONTROL_LBUTTON_DOWN_INDEX = 28;
	private final static int CONTROL_LBUTTON_UP_INDEX = 29;
	private final static int CONTROL_ML_LBUTTON_DOWN_INDEX = 30;
	private final static int CONTROL_ML_LBUTTON_UP_INDEX = 31;
	private final static int TOTAL_CONTROLS = 32;

	//endregion AgentUpdate Constants


	//region Enums
	/// <summary>
	/// Used to specify movement actions for your agent
	/// </summary>
	//[Flags]
	public enum ControlFlags
	{
		//uint
		/// <summary>Empty flag</summary>
		NONE((long)0),
		/// <summary>Move Forward (SL Keybinding: W/Up Arrow)</summary>
		AGENT_CONTROL_AT_POS((long)0x1 << CONTROL_AT_POS_INDEX),
		/// <summary>Move Backward (SL Keybinding: S/Down Arrow)</summary>
		AGENT_CONTROL_AT_NEG((long)0x1 << CONTROL_AT_NEG_INDEX),
		/// <summary>Move Left (SL Keybinding: Shift-(A/Left Arrow))</summary>
		AGENT_CONTROL_LEFT_POS((long)0x1 << CONTROL_LEFT_POS_INDEX),
		/// <summary>Move Right (SL Keybinding: Shift-(D/Right Arrow))</summary>
		AGENT_CONTROL_LEFT_NEG((long)0x1 << CONTROL_LEFT_NEG_INDEX),
		/// <summary>Not Flying: Jump/Flying: Move Up (SL Keybinding: E)</summary>
		AGENT_CONTROL_UP_POS((long)0x1 << CONTROL_UP_POS_INDEX),
		/// <summary>Not Flying: Croutch/Flying: Move Down (SL Keybinding: C)</summary>
		AGENT_CONTROL_UP_NEG((long)0x1 << CONTROL_UP_NEG_INDEX),
		/// <summary>Unused</summary>
		AGENT_CONTROL_PITCH_POS((long)0x1 << CONTROL_PITCH_POS_INDEX),
		/// <summary>Unused</summary>
		AGENT_CONTROL_PITCH_NEG((long)0x1 << CONTROL_PITCH_NEG_INDEX),
		/// <summary>Unused</summary>
		AGENT_CONTROL_YAW_POS((long)0x1 << CONTROL_YAW_POS_INDEX),
		/// <summary>Unused</summary>
		AGENT_CONTROL_YAW_NEG((long)0x1 << CONTROL_YAW_NEG_INDEX),
		/// <summary>ORed with AGENT_CONTROL_AT_* if the keyboard is being used</summary>
		AGENT_CONTROL_FAST_AT((long)0x1 << CONTROL_FAST_AT_INDEX),
		/// <summary>ORed with AGENT_CONTROL_LEFT_* if the keyboard is being used</summary>
		AGENT_CONTROL_FAST_LEFT((long)0x1 << CONTROL_FAST_LEFT_INDEX),
		/// <summary>ORed with AGENT_CONTROL_UP_* if the keyboard is being used</summary>
		AGENT_CONTROL_FAST_UP((long)0x1 << CONTROL_FAST_UP_INDEX),
		/// <summary>Fly</summary>
		AGENT_CONTROL_FLY((long)0x1 << CONTROL_FLY_INDEX),
		/// <summary></summary>
		AGENT_CONTROL_STOP((long)0x1 << CONTROL_STOP_INDEX),
		/// <summary>Finish our current animation</summary>
		AGENT_CONTROL_FINISH_ANIM((long) 0x1 << CONTROL_FINISH_ANIM_INDEX),
		/// <summary>Stand up from the ground or a prim seat</summary>
		AGENT_CONTROL_STAND_UP((long)0x1 << CONTROL_STAND_UP_INDEX),
		/// <summary>Sit on the ground at our current location</summary>
		AGENT_CONTROL_SIT_ON_GROUND((long)0x1 << CONTROL_SIT_ON_GROUND_INDEX),
		/// <summary>Whether mouselook is currently enabled</summary>
		AGENT_CONTROL_MOUSELOOK((long)0x1 << CONTROL_MOUSELOOK_INDEX),
		/// <summary>Legacy, used if a key was pressed for less than a certain amount of time</summary>
		AGENT_CONTROL_NUDGE_AT_POS((long) 0x1 << CONTROL_NUDGE_AT_POS_INDEX),
		/// <summary>Legacy, used if a key was pressed for less than a certain amount of time</summary>
		AGENT_CONTROL_NUDGE_AT_NEG((long) 0x1 << CONTROL_NUDGE_AT_NEG_INDEX),
		/// <summary>Legacy, used if a key was pressed for less than a certain amount of time</summary>
		AGENT_CONTROL_NUDGE_LEFT_POS((long) 0x1 << CONTROL_NUDGE_LEFT_POS_INDEX),
		/// <summary>Legacy, used if a key was pressed for less than a certain amount of time</summary>
		AGENT_CONTROL_NUDGE_LEFT_NEG((long) 0x1 << CONTROL_NUDGE_LEFT_NEG_INDEX),
		/// <summary>Legacy, used if a key was pressed for less than a certain amount of time</summary>
		AGENT_CONTROL_NUDGE_UP_POS((long) 0x1 << CONTROL_NUDGE_UP_POS_INDEX),
		/// <summary>Legacy, used if a key was pressed for less than a certain amount of time</summary>
		AGENT_CONTROL_NUDGE_UP_NEG((long) 0x1 << CONTROL_NUDGE_UP_NEG_INDEX),
		/// <summary></summary>
		AGENT_CONTROL_TURN_LEFT((long)0x1 << CONTROL_TURN_LEFT_INDEX),
		/// <summary></summary>
		AGENT_CONTROL_TURN_RIGHT((long) 0x1 << CONTROL_TURN_RIGHT_INDEX),
		/// <summary>Set when the avatar is idled or set to away. Note that the away animation is 
		/// activated separately from setting this flag</summary>
		AGENT_CONTROL_AWAY((long)0x1 << CONTROL_AWAY_INDEX),
		/// <summary></summary>
		AGENT_CONTROL_LBUTTON_DOWN((long) 0x1 << CONTROL_LBUTTON_DOWN_INDEX),
		/// <summary></summary>
		AGENT_CONTROL_LBUTTON_UP((long) 0x1 << CONTROL_LBUTTON_UP_INDEX),
		/// <summary></summary>
		AGENT_CONTROL_ML_LBUTTON_DOWN((long) 0x1 << CONTROL_ML_LBUTTON_DOWN_INDEX),
		/// <summary></summary>
		AGENT_CONTROL_ML_LBUTTON_UP((long) 0x1 << CONTROL_ML_LBUTTON_UP_INDEX);

		private long index;
		ControlFlags(long index)
		{
			this.index = index;
		}     

		public long getIndex()
		{
			return index;
		}  

		private static final Map<Long,ControlFlags> lookup  = new HashMap<Long,ControlFlags>();

		static {
			for(ControlFlags s : EnumSet.allOf(ControlFlags.class))
				lookup.put(s.getIndex(), s);
		}

		public static ControlFlags get(Long index)
		{
			return lookup.get(index);
		}

	}

	//endregion Enums


	/*Patch up code*/


	/// <summary>
	/// Permission request flags, asked when a script wants to control an Avatar
	/// </summary>
	//    [Flags]
	public enum ScriptPermission 
	{
		/// <summary>Placeholder for empty values, shouldn't ever see this</summary>
		None(0),
		/// <summary>Script wants ability to take money from you</summary>
		Debit(1 << 1),
		/// <summary>Script wants to take camera controls for you</summary>
		TakeControls (1 << 2),
		/// <summary>Script wants to remap avatars controls</summary>
		RemapControls (1 << 3),
		/// <summary>Script wants to trigger avatar animations</summary>
		/// <remarks>This function is not implemented on the grid</remarks>
		TriggerAnimation (1 << 4),
		/// <summary>Script wants to attach or detach the prim or primset to your avatar</summary>
		Attach (1 << 5),
		/// <summary>Script wants permission to release ownership</summary>
		/// <remarks>This function is not implemented on the grid
		/// The concept of "public" objects does not exist anymore.</remarks>
		ReleaseOwnership (1 << 6),
		/// <summary>Script wants ability to link/delink with other prims</summary>
		ChangeLinks (1 << 7),
		/// <summary>Script wants permission to change joints</summary>
		/// <remarks>This function is not implemented on the grid</remarks>
		ChangeJoints (1 << 8),
		/// <summary>Script wants permissions to change permissions</summary>
		/// <remarks>This function is not implemented on the grid</remarks>
		ChangePermissions (1 << 9),
		/// <summary>Script wants to track avatars camera position and rotation </summary>
		TrackCamera (1 << 10),
		/// <summary>Script wants to control your camera</summary>
		ControlCamera (1 << 11);
		private int index;
		ScriptPermission(int index)
		{
			this.index = index;
		}     

		public int getIndex()
		{
			return index;
		}

		private static final Map<Integer,ScriptPermission> lookup  
		= new HashMap<Integer,ScriptPermission>();

		static {
			for(ScriptPermission s : EnumSet.allOf(ScriptPermission.class))
				lookup.put(s.getIndex(), s);
		}

		public static ScriptPermission get(Integer index)
		{
			return lookup.get(index);
		}
	}


	/// <summary>
	/// Special commands used in Instant Messages
	/// </summary>
	public enum InstantMessageDialog
	{
		//ubyte
		/// <summary>Indicates a regular IM from another agent</summary>
		MessageFromAgent((byte)0),
		/// <summary>Simple notification box with an OK button</summary>
		MessageBox((byte)1),
		// <summary>Used to show a countdown notification with an OK
		// button, deprecated now</summary>
		//[Obsolete]
		//MessageBoxCountdown = 2,
		/// <summary>You've been invited to join a group.</summary>
		GroupInvitation((byte)3),
		/// <summary>Inventory offer</summary>
		InventoryOffered((byte)4),
		/// <summary>Accepted inventory offer</summary>
		InventoryAccepted((byte)5),
		/// <summary>Declined inventory offer</summary>
		InventoryDeclined((byte)6),
		/// <summary>Group vote</summary>
		GroupVote((byte)7),
		// <summary>A message to everyone in the agent's group, no longer
		// used</summary>
		//[Obsolete]
		//DeprecatedGroupMessage = 8,
		/// <summary>An object is offering its inventory</summary>
		TaskInventoryOffered((byte)9),
		/// <summary>Accept an inventory offer from an object</summary>
		TaskInventoryAccepted((byte)10),
		/// <summary>Decline an inventory offer from an object</summary>
		TaskInventoryDeclined((byte)11),
		/// <summary>Unknown</summary>
		NewUserDefault((byte)12),
		/// <summary>Start a session, or add users to a session</summary>
		SessionAdd((byte)13),
		/// <summary>Start a session, but don't prune offline users</summary>
		SessionOfflineAdd((byte)14),
		/// <summary>Start a session with your group</summary>
		SessionGroupStart((byte)15),
		/// <summary>Start a session without a calling card (finder or objects)</summary>
		SessionCardlessStart((byte)16),
		/// <summary>Send a message to a session</summary>
		SessionSend((byte)17),
		/// <summary>Leave a session</summary>
		SessionDrop((byte)18),
		/// <summary>Indicates that the IM is from an object</summary>
		MessageFromObject((byte)19),
		/// <summary>Sent an IM to a busy user, this is the auto response</summary>
		BusyAutoResponse((byte)20),
		/// <summary>Shows the message in the console and chat history</summary>
		ConsoleAndChatHistory((byte)21),
		/// <summary>Send a teleport lure</summary>
		RequestTeleport((byte)22),
		/// <summary>Response sent to the agent which inititiated a teleport invitation</summary>
		AcceptTeleport((byte)23),
		/// <summary>Response sent to the agent which inititiated a teleport invitation</summary>
		DenyTeleport((byte)24),
		/// <summary>Only useful if you have Linden permissions</summary>
		GodLikeRequestTeleport((byte)25),
		/// <summary>A placeholder type for future expansion, currently not
		/// used</summary>
		CurrentlyUnused((byte)26),
		// <summary>Notification of a new group election, this is 
		// deprecated</summary>
		//[Obsolete]
		//DeprecatedGroupElection = 27,
		/// <summary>IM to tell the user to go to an URL</summary>
		GotoUrl((byte)28),
		/// <summary>IM for help</summary>
		Session911Start((byte)29),
		/// <summary>IM sent automatically on call for help, sends a lure 
		/// to each Helper reached</summary>
		Lure911((byte)30),
		/// <summary>Like an IM but won't go to email</summary>
		FromTaskAsAlert((byte)31),
		/// <summary>IM from a group officer to all group members</summary>
		GroupNotice((byte)32),
		/// <summary>Unknown</summary>
		GroupNoticeInventoryAccepted((byte)33),
		/// <summary>Unknown</summary>
		GroupNoticeInventoryDeclined((byte)34),
		/// <summary>Accept a group invitation</summary>
		GroupInvitationAccept((byte)35),
		/// <summary>Decline a group invitation</summary>
		GroupInvitationDecline((byte)36),
		/// <summary>Unknown</summary>
		GroupNoticeRequested((byte)37),
		/// <summary>An avatar is offering you friendship</summary>
		FriendshipOffered((byte)38),
		/// <summary>An avatar has accepted your friendship offer</summary>
		FriendshipAccepted((byte)39),
		/// <summary>An avatar has declined your friendship offer</summary>
		FriendshipDeclined((byte)40),
		/// <summary>Indicates that a user has started typing</summary>
		StartTyping((byte)41),
		/// <summary>Indicates that a user has stopped typing</summary>
		StopTyping((byte)42);
		private byte index;
		InstantMessageDialog(byte index)
		{
			this.index = index;
		}     

		public byte getIndex()
		{
			return index;
		}

		private static final Map<Byte,InstantMessageDialog> lookup  = new HashMap<Byte,InstantMessageDialog>();

		static {
			for(InstantMessageDialog s : EnumSet.allOf(InstantMessageDialog.class))
				lookup.put(s.getIndex(), s);
		}

		public static InstantMessageDialog get(Byte index)
		{
			return lookup.get(index);
		}
	}

	/// <summary>
	/// Flag in Instant Messages, whether the IM should be delivered to
	/// offline avatars as well
	/// </summary>
	public enum InstantMessageOnline
	{
		/// <summary>Only deliver to online avatars</summary>
		Online (0),
		/// <summary>If the avatar is offline the message will be held until
		/// they login next, and possibly forwarded to their e-mail account</summary>
		Offline (1);
		private int index;
		InstantMessageOnline(int index)
		{
			this.index = index;
		}     

		public int getIndex()
		{
			return index;
		}

		private static final Map<Integer,InstantMessageOnline> lookup  = new HashMap<Integer,InstantMessageOnline>();

		static {
			for(InstantMessageOnline s : EnumSet.allOf(InstantMessageOnline.class))
				lookup.put(s.getIndex(), s);
		}

		public static InstantMessageOnline get(Integer index)
		{
			return lookup.get(index);
		}
	}


	/// <summary>
	/// Conversion type to denote Chat Packet types in an easier-to-understand format
	/// </summary>
	public enum ChatType
	{
		//ubyte
		/// <summary>Whisper (5m radius)</summary>
		Whisper((byte)0),
		/// <summary>Normal chat (10/20m radius), what the official viewer typically sends</summary>
		Normal((byte)1),
		/// <summary>Shouting! (100m radius)</summary>
		Shout((byte)2),
		// <summary>Say chat (10/20m radius) - The official viewer will 
		// print "[4:15] You say, hey" instead of "[4:15] You: hey"</summary>
		//[Obsolete]
		//Say = 3,
		/// <summary>Event message when an Avatar has begun to type</summary>
		StartTyping((byte)4),
		/// <summary>Event message when an Avatar has stopped typing</summary>
		StopTyping((byte)5),
		/// <summary>Send the message to the debug channel</summary>
		Debug((byte)6),
		/// <summary>Event message when an object uses llOwnerSay</summary>
		OwnerSay((byte)8),
		/// <summary>Special value to support llRegionSay, never sent to the client</summary>
		RegionSay((byte)Byte.MAX_VALUE);
		private byte index;
		ChatType(byte index)
		{
			this.index = index;
		}     

		public byte getIndex()
		{
			return index;
		}

		//			private static final Map<Byte,ChatType> lookup  = new HashMap<Byte,ChatType>();
		//
		//			static {
		//				for(ChatType s : EnumSet.allOf(ChatType.class))
		//					lookup.put(s.getIndex(), s);
		//			}
		//
		//			public static ChatType get(Byte index)
		//			{
		//				return lookup.get(index);
		//			}
	}

	/// <summary>
	/// Identifies the source of a chat message
	/// </summary>
	public enum ChatSourceType
	{
		//ubyte
		/// <summary>Chat from the grid or simulator</summary>
		System((byte)0),
		/// <summary>Chat from another avatar</summary>
		Agent((byte)1),
		/// <summary>Chat from an object</summary>
		Object((byte)2);
		private byte index;
		ChatSourceType(byte index)
		{
			this.index = index;
		}     

		public byte getIndex()
		{
			return index;
		}

		//			private static final Map<Byte,ChatSourceType> lookup  = new HashMap<Byte,ChatSourceType>();
		//
		//			static {
		//				for(ChatType s : EnumSet.allOf(ChatSourceType.class))
		//					lookup.put(s.getIndex(), s);
		//			}
		//
		//			public static ChatSourceType get(Byte index)
		//			{
		//				return lookup.get(index);
		//			}
	}

	/// <summary>
	/// 
	/// </summary>
	public enum ChatAudibleLevel
	{
		/// <summary></summary>
		Not((byte)-1),
		/// <summary></summary>
		Barely((byte)0),
		/// <summary></summary>
		Fully((byte)1);
		private byte index;
		ChatAudibleLevel(byte index)
		{
			this.index = index;
		}     

		public byte getIndex()
		{
			return index;
		}

		//			private static final Map<Byte,ChatAudibleLevel> lookup  = new HashMap<Byte,ChatAudibleLevel>();
		//
		//			static {
		//				for(ChatType s : EnumSet.allOf(ChatAudibleLevel.class))
		//					lookup.put(s.getIndex(), s);
		//			}
		//
		//			public static ChatAudibleLevel get(Byte index)
		//			{
		//				return lookup.get(index);
		//			}
	}

	/// <summary>
	/// Effect type used in ViewerEffect packets
	/// </summary>
	public enum EffectType
	{
		//ubyte
		/// <summary></summary>
		Text((byte)0),
		/// <summary></summary>
		Icon((byte)1),
		/// <summary></summary>
		Connector((byte)2),
		/// <summary></summary>
		FlexibleObject((byte)3),
		/// <summary></summary>
		AnimalControls((byte)4),
		/// <summary></summary>
		AnimationObject((byte)5),
		/// <summary></summary>
		Cloth((byte)6),
		/// <summary>Project a beam from a source to a destination, such as
		/// the one used when editing an object</summary>
		Beam((byte)7),
		/// <summary></summary>
		Glow((byte)8),
		/// <summary></summary>
		Point((byte)9),
		/// <summary></summary>
		Trail((byte)10),
		/// <summary>Create a swirl of particles around an object</summary>
		Sphere((byte)11),
		/// <summary></summary>
		Spiral((byte)12),
		/// <summary></summary>
		Edit((byte)13),
		/// <summary>Cause an avatar to look at an object</summary>
		LookAt((byte)14),
		/// <summary>Cause an avatar to point at an object</summary>
		PointAt((byte)15);
		private byte index;
		EffectType(byte index)
		{
			this.index = index;
		}     

		public byte getIndex()
		{
			return index;
		}

		//			private static final Map<Byte,EffectType> lookup  = new HashMap<Byte,EffectType>();
		//
		//			static {
		//				for(ChatType s : EnumSet.allOf(EffectType.class))
		//					lookup.put(s.getIndex(), s);
		//			}
		//
		//			public static EffectType get(Byte index)
		//			{
		//				return lookup.get(index);
		//			}
	}

	/// <summary>
	/// The action an avatar is doing when looking at something, used in 
	/// ViewerEffect packets for the LookAt effect
	/// </summary>
	public enum LookAtType
	{
		/// <summary></summary>
		None((byte)0),
		/// <summary></summary>
		Idle((byte)1),
		/// <summary></summary>
		AutoListen((byte)2),
		/// <summary></summary>
		FreeLook((byte)3),
		/// <summary></summary>
		Respond((byte)4),
		/// <summary></summary>
		Hover((byte)5),
		/// <summary>Deprecated</summary>
		//	        [Obsolete]
		Conversation((byte)6),
		/// <summary></summary>
		Select((byte)7),
		/// <summary></summary>
		Focus((byte)8),
		/// <summary></summary>
		Mouselook((byte)9),
		/// <summary></summary>
		Clear((byte)10);
		private byte index;
		LookAtType(byte index)
		{
			this.index = index;
		}     

		public byte getIndex()
		{
			return index;
		}

		//			private static final Map<Byte,LookAtType> lookup  = new HashMap<Byte,LookAtType>();
		//
		//			static {
		//				for(ChatType s : EnumSet.allOf(LookAtType.class))
		//					lookup.put(s.getIndex(), s);
		//			}
		//
		//			public static LookAtType get(Byte index)
		//			{
		//				return lookup.get(index);
		//			}
	}

	/// <summary>
	/// The action an avatar is doing when pointing at something, used in
	/// ViewerEffect packets for the PointAt effect
	/// </summary>
	public enum PointAtType
	{
		/// <summary></summary>
		None((byte)0),
		/// <summary></summary>
		Select((byte)1),
		/// <summary></summary>
		Grab((byte)2),
		/// <summary></summary>
		Clear((byte)3);
		private byte index;
		PointAtType(byte index)
		{
			this.index = index;
		}     

		public byte getIndex()
		{
			return index;
		}

		//			private static final Map<Byte,PointAtType> lookup  = new HashMap<Byte,PointAtType>();
		//
		//			static {
		//				for(ChatType s : EnumSet.allOf(PointAtType.class))
		//					lookup.put(s.getIndex(), s);
		//			}
		//
		//			public static PointAtType get(Byte index)
		//			{
		//				return lookup.get(index);
		//			}
	}

	/// <summary>
	/// Money transaction types
	/// </summary>
	public enum MoneyTransactionType
	{
		/// <summary></summary>
		None(0),
		/// <summary></summary>
		FailSimulatorTimeout(1),
		/// <summary></summary>
		FailDataserverTimeout(2),
		/// <summary></summary>
		ObjectClaim(1000),
		/// <summary></summary>
		LandClaim(1001),
		/// <summary></summary>
		GroupCreate(1002),
		/// <summary></summary>
		ObjectPublicClaim(1003),
		/// <summary></summary>
		GroupJoin(1004),
		/// <summary></summary>
		TeleportCharge(1100),
		/// <summary></summary>
		UploadCharge(1101),
		/// <summary></summary>
		LandAuction(1102),
		/// <summary></summary>
		ClassifiedCharge(1103),
		/// <summary></summary>
		ObjectTax(2000),
		/// <summary></summary>
		LandTax(2001),
		/// <summary></summary>
		LightTax(2002),
		/// <summary></summary>
		ParcelDirFee(2003),
		/// <summary></summary>
		GroupTax(2004),
		/// <summary></summary>
		ClassifiedRenew(2005),
		/// <summary></summary>
		GiveInventory(3000),
		/// <summary></summary>
		ObjectSale(5000),
		/// <summary></summary>
		Gift(5001),
		/// <summary></summary>
		LandSale(5002),
		/// <summary></summary>
		ReferBonus(5003),
		/// <summary></summary>
		InventorySale(5004),
		/// <summary></summary>
		RefundPurchase(5005),
		/// <summary></summary>
		LandPassSale(5006),
		/// <summary></summary>
		DwellBonus(5007),
		/// <summary></summary>
		PayObject(5008),
		/// <summary></summary>
		ObjectPays(5009),
		/// <summary></summary>
		GroupLandDeed(6001),
		/// <summary></summary>
		GroupObjectDeed(6002),
		/// <summary></summary>
		GroupLiability(6003),
		/// <summary></summary>
		GroupDividend(6004),
		/// <summary></summary>
		GroupMembershipDues(6005),
		/// <summary></summary>
		ObjectRelease(8000),
		/// <summary></summary>
		LandRelease(8001),
		/// <summary></summary>
		ObjectDelete(8002),
		/// <summary></summary>
		ObjectPublicDecay(8003),
		/// <summary></summary>
		ObjectPublicDelete(8004),
		/// <summary></summary>
		LindenAdjustment(9000),
		/// <summary></summary>
		LindenGrant(9001),
		/// <summary></summary>
		LindenPenalty(9002),
		/// <summary></summary>
		EventFee(9003),
		/// <summary></summary>
		EventPrize(9004),
		/// <summary></summary>
		StipendBasic(10000),
		/// <summary></summary>
		StipendDeveloper(10001),
		/// <summary></summary>
		StipendAlways(10002),
		/// <summary></summary>
		StipendDaily(10003),
		/// <summary></summary>
		StipendRating(10004),
		/// <summary></summary>
		StipendDelta(10005);

		private int index;
		MoneyTransactionType(int index)
		{
			this.index = index;
		}     

		public int getIndex()
		{
			return index;
		}

		private static final Map<Integer,MoneyTransactionType> lookup  
		= new HashMap<Integer,MoneyTransactionType>();

		static {
			for(MoneyTransactionType s : EnumSet.allOf(MoneyTransactionType.class))
				lookup.put(s.getIndex(), s);
		}

		public static MoneyTransactionType get(Integer index)
		{
			return lookup.get(index);
		}
	}
	/// <summary>
	/// 
	/// </summary>
	//	    [Flags]
	public enum TransactionFlags
	{
		/// <summary></summary>
		None((byte)0),
		/// <summary></summary>
		SourceGroup((byte)1),
		/// <summary></summary>
		DestGroup((byte)2),
		/// <summary></summary>
		OwnerGroup((byte)4),
		/// <summary></summary>
		SimultaneousContribution((byte)8),
		/// <summary></summary>
		ContributionRemoval((byte)16);
		private byte index;
		TransactionFlags(byte index)
		{
			this.index = index;
		}     

		public byte getIndex()
		{
			return index;
		}

		//			private static final Map<Byte,TransactionFlags> lookup  = new HashMap<Byte,TransactionFlags>();
		//
		//			static {
		//				for(ChatType s : EnumSet.allOf(TransactionFlags.class))
		//					lookup.put(s.getIndex(), s);
		//			}
		//
		//			public static TransactionFlags get(Byte index)
		//			{
		//				return lookup.get(index);
		//			}
	}
	/// <summary>
	/// 
	/// </summary>
	public enum MeanCollisionType
	{
		/// <summary></summary>
		None((byte)0),
		/// <summary></summary>
		Bump((byte)1),
		/// <summary></summary>
		LLPushObject((byte)2),
		/// <summary></summary>
		SelectedObjectCollide((byte)3),
		/// <summary></summary>
		ScriptedObjectCollide((byte)4),
		/// <summary></summary>
		PhysicalObjectCollide((byte)5);
		private byte index;
		MeanCollisionType(byte index)
		{
			this.index = index;
		}     

		public byte getIndex()
		{
			return index;
		}

		//			private static final Map<Byte,MeanCollisionType> lookup  = new HashMap<Byte,MeanCollisionType>();
		//
		//			static {
		//				for(ChatType s : EnumSet.allOf(MeanCollisionType.class))
		//					lookup.put(s.getIndex(), s);
		//			}
		//
		//			public static MeanCollisionType get(Byte index)
		//			{
		//				return lookup.get(index);
		//			}
	}

	/// <summary>
	/// Flags sent when a script takes or releases a control
	/// </summary>
	/// <remarks>NOTE: (need to verify) These might be a subset of the ControlFlags enum in Movement,</remarks>
	//	    [Flags]
	public enum ScriptControlChange
	{
		//uint
		/// <summary>No Flags set</summary>
		None((long)0),
		/// <summary>Forward (W or up Arrow)</summary>
		Forward((long)1),
		/// <summary>Back (S or down arrow)</summary>
		Back((long)2),
		/// <summary>Move left (shift+A or left arrow)</summary>
		Left((long)4),
		/// <summary>Move right (shift+D or right arrow)</summary>
		Right((long)8),
		/// <summary>Up (E or PgUp)</summary>
		Up((long)16),
		/// <summary>Down (C or PgDown)</summary>
		Down((long)32),
		/// <summary>Rotate left (A or left arrow)</summary>
		RotateLeft((long)256),
		/// <summary>Rotate right (D or right arrow)</summary>
		RotateRight((long)512),
		/// <summary>Left Mouse Button</summary>
		LeftButton((long)268435456),
		/// <summary>Left Mouse button in MouseLook</summary>
		MouseLookLeftButton((long)1073741824);
		private long index;
		ScriptControlChange(long index)
		{
			this.index = index;
		}     

		public long getIndex()
		{
			return index;
		}  

		//			private static final Map<Long,ScriptControlChange> lookup  = new HashMap<Long,ScriptControlChange>();
		//
		//			static {
		//				for(ScriptControlChange s : EnumSet.allOf(ScriptControlChange.class))
		//					lookup.put(s.getIndex(), s);
		//			}
		//
		//			public static ScriptControlChange get(Long index)
		//			{
		//				return lookup.get(index);
		//			}
	}

	/// <summary>
	/// Currently only used to hide your group title
	/// </summary>
	public enum AgentFlags
	{
		/// <summary>No flags set</summary>
		None((byte)0),
		/// <summary>Hide your group title</summary>
		HideTitle((byte)0x01);
		private byte index;
		AgentFlags(byte index)
		{
			this.index = index;
		}     

		public byte getIndex()
		{
			return index;
		} 
	}

	/// <summary>
	/// Action state of the avatar, which can currently be typing and
	/// editing
	/// </summary>
	//	    [Flags]
	public enum AgentState 
	{
		/// <summary></summary>
		None((byte)0x00),
		/// <summary></summary>
		Typing((byte)0x04),
		/// <summary></summary>
		Editing((byte)0x10);

		private byte index;
		AgentState(byte index)
		{
			this.index = index;
		}     

		public byte getIndex()
		{
			return index;
		}  
	}

	/// <summary>
	/// Current teleport status
	/// </summary>
	public enum TeleportStatus
	{
		/// <summary>Unknown status</summary>
		None,
		/// <summary>Teleport initialized</summary>
		Start,
		/// <summary>Teleport in progress</summary>
		Progress,
		/// <summary>Teleport failed</summary>
		Failed,
		/// <summary>Teleport completed</summary>
		Finished,
		/// <summary>Teleport cancelled</summary>
		Cancelled
	}

	/// <summary>
	/// 
	/// </summary>
	//	    [Flags]
	public enum TeleportFlags
	{
		//uint
		/// <summary>No flags set, or teleport failed</summary>
		Default((long)0),
		/// <summary>Set when newbie leaves help island for first time</summary>
		SetHomeToTarget((long)1 << 0),
		/// <summary></summary>
		SetLastToTarget((long)1 << 1),
		/// <summary>Via Lure</summary>
		ViaLure((long)1 << 2),
		/// <summary>Via Landmark</summary>
		ViaLandmark((long)1 << 3),
		/// <summary>Via Location</summary>
		ViaLocation((long)1 << 4),
		/// <summary>Via Home</summary>
		ViaHome((long)1 << 5),
		/// <summary>Via Telehub</summary>
		ViaTelehub((long)1 << 6),
		/// <summary>Via Login</summary>
		ViaLogin((long)1 << 7),
		/// <summary>Linden Summoned</summary>
		ViaGodlikeLure((long)1 << 8),
		/// <summary>Linden Forced me</summary>
		Godlike((long)1 << 9),
		/// <summary></summary>
		NineOneOne((long)1 << 10),
		/// <summary>Agent Teleported Home via Script</summary>
		DisableCancel((long)1 << 11),
		/// <summary></summary>
		ViaRegionID((long)1 << 12),
		/// <summary></summary>
		IsFlying((long)1 << 13),
		/// <summary></summary>
		ResetHome((long)1 << 14),
		/// <summary>forced to new location for example when avatar is banned or ejected</summary>
		ForceRedirect((long)1 << 15),
		/// <summary>Teleport Finished via a Lure</summary>
		FinishedViaLure((long)1 << 26),
		/// <summary>Finished, Sim Changed</summary>
		FinishedViaNewSim((long)1 << 28),
		/// <summary>Finished, Same Sim</summary>
		FinishedViaSameSim((long)1 << 29);
		private long index;
		TeleportFlags(long index)
		{
			this.index = index;
		}     

		public long getIndex()
		{
			return index;
		}  

		private static final Map<Long,TeleportFlags> lookup  = new HashMap<Long,TeleportFlags>();

		static {
			for(TeleportFlags s : EnumSet.allOf(TeleportFlags.class))
				lookup.put(s.getIndex(), s);
		}

		public static TeleportFlags get(Long index)
		{
			return lookup.get(index);
		}
	}

	/// <summary>
	/// 
	/// </summary>
	//	    [Flags]
	public enum TeleportLureFlags
	{
		/// <summary></summary>
		NormalLure(0),
		/// <summary></summary>
		GodlikeLure(1),
		/// <summary></summary>
		GodlikePursuit(2);
		private int index;
		TeleportLureFlags(int index)
		{
			this.index = index;
		}     

		public int getIndex()
		{
			return index;
		}

		//			private static final Map<Integer,TeleportLureFlags> lookup  
		//			= new HashMap<Integer,TeleportLureFlags>();
		//
		//			static {
		//				for(TeleportLureFlags s : EnumSet.allOf(TeleportLureFlags.class))
		//					lookup.put(s.getIndex(), s);
		//			}
		//
		//			public static TeleportLureFlags get(Integer index)
		//			{
		//				return lookup.get(index);
		//			}
	}

	/// <summary>
	/// 
	/// </summary>
	//	    [Flags]
	public enum ScriptSensorTypeFlags
	{
		/// <summary></summary>
		Agent(1),
		/// <summary></summary>
		Active(2),
		/// <summary></summary>
		Passive(4),
		/// <summary></summary>
		Scripted(8);
		private int index;
		ScriptSensorTypeFlags(int index)
		{
			this.index = index;
		}     

		public int getIndex()
		{
			return index;
		}

		//			private static final Map<Integer,ScriptSensorTypeFlags> lookup  
		//			= new HashMap<Integer,ScriptSensorTypeFlags>();
		//
		//			static {
		//				for(ScriptSensorTypeFlags s : EnumSet.allOf(ScriptSensorTypeFlags.class))
		//					lookup.put(s.getIndex(), s);
		//			}
		//
		//			public static ScriptSensorTypeFlags get(Integer index)
		//			{
		//				return lookup.get(index);
		//			}
	}

	/// <summary>
	/// Type of mute entry
	/// </summary>
	public enum MuteType
	{
		/// <summary>Object muted by name</summary>
		ByName(0),
		/// <summary>Muted residet</summary>
		Resident(1),
		/// <summary>Object muted by UUID</summary>
		Object(2),
		/// <summary>Muted group</summary>
		Group(3),
		/// <summary>Muted external entry</summary>
		External(4);
		private int index;
		MuteType(int index)
		{
			this.index = index;
		}     

		public int getIndex()
		{
			return index;
		}

		//			private static final Map<Integer,MuteType> lookup  
		//			= new HashMap<Integer,MuteType>();
		//
		//			static {
		//				for(MuteType s : EnumSet.allOf(MuteType.class))
		//					lookup.put(s.getIndex(), s);
		//			}
		//
		//			public static MuteType get(Integer index)
		//			{
		//				return lookup.get(index);
		//			}
	}

	/// <summary>
	/// Flags of mute entry
	/// </summary>
	//	    [Flags]
	public enum MuteFlags
	{
		/// <summary>No exceptions</summary>
		Default(0x0),
		/// <summary>Don't mute text chat</summary>
		TextChat(0x1),
		/// <summary>Don't mute voice chat</summary>
		VoiceChat (0x2),
		/// <summary>Don't mute particles</summary>
		Particles (0x4),
		/// <summary>Don't mute sounds</summary>
		ObjectSounds (0x8),
		/// <summary>Don't mute</summary>
		All (0xf);
		private int index;
		MuteFlags(int index)
		{
			this.index = index;
		}     

		public int getIndex()
		{
			return index;
		}

		//			private static final Map<Integer,MuteFlags> lookup  
		//			= new HashMap<Integer,MuteFlags>();
		//
		//			static {
		//				for(MuteFlags s : EnumSet.allOf(MuteFlags.class))
		//					lookup.put(s.getIndex(), s);
		//			}
		//
		//			public static MuteFlags get(Integer index)
		//			{
		//				return lookup.get(index);
		//			}
	}
	//endregion Enums

	//region Structs

	/// <summary>
	/// Instant Message
	/// </summary>
	//struct
	public  class InstantMessage
	{
		/// <summary>Key of sender</summary>
		public UUID FromAgentID;
		/// <summary>Name of sender</summary>
		public String FromAgentName;
		/// <summary>Key of destination avatar</summary>
		public UUID ToAgentID;
		/// <summary>ID of originating estate</summary>
		//uint
		public long ParentEstateID;
		/// <summary>Key of originating region</summary>
		public UUID RegionID;
		/// <summary>Coordinates in originating region</summary>
		public Vector3 Position;
		/// <summary>Instant message type</summary>
		public InstantMessageDialog Dialog;
		/// <summary>Group IM session toggle</summary>
		public boolean GroupIM;
		/// <summary>Key of IM session, for Group Messages, the groups UUID</summary>
		public UUID IMSessionID;
		/// <summary>Timestamp of the instant message</summary>
		public Date Timestamp;
		/// <summary>Instant message text</summary>
		public String Message;
		/// <summary>Whether this message is held for offline avatars</summary>
		public InstantMessageOnline Offline;
		/// <summary>Context specific packed data</summary>
		public byte[] BinaryBucket;

		/// <summary>Print the struct data as a string</summary>
		/// <returns>A String containing the field name, and field value</returns>
		@Override
		public String toString()
		{
			try {
				return Helpers.StructToString(this);
			} catch (Exception e) {
				JLogger.warn("Error " + Utils.getExceptionStackTraceAsString(e));
			}
			return super.toString();
		}
	}

	/// <summary>Represents muted object or resident</summary>
	public class MuteEntry
	{
		/// <summary>Type of the mute entry</summary>
		public MuteType Type;
		/// <summary>UUID of the mute etnry</summary>
		public UUID ID;
		/// <summary>Mute entry name</summary>
		public String Name;
		/// <summary>Mute flags</summary>
		public MuteFlags Flags;
	}

	/// <summary>Transaction detail sent with MoneyBalanceReply message</summary>
	public class TransactionInfo
	{
		/// <summary>Type of the transaction</summary>
		public int TransactionType; // FIXME: this should be an enum
		/// <summary>UUID of the transaction source</summary>
		public UUID SourceID;
		/// <summary>Is the transaction source a group</summary>
		public boolean IsSourceGroup;
		/// <summary>UUID of the transaction destination</summary>
		public UUID DestID;
		/// <summary>Is transaction destination a group</summary>
		public boolean IsDestGroup;
		/// <summary>Transaction amount</summary>
		public int Amount;
		/// <summary>Transaction description</summary>
		public String ItemDescription;
	}
	//endregion Structs

	/// <summary>
	/// Manager class for our own avatar
	/// </summary>
	//public partial class AgentManager

	//	        //region Delegates
	//	        /// <summary>
	//	        /// Called once attachment resource usage information has been collected
	//	        /// </summary>
	//	        /// <param name="success">Indicates if operation was successfull</param>
	//	        /// <param name="info">Attachment resource usage information</param>
	//	        public delegate void AttachmentResourcesCallback(boolean success, AttachmentResourcesMessage info);
	//	        //endregion Delegates
	//	
	//	        //region Event Delegates
	//	
	//	        /// <summary>The event subscribers. null if no subcribers</summary>
	//	        private EventHandler<ChatEventArgs> m_Chat;
	//	
	//	        /// <summary>Raises the ChatFromSimulator event</summary>
	//	        /// <param name="e">A ChatEventArgs object containing the
	//	        /// data returned from the data server</param>
	//	        protected virtual void OnChat(ChatEventArgs e)
	//	        {
	//	            EventHandler<ChatEventArgs> handler = m_Chat;
	//	            if (handler != null)
	//	                handler(this, e);
	//	        }
	//	
	//	        /// <summary>Thread sync lock object</summary>
	//	        private readonly object m_ChatLock = new object();
	//	
	//	        /// <summary>Raised when a scripted object or agent within range sends a public message</summary>
	//	        public event EventHandler<ChatEventArgs> ChatFromSimulator
	//	        {
	//	            add { synchronized (m_ChatLock) { m_Chat += value; } }
	//	            remove { synchronized (m_ChatLock) { m_Chat -= value; } }
	//	        }
	//	
	//	        /// <summary>The event subscribers. null if no subcribers</summary>
	//	        private EventHandler<ScriptDialogEventArgs> m_ScriptDialog;
	//	
	//	        /// <summary>Raises the ScriptDialog event</summary>
	//	        /// <param name="e">A SctriptDialogEventArgs object containing the
	//	        /// data returned from the data server</param>
	//	        protected virtual void OnScriptDialog(ScriptDialogEventArgs e)
	//	        {
	//	            EventHandler<ScriptDialogEventArgs> handler = m_ScriptDialog;
	//	            if (handler != null)
	//	                handler(this, e);
	//	        }
	//	
	//	        /// <summary>Thread sync lock object</summary>
	//	        private readonly object m_ScriptDialogLock = new object();
	//	        /// <summary>Raised when a scripted object sends a dialog box containing possible
	//	        /// options an agent can respond to</summary>
	//	        public event EventHandler<ScriptDialogEventArgs> ScriptDialog
	//	        {
	//	            add { synchronized (m_ScriptDialogLock) { m_ScriptDialog += value; } }
	//	            remove { synchronized (m_ScriptDialogLock) { m_ScriptDialog -= value; } }
	//	        }
	//	
	//	        /// <summary>The event subscribers. null if no subcribers</summary>
	//	        private EventHandler<ScriptQuestionEventArgs> m_ScriptQuestion;
	//	
	//	        /// <summary>Raises the ScriptQuestion event</summary>
	//	        /// <param name="e">A ScriptQuestionEventArgs object containing the
	//	        /// data returned from the data server</param>
	//	        protected virtual void OnScriptQuestion(ScriptQuestionEventArgs e)
	//	        {
	//	            EventHandler<ScriptQuestionEventArgs> handler = m_ScriptQuestion;
	//	            if (handler != null)
	//	                handler(this, e);
	//	        }
	//	
	//	        /// <summary>Thread sync lock object</summary>
	//	        private readonly object m_ScriptQuestionLock = new object();
	//	        /// <summary>Raised when an object requests a change in the permissions an agent has permitted</summary>
	//	        public event EventHandler<ScriptQuestionEventArgs> ScriptQuestion
	//	        {
	//	            add { synchronized (m_ScriptQuestionLock) { m_ScriptQuestion += value; } }
	//	            remove { synchronized (m_ScriptQuestionLock) { m_ScriptQuestion -= value; } }
	//	        }
	//	
	//	        /// <summary>The event subscribers. null if no subcribers</summary>
	//	        private EventHandler<LoadUrlEventArgs> m_LoadURL;
	//	
	//	        /// <summary>Raises the LoadURL event</summary>
	//	        /// <param name="e">A LoadUrlEventArgs object containing the
	//	        /// data returned from the data server</param>
	//	        protected virtual void OnLoadURL(LoadUrlEventArgs e)
	//	        {
	//	            EventHandler<LoadUrlEventArgs> handler = m_LoadURL;
	//	            if (handler != null)
	//	                handler(this, e);
	//	        }
	//	
	//	        /// <summary>Thread sync lock object</summary>
	//	        private readonly object m_LoadUrlLock = new object();
	//	        /// <summary>Raised when a script requests an agent open the specified URL</summary>
	//	        public event EventHandler<LoadUrlEventArgs> LoadURL
	//	        {
	//	            add { synchronized (m_LoadUrlLock) { m_LoadURL += value; } }
	//	            remove { synchronized (m_LoadUrlLock) { m_LoadURL -= value; } }
	//	        }
	//	
	//	        /// <summary>The event subscribers. null if no subcribers</summary>
	//	        private EventHandler<BalanceEventArgs> m_Balance;
	//	
	//	        /// <summary>Raises the MoneyBalance event</summary>
	//	        /// <param name="e">A BalanceEventArgs object containing the
	//	        /// data returned from the data server</param>
	//	        protected virtual void OnBalance(BalanceEventArgs e)
	//	        {
	//	            EventHandler<BalanceEventArgs> handler = m_Balance;
	//	            if (handler != null)
	//	                handler(this, e);
	//	        }
	//	
	//	        /// <summary>Thread sync lock object</summary>
	//	        private readonly object m_BalanceLock = new object();
	//	
	//	        /// <summary>Raised when an agents currency balance is updated</summary>
	//	        public event EventHandler<BalanceEventArgs> MoneyBalance
	//	        {
	//	            add { synchronized (m_BalanceLock) { m_Balance += value; } }
	//	            remove { synchronized (m_BalanceLock) { m_Balance -= value; } }
	//	        }
	//	
	//	        /// <summary>The event subscribers. null if no subcribers</summary>
	//	        private EventHandler<MoneyBalanceReplyEventArgs> m_MoneyBalance;
	//	
	//	        /// <summary>Raises the MoneyBalanceReply event</summary>
	//	        /// <param name="e">A MoneyBalanceReplyEventArgs object containing the
	//	        /// data returned from the data server</param>
	//	        protected virtual void OnMoneyBalanceReply(MoneyBalanceReplyEventArgs e)
	//	        {
	//	            EventHandler<MoneyBalanceReplyEventArgs> handler = m_MoneyBalance;
	//	            if (handler != null)
	//	                handler(this, e);
	//	        }
	//	
	//	        /// <summary>Thread sync lock object</summary>
	//	        private readonly object m_MoneyBalanceReplyLock = new object();
	//	
	//	        /// <summary>Raised when a transaction occurs involving currency such as a land purchase</summary>
	//	        public event EventHandler<MoneyBalanceReplyEventArgs> MoneyBalanceReply
	//	        {
	//	            add { synchronized (m_MoneyBalanceReplyLock) { m_MoneyBalance += value; } }
	//	            remove { synchronized (m_MoneyBalanceReplyLock) { m_MoneyBalance -= value; } }
	//	        }
	//	
	//	        /// <summary>The event subscribers. null if no subcribers</summary>
	//	        private EventHandler<InstantMessageEventArgs> m_InstantMessage;
	//	
	//	        /// <summary>Raises the IM event</summary>
	//	        /// <param name="e">A InstantMessageEventArgs object containing the
	//	        /// data returned from the data server</param>
	//	        protected virtual void OnInstantMessage(InstantMessageEventArgs e)
	//	        {
	//	            EventHandler<InstantMessageEventArgs> handler = m_InstantMessage;
	//	            if (handler != null)
	//	                handler(this, e);
	//	        }
	//	
	//	        /// <summary>Thread sync lock object</summary>
	//	        private readonly object m_InstantMessageLock = new object();
	//	        /// <summary>Raised when an ImprovedInstantMessage packet is recieved from the simulator, this is used for everything from
	//	        /// private messaging to friendship offers. The Dialog field defines what type of message has arrived</summary>
	//	        public event EventHandler<InstantMessageEventArgs> IM
	//	        {
	//	            add { synchronized (m_InstantMessageLock) { m_InstantMessage += value; } }
	//	            remove { synchronized (m_InstantMessageLock) { m_InstantMessage -= value; } }
	//	        }
	//	
	//	        /// <summary>The event subscribers. null if no subcribers</summary>
	//	        private EventHandler<TeleportEventArgs> m_Teleport;
	//	
	//	        /// <summary>Raises the TeleportProgress event</summary>
	//	        /// <param name="e">A TeleportEventArgs object containing the
	//	        /// data returned from the data server</param>
	//	        protected virtual void OnTeleport(TeleportEventArgs e)
	//	        {
	//	            EventHandler<TeleportEventArgs> handler = m_Teleport;
	//	            if (handler != null)
	//	                handler(this, e);
	//	        }
	//	
	//	        /// <summary>Thread sync lock object</summary>
	//	        private readonly object m_TeleportLock = new object();
	//	        /// <summary>Raised when an agent has requested a teleport to another location, or when responding to a lure. Raised multiple times
	//	        /// for each teleport indicating the progress of the request</summary>
	//	        public event EventHandler<TeleportEventArgs> TeleportProgress
	//	        {
	//	            add { synchronized (m_TeleportLock) { m_Teleport += value; } }
	//	            remove { synchronized (m_TeleportLock) { m_Teleport += value; } }
	//	        }
	//	
	//	        /// <summary>The event subscribers. null if no subcribers</summary>
	//	        private EventHandler<AgentDataReplyEventArgs> m_AgentData;
	//	
	//	        /// <summary>Raises the AgentDataReply event</summary>
	//	        /// <param name="e">A AgentDataReplyEventArgs object containing the
	//	        /// data returned from the data server</param>
	//	        protected virtual void OnAgentData(AgentDataReplyEventArgs e)
	//	        {
	//	            EventHandler<AgentDataReplyEventArgs> handler = m_AgentData;
	//	            if (handler != null)
	//	                handler(this, e);
	//	        }
	//	
	//	        /// <summary>Thread sync lock object</summary>
	//	        private readonly object m_AgentDataLock = new object();
	//	
	//	        /// <summary>Raised when a simulator sends agent specific information for our avatar.</summary>
	//	        public event EventHandler<AgentDataReplyEventArgs> AgentDataReply
	//	        {
	//	            add { synchronized (m_AgentDataLock) { m_AgentData += value; } }
	//	            remove { synchronized (m_AgentDataLock) { m_AgentData -= value; } }
	//	        }
	//	
	//	        /// <summary>The event subscribers. null if no subcribers</summary>
	//	        private EventHandler<AnimationsChangedEventArgs> m_AnimationsChanged;
	//	
	//	        /// <summary>Raises the AnimationsChanged event</summary>
	//	        /// <param name="e">A AnimationsChangedEventArgs object containing the
	//	        /// data returned from the data server</param>
	//	        protected virtual void OnAnimationsChanged(AnimationsChangedEventArgs e)
	//	        {
	//	            EventHandler<AnimationsChangedEventArgs> handler = m_AnimationsChanged;
	//	            if (handler != null)
	//	                handler(this, e);
	//	        }
	//	
	//	        /// <summary>Thread sync lock object</summary>
	//	        private readonly object m_AnimationsChangedLock = new object();
	//	
	//	        /// <summary>Raised when our agents animation playlist changes</summary>
	//	        public event EventHandler<AnimationsChangedEventArgs> AnimationsChanged
	//	        {
	//	            add { synchronized (m_AnimationsChangedLock) { m_AnimationsChanged += value; } }
	//	            remove { synchronized (m_AnimationsChangedLock) { m_AnimationsChanged -= value; } }
	//	        }
	//	
	//	        /// <summary>The event subscribers. null if no subcribers</summary>
	//	        private EventHandler<MeanCollisionEventArgs> m_MeanCollision;
	//	
	//	        /// <summary>Raises the MeanCollision event</summary>
	//	        /// <param name="e">A MeanCollisionEventArgs object containing the
	//	        /// data returned from the data server</param>
	//	        protected virtual void OnMeanCollision(MeanCollisionEventArgs e)
	//	        {
	//	            EventHandler<MeanCollisionEventArgs> handler = m_MeanCollision;
	//	            if (handler != null)
	//	                handler(this, e);
	//	        }
	//	
	//	        /// <summary>Thread sync lock object</summary>
	//	        private readonly object m_MeanCollisionLock = new object();
	//	
	//	        /// <summary>Raised when an object or avatar forcefully collides with our agent</summary>
	//	        public event EventHandler<MeanCollisionEventArgs> MeanCollision
	//	        {
	//	            add { synchronized (m_MeanCollisionLock) { m_MeanCollision += value; } }
	//	            remove { synchronized (m_MeanCollisionLock) { m_MeanCollision -= value; } }
	//	        }
	//	
	//	        /// <summary>The event subscribers. null if no subcribers</summary>
	//	        private EventHandler<RegionCrossedEventArgs> m_RegionCrossed;
	//	
	//	        /// <summary>Raises the RegionCrossed event</summary>
	//	        /// <param name="e">A RegionCrossedEventArgs object containing the
	//	        /// data returned from the data server</param>
	//	        protected virtual void OnRegionCrossed(RegionCrossedEventArgs e)
	//	        {
	//	            EventHandler<RegionCrossedEventArgs> handler = m_RegionCrossed;
	//	            if (handler != null)
	//	                handler(this, e);
	//	        }
	//	
	//	        /// <summary>Thread sync lock object</summary>
	//	        private readonly object m_RegionCrossedLock = new object();
	//	
	//	        /// <summary>Raised when our agent crosses a region border into another region</summary>
	//	        public event EventHandler<RegionCrossedEventArgs> RegionCrossed
	//	        {
	//	            add { synchronized (m_RegionCrossedLock) { m_RegionCrossed += value; } }
	//	            remove { synchronized (m_RegionCrossedLock) { m_RegionCrossed -= value; } }
	//	        }
	//	
	//	        /// <summary>The event subscribers. null if no subcribers</summary>
	//	        private EventHandler<GroupChatJoinedEventArgs> m_GroupChatJoined;
	//	
	//	        /// <summary>Raises the GroupChatJoined event</summary>
	//	        /// <param name="e">A GroupChatJoinedEventArgs object containing the
	//	        /// data returned from the data server</param>
	//	        protected virtual void OnGroupChatJoined(GroupChatJoinedEventArgs e)
	//	        {
	//	            EventHandler<GroupChatJoinedEventArgs> handler = m_GroupChatJoined;
	//	            if (handler != null)
	//	                handler(this, e);
	//	        }
	//	
	//	        /// <summary>Thread sync lock object</summary>
	//	        private readonly object m_GroupChatJoinedLock = new object();
	//	
	//	        /// <summary>Raised when our agent succeeds or fails to join a group chat session</summary>
	//	        public event EventHandler<GroupChatJoinedEventArgs> GroupChatJoined
	//	        {
	//	            add { synchronized (m_GroupChatJoinedLock) { m_GroupChatJoined += value; } }
	//	            remove { synchronized (m_GroupChatJoinedLock) { m_GroupChatJoined -= value; } }
	//	        }
	//	
	//	        /// <summary>The event subscribers. null if no subcribers</summary>
	//	        private EventHandler<AlertMessageEventArgs> m_AlertMessage;
	//	
	//	        /// <summary>Raises the AlertMessage event</summary>
	//	        /// <param name="e">A AlertMessageEventArgs object containing the
	//	        /// data returned from the data server</param>
	//	        protected virtual void OnAlertMessage(AlertMessageEventArgs e)
	//	        {
	//	            EventHandler<AlertMessageEventArgs> handler = m_AlertMessage;
	//	            if (handler != null)
	//	                handler(this, e);
	//	        }
	//	
	//	        /// <summary>Thread sync lock object</summary>
	//	        private readonly object m_AlertMessageLock = new object();
	//	
	//	        /// <summary>Raised when a simulator sends an urgent message usually indication the recent failure of
	//	        /// another action we have attempted to take such as an attempt to enter a parcel where we are denied access</summary>
	//	        public event EventHandler<AlertMessageEventArgs> AlertMessage
	//	        {
	//	            add { synchronized (m_AlertMessageLock) { m_AlertMessage += value; } }
	//	            remove { synchronized (m_AlertMessageLock) { m_AlertMessage -= value; } }
	//	        }
	//	
	//	        /// <summary>The event subscribers. null if no subcribers</summary>
	//	        private EventHandler<ScriptControlEventArgs> m_ScriptControl;
	//	
	//	        /// <summary>Raises the ScriptControlChange event</summary>
	//	        /// <param name="e">A ScriptControlEventArgs object containing the
	//	        /// data returned from the data server</param>
	//	        protected virtual void OnScriptControlChange(ScriptControlEventArgs e)
	//	        {
	//	            EventHandler<ScriptControlEventArgs> handler = m_ScriptControl;
	//	            if (handler != null)
	//	                handler(this, e);
	//	        }
	//	
	//	        /// <summary>Thread sync lock object</summary>
	//	        private readonly object m_ScriptControlLock = new object();
	//	
	//	        /// <summary>Raised when a script attempts to take or release specified controls for our agent</summary>
	//	        public event EventHandler<ScriptControlEventArgs> ScriptControlChange
	//	        {
	//	            add { synchronized (m_ScriptControlLock) { m_ScriptControl += value; } }
	//	            remove { synchronized (m_ScriptControlLock) { m_ScriptControl -= value; } }
	//	        }
	//	
	//	        /// <summary>The event subscribers. null if no subcribers</summary>
	//	        private EventHandler<CameraConstraintEventArgs> m_CameraConstraint;
	//	
	//	        /// <summary>Raises the CameraConstraint event</summary>
	//	        /// <param name="e">A CameraConstraintEventArgs object containing the
	//	        /// data returned from the data server</param>
	//	        protected virtual void OnCameraConstraint(CameraConstraintEventArgs e)
	//	        {
	//	            EventHandler<CameraConstraintEventArgs> handler = m_CameraConstraint;
	//	            if (handler != null)
	//	                handler(this, e);
	//	        }
	//	
	//	        /// <summary>Thread sync lock object</summary>
	//	        private readonly object m_CameraConstraintLock = new object();
	//	
	//	        /// <summary>Raised when the simulator detects our agent is trying to view something
	//	        /// beyond its limits</summary>
	//	        public event EventHandler<CameraConstraintEventArgs> CameraConstraint
	//	        {
	//	            add { synchronized (m_CameraConstraintLock) { m_CameraConstraint += value; } }
	//	            remove { synchronized (m_CameraConstraintLock) { m_CameraConstraint -= value; } }
	//	        }
	//	
	//	        /// <summary>The event subscribers. null if no subcribers</summary>
	//	        private EventHandler<ScriptSensorReplyEventArgs> m_ScriptSensorReply;
	//	
	//	        /// <summary>Raises the ScriptSensorReply event</summary>
	//	        /// <param name="e">A ScriptSensorReplyEventArgs object containing the
	//	        /// data returned from the data server</param>
	//	        protected virtual void OnScriptSensorReply(ScriptSensorReplyEventArgs e)
	//	        {
	//	            EventHandler<ScriptSensorReplyEventArgs> handler = m_ScriptSensorReply;
	//	            if (handler != null)
	//	                handler(this, e);
	//	        }
	//	
	//	        /// <summary>Thread sync lock object</summary>
	//	        private readonly object m_ScriptSensorReplyLock = new object();
	//	
	//	        /// <summary>Raised when a script sensor reply is received from a simulator</summary>
	//	        public event EventHandler<ScriptSensorReplyEventArgs> ScriptSensorReply
	//	        {
	//	            add { synchronized (m_ScriptSensorReplyLock) { m_ScriptSensorReply += value; } }
	//	            remove { synchronized (m_ScriptSensorReplyLock) { m_ScriptSensorReply -= value; } }
	//	        }
	//	
	//	        /// <summary>The event subscribers. null if no subcribers</summary>
	//	        private EventHandler<AvatarSitResponseEventArgs> m_AvatarSitResponse;
	//	
	//	        /// <summary>Raises the AvatarSitResponse event</summary>
	//	        /// <param name="e">A AvatarSitResponseEventArgs object containing the
	//	        /// data returned from the data server</param>
	//	        protected virtual void OnAvatarSitResponse(AvatarSitResponseEventArgs e)
	//	        {
	//	            EventHandler<AvatarSitResponseEventArgs> handler = m_AvatarSitResponse;
	//	            if (handler != null)
	//	                handler(this, e);
	//	        }
	//	
	//	        /// <summary>Thread sync lock object</summary>
	//	        private readonly object m_AvatarSitResponseLock = new object();
	//	
	//	        /// <summary>Raised in response to a <see cref="RequestSit"/> request</summary>
	//	        public event EventHandler<AvatarSitResponseEventArgs> AvatarSitResponse
	//	        {
	//	            add { synchronized (m_AvatarSitResponseLock) { m_AvatarSitResponse += value; } }
	//	            remove { synchronized (m_AvatarSitResponseLock) { m_AvatarSitResponse -= value; } }
	//	        }
	//	
	//	        /// <summary>The event subscribers. null if no subcribers</summary>
	//	        private EventHandler<ChatSessionMemberAddedEventArgs> m_ChatSessionMemberAdded;
	//	
	//	        /// <summary>Raises the ChatSessionMemberAdded event</summary>
	//	        /// <param name="e">A ChatSessionMemberAddedEventArgs object containing the
	//	        /// data returned from the data server</param>
	//	        protected virtual void OnChatSessionMemberAdded(ChatSessionMemberAddedEventArgs e)
	//	        {
	//	            EventHandler<ChatSessionMemberAddedEventArgs> handler = m_ChatSessionMemberAdded;
	//	            if (handler != null)
	//	                handler(this, e);
	//	        }
	//	
	//	        /// <summary>Thread sync lock object</summary>
	//	        private readonly object m_ChatSessionMemberAddedLock = new object();
	//	
	//	        /// <summary>Raised when an avatar enters a group chat session we are participating in</summary>
	//	        public event EventHandler<ChatSessionMemberAddedEventArgs> ChatSessionMemberAdded
	//	        {
	//	            add { synchronized (m_ChatSessionMemberAddedLock) { m_ChatSessionMemberAdded += value; } }
	//	            remove { synchronized (m_ChatSessionMemberAddedLock) { m_ChatSessionMemberAdded -= value; } }
	//	        }
	//	
	//	        /// <summary>The event subscribers. null if no subcribers</summary>
	//	        private EventHandler<ChatSessionMemberLeftEventArgs> m_ChatSessionMemberLeft;
	//	
	//	        /// <summary>Raises the ChatSessionMemberLeft event</summary>
	//	        /// <param name="e">A ChatSessionMemberLeftEventArgs object containing the
	//	        /// data returned from the data server</param>
	//	        protected virtual void OnChatSessionMemberLeft(ChatSessionMemberLeftEventArgs e)
	//	        {
	//	            EventHandler<ChatSessionMemberLeftEventArgs> handler = m_ChatSessionMemberLeft;
	//	            if (handler != null)
	//	                handler(this, e);
	//	        }
	//	
	//	        /// <summary>Thread sync lock object</summary>
	//	        private readonly object m_ChatSessionMemberLeftLock = new object();
	//	
	//	        /// <summary>Raised when an agent exits a group chat session we are participating in</summary>
	//	        public event EventHandler<ChatSessionMemberLeftEventArgs> ChatSessionMemberLeft
	//	        {
	//	            add { synchronized (m_ChatSessionMemberLeftLock) { m_ChatSessionMemberLeft += value; } }
	//	            remove { synchronized (m_ChatSessionMemberLeftLock) { m_ChatSessionMemberLeft -= value; } }
	//	        }
	//	
	//	        /// <summary>The event subscribers, null of no subscribers</summary>
	//	        private EventHandler<SetDisplayNameReplyEventArgs> m_SetDisplayNameReply;
	//	
	//	        ///<summary>Raises the SetDisplayNameReply Event</summary>
	//	        /// <param name="e">A SetDisplayNameReplyEventArgs object containing
	//	        /// the data sent from the simulator</param>
	//	        protected virtual void OnSetDisplayNameReply(SetDisplayNameReplyEventArgs e)
	//	        {
	//	            EventHandler<SetDisplayNameReplyEventArgs> handler = m_SetDisplayNameReply;
	//	            if (handler != null)
	//	                handler(this, e);
	//	        }
	//	
	//	        /// <summary>Thread sync lock object</summary>
	//	        private readonly object m_SetDisplayNameReplyLock = new object();
	//	
	//	        /// <summary>Raised when the simulator sends us data containing
	//	        /// the details of display name change</summary>
	//	        public event EventHandler<SetDisplayNameReplyEventArgs> SetDisplayNameReply
	//	        {
	//	            add { synchronized (m_SetDisplayNameReplyLock) { m_SetDisplayNameReply += value; } }
	//	            remove { synchronized (m_SetDisplayNameReplyLock) { m_SetDisplayNameReply -= value; } }
	//	        }
	//	
	//	        /// <summary>The event subscribers. null if no subcribers</summary>
	//	        private EventHandler<EventArgs> m_MuteListUpdated;
	//	
	//	        /// <summary>Raises the MuteListUpdated event</summary>
	//	        /// <param name="e">A EventArgs object containing the
	//	        /// data returned from the data server</param>
	//	        protected virtual void OnMuteListUpdated(EventArgs e)
	//	        {
	//	            EventHandler<EventArgs> handler = m_MuteListUpdated;
	//	            if (handler != null)
	//	                handler(this, e);
	//	        }
	//	
	//	        /// <summary>Thread sync lock object</summary>
	//	        private readonly object m_MuteListUpdatedLock = new object();
	//	
	//	        /// <summary>Raised when a scripted object or agent within range sends a public message</summary>
	//	        public event EventHandler<EventArgs> MuteListUpdated
	//	        {
	//	            add { synchronized (m_MuteListUpdatedLock) { m_MuteListUpdated += value; } }
	//	            remove { synchronized (m_MuteListUpdatedLock) { m_MuteListUpdated -= value; } }
	//	        }
	//	        //endregion Callbacks


	/// <summary>Reference to the GridClient instance</summary>
	private GridClient Client;
	/// <summary>Used for movement and camera tracking</summary>
	//readonly
	public AgentMovement Movement;
	/// <summary>Currently playing animations for the agent. Can be used to
	/// check the current movement status such as walking, hovering, aiming,
	/// etc. by checking against system animations found in the Animations class</summary>
	public InternalDictionary<UUID, Integer> SignaledAnimations = new InternalDictionary<UUID, Integer>();
	/// <summary>Dictionary containing current Group Chat sessions and members</summary>
	public InternalDictionary<UUID, List<ChatSessionMember>> GroupChatSessions = new InternalDictionary<UUID, List<ChatSessionMember>>();
	/// <summary>Dictionary containing mute list keyead on mute name and key</summary>
	public InternalDictionary<String, MuteEntry> MuteList = new InternalDictionary<String, MuteEntry>();

	//region Properties

	/// <summary>Your (client) avatars <see cref="UUID"/></summary>
	/// <remarks>"client", "agent", and "avatar" all represent the same thing</remarks>
	public UUID getAgentID() {return id;}
	/// <summary>Temporary <seealso cref="UUID"/> assigned to this session, used for 
	/// verifying our identity in packets</summary>
	public UUID getSessionID() {return sessionID;}
	/// <summary>Shared secret <seealso cref="UUID"/> that is never sent over the wire</summary>
	public UUID getSecureSessionID() {return secureSessionID;}
	/// <summary>Your (client) avatar ID, local to the current region/sim</summary>
	public long getLocalID() {return localID;}
	/// <summary>Where the avatar started at login. Can be "last", "home" 
	/// or a login <seealso cref="T:OpenMetaverse.URI"/></summary>
	public String getStartLocation() {return startLocation;}
	/// <summary>The access level of this agent, usually M or PG</summary>
	public String getAgentAccess() {return agentAccess;}
	/// <summary>The CollisionPlane of Agent</summary>
	public Vector4 getCollisionPlane() {return collisionPlane;}
	/// <summary>An <seealso cref="Vector3"/> representing the velocity of our agent</summary>
	public Vector3 getVelocity() {return velocity;}
	/// <summary>An <seealso cref="Vector3"/> representing the acceleration of our agent</summary>
	public Vector3 getAcceleration() {return acceleration;}
	/// <summary>A <seealso cref="Vector3"/> which specifies the angular speed, and axis about which an Avatar is rotating.</summary>
	public Vector3 getAngularVelocity() {return angularVelocity;}
	/// <summary>Position avatar client will goto when login to 'home' or during
	/// teleport request to 'home' region.</summary>
	public Vector3 getHomePosition() {return homePosition;}
	/// <summary>LookAt point saved/restored with HomePosition</summary>
	public Vector3 getHomeLookAt() {return homeLookAt;}
	/// <summary>Avatar First Name (i.e. Philip)</summary>
	public String getFirstName() {return firstName;}
	/// <summary>Avatar Last Name (i.e. Linden)</summary>
	public String getLastName() {return lastName;}
	/// <summary>Avatar Full Name (i.e. Philip Linden)</summary>
	public String getName()
	{
		// This is a fairly common request, so assume the name doesn't
		// change mid-session and cache the result
		if (fullName == null || fullName.length() < 2)
			fullName = String.format("%s %s", firstName, lastName);
		return fullName;
	}
	/// <summary>Gets the health of the agent</summary>
	public float getHealth() {return health;}
	/// <summary>Gets the current balance of the agent</summary>
	public int getBalance() {return balance;}
	/// <summary>Gets the local ID of the prim the agent is sitting on,
	/// zero if the avatar is not currently sitting</summary>
	public long getSittingOn() {return sittingOn;}
	/// <summary>Gets the <seealso cref="UUID"/> of the agents active group.</summary>
	public UUID getActiveGroup() {return activeGroup;}
	/// <summary>Gets the Agents powers in the currently active group</summary>
	public GroupPowers getActiveGroupPowers() {return activeGroupPowers;}
	/// <summary>Current status message for teleporting</summary>
	public String getTeleportMessage() {return teleportMessage;}
	/// <summary>Current position of the agent as a relative offset from
	/// the simulator, or the parent object if we are sitting on something</summary>
	public Vector3 getRelativePosition() {return relativePosition; } 

	public void setRelativePosition(Vector3 value) 
	{ relativePosition = value;}
	/// <summary>Current rotation of the agent as a relative rotation from
	/// the simulator, or the parent object if we are sitting on something</summary>
	public Quaternion getRelativeRotation() {return relativeRotation; } 
	public void getRelativeRotation(Quaternion value)	        
	{ relativeRotation = value;}
	/// <summary>Current position of the agent in the simulator</summary>
	public Vector3 getSimPosition()
	{
		// simple case, agent not seated
		if (sittingOn == 0)
		{
			return relativePosition;
		}

		// a bit more complicatated, agent sitting on a prim
		Primitive p = null;
		Vector3 fullPosition = relativePosition;

		if ((( p = Client.network.getCurrentSim().ObjectsPrimitives.get(sittingOn)) != null))
		{
			fullPosition = Vector3.add(p.Position,  Vector3.multiply(relativePosition,  p.Rotation));
		}

		// go up the hiearchy trying to find the root prim
		while (p != null && p.ParentID != 0)
		{
			Avatar av;
			if (((av = Client.network.getCurrentSim().ObjectsAvatars.get(p.ParentID)) != null))
			{
				p = av;
				fullPosition = Vector3.add(fullPosition, p.Position);
			}
			else
			{
				if (((p = Client.network.getCurrentSim().ObjectsPrimitives.get(p.ParentID)) !=null))
				{
					fullPosition = Vector3.add(fullPosition, p.Position);
				}
			}
		}

		if (p != null) // we found the root prim
		{
			return fullPosition;
		}

		// Didn't find the seat's root prim, try returning coarse loaction
		if (((fullPosition = Client.network.getCurrentSim().avatarPositions.get(getAgentID()))!=null))
		{
			return fullPosition;
		}

		JLogger.warn("Failed to determine agents sim position");
		return relativePosition;
	}

	/// <summary>
	/// A <seealso cref="Quaternion"/> representing the agents current rotation
	/// </summary>
	public Quaternion getSimRotation()
	{
		if (sittingOn != 0)
		{
			Primitive parent;
			if (Client.network.getCurrentSim() != null && ((parent = Client.network.getCurrentSim().ObjectsPrimitives.get(sittingOn))!=null))
			{
				return Quaternion.multiply(relativeRotation,  parent.Rotation);
			}
			else
			{
				JLogger.warn("Currently sitting on object " + sittingOn + " which is not tracked, SimRotation will be inaccurate");
				return relativeRotation;
			}
		}
		else
		{
			return relativeRotation;
		}
	}

	/// <summary>Returns the global grid position of the avatar</summary>
	public Vector3d getGlobalPosition()
	{
		if (Client.network.getCurrentSim() != null)
		{
			//uint
			long globalX, globalY;
			long[] xy = new long[2]; 
			Utils.longToUInts(Client.network.getCurrentSim().Handle.longValue(), xy);
			globalX = xy[0];
			globalY = xy[1];
			//	                    Utils.LongToUInts(Client.network.getCurrentSim().Handle, out globalX, out globalY);
			Vector3 pos = getSimPosition();

			return new Vector3d(
					(double)globalX + (double)pos.X,
					(double)globalY + (double)pos.Y,
					(double)pos.Z);
		}
		else
			return Vector3d.Zero;
	}

	//endregion Properties

	//uint
	private long localID;
	private Vector3 relativePosition;
	private Quaternion relativeRotation = Quaternion.Identity;
	private Vector4 collisionPlane;
	private Vector3 velocity;
	private Vector3 acceleration;
	private Vector3 angularVelocity;
	//uint
	private long sittingOn;
	private int lastInterpolation;
	//region Private Members

	private UUID id;
	private UUID sessionID;
	private UUID secureSessionID;
	private String startLocation = "";
	private String agentAccess = "";
	private Vector3 homePosition;
	private Vector3 homeLookAt;
	private String firstName = "";
	private String lastName = "";
	private String fullName;
	private String teleportMessage = "";
	private TeleportStatus teleportStat = TeleportStatus.None;
	private ManualResetEvent teleportEvent = new ManualResetEvent(false);
	//uint
	private long heightWidthGenCounter;
	private float health;
	private int balance;
	private UUID activeGroup;
	private GroupPowers activeGroupPowers;
	private Map<UUID, AssetGesture> gestureCache = new HashMap<UUID, AssetGesture>();
	//endregion Private Members

	/// <summary>
	/// Constructor, setup callbacks for packets related to our avatar
	/// </summary>
	/// <param name="client">A reference to the <seealso cref="T:OpenMetaverse.GridClient"/> Class</param>
	public AgentManager(GridClient client)
	{
		Client = client;
		//TODO Need to uncomment following
		Movement = new AgentMovement(Client);
		//TODO Need to implement

		//
		//            Client.network.Disconnected += Network_OnDisconnected;
		//
		//            // Teleport callbacks            
		//            Client.network.RegisterCallback(PacketType.TeleportStart, TeleportHandler);
		//            Client.network.RegisterCallback(PacketType.TeleportProgress, TeleportHandler);
		//            Client.network.RegisterCallback(PacketType.TeleportFailed, TeleportHandler);
		//            Client.network.RegisterCallback(PacketType.TeleportCancel, TeleportHandler);
		//            Client.network.RegisterCallback(PacketType.TeleportLocal, TeleportHandler);
		//            // these come in via the EventQueue
		//            Client.network.RegisterEventCallback("TeleportFailed", new Caps.EventQueueCallback(TeleportFailedEventHandler));
		//            Client.network.RegisterEventCallback("TeleportFinish", new Caps.EventQueueCallback(TeleportFinishEventHandler));
		//
		//            // Instant message callback
		//            Client.network.RegisterCallback(PacketType.ImprovedInstantMessage, InstantMessageHandler);
		//            // Chat callback
		//            Client.network.RegisterCallback(PacketType.ChatFromSimulator, ChatHandler);
		//            // Script dialog callback
		//            Client.network.RegisterCallback(PacketType.ScriptDialog, ScriptDialogHandler);
		//            // Script question callback
		//            Client.network.RegisterCallback(PacketType.ScriptQuestion, ScriptQuestionHandler);
		//            // Script URL callback
		//            Client.network.RegisterCallback(PacketType.LoadURL, LoadURLHandler);
		//            // Movement complete callback
		//            Client.network.RegisterCallback(PacketType.AgentMovementComplete, MovementCompleteHandler);
		//            // Health callback
		//            Client.network.RegisterCallback(PacketType.HealthMessage, HealthHandler);
		//            // Money callback
		//            Client.network.RegisterCallback(PacketType.MoneyBalanceReply, MoneyBalanceReplyHandler);
		//            //Agent update callback
		//            Client.network.RegisterCallback(PacketType.AgentDataUpdate, AgentDataUpdateHandler);
		//            // Animation callback
		//            Client.network.RegisterCallback(PacketType.AvatarAnimation, AvatarAnimationHandler, false);
		//            // Object colliding into our agent callback
		//            Client.network.RegisterCallback(PacketType.MeanCollisionAlert, MeanCollisionAlertHandler);
		//            // Region Crossing
		//            Client.network.RegisterCallback(PacketType.CrossedRegion, CrossedRegionHandler);
		//            Client.network.RegisterEventCallback("CrossedRegion", new Caps.EventQueueCallback(CrossedRegionEventHandler));
		//            // CAPS callbacks
		//            Client.network.RegisterEventCallback("EstablishAgentCommunication", new Caps.EventQueueCallback(EstablishAgentCommunicationEventHandler));
		//            Client.network.RegisterEventCallback("SetDisplayNameReply", new Caps.EventQueueCallback(SetDisplayNameReplyEventHandler));
		//            // Incoming Group Chat
		//            Client.network.RegisterEventCallback("ChatterBoxInvitation", new Caps.EventQueueCallback(ChatterBoxInvitationEventHandler));
		//            // Outgoing Group Chat Reply
		//            Client.network.RegisterEventCallback("ChatterBoxSessionEventReply", new Caps.EventQueueCallback(ChatterBoxSessionEventReplyEventHandler));
		//            Client.network.RegisterEventCallback("ChatterBoxSessionStartReply", new Caps.EventQueueCallback(ChatterBoxSessionStartReplyEventHandler));
		//            Client.network.RegisterEventCallback("ChatterBoxSessionAgentListUpdates", new Caps.EventQueueCallback(ChatterBoxSessionAgentListUpdatesEventHandler));
		            
					// Login
					Client.network.RegisterLoginResponseCallback(new Observer()
		            {
						public void update(Observable arg0, Object arg1) {
							LoginResponseCallbackArg obj = (LoginResponseCallbackArg)arg1;
							Network_OnLoginResponse(obj.isLoginSuccess(), obj.isRedirect(), 
									obj.getMessage(), obj.getReason(), obj.getReplyData());
						}	
		            });
		            
		//            // Alert Messages
		//            Client.network.RegisterCallback(PacketType.AlertMessage, AlertMessageHandler);
		//            // script control change messages, ie: when an in-world LSL script wants to take control of your agent.
		//            Client.network.RegisterCallback(PacketType.ScriptControlChange, ScriptControlChangeHandler);
		//            // Camera Constraint (probably needs to move to AgentManagerCamera TODO:
		//            Client.network.RegisterCallback(PacketType.CameraConstraint, CameraConstraintHandler);
		//            Client.network.RegisterCallback(PacketType.ScriptSensorReply, ScriptSensorReplyHandler);
		//            Client.network.RegisterCallback(PacketType.AvatarSitResponse, AvatarSitResponseHandler);
		//            // Process mute list update message
		//            Client.network.RegisterCallback(PacketType.MuteListUpdate, MuteListUpdateHander);
	}

	//TODO Need to implement
	//region Chat and instant messages

	/// <summary>
	/// Send a text message from the Agent to the Simulator
	/// </summary>
	/// <param name="message">A <see cref="string"/> containing the message</param>
	/// <param name="channel">The channel to send the message on, 0 is the public channel. Channels above 0
	/// can be used however only scripts listening on the specified channel will see the message</param>
	/// <param name="type">Denotes the type of message being sent, shout, whisper, etc.</param>
	public void Chat(String message, int channel, ChatType type)
	{
		ChatFromViewerPacket chat = new ChatFromViewerPacket();
		chat.AgentData.AgentID = this.id;
		chat.AgentData.SessionID = Client.self.getSessionID();
		chat.ChatData.Channel = channel;
		chat.ChatData.Message = Utils.stringToBytes(message);
		chat.ChatData.Type = (byte)type.getIndex();

		Client.network.SendPacket(chat);
	}

	/// <summary>
	/// Request any instant messages sent while the client was offline to be resent.
	/// </summary>
	public void RetrieveInstantMessages()
	{
		RetrieveInstantMessagesPacket p = new RetrieveInstantMessagesPacket();
		p.AgentData.AgentID = Client.self.getAgentID();
		p.AgentData.SessionID = Client.self.getSessionID();
		Client.network.SendPacket(p);
	}

	/// <summary>
	/// Send an Instant Message to another Avatar
	/// </summary>
	/// <param name="target">The recipients <see cref="UUID"/></param>
	/// <param name="message">A <see cref="string"/> containing the message to send</param>
	public void InstantMessage(UUID target, String message)
	{
		InstantMessage(getName(), target, message, getAgentID().equals(target) ? getAgentID() : UUID.xor(target, getAgentID()),
				InstantMessageDialog.MessageFromAgent, InstantMessageOnline.Offline, this.getSimPosition(),
				UUID.Zero, Utils.EmptyBytes);
	}

	/// <summary>
	/// Send an Instant Message to an existing group chat or conference chat
	/// </summary>
	/// <param name="target">The recipients <see cref="UUID"/></param>
	/// <param name="message">A <see cref="string"/> containing the message to send</param>
	/// <param name="imSessionID">IM session ID (to differentiate between IM windows)</param>
	public void InstantMessage(UUID target, String message, UUID imSessionID)
	{
		InstantMessage(getName(), target, message, imSessionID,
				InstantMessageDialog.MessageFromAgent, InstantMessageOnline.Offline, this.getSimPosition(),
				UUID.Zero, Utils.EmptyBytes);
	}

	/// <summary>
	/// Send an Instant Message
	/// </summary>
	/// <param name="fromName">The name this IM will show up as being from</param>
	/// <param name="target">Key of Avatar</param>
	/// <param name="message">Text message being sent</param>
	/// <param name="imSessionID">IM session ID (to differentiate between IM windows)</param>
	/// <param name="conferenceIDs">IDs of sessions for a conference</param>
	public void InstantMessage(String fromName, UUID target, String message, UUID imSessionID,
			UUID[] conferenceIDs)
	{
		byte[] binaryBucket;

		if (conferenceIDs != null && conferenceIDs.length > 0)
		{
			binaryBucket = new byte[16 * conferenceIDs.length];
			for (int i = 0; i < conferenceIDs.length; ++i)
				Utils.arraycopy(conferenceIDs[i].GetBytes(), 0, binaryBucket, i * 16, 16);
		}
		else
		{
			binaryBucket = Utils.EmptyBytes;
		}

		InstantMessage(fromName, target, message, imSessionID, InstantMessageDialog.MessageFromAgent,
				InstantMessageOnline.Offline, Vector3.Zero, UUID.Zero, binaryBucket);
	}

	/// <summary>
	/// Send an Instant Message
	/// </summary>
	/// <param name="fromName">The name this IM will show up as being from</param>
	/// <param name="target">Key of Avatar</param>
	/// <param name="message">Text message being sent</param>
	/// <param name="imSessionID">IM session ID (to differentiate between IM windows)</param>
	/// <param name="dialog">Type of instant message to send</param>
	/// <param name="offline">Whether to IM offline avatars as well</param>
	/// <param name="position">Senders Position</param>
	/// <param name="regionID">RegionID Sender is In</param>
	/// <param name="binaryBucket">Packed binary data that is specific to
	/// the dialog type</param>
	public void InstantMessage(String fromName, UUID target, String message, UUID imSessionID,
			InstantMessageDialog dialog, InstantMessageOnline offline, Vector3 position, UUID regionID,
			byte[] binaryBucket)
	{
		if (target != UUID.Zero)
		{
			ImprovedInstantMessagePacket im = new ImprovedInstantMessagePacket();

			if (imSessionID.equals(UUID.Zero) || imSessionID.equals(getAgentID()))
				imSessionID = getAgentID().equals(target) ? getAgentID() : UUID.xor(target , getAgentID());

				im.AgentData.AgentID = Client.self.getAgentID();
				im.AgentData.SessionID = Client.self.getSessionID();

				im.MessageBlock.Dialog = (byte)dialog.getIndex();
				im.MessageBlock.FromAgentName = Utils.stringToBytes(fromName);
				im.MessageBlock.FromGroup = false;
				im.MessageBlock.ID = imSessionID;
				im.MessageBlock.Message = Utils.stringToBytes(message);
				im.MessageBlock.Offline = (byte)offline.getIndex();
				im.MessageBlock.ToAgentID = target;

				if (binaryBucket != null)
					im.MessageBlock.BinaryBucket = binaryBucket;
				else
					im.MessageBlock.BinaryBucket = Utils.EmptyBytes;

				// These fields are mandatory, even if we don't have valid values for them
				im.MessageBlock.Position = Vector3.Zero;
						//TODO: Allow region id to be correctly set by caller or fetched from Client.*
						im.MessageBlock.RegionID = regionID;

						// Send the message
						Client.network.SendPacket(im);
		}
		else
		{
			JLogger.error(String.format("Suppressing instant message \"%s\" to UUID.Zero", message));
		}
	}

	/// <summary>
	/// Send an Instant Message to a group
	/// </summary>
	/// <param name="groupID"><seealso cref="UUID"/> of the group to send message to</param>
	/// <param name="message">Text Message being sent.</param>
	public void InstantMessageGroup(UUID groupID, String message)
	{
		InstantMessageGroup(getName(), groupID, message);
	}

	/// <summary>
	/// Send an Instant Message to a group the agent is a member of
	/// </summary>
	/// <param name="fromName">The name this IM will show up as being from</param>
	/// <param name="groupID"><seealso cref="UUID"/> of the group to send message to</param>
	/// <param name="message">Text message being sent</param>
	public void InstantMessageGroup(String fromName, UUID groupID, String message)
	{
		synchronized (GroupChatSessions.getDictionary())
		{
			if (GroupChatSessions.containsKey(groupID))
			{
				ImprovedInstantMessagePacket im = new ImprovedInstantMessagePacket();

				im.AgentData.AgentID = Client.self.getAgentID();
				im.AgentData.SessionID = Client.self.getSessionID();
				im.MessageBlock.Dialog = (byte)InstantMessageDialog.SessionSend.getIndex();
				im.MessageBlock.FromAgentName = Utils.stringToBytes(fromName);
				im.MessageBlock.FromGroup = false;
				im.MessageBlock.Message = Utils.stringToBytes(message);
				im.MessageBlock.Offline = 0;
				im.MessageBlock.ID = groupID;
				im.MessageBlock.ToAgentID = groupID;
				im.MessageBlock.Position = Vector3.Zero;
				im.MessageBlock.RegionID = UUID.Zero;
				im.MessageBlock.BinaryBucket = Utils.stringToBytes("\0");

				Client.network.SendPacket(im);
			}
			else
			{
				JLogger.error("No Active group chat session appears to exist, use RequestJoinGroupChat() to join one");
			}
		}
	}

	/// <summary>
	/// Send a request to join a group chat session
	/// </summary>
	/// <param name="groupID"><seealso cref="UUID"/> of Group to leave</param>
	public void RequestJoinGroupChat(UUID groupID)
	{
		ImprovedInstantMessagePacket im = new ImprovedInstantMessagePacket();

		im.AgentData.AgentID = Client.self.getAgentID();
		im.AgentData.SessionID = Client.self.getSessionID();
		im.MessageBlock.Dialog = (byte)InstantMessageDialog.SessionGroupStart.getIndex();
		im.MessageBlock.FromAgentName = Utils.stringToBytes(Client.self.getName());
		im.MessageBlock.FromGroup = false;
		im.MessageBlock.Message = Utils.EmptyBytes;
		im.MessageBlock.ParentEstateID = 0;
		im.MessageBlock.Offline = 0;
		im.MessageBlock.ID = groupID;
		im.MessageBlock.ToAgentID = groupID;
		im.MessageBlock.BinaryBucket = Utils.EmptyBytes;
		im.MessageBlock.Position = Client.self.getSimPosition();
		im.MessageBlock.RegionID = UUID.Zero;

		Client.network.SendPacket(im);
	}

	/// <summary>
	/// Exit a group chat session. This will stop further Group chat messages
	/// from being sent until session is rejoined.
	/// </summary>
	/// <param name="groupID"><seealso cref="UUID"/> of Group chat session to leave</param>
	public void RequestLeaveGroupChat(UUID groupID)
	{
		ImprovedInstantMessagePacket im = new ImprovedInstantMessagePacket();

		im.AgentData.AgentID = Client.self.getAgentID();
		im.AgentData.SessionID = Client.self.getSessionID();
		im.MessageBlock.Dialog = (byte)InstantMessageDialog.SessionDrop.getIndex();
		im.MessageBlock.FromAgentName = Utils.stringToBytes(Client.self.getName());
		im.MessageBlock.FromGroup = false;
		im.MessageBlock.Message = Utils.EmptyBytes;
		im.MessageBlock.Offline = 0;
		im.MessageBlock.ID = groupID;
		im.MessageBlock.ToAgentID = groupID;
		im.MessageBlock.BinaryBucket = Utils.EmptyBytes;
		im.MessageBlock.Position = Vector3.Zero;
		im.MessageBlock.RegionID = UUID.Zero;

		Client.network.SendPacket(im);

		synchronized (GroupChatSessions.getDictionary())
		{
			if (GroupChatSessions.containsKey(groupID))
				GroupChatSessions.remove(groupID);
		}
	}

	/// <summary>
	/// Reply to script dialog questions. 
	/// </summary>
	/// <param name="channel">Channel initial request came on</param>
	/// <param name="buttonIndex">Index of button you're "clicking"</param>
	/// <param name="buttonlabel">Label of button you're "clicking"</param>
	/// <param name="objectID"><seealso cref="UUID"/> of Object that sent the dialog request</param>
	/// <seealso cref="OnScriptDialog"/>
	public void ReplyToScriptDialog(int channel, int buttonIndex, String buttonlabel, UUID objectID)
	{
		ScriptDialogReplyPacket reply = new ScriptDialogReplyPacket();

		reply.AgentData.AgentID = Client.self.getAgentID();
		reply.AgentData.SessionID = Client.self.getSessionID();

		reply.Data.ButtonIndex = buttonIndex;
		reply.Data.ButtonLabel = Utils.stringToBytes(buttonlabel);
		reply.Data.ChatChannel = channel;
		reply.Data.ObjectID = objectID;

		Client.network.SendPacket(reply);
	}

	/// <summary>
	/// Accept invite for to a chatterbox session
	/// </summary>
	/// <param name="session_id"><seealso cref="UUID"/> of session to accept invite to</param>
	public void ChatterBoxAcceptInvite(UUID session_id) throws Exception
	{
		if (Client.network.getCurrentSim() == null || Client.network.getCurrentSim().Caps == null)
			throw new Exception("ChatSessionRequest capability is not currently available");

		URI url = Client.network.getCurrentSim().Caps.CapabilityURI("ChatSessionRequest");

		if (url != null)
		{
			ChatSessionAcceptInvitation acceptInvite = new ChatSessionAcceptInvitation();
			acceptInvite.SessionID = session_id;

			CapsHttpClient request = new CapsHttpClient(url);
			request.BeginGetResponse(acceptInvite.Serialize(), OSDFormat.Xml, Client.settings.CAPS_TIMEOUT);

			synchronized (GroupChatSessions.getDictionary())
			{
				if (!GroupChatSessions.containsKey(session_id))
					GroupChatSessions.add(session_id, new ArrayList<ChatSessionMember>());
			}
		}
		else
		{
			throw new Exception("ChatSessionRequest capability is not currently available");
		}

	}

	/// <summary>
	/// Start a friends conference
	/// </summary>
	/// <param name="participants"><seealso cref="UUID"/> List of UUIDs to start a conference with</param>
	/// <param name="tmp_session_id">the temportary session ID returned in the <see cref="OnJoinedGroupChat"/> callback></param>
	public void StartIMConference(List<UUID> participants, UUID tmp_session_id) throws Exception
	{
		if (Client.network.getCurrentSim() == null || Client.network.getCurrentSim().Caps == null)
			throw new Exception("ChatSessionRequest capability is not currently available");

		URI url = Client.network.getCurrentSim().Caps.CapabilityURI("ChatSessionRequest");

		if (url != null)
		{
			ChatSessionRequestStartConference startConference = new ChatSessionRequestStartConference();

			startConference.AgentsBlock = new UUID[participants.size()];
			for (int i = 0; i < participants.size(); i++)
				startConference.AgentsBlock[i] = participants.get(i);

			startConference.SessionID = tmp_session_id;

			CapsHttpClient request = new CapsHttpClient(url);
			request.BeginGetResponse(startConference.Serialize(), OSDFormat.Xml, Client.settings.CAPS_TIMEOUT);
		}
		else
		{
			throw new Exception("ChatSessionRequest capability is not currently available");
		}
	}

	//endregion Chat and instant messages

	//region Viewer Effects

	/// <summary>
	/// Start a particle stream between an agent and an object
	/// </summary>
	/// <param name="sourceAvatar"><seealso cref="UUID"/> Key of the source agent</param>
	/// <param name="targetObject"><seealso cref="UUID"/> Key of the target object</param>
	/// <param name="globalOffset"></param>
	/// <param name="type">The type from the <seealso cref="T:PointAtType"/> enum</param>
	/// <param name="effectID">A unique <seealso cref="UUID"/> for this effect</param>
	public void PointAtEffect(UUID sourceAvatar, UUID targetObject, Vector3d globalOffset, PointAtType type,
			UUID effectID)
	{
		ViewerEffectPacket effect = new ViewerEffectPacket();

		effect.AgentData.AgentID = Client.self.getAgentID();
		effect.AgentData.SessionID = Client.self.getSessionID();

		effect.Effect = new ViewerEffectPacket.EffectBlock[1];
		effect.Effect[0] = new ViewerEffectPacket.EffectBlock();
		effect.Effect[0].AgentID = Client.self.getAgentID();
		effect.Effect[0].Color = new byte[4];
		//TODO Verify following
		//	            effect.Effect[0].Duration = (type == PointAtType.Clear) ? 0.0f : Single.MaxValue / 4.0f;
		effect.Effect[0].Duration = (type == PointAtType.Clear) ? 0.0f : Float.MAX_VALUE / 4.0f;

		effect.Effect[0].ID = effectID;
		effect.Effect[0].Type = (byte)EffectType.PointAt.getIndex();

		byte[] typeData = new byte[57];
		if (sourceAvatar != UUID.Zero)
			Utils.arraycopy(sourceAvatar.GetBytes(), 0, typeData, 0, 16);
		if (targetObject != UUID.Zero)
			Utils.arraycopy(targetObject.GetBytes(), 0, typeData, 16, 16);
		Utils.arraycopy(globalOffset.getBytes(), 0, typeData, 32, 24);
		typeData[56] = (byte)type.getIndex();

		effect.Effect[0].TypeData = typeData;

		Client.network.SendPacket(effect);
	}

	/// <summary>
	/// Start a particle stream between an agent and an object
	/// </summary>
	/// <param name="sourceAvatar"><seealso cref="UUID"/> Key of the source agent</param>
	/// <param name="targetObject"><seealso cref="UUID"/> Key of the target object</param>
	/// <param name="globalOffset">A <seealso cref="Vector3d"/> representing the beams offset from the source</param>
	/// <param name="type">A <seealso cref="T:PointAtType"/> which sets the avatars lookat animation</param>
	/// <param name="effectID"><seealso cref="UUID"/> of the Effect</param>
	public void LookAtEffect(UUID sourceAvatar, UUID targetObject, Vector3d globalOffset, LookAtType type,
			UUID effectID)
	{
		ViewerEffectPacket effect = new ViewerEffectPacket();

		effect.AgentData.AgentID = Client.self.getAgentID();
		effect.AgentData.SessionID = Client.self.getSessionID();

		float duration;

		switch (type)
		{
		case Clear:
			duration = 2.0f;
			break;
		case Hover:
			duration = 1.0f;
			break;
		case FreeLook:
			duration = 2.0f;
			break;
		case Idle:
			duration = 3.0f;
			break;
		case AutoListen:
		case Respond:
			duration = 4.0f;
			break;
		case None:
		case Select:
		case Focus:
		case Mouselook:
			//TODO verify following
			//	                    duration = Single.MaxValue / 2.0f;
			duration = Float.MAX_VALUE / 2.0f;

			break;
		default:
			duration = 0.0f;
			break;
		}

		effect.Effect = new ViewerEffectPacket.EffectBlock[1];
		effect.Effect[0] = new ViewerEffectPacket.EffectBlock();
		effect.Effect[0].AgentID = Client.self.getAgentID();
		effect.Effect[0].Color = new byte[4];
		effect.Effect[0].Duration = duration;
		effect.Effect[0].ID = effectID;
		effect.Effect[0].Type = (byte)EffectType.LookAt.getIndex();

		byte[] typeData = new byte[57];
		Utils.arraycopy(sourceAvatar.GetBytes(), 0, typeData, 0, 16);
		Utils.arraycopy(targetObject.GetBytes(), 0, typeData, 16, 16);
		Utils.arraycopy(globalOffset.getBytes(), 0, typeData, 32, 24);
		typeData[56] = (byte)type.getIndex();

		effect.Effect[0].TypeData = typeData;

		Client.network.SendPacket(effect);
	}

	/// <summary>
	/// Create a particle beam between an avatar and an primitive 
	/// </summary>
	/// <param name="sourceAvatar">The ID of source avatar</param>
	/// <param name="targetObject">The ID of the target primitive</param>
	/// <param name="globalOffset">global offset</param>
	/// <param name="color">A <see cref="Color4"/> object containing the combined red, green, blue and alpha 
	/// color values of particle beam</param>
	/// <param name="duration">a float representing the duration the parcicle beam will last</param>
	/// <param name="effectID">A Unique ID for the beam</param>
	/// <seealso cref="ViewerEffectPacket"/>
	public void BeamEffect(UUID sourceAvatar, UUID targetObject, Vector3d globalOffset, Color4 color,
			float duration, UUID effectID)
	{
		ViewerEffectPacket effect = new ViewerEffectPacket();

		effect.AgentData.AgentID = Client.self.getAgentID();
		effect.AgentData.SessionID = Client.self.getSessionID();

		effect.Effect = new ViewerEffectPacket.EffectBlock[1];
		effect.Effect[0] = new ViewerEffectPacket.EffectBlock();
		effect.Effect[0].AgentID = Client.self.getAgentID();
		effect.Effect[0].Color = color.getBytes();
		effect.Effect[0].Duration = duration;
		effect.Effect[0].ID = effectID;
		effect.Effect[0].Type = (byte)EffectType.Beam.getIndex();

		byte[] typeData = new byte[56];
		Utils.arraycopy(sourceAvatar.GetBytes(), 0, typeData, 0, 16);
		Utils.arraycopy(targetObject.GetBytes(), 0, typeData, 16, 16);
		Utils.arraycopy(globalOffset.getBytes(), 0, typeData, 32, 24);

		effect.Effect[0].TypeData = typeData;

		Client.network.SendPacket(effect);
	}

	/// <summary>
	/// Create a particle swirl around a target position using a <seealso cref="ViewerEffectPacket"/> packet
	/// </summary>
	/// <param name="globalOffset">global offset</param>
	/// <param name="color">A <see cref="Color4"/> object containing the combined red, green, blue and alpha 
	/// color values of particle beam</param>
	/// <param name="duration">a float representing the duration the parcicle beam will last</param>
	/// <param name="effectID">A Unique ID for the beam</param>
	public void SphereEffect(Vector3d globalOffset, Color4 color, float duration, UUID effectID)
	{
		ViewerEffectPacket effect = new ViewerEffectPacket();

		effect.AgentData.AgentID = Client.self.getAgentID();
		effect.AgentData.SessionID = Client.self.getSessionID();

		effect.Effect = new ViewerEffectPacket.EffectBlock[1];
		effect.Effect[0] = new ViewerEffectPacket.EffectBlock();
		effect.Effect[0].AgentID = Client.self.getAgentID();
		effect.Effect[0].Color = color.getBytes();
		effect.Effect[0].Duration = duration;
		effect.Effect[0].ID = effectID;
		effect.Effect[0].Type = (byte)EffectType.Sphere.getIndex();

		byte[] typeData = new byte[56];
		Utils.arraycopy(UUID.Zero.GetBytes(), 0, typeData, 0, 16);
		Utils.arraycopy(UUID.Zero.GetBytes(), 0, typeData, 16, 16);
		Utils.arraycopy(globalOffset.getBytes(), 0, typeData, 32, 24);

		effect.Effect[0].TypeData = typeData;

		Client.network.SendPacket(effect);
	}


	//endregion Viewer Effects

	//region Movement Actions

	/// <summary>
	/// Sends a request to sit on the specified object
	/// </summary>
	/// <param name="targetID"><seealso cref="UUID"/> of the object to sit on</param>
	/// <param name="offset">Sit at offset</param>
	public void RequestSit(UUID targetID, Vector3 offset)
	{
		AgentRequestSitPacket requestSit = new AgentRequestSitPacket();
		requestSit.AgentData.AgentID = Client.self.getAgentID();
		requestSit.AgentData.SessionID = Client.self.getSessionID();
		requestSit.TargetObject.TargetID = targetID;
		requestSit.TargetObject.Offset = offset;
		Client.network.SendPacket(requestSit);
	}

	/// <summary>
	/// Follows a call to <seealso cref="RequestSit"/> to actually sit on the object
	/// </summary>
	public void Sit()
	{
		AgentSitPacket sit = new AgentSitPacket();
		sit.AgentData.AgentID = Client.self.getAgentID();
		sit.AgentData.SessionID = Client.self.getSessionID();
		Client.network.SendPacket(sit);
	}

	/// <summary>Stands up from sitting on a prim or the ground</summary>
	/// <returns>true of AgentUpdate was sent</returns>
	public boolean Stand()
	{
		if (Client.settings.SEND_AGENT_UPDATES)
		{
			Movement.setSitOnGround(false);
			Movement.setStandUp(true);
			Movement.SendUpdate();
			Movement.setStandUp(false);
			Movement.SendUpdate();
			return true;
		}
		else
		{
			JLogger.warn("Attempted to Stand() but agent updates are disabled");
			return false;
		}
	}

	/// <summary>
	/// Does a "ground sit" at the avatar's current position
	/// </summary>
	public void SitOnGround()
	{
		Movement.setSitOnGround(true);
		Movement.SendUpdate(true);
	}

	/// <summary>
	/// Starts or stops flying
	/// </summary>
	/// <param name="start">True to start flying, false to stop flying</param>
	public void Fly(boolean start)
	{
		if (start)
			Movement.setFly(true);
		else
			Movement.setFly(false);

		Movement.SendUpdate(true);
	}

	/// <summary>
	/// Starts or stops crouching
	/// </summary>
	/// <param name="crouching">True to start crouching, false to stop crouching</param>
	public void Crouch(boolean crouching)
	{
		Movement.setUpNeg(crouching);
		Movement.SendUpdate(true);
	}

	/// <summary>
	/// Starts a jump (begin holding the jump key)
	/// </summary>
	public void Jump(boolean jumping)
	{
		Movement.setUpPos(jumping);
		Movement.setFastUp(jumping);
		Movement.SendUpdate(true);
	}

	/// <summary>
	/// Use the autopilot sim function to move the avatar to a new
	/// position. Uses double precision to get precise movements
	/// </summary>
	/// <remarks>The z value is currently not handled properly by the simulator</remarks>
	/// <param name="globalX">Global X coordinate to move to</param>
	/// <param name="globalY">Global Y coordinate to move to</param>
	/// <param name="z">Z coordinate to move to</param>
	public void AutoPilot(double globalX, double globalY, double z)
	{
		GenericMessagePacket autopilot = new GenericMessagePacket();

		autopilot.AgentData.AgentID = Client.self.getAgentID();
		autopilot.AgentData.SessionID = Client.self.getSessionID();
		autopilot.AgentData.TransactionID = UUID.Zero;
		autopilot.MethodData.Invoice = UUID.Zero;
		autopilot.MethodData.Method = Utils.stringToBytes("autopilot");
		autopilot.ParamList = new GenericMessagePacket.ParamListBlock[3];
		autopilot.ParamList[0] = new GenericMessagePacket.ParamListBlock();
		autopilot.ParamList[0].Parameter = Utils.stringToBytes(Double.toString(globalX));
		autopilot.ParamList[1] = new GenericMessagePacket.ParamListBlock();
		autopilot.ParamList[1].Parameter = Utils.stringToBytes(Double.toString(globalY));
		autopilot.ParamList[2] = new GenericMessagePacket.ParamListBlock();
		autopilot.ParamList[2].Parameter = Utils.stringToBytes(Double.toString(z));

		Client.network.SendPacket(autopilot);
	}

	/// <summary>
	/// Use the autopilot sim function to move the avatar to a new position
	/// </summary>
	/// <remarks>The z value is currently not handled properly by the simulator</remarks>
	/// <param name="globalX">Integer value for the global X coordinate to move to</param>
	/// <param name="globalY">Integer value for the global Y coordinate to move to</param>
	/// <param name="z">Floating-point value for the Z coordinate to move to</param>
	//	        public void AutoPilot(ulong globalX, ulong globalY, float z)
	public void AutoPilot(BigInteger globalX, BigInteger globalY, float z)
	{
		GenericMessagePacket autopilot = new GenericMessagePacket();

		autopilot.AgentData.AgentID = Client.self.getAgentID();
		autopilot.AgentData.SessionID = Client.self.getSessionID();
		autopilot.AgentData.TransactionID = UUID.Zero;
		autopilot.MethodData.Invoice = UUID.Zero;
		autopilot.MethodData.Method = Utils.stringToBytes("autopilot");
		autopilot.ParamList = new GenericMessagePacket.ParamListBlock[3];
		autopilot.ParamList[0] = new GenericMessagePacket.ParamListBlock();
		autopilot.ParamList[0].Parameter = Utils.stringToBytes(globalX.toString());
		autopilot.ParamList[1] = new GenericMessagePacket.ParamListBlock();
		autopilot.ParamList[1].Parameter = Utils.stringToBytes(globalY.toString());
		autopilot.ParamList[2] = new GenericMessagePacket.ParamListBlock();
		autopilot.ParamList[2].Parameter = Utils.stringToBytes(Float.toString(z));

		Client.network.SendPacket(autopilot);
	}

	/// <summary>
	/// Use the autopilot sim function to move the avatar to a new position
	/// </summary>
	/// <remarks>The z value is currently not handled properly by the simulator</remarks>
	/// <param name="localX">Integer value for the local X coordinate to move to</param>
	/// <param name="localY">Integer value for the local Y coordinate to move to</param>
	/// <param name="z">Floating-point value for the Z coordinate to move to</param>
	public void AutoPilotLocal(int localX, int localY, float z)
	{
		//uint
		long x, y;
		long[] a = new long[2];

		Utils.longToUInts(Client.network.getCurrentSim().Handle.longValue(), a);
		x = a[0];
		y = a[1];
		AutoPilot(new BigInteger(Utils.int64ToBytesLit(x + localX)), new BigInteger(Utils.int64ToBytesLit(y + localY)), z);
	}

	/// <summary>Macro to cancel autopilot sim function</summary>
	/// <remarks>Not certain if this is how it is really done</remarks>
	/// <returns>true if control flags were set and AgentUpdate was sent to the simulator</returns>
	public boolean AutoPilotCancel()
	{
		if (Client.settings.SEND_AGENT_UPDATES)
		{
			Movement.setAtPos(true);
			Movement.SendUpdate();
			Movement.setAtPos(false);
			Movement.SendUpdate();
			return true;
		}
		else
		{
			JLogger.warn("Attempted to AutoPilotCancel() but agent updates are disabled");
			return false;
		}
	}

	//endregion Movement actions

	//region Touch and grab

	/// <summary>
	/// Grabs an object
	/// </summary>
	/// <param name="objectLocalID">an unsigned integer of the objects ID within the simulator</param>
	/// <seealso cref="Simulator.ObjectsPrimitives"/>
	public void Grab(long objectLocalID)
	{
		Grab(objectLocalID, Vector3.Zero, Vector3.Zero, Vector3.Zero, 0, Vector3.Zero, Vector3.Zero, Vector3.Zero);
	}

	/// <summary>
	/// Overload: Grab a simulated object
	/// </summary>
	/// <param name="objectLocalID">an unsigned integer of the objects ID within the simulator</param>
	/// <param name="grabOffset"></param>
	/// <param name="uvCoord">The texture coordinates to grab</param>
	/// <param name="stCoord">The surface coordinates to grab</param>
	/// <param name="faceIndex">The face of the position to grab</param>
	/// <param name="position">The region coordinates of the position to grab</param>
	/// <param name="normal">The surface normal of the position to grab (A normal is a vector perpindicular to the surface)</param>
	/// <param name="binormal">The surface binormal of the position to grab (A binormal is a vector tangen to the surface
	/// pointing along the U direction of the tangent space</param>
	public void Grab(long objectLocalID, Vector3 grabOffset, Vector3 uvCoord, Vector3 stCoord, int faceIndex, Vector3 position,
			Vector3 normal, Vector3 binormal)
	{
		ObjectGrabPacket grab = new ObjectGrabPacket();

		grab.AgentData.AgentID = Client.self.getAgentID();
		grab.AgentData.SessionID = Client.self.getSessionID();

		grab.ObjectData.LocalID = objectLocalID;
		grab.ObjectData.GrabOffset = grabOffset;

		grab.SurfaceInfo = new ObjectGrabPacket.SurfaceInfoBlock[1];
		grab.SurfaceInfo[0] = new ObjectGrabPacket.SurfaceInfoBlock();
		grab.SurfaceInfo[0].UVCoord = uvCoord;
		grab.SurfaceInfo[0].STCoord = stCoord;
		grab.SurfaceInfo[0].FaceIndex = faceIndex;
		grab.SurfaceInfo[0].Position = position;
		grab.SurfaceInfo[0].Normal = normal;
		grab.SurfaceInfo[0].Binormal = binormal;

		Client.network.SendPacket(grab);
	}

	/// <summary>
	/// Drag an object
	/// </summary>
	/// <param name="objectID"><seealso cref="UUID"/> of the object to drag</param>
	/// <param name="grabPosition">Drag target in region coordinates</param>
	public void GrabUpdate(UUID objectID, Vector3 grabPosition)
	{
		GrabUpdate(objectID, grabPosition, Vector3.Zero, Vector3.Zero, Vector3.Zero, 0, Vector3.Zero, Vector3.Zero, Vector3.Zero);
	}

	/// <summary>
	/// Overload: Drag an object
	/// </summary>
	/// <param name="objectID"><seealso cref="UUID"/> of the object to drag</param>
	/// <param name="grabPosition">Drag target in region coordinates</param>
	/// <param name="grabOffset"></param>
	/// <param name="uvCoord">The texture coordinates to grab</param>
	/// <param name="stCoord">The surface coordinates to grab</param>
	/// <param name="faceIndex">The face of the position to grab</param>
	/// <param name="position">The region coordinates of the position to grab</param>
	/// <param name="normal">The surface normal of the position to grab (A normal is a vector perpindicular to the surface)</param>
	/// <param name="binormal">The surface binormal of the position to grab (A binormal is a vector tangen to the surface
	/// pointing along the U direction of the tangent space</param>
	public void GrabUpdate(UUID objectID, Vector3 grabPosition, Vector3 grabOffset, Vector3 uvCoord, Vector3 stCoord, int faceIndex, Vector3 position,
			Vector3 normal, Vector3 binormal)
	{
		ObjectGrabUpdatePacket grab = new ObjectGrabUpdatePacket();
		grab.AgentData.AgentID = Client.self.getAgentID();
		grab.AgentData.SessionID = Client.self.getSessionID();

		grab.ObjectData.ObjectID = objectID;
		grab.ObjectData.GrabOffsetInitial = grabOffset;
		grab.ObjectData.GrabPosition = grabPosition;
		grab.ObjectData.TimeSinceLast = 0;

		grab.SurfaceInfo = new ObjectGrabUpdatePacket.SurfaceInfoBlock[1];
		grab.SurfaceInfo[0] = new ObjectGrabUpdatePacket.SurfaceInfoBlock();
		grab.SurfaceInfo[0].UVCoord = uvCoord;
		grab.SurfaceInfo[0].STCoord = stCoord;
		grab.SurfaceInfo[0].FaceIndex = faceIndex;
		grab.SurfaceInfo[0].Position = position;
		grab.SurfaceInfo[0].Normal = normal;
		grab.SurfaceInfo[0].Binormal = binormal;

		Client.network.SendPacket(grab);
	}

	/// <summary>
	/// Release a grabbed object
	/// </summary>
	/// <param name="objectLocalID">The Objects Simulator Local ID</param>
	/// <seealso cref="Simulator.ObjectsPrimitives"/>
	/// <seealso cref="Grab"/>
	/// <seealso cref="GrabUpdate"/>
	public void DeGrab(long objectLocalID)
	{
		DeGrab(objectLocalID, Vector3.Zero, Vector3.Zero, 0, Vector3.Zero, Vector3.Zero, Vector3.Zero);
	}

	/// <summary>
	/// Release a grabbed object
	/// </summary>
	/// <param name="objectLocalID">The Objects Simulator Local ID</param>
	/// <param name="uvCoord">The texture coordinates to grab</param>
	/// <param name="stCoord">The surface coordinates to grab</param>
	/// <param name="faceIndex">The face of the position to grab</param>
	/// <param name="position">The region coordinates of the position to grab</param>
	/// <param name="normal">The surface normal of the position to grab (A normal is a vector perpindicular to the surface)</param>
	/// <param name="binormal">The surface binormal of the position to grab (A binormal is a vector tangen to the surface
	/// pointing along the U direction of the tangent space</param>
	public void DeGrab(long objectLocalID, Vector3 uvCoord, Vector3 stCoord, int faceIndex, Vector3 position,
			Vector3 normal, Vector3 binormal)
	{
		ObjectDeGrabPacket degrab = new ObjectDeGrabPacket();
		degrab.AgentData.AgentID = Client.self.getAgentID();
		degrab.AgentData.SessionID = Client.self.getSessionID();

		degrab.ObjectData.LocalID = objectLocalID;

		degrab.SurfaceInfo = new ObjectDeGrabPacket.SurfaceInfoBlock[1];
		degrab.SurfaceInfo[0] = new ObjectDeGrabPacket.SurfaceInfoBlock();
		degrab.SurfaceInfo[0].UVCoord = uvCoord;
		degrab.SurfaceInfo[0].STCoord = stCoord;
		degrab.SurfaceInfo[0].FaceIndex = faceIndex;
		degrab.SurfaceInfo[0].Position = position;
		degrab.SurfaceInfo[0].Normal = normal;
		degrab.SurfaceInfo[0].Binormal = binormal;

		Client.network.SendPacket(degrab);
	}

	/// <summary>
	/// Touches an object
	/// </summary>
	/// <param name="objectLocalID">an unsigned integer of the objects ID within the simulator</param>
	/// <seealso cref="Simulator.ObjectsPrimitives"/>
	public void Touch(long objectLocalID)
	{
		Client.self.Grab(objectLocalID);
		Client.self.DeGrab(objectLocalID);
	}

	//endregion Touch and grab

	//region Money

	/// <summary>
	/// Request the current L$ balance
	/// </summary>
	public void RequestBalance()
	{
		MoneyBalanceRequestPacket money = new MoneyBalanceRequestPacket();
		money.AgentData.AgentID = Client.self.getAgentID();
		money.AgentData.SessionID = Client.self.getSessionID();
		money.MoneyData.TransactionID = UUID.Zero;

		Client.network.SendPacket(money);
	}

	/// <summary>
	/// Give Money to destination Avatar
	/// </summary>
	/// <param name="target">UUID of the Target Avatar</param>
	/// <param name="amount">Amount in L$</param>
	public void GiveAvatarMoney(UUID target, int amount)
	{
		GiveMoney(target, amount, "", MoneyTransactionType.Gift, TransactionFlags.None);
	}

	/// <summary>
	/// Give Money to destination Avatar
	/// </summary>
	/// <param name="target">UUID of the Target Avatar</param>
	/// <param name="amount">Amount in L$</param>
	/// <param name="description">Description that will show up in the
	/// recipients transaction history</param>
	public void GiveAvatarMoney(UUID target, int amount, String description)
	{
		GiveMoney(target, amount, description, MoneyTransactionType.Gift, TransactionFlags.None);
	}

	/// <summary>
	/// Give L$ to an object
	/// </summary>
	/// <param name="target">object <seealso cref="UUID"/> to give money to</param>
	/// <param name="amount">amount of L$ to give</param>
	/// <param name="objectName">name of object</param>
	public void GiveObjectMoney(UUID target, int amount, String objectName)
	{
		GiveMoney(target, amount, objectName, MoneyTransactionType.PayObject, TransactionFlags.None);
	}

	/// <summary>
	/// Give L$ to a group
	/// </summary>
	/// <param name="target">group <seealso cref="UUID"/> to give money to</param>
	/// <param name="amount">amount of L$ to give</param>
	public void GiveGroupMoney(UUID target, int amount)
	{
		GiveMoney(target, amount, "", MoneyTransactionType.Gift, TransactionFlags.DestGroup);
	}

	/// <summary>
	/// Give L$ to a group
	/// </summary>
	/// <param name="target">group <seealso cref="UUID"/> to give money to</param>
	/// <param name="amount">amount of L$ to give</param>
	/// <param name="description">description of transaction</param>
	public void GiveGroupMoney(UUID target, int amount, String description)
	{
		GiveMoney(target, amount, description, MoneyTransactionType.Gift, TransactionFlags.DestGroup);
	}

	/// <summary>
	/// Pay texture/animation upload fee
	/// </summary>
	public void PayUploadFee()
	{
		GiveMoney(UUID.Zero, Client.settings.UPLOAD_COST(), "", MoneyTransactionType.UploadCharge,
				TransactionFlags.None);
	}

	/// <summary>
	/// Pay texture/animation upload fee
	/// </summary>
	/// <param name="description">description of the transaction</param>
	public void PayUploadFee(String description)
	{
		GiveMoney(UUID.Zero, Client.settings.UPLOAD_COST(), description, MoneyTransactionType.UploadCharge,
				TransactionFlags.None);
	}

	/// <summary>
	/// Give Money to destination Object or Avatar
	/// </summary>
	/// <param name="target">UUID of the Target Object/Avatar</param>
	/// <param name="amount">Amount in L$</param>
	/// <param name="description">Reason (Optional normally)</param>
	/// <param name="type">The type of transaction</param>
	/// <param name="flags">Transaction flags, mostly for identifying group
	/// transactions</param>
	public void GiveMoney(UUID target, int amount, String description, MoneyTransactionType type, TransactionFlags flags)
	{
		MoneyTransferRequestPacket money = new MoneyTransferRequestPacket();
		money.AgentData.AgentID = this.id;
		money.AgentData.SessionID = Client.self.getSessionID();
		money.MoneyData.Description = Utils.stringToBytes(description);
		money.MoneyData.DestID = target;
		money.MoneyData.SourceID = this.id;
		money.MoneyData.TransactionType = (int)type.getIndex();
		money.MoneyData.AggregatePermInventory = 0; // This is weird, apparently always set to zero though
		money.MoneyData.AggregatePermNextOwner = 0; // This is weird, apparently always set to zero though
		money.MoneyData.Flags = (byte)flags.getIndex();
		money.MoneyData.Amount = amount;

		Client.network.SendPacket(money);
	}

	//endregion Money

	//TODO Need to Implement
	//	        //region Gestures
	//	        /// <summary>
	//	        /// Plays a gesture
	//	        /// </summary>
	//	        /// <param name="gestureID">Asset <seealso cref="UUID"/> of the gesture</param>
	//	        public void PlayGesture(UUID gestureID)
	//	        {
	//	            Thread t = new Thread(new ThreadStart(delegate()
	//	                {
	//	                    // First fetch the guesture
	//	                    AssetGesture gesture = null;
	//	
	//	                    if (gestureCache.containsKey(gestureID))
	//	                    {
	//	                        gesture = gestureCache[gestureID];
	//	                    }
	//	                    else
	//	                    {
	//	                        AutoResetEvent gotAsset = new AutoResetEvent(false);
	//	
	//	                        Client.Assets.RequestAsset(gestureID, AssetType.Gesture, true,
	//	                                                    delegate(AssetDownload transfer, Asset asset)
	//	                                                    {
	//	                                                        if (transfer.Success)
	//	                                                        {
	//	                                                            gesture = (AssetGesture)asset;
	//	                                                        }
	//	
	//	                                                        gotAsset.Set();
	//	                                                    }
	//	                        );
	//	
	//	                        gotAsset.WaitOne(30 * 1000, false);
	//	
	//	                        if (gesture != null && gesture.Decode())
	//	                        {
	//	                            synchronized (gestureCache)
	//	                            {
	//	                                if (!gestureCache.containsKey(gestureID))
	//	                                {
	//	                                    gestureCache[gestureID] = gesture;
	//	                                }
	//	                            }
	//	                        }
	//	                    }
	//	
	//	                    // We got it, now we play it
	//	                    if (gesture != null)
	//	                    {
	//	                        for (int i = 0; i < gesture.Sequence.Count; i++)
	//	                        {
	//	                            GestureStep step = gesture.Sequence[i];
	//	
	//	                            switch (step.GestureStepType)
	//	                            {
	//	                                case GestureStepType.Chat:
	//	                                    Chat(((GestureStepChat)step).Text, 0, ChatType.Normal);
	//	                                    break;
	//	
	//	                                case GestureStepType.Animation:
	//	                                    GestureStepAnimation anim = (GestureStepAnimation)step;
	//	
	//	                                    if (anim.AnimationStart)
	//	                                    {
	//	                                        if (SignaledAnimations.containsKey(anim.ID))
	//	                                        {
	//	                                            AnimationStop(anim.ID, true);
	//	                                        }
	//	                                        AnimationStart(anim.ID, true);
	//	                                    }
	//	                                    else
	//	                                    {
	//	                                        AnimationStop(anim.ID, true);
	//	                                    }
	//	                                    break;
	//	
	//	                                case GestureStepType.Sound:
	//	                                    Client.Sound.PlaySound(((GestureStepSound)step).ID);
	//	                                    break;
	//	
	//	                                case GestureStepType.Wait:
	//	                                    GestureStepWait wait = (GestureStepWait)step;
	//	                                    if (wait.WaitForTime)
	//	                                    {
	//	                                        Thread.Sleep((int)(1000f * wait.WaitTime));
	//	                                    }
	//	                                    if (wait.WaitForAnimation)
	//	                                    {
	//	                                        // TODO: implement waiting for all animations to end that were triggered
	//	                                        // during playing of this guesture sequence
	//	                                    }
	//	                                    break;
	//	                            }
	//	                        }
	//	                    }
	//	                }));
	//	
	//	            t.IsBackground = true;
	//	            t.Name = "Gesture thread: " + gestureID;
	//	            t.Start();
	//	        }
	//	
	//	        /// <summary>
	//	        /// Mark gesture active
	//	        /// </summary>
	//	        /// <param name="invID">Inventory <seealso cref="UUID"/> of the gesture</param>
	//	        /// <param name="assetID">Asset <seealso cref="UUID"/> of the gesture</param>
	//	        public void ActivateGesture(UUID invID, UUID assetID)
	//	        {
	//	            ActivateGesturesPacket p = new ActivateGesturesPacket();
	//	
	//	            p.AgentData.AgentID = getAgentID();
	//	            p.AgentData.SessionID = getSessionID();
	//	            p.AgentData.Flags = 0x00;
	//	
	//	            ActivateGesturesPacket.DataBlock b = new ActivateGesturesPacket.DataBlock();
	//	            b.ItemID = invID;
	//	            b.AssetID = assetID;
	//	            b.GestureFlags = 0x00;
	//	
	//	            p.Data = new ActivateGesturesPacket.DataBlock[1];
	//	            p.Data[0] = b;
	//	
	//	            Client.network.SendPacket(p);
	//	
	//	        }
	//	
	//	        /// <summary>
	//	        /// Mark gesture inactive
	//	        /// </summary>
	//	        /// <param name="invID">Inventory <seealso cref="UUID"/> of the gesture</param>
	//	        public void DeactivateGesture(UUID invID)
	//	        {
	//	            DeactivateGesturesPacket p = new DeactivateGesturesPacket();
	//	
	//	            p.AgentData.AgentID = getAgentID();
	//	            p.AgentData.SessionID = getSessionID();
	//	            p.AgentData.Flags = 0x00;
	//	
	//	            DeactivateGesturesPacket.DataBlock b = new DeactivateGesturesPacket.DataBlock();
	//	            b.ItemID = invID;
	//	            b.GestureFlags = 0x00;
	//	
	//	            p.Data = new DeactivateGesturesPacket.DataBlock[1];
	//	            p.Data[0] = b;
	//	
	//	            Client.network.SendPacket(p);
	//	        }
	//	        //endregion
	//	
	//	        //region Animations
	//	
	//	        /// <summary>
	//	        /// Send an AgentAnimation packet that toggles a single animation on
	//	        /// </summary>
	//	        /// <param name="animation">The <seealso cref="UUID"/> of the animation to start playing</param>
	//	        /// <param name="reliable">Whether to ensure delivery of this packet or not</param>
	//	        public void AnimationStart(UUID animation, boolean reliable)
	//	        {
	//	            Map<UUID, Boolean> animations = new HashMap<UUID, Boolean>();
	//	            animations.put(animation, true);
	//	
	//	            Animate(animations, reliable);
	//	        }
	//	
	//	        /// <summary>
	//	        /// Send an AgentAnimation packet that toggles a single animation off
	//	        /// </summary>
	//	        /// <param name="animation">The <seealso cref="UUID"/> of a 
	//	        /// currently playing animation to stop playing</param>
	//	        /// <param name="reliable">Whether to ensure delivery of this packet or not</param>
	//	        public void AnimationStop(UUID animation, boolean reliable)
	//	        {
	//	            Map<UUID, Boolean> animations = new HashMap<UUID, Boolean>();
	//	            animations.put(animation, false);
	//	
	//	            Animate(animations, reliable);
	//	        }
	//	
	//	        /// <summary>
	//	        /// Send an AgentAnimation packet that will toggle animations on or off
	//	        /// </summary>
	//	        /// <param name="animations">A list of animation <seealso cref="UUID"/>s, and whether to
	//	        /// turn that animation on or off</param>
	//	        /// <param name="reliable">Whether to ensure delivery of this packet or not</param>
	//	        public void Animate(Map<UUID, Boolean> animations, boolean reliable)
	//	        {
	//	            AgentAnimationPacket animate = new AgentAnimationPacket();
	//	            animate.header.Reliable = reliable;
	//	
	//	            animate.AgentData.AgentID = Client.self.getAgentID();
	//	            animate.AgentData.SessionID = Client.self.getSessionID();
	//	            animate.AnimationList = new AgentAnimationPacket.AnimationListBlock[animations.size()];
	//	            int i = 0;
	//	
	//	            for (Entry<UUID, Boolean> animation : animations.entrySet())
	//	            {
	//	                animate.AnimationList[i] = new AgentAnimationPacket.AnimationListBlock();
	//	                animate.AnimationList[i].AnimID = animation.getKey();
	//	                animate.AnimationList[i].StartAnim = animation.getValue();
	//	
	//	                i++;
	//	            }
	//	
	//	            // TODO: Implement support for this
	//	            animate.PhysicalAvatarEventList = new AgentAnimationPacket.PhysicalAvatarEventListBlock[0];
	//	
	//	            Client.network.SendPacket(animate);
	//	        }
	//	
	//	        //endregion Animations
	//	
	//	        //region Teleporting
	//	
	//	        /// <summary>
	//	        /// Teleports agent to their stored home location
	//	        /// </summary>
	//	        /// <returns>true on successful teleport to home location</returns>
	//	        public boolean GoHome() throws InterruptedException
	//	        {
	//	            return Teleport(UUID.Zero);
	//	        }
	//	
	//	        /// <summary>
	//	        /// Teleport agent to a landmark
	//	        /// </summary>
	//	        /// <param name="landmark"><seealso cref="UUID"/> of the landmark to teleport agent to</param>
	//	        /// <returns>true on success, false on failure</returns>
	//	        public boolean Teleport(UUID landmark) throws InterruptedException
	//	        {
	//	            teleportStat = TeleportStatus.None;
	//	            teleportEvent.reset();
	//	            TeleportLandmarkRequestPacket p = new TeleportLandmarkRequestPacket();
	//	            p.Info = new TeleportLandmarkRequestPacket.InfoBlock();
	//	            p.Info.AgentID = Client.self.getAgentID();
	//	            p.Info.SessionID = Client.self.getSessionID();
	//	            p.Info.LandmarkID = landmark;
	//	            Client.network.SendPacket(p);
	//	
	//	            teleportEvent.waitOne(Client.settings.TELEPORT_TIMEOUT);
	//	
	//	            if (teleportStat == TeleportStatus.None ||
	//	                teleportStat == TeleportStatus.Start ||
	//	                teleportStat == TeleportStatus.Progress)
	//	            {
	//	                teleportMessage = "Teleport timed out.";
	//	                teleportStat = TeleportStatus.Failed;
	//	            }
	//	
	//	            return (teleportStat == TeleportStatus.Finished);
	//	        }
	//	
	//	        /// <summary>
	//	        /// Attempt to look up a simulator name and teleport to the discovered
	//	        /// destination
	//	        /// </summary>
	//	        /// <param name="simName">Region name to look up</param>
	//	        /// <param name="position">Position to teleport to</param>
	//	        /// <returns>True if the lookup and teleport were successful, otherwise
	//	        /// false</returns>
	//	        public boolean Teleport(String simName, Vector3 position)
	//	        {
	//	            return Teleport(simName, position, new Vector3(0, 1.0f, 0));
	//	        }
	//	
	//	        /// <summary>
	//	        /// Attempt to look up a simulator name and teleport to the discovered
	//	        /// destination
	//	        /// </summary>
	//	        /// <param name="simName">Region name to look up</param>
	//	        /// <param name="position">Position to teleport to</param>
	//	        /// <param name="lookAt">Target to look at</param>
	//	        /// <returns>True if the lookup and teleport were successful, otherwise
	//	        /// false</returns>
	//	        public boolean Teleport(String simName, Vector3 position, Vector3 lookAt)
	//	        {
	//	            if (Client.network.getCurrentSim() == null)
	//	                return false;
	//	
	//	            teleportStat = TeleportStatus.None;
	//	
	//	            if (simName != Client.network.getCurrentSim().Name)
	//	            {
	//	                // Teleporting to a foreign sim
	//	                GridRegion region;
	//	
	//	                if (Client.grid.GetGridRegion(simName, GridLayerType.Objects, out region))
	//	                {
	//	                    return Teleport(region.RegionHandle, position, lookAt);
	//	                }
	//	                else
	//	                {
	//	                    teleportMessage = "Unable to resolve name: " + simName;
	//	                    teleportStat = TeleportStatus.Failed;
	//	                    return false;
	//	                }
	//	            }
	//	            else
	//	            {
	//	                // Teleporting to the sim we're already in
	//	                return Teleport(Client.network.getCurrentSim().Handle, position, lookAt);
	//	            }
	//	        }
	//	
	//	        /// <summary>
	//	        /// Teleport agent to another region
	//	        /// </summary>
	//	        /// <param name="regionHandle">handle of region to teleport agent to</param>
	//	        /// <param name="position"><seealso cref="Vector3"/> position in destination sim to teleport to</param>
	//	        /// <returns>true on success, false on failure</returns>
	//	        /// <remarks>This call is blocking</remarks>
	//	        public boolean Teleport(ulong regionHandle, Vector3 position)
	//	        {
	//	            return Teleport(regionHandle, position, new Vector3(0.0f, 1.0f, 0.0f));
	//	        }
	//	
	//	        /// <summary>
	//	        /// Teleport agent to another region
	//	        /// </summary>
	//	        /// <param name="regionHandle">handle of region to teleport agent to</param>
	//	        /// <param name="position"><seealso cref="Vector3"/> position in destination sim to teleport to</param>
	//	        /// <param name="lookAt"><seealso cref="Vector3"/> direction in destination sim agent will look at</param>
	//	        /// <returns>true on success, false on failure</returns>
	//	        /// <remarks>This call is blocking</remarks>
	//	        public boolean Teleport(ulong regionHandle, Vector3 position, Vector3 lookAt)
	//	        {
	//	            if (Client.network.getCurrentSim() == null ||
	//	                Client.network.getCurrentSim().Caps == null ||
	//	                !Client.network.getCurrentSim().Caps.IsEventQueueRunning)
	//	            {
	//	                // Wait a bit to see if the event queue comes online
	//	                AutoResetEvent queueEvent = new AutoResetEvent(false);
	//	                EventHandler<EventQueueRunningEventArgs> queueCallback =
	//	                    delegate(object sender, EventQueueRunningEventArgs e)
	//	                    {
	//	                        if (e.Simulator == Client.network.getCurrentSim())
	//	                            queueEvent.Set();
	//	                    };
	//	
	//	                Client.network.EventQueueRunning += queueCallback;
	//	                queueEvent.WaitOne(10 * 1000, false);
	//	                Client.network.EventQueueRunning -= queueCallback;
	//	            }
	//	
	//	            teleportStat = TeleportStatus.None;
	//	            teleportEvent.reset();
	//	
	//	            RequestTeleport(regionHandle, position, lookAt);
	//	
	//	            teleportEvent.WaitOne(Client.settings.TELEPORT_TIMEOUT, false);
	//	
	//	            if (teleportStat == TeleportStatus.None ||
	//	                teleportStat == TeleportStatus.Start ||
	//	                teleportStat == TeleportStatus.Progress)
	//	            {
	//	                teleportMessage = "Teleport timed out.";
	//	                teleportStat = TeleportStatus.Failed;
	//	            }
	//	
	//	            return (teleportStat == TeleportStatus.Finished);
	//	        }
	//	
	//        /// <summary>
	//        /// Request teleport to a another simulator
	//        /// </summary>
	//        /// <param name="regionHandle">handle of region to teleport agent to</param>
	//        /// <param name="position"><seealso cref="Vector3"/> position in destination sim to teleport to</param>
	//        public void RequestTeleport(ulong regionHandle, Vector3 position)
	//        {
	//            RequestTeleport(regionHandle, position, new Vector3(0.0f, 1.0f, 0.0f));
	//        }
	//
	//        /// <summary>
	//        /// Request teleport to a another simulator
	//        /// </summary>
	//        /// <param name="regionHandle">handle of region to teleport agent to</param>
	//        /// <param name="position"><seealso cref="Vector3"/> position in destination sim to teleport to</param>
	//        /// <param name="lookAt"><seealso cref="Vector3"/> direction in destination sim agent will look at</param>
	//        public void RequestTeleport(ulong regionHandle, Vector3 position, Vector3 lookAt)
	//        {
	//            if (Client.network.getCurrentSim() != null &&
	//                Client.network.getCurrentSim().Caps != null &&
	//                Client.network.getCurrentSim().Caps.IsEventQueueRunning)
	//            {
	//                TeleportLocationRequestPacket teleport = new TeleportLocationRequestPacket();
	//                teleport.AgentData.AgentID = Client.self.getAgentID();
	//                teleport.AgentData.SessionID = Client.self.getSessionID();
	//                teleport.Info.LookAt = lookAt;
	//                teleport.Info.Position = position;
	//                teleport.Info.RegionHandle = regionHandle;
	//
	//                Logger.Log("Requesting teleport to region handle " + regionHandle.ToString(), Helpers.LogLevel.Info, Client);
	//
	//                Client.network.SendPacket(teleport);
	//            }
	//            else
	//            {
	//                teleportMessage = "CAPS event queue is not running";
	//                teleportEvent.Set();
	//                teleportStat = TeleportStatus.Failed;
	//            }
	//        }
	//
	//        /// <summary>
	//        /// Teleport agent to a landmark
	//        /// </summary>
	//        /// <param name="landmark"><seealso cref="UUID"/> of the landmark to teleport agent to</param>
	//        public void RequestTeleport(UUID landmark)
	//        {
	//            TeleportLandmarkRequestPacket p = new TeleportLandmarkRequestPacket();
	//            p.Info = new TeleportLandmarkRequestPacket.InfoBlock();
	//            p.Info.AgentID = Client.self.getAgentID();
	//            p.Info.SessionID = Client.self.getSessionID();
	//            p.Info.LandmarkID = landmark;
	//            Client.network.SendPacket(p);
	//        }
	//
	//        /// <summary>
	//        /// Send a teleport lure to another avatar with default "Join me in ..." invitation message
	//        /// </summary>
	//        /// <param name="targetID">target avatars <seealso cref="UUID"/> to lure</param>
	//        public void SendTeleportLure(UUID targetID)
	//        {
	//            SendTeleportLure(targetID, "Join me in " + Client.network.getCurrentSim().Name + "!");
	//        }
	//
	//        /// <summary>
	//        /// Send a teleport lure to another avatar with custom invitation message
	//        /// </summary>
	//        /// <param name="targetID">target avatars <seealso cref="UUID"/> to lure</param>
	//        /// <param name="message">custom message to send with invitation</param>
	//        public void SendTeleportLure(UUID targetID, String message)
	//        {
	//            StartLurePacket p = new StartLurePacket();
	//            p.AgentData.AgentID = Client.Self.id;
	//            p.AgentData.SessionID = Client.self.getSessionID();
	//            p.Info.LureType = 0;
	//            p.Info.Message = Utils.stringToBytes(message);
	//            p.TargetData = new StartLurePacket.TargetDataBlock[] { new StartLurePacket.TargetDataBlock() };
	//            p.TargetData[0].TargetID = targetID;
	//            Client.network.SendPacket(p);
	//        }
	//
	//        /// <summary>
	//        /// Respond to a teleport lure by either accepting it and initiating 
	//        /// the teleport, or denying it
	//        /// </summary>
	//        /// <param name="requesterID"><seealso cref="UUID"/> of the avatar sending the lure</param>
	//        /// <param name="sessionID">IM session <seealso cref="UUID"/> of the incoming lure request</param>
	//        /// <param name="accept">true to accept the lure, false to decline it</param>
	//        public void TeleportLureRespond(UUID requesterID, UUID sessionID, boolean accept)
	//        {
	//            if (accept)
	//            {
	//                TeleportLureRequestPacket lure = new TeleportLureRequestPacket();
	//
	//                lure.Info.AgentID = Client.self.getAgentID();
	//                lure.Info.SessionID = Client.self.getSessionID();
	//                lure.Info.LureID = sessionID;
	//                lure.Info.TeleportFlags = (uint)TeleportFlags.ViaLure;
	//
	//                Client.network.SendPacket(lure);
	//            }
	//            else
	//            {
	//                InstantMessage(Name, requesterID, "", sessionID,
	//                    accept ? InstantMessageDialog.AcceptTeleport : InstantMessageDialog.DenyTeleport,
	//                    InstantMessageOnline.Offline, this.getSimPosition(), UUID.Zero, Utils.EmptyBytes);
	//            }
	//        }
	//
	//        //endregion Teleporting
	//
	//        //region Misc
	//
	//        /// <summary>
	//        /// Update agent profile
	//        /// </summary>
	//        /// <param name="profile"><seealso cref="OpenMetaverse.Avatar.AvatarProperties"/> struct containing updated 
	//        /// profile information</param>
	//        public void UpdateProfile(Avatar.AvatarProperties profile)
	//        {
	//            AvatarPropertiesUpdatePacket apup = new AvatarPropertiesUpdatePacket();
	//            apup.AgentData.AgentID = id;
	//            apup.AgentData.SessionID = sessionID;
	//            apup.PropertiesData.AboutText = Utils.stringToBytes(profile.AboutText);
	//            apup.PropertiesData.AllowPublish = profile.AllowPublish;
	//            apup.PropertiesData.FLAboutText = Utils.stringToBytes(profile.FirstLifeText);
	//            apup.PropertiesData.FLImageID = profile.FirstLifeImage;
	//            apup.PropertiesData.ImageID = profile.ProfileImage;
	//            apup.PropertiesData.MaturePublish = profile.MaturePublish;
	//            apup.PropertiesData.ProfileURL = Utils.stringToBytes(profile.ProfileURL);
	//
	//            Client.network.SendPacket(apup);
	//        }
	//
	//        /// <summary>
	//        /// Update agents profile interests
	//        /// </summary>
	//        /// <param name="interests">selection of interests from <seealso cref="T:OpenMetaverse.Avatar.Interests"/> struct</param>
	//        public void UpdateInterests(Avatar.Interests interests)
	//        {
	//            AvatarInterestsUpdatePacket aiup = new AvatarInterestsUpdatePacket();
	//            aiup.AgentData.AgentID = id;
	//            aiup.AgentData.SessionID = sessionID;
	//            aiup.PropertiesData.LanguagesText = Utils.stringToBytes(interests.LanguagesText);
	//            aiup.PropertiesData.SkillsMask = interests.SkillsMask;
	//            aiup.PropertiesData.SkillsText = Utils.stringToBytes(interests.SkillsText);
	//            aiup.PropertiesData.WantToMask = interests.WantToMask;
	//            aiup.PropertiesData.WantToText = Utils.stringToBytes(interests.WantToText);
	//
	//            Client.network.SendPacket(aiup);
	//        }
	//
	//        /// <summary>
	//        /// Set the height and the width of the client window. This is used
	//        /// by the server to build a virtual camera frustum for our avatar
	//        /// </summary>
	//        /// <param name="height">New height of the viewer window</param>
	//        /// <param name="width">New width of the viewer window</param>
	//        public void SetHeightWidth(ushort height, ushort width)
	//        {
	//            AgentHeightWidthPacket heightwidth = new AgentHeightWidthPacket();
	//            heightwidth.AgentData.AgentID = Client.self.getAgentID();
	//            heightwidth.AgentData.SessionID = Client.self.getSessionID();
	//            heightwidth.AgentData.CircuitCode = Client.network.CircuitCode;
	//            heightwidth.HeightWidthBlock.Height = height;
	//            heightwidth.HeightWidthBlock.Width = width;
	//            heightwidth.HeightWidthBlock.GenCounter = heightWidthGenCounter++;
	//
	//            Client.network.SendPacket(heightwidth);
	//        }
	//
	//        /// <summary>
	//        /// Request the list of muted objects and avatars for this agent
	//        /// </summary>
	//        public void RequestMuteList()
	//        {
	//            MuteListRequestPacket mute = new MuteListRequestPacket();
	//            mute.AgentData.AgentID = Client.self.getAgentID();
	//            mute.AgentData.SessionID = Client.self.getSessionID();
	//            mute.MuteData.MuteCRC = 0;
	//
	//            Client.network.SendPacket(mute);
	//        }
	//
	//        /// <summary>
	//        /// Mute an object, resident, etc.
	//        /// </summary>
	//        /// <param name="type">Mute type</param>
	//        /// <param name="id">Mute UUID</param>
	//        /// <param name="name">Mute name</param>
	//        public void UpdateMuteListEntry(MuteType type, UUID id, String name)
	//        {
	//            UpdateMuteListEntry(type, id, name, MuteFlags.Default);
	//        }
	//
	//        /// <summary>
	//        /// Mute an object, resident, etc.
	//        /// </summary>
	//        /// <param name="type">Mute type</param>
	//        /// <param name="id">Mute UUID</param>
	//        /// <param name="name">Mute name</param>
	//        /// <param name="flags">Mute flags</param>
	//        public void UpdateMuteListEntry(MuteType type, UUID id, String name, MuteFlags flags)
	//        {
	//            UpdateMuteListEntryPacket p = new UpdateMuteListEntryPacket();
	//            p.AgentData.AgentID = Client.self.getAgentID();
	//            p.AgentData.SessionID = Client.self.getSessionID();
	//
	//            p.MuteData.MuteType = (int)type;
	//            p.MuteData.MuteID = id;
	//            p.MuteData.MuteName = Utils.stringToBytes(name);
	//            p.MuteData.MuteFlags = (uint)flags;
	//
	//            Client.network.SendPacket(p);
	//
	//            MuteEntry me = new MuteEntry();
	//            me.Type = type;
	//            me.ID = id;
	//            me.Name = name;
	//            me.Flags = flags;
	//            synchronized (MuteList.getDictionary())
	//            {
	//                MuteList[string.Format("{0}|{1}", me.ID, me.Name)] = me;
	//            }
	//            OnMuteListUpdated(EventArgs.Empty);
	//
	//        }
	//
	//        /// <summary>
	//        /// Unmute an object, resident, etc.
	//        /// </summary>
	//        /// <param name="id">Mute UUID</param>
	//        /// <param name="name">Mute name</param>
	//        public void RemoveMuteListEntry(UUID id, String name)
	//        {
	//            RemoveMuteListEntryPacket p = new RemoveMuteListEntryPacket();
	//            p.AgentData.AgentID = Client.self.getAgentID();
	//            p.AgentData.SessionID = Client.self.getSessionID();
	//
	//            p.MuteData.MuteID = id;
	//            p.MuteData.MuteName = Utils.stringToBytes(name);
	//            
	//            Client.network.SendPacket(p);
	//
	//            String listKey = string.Format("{0}|{1}", id, name);
	//            if (MuteList.containsKey(listKey))
	//            {
	//                synchronized (MuteList.getDictionary())
	//                {
	//                    MuteList.Remove(listKey);
	//                }
	//                OnMuteListUpdated(EventArgs.Empty);
	//            }
	//        }
	//
	//        /// <summary>
	//        /// Sets home location to agents current position
	//        /// </summary>
	//        /// <remarks>will fire an AlertMessage (<seealso cref="E:OpenMetaverse.AgentManager.OnAlertMessage"/>) with 
	//        /// success or failure message</remarks>
	//        public void SetHome()
	//        {
	//            SetStartLocationRequestPacket s = new SetStartLocationRequestPacket();
	//            s.AgentData = new SetStartLocationRequestPacket.AgentDataBlock();
	//            s.AgentData.AgentID = Client.self.getAgentID();
	//            s.AgentData.SessionID = Client.self.getSessionID();
	//            s.StartLocationData = new SetStartLocationRequestPacket.StartLocationDataBlock();
	//            s.StartLocationData.LocationPos = Client.self.getSimPosition();
	//            s.StartLocationData.LocationID = 1;
	//            s.StartLocationData.SimName = Utils.stringToBytes("");
	//            s.StartLocationData.LocationLookAt = Movement.Camera.AtAxis;
	//            Client.network.SendPacket(s);
	//        }
	//
	/// <summary>
	/// Move an agent in to a simulator. This packet is the last packet
	/// needed to complete the transition in to a new simulator
	/// </summary>
	/// <param name="simulator"><seealso cref="T:OpenMetaverse.Simulator"/> Object</param>
	public void CompleteAgentMovement(Simulator simulator)
	{
		CompleteAgentMovementPacket move = new CompleteAgentMovementPacket();

		move.AgentData.AgentID = Client.self.getAgentID();
		move.AgentData.SessionID = Client.self.getSessionID();
		move.AgentData.CircuitCode = Client.network.getCircuitCode();

		Client.network.SendPacket(move, simulator);
	}

	//        /// <summary>
	//        /// Reply to script permissions request
	//        /// </summary>
	//        /// <param name="simulator"><seealso cref="T:OpenMetaverse.Simulator"/> Object</param>
	//        /// <param name="itemID"><seealso cref="UUID"/> of the itemID requesting permissions</param>
	//        /// <param name="taskID"><seealso cref="UUID"/> of the taskID requesting permissions</param>
	//        /// <param name="permissions"><seealso cref="OpenMetaverse.ScriptPermission"/> list of permissions to allow</param>
	//        public void ScriptQuestionReply(Simulator simulator, UUID itemID, UUID taskID, ScriptPermission permissions)
	//        {
	//            ScriptAnswerYesPacket yes = new ScriptAnswerYesPacket();
	//            yes.AgentData.AgentID = Client.self.getAgentID();
	//            yes.AgentData.SessionID = Client.self.getSessionID();
	//            yes.Data.ItemID = itemID;
	//            yes.Data.TaskID = taskID;
	//            yes.Data.Questions = (int)permissions;
	//
	//            Client.network.SendPacket(yes, simulator);
	//        }
	//
	//        /// <summary>
	//        /// Respond to a group invitation by either accepting or denying it
	//        /// </summary>
	//        /// <param name="groupID">UUID of the group (sent in the AgentID field of the invite message)</param>
	//        /// <param name="imSessionID">IM Session ID from the group invitation message</param>
	//        /// <param name="accept">Accept the group invitation or deny it</param>
	//        public void GroupInviteRespond(UUID groupID, UUID imSessionID, boolean accept)
	//        {
	//            InstantMessage(Name, groupID, "", imSessionID,
	//                accept ? InstantMessageDialog.GroupInvitationAccept : InstantMessageDialog.GroupInvitationDecline,
	//                InstantMessageOnline.Offline, Vector3.Zero, UUID.Zero, Utils.EmptyBytes);
	//        }
	//
	//        /// <summary>
	//        /// Requests script detection of objects and avatars
	//        /// </summary>
	//        /// <param name="name">name of the object/avatar to search for</param>
	//        /// <param name="searchID">UUID of the object or avatar to search for</param>
	//        /// <param name="type">Type of search from ScriptSensorTypeFlags</param>
	//        /// <param name="range">range of scan (96 max?)</param>
	//        /// <param name="arc">the arc in radians to search within</param>
	//        /// <param name="requestID">an user generated ID to correlate replies with</param>
	//        /// <param name="sim">Simulator to perform search in</param>
	//        public void RequestScriptSensor(String name, UUID searchID, ScriptSensorTypeFlags type, float range, float arc, UUID requestID, Simulator sim)
	//        {
	//            ScriptSensorRequestPacket request = new ScriptSensorRequestPacket();
	//            request.Requester.Arc = arc;
	//            request.Requester.Range = range;
	//            request.Requester.RegionHandle = sim.Handle;
	//            request.Requester.RequestID = requestID;
	//            request.Requester.SearchDir = Quaternion.Identity; // TODO: this needs to be tested
	//            request.Requester.SearchID = searchID;
	//            request.Requester.SearchName = Utils.stringToBytes(name);
	//            request.Requester.SearchPos = Vector3.Zero;
	//            request.Requester.SearchRegions = 0; // TODO: ?
	//            request.Requester.SourceID = Client.self.getAgentID();
	//            request.Requester.Type = (int)type;
	//
	//            Client.network.SendPacket(request, sim);
	//        }
	//
	//        /// <summary>
	//        /// Create or update profile pick
	//        /// </summary>
	//        /// <param name="pickID">UUID of the pick to update, or random UUID to create a new pick</param>
	//        /// <param name="topPick">Is this a top pick? (typically false)</param>
	//        /// <param name="parcelID">UUID of the parcel (UUID.Zero for the current parcel)</param>
	//        /// <param name="name">Name of the pick</param>
	//        /// <param name="globalPosition">Global position of the pick landmark</param>
	//        /// <param name="textureID">UUID of the image displayed with the pick</param>
	//        /// <param name="description">Long description of the pick</param>
	//        public void PickInfoUpdate(UUID pickID, boolean topPick, UUID parcelID, String name, Vector3d globalPosition, UUID textureID, String description)
	//        {
	//            PickInfoUpdatePacket pick = new PickInfoUpdatePacket();
	//            pick.AgentData.AgentID = Client.self.getAgentID();
	//            pick.AgentData.SessionID = Client.self.getSessionID();
	//            pick.Data.PickID = pickID;
	//            pick.Data.Desc = Utils.stringToBytes(description);
	//            pick.Data.CreatorID = Client.self.getAgentID();
	//            pick.Data.TopPick = topPick;
	//            pick.Data.ParcelID = parcelID;
	//            pick.Data.Name = Utils.stringToBytes(name);
	//            pick.Data.SnapshotID = textureID;
	//            pick.Data.PosGlobal = globalPosition;
	//            pick.Data.SortOrder = 0;
	//            pick.Data.Enabled = false;
	//
	//            Client.network.SendPacket(pick);
	//        }
	//
	//        /// <summary>
	//        /// Delete profile pick
	//        /// </summary>
	//        /// <param name="pickID">UUID of the pick to delete</param>
	//        public void PickDelete(UUID pickID)
	//        {
	//            PickDeletePacket delete = new PickDeletePacket();
	//            delete.AgentData.AgentID = Client.self.getAgentID();
	//            delete.AgentData.SessionID = Client.Self.sessionID;
	//            delete.Data.PickID = pickID;
	//
	//            Client.network.SendPacket(delete);
	//        }
	//
	//        /// <summary>
	//        /// Create or update profile Classified
	//        /// </summary>
	//        /// <param name="classifiedID">UUID of the classified to update, or random UUID to create a new classified</param>
	//        /// <param name="category">Defines what catagory the classified is in</param>
	//        /// <param name="snapshotID">UUID of the image displayed with the classified</param>
	//        /// <param name="price">Price that the classified will cost to place for a week</param>
	//        /// <param name="position">Global position of the classified landmark</param>
	//        /// <param name="name">Name of the classified</param>
	//        /// <param name="desc">Long description of the classified</param>
	//        /// <param name="autoRenew">if true, auto renew classified after expiration</param>
	//        public void UpdateClassifiedInfo(UUID classifiedID, DirectoryManager.ClassifiedCategories category,
	//            UUID snapshotID, int price, Vector3d position, String name, String desc, boolean autoRenew)
	//        {
	//            ClassifiedInfoUpdatePacket classified = new ClassifiedInfoUpdatePacket();
	//            classified.AgentData.AgentID = Client.self.getAgentID();
	//            classified.AgentData.SessionID = Client.self.getSessionID();
	//
	//            classified.Data.ClassifiedID = classifiedID;
	//            classified.Data.Category = (uint)category;
	//
	//            classified.Data.ParcelID = UUID.Zero;
	//            // TODO: verify/fix ^
	//            classified.Data.ParentEstate = 0;
	//            // TODO: verify/fix ^
	//
	//            classified.Data.SnapshotID = snapshotID;
	//            classified.Data.PosGlobal = position;
	//
	//            classified.Data.ClassifiedFlags = autoRenew ? (byte)32 : (byte)0;
	//            // TODO: verify/fix ^
	//
	//            classified.Data.PriceForListing = price;
	//            classified.Data.Name = Utils.stringToBytes(name);
	//            classified.Data.Desc = Utils.stringToBytes(desc);
	//            Client.network.SendPacket(classified);
	//        }
	//
	//        /// <summary>
	//        /// Create or update profile Classified
	//        /// </summary>
	//        /// <param name="classifiedID">UUID of the classified to update, or random UUID to create a new classified</param>
	//        /// <param name="category">Defines what catagory the classified is in</param>
	//        /// <param name="snapshotID">UUID of the image displayed with the classified</param>
	//        /// <param name="price">Price that the classified will cost to place for a week</param>
	//        /// <param name="name">Name of the classified</param>
	//        /// <param name="desc">Long description of the classified</param>
	//        /// <param name="autoRenew">if true, auto renew classified after expiration</param>
	//        public void UpdateClassifiedInfo(UUID classifiedID, DirectoryManager.ClassifiedCategories category, UUID snapshotID, int price, String name, String desc, boolean autoRenew)
	//        {
	//            UpdateClassifiedInfo(classifiedID, category, snapshotID, price, Client.Self.GlobalPosition, name, desc, autoRenew);
	//        }
	//
	//        /// <summary>
	//        /// Delete a classified ad
	//        /// </summary>
	//        /// <param name="classifiedID">The classified ads ID</param>
	//        public void DeleteClassfied(UUID classifiedID)
	//        {
	//            ClassifiedDeletePacket classified = new ClassifiedDeletePacket();
	//            classified.AgentData.AgentID = Client.self.getAgentID();
	//            classified.AgentData.SessionID = Client.self.getSessionID();
	//
	//            classified.Data.ClassifiedID = classifiedID;
	//            Client.network.SendPacket(classified);
	//        }
	//
	//        /// <summary>
	//        /// Fetches resource usage by agents attachmetns
	//        /// </summary>
	//        /// <param name="callback">Called when the requested information is collected</param>
	//        public void GetAttachmentResources(AttachmentResourcesCallback callback)
	//        {
	//            try
	//            {
	//                URI url = Client.network.getCurrentSim().Caps.CapabilityURI("AttachmentResources");
	//                CapsHttpClient request = new CapsHttpClient(url);
	//
	//                request.OnComplete += delegate(CapsHttpClient client, OSD result, Exception error)
	//                {
	//                    try
	//                    {
	//                        if (result == null || error != null)
	//                        {
	//                            callback(false, null);
	//                        }
	//                        AttachmentResourcesMessage info = AttachmentResourcesMessage.FromOSD(result);
	//                        callback(true, info);
	//
	//                    }
	//                    catch (Exception ex)
	//                    {
	//                        Logger.Log("Failed fetching AttachmentResources", Helpers.LogLevel.Error, Client, ex);
	//                        callback(false, null);
	//                    }
	//                };
	//
	//                request.BeginGetResponse(Client.settings.CAPS_TIMEOUT);
	//            }
	//            catch (Exception ex)
	//            {
	//                Logger.Log("Failed fetching AttachmentResources", Helpers.LogLevel.Error, Client, ex);
	//                callback(false, null);
	//            }
	//        }
	//
	//        /// <summary>
	//        /// Initates request to set a new display name
	//        /// </summary>
	//        /// <param name="oldName">Previous display name</param>
	//        /// <param name="newName">Desired new display name</param>
	//        public void SetDisplayName(String oldName, String newName)
	//        {
	//            Uri uri;
	//
	//            if (Client.network.getCurrentSim() == null ||
	//                Client.network.getCurrentSim().Caps == null ||
	//                (uri = Client.network.getCurrentSim().Caps.CapabilityURI("SetDisplayName")) == null)
	//            {
	//                Logger.Log("Unable to invoke SetDisplyName capability at this time", Helpers.LogLevel.Warning, Client);
	//                return;
	//            }
	//
	//            SetDisplayNameMessage msg = new SetDisplayNameMessage();
	//            msg.OldDisplayName = oldName;
	//            msg.NewDisplayName = newName;
	//
	//            CapsHttpClient cap = new CapsHttpClient(uri);
	//            cap.BeginGetResponse(msg.Serialize(), OSDFormat.Xml, Client.settings.CAPS_TIMEOUT);
	//        }
	//
	//        /// <summary>
	//        /// Tells the sim what UI language is used, and if it's ok to share that with scripts
	//        /// </summary>
	//        /// <param name="language">Two letter language code</param>
	//        /// <param name="isPublic">Share language info with scripts</param>
	//        public void UpdateAgentLanguage(String language, boolean isPublic)
	//        {
	//            try
	//            {
	//                UpdateAgentLanguageMessage msg = new UpdateAgentLanguageMessage();
	//                msg.Language = language;
	//                msg.LanguagePublic = isPublic;
	//
	//                URI url = Client.network.getCurrentSim().Caps.CapabilityURI("UpdateAgentLanguage");
	//                CapsHttpClient request = new CapsHttpClient(url);
	//                request.BeginGetResponse(msg.Serialize(), OSDFormat.Xml, Client.settings.CAPS_TIMEOUT);
	//            }
	//            catch (Exception ex)
	//            {
	//                Logger.Log("Failes to update agent language", Helpers.LogLevel.Error, Client, ex);
	//            }
	//        }
	//        //endregion Misc
	//
	//        //region Packet Handlers
	//
	//        /// <summary>
	//        /// Take an incoming ImprovedInstantMessage packet, auto-parse, and if
	//        /// OnInstantMessage is defined call that with the appropriate arguments
	//        /// </summary>
	//        /// <param name="sender">The sender</param>
	//        /// <param name="e">The EventArgs object containing the packet data</param>
	//        protected void InstantMessageHandler(object sender, PacketReceivedEventArgs e)
	//        {
	//            Packet packet = e.Packet;
	//            Simulator simulator = e.Simulator;
	//
	//            if (packet.Type == PacketType.ImprovedInstantMessage)
	//            {
	//                ImprovedInstantMessagePacket im = (ImprovedInstantMessagePacket)packet;
	//
	//                if (m_InstantMessage != null)
	//                {
	//                    InstantMessage message;
	//                    message.FromAgentID = im.AgentData.AgentID;
	//                    message.FromAgentName = Utils.BytesToString(im.MessageBlock.FromAgentName);
	//                    message.ToAgentID = im.MessageBlock.ToAgentID;
	//                    message.ParentEstateID = im.MessageBlock.ParentEstateID;
	//                    message.RegionID = im.MessageBlock.RegionID;
	//                    message.Position = im.MessageBlock.Position;
	//                    message.Dialog = (InstantMessageDialog)im.MessageBlock.Dialog;
	//                    message.GroupIM = im.MessageBlock.FromGroup;
	//                    message.IMSessionID = im.MessageBlock.ID;
	//                    message.Timestamp = new Date(im.MessageBlock.Timestamp);
	//                    message.Message = Utils.BytesToString(im.MessageBlock.Message);
	//                    message.Offline = (InstantMessageOnline)im.MessageBlock.Offline;
	//                    message.BinaryBucket = im.MessageBlock.BinaryBucket;
	//
	//                    OnInstantMessage(new InstantMessageEventArgs(message, simulator));
	//                }
	//            }
	//        }
	//
	//        /// <summary>
	//        /// Take an incoming Chat packet, auto-parse, and if OnChat is defined call 
	//        ///   that with the appropriate arguments.
	//        /// </summary>
	//        /// <param name="sender">The sender</param>
	//        /// <param name="e">The EventArgs object containing the packet data</param>
	//        protected void ChatHandler(object sender, PacketReceivedEventArgs e)
	//        {
	//            if (m_Chat != null)
	//            {
	//                Packet packet = e.Packet;
	//
	//                ChatFromSimulatorPacket chat = (ChatFromSimulatorPacket)packet;
	//
	//                OnChat(new ChatEventArgs(e.Simulator, Utils.BytesToString(chat.ChatData.Message),
	//                    (ChatAudibleLevel)chat.ChatData.Audible,
	//                    (ChatType)chat.ChatData.ChatType,
	//                    (ChatSourceType)chat.ChatData.SourceType,
	//                    Utils.BytesToString(chat.ChatData.FromName),
	//                    chat.ChatData.SourceID,
	//                    chat.ChatData.OwnerID,
	//                    chat.ChatData.Position));
	//            }
	//        }
	//
	//        /// <summary>
	//        /// Used for parsing llDialogs
	//        /// </summary>
	//        /// <param name="sender">The sender</param>
	//        /// <param name="e">The EventArgs object containing the packet data</param>
	//        protected void ScriptDialogHandler(object sender, PacketReceivedEventArgs e)
	//        {
	//            if (m_ScriptDialog != null)
	//            {
	//                Packet packet = e.Packet;
	//
	//                ScriptDialogPacket dialog = (ScriptDialogPacket)packet;
	//                List<String> buttons = new ArrayList<String>();
	//
	//                foreach (ScriptDialogPacket.ButtonsBlock button in dialog.Buttons)
	//                {
	//                    buttons.Add(Utils.BytesToString(button.ButtonLabel));
	//                }
	//
	//                UUID ownerID = UUID.Zero;
	//
	//                if (dialog.OwnerData != null && dialog.OwnerData.Length > 0)
	//                {
	//                    ownerID = dialog.OwnerData[0].OwnerID;
	//                }
	//
	//                OnScriptDialog(new ScriptDialogEventArgs(Utils.BytesToString(dialog.Data.Message),
	//                    Utils.BytesToString(dialog.Data.ObjectName),
	//                    dialog.Data.ImageID,
	//                    dialog.Data.ObjectID,
	//                    Utils.BytesToString(dialog.Data.FirstName),
	//                    Utils.BytesToString(dialog.Data.LastName),
	//                    dialog.Data.ChatChannel,
	//                    buttons,
	//                    ownerID));
	//            }
	//        }
	//
	//        /// <summary>
	//        /// Used for parsing llRequestPermissions dialogs
	//        /// </summary>
	//        /// <param name="sender">The sender</param>
	//        /// <param name="e">The EventArgs object containing the packet data</param>
	//        protected void ScriptQuestionHandler(object sender, PacketReceivedEventArgs e)
	//        {
	//            if (m_ScriptQuestion != null)
	//            {
	//                Packet packet = e.Packet;
	//                Simulator simulator = e.Simulator;
	//
	//                ScriptQuestionPacket question = (ScriptQuestionPacket)packet;
	//
	//                OnScriptQuestion(new ScriptQuestionEventArgs(simulator,
	//                        question.Data.TaskID,
	//                        question.Data.ItemID,
	//                        Utils.BytesToString(question.Data.ObjectName),
	//                        Utils.BytesToString(question.Data.ObjectOwner),
	//                        (ScriptPermission)question.Data.Questions));
	//            }
	//        }
	//
	//        /// <summary>
	//        /// Handles Script Control changes when Script with permissions releases or takes a control
	//        /// </summary>
	//        /// <param name="sender">The sender</param>
	//        /// <param name="e">The EventArgs object containing the packet data</param>
	//        private void ScriptControlChangeHandler(object sender, PacketReceivedEventArgs e)
	//        {
	//            if (m_ScriptControl != null)
	//            {
	//                Packet packet = e.Packet;
	//
	//                ScriptControlChangePacket change = (ScriptControlChangePacket)packet;
	//                for (int i = 0; i < change.Data.Length; i++)
	//                {
	//                    OnScriptControlChange(new ScriptControlEventArgs((ScriptControlChange)change.Data[i].Controls,
	//                            change.Data[i].PassToAgent,
	//                            change.Data[i].TakeControls));
	//                }
	//            }
	//        }
	//
	//        /// <summary>
	//        /// Used for parsing llLoadURL Dialogs
	//        /// </summary>
	//        /// <param name="sender">The sender</param>
	//        /// <param name="e">The EventArgs object containing the packet data</param>
	//        protected void LoadURLHandler(object sender, PacketReceivedEventArgs e)
	//        {
	//
	//            if (m_LoadURL != null)
	//            {
	//                Packet packet = e.Packet;
	//
	//                LoadURLPacket loadURL = (LoadURLPacket)packet;
	//
	//                OnLoadURL(new LoadUrlEventArgs(
	//                    Utils.BytesToString(loadURL.Data.ObjectName),
	//                    loadURL.Data.ObjectID,
	//                    loadURL.Data.OwnerID,
	//                    loadURL.Data.OwnerIsGroup,
	//                    Utils.BytesToString(loadURL.Data.Message),
	//                    Utils.BytesToString(loadURL.Data.URL)
	//                ));
	//            }
	//        }
	//
	//        /// <summary>
	//        /// Update client's Position, LookAt and region handle from incoming packet
	//        /// </summary>
	//        /// <param name="sender">The sender</param>
	//        /// <param name="e">The EventArgs object containing the packet data</param>
	//        /// <remarks>This occurs when after an avatar moves into a new sim</remarks>
	//        private void MovementCompleteHandler(object sender, PacketReceivedEventArgs e)
	//        {
	//            Packet packet = e.Packet;
	//            Simulator simulator = e.Simulator;
	//
	//            AgentMovementCompletePacket movement = (AgentMovementCompletePacket)packet;
	//
	//            relativePosition = movement.Data.Position;
	//            Movement.Camera.LookDirection(movement.Data.LookAt);
	//            simulator.Handle = movement.Data.RegionHandle;
	//            simulator.SimVersion = Utils.BytesToString(movement.SimData.ChannelVersion);
	//            simulator.AgentMovementComplete = true;
	//        }
	//
	//        /// <summary>Process an incoming packet and raise the appropriate events</summary>
	//        /// <param name="sender">The sender</param>
	//        /// <param name="e">The EventArgs object containing the packet data</param>
	//        protected void HealthHandler(object sender, PacketReceivedEventArgs e)
	//        {
	//            Packet packet = e.Packet;
	//            health = ((HealthMessagePacket)packet).HealthData.Health;
	//        }
	//
	//        /// <summary>Process an incoming packet and raise the appropriate events</summary>
	//        /// <param name="sender">The sender</param>
	//        /// <param name="e">The EventArgs object containing the packet data</param>
	//        protected void AgentDataUpdateHandler(object sender, PacketReceivedEventArgs e)
	//        {
	//            Packet packet = e.Packet;
	//            Simulator simulator = e.Simulator;
	//
	//            AgentDataUpdatePacket p = (AgentDataUpdatePacket)packet;
	//
	//            if (p.AgentData.AgentID == simulator.Client.self.getAgentID())
	//            {
	//                firstName = Utils.BytesToString(p.AgentData.FirstName);
	//                lastName = Utils.BytesToString(p.AgentData.LastName);
	//                activeGroup = p.AgentData.ActiveGroupID;
	//                activeGroupPowers = (GroupPowers)p.AgentData.GroupPowers;
	//
	//                if (m_AgentData != null)
	//                {
	//                    String groupTitle = Utils.BytesToString(p.AgentData.GroupTitle);
	//                    String groupName = Utils.BytesToString(p.AgentData.GroupName);
	//
	//                    OnAgentData(new AgentDataReplyEventArgs(firstName, lastName, activeGroup, groupTitle, activeGroupPowers, groupName));
	//                }
	//            }
	//            else
	//            {
	//                Logger.Log("Got an AgentDataUpdate packet for avatar " + p.AgentData.AgentID.ToString() +
	//                    " instead of " + Client.self.getAgentID().ToString() + ", this shouldn't happen", Helpers.LogLevel.Error, Client);
	//            }
	//        }
	//
	//        /// <summary>Process an incoming packet and raise the appropriate events</summary>
	//        /// <param name="sender">The sender</param>
	//        /// <param name="e">The EventArgs object containing the packet data</param>
	//        protected void MoneyBalanceReplyHandler(object sender, PacketReceivedEventArgs e)
	//        {
	//            Packet packet = e.Packet;
	//
	//            if (packet.Type == PacketType.MoneyBalanceReply)
	//            {
	//                MoneyBalanceReplyPacket reply = (MoneyBalanceReplyPacket)packet;
	//                this.balance = reply.MoneyData.MoneyBalance;
	//
	//                if (m_MoneyBalance != null)
	//                {
	//                    TransactionInfo transactionInfo = new TransactionInfo();
	//                    transactionInfo.TransactionType = reply.TransactionInfo.TransactionType;
	//                    transactionInfo.SourceID = reply.TransactionInfo.SourceID;
	//                    transactionInfo.IsSourceGroup = reply.TransactionInfo.IsSourceGroup;
	//                    transactionInfo.DestID = reply.TransactionInfo.DestID;
	//                    transactionInfo.IsDestGroup = reply.TransactionInfo.IsDestGroup;
	//                    transactionInfo.Amount = reply.TransactionInfo.Amount;
	//                    transactionInfo.ItemDescription =  Utils.BytesToString(reply.TransactionInfo.ItemDescription);
	//
	//                    OnMoneyBalanceReply(new MoneyBalanceReplyEventArgs(reply.MoneyData.TransactionID,
	//                        reply.MoneyData.TransactionSuccess,
	//                        reply.MoneyData.MoneyBalance,
	//                        reply.MoneyData.SquareMetersCredit,
	//                        reply.MoneyData.SquareMetersCommitted,
	//                        Utils.BytesToString(reply.MoneyData.Description),
	//                        transactionInfo));
	//                }
	//            }
	//
	//            if (m_Balance != null)
	//            {
	//                OnBalance(new BalanceEventArgs(balance));
	//            }
	//        }
	//
	//        /// <summary>
	//        /// EQ Message fired with the result of SetDisplayName request
	//        /// </summary>
	//        /// <param name="capsKey">The message key</param>
	//        /// <param name="message">the IMessage object containing the deserialized data sent from the simulator</param>
	//        /// <param name="simulator">The <see cref="Simulator"/> which originated the packet</param>
	//        protected void SetDisplayNameReplyEventHandler(String capsKey, IMessage message, Simulator simulator)
	//        {
	//            if (m_SetDisplayNameReply != null)
	//            {
	//                SetDisplayNameReplyMessage msg = (SetDisplayNameReplyMessage)message;
	//                OnSetDisplayNameReply(new SetDisplayNameReplyEventArgs(msg.Status, msg.Reason, msg.DisplayName));
	//            }
	//        }
	//
	//        protected void EstablishAgentCommunicationEventHandler(String capsKey, IMessage message, Simulator simulator)
	//        {
	//            EstablishAgentCommunicationMessage msg = (EstablishAgentCommunicationMessage)message;
	//
	//            if (Client.settings.MULTIPLE_SIMS)
	//            {
	//
	//                IPEndPoint endPoint = new IPEndPoint(msg.Address, msg.Port);
	//                Simulator sim = Client.network.FindSimulator(endPoint);
	//
	//                if (sim == null)
	//                {
	//                    Logger.Log("Got EstablishAgentCommunication for unknown sim " + msg.Address + ":" + msg.Port,
	//                        Helpers.LogLevel.Error, Client);
	//
	//                    // FIXME: Should we use this opportunity to connect to the simulator?
	//                }
	//                else
	//                {
	//                    Logger.Log("Got EstablishAgentCommunication for " + sim.ToString(),
	//                        Helpers.LogLevel.Info, Client);
	//
	//                    sim.SetSeedCaps(msg.SeedCapability.ToString());
	//                }
	//            }
	//        }
	//
	//        /// <summary>
	//        /// Process TeleportFailed message sent via EventQueue, informs agent its last teleport has failed and why.
	//        /// </summary>
	//        /// <param name="messageKey">The Message Key</param>
	//        /// <param name="message">An IMessage object Deserialized from the recieved message event</param>
	//        /// <param name="simulator">The simulator originating the event message</param>
	//        public void TeleportFailedEventHandler(String messageKey, IMessage message, Simulator simulator)
	//        {
	//            TeleportFailedMessage msg = (TeleportFailedMessage)message;
	//
	//            TeleportFailedPacket failedPacket = new TeleportFailedPacket();
	//            failedPacket.Info.AgentID = msg.AgentID;
	//            failedPacket.Info.Reason = Utils.stringToBytes(msg.Reason);
	//
	//            TeleportHandler(this, new PacketReceivedEventArgs(failedPacket, simulator));
	//        }
	//
	//        /// <summary>
	//        /// Process TeleportFinish from Event Queue and pass it onto our TeleportHandler
	//        /// </summary>
	//        /// <param name="capsKey">The message system key for this event</param>
	//        /// <param name="message">IMessage object containing decoded data from OSD</param>
	//        /// <param name="simulator">The simulator originating the event message</param>
	//        private void TeleportFinishEventHandler(String capsKey, IMessage message, Simulator simulator)
	//        {
	//            TeleportFinishMessage msg = (TeleportFinishMessage)message;
	//
	//            TeleportFinishPacket p = new TeleportFinishPacket();
	//            p.Info.AgentID = msg.AgentID;
	//            p.Info.LocationID = (uint)msg.LocationID;
	//            p.Info.RegionHandle = msg.RegionHandle;
	//            p.Info.SeedCapability = Utils.stringToBytes(msg.SeedCapability.ToString()); // FIXME: Check This
	//            p.Info.SimAccess = (byte)msg.SimAccess;
	//            p.Info.SimIP = Utils.IPToUInt(msg.IP);
	//            p.Info.SimPort = (ushort)msg.Port;
	//            p.Info.TeleportFlags = (uint)msg.Flags;
	//
	//            // pass the packet onto the teleport handler
	//            TeleportHandler(this, new PacketReceivedEventArgs(p, simulator));
	//        }
	//
	//        /// <summary>Process an incoming packet and raise the appropriate events</summary>
	//        /// <param name="sender">The sender</param>
	//        /// <param name="e">The EventArgs object containing the packet data</param>
	//        protected void TeleportHandler(object sender, PacketReceivedEventArgs e)
	//        {
	//            Packet packet = e.Packet;
	//            Simulator simulator = e.Simulator;
	//
	//            boolean finished = false;
	//            TeleportFlags flags = TeleportFlags.Default;
	//
	//            if (packet.Type == PacketType.TeleportStart)
	//            {
	//                TeleportStartPacket start = (TeleportStartPacket)packet;
	//
	//                teleportMessage = "Teleport started";
	//                flags = (TeleportFlags)start.Info.TeleportFlags;
	//                teleportStat = TeleportStatus.Start;
	//
	//                Logger.DebugLog("TeleportStart received, Flags: " + flags.ToString(), Client);
	//            }
	//            else if (packet.Type == PacketType.TeleportProgress)
	//            {
	//                TeleportProgressPacket progress = (TeleportProgressPacket)packet;
	//
	//                teleportMessage = Utils.BytesToString(progress.Info.Message);
	//                flags = (TeleportFlags)progress.Info.TeleportFlags;
	//                teleportStat = TeleportStatus.Progress;
	//
	//                Logger.DebugLog("TeleportProgress received, Message: " + teleportMessage + ", Flags: " + flags.ToString(), Client);
	//            }
	//            else if (packet.Type == PacketType.TeleportFailed)
	//            {
	//                TeleportFailedPacket failed = (TeleportFailedPacket)packet;
	//
	//                teleportMessage = Utils.BytesToString(failed.Info.Reason);
	//                teleportStat = TeleportStatus.Failed;
	//                finished = true;
	//
	//                Logger.DebugLog("TeleportFailed received, Reason: " + teleportMessage, Client);
	//            }
	//            else if (packet.Type == PacketType.TeleportFinish)
	//            {
	//                TeleportFinishPacket finish = (TeleportFinishPacket)packet;
	//
	//                flags = (TeleportFlags)finish.Info.TeleportFlags;
	//                String seedcaps = Utils.BytesToString(finish.Info.SeedCapability);
	//                finished = true;
	//
	//                Logger.DebugLog("TeleportFinish received, Flags: " + flags.ToString(), Client);
	//
	//                // Connect to the new sim
	//                Client.network.getCurrentSim().AgentMovementComplete = false; // we're not there anymore
	//                Simulator newSimulator = Client.network.Connect(new IPAddress(finish.Info.SimIP),
	//                    finish.Info.SimPort, finish.Info.RegionHandle, true, seedcaps);
	//
	//                if (newSimulator != null)
	//                {
	//                    teleportMessage = "Teleport finished";
	//                    teleportStat = TeleportStatus.Finished;
	//
	//                    Logger.Log("Moved to new sim " + newSimulator.ToString(), Helpers.LogLevel.Info, Client);
	//                }
	//                else
	//                {
	//                    teleportMessage = "Failed to connect to the new sim after a teleport";
	//                    teleportStat = TeleportStatus.Failed;
	//
	//                    // We're going to get disconnected now
	//                    Logger.Log(teleportMessage, Helpers.LogLevel.Error, Client);
	//                }
	//            }
	//            else if (packet.Type == PacketType.TeleportCancel)
	//            {
	//                //TeleportCancelPacket cancel = (TeleportCancelPacket)packet;
	//
	//                teleportMessage = "Cancelled";
	//                teleportStat = TeleportStatus.Cancelled;
	//                finished = true;
	//
	//                Logger.DebugLog("TeleportCancel received from " + simulator.ToString(), Client);
	//            }
	//            else if (packet.Type == PacketType.TeleportLocal)
	//            {
	//                TeleportLocalPacket local = (TeleportLocalPacket)packet;
	//
	//                teleportMessage = "Teleport finished";
	//                flags = (TeleportFlags)local.Info.TeleportFlags;
	//                teleportStat = TeleportStatus.Finished;
	//                relativePosition = local.Info.Position;
	//                Movement.Camera.LookDirection(local.Info.LookAt);
	//                // This field is apparently not used for anything
	//                //local.Info.LocationID;
	//                finished = true;
	//
	//                Logger.DebugLog("TeleportLocal received, Flags: " + flags.ToString(), Client);
	//            }
	//
	//            if (m_Teleport != null)
	//            {
	//                OnTeleport(new TeleportEventArgs(teleportMessage, teleportStat, flags));
	//            }
	//
	//            if (finished) teleportEvent.Set();
	//        }
	//
	//        /// <summary>Process an incoming packet and raise the appropriate events</summary>
	//        /// <param name="sender">The sender</param>
	//        /// <param name="e">The EventArgs object containing the packet data</param>
	//        protected void AvatarAnimationHandler(object sender, PacketReceivedEventArgs e)
	//        {
	//            Packet packet = e.Packet;
	//            AvatarAnimationPacket animation = (AvatarAnimationPacket)packet;
	//
	//            if (animation.Sender.ID == Client.self.getAgentID())
	//            {
	//                synchronized (SignaledAnimations.getDictionary())
	//                {
	//                    // Reset the signaled animation list
	//                    SignaledAnimations.getDictionary().Clear();
	//
	//                    for (int i = 0; i < animation.AnimationList.Length; i++)
	//                    {
	//                        UUID animID = animation.AnimationList[i].AnimID;
	//                        int sequenceID = animation.AnimationList[i].AnimSequenceID;
	//
	//                        // Add this animation to the list of currently signaled animations
	//                        SignaledAnimations.getDictionary()[animID] = sequenceID;
	//
	//                        if (i < animation.AnimationSourceList.Length)
	//                        {
	//                            // FIXME: The server tells us which objects triggered our animations,
	//                            // we should store this info
	//
	//                            //animation.AnimationSourceList[i].ObjectID
	//                        }
	//
	//                        if (i < animation.PhysicalAvatarEventList.Length)
	//                        {
	//                            // FIXME: What is this?
	//                        }
	//
	//                        if (Client.settings.SEND_AGENT_UPDATES)
	//                        {
	//                            // We have to manually tell the server to stop playing some animations
	//                            if (animID == Animations.STANDUP ||
	//                                animID == Animations.PRE_JUMP ||
	//                                animID == Animations.LAND ||
	//                                animID == Animations.MEDIUM_LAND)
	//                            {
	//                                Movement.setFinishAnim(true);
	//                                Movement.SendUpdate(true);
	//                                Movement.setFinishAnim(false);
	//                            }
	//                        }
	//                    }
	//                }
	//            }
	//
	//            if (m_AnimationsChanged != null)
	//            {
	//                ThreadPool.QueueUserWorkItem(delegate(object o)
	//                { OnAnimationsChanged(new AnimationsChangedEventArgs(this.SignaledAnimations)); });
	//            }
	//
	//        }
	//
	//        /// <summary>Process an incoming packet and raise the appropriate events</summary>
	//        /// <param name="sender">The sender</param>
	//        /// <param name="e">The EventArgs object containing the packet data</param>
	//        protected void MeanCollisionAlertHandler(object sender, PacketReceivedEventArgs e)
	//        {
	//            if (m_MeanCollision != null)
	//            {
	//                Packet packet = e.Packet;
	//                MeanCollisionAlertPacket collision = (MeanCollisionAlertPacket)packet;
	//
	//                for (int i = 0; i < collision.MeanCollision.Length; i++)
	//                {
	//                    MeanCollisionAlertPacket.MeanCollisionBlock block = collision.MeanCollision[i];
	//
	//                    Date time = Utils.UnixTimeToDateTime(block.Time);
	//                    MeanCollisionType type = (MeanCollisionType)block.Type;
	//
	//                    OnMeanCollision(new MeanCollisionEventArgs(type, block.Perp, block.Victim, block.Mag, time));
	//                }
	//            }
	//        }
	//
	        private void Network_OnLoginResponse(boolean loginSuccess, boolean redirect, String message, String reason,
	            LoginResponseData reply)
	        {
	            id = reply.AgentID;
	            sessionID = reply.SessionID;
	            secureSessionID = reply.SecureSessionID;
	            firstName = reply.FirstName;
	            lastName = reply.LastName;
	            startLocation = reply.StartLocation;
	            agentAccess = reply.AgentAccess;
	            Movement.Camera.LookDirection(reply.LookAt);
	            homePosition = reply.HomePosition;
	            homeLookAt = reply.HomeLookAt;
	        }
	
	        private void Network_OnDisconnected(Object sender, DisconnectedEventArgs e)
	        {
	            // Null out the cached fullName since it can change after logging
	            // in again (with a different account name or different login
	            // server but using the same GridClient object
	            fullName = null;
	        }
	//
	//        /// <summary>
	//        /// Crossed region handler for message that comes across the EventQueue. Sent to an agent
	//        /// when the agent crosses a sim border into a new region.
	//        /// </summary>
	//        /// <param name="capsKey">The message key</param>
	//        /// <param name="message">the IMessage object containing the deserialized data sent from the simulator</param>
	//        /// <param name="simulator">The <see cref="Simulator"/> which originated the packet</param>
	//        private void CrossedRegionEventHandler(String capsKey, IMessage message, Simulator simulator)
	//        {
	//            CrossedRegionMessage crossed = (CrossedRegionMessage)message;
	//
	//            IPEndPoint endPoint = new IPEndPoint(crossed.IP, crossed.Port);
	//
	//            Logger.DebugLog("Crossed in to new region area, attempting to connect to " + endPoint.ToString(), Client);
	//
	//            Simulator oldSim = Client.network.getCurrentSim();
	//            Simulator newSim = Client.network.Connect(endPoint, crossed.RegionHandle, true, crossed.SeedCapability.ToString());
	//
	//            if (newSim != null)
	//            {
	//                Logger.Log("Finished crossing over in to region " + newSim.ToString(), Helpers.LogLevel.Info, Client);
	//                oldSim.AgentMovementComplete = false; // We're no longer there
	//                if (m_RegionCrossed != null)
	//                {
	//                    OnRegionCrossed(new RegionCrossedEventArgs(oldSim, newSim));
	//                }
	//            }
	//            else
	//            {
	//                // The old simulator will (poorly) handle our movement still, so the connection isn't
	//                // completely shot yet
	//                Logger.Log("Failed to connect to new region " + endPoint.ToString() + " after crossing over",
	//                    Helpers.LogLevel.Warning, Client);
	//            }
	//        }
	//
	//        /// <summary>Process an incoming packet and raise the appropriate events</summary>
	//        /// <param name="sender">The sender</param>
	//        /// <param name="e">The EventArgs object containing the packet data</param>
	//        /// <remarks>This packet is now being sent via the EventQueue</remarks>
	//        protected void CrossedRegionHandler(object sender, PacketReceivedEventArgs e)
	//        {
	//            Packet packet = e.Packet;
	//            CrossedRegionPacket crossing = (CrossedRegionPacket)packet;
	//            String seedCap = Utils.BytesToString(crossing.RegionData.SeedCapability);
	//            IPEndPoint endPoint = new IPEndPoint(crossing.RegionData.SimIP, crossing.RegionData.SimPort);
	//
	//            Logger.DebugLog("Crossed in to new region area, attempting to connect to " + endPoint.ToString(), Client);
	//
	//            Simulator oldSim = Client.network.getCurrentSim();
	//            Simulator newSim = Client.network.Connect(endPoint, crossing.RegionData.RegionHandle, true, seedCap);
	//
	//            if (newSim != null)
	//            {
	//                Logger.Log("Finished crossing over in to region " + newSim.ToString(), Helpers.LogLevel.Info, Client);
	//
	//                if (m_RegionCrossed != null)
	//                {
	//                    OnRegionCrossed(new RegionCrossedEventArgs(oldSim, newSim));
	//                }
	//            }
	//            else
	//            {
	//                // The old simulator will (poorly) handle our movement still, so the connection isn't
	//                // completely shot yet
	//                Logger.Log("Failed to connect to new region " + endPoint.ToString() + " after crossing over",
	//                    Helpers.LogLevel.Warning, Client);
	//            }
	//        }
	//
	//        /// <summary>
	//        /// Group Chat event handler
	//        /// </summary>
	//        /// <param name="capsKey">The capability Key</param>
	//        /// <param name="message">IMessage object containing decoded data from OSD</param>
	//        /// <param name="simulator"></param>
	//        protected void ChatterBoxSessionEventReplyEventHandler(String capsKey, IMessage message, Simulator simulator)
	//        {
	//            ChatterboxSessionEventReplyMessage msg = (ChatterboxSessionEventReplyMessage)message;
	//
	//            if (!msg.Success)
	//            {
	//                RequestJoinGroupChat(msg.SessionID);
	//                Logger.Log("Attempt to send group chat to non-existant session for group " + msg.SessionID,
	//                    Helpers.LogLevel.Info, Client);
	//            }
	//        }
	//
	//        /// <summary>
	//        /// Response from request to join a group chat
	//        /// </summary>
	//        /// <param name="capsKey"></param>
	//        /// <param name="message">IMessage object containing decoded data from OSD</param>
	//        /// <param name="simulator"></param>
	//        protected void ChatterBoxSessionStartReplyEventHandler(String capsKey, IMessage message, Simulator simulator)
	//        {
	//            ChatterBoxSessionStartReplyMessage msg = (ChatterBoxSessionStartReplyMessage)message;
	//
	//            if (msg.Success)
	//            {
	//                synchronized (GroupChatSessions.getDictionary())
	//                    if (!GroupChatSessions.containsKey(msg.SessionID))
	//                        GroupChatSessions.Add(msg.SessionID, new ArrayList<ChatSessionMember>());
	//            }
	//
	//            OnGroupChatJoined(new GroupChatJoinedEventArgs(msg.SessionID, msg.SessionName, msg.TempSessionID, msg.Success));
	//        }
	//
	//        /// <summary>
	//        /// Someone joined or left group chat
	//        /// </summary>
	//        /// <param name="capsKey"></param>
	//        /// <param name="message">IMessage object containing decoded data from OSD</param>
	//        /// <param name="simulator"></param>
	//        private void ChatterBoxSessionAgentListUpdatesEventHandler(String capsKey, IMessage message, Simulator simulator)
	//        {
	//            ChatterBoxSessionAgentListUpdatesMessage msg = (ChatterBoxSessionAgentListUpdatesMessage)message;
	//
	//            synchronized (GroupChatSessions.getDictionary())
	//                if (!GroupChatSessions.containsKey(msg.SessionID))
	//                    GroupChatSessions.Add(msg.SessionID, new ArrayList<ChatSessionMember>());
	//
	//            for (int i = 0; i < msg.Updates.Length; i++)
	//            {
	//                ChatSessionMember fndMbr;
	//                synchronized (GroupChatSessions.getDictionary())
	//                {
	//                    fndMbr = GroupChatSessions[msg.SessionID].Find(delegate(ChatSessionMember member)
	//                    {
	//                        return member.AvatarKey == msg.Updates[i].AgentID;
	//                    });
	//                }
	//
	//                if (msg.Updates[i].Transition != null)
	//                {
	//                    if (msg.Updates[i].Transition.Equals("ENTER"))
	//                    {
	//                        if (fndMbr.AvatarKey == UUID.Zero)
	//                        {
	//                            fndMbr = new ChatSessionMember();
	//                            fndMbr.AvatarKey = msg.Updates[i].AgentID;
	//
	//                            synchronized (GroupChatSessions.getDictionary())
	//                                GroupChatSessions[msg.SessionID].Add(fndMbr);
	//
	//                            if (m_ChatSessionMemberAdded != null)
	//                            {
	//                                OnChatSessionMemberAdded(new ChatSessionMemberAddedEventArgs(msg.SessionID, fndMbr.AvatarKey));
	//                            }
	//                        }
	//                    }
	//                    else if (msg.Updates[i].Transition.Equals("LEAVE"))
	//                    {
	//                        if (fndMbr.AvatarKey != UUID.Zero)
	//                            synchronized (GroupChatSessions.getDictionary())
	//                                GroupChatSessions[msg.SessionID].Remove(fndMbr);
	//
	//                        if (m_ChatSessionMemberLeft != null)
	//                        {
	//                            OnChatSessionMemberLeft(new ChatSessionMemberLeftEventArgs(msg.SessionID, msg.Updates[i].AgentID));
	//                        }
	//                    }
	//                }
	//
	//                // handle updates
	//                ChatSessionMember update_member = GroupChatSessions.getDictionary()[msg.SessionID].Find(delegate(ChatSessionMember m)
	//                {
	//                    return m.AvatarKey == msg.Updates[i].AgentID;
	//                });
	//
	//
	//                update_member.MuteText = msg.Updates[i].MuteText;
	//                update_member.MuteVoice = msg.Updates[i].MuteVoice;
	//
	//                update_member.CanVoiceChat = msg.Updates[i].CanVoiceChat;
	//                update_member.IsModerator = msg.Updates[i].IsModerator;
	//
	//                // replace existing member record
	//                synchronized (GroupChatSessions.getDictionary())
	//                {
	//                    int found = GroupChatSessions.getDictionary()[msg.SessionID].FindIndex(delegate(ChatSessionMember m)
	//                    {
	//                        return m.AvatarKey == msg.Updates[i].AgentID;
	//                    });
	//
	//                    if (found >= 0)
	//                        GroupChatSessions.getDictionary()[msg.SessionID][found] = update_member;
	//                }
	//            }
	//        }
	//
	//        /// <summary>
	//        /// Handle a group chat Invitation
	//        /// </summary>
	//        /// <param name="capsKey">Caps Key</param>
	//        /// <param name="message">IMessage object containing decoded data from OSD</param>
	//        /// <param name="simulator">Originating Simulator</param>
	//        private void ChatterBoxInvitationEventHandler(String capsKey, IMessage message, Simulator simulator)
	//        {
	//            if (m_InstantMessage != null)
	//            {
	//                ChatterBoxInvitationMessage msg = (ChatterBoxInvitationMessage)message;
	//
	//                //TODO: do something about invitations to voice group chat/friends conference
	//                //Skip for now
	//                if (msg.Voice) return;
	//
	//                InstantMessage im = new InstantMessage();
	//
	//                im.FromAgentID = msg.FromAgentID;
	//                im.FromAgentName = msg.FromAgentName;
	//                im.ToAgentID = msg.ToAgentID;
	//                im.ParentEstateID = (uint)msg.ParentEstateID;
	//                im.RegionID = msg.RegionID;
	//                im.Position = msg.Position;
	//                im.Dialog = msg.Dialog;
	//                im.GroupIM = msg.GroupIM;
	//                im.IMSessionID = msg.IMSessionID;
	//                im.Timestamp = msg.Timestamp;
	//                im.Message = msg.Message;
	//                im.Offline = msg.Offline;
	//                im.BinaryBucket = msg.BinaryBucket;
	//                try
	//                {
	//                    ChatterBoxAcceptInvite(msg.IMSessionID);
	//                }
	//                catch (Exception ex)
	//                {
	//                    Logger.Log("Failed joining IM:", Helpers.LogLevel.Warning, Client, ex);
	//                }
	//                OnInstantMessage(new InstantMessageEventArgs(im, simulator));
	//            }
	//        }
	//
	//
	//        /// <summary>
	//        /// Moderate a chat session
	//        /// </summary>
	//        /// <param name="sessionID">the <see cref="UUID"/> of the session to moderate, for group chats this will be the groups UUID</param>
	//        /// <param name="memberID">the <see cref="UUID"/> of the avatar to moderate</param>
	//        /// <param name="key">Either "voice" to moderate users voice, or "text" to moderate users text session</param>
	//        /// <param name="moderate">true to moderate (silence user), false to allow avatar to speak</param>
	//        public void ModerateChatSessions(UUID sessionID, UUID memberID, String key, boolean moderate)
	//        {
	//            if (Client.network.getCurrentSim() == null || Client.network.getCurrentSim().Caps == null)
	//                throw new Exception("ChatSessionRequest capability is not currently available");
	//
	//            URI url = Client.network.getCurrentSim().Caps.CapabilityURI("ChatSessionRequest");
	//
	//            if (url != null)
	//            {
	//                ChatSessionRequestMuteUpdate req = new ChatSessionRequestMuteUpdate();
	//
	//                req.RequestKey = key;
	//                req.RequestValue = moderate;
	//                req.SessionID = sessionID;
	//                req.AgentID = memberID;
	//
	//                CapsHttpClient request = new CapsHttpClient(url);
	//                request.BeginGetResponse(req.Serialize(), OSDFormat.Xml, Client.settings.CAPS_TIMEOUT);
	//            }
	//            else
	//            {
	//                throw new Exception("ChatSessionRequest capability is not currently available");
	//            }
	//        }
	//
	//        /// <summary>Process an incoming packet and raise the appropriate events</summary>
	//        /// <param name="sender">The sender</param>
	//        /// <param name="e">The EventArgs object containing the packet data</param>
	//        protected void AlertMessageHandler(object sender, PacketReceivedEventArgs e)
	//        {
	//            if (m_AlertMessage != null)
	//            {
	//                Packet packet = e.Packet;
	//
	//                AlertMessagePacket alert = (AlertMessagePacket)packet;
	//
	//                OnAlertMessage(new AlertMessageEventArgs(Utils.BytesToString(alert.AlertData.Message)));
	//            }
	//        }
	//
	//        /// <summary>Process an incoming packet and raise the appropriate events</summary>
	//        /// <param name="sender">The sender</param>
	//        /// <param name="e">The EventArgs object containing the packet data</param>
	//        protected void CameraConstraintHandler(object sender, PacketReceivedEventArgs e)
	//        {
	//            if (m_CameraConstraint != null)
	//            {
	//                Packet packet = e.Packet;
	//
	//                CameraConstraintPacket camera = (CameraConstraintPacket)packet;
	//                OnCameraConstraint(new CameraConstraintEventArgs(camera.CameraCollidePlane.Plane));
	//            }
	//        }
	//
	//        /// <summary>Process an incoming packet and raise the appropriate events</summary>
	//        /// <param name="sender">The sender</param>
	//        /// <param name="e">The EventArgs object containing the packet data</param>
	//        protected void ScriptSensorReplyHandler(object sender, PacketReceivedEventArgs e)
	//        {
	//            if (m_ScriptSensorReply != null)
	//            {
	//                Packet packet = e.Packet;
	//
	//                ScriptSensorReplyPacket reply = (ScriptSensorReplyPacket)packet;
	//
	//                for (int i = 0; i < reply.SensedData.Length; i++)
	//                {
	//                    ScriptSensorReplyPacket.SensedDataBlock block = reply.SensedData[i];
	//                    ScriptSensorReplyPacket.RequesterBlock requestor = reply.Requester;
	//
	//                    OnScriptSensorReply(new ScriptSensorReplyEventArgs(requestor.SourceID, block.GroupID, Utils.BytesToString(block.Name),
	//                      block.ObjectID, block.OwnerID, block.Position, block.Range, block.Rotation, (ScriptSensorTypeFlags)block.Type, block.Velocity));
	//                }
	//            }
	//
	//        }
	//
	//        /// <summary>Process an incoming packet and raise the appropriate events</summary>
	//        /// <param name="sender">The sender</param>
	//        /// <param name="e">The EventArgs object containing the packet data</param>
	//        protected void AvatarSitResponseHandler(object sender, PacketReceivedEventArgs e)
	//        {
	//            if (m_AvatarSitResponse != null)
	//            {
	//                Packet packet = e.Packet;
	//
	//                AvatarSitResponsePacket sit = (AvatarSitResponsePacket)packet;
	//
	//                OnAvatarSitResponse(new AvatarSitResponseEventArgs(sit.SitObject.ID, sit.SitTransform.AutoPilot, sit.SitTransform.CameraAtOffset,
	//                  sit.SitTransform.CameraEyeOffset, sit.SitTransform.ForceMouselook, sit.SitTransform.SitPosition,
	//                  sit.SitTransform.SitRotation));
	//            }
	//        }
	//
	//        protected void MuteListUpdateHander(object sender, PacketReceivedEventArgs e)
	//        {
	//            MuteListUpdatePacket packet = (MuteListUpdatePacket)e.Packet;
	//            if (packet.MuteData.AgentID != Client.self.getAgentID())
	//            {
	//                return;
	//            }
	//
	//            ThreadPool.QueueUserWorkItem(sync =>
	//            {
	//                using (AutoResetEvent gotMuteList = new AutoResetEvent(false))
	//                {
	//                    String fileName = Utils.BytesToString(packet.MuteData.Filename);
	//                    String muteList = string.Empty;
	//                    ulong xferID = 0;
	//                    byte[] assetData = null;
	//
	//                    EventHandler<XferReceivedEventArgs> xferCallback = (object xsender, XferReceivedEventArgs xe) =>
	//                    {
	//                        if (xe.Xfer.XferID == xferID)
	//                        {
	//                            assetData = xe.Xfer.AssetData;
	//                            gotMuteList.Set();
	//                        }
	//                    };
	//
	//
	//                    Client.Assets.XferReceived += xferCallback;
	//                    xferID = Client.Assets.RequestAssetXfer(fileName, true, false, UUID.Zero, AssetType.Unknown, true);
	//
	//                    if (gotMuteList.WaitOne(60 * 1000, false))
	//                    {
	//                        muteList = Utils.BytesToString(assetData);
	//
	//                        synchronized (MuteList.getDictionary())
	//                        {
	//                            MuteList.getDictionary().Clear();
	//                            foreach (var line in muteList.Split('\n'))
	//                            {
	//                                if (line.Trim() == string.Empty) continue;
	//
	//                                try
	//                                {
	//                                    Match m;
	//                                    if ((m = Regex.Match(line, @"(?<MyteType>\d+)\s+(?<Key>[a-zA-Z0-9-]+)\s+(?<Name>[^|]+)|(?<Flags>.+)", RegexOptions.CultureInvariant)).Success)
	//                                    {
	//                                        MuteEntry me = new MuteEntry();
	//                                        me.Type = (MuteType)int.Parse(m.Groups["MyteType"].Value);
	//                                        me.ID = new UUID(m.Groups["Key"].Value);
	//                                        me.Name = m.Groups["Name"].Value;
	//                                        int flags = 0;
	//                                        int.TryParse(m.Groups["Flags"].Value, out flags);
	//                                        me.Flags = (MuteFlags)flags;
	//                                        MuteList[string.Format("{0}|{1}", me.ID, me.Name)] = me;
	//                                    }
	//                                    else
	//                                    {
	//                                        throw new ArgumentException("Invalid mutelist entry line");
	//                                    }
	//                                }
	//                                catch (Exception ex)
	//                                {
	//                                    Logger.Log("Failed to parse the mute list line: " + line, Helpers.LogLevel.Warning, Client, ex);
	//                                }
	//                            }
	//                        }
	//
	//                        OnMuteListUpdated(EventArgs.Empty);
	//                    }
	//                    else
	//                    {
	//                        Logger.Log("Timed out waiting for mute list download", Helpers.LogLevel.Warning, Client);
	//                    }
	//
	//                    Client.Assets.XferReceived -= xferCallback;
	//
	//                }
	//            });
	//        }
	//
	//        //endregion Packet Handlers
	//    }
	//
	//region Event Argument Classes

	/// <summary>
	/// 
	/// </summary>
	public class ChatEventArgs extends EventArgs
	{
		private  Simulator m_Simulator;
		private  String m_Message;
		private  ChatAudibleLevel m_AudibleLevel;
		private  ChatType m_Type;
		private  ChatSourceType m_SourceType;
		private  String m_FromName;
		private  UUID m_SourceID;
		private  UUID m_OwnerID;
		private  Vector3 m_Position;

		/// <summary>Get the simulator sending the message</summary>
		public Simulator getSimulator() {  return m_Simulator; } 
		/// <summary>Get the message sent</summary>
		public String getMessage() { return m_Message; } 
		/// <summary>Get the audible level of the message</summary>
		public ChatAudibleLevel getAudibleLevel() {  return m_AudibleLevel; } 
		/// <summary>Get the type of message sent: whisper, shout, etc</summary>
		public ChatType getType() { return m_Type; } 
		/// <summary>Get the source type of the message sender</summary>
		public ChatSourceType getSourceType() { return m_SourceType; } 
		/// <summary>Get the name of the agent or object sending the message</summary>
		public String getFromName() { return m_FromName; } 
		/// <summary>Get the ID of the agent or object sending the message</summary>
		public UUID getSourceID() { return m_SourceID; } 
		/// <summary>Get the ID of the object owner, or the agent ID sending the message</summary>
		public UUID getOwnerID() { return m_OwnerID; } 
		/// <summary>Get the position of the agent or object sending the message</summary>
		public Vector3 getPosition() { return m_Position; } 

		/// <summary>
		/// Construct a new instance of the ChatEventArgs object
		/// </summary>
		/// <param name="simulator">Sim from which the message originates</param>
		/// <param name="message">The message sent</param>
		/// <param name="audible">The audible level of the message</param>
		/// <param name="type">The type of message sent: whisper, shout, etc</param>
		/// <param name="sourceType">The source type of the message sender</param>
		/// <param name="fromName">The name of the agent or object sending the message</param>
		/// <param name="sourceId">The ID of the agent or object sending the message</param>
		/// <param name="ownerid">The ID of the object owner, or the agent ID sending the message</param>
		/// <param name="position">The position of the agent or object sending the message</param>
		public ChatEventArgs(Simulator simulator, String message, ChatAudibleLevel audible, ChatType type,
				ChatSourceType sourceType, String fromName, UUID sourceId, UUID ownerid, Vector3 position)
		{
			this.m_Simulator = simulator;
			this.m_Message = message;
			this.m_AudibleLevel = audible;
			this.m_Type = type;
			this.m_SourceType = sourceType;
			this.m_FromName = fromName;
			this.m_SourceID = sourceId;
			this.m_Position = position;
			this.m_OwnerID = ownerid;
		}
	}

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

	/// <summary>Contains the data sent when a primitive requests debit or other permissions
	/// requesting a YES or NO answer</summary>
	public class ScriptQuestionEventArgs extends EventArgs
	{
		private  Simulator m_Simulator;
		private  UUID m_TaskID;
		private  UUID m_ItemID;
		private  String m_ObjectName;
		private  String m_ObjectOwnerName;
		private  ScriptPermission m_Questions;

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
		public ScriptPermission getQuestions() { return m_Questions; } 

		/// <summary>
		/// Construct a new instance of the ScriptQuestionEventArgs
		/// </summary>
		/// <param name="simulator">The simulator containing the object sending the request</param>
		/// <param name="taskID">The ID of the script making the request</param>
		/// <param name="itemID">The ID of the primitive containing the script making the request</param>
		/// <param name="objectName">The name of the primitive making the request</param>
		/// <param name="objectOwner">The name of the owner of the object making the request</param>
		/// <param name="questions">The permissions being requested</param>
		public ScriptQuestionEventArgs(Simulator simulator, UUID taskID, UUID itemID, String objectName, String objectOwner, ScriptPermission questions)
		{
			this.m_Simulator = simulator;
			this.m_TaskID = taskID;
			this.m_ItemID = itemID;
			this.m_ObjectName = objectName;
			this.m_ObjectOwnerName = objectOwner;
			this.m_Questions = questions;
		}

	}

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

	/// <summary>The date received from an ImprovedInstantMessage</summary>
	public class InstantMessageEventArgs extends EventArgs
	{
		private  InstantMessage m_IM;
		private  Simulator m_Simulator;

		/// <summary>Get the InstantMessage object</summary>
		public InstantMessage getIM() { return m_IM; } 
		/// <summary>Get the simulator where the InstantMessage origniated</summary>
		public Simulator getSimulator() { return m_Simulator; } 

		/// <summary>
		/// Construct a new instance of the InstantMessageEventArgs object
		/// </summary>
		/// <param name="im">the InstantMessage object</param>
		/// <param name="simulator">the simulator where the InstantMessage origniated</param>
		public InstantMessageEventArgs(InstantMessage im, Simulator simulator)
		{
			this.m_IM = im;
			this.m_Simulator = simulator;
		}
	}

	/// <summary>Contains the currency balance</summary>
	public class BalanceEventArgs extends EventArgs
	{
		private int m_Balance;

		/// <summary>
		/// Get the currenct balance
		/// </summary>
		public int getBalance() {  return m_Balance; } 

		/// <summary>
		/// Construct a new BalanceEventArgs object
		/// </summary>
		/// <param name="balance">The currenct balance</param>
		public BalanceEventArgs(int balance)
		{
			this.m_Balance = balance;
		}
	}

	/// <summary>Contains the transaction summary when an item is purchased, 
	/// money is given, or land is purchased</summary>
	public class MoneyBalanceReplyEventArgs extends EventArgs
	{
		private  UUID m_TransactionID;
		private  boolean m_Success;
		private  int m_Balance;
		private  int m_MetersCredit;
		private  int m_MetersCommitted;
		private  String m_Description;
		private TransactionInfo m_TransactionInfo;

		/// <summary>Get the ID of the transaction</summary>
		public UUID getTransactionID() { return m_TransactionID; } 
		/// <summary>True of the transaction was successful</summary>
		public boolean getSuccess() { return m_Success; } 
		/// <summary>Get the remaining currency balance</summary>
		public int getBalance() { return m_Balance; } 
		/// <summary>Get the meters credited</summary>
		public int getMetersCredit() { return m_MetersCredit; } 
		/// <summary>Get the meters comitted</summary>
		public int getMetersCommitted() { return m_MetersCommitted; } 
		/// <summary>Get the description of the transaction</summary>
		public String getDescription() { return m_Description; } 
		/// <summary>Detailed transaction information</summary>
		public TransactionInfo getTransactionInfo() { return m_TransactionInfo; } 
		/// <summary>
		/// Construct a new instance of the MoneyBalanceReplyEventArgs object
		/// </summary>
		/// <param name="transactionID">The ID of the transaction</param>
		/// <param name="transactionSuccess">True of the transaction was successful</param>
		/// <param name="balance">The current currency balance</param>
		/// <param name="metersCredit">The meters credited</param>
		/// <param name="metersCommitted">The meters comitted</param>
		/// <param name="description">A brief description of the transaction</param>
		public MoneyBalanceReplyEventArgs(UUID transactionID, boolean transactionSuccess, int balance, int metersCredit, int metersCommitted, String description, TransactionInfo transactionInfo)
		{
			this.m_TransactionID = transactionID;
			this.m_Success = transactionSuccess;
			this.m_Balance = balance;
			this.m_MetersCredit = metersCredit;
			this.m_MetersCommitted = metersCommitted;
			this.m_Description = description;
			this.m_TransactionInfo = transactionInfo;
		}
	}

	// String message, TeleportStatus status, TeleportFlags flags
	public class TeleportEventArgs extends EventArgs
	{
		private  String m_Message;
		private  TeleportStatus m_Status;
		private  TeleportFlags m_Flags;

		public String getMessage() {return m_Message;}
		public TeleportStatus getStatus() {return m_Status;}
		public TeleportFlags getFlags() {return m_Flags;}

		public TeleportEventArgs(String message, TeleportStatus status, TeleportFlags flags)
		{
			this.m_Message = message;
			this.m_Status = status;
			this.m_Flags = flags;
		}
	}

	/// <summary>Data sent from the simulator containing information about your agent and active group information</summary>
	public class AgentDataReplyEventArgs extends EventArgs
	{
		private  String m_FirstName;
		private  String m_LastName;
		private  UUID m_ActiveGroupID;
		private  String m_GroupTitle;
		private  GroupPowers m_GroupPowers;
		private  String m_GroupName;

		/// <summary>Get the agents first name</summary>
		public String getFirstName() {return m_FirstName;}
		/// <summary>Get the agents last name</summary>
		public String getLastName() {return m_LastName;}
		/// <summary>Get the active group ID of your agent</summary>
		public UUID getActiveGroupID() {return m_ActiveGroupID;}
		/// <summary>Get the active groups title of your agent</summary>
		public String getGroupTitle() {return m_GroupTitle;}
		/// <summary>Get the combined group powers of your agent</summary>
		public GroupPowers getGroupPowers() {return m_GroupPowers;}
		/// <summary>Get the active group name of your agent</summary>
		public String getGroupName() {return m_GroupName;}

		/// <summary>
		/// Construct a new instance of the AgentDataReplyEventArgs object
		/// </summary>
		/// <param name="firstName">The agents first name</param>
		/// <param name="lastName">The agents last name</param>
		/// <param name="activeGroupID">The agents active group ID</param>
		/// <param name="groupTitle">The group title of the agents active group</param>
		/// <param name="groupPowers">The combined group powers the agent has in the active group</param>
		/// <param name="groupName">The name of the group the agent has currently active</param>
		public AgentDataReplyEventArgs(String firstName, String lastName, UUID activeGroupID,
				String groupTitle, GroupPowers groupPowers, String groupName)
		{
			this.m_FirstName = firstName;
			this.m_LastName = lastName;
			this.m_ActiveGroupID = activeGroupID;
			this.m_GroupTitle = groupTitle;
			this.m_GroupPowers = groupPowers;
			this.m_GroupName = groupName;
		}
	}

	/// <summary>Data sent by the simulator to indicate the active/changed animations
	/// applied to your agent</summary>
	public class AnimationsChangedEventArgs extends EventArgs
	{
		private  InternalDictionary<UUID, Integer> m_Animations;

		/// <summary>Get the dictionary that contains the changed animations</summary>
		public InternalDictionary<UUID, Integer> getAnimations() {return m_Animations;}

		/// <summary>
		/// Construct a new instance of the AnimationsChangedEventArgs class
		/// </summary>
		/// <param name="agentAnimations">The dictionary that contains the changed animations</param>
		public AnimationsChangedEventArgs(InternalDictionary<UUID, Integer> agentAnimations)
		{
			this.m_Animations = agentAnimations;
		}

	}

	/// <summary>
	/// Data sent from a simulator indicating a collision with your agent
	/// </summary>
	public class MeanCollisionEventArgs extends EventArgs
	{
		private  MeanCollisionType m_Type;
		private  UUID m_Aggressor;
		private  UUID m_Victim;
		private  float m_Magnitude;
		private  Date m_Time;

		/// <summary>Get the Type of collision</summary>
		public MeanCollisionType getType() {return m_Type;}
		/// <summary>Get the ID of the agent or object that collided with your agent</summary>
		public UUID getAggressor() {return m_Aggressor;}
		/// <summary>Get the ID of the agent that was attacked</summary>
		public UUID getVictim() {return m_Victim;}
		/// <summary>A value indicating the strength of the collision</summary>
		public float getMagnitude() {return m_Magnitude;}
		/// <summary>Get the time the collision occurred</summary>
		public Date getTime() {return m_Time;}

		/// <summary>
		/// Construct a new instance of the MeanCollisionEventArgs class
		/// </summary>
		/// <param name="type">The type of collision that occurred</param>
		/// <param name="perp">The ID of the agent or object that perpetrated the agression</param>
		/// <param name="victim">The ID of the Victim</param>
		/// <param name="magnitude">The strength of the collision</param>
		/// <param name="time">The Time the collision occurred</param>
		public MeanCollisionEventArgs(MeanCollisionType type, UUID perp, UUID victim,
				float magnitude, Date time)
		{
			this.m_Type = type;
			this.m_Aggressor = perp;
			this.m_Victim = victim;
			this.m_Magnitude = magnitude;
			this.m_Time = time;
		}
	}

	/// <summary>Data sent to your agent when it crosses region boundaries</summary>
	public class RegionCrossedEventArgs extends EventArgs
	{
		private  Simulator m_OldSimulator;
		private  Simulator m_NewSimulator;

		/// <summary>Get the simulator your agent just left</summary>
		public Simulator getOldSimulator() {return m_OldSimulator;}
		/// <summary>Get the simulator your agent is now in</summary>
		public Simulator getNewSimulator() {return m_NewSimulator;}

		/// <summary>
		/// Construct a new instance of the RegionCrossedEventArgs class
		/// </summary>
		/// <param name="oldSim">The simulator your agent just left</param>
		/// <param name="newSim">The simulator your agent is now in</param>
		public RegionCrossedEventArgs(Simulator oldSim, Simulator newSim)
		{
			this.m_OldSimulator = oldSim;
			this.m_NewSimulator = newSim;
		}
	}

	/// <summary>Data sent from the simulator when your agent joins a group chat session</summary>
	public class GroupChatJoinedEventArgs extends EventArgs
	{
		private  UUID m_SessionID;
		private  String m_SessionName;
		private  UUID m_TmpSessionID;
		private  boolean m_Success;

		/// <summary>Get the ID of the group chat session</summary>
		public UUID getSessionID() {return m_SessionID;}
		/// <summary>Get the name of the session</summary>
		public String getSessionName() {return m_SessionName;}
		/// <summary>Get the temporary session ID used for establishing new sessions</summary>
		public UUID getTmpSessionID() {return m_TmpSessionID;}
		/// <summary>True if your agent successfully joined the session</summary>
		public boolean getSuccess() {return m_Success;}

		/// <summary>
		/// Construct a new instance of the GroupChatJoinedEventArgs class
		/// </summary>
		/// <param name="groupChatSessionID">The ID of the session</param>
		/// <param name="sessionName">The name of the session</param>
		/// <param name="tmpSessionID">A temporary session id used for establishing new sessions</param>
		/// <param name="success">True of your agent successfully joined the session</param>
		public GroupChatJoinedEventArgs(UUID groupChatSessionID, String sessionName, UUID tmpSessionID, boolean success)
		{
			this.m_SessionID = groupChatSessionID;
			this.m_SessionName = sessionName;
			this.m_TmpSessionID = tmpSessionID;
			this.m_Success = success;
		}
	}

	/// <summary>Data sent by the simulator containing urgent messages</summary>
	public class AlertMessageEventArgs extends EventArgs
	{
		private  String m_Message;

		/// <summary>Get the alert message</summary>
		public String getMessage() {return m_Message;}

		/// <summary>
		/// Construct a new instance of the AlertMessageEventArgs class
		/// </summary>
		/// <param name="message">The alert message</param>
		public AlertMessageEventArgs(String message)
		{
			this.m_Message = message;
		}
	}

	/// <summary>Data sent by a script requesting to take or release specified controls to your agent</summary>
	public class ScriptControlEventArgs extends EventArgs
	{
		private  ScriptControlChange m_Controls;
		private  boolean m_Pass;
		private  boolean m_Take;

		/// <summary>Get the controls the script is attempting to take or release to the agent</summary>
		public ScriptControlChange getControls() {return m_Controls;}
		/// <summary>True if the script is passing controls back to the agent</summary>
		public boolean getPass() {return m_Pass;}
		/// <summary>True if the script is requesting controls be released to the script</summary>
		public boolean getTake() {return m_Take;}

		/// <summary>
		/// Construct a new instance of the ScriptControlEventArgs class
		/// </summary>
		/// <param name="controls">The controls the script is attempting to take or release to the agent</param>
		/// <param name="pass">True if the script is passing controls back to the agent</param>
		/// <param name="take">True if the script is requesting controls be released to the script</param>
		public ScriptControlEventArgs(ScriptControlChange controls, boolean pass, boolean take)
		{
			m_Controls = controls;
			m_Pass = pass;
			m_Take = take;
		}
	}

	/// <summary>
	/// Data sent from the simulator to an agent to indicate its view limits
	/// </summary>
	public class CameraConstraintEventArgs extends EventArgs
	{
		private  Vector4 m_CollidePlane;

		/// <summary>Get the collision plane</summary>
		public Vector4 getCollidePlane() {return m_CollidePlane;}

		/// <summary>
		/// Construct a new instance of the CameraConstraintEventArgs class
		/// </summary>
		/// <param name="collidePlane">The collision plane</param>
		public CameraConstraintEventArgs(Vector4 collidePlane)
		{
			m_CollidePlane = collidePlane;
		}
	}

	/// <summary>
	/// Data containing script sensor requests which allow an agent to know the specific details
	/// of a primitive sending script sensor requests
	/// </summary>
	public class ScriptSensorReplyEventArgs extends EventArgs
	{
		private  UUID m_RequestorID;
		private  UUID m_GroupID;
		private  String m_Name;
		private  UUID m_ObjectID;
		private  UUID m_OwnerID;
		private  Vector3 m_Position;
		private  float m_Range;
		private  Quaternion m_Rotation;
		private  ScriptSensorTypeFlags m_Type;
		private  Vector3 m_Velocity;

		/// <summary>Get the ID of the primitive sending the sensor</summary>
		public UUID getRequestorID() {return m_RequestorID;}
		/// <summary>Get the ID of the group associated with the primitive</summary>
		public UUID getGroupID() {return m_GroupID;}
		/// <summary>Get the name of the primitive sending the sensor</summary>
		public String getName() {return m_Name;}
		/// <summary>Get the ID of the primitive sending the sensor</summary>
		public UUID getObjectID() {return m_ObjectID;}
		/// <summary>Get the ID of the owner of the primitive sending the sensor</summary>
		public UUID getOwnerID() {return m_OwnerID;}
		/// <summary>Get the position of the primitive sending the sensor</summary>
		public Vector3 getPosition() {return m_Position;}
		/// <summary>Get the range the primitive specified to scan</summary>
		public float getRange() {return m_Range;}
		/// <summary>Get the rotation of the primitive sending the sensor</summary>
		public Quaternion getRotation() {return m_Rotation;}
		/// <summary>Get the type of sensor the primitive sent</summary>
		public ScriptSensorTypeFlags getType() {return m_Type;}
		/// <summary>Get the velocity of the primitive sending the sensor</summary>
		public Vector3 getVelocity() {return m_Velocity;}

		/// <summary>
		/// Construct a new instance of the ScriptSensorReplyEventArgs
		/// </summary>
		/// <param name="requestorID">The ID of the primitive sending the sensor</param>
		/// <param name="groupID">The ID of the group associated with the primitive</param>
		/// <param name="name">The name of the primitive sending the sensor</param>
		/// <param name="objectID">The ID of the primitive sending the sensor</param>
		/// <param name="ownerID">The ID of the owner of the primitive sending the sensor</param>
		/// <param name="position">The position of the primitive sending the sensor</param>
		/// <param name="range">The range the primitive specified to scan</param>
		/// <param name="rotation">The rotation of the primitive sending the sensor</param>
		/// <param name="type">The type of sensor the primitive sent</param>
		/// <param name="velocity">The velocity of the primitive sending the sensor</param>
		public ScriptSensorReplyEventArgs(UUID requestorID, UUID groupID, String name,
				UUID objectID, UUID ownerID, Vector3 position, float range, Quaternion rotation,
				ScriptSensorTypeFlags type, Vector3 velocity)
		{
			this.m_RequestorID = requestorID;
			this.m_GroupID = groupID;
			this.m_Name = name;
			this.m_ObjectID = objectID;
			this.m_OwnerID = ownerID;
			this.m_Position = position;
			this.m_Range = range;
			this.m_Rotation = rotation;
			this.m_Type = type;
			this.m_Velocity = velocity;
		}
	}

	/// <summary>Contains the response data returned from the simulator in response to a <see cref="RequestSit"/></summary>
	public class AvatarSitResponseEventArgs extends EventArgs
	{
		private  UUID m_ObjectID;
		private  boolean m_Autopilot;
		private  Vector3 m_CameraAtOffset;
		private  Vector3 m_CameraEyeOffset;
		private  boolean m_ForceMouselook;
		private  Vector3 m_SitPosition;
		private  Quaternion m_SitRotation;

		/// <summary>Get the ID of the primitive the agent will be sitting on</summary>
		public UUID getObjectID() {return m_ObjectID;}
		/// <summary>True if the simulator Autopilot functions were involved</summary>
		public boolean getAutopilot() {return m_Autopilot;}
		/// <summary>Get the camera offset of the agent when seated</summary>
		public Vector3 getCameraAtOffset() {return m_CameraAtOffset;}
		/// <summary>Get the camera eye offset of the agent when seated</summary>
		public Vector3 getCameraEyeOffset() {return m_CameraEyeOffset;}
		/// <summary>True of the agent will be in mouselook mode when seated</summary>
		public boolean getForceMouselook() {return m_ForceMouselook;}
		/// <summary>Get the position of the agent when seated</summary>
		public Vector3 getSitPosition() {return m_SitPosition;}
		/// <summary>Get the rotation of the agent when seated</summary>
		public Quaternion getSitRotation() {return m_SitRotation;}

		/// <summary>Construct a new instance of the AvatarSitResponseEventArgs object</summary>
		public AvatarSitResponseEventArgs(UUID objectID, boolean autoPilot, Vector3 cameraAtOffset,
				Vector3 cameraEyeOffset, boolean forceMouselook, Vector3 sitPosition, Quaternion sitRotation)
		{
			this.m_ObjectID = objectID;
			this.m_Autopilot = autoPilot;
			this.m_CameraAtOffset = cameraAtOffset;
			this.m_CameraEyeOffset = cameraEyeOffset;
			this.m_ForceMouselook = forceMouselook;
			this.m_SitPosition = sitPosition;
			this.m_SitRotation = sitRotation;
		}
	}

	/// <summary>Data sent when an agent joins a chat session your agent is currently participating in</summary>
	public class ChatSessionMemberAddedEventArgs extends EventArgs
	{
		private  UUID m_SessionID;
		private  UUID m_AgentID;

		/// <summary>Get the ID of the chat session</summary>
		public UUID getSessionID() {return m_SessionID;}
		/// <summary>Get the ID of the agent that joined</summary>
		public UUID getAgentID() {return m_AgentID;}

		/// <summary>
		/// Construct a new instance of the ChatSessionMemberAddedEventArgs object
		/// </summary>
		/// <param name="sessionID">The ID of the chat session</param>
		/// <param name="agentID">The ID of the agent joining</param>
		public ChatSessionMemberAddedEventArgs(UUID sessionID, UUID agentID)
		{
			this.m_SessionID = sessionID;
			this.m_AgentID = agentID;
		}
	}

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

	//endregion


	public static class AgentMovement
	{
		/// <summary>
		/// Camera controls for the agent, mostly a thin wrapper around
		/// CoordinateFrame. This class is only responsible for state
		/// tracking and math, it does not send any packets
		/// </summary>
		public static class AgentCamera
		{
			/// <summary></summary>
			public float Far;

			/// <summary>The camera is a local frame of reference inside of
			/// the larger grid space. This is where the math happens</summary>
			private CoordinateFrame Frame;

			/// <summary></summary>
			public Vector3 getPosition()
			{
				return Frame.getOrigin();
			}

			public void setPosition(Vector3 value)
			{
				Frame.setOrigin(value); 
			}

			/// <summary></summary>
			public Vector3 getAtAxis()
			{
				return Frame.getXAxis();
			}

			public void setAtAxis(Vector3 value)
			{
				Frame.setYAxis(value);
			}

			/// <summary></summary>
			public Vector3 getLeftAxis()
			{
				return Frame.getXAxis();
			}

			public void setLeftAxis(Vector3 value)
			{
				Frame.setXAxis(value);
			}

			/// <summary></summary>
			public Vector3 getUpAxis()
			{
				return Frame.getZAxis();
			}

			public void setUpAxis(Vector3 value)
			{
				Frame.setZAxis(value);
			}

			/// <summary>
			/// Default constructor
			/// </summary>
			public AgentCamera()
			{
				Frame = new CoordinateFrame(new Vector3(128f, 128f, 20f));
				Far = 128f;
			}

			public void Roll(float angle) throws Exception
			{
				Frame.Roll(angle);
			}

			public void Pitch(float angle) throws Exception
			{
				Frame.Pitch(angle);
			}

			public void Yaw(float angle) throws Exception
			{
				Frame.Yaw(angle);
			}

			public void LookDirection(Vector3 target)
			{
				Frame.LookDirection(target);
			}

			public void LookDirection(Vector3 target, Vector3 upDirection)
			{
				Frame.LookDirection(target, upDirection);
			}

			public void LookDirection(double heading)
			{
				Frame.LookDirection(heading);
			}

			public void LookAt(Vector3 position, Vector3 target)
			{
				Frame.LookAt(position, target);
			}

			public void LookAt(Vector3 position, Vector3 target, Vector3 upDirection)
			{
				Frame.LookAt(position, target, upDirection);
			}

			public void SetPositionOrientation(Vector3 position, float roll, float pitch, float yaw) throws Exception
			{
				Frame.origin = position;

				Frame.ResetAxes();

				Frame.Roll(roll);
				Frame.Pitch(pitch);
				Frame.Yaw(yaw);
			}
		}


		/// <summary> 
		/// Agent movement and camera control
		/// 
		/// Agent movement is controlled by setting specific <seealso cref="T:AgentManager.ControlFlags"/>
		/// After the control flags are set, An AgentUpdate is required to update the simulator of the specified flags
		/// This is most easily accomplished by setting one or more of the AgentMovement properties
		/// 
		/// Movement of an avatar is always based on a compass direction, for example AtPos will move the 
		/// agent from West to East or forward on the X Axis, AtNeg will of course move agent from 
		/// East to West or backward on the X Axis, LeftPos will be South to North or forward on the Y Axis
		/// The Z axis is Up, finer grained control of movements can be done using the Nudge properties
		/// </summary> 

		//region Properties

		/// <summary>Move agent positive along the X axis</summary>
		public boolean getAtPos()
		{
			return GetControlFlag(AgentManager.ControlFlags.AGENT_CONTROL_AT_POS);
		}

		public void setAtPos(boolean value)
		{
			SetControlFlag(AgentManager.ControlFlags.AGENT_CONTROL_AT_POS, value);
		}

		/// <summary>Move agent negative along the X axis</summary>
		public boolean getAtNeg()
		{
			return GetControlFlag(AgentManager.ControlFlags.AGENT_CONTROL_AT_NEG);
		}

		public void setAtNeg(boolean value)
		{
			SetControlFlag(AgentManager.ControlFlags.AGENT_CONTROL_AT_NEG, value);
		}

		/// <summary>Move agent positive along the Y axis</summary>
		public boolean getLeftPos()
		{
			return GetControlFlag(AgentManager.ControlFlags.AGENT_CONTROL_LEFT_POS);
		}

		public void setLeftPos(boolean value)
		{
			SetControlFlag(AgentManager.ControlFlags.AGENT_CONTROL_LEFT_POS, value);
		}
		/// <summary>Move agent negative along the Y axis</summary>
		public boolean getLeftNeg()
		{
			return GetControlFlag(AgentManager.ControlFlags.AGENT_CONTROL_LEFT_NEG);
		}

		public void setLeftNeg(boolean value)
		{
			SetControlFlag(AgentManager.ControlFlags.AGENT_CONTROL_LEFT_NEG, value);
		}
		/// <summary>Move agent positive along the Z axis</summary>
		public boolean getUpPos()
		{
			return GetControlFlag(AgentManager.ControlFlags.AGENT_CONTROL_UP_POS);
		}

		public void setUpPos(boolean value)
		{
			SetControlFlag(AgentManager.ControlFlags.AGENT_CONTROL_UP_POS, value);
		}
		/// <summary>Move agent negative along the Z axis</summary>
		public boolean getUpNeg()
		{
			return GetControlFlag(AgentManager.ControlFlags.AGENT_CONTROL_UP_NEG);
		}

		public void setUpNeg(boolean value)
		{
			SetControlFlag(AgentManager.ControlFlags.AGENT_CONTROL_UP_NEG, value);
		}
		/// <summary></summary>
		public boolean getPitchPos()
		{
			return GetControlFlag(AgentManager.ControlFlags.AGENT_CONTROL_PITCH_POS);
		}

		public void setPitchPos(boolean value)
		{
			SetControlFlag(AgentManager.ControlFlags.AGENT_CONTROL_PITCH_POS, value);
		}
		/// <summary></summary>
		public boolean getPitchNeg()
		{
			return GetControlFlag(AgentManager.ControlFlags.AGENT_CONTROL_PITCH_NEG);
		}

		public void setPitchNeg(boolean value)
		{
			SetControlFlag(AgentManager.ControlFlags.AGENT_CONTROL_PITCH_NEG, value);
		}
		/// <summary></summary>
		public boolean getYawPos()
		{
			return GetControlFlag(AgentManager.ControlFlags.AGENT_CONTROL_YAW_POS);
		}

		public void setYawPos(boolean value)
		{
			SetControlFlag(AgentManager.ControlFlags.AGENT_CONTROL_YAW_POS, value);
		}
		/// <summary></summary>
		public boolean getYawNeg()
		{
			return GetControlFlag(AgentManager.ControlFlags.AGENT_CONTROL_YAW_NEG);
		}

		public void setYawNeg(boolean value)
		{
			SetControlFlag(AgentManager.ControlFlags.AGENT_CONTROL_YAW_NEG, value);
		}
		/// <summary></summary>
		public boolean getFastAt()
		{
			return GetControlFlag(AgentManager.ControlFlags.AGENT_CONTROL_FAST_AT);
		}

		public void setFastAt(boolean value)
		{
			SetControlFlag(AgentManager.ControlFlags.AGENT_CONTROL_FAST_AT, value);
		}
		/// <summary></summary>
		public boolean getFastLeft()
		{
			return GetControlFlag(AgentManager.ControlFlags.AGENT_CONTROL_FAST_LEFT);
		}

		public void setFastLeft(boolean value)
		{
			SetControlFlag(AgentManager.ControlFlags.AGENT_CONTROL_FAST_LEFT, value);
		}
		/// <summary></summary>
		public boolean getFastUp()
		{
			return GetControlFlag(AgentManager.ControlFlags.AGENT_CONTROL_FAST_UP);
		}

		public void setFastUp(boolean value)
		{
			SetControlFlag(AgentManager.ControlFlags.AGENT_CONTROL_FAST_UP, value);
		}
		/// <summary>Causes simulator to make agent fly</summary>
		public boolean getFly()
		{
			return GetControlFlag(AgentManager.ControlFlags.AGENT_CONTROL_FLY);
		}

		public void setFly(boolean value)
		{
			SetControlFlag(AgentManager.ControlFlags.AGENT_CONTROL_FLY, value);
		}
		/// <summary>Stop movement</summary>
		public boolean getStop()
		{
			return GetControlFlag(AgentManager.ControlFlags.AGENT_CONTROL_STOP); 
		}

		public void setStop(boolean value)
		{
			SetControlFlag(AgentManager.ControlFlags.AGENT_CONTROL_STOP, value);
		}
		/// <summary>Finish animation</summary>
		public boolean getFinishAnim()
		{
			return GetControlFlag(AgentManager.ControlFlags.AGENT_CONTROL_FINISH_ANIM);
		}

		public void setFinishAnim(boolean value)
		{
			SetControlFlag(AgentManager.ControlFlags.AGENT_CONTROL_FINISH_ANIM, value);
		}
		/// <summary>Stand up from a sit</summary>
		public boolean getStandUp()
		{
			return GetControlFlag(AgentManager.ControlFlags.AGENT_CONTROL_STAND_UP);
		}

		public void setStandUp(boolean value)
		{
			SetControlFlag(AgentManager.ControlFlags.AGENT_CONTROL_STAND_UP, value);
		}
		/// <summary>Tells simulator to sit agent on ground</summary>
		public boolean getSitOnGround()
		{
			return GetControlFlag(AgentManager.ControlFlags.AGENT_CONTROL_SIT_ON_GROUND);
		}

		public void setSitOnGround(boolean value)
		{
			SetControlFlag(AgentManager.ControlFlags.AGENT_CONTROL_SIT_ON_GROUND, value);
		}
		/// <summary>Place agent into mouselook mode</summary>
		public boolean getMouselook()
		{
			return GetControlFlag(AgentManager.ControlFlags.AGENT_CONTROL_MOUSELOOK);
		}

		public void setMouselook(boolean value)
		{
			SetControlFlag(AgentManager.ControlFlags.AGENT_CONTROL_MOUSELOOK, value);
		}
		/// <summary>Nudge agent positive along the X axis</summary>
		public boolean getNudgeAtPos()
		{
			return GetControlFlag(AgentManager.ControlFlags.AGENT_CONTROL_NUDGE_AT_POS);
		}

		public void setNudgeAtPos(boolean value)
		{
			SetControlFlag(AgentManager.ControlFlags.AGENT_CONTROL_NUDGE_AT_POS, value);
		}
		/// <summary>Nudge agent negative along the X axis</summary>
		public boolean getNudgeAtNeg()
		{
			return GetControlFlag(AgentManager.ControlFlags.AGENT_CONTROL_NUDGE_AT_NEG); 
		}

		public void setNudgeAtNeg(boolean value)
		{
			SetControlFlag(AgentManager.ControlFlags.AGENT_CONTROL_NUDGE_AT_NEG, value);
		}
		/// <summary>Nudge agent positive along the Y axis</summary>
		public boolean getNudgeLeftPos()
		{
			return GetControlFlag(AgentManager.ControlFlags.AGENT_CONTROL_NUDGE_LEFT_POS);
		}

		public void setNudgeLeftPos(boolean value)
		{
			SetControlFlag(AgentManager.ControlFlags.AGENT_CONTROL_NUDGE_LEFT_POS, value);
		}
		/// <summary>Nudge agent negative along the Y axis</summary>
		public boolean getNudgeLeftNeg()
		{
			return GetControlFlag(AgentManager.ControlFlags.AGENT_CONTROL_NUDGE_LEFT_NEG);
		}

		public void setNudgeLeftNeg(boolean value)
		{
			SetControlFlag(AgentManager.ControlFlags.AGENT_CONTROL_NUDGE_LEFT_NEG, value);
		}
		/// <summary>Nudge agent positive along the Z axis</summary>
		public boolean getNudgeUpPos()
		{
			return GetControlFlag(AgentManager.ControlFlags.AGENT_CONTROL_NUDGE_UP_POS);
		}

		public void setNudgeUpPos(boolean value)
		{
			SetControlFlag(AgentManager.ControlFlags.AGENT_CONTROL_NUDGE_UP_POS, value);
		}
		/// <summary>Nudge agent negative along the Z axis</summary>
		public boolean getNudgeUpNeg()
		{
			return GetControlFlag(AgentManager.ControlFlags.AGENT_CONTROL_NUDGE_UP_NEG);
		}

		public void setNudgeUpNeg(boolean value)
		{
			SetControlFlag(AgentManager.ControlFlags.AGENT_CONTROL_NUDGE_UP_NEG, value);
		}
		/// <summary></summary>
		public boolean getTurnLeft()
		{
			return GetControlFlag(AgentManager.ControlFlags.AGENT_CONTROL_TURN_LEFT);
		}

		public void setTurnLeft(boolean value)
		{
			SetControlFlag(AgentManager.ControlFlags.AGENT_CONTROL_TURN_LEFT, value);
		}
		/// <summary></summary>
		public boolean getTurnRight()
		{
			return GetControlFlag(AgentManager.ControlFlags.AGENT_CONTROL_TURN_RIGHT);
		}

		public void setTurnRight(boolean value)
		{
			SetControlFlag(AgentManager.ControlFlags.AGENT_CONTROL_TURN_RIGHT, value);
		}
		/// <summary>Tell simulator to mark agent as away</summary>
		public boolean getAway()
		{
			return GetControlFlag(AgentManager.ControlFlags.AGENT_CONTROL_AWAY);
		}

		public void setAway(boolean value)
		{
			SetControlFlag(AgentManager.ControlFlags.AGENT_CONTROL_AWAY, value);
		}
		/// <summary></summary>
		public boolean getLButtonDown()
		{
			return GetControlFlag(AgentManager.ControlFlags.AGENT_CONTROL_LBUTTON_DOWN);
		}

		public void setLButtonDown(boolean value)
		{
			SetControlFlag(AgentManager.ControlFlags.AGENT_CONTROL_LBUTTON_DOWN, value);
		}
		/// <summary></summary>
		public boolean getLButtonUp()
		{
			return GetControlFlag(AgentManager.ControlFlags.AGENT_CONTROL_LBUTTON_UP);
		}

		public void setLButtonUp(boolean value)
		{
			SetControlFlag(AgentManager.ControlFlags.AGENT_CONTROL_LBUTTON_UP, value);
		}
		/// <summary></summary>
		public boolean getMLButtonDown()
		{
			return GetControlFlag(AgentManager.ControlFlags.AGENT_CONTROL_ML_LBUTTON_DOWN);
		}

		public void setMLButtonDown(boolean value)
		{
			SetControlFlag(AgentManager.ControlFlags.AGENT_CONTROL_ML_LBUTTON_DOWN, value);
		}
		/// <summary></summary>
		public boolean getMLButtonUp()
		{
			return GetControlFlag(AgentManager.ControlFlags.AGENT_CONTROL_ML_LBUTTON_UP);
		}

		public void setMLButtonUp(boolean value)
		{
			SetControlFlag(AgentManager.ControlFlags.AGENT_CONTROL_ML_LBUTTON_UP, value);
		}
		/// <summary>
		/// Returns "always run" value, or changes it by sending a SetAlwaysRunPacket
		/// </summary>
		public boolean getAlwaysRun()
		{
			return alwaysRun;
		}

		public void setAlwaysRun(boolean value)
		{
			alwaysRun = value;
			SetAlwaysRunPacket run = new SetAlwaysRunPacket();
			run.AgentData.AgentID = Client.self.getAgentID();
			run.AgentData.SessionID = Client.self.getSessionID();
			run.AgentData.AlwaysRun = alwaysRun;
			Client.network.SendPacket(run);
		}

		/// <summary>The current value of the agent control flags</summary>
		//uint
		public long getAgentControls()
		{
			return agentControls;
		}

		/// <summary>Gets or sets the interval in milliseconds at which
		/// AgentUpdate packets are sent to the current simulator. Setting
		/// this to a non-zero value will also enable the packet sending if
		/// it was previously off, and setting it to zero will disable</summary>
		public int getUpdateInterval()
		{
			return updateInterval;
		}

		public void setUpdateInterval(int value)
		{
			if (value > 0)
			{
				if (updateTimer != null)
				{
					//				updateTimer.Change(value, value);
					createUpdateTimer(value, value);
				}
				updateInterval = value;
			}
			else
			{
				if (updateTimer != null)
				{
					//				updateTimer.Change(Timeout.Infinite, Timeout.Infinite);
					//				createUpdateTimer();
					CleanupTimer();
					updateTimer = new Timer();
				}
				updateInterval = 0;
			}
		}

		/// <summary>Gets or sets whether AgentUpdate packets are sent to
		/// the current simulator</summary>
		public boolean getUpdateEnabled()
		{
			return (updateInterval != 0);
		}

		/// <summary>Reset movement controls every time we send an update</summary>
		public boolean getAutoResetControls()
		{
			return autoResetControls;
		}
		public void setAutoResetControls(boolean value)
		{
			autoResetControls = value;
		}

		//endregion Properties

		/// <summary>Agent camera controls</summary>
		public AgentCamera Camera;
		/// <summary>Currently only used for hiding your group title</summary>
		public AgentFlags Flags = AgentFlags.None;
		/// <summary>Action state of the avatar, which can currently be
		/// typing and editing</summary>
		public AgentState State = AgentState.None;
		/// <summary></summary>
		public Quaternion BodyRotation = Quaternion.Identity;
		/// <summary></summary>
		public Quaternion HeadRotation = Quaternion.Identity;

		//region Change tracking
		/// <summary></summary>
		private Quaternion LastBodyRotation;
		/// <summary></summary>
		private Quaternion LastHeadRotation;
		/// <summary></summary>
		private Vector3 LastCameraCenter;
		/// <summary></summary>
		private Vector3 LastCameraXAxis;
		/// <summary></summary>
		private Vector3 LastCameraYAxis;
		/// <summary></summary>
		private Vector3 LastCameraZAxis;
		/// <summary></summary>
		private float LastFar;
		//endregion Change tracking

		private boolean alwaysRun;
		private GridClient Client;
		//uint
		private long agentControls;
		private int duplicateCount;
		private AgentState lastState;
		/// <summary>Timer for sending AgentUpdate packets</summary>
		private Timer updateTimer;
		private int updateInterval;
		private boolean autoResetControls;

		private class LoginProgressObserver extends EventObserver<LoginProgressEventArgs>
		{
			@Override
			public void handleEvent(Observable o, LoginProgressEventArgs arg) {
				Network_OnConnected(o, arg);
			}
		} 

		private class DisconnectedObserver extends EventObserver<DisconnectedEventArgs>
		{
			@Override
			public void handleEvent(Observable o, DisconnectedEventArgs arg) {
				Network_OnDisconnected(o, arg);
			}
		} 

		/// <summary>Default constructor</summary>
		public AgentMovement(GridClient client)
		{
			Client = client;
			Camera = new AgentCamera();
			Client.network.RegisterLoginProgressCallback(new LoginProgressObserver());                
			Client.network.RegisterOnDisconnectedCallback(new DisconnectedObserver());
			updateInterval = Settings.DEFAULT_AGENT_UPDATE_INTERVAL;
		}

		private void CleanupTimer()
		{
			if (updateTimer != null)
			{
				updateTimer.cancel();
				updateTimer = null;
			}
		}

		private void Network_OnDisconnected(Object sender, DisconnectedEventArgs e)
		{
			CleanupTimer();
		}

		private void Network_OnConnected(Object sender, LoginProgressEventArgs e)
		{
			if (e.getStatus() == LoginStatus.Success)
			{
				//			CleanupTimer();
				////			updateTimer = new Timer(new TimerCallback(UpdateTimer_Elapsed), null, updateInterval, updateInterval);
				//			updateTimer = new Timer();
				//			updateTimer.schedule(new TimerTask()
				//			{ @Override
				//				public void run() {
				//					UpdateTimer_Elapsed(null);
				//				}
				//			}, updateInterval, updateInterval);

				createUpdateTimer(updateInterval, updateInterval);
			}
		}

		private void createUpdateTimer(int delay, int period)
		{
			CleanupTimer();
			updateTimer = new Timer();
			updateTimer.schedule(new TimerTask()
			{ @Override
				public void run() {
				UpdateTimer_Elapsed(null);
			}
			}, delay, period);
		}

		/// <summary>
		/// Send an AgentUpdate with the camera set at the current agent
		/// position and pointing towards the heading specified
		/// </summary>
		/// <param name="heading">Camera rotation in radians</param>
		/// <param name="reliable">Whether to send the AgentUpdate reliable
		/// or not</param>
		public void UpdateFromHeading(double heading, boolean reliable)
		{
			Camera.setPosition(Client.self.getSimPosition());
			Camera.LookDirection(heading);

			BodyRotation.Z = (float)Math.sin(heading / 2.0d);
			BodyRotation.W = (float)Math.cos(heading / 2.0d);
			HeadRotation = BodyRotation;

			SendUpdate(reliable);
		}

		/// <summary>
		/// Rotates the avatar body and camera toward a target position.
		/// This will also anchor the camera position on the avatar
		/// </summary>
		/// <param name="target">Region coordinates to turn toward</param>
		public boolean TurnToward(Vector3 target)
		{
			if (Client.settings.SEND_AGENT_UPDATES)
			{
				Quaternion parentRot = Quaternion.Identity;

				if (Client.self.getSittingOn() > 0)
				{
					if (!Client.network.getCurrentSim().ObjectsPrimitives.containsKey(Client.self.getSittingOn()))
					{
						JLogger.warn("Attempted TurnToward but parent prim is not in dictionary");
						return false;
					}
					else parentRot = Client.network.getCurrentSim().ObjectsPrimitives.get(Client.self.getSittingOn()).Rotation;
				}

				Quaternion between = Vector3.rotationBetween(Vector3.UnitX, Vector3.normalize(Vector3.substract(target, Client.self.getSimPosition())));
				Quaternion rot = Quaternion.multiply(between, Quaternion.divide(Quaternion.Identity, parentRot));

				BodyRotation = rot;
				HeadRotation = rot;
				Camera.LookAt(Client.self.getSimPosition(), target);

				SendUpdate();

				return true;
			}
			else
			{
				JLogger.warn("Attempted TurnToward but agent updates are disabled");
				return false;
			}
		}

		/// <summary>
		/// Send new AgentUpdate packet to update our current camera 
		/// position and rotation
		/// </summary>
		public void SendUpdate()
		{
			SendUpdate(false, Client.network.getCurrentSim());
		}

		/// <summary>
		/// Send new AgentUpdate packet to update our current camera 
		/// position and rotation
		/// </summary>
		/// <param name="reliable">Whether to require server acknowledgement
		/// of this packet</param>
		public void SendUpdate(boolean reliable)
		{
			SendUpdate(reliable, Client.network.getCurrentSim());
		}

		/// <summary>
		/// Send new AgentUpdate packet to update our current camera 
		/// position and rotation
		/// </summary>
		/// <param name="reliable">Whether to require server acknowledgement
		/// of this packet</param>
		/// <param name="simulator">Simulator to send the update to</param>
		public void SendUpdate(boolean reliable, Simulator simulator)
		{
			// Since version 1.40.4 of the Linden simulator, sending this update
			// causes corruption of the agent position in the simulator
			if (simulator != null && (!simulator.AgentMovementComplete))
				return;

			Vector3 origin = Camera.getPosition();
			Vector3 xAxis = Camera.getLeftAxis();
			Vector3 yAxis = Camera.getAtAxis();
			Vector3 zAxis = Camera.getUpAxis();

			// Attempted to sort these in a rough order of how often they might change
			if (agentControls == 0 &&
					yAxis == LastCameraYAxis &&
					origin == LastCameraCenter &&
					State == lastState &&
					HeadRotation == LastHeadRotation &&
					BodyRotation == LastBodyRotation &&
					xAxis == LastCameraXAxis &&
					Camera.Far == LastFar &&
					zAxis == LastCameraZAxis)
			{
				++duplicateCount;
			}
			else
			{
				duplicateCount = 0;
			}

			if (Client.settings.DISABLE_AGENT_UPDATE_DUPLICATE_CHECK || duplicateCount < 10)
			{
				// Store the current state to do duplicate checking
				LastHeadRotation = HeadRotation;
				LastBodyRotation = BodyRotation;
				LastCameraYAxis = yAxis;
				LastCameraCenter = origin;
				LastCameraXAxis = xAxis;
				LastCameraZAxis = zAxis;
				LastFar = Camera.Far;
				lastState = State;

				// Build the AgentUpdate packet and send it
				AgentUpdatePacket update = new AgentUpdatePacket();
				update.header.Reliable = reliable;

				update.AgentData.AgentID = Client.self.getAgentID();
				update.AgentData.SessionID = Client.self.getSessionID();
				update.AgentData.HeadRotation = HeadRotation;
				update.AgentData.BodyRotation = BodyRotation;
				update.AgentData.CameraAtAxis = yAxis;
				update.AgentData.CameraCenter = origin;
				update.AgentData.CameraLeftAxis = xAxis;
				update.AgentData.CameraUpAxis = zAxis;
				update.AgentData.Far = Camera.Far;
				update.AgentData.State = (byte)State.getIndex();
				update.AgentData.ControlFlags = agentControls;
				update.AgentData.Flags = (byte)Flags.getIndex();

				Client.network.SendPacket(update, simulator);

				if (autoResetControls) {
					ResetControlFlags();
				}
			}
		}

		/// <summary>
		/// Builds an AgentUpdate packet entirely from parameters. This
		/// will not touch the state of Self.Movement or
		/// Self.Movement.Camera in any way
		/// </summary>
		/// <param name="controlFlags"></param>
		/// <param name="position"></param>
		/// <param name="forwardAxis"></param>
		/// <param name="leftAxis"></param>
		/// <param name="upAxis"></param>
		/// <param name="bodyRotation"></param>
		/// <param name="headRotation"></param>
		/// <param name="farClip"></param>
		/// <param name="reliable"></param>
		/// <param name="flags"></param>
		/// <param name="state"></param>
		public void SendManualUpdate(AgentManager.ControlFlags controlFlags, Vector3 position, Vector3 forwardAxis,
				Vector3 leftAxis, Vector3 upAxis, Quaternion bodyRotation, Quaternion headRotation, float farClip,
				AgentFlags flags, AgentState state, boolean reliable)
		{
			// Since version 1.40.4 of the Linden simulator, sending this update
			// causes corruption of the agent position in the simulator
			if (Client.network.getCurrentSim() != null && (!Client.network.getCurrentSim().getHandshakeComplete()))
				return;

			AgentUpdatePacket update = new AgentUpdatePacket();

			update.AgentData.AgentID = Client.self.getAgentID();
			update.AgentData.SessionID = Client.self.getSessionID();
			update.AgentData.BodyRotation = bodyRotation;
			update.AgentData.HeadRotation = headRotation;
			update.AgentData.CameraCenter = position;
			update.AgentData.CameraAtAxis = forwardAxis;
			update.AgentData.CameraLeftAxis = leftAxis;
			update.AgentData.CameraUpAxis = upAxis;
			update.AgentData.Far = farClip;
			update.AgentData.ControlFlags = (long)controlFlags.getIndex();
			update.AgentData.Flags = (byte)flags.getIndex();
			update.AgentData.State = (byte)state.getIndex();

			update.header.Reliable = reliable;

			Client.network.SendPacket(update);
		}

		private boolean GetControlFlag(ControlFlags flag)
		{
			return (agentControls & (long)flag.getIndex()) != 0;
		}

		private void SetControlFlag(ControlFlags flag, boolean value)
		{
			if (value) agentControls |= flag.getIndex();
			else agentControls &= ~(flag.getIndex());
		}

		private void ResetControlFlags()
		{
			// Reset all of the flags except for persistent settings like
			// away, fly, mouselook, and crouching
			agentControls &=
					(long)(ControlFlags.AGENT_CONTROL_AWAY.getIndex() |
							ControlFlags.AGENT_CONTROL_FLY.getIndex() |
							ControlFlags.AGENT_CONTROL_MOUSELOOK.getIndex() |
							ControlFlags.AGENT_CONTROL_UP_NEG.getIndex());
		}

		private void UpdateTimer_Elapsed(Object obj)
		{
			if (Client.network.getConnected() && Client.settings.SEND_AGENT_UPDATES)
			{
				//Send an AgentUpdate packet
				SendUpdate(false, Client.network.getCurrentSim());
			}
		}
	}
}
