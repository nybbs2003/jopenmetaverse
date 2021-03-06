/**
 * A library to interact with Virtual Worlds such as OpenSim
 * Copyright (C) 2012  Jitendra Chauhan, Email: jitendra.chauhan@gmail.com
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License,
 * or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation,
 * Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package com.ngt.jopenmetaverse.shared.sim;

import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import com.ngt.jopenmetaverse.shared.cap.http.CapsHttpClient;
import com.ngt.jopenmetaverse.shared.protocol.ParcelPropertiesUpdatePacket;
import com.ngt.jopenmetaverse.shared.protocol.ParcelSetOtherCleanTimePacket;
import com.ngt.jopenmetaverse.shared.sim.message.LindenMessages.ParcelPropertiesUpdateMessage;
import com.ngt.jopenmetaverse.shared.structureddata.OSDFormat;
import com.ngt.jopenmetaverse.shared.structureddata.OSDMap;
import com.ngt.jopenmetaverse.shared.types.UUID;
import com.ngt.jopenmetaverse.shared.types.Vector3;
import com.ngt.jopenmetaverse.shared.util.Utils;


public class ParcelManager {
	public ParcelManager(GridClient client)
	{
		//TODO Need to implement
	}

	//region Enums

	/// <summary>
	/// Type of return to use when returning objects from a parcel
	/// </summary>
	public static enum ObjectReturnType
	{
		//Actual Size uint

		/// <summary></summary>
		None((long) 0),
		/// <summary>Return objects owned by parcel owner</summary>
		Owner((long)1 << 1),
		/// <summary>Return objects set to group</summary>
		Group((long)1 << 2),
		/// <summary>Return objects not owned by parcel owner or set to group</summary>
		Other((long)1 << 3),
		/// <summary>Return a specific list of objects on parcel</summary>
		List((long)1 << 4),
		/// <summary>Return objects that are marked for-sale</summary>
		Sell((long)1 << 5);
		private long index;
		ObjectReturnType(long index)
		{
			this.index = index;
		}     

		public long getIndex()
		{
			return index;
		}
	}

	/// <summary>
	/// Blacklist/Whitelist flags used in parcels Access List
	/// </summary>
	public static enum ParcelAccessFlags
	{
		//uint
		/// <summary>Agent is denied access</summary>
		NoAccess((long)0),
		/// <summary>Agent is granted access</summary>
		Access((long)1);
		private long index;
		ParcelAccessFlags(long index)
		{
			this.index = index;
		}     

		public long getIndex()
		{
			return index;
		}
	}

	/// <summary>
	/// The result of a request for parcel properties
	/// </summary>
	public static enum ParcelResult
	{
		/// <summary>No matches were found for the request</summary>
		NoData((int)-1),
		/// <summary>Request matched a single parcel</summary>
		Single((int)0),
		/// <summary>Request matched multiple parcels</summary>
		Multiple((int)1);
		private int index;
		ParcelResult(int index)
		{
			this.index = index;
		}     

		public int getIndex()
		{
			return index;
		}

		private static final Map<Integer,ParcelResult> lookup  = new HashMap<Integer,ParcelResult>();

		static {
			for(ParcelResult s : EnumSet.allOf(ParcelResult.class))
				lookup.put(s.getIndex(), s);
		}

		public static ParcelResult get(Integer index)
		{
			return lookup.get(index);
		}
	}

	//    /// <summary>
	//    /// Flags used in the ParcelAccessListRequest packet to specify whether
	//    /// we want the access list (whitelist), ban list (blacklist), or both
	//    /// </summary>
	//    //[Flags]
	//    public static enum AccessList : uint
	//    {
	//        /// <summary>Request the access list</summary>
	//        Access = 1 << 0,
	//        /// <summary>Request the ban list</summary>
	//        Ban = 1 << 1,
	//        /// <summary>Request both White and Black lists</summary>
	//        Both = Access | Ban
	//    }
	//
	//    /// <summary>
	//    /// Sequence ID in ParcelPropertiesReply packets (sent when avatar
	//    /// tries to cross a parcel border)
	//    /// </summary>
	//    public static enum ParcelPropertiesStatus : int
	//    {
	//        /// <summary>Parcel is currently selected</summary>
	//        ParcelSelected = -10000,
	//        /// <summary>Parcel restricted to a group the avatar is not a
	//        /// member of</summary>
	//        CollisionNotInGroup = -20000,
	//        /// <summary>Avatar is banned from the parcel</summary>
	//        CollisionBanned = -30000,
	//        /// <summary>Parcel is restricted to an access list that the
	//        /// avatar is not on</summary>
	//        CollisionNotOnAccessList = -40000,
	//        /// <summary>Response to hovering over a parcel</summary>
	//        HoveredOverParcel = -50000
	//    }
	//
	//    /// <summary>
	//    /// The tool to use when modifying terrain levels
	//    /// </summary>
	//    public static enum TerraformAction : byte
	//    {
	//        /// <summary>Level the terrain</summary>
	//        Level = 0,
	//        /// <summary>Raise the terrain</summary>
	//        Raise = 1,
	//        /// <summary>Lower the terrain</summary>
	//        Lower = 2,
	//        /// <summary>Smooth the terrain</summary>
	//        Smooth = 3,
	//        /// <summary>Add random noise to the terrain</summary>
	//        Noise = 4,
	//        /// <summary>Revert terrain to simulator default</summary>
	//        Revert = 5
	//    }
	//
	//    /// <summary>
	//    /// The tool size to use when changing terrain levels
	//    /// </summary>
	//    public static enum TerraformBrushSize : byte
	//    {
	//        /// <summary>Small</summary>
	//        Small = 1,
	//        /// <summary>Medium</summary>
	//        Medium = 2,
	//        /// <summary>Large</summary>
	//        Large = 4
	//    }
	//
	//    /// <summary>
	//    /// Reasons agent is denied access to a parcel on the simulator
	//    /// </summary>
	//    public static enum AccessDeniedReason : byte
	//    {
	//        /// <summary>Agent is not denied, access is granted</summary>
	//        NotDenied = 0,
	//        /// <summary>Agent is not a member of the group set for the parcel, or which owns the parcel</summary>
	//        NotInGroup = 1,
	//        /// <summary>Agent is not on the parcels specific allow list</summary>
	//        NotOnAllowList = 2,
	//        /// <summary>Agent is on the parcels ban list</summary>
	//        BannedFromParcel = 3,
	//        /// <summary>Unknown</summary>
	//        NoAccess = 4,
	//        /// <summary>Agent is not age verified and parcel settings deny access to non age verified avatars</summary>
	//        NotAgeVerified = 5
	//    }
	//
	//    /// <summary>
	//    /// Parcel overlay type. This is used primarily for highlighting and
	//    /// coloring which is why it is a single integer instead of a set of
	//    /// flags
	//    /// </summary>
	//    /// <remarks>These values seem to be poorly thought out. The first three
	//    /// bits represent a single value, not flags. For example Auction (0x05) is
	//    /// not a combination of OwnedByOther (0x01) and ForSale(0x04). However,
	//    /// the BorderWest and BorderSouth values are bit flags that get attached
	//    /// to the value stored in the first three bits. Bits four, five, and six
	//    /// are unused</remarks>
	//    //[Flags]
	//    public static enum ParcelOverlayType : byte
	//    {
	//        /// <summary>Public land</summary>
	//        Public = 0,
	//        /// <summary>Land is owned by another avatar</summary>
	//        OwnedByOther = 1,
	//        /// <summary>Land is owned by a group</summary>
	//        OwnedByGroup = 2,
	//        /// <summary>Land is owned by the current avatar</summary>
	//        OwnedBySelf = 3,
	//        /// <summary>Land is for sale</summary>
	//        ForSale = 4,
	//        /// <summary>Land is being auctioned</summary>
	//        Auction = 5,
	//        /// <summary>Land is private</summary>
	//        Private = 32,
	//        /// <summary>To the west of this area is a parcel border</summary>
	//        BorderWest = 64,
	//        /// <summary>To the south of this area is a parcel border</summary>
	//        BorderSouth = 128
	//    }
	//
	/// <summary>
	/// Various parcel properties
	/// </summary>
	//[Flags]
	public static enum ParcelFlags
	{
		//uint

		/// <summary>No flags set</summary>
		None((long)0),
		/// <summary>Allow avatars to fly (a client-side only restriction)</summary>
		AllowFly((long)1 << 0),
		/// <summary>Allow foreign scripts to run</summary>
		AllowOtherScripts((long)1 << 1),
		/// <summary>This parcel is for sale</summary>
		ForSale((long)1 << 2),
		/// <summary>Allow avatars to create a landmark on this parcel</summary>
		AllowLandmark((long)1 << 3),
		/// <summary>Allows all avatars to edit the terrain on this parcel</summary>
		AllowTerraform((long)1 << 4),
		/// <summary>Avatars have health and can take damage on this parcel.
		/// If set, avatars can be killed and sent home here</summary>
		AllowDamage((long)1 << 5),
		/// <summary>Foreign avatars can create objects here</summary>
		CreateObjects((long)1 << 6),
		/// <summary>All objects on this parcel can be purchased</summary>
		ForSaleObjects((long)1 << 7),
		/// <summary>Access is restricted to a group</summary>
		UseAccessGroup((long)1 << 8),
		/// <summary>Access is restricted to a whitelist</summary>
		UseAccessList((long)1 << 9),
		/// <summary>Ban blacklist is enabled</summary>
		UseBanList((long)1 << 10),
		/// <summary>Unknown</summary>
		UsePassList((long)1 << 11),
		/// <summary>List this parcel in the search directory</summary>
		ShowDirectory((long)1 << 12),
		/// <summary>Allow personally owned parcels to be deeded to group</summary>
		AllowDeedToGroup((long)1 << 13),
		/// <summary>If Deeded, owner contributes required tier to group parcel is deeded to</summary>
		ContributeWithDeed((long)1 << 14),
		/// <summary>Restrict sounds originating on this parcel to the 
		/// parcel boundaries</summary>
		SoundLocal((long)1 << 15),
		/// <summary>Objects on this parcel are sold when the land is 
		/// purchsaed</summary>
		SellParcelObjects((long)1 << 16),
		/// <summary>Allow this parcel to be published on the web</summary>
		AllowPublish((long)1 << 17),
		/// <summary>The information for this parcel is mature content</summary>
		MaturePublish((long)1 << 18),
		/// <summary>The media URL is an HTML page</summary>
		UrlWebPage((long)1 << 19),
		/// <summary>The media URL is a raw HTML string</summary>
		UrlRawHtml((long)1 << 20),
		/// <summary>Restrict foreign object pushes</summary>
		RestrictPushObject((long)1 << 21),
		/// <summary>Ban all non identified/transacted avatars</summary>
		DenyAnonymous((long)1 << 22),
		// <summary>Ban all identified avatars [OBSOLETE]</summary>
		//[Obsolete]
		// This was obsoleted in 1.19.0 but appears to be recycled and is used on linden homes parcels
		LindenHome((long)1 << 23),
		// <summary>Ban all transacted avatars [OBSOLETE]</summary>
		//[Obsolete]
		//DenyTransacted = 1 << 24,
		/// <summary>Allow group-owned scripts to run</summary>
		AllowGroupScripts((long)1 << 25),
		/// <summary>Allow object creation by group members or group 
		/// objects</summary>
		CreateGroupObjects((long)1 << 26),
		/// <summary>Allow all objects to enter this parcel</summary>
		AllowAPrimitiveEntry((long)1 << 27),
		/// <summary>Only allow group and owner objects to enter this parcel</summary>
		AllowGroupObjectEntry((long)1 << 28),
		/// <summary>Voice Enabled on this parcel</summary>
		AllowVoiceChat((long)1 << 29),
		/// <summary>Use Estate Voice channel for Voice on this parcel</summary>
		UseEstateVoiceChan((long)1 << 30),
		/// <summary>Deny Age Unverified Users</summary>
		DenyAgeUnverified((long)1 << 31);
		private long index;
		ParcelFlags(long index)
		{
			this.index = index;
		}     

		public long getIndex()
		{
			return index;
		}
		
		private static final Map<Long,ParcelFlags> lookup  = new HashMap<Long,ParcelFlags>();

		static {
			for(ParcelFlags s : EnumSet.allOf(ParcelFlags.class))
				lookup.put(s.getIndex(), s);
		}

		public static EnumSet<ParcelFlags> get(Long index)
		{
			EnumSet<ParcelFlags> enumsSet = EnumSet.allOf(ParcelFlags.class);
			for(Entry<Long,ParcelFlags> entry: lookup.entrySet())
			{
				if((entry.getKey().longValue() | index) != index)
				{
					enumsSet.remove(entry.getValue());
				}
			}
			return enumsSet;
		}
		
		public static long getIndex(EnumSet<ParcelFlags> enumSet)
		{
			long ret = 0;
			for(ParcelFlags s: enumSet)
			{
				ret |= s.getIndex();
			}
			return ret;
		}

	}

	/// <summary>
	/// Parcel ownership status
	/// </summary>
	public static enum ParcelStatus
	{
		/// <summary>Placeholder</summary>
		None((byte)-1),
		/// <summary>Parcel is leased (owned) by an avatar or group</summary>
		Leased((byte)0),
		/// <summary>Parcel is in process of being leased (purchased) by an avatar or group</summary>
		LeasePending((byte)1),
		/// <summary>Parcel has been abandoned back to Governor Linden</summary>
		Abandoned((byte)2);
		private byte index;
		ParcelStatus(byte index)
		{
			this.index = index;
		}     

		public byte getIndex()
		{
			return index;
		}

		private static final Map<Byte,ParcelStatus> lookup  = new HashMap<Byte,ParcelStatus>();

		static {
			for(ParcelStatus s : EnumSet.allOf(ParcelStatus.class))
				lookup.put(s.getIndex(), s);
		}

		public static ParcelStatus get(Byte index)
		{
			return lookup.get(index);
		}
	}

	/// <summary>
	/// Category parcel is listed in under search
	/// </summary>
	public static enum ParcelCategory
	{
		/// <summary>No assigned category</summary>
		None((byte) 0),
		/// <summary>Linden Infohub or public area</summary>
		Linden((byte) 1),
		/// <summary>Adult themed area</summary>
		Adult((byte) 2),
		/// <summary>Arts and Culture</summary>
		Arts((byte) 3),
		/// <summary>Business</summary>
		Business((byte) 4),
		/// <summary>Educational</summary>
		Educational((byte) 5),
		/// <summary>Gaming</summary>
		Gaming((byte) 6),
		/// <summary>Hangout or Club</summary>
		Hangout((byte) 7),
		/// <summary>Newcomer friendly</summary>
		Newcomer((byte) 8),
		/// <summary>Parks and Nature</summary>
		Park((byte) 9),
		/// <summary>Residential</summary>
		Residential((byte) 10),
		/// <summary>Shopping</summary>
		Shopping((byte) 11),
		/// <summary>Not Used?</summary>
		Stage((byte) 12),
		/// <summary>Other</summary>
		Other((byte) 13),
		/// <summary>Not an actual category, only used for queries</summary>
		Any((byte)-1);
		
		private byte index;
		ParcelCategory(byte index)
		{
			this.index = index;
		}     

		public byte getIndex()
		{
			return index;
		}

		private static final Map<Byte,ParcelCategory> lookup  = new HashMap<Byte,ParcelCategory>();

		static {
			for(ParcelCategory s : EnumSet.allOf(ParcelCategory.class))
				lookup.put(s.getIndex(), s);
		}

		public static ParcelCategory get(Byte index)
		{
			return lookup.get(index);
		}
	}

	/// <summary>
	/// Type of teleport landing for a parcel
	/// </summary>
	public static enum LandingType
	{
		/// <summary>Unset, simulator default</summary>
		None((byte)0),
		/// <summary>Specific landing point set for this parcel</summary>
		LandingPoint((byte)1),
		/// <summary>No landing point set, direct teleports enabled for
		/// this parcel</summary>
		Direct((byte)2);
		private byte index;
		LandingType(byte index)
		{
			this.index = index;
		}     

		public byte getIndex()
		{
			return index;
		}
		
		private static final Map<Byte,LandingType> lookup  = new HashMap<Byte,LandingType>();

		static {
			for(LandingType s : EnumSet.allOf(LandingType.class))
				lookup.put(s.getIndex(), s);
		}

		public static LandingType get(Byte index)
		{
			return lookup.get(index);
		}
	}

	    /// <summary>
	    /// Parcel Media Command used in ParcelMediaCommandMessage
	    /// </summary>
	    public static enum ParcelMediaCommand 
	    {
	        /// <summary>Stop the media stream and go back to the first frame</summary>
	        Stop((long) 0),
	        /// <summary>Pause the media stream (stop playing but stay on current frame)</summary>
	        Pause((long)1),
	        /// <summary>Start the current media stream playing and stop when the end is reached</summary>
	        Play((long)2),
	        /// <summary>Start the current media stream playing, 
	        /// loop to the beginning when the end is reached and continue to play</summary>
	        Loop((long)3),
	        /// <summary>Specifies the texture to replace with video</summary>
	        /// <remarks>If passing the key of a texture, it must be explicitly typecast as a key, 
	        /// not just passed within double quotes.</remarks>
	        Texture((long)4),
	        /// <summary>Specifies the movie URL (254 characters max)</summary>
	        URL((long)5),
	        /// <summary>Specifies the time index at which to begin playing</summary>
	        Time((long)6),
	        /// <summary>Specifies a single agent to apply the media command to</summary>
	        Agent((long)7),
	        /// <summary>Unloads the stream. While the stop command sets the texture to the first frame of the movie, 
	        /// unload resets it to the real texture that the movie was replacing.</summary>
	        Unload((long)8),
	        /// <summary>Turn on/off the auto align feature, similar to the auto align checkbox in the parcel media properties 
	        /// (NOT to be confused with the "align" function in the textures view of the editor!) Takes TRUE or FALSE as parameter.</summary>
	        AutoAlign((long)9),
	        /// <summary>Allows a Web page or image to be placed on a prim (1.19.1 RC0 and later only). 
	        /// Use "text/html" for HTML.</summary>
	        Type((long)10),
	        /// <summary>Resizes a Web page to fit on x, y pixels (1.19.1 RC0 and later only).</summary>
	        /// <remarks>This might still not be working</remarks>
	        Size((long)11),
	        /// <summary>Sets a description for the media being displayed (1.19.1 RC0 and later only).</summary>
	        Desc((long)12);
	        
	    	private long index;
	    	ParcelMediaCommand(long index)
			{
				this.index = index;
			}     

			public long getIndex()
			{
				return index;
			}
			
			private static final Map<Long,ParcelMediaCommand> lookup  = new HashMap<Long,ParcelMediaCommand>();

			static {
				for(ParcelMediaCommand s : EnumSet.allOf(ParcelMediaCommand.class))
					lookup.put(s.getIndex(), s);
			}

			public static ParcelMediaCommand get(Long index)
			{
				return lookup.get(index);
			}
	        
	    }
	
	    //endregion Enums
	
	    //region Structs
	
	    /// <summary>
	    /// Some information about a parcel of land returned from a DirectoryManager search
	    /// </summary>
	    public class ParcelInfo
	    {
	        /// <summary>Global Key of record</summary>
	        public UUID ID;
	        /// <summary>Parcel Owners <seealso cref="UUID"/></summary>
	        public UUID OwnerID;
	        /// <summary>Name field of parcel, limited to 128 characters</summary>
	        public String Name;
	        /// <summary>Description field of parcel, limited to 256 characters</summary>
	        public String Description;
	        /// <summary>Total Square meters of parcel</summary>
	        public int ActualArea;
	        /// <summary>Total area billable as Tier, for group owned land this will be 10% less than ActualArea</summary>
	        public int BillableArea;
	        /// <summary>True of parcel is in Mature simulator</summary>
	        public boolean Mature;
	        /// <summary>Grid global X position of parcel</summary>
	        public float GlobalX;
	        /// <summary>Grid global Y position of parcel</summary>
	        public float GlobalY;
	        /// <summary>Grid global Z position of parcel (not used)</summary>
	        public float GlobalZ;
	        /// <summary>Name of simulator parcel is located in</summary>
	        public String SimName;
	        /// <summary>Texture <seealso cref="T:OpenMetaverse.UUID"/> of parcels display picture</summary>
	        public UUID SnapshotID;
	        /// <summary>Float representing calculated traffic based on time spent on parcel by avatars</summary>
	        public float Dwell;
	        /// <summary>Sale price of parcel (not used)</summary>
	        public int SalePrice;
	        /// <summary>Auction ID of parcel</summary>
	        public int AuctionID;
	    }
	
	    /// <summary>
	    /// Parcel Media Information
	    /// </summary>
	    public class ParcelMedia
	    {
	        /// <summary>A byte, if 0x1 viewer should auto scale media to fit object</summary>
	        public boolean MediaAutoScale;
	        /// <summary>A boolean, if true the viewer should loop the media</summary>
	        public boolean MediaLoop;
	        /// <summary>The Asset UUID of the Texture which when applied to a 
	        /// primitive will display the media</summary>
	        public UUID MediaID;
	        /// <summary>A URL which points to any Quicktime supported media type</summary>
	        public String MediaURL;
	        /// <summary>A description of the media</summary>
	        public String MediaDesc;
	        /// <summary>An Integer which represents the height of the media</summary>
	        public int MediaHeight;
	        /// <summary>An integer which represents the width of the media</summary>
	        public int MediaWidth;
	        /// <summary>A string which contains the mime type of the media</summary>
	        public String MediaType;
	    }
	
	    //endregion Structs
	
	    //region Parcel Class
	
	    /// <summary>
	    /// Parcel of land, a portion of virtual real estate in a simulator
	    /// </summary>
	    public class Parcel
	    {
	        /// <summary>The total number of contiguous 4x4 meter blocks your agent owns within this parcel</summary>        
	        public int SelfCount;
	        /// <summary>The total number of contiguous 4x4 meter blocks contained in this parcel owned by a group or agent other than your own</summary>
	        public int OtherCount;
	        /// <summary>Deprecated, Value appears to always be 0</summary>
	        public int PublicCount;
	        /// <summary>Simulator-local ID of this parcel</summary>
	        public int LocalID;
	        /// <summary>UUID of the owner of this parcel</summary>
	        public UUID OwnerID;
	        /// <summary>Whether the land is deeded to a group or not</summary>
	        public boolean IsGroupOwned;
	        /// <summary></summary>
	        //uint
	        public long AuctionID;
	        /// <summary>Date land was claimed</summary>
	        public Date ClaimDate;
	        /// <summary>Appears to always be zero</summary>
	        public int ClaimPrice;
	        /// <summary>This field is no longer used</summary>
	        public int RentPrice;
	        /// <summary>Minimum corner of the axis-aligned bounding box for this
	        /// parcel</summary>
	        public Vector3 AABBMin;
	        /// <summary>Maximum corner of the axis-aligned bounding box for this
	        /// parcel</summary>
	        public Vector3 AABBMax;
	        /// <summary>Bitmap describing land layout in 4x4m squares across the 
	        /// entire region</summary>
	        public byte[] Bitmap;
	        /// <summary>Total parcel land area</summary>
	        public int Area;
	        /// <summary></summary>
	        public ParcelStatus Status;
	        /// <summary>Maximum primitives across the entire simulator owned by the same agent or group that owns this parcel that can be used</summary>
	        public int SimWideMaxPrims;
	        /// <summary>Total primitives across the entire simulator calculated by combining the allowed prim counts for each parcel
	        /// owned by the agent or group that owns this parcel</summary>
	        public int SimWideTotalPrims;
	        /// <summary>Maximum number of primitives this parcel supports</summary>
	        public int MaxPrims;
	        /// <summary>Total number of primitives on this parcel</summary>
	        public int TotalPrims;
	        /// <summary>For group-owned parcels this indicates the total number of prims deeded to the group,
	        /// for parcels owned by an individual this inicates the number of prims owned by the individual</summary>
	        public int OwnerPrims;
	        /// <summary>Total number of primitives owned by the parcel group on 
	        /// this parcel, or for parcels owned by an individual with a group set the
	        /// total number of prims set to that group.</summary>
	        public int GroupPrims;
	        /// <summary>Total number of prims owned by other avatars that are not set to group, or not the parcel owner</summary>
	        public int OtherPrims;
	        /// <summary>A bonus multiplier which allows parcel prim counts to go over times this amount, this does not affect
	        /// the max prims per simulator. e.g: 117 prim parcel limit x 1.5 bonus = 175 allowed</summary>
	        public float ParcelPrimBonus;
	        /// <summary>Autoreturn value in minutes for others' objects</summary>
	        public int OtherCleanTime;
	        /// <summary></summary>
	        public EnumSet<ParcelFlags> Flags;
	        /// <summary>Sale price of the parcel, only useful if ForSale is set</summary>
	        /// <remarks>The SalePrice will remain the same after an ownership
	        /// transfer (sale), so it can be used to see the purchase price after
	        /// a sale if the new owner has not changed it</remarks>
	        public int SalePrice;
	        /// <summary>Parcel Name</summary>
	        public String Name;
	        /// <summary>Parcel Description</summary>
	        public String Desc;
	        /// <summary>URL For Music Stream</summary>
	        public String MusicURL;
	        /// <summary></summary>
	        public UUID GroupID;
	        /// <summary>Price for a temporary pass</summary>
	        public int PassPrice;
	        /// <summary>How long is pass valid for</summary>
	        public float PassHours;
	        /// <summary></summary>
	        public ParcelCategory Category;
	        /// <summary>Key of authorized buyer</summary>
	        public UUID AuthBuyerID;
	        /// <summary>Key of parcel snapshot</summary>
	        public UUID SnapshotID;
	        /// <summary>The landing point location</summary>
	        public Vector3 UserLocation;
	        /// <summary>The landing point LookAt</summary>
	        public Vector3 UserLookAt;
	        /// <summary>The type of landing enforced from the <see cref="LandingType"/> enum</summary>
	        public LandingType Landing;
	        /// <summary></summary>
	        public float Dwell;
	        /// <summary></summary>
	        public boolean RegionDenyAnonymous;
	        /// <summary></summary>
	        public boolean RegionPushOverride;
	        /// <summary>Access list of who is whitelisted on this
	        /// parcel</summary>
	        public List<ParcelAccessFlags> AccessWhiteList;
	        /// <summary>Access list of who is blacklisted on this
	        /// parcel</summary>
	        public List<ParcelAccessFlags> AccessBlackList;
	        /// <summary>TRUE of region denies access to age unverified users</summary>
	        public boolean RegionDenyAgeUnverified;
	        /// <summary>true to obscure (hide) media url</summary>
	        public boolean ObscureMedia;
	        /// <summary>true to obscure (hide) music url</summary>
	        public boolean ObscureMusic;
	        /// <summary>A struct containing media details</summary>
	        public ParcelMedia Media;
	
	        /// <summary>
	        /// Displays a parcel object in string format
	        /// </summary>
	        /// <returns>string containing key=value pairs of a parcel object</returns>
	        @Override
	        public String toString()
	        {
	        	//TODO need to implement
//	            String result = "";
//	            Type parcelType = this.GetType();
//	            FieldInfo[] fields = parcelType.GetFields();
//	            foreach (FieldInfo field in fields)
//	            {
//	                result += (field.Name + " = " + field.GetValue(this) + " ");
//	            }
//	            return result;
	        	return super.toString();
	        }
	        /// <summary>
	        /// Defalt constructor
	        /// </summary>
	        /// <param name="localID">Local ID of this parcel</param>
	        public Parcel(int localID)
	        {
	            LocalID = localID;
	            ClaimDate = Utils.Epoch;
	            Bitmap = Utils.EmptyBytes;
	            Name = "";
	            Desc = "";
	            MusicURL = "";
	            AccessWhiteList = new ArrayList<ParcelAccessFlags>(0);
	            AccessBlackList = new ArrayList<ParcelAccessFlags>(0);
	            Media = new ParcelMedia();
	        }
	
	        /// <summary>
	        /// Update the simulator with any local changes to this Parcel object
	        /// </summary>
	        /// <param name="simulator">Simulator to send updates to</param>
	        /// <param name="wantReply">Whether we want the simulator to confirm
	        /// the update with a reply packet or not</param>
	        public void Update(Simulator simulator, boolean wantReply) throws Exception
	        {
	            URI url = simulator.Caps.CapabilityURI("ParcelPropertiesUpdate");
	
	            if (url != null)
	            {
	                ParcelPropertiesUpdateMessage req = new ParcelPropertiesUpdateMessage();
	                req.AuthBuyerID = this.AuthBuyerID;
	                req.Category = this.Category;
	                req.Desc = this.Desc;
	                req.GroupID = this.GroupID;
	                req.Landing = this.Landing;
	                req.LocalID = this.LocalID;
	                req.MediaAutoScale = this.Media.MediaAutoScale;
	                req.MediaDesc = this.Media.MediaDesc;
	                req.MediaHeight = this.Media.MediaHeight;
	                req.MediaID = this.Media.MediaID;
	                req.MediaLoop = this.Media.MediaLoop;
	                req.MediaType = this.Media.MediaType;
	                req.MediaURL = this.Media.MediaURL;
	                req.MediaWidth = this.Media.MediaWidth;
	                req.MusicURL = this.MusicURL;
	                req.Name = this.Name;
	                req.ObscureMedia = this.ObscureMedia;
	                req.ObscureMusic = this.ObscureMusic;
	                req.parcelFlags = this.Flags;
	                req.PassHours = this.PassHours;
	                req.PassPrice = (long)this.PassPrice;
	                req.SalePrice = (long)this.SalePrice;
	                req.SnapshotID = this.SnapshotID;
	                req.UserLocation = this.UserLocation;
	                req.UserLookAt = this.UserLookAt;
	               
	                OSDMap body = req.Serialize();
	
	                CapsHttpClient capsPost = new CapsHttpClient(url);
	                capsPost.BeginGetResponse(body, OSDFormat.Xml, simulator.Client.settings.CAPS_TIMEOUT);
	            }
	            else
	            {
	                ParcelPropertiesUpdatePacket request = new ParcelPropertiesUpdatePacket();
	
	                request.AgentData.AgentID = simulator.Client.self.getAgentID();
	                request.AgentData.SessionID = simulator.Client.self.getSessionID();
	
	                request.ParcelData.LocalID = this.LocalID;
	
	                request.ParcelData.AuthBuyerID = this.AuthBuyerID;
	                request.ParcelData.Category = (byte)this.Category.getIndex();
	                request.ParcelData.Desc = Utils.stringToBytesWithTrailingNullByte(this.Desc);
	                request.ParcelData.GroupID = this.GroupID;
	                request.ParcelData.LandingType = (byte)this.Landing.getIndex();
	                request.ParcelData.MediaAutoScale = (this.Media.MediaAutoScale) ? (byte)0x1 : (byte)0x0;
	                request.ParcelData.MediaID = this.Media.MediaID;
	                request.ParcelData.MediaURL = Utils.stringToBytesWithTrailingNullByte(this.Media.MediaURL.toString());
	                request.ParcelData.MusicURL = Utils.stringToBytesWithTrailingNullByte(this.MusicURL.toString());
	                request.ParcelData.Name = Utils.stringToBytesWithTrailingNullByte(this.Name);
	                if (wantReply) request.ParcelData.Flags = 1;
	                request.ParcelData.ParcelFlags = ParcelFlags.getIndex(this.Flags);
	                request.ParcelData.PassHours = this.PassHours;
	                request.ParcelData.PassPrice = this.PassPrice;
	                request.ParcelData.SalePrice = this.SalePrice;
	                request.ParcelData.SnapshotID = this.SnapshotID;
	                request.ParcelData.UserLocation = this.UserLocation;
	                request.ParcelData.UserLookAt = this.UserLookAt;
	
	                simulator.SendPacket(request);
	            }
	
	            UpdateOtherCleanTime(simulator);
	            
	        }
	
	        /// <summary>
	        /// Set Autoreturn time
	        /// </summary>
	        /// <param name="simulator">Simulator to send the update to</param>
	        public void UpdateOtherCleanTime(Simulator simulator)
	        {
	            ParcelSetOtherCleanTimePacket request = new ParcelSetOtherCleanTimePacket();
	            request.AgentData.AgentID = simulator.Client.self.getAgentID();
	            request.AgentData.SessionID = simulator.Client.self.getSessionID();
	            request.ParcelData.LocalID = this.LocalID;
	            request.ParcelData.OtherCleanTime = this.OtherCleanTime;
	
	            simulator.SendPacket(request);
	        }
	    }
	
	    //endregion Parcel Class
	//
	//    /// <summary>
	//    /// Parcel (subdivided simulator lots) subsystem
	//    /// </summary>
	//    public class ParcelManager
	//    {
	//        //region Structs
	//
	//        /// <summary>
	//        /// Parcel Accesslist
	//        /// </summary>
	//        public struct ParcelAccessEntry
	//        {
	//            /// <summary>Agents <seealso cref="T:OpenMetaverse.UUID"/></summary>
	//            public UUID AgentID;
	//            /// <summary></summary>
	//            public DateTime Time;
	//            /// <summary>Flags for specific entry in white/black lists</summary>
	//            public AccessList Flags;
	//        }
	//
	//        /// <summary>
	//        /// Owners of primitives on parcel
	//        /// </summary>
	//        public struct ParcelPrimOwners
	//        {
	//            /// <summary>Prim Owners <seealso cref="T:OpenMetaverse.UUID"/></summary>
	//            public UUID OwnerID;
	//            /// <summary>True of owner is group</summary>
	//            public bool IsGroupOwned;
	//            /// <summary>Total count of prims owned by OwnerID</summary>
	//            public int Count;
	//            /// <summary>true of OwnerID is currently online and is not a group</summary>
	//            public bool OnlineStatus;
	//            /// <summary>The date of the most recent prim left by OwnerID</summary>
	//            public DateTime NewestPrim;
	//        }
	//
	//        //endregion Structs
	//
	//        //region Delegates
	//        /// <summary>
	//        /// Called once parcel resource usage information has been collected
	//        /// </summary>
	//        /// <param name="success">Indicates if operation was successfull</param>
	//        /// <param name="info">Parcel resource usage information</param>
	//        public delegate void LandResourcesCallback(bool success, LandResourcesInfo info);
	//
	//        /// <summary>The event subscribers. null if no subcribers</summary>
	//        private EventHandler<ParcelDwellReplyEventArgs> m_DwellReply;
	//
	//        /// <summary>Raises the ParcelDwellReply event</summary>
	//        /// <param name="e">A ParcelDwellReplyEventArgs object containing the
	//        /// data returned from the simulator</param>
	//        protected virtual void OnParcelDwellReply(ParcelDwellReplyEventArgs e)
	//        {
	//            EventHandler<ParcelDwellReplyEventArgs> handler = m_DwellReply;
	//            if (handler != null)
	//                handler(this, e);
	//        }
	//
	//        /// <summary>Thread sync lock object</summary>
	//        private readonly object m_DwellReplyLock = new object();
	//        
	//        /// <summary>Raised when the simulator responds to a <see cref="RequestDwell"/> request</summary>
	//        public event EventHandler<ParcelDwellReplyEventArgs> ParcelDwellReply
	//        {
	//            add { lock (m_DwellReplyLock) { m_DwellReply += value; } }
	//            remove { lock (m_DwellReplyLock) { m_DwellReply -= value; } }
	//        }
	//
	//        /// <summary>The event subscribers. null if no subcribers</summary>
	//        private EventHandler<ParcelInfoReplyEventArgs> m_ParcelInfo;
	//
	//        /// <summary>Raises the ParcelInfoReply event</summary>
	//        /// <param name="e">A ParcelInfoReplyEventArgs object containing the
	//        /// data returned from the simulator</param>
	//        protected virtual void OnParcelInfoReply(ParcelInfoReplyEventArgs e)
	//        {
	//            EventHandler<ParcelInfoReplyEventArgs> handler = m_ParcelInfo;
	//            if (handler != null)
	//                handler(this, e);
	//        }
	//
	//        /// <summary>Thread sync lock object</summary>
	//        private readonly object m_ParcelInfoLock = new object();
	//
	//        /// <summary>Raised when the simulator responds to a <see cref="RequestParcelInfo"/> request</summary>
	//        public event EventHandler<ParcelInfoReplyEventArgs> ParcelInfoReply
	//        {
	//            add { lock (m_ParcelInfoLock) { m_ParcelInfo += value; } }
	//            remove { lock (m_ParcelInfoLock) { m_ParcelInfo -= value; } }
	//        }
	//
	//        /// <summary>The event subscribers. null if no subcribers</summary>
	//        private EventHandler<ParcelPropertiesEventArgs> m_ParcelProperties;
	//
	//        /// <summary>Raises the ParcelProperties event</summary>
	//        /// <param name="e">A ParcelPropertiesEventArgs object containing the
	//        /// data returned from the simulator</param>
	//        protected virtual void OnParcelProperties(ParcelPropertiesEventArgs e)
	//        {
	//            EventHandler<ParcelPropertiesEventArgs> handler = m_ParcelProperties;
	//            if (handler != null)
	//                handler(this, e);
	//        }
	//
	//        /// <summary>Thread sync lock object</summary>
	//        private readonly object m_ParcelPropertiesLock = new object();
	//
	//        /// <summary>Raised when the simulator responds to a <see cref="RequestParcelProperties"/> request</summary>
	//        public event EventHandler<ParcelPropertiesEventArgs> ParcelProperties
	//        {
	//            add { lock (m_ParcelPropertiesLock) { m_ParcelProperties += value; } }
	//            remove { lock (m_ParcelPropertiesLock) { m_ParcelProperties -= value; } }
	//        }
	//
	//        /// <summary>The event subscribers. null if no subcribers</summary>
	//        private EventHandler<ParcelAccessListReplyEventArgs> m_ParcelACL;
	//
	//        /// <summary>Raises the ParcelAccessListReply event</summary>
	//        /// <param name="e">A ParcelAccessListReplyEventArgs object containing the
	//        /// data returned from the simulator</param>
	//        protected virtual void OnParcelAccessListReply(ParcelAccessListReplyEventArgs e)
	//        {
	//            EventHandler<ParcelAccessListReplyEventArgs> handler = m_ParcelACL;
	//            if (handler != null)
	//                handler(this, e);
	//        }
	//
	//        /// <summary>Thread sync lock object</summary>
	//        private readonly object m_ParcelACLLock = new object();
	//
	//        /// <summary>Raised when the simulator responds to a <see cref="RequestParcelAccessList"/> request</summary>
	//        public event EventHandler<ParcelAccessListReplyEventArgs> ParcelAccessListReply
	//        {
	//            add { lock (m_ParcelACLLock) { m_ParcelACL += value; } }
	//            remove { lock (m_ParcelACLLock) { m_ParcelACL -= value; } }
	//        }
	//
	//        /// <summary>The event subscribers. null if no subcribers</summary>
	//        private EventHandler<ParcelObjectOwnersReplyEventArgs> m_ParcelObjectOwnersReply;
	//
	//        /// <summary>Raises the ParcelObjectOwnersReply event</summary>
	//        /// <param name="e">A ParcelObjectOwnersReplyEventArgs object containing the
	//        /// data returned from the simulator</param>
	//        protected virtual void OnParcelObjectOwnersReply(ParcelObjectOwnersReplyEventArgs e)
	//        {
	//            EventHandler<ParcelObjectOwnersReplyEventArgs> handler = m_ParcelObjectOwnersReply;
	//            if (handler != null)
	//                handler(this, e);
	//        }
	//
	//        /// <summary>Thread sync lock object</summary>
	//        private readonly object m_ParcelObjectOwnersLock = new object();
	//
	//        /// <summary>Raised when the simulator responds to a <see cref="RequestObjectOwners"/> request</summary>
	//        public event EventHandler<ParcelObjectOwnersReplyEventArgs> ParcelObjectOwnersReply
	//        {
	//            add { lock (m_ParcelObjectOwnersLock) { m_ParcelObjectOwnersReply += value; } }
	//            remove { lock (m_ParcelObjectOwnersLock) { m_ParcelObjectOwnersReply -= value; } }
	//        }
	//
	//        /// <summary>The event subscribers. null if no subcribers</summary>
	//        private EventHandler<SimParcelsDownloadedEventArgs> m_SimParcelsDownloaded;
	//
	//        /// <summary>Raises the SimParcelsDownloaded event</summary>
	//        /// <param name="e">A SimParcelsDownloadedEventArgs object containing the
	//        /// data returned from the simulator</param>
	//        protected virtual void OnSimParcelsDownloaded(SimParcelsDownloadedEventArgs e)
	//        {
	//            EventHandler<SimParcelsDownloadedEventArgs> handler = m_SimParcelsDownloaded;
	//            if (handler != null)
	//                handler(this, e);
	//        }
	//
	//        /// <summary>Thread sync lock object</summary>
	//        private readonly object m_SimParcelsDownloadedLock = new object();
	//
	//        /// <summary>Raised when the simulator responds to a <see cref="RequestAllSimParcels"/> request</summary>
	//        public event EventHandler<SimParcelsDownloadedEventArgs> SimParcelsDownloaded
	//        {
	//            add { lock (m_SimParcelsDownloadedLock) { m_SimParcelsDownloaded += value; } }
	//            remove { lock (m_SimParcelsDownloadedLock) { m_SimParcelsDownloaded -= value; } }
	//        }
	//
	//        /// <summary>The event subscribers. null if no subcribers</summary>
	//        private EventHandler<ForceSelectObjectsReplyEventArgs> m_ForceSelectObjects;
	//
	//        /// <summary>Raises the ForceSelectObjectsReply event</summary>
	//        /// <param name="e">A ForceSelectObjectsReplyEventArgs object containing the
	//        /// data returned from the simulator</param>
	//        protected virtual void OnForceSelectObjectsReply(ForceSelectObjectsReplyEventArgs e)
	//        {
	//            EventHandler<ForceSelectObjectsReplyEventArgs> handler = m_ForceSelectObjects;
	//            if (handler != null)
	//                handler(this, e);
	//        }
	//
	//        /// <summary>Thread sync lock object</summary>
	//        private readonly object m_ForceSelectObjectsLock = new object();
	//
	//        /// <summary>Raised when the simulator responds to a <see cref="RequestForceSelectObjects"/> request</summary>
	//        public event EventHandler<ForceSelectObjectsReplyEventArgs> ForceSelectObjectsReply
	//        {
	//            add { lock (m_ForceSelectObjectsLock) { m_ForceSelectObjects += value; } }
	//            remove { lock (m_ForceSelectObjectsLock) { m_ForceSelectObjects -= value; } }
	//        }
	//
	//        /// <summary>The event subscribers. null if no subcribers</summary>
	//        private EventHandler<ParcelMediaUpdateReplyEventArgs> m_ParcelMediaUpdateReply;
	//
	//        /// <summary>Raises the ParcelMediaUpdateReply event</summary>
	//        /// <param name="e">A ParcelMediaUpdateReplyEventArgs object containing the
	//        /// data returned from the simulator</param>
	//        protected virtual void OnParcelMediaUpdateReply(ParcelMediaUpdateReplyEventArgs e)
	//        {
	//            EventHandler<ParcelMediaUpdateReplyEventArgs> handler = m_ParcelMediaUpdateReply;
	//            if (handler != null)
	//                handler(this, e);
	//        }
	//
	//        /// <summary>Thread sync lock object</summary>
	//        private readonly object m_ParcelMediaUpdateReplyLock = new object();
	//
	//        /// <summary>Raised when the simulator responds to a Parcel Update request</summary>
	//        public event EventHandler<ParcelMediaUpdateReplyEventArgs> ParcelMediaUpdateReply
	//        {
	//            add { lock (m_ParcelMediaUpdateReplyLock) { m_ParcelMediaUpdateReply += value; } }
	//            remove { lock (m_ParcelMediaUpdateReplyLock) { m_ParcelMediaUpdateReply -= value; } }
	//        }
	//
	//        /// <summary>The event subscribers. null if no subcribers</summary>
	//        private EventHandler<ParcelMediaCommandEventArgs> m_ParcelMediaCommand;
	//
	//        /// <summary>Raises the ParcelMediaCommand event</summary>
	//        /// <param name="e">A ParcelMediaCommandEventArgs object containing the
	//        /// data returned from the simulator</param>
	//        protected virtual void OnParcelMediaCommand(ParcelMediaCommandEventArgs e)
	//        {
	//            EventHandler<ParcelMediaCommandEventArgs> handler = m_ParcelMediaCommand;
	//            if (handler != null)
	//                handler(this, e);
	//        }
	//
	//        /// <summary>Thread sync lock object</summary>
	//        private readonly object m_ParcelMediaCommandLock = new object();
	//
	//        /// <summary>Raised when the parcel your agent is located sends a ParcelMediaCommand</summary>
	//        public event EventHandler<ParcelMediaCommandEventArgs> ParcelMediaCommand
	//        {
	//            add { lock (m_ParcelMediaCommandLock) { m_ParcelMediaCommand += value; } }
	//            remove { lock (m_ParcelMediaCommandLock) { m_ParcelMediaCommand -= value; } }
	//        }
	//        //endregion Delegates
	//
	//        private GridClient Client;
	//        private AutoResetEvent WaitForSimParcel;
	//
	//        //region Public Methods
	//
	//        /// <summary>
	//        /// Default constructor
	//        /// </summary>
	//        /// <param name="client">A reference to the GridClient object</param>
	//        public ParcelManager(GridClient client)
	//        {
	//            Client = client;
	//            
	//            // Setup the callbacks
	//            Client.Network.RegisterCallback(PacketType.ParcelInfoReply, ParcelInfoReplyHandler);
	//            Client.Network.RegisterEventCallback("ParcelObjectOwnersReply", new Caps.EventQueueCallback(ParcelObjectOwnersReplyHandler));
	//            // CAPS packet handler, to allow for Media Data not contained in the message template
	//            Client.Network.RegisterEventCallback("ParcelProperties", new Caps.EventQueueCallback(ParcelPropertiesReplyHandler));
	//            Client.Network.RegisterCallback(PacketType.ParcelDwellReply, ParcelDwellReplyHandler);
	//            Client.Network.RegisterCallback(PacketType.ParcelAccessListReply, ParcelAccessListReplyHandler);
	//            Client.Network.RegisterCallback(PacketType.ForceObjectSelect, SelectParcelObjectsReplyHandler);
	//            Client.Network.RegisterCallback(PacketType.ParcelMediaUpdate, ParcelMediaUpdateHandler);
	//            Client.Network.RegisterCallback(PacketType.ParcelOverlay, ParcelOverlayHandler);
	//            Client.Network.RegisterCallback(PacketType.ParcelMediaCommandMessage, ParcelMediaCommandMessagePacketHandler);
	//        }
	//
	//        /// <summary>
	//        /// Request basic information for a single parcel
	//        /// </summary>
	//        /// <param name="parcelID">Simulator-local ID of the parcel</param>
	//        public void RequestParcelInfo(UUID parcelID)
	//        {
	//            ParcelInfoRequestPacket request = new ParcelInfoRequestPacket();
	//            request.AgentData.AgentID = Client.Self.AgentID;
	//            request.AgentData.SessionID = Client.Self.SessionID;
	//            request.Data.ParcelID = parcelID;
	//
	//            Client.Network.SendPacket(request);
	//        }
	//
	//        /// <summary>
	//        /// Request properties of a single parcel
	//        /// </summary>
	//        /// <param name="simulator">Simulator containing the parcel</param>
	//        /// <param name="localID">Simulator-local ID of the parcel</param>
	//        /// <param name="sequenceID">An arbitrary integer that will be returned
	//        /// with the ParcelProperties reply, useful for distinguishing between
	//        /// multiple simultaneous requests</param>
	//        public void RequestParcelProperties(Simulator simulator, int localID, int sequenceID)
	//        {
	//            ParcelPropertiesRequestByIDPacket request = new ParcelPropertiesRequestByIDPacket();
	//
	//            request.AgentData.AgentID = Client.Self.AgentID;
	//            request.AgentData.SessionID = Client.Self.SessionID;
	//
	//            request.ParcelData.LocalID = localID;
	//            request.ParcelData.SequenceID = sequenceID;
	//
	//            Client.Network.SendPacket(request, simulator);
	//        }
	//
	//        /// <summary>
	//        /// Request the access list for a single parcel
	//        /// </summary>
	//        /// <param name="simulator">Simulator containing the parcel</param>
	//        /// <param name="localID">Simulator-local ID of the parcel</param>
	//        /// <param name="sequenceID">An arbitrary integer that will be returned
	//        /// with the ParcelAccessList reply, useful for distinguishing between
	//        /// multiple simultaneous requests</param>
	//        /// <param name="flags"></param>
	//        public void RequestParcelAccessList(Simulator simulator, int localID, AccessList flags, int sequenceID)
	//        {
	//            ParcelAccessListRequestPacket request = new ParcelAccessListRequestPacket();
	//
	//            request.AgentData.AgentID = Client.Self.AgentID;
	//            request.AgentData.SessionID = Client.Self.SessionID;
	//            request.Data.LocalID = localID;
	//            request.Data.Flags = (uint)flags;
	//            request.Data.SequenceID = sequenceID;
	//
	//            Client.Network.SendPacket(request, simulator);
	//        }
	//
	//        /// <summary>
	//        /// Request properties of parcels using a bounding box selection
	//        /// </summary>
	//        /// <param name="simulator">Simulator containing the parcel</param>
	//        /// <param name="north">Northern boundary of the parcel selection</param>
	//        /// <param name="east">Eastern boundary of the parcel selection</param>
	//        /// <param name="south">Southern boundary of the parcel selection</param>
	//        /// <param name="west">Western boundary of the parcel selection</param>
	//        /// <param name="sequenceID">An arbitrary integer that will be returned
	//        /// with the ParcelProperties reply, useful for distinguishing between
	//        /// different types of parcel property requests</param>
	//        /// <param name="snapSelection">A boolean that is returned with the
	//        /// ParcelProperties reply, useful for snapping focus to a single
	//        /// parcel</param>
	//        public void RequestParcelProperties(Simulator simulator, float north, float east, float south, float west,
	//            int sequenceID, bool snapSelection)
	//        {
	//            ParcelPropertiesRequestPacket request = new ParcelPropertiesRequestPacket();
	//
	//            request.AgentData.AgentID = Client.Self.AgentID;
	//            request.AgentData.SessionID = Client.Self.SessionID;
	//            request.ParcelData.North = north;
	//            request.ParcelData.East = east;
	//            request.ParcelData.South = south;
	//            request.ParcelData.West = west;
	//            request.ParcelData.SequenceID = sequenceID;
	//            request.ParcelData.SnapSelection = snapSelection;
	//
	//            Client.Network.SendPacket(request, simulator);
	//        }
	//
	//        /// <summary>
	//        /// Request all simulator parcel properties (used for populating the <code>Simulator.Parcels</code> 
	//        /// dictionary)
	//        /// </summary>
	//        /// <param name="simulator">Simulator to request parcels from (must be connected)</param>
	//        public void RequestAllSimParcels(Simulator simulator)
	//        {
	//            RequestAllSimParcels(simulator, false, 750);
	//        }
	//
	//        /// <summary>
	//        /// Request all simulator parcel properties (used for populating the <code>Simulator.Parcels</code> 
	//        /// dictionary)
	//        /// </summary>
	//        /// <param name="simulator">Simulator to request parcels from (must be connected)</param>
	//        /// <param name="refresh">If TRUE, will force a full refresh</param>
	//        /// <param name="msDelay">Number of milliseconds to pause in between each request</param>
	//        public void RequestAllSimParcels(Simulator simulator, bool refresh, int msDelay)
	//        {
	//            if (simulator.DownloadingParcelMap)
	//            {
	//                Logger.Log("Already downloading parcels in " + simulator.Name, Helpers.LogLevel.Info, Client);
	//                return;
	//            }
	//            else
	//            {
	//                simulator.DownloadingParcelMap = true;
	//                WaitForSimParcel = new AutoResetEvent(false);
	//            }
	//
	//            if (refresh)
	//            {
	//                    for (int y = 0; y < 64; y++)
	//                        for (int x = 0; x < 64; x++)
	//                            simulator.ParcelMap[y, x] = 0;
	//            }
	//
	//            Thread th = new Thread(delegate()
	//            {
	//                int count = 0, timeouts = 0, y, x;
	//
	//                for (y = 0; y < 64; y++)
	//                {
	//                    for (x = 0; x < 64; x++)
	//                    {
	//                        if (!Client.Network.Connected)
	//                            return;
	//
	//                        if (simulator.ParcelMap[y, x] == 0)
	//                        {
	//                            Client.Parcels.RequestParcelProperties(simulator,
	//                                                             (y + 1) * 4.0f, (x + 1) * 4.0f,
	//                                                             y * 4.0f, x * 4.0f, int.MaxValue, false);
	//
	//                            // Wait the given amount of time for a reply before sending the next request
	//                            if (!WaitForSimParcel.WaitOne(msDelay, false))
	//                                ++timeouts;
	//
	//                            ++count;
	//                        }
	//                    }
	//                }
	//
	//                Logger.Log(String.Format(
	//                    "Full simulator parcel information retrieved. Sent {0} parcel requests. Current outgoing queue: {1}, Retry Count {2}",
	//                    count, Client.Network.OutboxCount, timeouts), Helpers.LogLevel.Info, Client);
	//
	//                simulator.DownloadingParcelMap = false;
	//            });
	//
	//            th.Start();
	//        }
	//
	//        /// <summary>
	//        /// Request the dwell value for a parcel
	//        /// </summary>
	//        /// <param name="simulator">Simulator containing the parcel</param>
	//        /// <param name="localID">Simulator-local ID of the parcel</param>
	//        public void RequestDwell(Simulator simulator, int localID)
	//        {
	//            ParcelDwellRequestPacket request = new ParcelDwellRequestPacket();
	//            request.AgentData.AgentID = Client.Self.AgentID;
	//            request.AgentData.SessionID = Client.Self.SessionID;
	//            request.Data.LocalID = localID;
	//            request.Data.ParcelID = UUID.Zero; // Not used by clients
	//
	//            Client.Network.SendPacket(request, simulator);
	//        }
	//
	//        /// <summary>
	//        /// Send a request to Purchase a parcel of land
	//        /// </summary>
	//        /// <param name="simulator">The Simulator the parcel is located in</param>
	//        /// <param name="localID">The parcels region specific local ID</param>
	//        /// <param name="forGroup">true if this parcel is being purchased by a group</param>
	//        /// <param name="groupID">The groups <seealso cref="T:OpenMetaverse.UUID"/></param>
	//        /// <param name="removeContribution">true to remove tier contribution if purchase is successful</param>
	//        /// <param name="parcelArea">The parcels size</param>
	//        /// <param name="parcelPrice">The purchase price of the parcel</param>
	//        /// <returns></returns>
	//        public void Buy(Simulator simulator, int localID, bool forGroup, UUID groupID,
	//            bool removeContribution, int parcelArea, int parcelPrice)
	//        {
	//            ParcelBuyPacket request = new ParcelBuyPacket();
	//
	//            request.AgentData.AgentID = Client.Self.AgentID;
	//            request.AgentData.SessionID = Client.Self.SessionID;
	//
	//            request.Data.Final = true;
	//            request.Data.GroupID = groupID;
	//            request.Data.LocalID = localID;
	//            request.Data.IsGroupOwned = forGroup;
	//            request.Data.RemoveContribution = removeContribution;
	//
	//            request.ParcelData.Area = parcelArea;
	//            request.ParcelData.Price = parcelPrice;
	//
	//            Client.Network.SendPacket(request, simulator);
	//        }
	//
	//        /// <summary>
	//        /// Reclaim a parcel of land
	//        /// </summary>
	//        /// <param name="simulator">The simulator the parcel is in</param>
	//        /// <param name="localID">The parcels region specific local ID</param>
	//        public void Reclaim(Simulator simulator, int localID)
	//        {
	//            ParcelReclaimPacket request = new ParcelReclaimPacket();
	//            request.AgentData.AgentID = Client.Self.AgentID;
	//            request.AgentData.SessionID = Client.Self.SessionID;
	//
	//            request.Data.LocalID = localID;
	//
	//            Client.Network.SendPacket(request, simulator);
	//        }
	//
	//        /// <summary>
	//        /// Deed a parcel to a group
	//        /// </summary>
	//        /// <param name="simulator">The simulator the parcel is in</param>
	//        /// <param name="localID">The parcels region specific local ID</param>
	//        /// <param name="groupID">The groups <seealso cref="T:OpenMetaverse.UUID"/></param>
	//        public void DeedToGroup(Simulator simulator, int localID, UUID groupID)
	//        {
	//            ParcelDeedToGroupPacket request = new ParcelDeedToGroupPacket();
	//            request.AgentData.AgentID = Client.Self.AgentID;
	//            request.AgentData.SessionID = Client.Self.SessionID;
	//
	//            request.Data.LocalID = localID;
	//            request.Data.GroupID = groupID;
	//
	//            Client.Network.SendPacket(request, simulator);
	//        }
	//
	//        /// <summary>
	//        /// Request prim owners of a parcel of land.
	//        /// </summary>
	//        /// <param name="simulator">Simulator parcel is in</param>
	//        /// <param name="localID">The parcels region specific local ID</param>
	//        public void RequestObjectOwners(Simulator simulator, int localID)
	//        {
	//            ParcelObjectOwnersRequestPacket request = new ParcelObjectOwnersRequestPacket();
	//
	//            request.AgentData.AgentID = Client.Self.AgentID;
	//            request.AgentData.SessionID = Client.Self.SessionID;
	//
	//            request.ParcelData.LocalID = localID;
	//            Client.Network.SendPacket(request, simulator);
	//        }
	//
	//        /// <summary>
	//        /// Return objects from a parcel
	//        /// </summary>
	//        /// <param name="simulator">Simulator parcel is in</param>
	//        /// <param name="localID">The parcels region specific local ID</param>
	//        /// <param name="type">the type of objects to return, <seealso cref="T:OpenMetaverse.ObjectReturnType"/></param>
	//        /// <param name="ownerIDs">A list containing object owners <seealso cref="OpenMetaverse.UUID"/>s to return</param>
	//        public void ReturnObjects(Simulator simulator, int localID, ObjectReturnType type, List<UUID> ownerIDs)
	//        {
	//            ParcelReturnObjectsPacket request = new ParcelReturnObjectsPacket();
	//            request.AgentData.AgentID = Client.Self.AgentID;
	//            request.AgentData.SessionID = Client.Self.SessionID;
	//
	//            request.ParcelData.LocalID = localID;
	//            request.ParcelData.ReturnType = (uint)type;
	//
	//            // A single null TaskID is (not) used for parcel object returns
	//            request.TaskIDs = new ParcelReturnObjectsPacket.TaskIDsBlock[1];
	//            request.TaskIDs[0] = new ParcelReturnObjectsPacket.TaskIDsBlock();
	//            request.TaskIDs[0].TaskID = UUID.Zero;
	//
	//            // Convert the list of owner UUIDs to packet blocks if a list is given
	//            if (ownerIDs != null)
	//            {
	//                request.OwnerIDs = new ParcelReturnObjectsPacket.OwnerIDsBlock[ownerIDs.Count];
	//
	//                for (int i = 0; i < ownerIDs.Count; i++)
	//                {
	//                    request.OwnerIDs[i] = new ParcelReturnObjectsPacket.OwnerIDsBlock();
	//                    request.OwnerIDs[i].OwnerID = ownerIDs[i];
	//                }
	//            }
	//            else
	//            {
	//                request.OwnerIDs = new ParcelReturnObjectsPacket.OwnerIDsBlock[0];
	//            }
	//
	//            Client.Network.SendPacket(request, simulator);
	//        }
	//
	//        /// <summary>
	//        /// Subdivide (split) a parcel
	//        /// </summary>
	//        /// <param name="simulator"></param>
	//        /// <param name="west"></param>
	//        /// <param name="south"></param>
	//        /// <param name="east"></param>
	//        /// <param name="north"></param>
	//        public void ParcelSubdivide(Simulator simulator, float west, float south, float east, float north)
	//        {
	//            ParcelDividePacket divide = new ParcelDividePacket();
	//            divide.AgentData.AgentID = Client.Self.AgentID;
	//            divide.AgentData.SessionID = Client.Self.SessionID;
	//            divide.ParcelData.East = east;
	//            divide.ParcelData.North = north;
	//            divide.ParcelData.South = south;
	//            divide.ParcelData.West = west;
	//
	//            Client.Network.SendPacket(divide, simulator);
	//        }
	//
	//        /// <summary>
	//        /// Join two parcels of land creating a single parcel
	//        /// </summary>
	//        /// <param name="simulator"></param>
	//        /// <param name="west"></param>
	//        /// <param name="south"></param>
	//        /// <param name="east"></param>
	//        /// <param name="north"></param>
	//        public void ParcelJoin(Simulator simulator, float west, float south, float east, float north)
	//        {
	//            ParcelJoinPacket join = new ParcelJoinPacket();
	//            join.AgentData.AgentID = Client.Self.AgentID;
	//            join.AgentData.SessionID = Client.Self.SessionID;
	//            join.ParcelData.East = east;
	//            join.ParcelData.North = north;
	//            join.ParcelData.South = south;
	//            join.ParcelData.West = west;
	//
	//            Client.Network.SendPacket(join, simulator);
	//        }
	//
	//        /// <summary>
	//        /// Get a parcels LocalID
	//        /// </summary>
	//        /// <param name="simulator">Simulator parcel is in</param>
	//        /// <param name="position">Vector3 position in simulator (Z not used)</param>
	//        /// <returns>0 on failure, or parcel LocalID on success.</returns>
	//        /// <remarks>A call to <code>Parcels.RequestAllSimParcels</code> is required to populate map and
	//        /// dictionary.</remarks>
	//        public int GetParcelLocalID(Simulator simulator, Vector3 position)
	//        {
	//            if (simulator.ParcelMap[(byte)position.Y / 4, (byte)position.X / 4] > 0)
	//            {
	//                return simulator.ParcelMap[(byte)position.Y / 4, (byte)position.X / 4];
	//            }
	//            else
	//            {
	//                Logger.Log(String.Format("ParcelMap returned an default/invalid value for location {0}/{1} Did you use RequestAllSimParcels() to populate the dictionaries?", (byte)position.Y / 4, (byte)position.X / 4 ), Helpers.LogLevel.Warning);
	//                return 0;
	//            }
	//        }
	//
	//        /// <summary>
	//        /// Terraform (raise, lower, etc) an area or whole parcel of land
	//        /// </summary>
	//        /// <param name="simulator">Simulator land area is in.</param>
	//        /// <param name="localID">LocalID of parcel, or -1 if using bounding box</param>
	//        /// <param name="action">From Enum, Raise, Lower, Level, Smooth, Etc.</param>
	//        /// <param name="brushSize">Size of area to modify</param>
	//        /// <returns>true on successful request sent.</returns>
	//        /// <remarks>Settings.STORE_LAND_PATCHES must be true, 
	//        /// Parcel information must be downloaded using <code>RequestAllSimParcels()</code></remarks>
	//        public bool Terraform(Simulator simulator, int localID, TerraformAction action, TerraformBrushSize brushSize)
	//        {
	//            return Terraform(simulator, localID, 0f, 0f, 0f, 0f, action, brushSize, 1);
	//        }
	//
	//        /// <summary>
	//        /// Terraform (raise, lower, etc) an area or whole parcel of land
	//        /// </summary>
	//        /// <param name="simulator">Simulator land area is in.</param>
	//        /// <param name="west">west border of area to modify</param>
	//        /// <param name="south">south border of area to modify</param>
	//        /// <param name="east">east border of area to modify</param>
	//        /// <param name="north">north border of area to modify</param>
	//        /// <param name="action">From Enum, Raise, Lower, Level, Smooth, Etc.</param>
	//        /// <param name="brushSize">Size of area to modify</param>
	//        /// <returns>true on successful request sent.</returns>
	//        /// <remarks>Settings.STORE_LAND_PATCHES must be true, 
	//        /// Parcel information must be downloaded using <code>RequestAllSimParcels()</code></remarks>
	//        public bool Terraform(Simulator simulator, float west, float south, float east, float north,
	//            TerraformAction action, TerraformBrushSize brushSize)
	//        {
	//            return Terraform(simulator, -1, west, south, east, north, action, brushSize, 1);
	//        }
	//
	//        /// <summary>
	//        /// Terraform (raise, lower, etc) an area or whole parcel of land
	//        /// </summary>
	//        /// <param name="simulator">Simulator land area is in.</param>
	//        /// <param name="localID">LocalID of parcel, or -1 if using bounding box</param>
	//        /// <param name="west">west border of area to modify</param>
	//        /// <param name="south">south border of area to modify</param>
	//        /// <param name="east">east border of area to modify</param>
	//        /// <param name="north">north border of area to modify</param>
	//        /// <param name="action">From Enum, Raise, Lower, Level, Smooth, Etc.</param>
	//        /// <param name="brushSize">Size of area to modify</param>
	//        /// <param name="seconds">How many meters + or - to lower, 1 = 1 meter</param>
	//        /// <returns>true on successful request sent.</returns>
	//        /// <remarks>Settings.STORE_LAND_PATCHES must be true, 
	//        /// Parcel information must be downloaded using <code>RequestAllSimParcels()</code></remarks>
	//        public bool Terraform(Simulator simulator, int localID, float west, float south, float east, float north,
	//            TerraformAction action, TerraformBrushSize brushSize, int seconds)
	//        {
	//            float height = 0f;
	//            int x, y;
	//            if (localID == -1)
	//            {
	//                x = (int)east - (int)west / 2;
	//                y = (int)north - (int)south / 2;
	//            }
	//            else
	//            {
	//                Parcel p;
	//                if (!simulator.Parcels.TryGetValue(localID, out p))
	//                {
	//                    Logger.Log(String.Format("Can't find parcel {0} in simulator {1}", localID, simulator),
	//                        Helpers.LogLevel.Warning, Client);
	//                    return false;
	//                }
	//
	//                x = (int)p.AABBMax.X - (int)p.AABBMin.X / 2;
	//                y = (int)p.AABBMax.Y - (int)p.AABBMin.Y / 2;
	//            }
	//
	//            if (!simulator.TerrainHeightAtPoint(x, y, out height))
	//            {
	//                Logger.Log("Land Patch not stored for location", Helpers.LogLevel.Warning, Client);
	//                return false;
	//            }
	//
	//            Terraform(simulator, localID, west, south, east, north, action, brushSize, seconds, height);
	//            return true;
	//        }
	//
	//        /// <summary>
	//        /// Terraform (raise, lower, etc) an area or whole parcel of land
	//        /// </summary>
	//        /// <param name="simulator">Simulator land area is in.</param>
	//        /// <param name="localID">LocalID of parcel, or -1 if using bounding box</param>
	//        /// <param name="west">west border of area to modify</param>
	//        /// <param name="south">south border of area to modify</param>
	//        /// <param name="east">east border of area to modify</param>
	//        /// <param name="north">north border of area to modify</param>
	//        /// <param name="action">From Enum, Raise, Lower, Level, Smooth, Etc.</param>
	//        /// <param name="brushSize">Size of area to modify</param>
	//        /// <param name="seconds">How many meters + or - to lower, 1 = 1 meter</param>
	//        /// <param name="height">Height at which the terraform operation is acting at</param>
	//        public void Terraform(Simulator simulator, int localID, float west, float south, float east, float north,
	//            TerraformAction action, TerraformBrushSize brushSize, int seconds, float height)
	//        {
	//            ModifyLandPacket land = new ModifyLandPacket();
	//            land.AgentData.AgentID = Client.Self.AgentID;
	//            land.AgentData.SessionID = Client.Self.SessionID;
	//
	//            land.ModifyBlock.Action = (byte)action;
	//            land.ModifyBlock.BrushSize = (byte)brushSize;
	//            land.ModifyBlock.Seconds = seconds;
	//            land.ModifyBlock.Height = height;
	//
	//            land.ParcelData = new ModifyLandPacket.ParcelDataBlock[1];
	//            land.ParcelData[0] = new ModifyLandPacket.ParcelDataBlock();
	//            land.ParcelData[0].LocalID = localID;
	//            land.ParcelData[0].West = west;
	//            land.ParcelData[0].South = south;
	//            land.ParcelData[0].East = east;
	//            land.ParcelData[0].North = north;
	//
	//            land.ModifyBlockExtended = new ModifyLandPacket.ModifyBlockExtendedBlock[1];
	//            land.ModifyBlockExtended[0] = new ModifyLandPacket.ModifyBlockExtendedBlock();
	//            land.ModifyBlockExtended[0].BrushSize = (float)brushSize;
	//
	//            Client.Network.SendPacket(land, simulator);
	//        }
	//
	//        /// <summary>
	//        /// Sends a request to the simulator to return a list of objects owned by specific owners
	//        /// </summary>
	//        /// <param name="localID">Simulator local ID of parcel</param>
	//        /// <param name="selectType">Owners, Others, Etc</param>
	//        /// <param name="ownerID">List containing keys of avatars objects to select; 
	//        /// if List is null will return Objects of type <c>selectType</c></param>
	//        /// <remarks>Response data is returned in the event <seealso cref="E:ForceSelectObjectsReply"/></remarks>
	//        public void RequestSelectObjects(int localID, ObjectReturnType selectType, UUID ownerID)
	//        {
	//            ParcelSelectObjectsPacket select = new ParcelSelectObjectsPacket();
	//            select.AgentData.AgentID = Client.Self.AgentID;
	//            select.AgentData.SessionID = Client.Self.SessionID;
	//
	//            select.ParcelData.LocalID = localID;
	//            select.ParcelData.ReturnType = (uint)selectType;
	//
	//            select.ReturnIDs = new ParcelSelectObjectsPacket.ReturnIDsBlock[1];
	//            select.ReturnIDs[0] = new ParcelSelectObjectsPacket.ReturnIDsBlock();
	//            select.ReturnIDs[0].ReturnID = ownerID;
	//
	//            Client.Network.SendPacket(select);
	//        }
	//
	//        /// <summary>
	//        /// Eject and optionally ban a user from a parcel
	//        /// </summary>
	//        /// <param name="targetID">target key of avatar to eject</param>
	//        /// <param name="ban">true to also ban target</param>
	//        public void EjectUser(UUID targetID, bool ban)
	//        {
	//            EjectUserPacket eject = new EjectUserPacket();
	//            eject.AgentData.AgentID = Client.Self.AgentID;
	//            eject.AgentData.SessionID = Client.Self.SessionID;
	//            eject.Data.TargetID = targetID;
	//            if (ban) eject.Data.Flags = 1;
	//            else eject.Data.Flags = 0;
	//
	//            Client.Network.SendPacket(eject);
	//        }
	//
	//        /// <summary>
	//        /// Freeze or unfreeze an avatar over your land
	//        /// </summary>
	//        /// <param name="targetID">target key to freeze</param>
	//        /// <param name="freeze">true to freeze, false to unfreeze</param>
	//        public void FreezeUser(UUID targetID, bool freeze)
	//        {
	//            FreezeUserPacket frz = new FreezeUserPacket();
	//            frz.AgentData.AgentID = Client.Self.AgentID;
	//            frz.AgentData.SessionID = Client.Self.SessionID;
	//            frz.Data.TargetID = targetID;
	//            if (freeze) frz.Data.Flags = 0;
	//            else frz.Data.Flags = 1;
	//
	//            Client.Network.SendPacket(frz);
	//        }
	//
	//        /// <summary>
	//        /// Abandon a parcel of land
	//        /// </summary>
	//        /// <param name="simulator">Simulator parcel is in</param>
	//        /// <param name="localID">Simulator local ID of parcel</param>
	//        public void ReleaseParcel(Simulator simulator, int localID)
	//        {
	//            ParcelReleasePacket abandon = new ParcelReleasePacket();
	//            abandon.AgentData.AgentID = Client.Self.AgentID;
	//            abandon.AgentData.SessionID = Client.Self.SessionID;
	//            abandon.Data.LocalID = localID;
	//
	//            Client.Network.SendPacket(abandon, simulator);
	//        }
	//
	//        /// <summary>
	//        /// Requests the UUID of the parcel in a remote region at a specified location
	//        /// </summary>
	//        /// <param name="location">Location of the parcel in the remote region</param>
	//        /// <param name="regionHandle">Remote region handle</param>
	//        /// <param name="regionID">Remote region UUID</param>
	//        /// <returns>If successful UUID of the remote parcel, UUID.Zero otherwise</returns>
	//        public UUID RequestRemoteParcelID(Vector3 location, ulong regionHandle, UUID regionID)
	//        {
	//            Uri url = Client.Network.CurrentSim.Caps.CapabilityURI("RemoteParcelRequest");
	//
	//            if (url != null)
	//            {
	//                RemoteParcelRequestRequest msg = new RemoteParcelRequestRequest();
	//                msg.Location = location;
	//                msg.RegionHandle = regionHandle;
	//                msg.RegionID = regionID;
	//
	//                try
	//                {
	//                    CapsClient request = new CapsClient(url);
	//                    OSD result = request.GetResponse(msg.Serialize(), OSDFormat.Xml, Client.Settings.CAPS_TIMEOUT);
	//                    RemoteParcelRequestReply response = new RemoteParcelRequestReply();
	//                    response.Deserialize((OSDMap)result);
	//                    return response.ParcelID;
	//                }
	//                catch (Exception)
	//                {
	//                    Logger.Log("Failed to fetch remote parcel ID", Helpers.LogLevel.Debug, Client);
	//                }
	//            }
	//            
	//            return UUID.Zero;
	//
	//        }
	//
	//        /// <summary>
	//        /// Retrieves information on resources used by the parcel
	//        /// </summary>
	//        /// <param name="parcelID">UUID of the parcel</param>
	//        /// <param name="getDetails">Should per object resource usage be requested</param>
	//        /// <param name="callback">Callback invoked when the request is complete</param>
	//        public void GetParcelResouces(UUID parcelID, bool getDetails, LandResourcesCallback callback)
	//        {
	//            try
	//            {
	//                Uri url = Client.Network.CurrentSim.Caps.CapabilityURI("LandResources");
	//                CapsClient request = new CapsClient(url);
	//
	//                request.OnComplete += delegate(CapsClient client, OSD result, Exception error)
	//                {
	//                    try
	//                    {
	//                        if (result == null || error != null)
	//                        {
	//                            callback(false, null);
	//                        }
	//                        LandResourcesMessage response = new LandResourcesMessage();
	//                        response.Deserialize((OSDMap)result);
	//
	//                        CapsClient summaryRequest = new CapsClient(response.ScriptResourceSummary);
	//                        OSD summaryResponse = summaryRequest.GetResponse(Client.Settings.CAPS_TIMEOUT);
	//
	//                        LandResourcesInfo res = new LandResourcesInfo();
	//                        res.Deserialize((OSDMap)summaryResponse);
	//
	//                        if (response.ScriptResourceDetails != null && getDetails)
	//                        {
	//                            CapsClient detailRequest = new CapsClient(response.ScriptResourceDetails);
	//                            OSD detailResponse = detailRequest.GetResponse(Client.Settings.CAPS_TIMEOUT);
	//                            res.Deserialize((OSDMap)detailResponse);
	//                        }
	//                        callback(true, res);
	//                    }
	//                    catch (Exception ex)
	//                    {
	//                        Logger.Log("Failed fetching land resources", Helpers.LogLevel.Error, Client, ex);
	//                        callback(false, null);
	//                    }
	//                };
	//
	//                LandResourcesRequest param = new LandResourcesRequest();
	//                param.ParcelID = parcelID;
	//                request.BeginGetResponse(param.Serialize(), OSDFormat.Xml, Client.Settings.CAPS_TIMEOUT);
	//
	//            }
	//            catch (Exception ex)
	//            {
	//                Logger.Log("Failed fetching land resources:", Helpers.LogLevel.Error, Client, ex);
	//                callback(false, null);
	//            }
	//        }
	//
	//        //endregion Public Methods
	//
	//        //region Packet Handlers
	//
	//        /// <summary>Process an incoming packet and raise the appropriate events</summary>
	//        /// <param name="sender">The sender</param>
	//        /// <param name="e">The EventArgs object containing the packet data</param>
	//        /// <remarks>Raises the <see cref="ParcelDwellReply"/> event</remarks>
	//        protected void ParcelDwellReplyHandler(object sender, PacketReceivedEventArgs e)
	//        {            
	//            if (m_DwellReply != null || Client.Settings.ALWAYS_REQUEST_PARCEL_DWELL == true)
	//            {
	//                Packet packet = e.Packet;
	//                Simulator simulator = e.Simulator;
	//
	//                ParcelDwellReplyPacket dwell = (ParcelDwellReplyPacket)packet;
	//
	//                lock (simulator.Parcels.Dictionary)
	//                {
	//                    if (simulator.Parcels.Dictionary.ContainsKey(dwell.Data.LocalID))
	//                    {
	//                        Parcel parcel = simulator.Parcels.Dictionary[dwell.Data.LocalID];
	//                        parcel.Dwell = dwell.Data.Dwell;
	//                        simulator.Parcels.Dictionary[dwell.Data.LocalID] = parcel;
	//                    }
	//                }
	//
	//                if (m_DwellReply != null)
	//                {
	//                    OnParcelDwellReply(new ParcelDwellReplyEventArgs(dwell.Data.ParcelID, dwell.Data.LocalID, dwell.Data.Dwell));
	//                }
	//            }
	//        }
	//
	//        /// <summary>Process an incoming packet and raise the appropriate events</summary>
	//        /// <param name="sender">The sender</param>
	//        /// <param name="e">The EventArgs object containing the packet data</param>
	//        /// <remarks>Raises the <see cref="ParcelInfoReply"/> event</remarks>
	//        protected void ParcelInfoReplyHandler(object sender, PacketReceivedEventArgs e)
	//        {            
	//            if (m_ParcelInfo != null)
	//            {
	//                Packet packet = e.Packet;
	//                ParcelInfoReplyPacket info = (ParcelInfoReplyPacket)packet;
	//
	//                ParcelInfo parcelInfo = new ParcelInfo();
	//
	//                parcelInfo.ActualArea = info.Data.ActualArea;
	//                parcelInfo.AuctionID = info.Data.AuctionID;
	//                parcelInfo.BillableArea = info.Data.BillableArea;
	//                parcelInfo.Description = Utils.BytesToString(info.Data.Desc);
	//                parcelInfo.Dwell = info.Data.Dwell;
	//                parcelInfo.GlobalX = info.Data.GlobalX;
	//                parcelInfo.GlobalY = info.Data.GlobalY;
	//                parcelInfo.GlobalZ = info.Data.GlobalZ;
	//                parcelInfo.ID = info.Data.ParcelID;
	//                parcelInfo.Mature = ((info.Data.Flags & 1) != 0) ? true : false;
	//                parcelInfo.Name = Utils.BytesToString(info.Data.Name);
	//                parcelInfo.OwnerID = info.Data.OwnerID;
	//                parcelInfo.SalePrice = info.Data.SalePrice;
	//                parcelInfo.SimName = Utils.BytesToString(info.Data.SimName);
	//                parcelInfo.SnapshotID = info.Data.SnapshotID;
	//
	//                OnParcelInfoReply(new ParcelInfoReplyEventArgs(parcelInfo));                
	//            }
	//        }
	//
	//        protected void ParcelPropertiesReplyHandler(string capsKey, IMessage message, Simulator simulator)
	//        {                        
	//            if (m_ParcelProperties != null || Client.Settings.PARCEL_TRACKING == true)
	//            {
	//                ParcelPropertiesMessage msg = (ParcelPropertiesMessage)message;
	//                
	//                Parcel parcel = new Parcel(msg.LocalID);
	//
	//                parcel.AABBMax = msg.AABBMax;
	//                parcel.AABBMin = msg.AABBMin;
	//                parcel.Area = msg.Area;
	//                parcel.AuctionID = msg.AuctionID;
	//                parcel.AuthBuyerID = msg.AuthBuyerID;
	//                parcel.Bitmap = msg.Bitmap;
	//                parcel.Category = msg.Category;
	//                parcel.ClaimDate = msg.ClaimDate;
	//                parcel.ClaimPrice = msg.ClaimPrice;
	//                parcel.Desc = msg.Desc;
	//                parcel.Flags = msg.ParcelFlags;
	//                parcel.GroupID = msg.GroupID;
	//                parcel.GroupPrims = msg.GroupPrims;
	//                parcel.IsGroupOwned = msg.IsGroupOwned;
	//                parcel.Landing = msg.LandingType;
	//                parcel.MaxPrims = msg.MaxPrims;
	//                parcel.Media.MediaAutoScale = msg.MediaAutoScale;
	//                parcel.Media.MediaID = msg.MediaID;
	//                parcel.Media.MediaURL = msg.MediaURL;
	//                parcel.MusicURL = msg.MusicURL;
	//                parcel.Name = msg.Name;
	//                parcel.OtherCleanTime = msg.OtherCleanTime;
	//                parcel.OtherCount = msg.OtherCount;
	//                parcel.OtherPrims = msg.OtherPrims;
	//                parcel.OwnerID = msg.OwnerID;
	//                parcel.OwnerPrims = msg.OwnerPrims;
	//                parcel.ParcelPrimBonus = msg.ParcelPrimBonus;
	//                parcel.PassHours = msg.PassHours;
	//                parcel.PassPrice = msg.PassPrice;
	//                parcel.PublicCount = msg.PublicCount;
	//                parcel.RegionDenyAgeUnverified = msg.RegionDenyAgeUnverified;
	//                parcel.RegionDenyAnonymous = msg.RegionDenyAnonymous;
	//                parcel.RegionPushOverride = msg.RegionPushOverride;
	//                parcel.RentPrice = msg.RentPrice;
	//                ParcelResult result = msg.RequestResult;
	//                parcel.SalePrice = msg.SalePrice;
	//                int selectedPrims = msg.SelectedPrims;
	//                parcel.SelfCount = msg.SelfCount;
	//                int sequenceID = msg.SequenceID;
	//                parcel.SimWideMaxPrims = msg.SimWideMaxPrims;
	//                parcel.SimWideTotalPrims = msg.SimWideTotalPrims;
	//                bool snapSelection = msg.SnapSelection;
	//                parcel.SnapshotID = msg.SnapshotID;
	//                parcel.Status = msg.Status;
	//                parcel.TotalPrims = msg.TotalPrims;
	//                parcel.UserLocation = msg.UserLocation;
	//                parcel.UserLookAt = msg.UserLookAt;
	//                parcel.Media.MediaDesc = msg.MediaDesc;
	//                parcel.Media.MediaHeight = msg.MediaHeight;
	//                parcel.Media.MediaWidth = msg.MediaWidth;
	//                parcel.Media.MediaLoop = msg.MediaLoop;
	//                parcel.Media.MediaType = msg.MediaType;
	//                parcel.ObscureMedia = msg.ObscureMedia;
	//                parcel.ObscureMusic = msg.ObscureMusic;
	//
	//                if (Client.Settings.PARCEL_TRACKING)
	//                {
	//                    lock (simulator.Parcels.Dictionary)
	//                        simulator.Parcels.Dictionary[parcel.LocalID] = parcel;
	//
	//                    bool set = false;
	//                    int y, x, index, bit;
	//                    for (y = 0; y < 64; y++)
	//                    {
	//                        for (x = 0; x < 64; x++)
	//                        {
	//                            index = (y * 64) + x;
	//                            bit = index % 8;
	//                            index >>= 3;
	//
	//                            if ((parcel.Bitmap[index] & (1 << bit)) != 0)
	//                            {
	//                                simulator.ParcelMap[y, x] = parcel.LocalID;
	//                                set = true;
	//                            }
	//                        }
	//                    }
	//
	//                    if (!set)
	//                    {
	//                        Logger.Log("Received a parcel with a bitmap that did not map to any locations",
	//                            Helpers.LogLevel.Warning);
	//                    }
	//                }
	//
	//                if (sequenceID.Equals(int.MaxValue) && WaitForSimParcel != null)
	//                    WaitForSimParcel.Set();
	//
	//                // auto request acl, will be stored in parcel tracking dictionary if enabled
	//                if (Client.Settings.ALWAYS_REQUEST_PARCEL_ACL)
	//                    Client.Parcels.RequestParcelAccessList(simulator, parcel.LocalID,
	//                        AccessList.Both, sequenceID);
	//
	//                // auto request dwell, will be stored in parcel tracking dictionary if enables
	//                if (Client.Settings.ALWAYS_REQUEST_PARCEL_DWELL)
	//                    Client.Parcels.RequestDwell(simulator, parcel.LocalID);
	//
	//                // Fire the callback for parcel properties being received
	//                if (m_ParcelProperties != null)
	//                {
	//                    OnParcelProperties(new ParcelPropertiesEventArgs(simulator, parcel, result, selectedPrims, sequenceID, snapSelection));                    
	//                }
	//
	//                // Check if all of the simulator parcels have been retrieved, if so fire another callback
	//                if (simulator.IsParcelMapFull() && m_SimParcelsDownloaded != null)
	//                {
	//                    OnSimParcelsDownloaded(new SimParcelsDownloadedEventArgs(simulator, simulator.Parcels, simulator.ParcelMap));
	//                }
	//            }
	//        }
	//
	//        /// <summary>Process an incoming packet and raise the appropriate events</summary>
	//        /// <param name="sender">The sender</param>
	//        /// <param name="e">The EventArgs object containing the packet data</param>
	//        /// <remarks>Raises the <see cref="ParcelAccessListReply"/> event</remarks>
	//        protected void ParcelAccessListReplyHandler(object sender, PacketReceivedEventArgs e)
	//        {
	//            if (m_ParcelACL != null || Client.Settings.ALWAYS_REQUEST_PARCEL_ACL == true)
	//            {
	//                Packet packet = e.Packet;
	//                Simulator simulator = e.Simulator;
	//
	//                ParcelAccessListReplyPacket reply = (ParcelAccessListReplyPacket)packet;
	//
	//                List<ParcelAccessEntry> accessList = new List<ParcelAccessEntry>(reply.List.Length);
	//                   
	//                    for (int i = 0; i < reply.List.Length; i++)
	//                    {
	//                        ParcelAccessEntry pae = new ParcelAccessEntry();
	//                        pae.AgentID = reply.List[i].ID;
	//                        pae.Time = Utils.UnixTimeToDateTime((uint)reply.List[i].Time);
	//                        pae.Flags = (AccessList)reply.List[i].Flags;
	//
	//                        accessList.Add(pae);
	//                    }
	//
	//                    lock (simulator.Parcels.Dictionary)
	//                    {
	//                        if (simulator.Parcels.Dictionary.ContainsKey(reply.Data.LocalID))
	//                        {
	//                            Parcel parcel = simulator.Parcels.Dictionary[reply.Data.LocalID];
	//                            if ((AccessList)reply.Data.Flags == AccessList.Ban)
	//                                parcel.AccessBlackList = accessList;
	//                            else
	//                                parcel.AccessWhiteList = accessList;
	//
	//                            simulator.Parcels.Dictionary[reply.Data.LocalID] = parcel;
	//                        }
	//                    }
	//                
	//
	//                if (m_ParcelACL != null)
	//                {
	//                    OnParcelAccessListReply(new ParcelAccessListReplyEventArgs(simulator, reply.Data.SequenceID, reply.Data.LocalID, 
	//                        reply.Data.Flags, accessList));                    
	//                }
	//            }
	//        }
	//        
	//        protected void ParcelObjectOwnersReplyHandler(string capsKey, IMessage message, Simulator simulator)
	//        {
	//            if (m_ParcelObjectOwnersReply != null)
	//            {
	//                List<ParcelPrimOwners> primOwners = new List<ParcelPrimOwners>();
	//
	//                ParcelObjectOwnersReplyMessage msg = (ParcelObjectOwnersReplyMessage)message;
	//                
	//                for (int i = 0; i < msg.PrimOwnersBlock.Length; i++)
	//                {
	//                    ParcelPrimOwners primOwner = new ParcelPrimOwners();
	//                    primOwner.OwnerID = msg.PrimOwnersBlock[i].OwnerID;
	//                    primOwner.Count = msg.PrimOwnersBlock[i].Count;
	//                    primOwner.IsGroupOwned = msg.PrimOwnersBlock[i].IsGroupOwned;
	//                    primOwner.OnlineStatus = msg.PrimOwnersBlock[i].OnlineStatus;
	//                    primOwner.NewestPrim = msg.PrimOwnersBlock[i].TimeStamp;
	//
	//                    primOwners.Add(primOwner);
	//                }
	//
	//                OnParcelObjectOwnersReply(new ParcelObjectOwnersReplyEventArgs(simulator, primOwners));
	//            }                
	//        }
	//
	//        /// <summary>Process an incoming packet and raise the appropriate events</summary>
	//        /// <param name="sender">The sender</param>
	//        /// <param name="e">The EventArgs object containing the packet data</param>
	//        /// <remarks>Raises the <see cref="ForceSelectObjectsReply"/> event</remarks>
	//        protected void SelectParcelObjectsReplyHandler(object sender, PacketReceivedEventArgs e)
	//        {
	//            if (m_ForceSelectObjects != null)
	//            {
	//                Packet packet = e.Packet;
	//                Simulator simulator = e.Simulator;
	//
	//                ForceObjectSelectPacket reply = (ForceObjectSelectPacket)packet;
	//                List<uint> objectIDs = new List<uint>(reply.Data.Length);
	//
	//                for (int i = 0; i < reply.Data.Length; i++)
	//                {
	//                    objectIDs.Add(reply.Data[i].LocalID);
	//                }
	//
	//                OnForceSelectObjectsReply(new ForceSelectObjectsReplyEventArgs(simulator, objectIDs, reply._Header.ResetList));
	//            }
	//        }
	//
	//        /// <summary>Process an incoming packet and raise the appropriate events</summary>
	//        /// <param name="sender">The sender</param>
	//        /// <param name="e">The EventArgs object containing the packet data</param>
	//        /// <remarks>Raises the <see cref="ParcelMediaUpdateReply"/> event</remarks>
	//        protected void ParcelMediaUpdateHandler(object sender, PacketReceivedEventArgs e)
	//        {
	//            if (m_ParcelMediaUpdateReply != null)
	//            {
	//                Packet packet = e.Packet;
	//                Simulator simulator = e.Simulator;
	//
	//                ParcelMediaUpdatePacket reply = (ParcelMediaUpdatePacket)packet;
	//                ParcelMedia media = new ParcelMedia();
	//
	//                media.MediaAutoScale = (reply.DataBlock.MediaAutoScale == (byte)0x1) ? true : false;
	//                media.MediaID = reply.DataBlock.MediaID;
	//                media.MediaDesc = Utils.BytesToString(reply.DataBlockExtended.MediaDesc);
	//                media.MediaHeight = reply.DataBlockExtended.MediaHeight;
	//                media.MediaLoop = ((reply.DataBlockExtended.MediaLoop & 1) != 0) ? true : false;
	//                media.MediaType = Utils.BytesToString(reply.DataBlockExtended.MediaType);
	//                media.MediaWidth = reply.DataBlockExtended.MediaWidth;
	//                media.MediaURL = Utils.BytesToString(reply.DataBlock.MediaURL);
	//
	//                OnParcelMediaUpdateReply(new ParcelMediaUpdateReplyEventArgs(simulator, media));
	//            }
	//        }
	//
	//        /// <summary>Process an incoming packet and raise the appropriate events</summary>
	//        /// <param name="sender">The sender</param>
	//        /// <param name="e">The EventArgs object containing the packet data</param>
	//        protected void ParcelOverlayHandler(object sender, PacketReceivedEventArgs e)
	//        {
	//            const int OVERLAY_COUNT = 4;
	//            Packet packet = e.Packet;
	//            Simulator simulator = e.Simulator;
	//
	//            ParcelOverlayPacket overlay = (ParcelOverlayPacket)packet;
	//
	//            if (overlay.ParcelData.SequenceID >= 0 && overlay.ParcelData.SequenceID < OVERLAY_COUNT)
	//            {
	//                int length = overlay.ParcelData.Data.Length;
	//
	//                Buffer.BlockCopy(overlay.ParcelData.Data, 0, simulator.ParcelOverlay,
	//                    overlay.ParcelData.SequenceID * length, length);
	//                simulator.ParcelOverlaysReceived++;
	//
	//                if (simulator.ParcelOverlaysReceived >= OVERLAY_COUNT)
	//                {
	//                    // TODO: ParcelOverlaysReceived should become internal, and reset to zero every 
	//                    // time it hits four. Also need a callback here
	//                }
	//            }
	//            else
	//            {
	//                Logger.Log("Parcel overlay with sequence ID of " + overlay.ParcelData.SequenceID +
	//                    " received from " + simulator.ToString(), Helpers.LogLevel.Warning, Client);
	//            }
	//        }
	//
	//        /// <summary>Process an incoming packet and raise the appropriate events</summary>
	//        /// <param name="sender">The sender</param>
	//        /// <param name="e">The EventArgs object containing the packet data</param>
	//        /// <remarks>Raises the <see cref="ParcelMediaCommand"/> event</remarks>
	//        protected void ParcelMediaCommandMessagePacketHandler(object sender, PacketReceivedEventArgs e)
	//        {
	//            if (m_ParcelMediaCommand != null)
	//            {
	//                Packet packet = e.Packet;
	//                Simulator simulator = e.Simulator;
	//
	//                ParcelMediaCommandMessagePacket pmc = (ParcelMediaCommandMessagePacket)packet;
	//                ParcelMediaCommandMessagePacket.CommandBlockBlock block = pmc.CommandBlock;
	//
	//                OnParcelMediaCommand(new ParcelMediaCommandEventArgs(simulator, pmc.Header.Sequence, (ParcelFlags)block.Flags, 
	//                    (ParcelMediaCommand)block.Command, block.Time));                
	//            }
	//        }
	//
	//        //endregion Packet Handlers
	//    }
	//    //region EventArgs classes
	//    
	//    /// <summary>Contains a parcels dwell data returned from the simulator in response to an <see cref="RequestParcelDwell"/></summary>
	//    public class ParcelDwellReplyEventArgs : EventArgs
	//    {
	//        private readonly UUID m_ParcelID;
	//        private readonly int m_LocalID;
	//        private readonly float m_Dwell;
	//
	//        /// <summary>Get the global ID of the parcel</summary>
	//        public UUID ParcelID { get { return m_ParcelID; } }
	//        /// <summary>Get the simulator specific ID of the parcel</summary>
	//        public int LocalID { get { return m_LocalID; } }
	//        /// <summary>Get the calculated dwell</summary>
	//        public float Dwell { get { return m_Dwell; } }
	//
	//        /// <summary>
	//        /// Construct a new instance of the ParcelDwellReplyEventArgs class
	//        /// </summary>
	//        /// <param name="parcelID">The global ID of the parcel</param>
	//        /// <param name="localID">The simulator specific ID of the parcel</param>
	//        /// <param name="dwell">The calculated dwell for the parcel</param>
	//        public ParcelDwellReplyEventArgs(UUID parcelID, int localID, float dwell)
	//        {
	//            this.m_ParcelID = parcelID;
	//            this.m_LocalID = localID;
	//            this.m_Dwell = dwell;
	//        }
	//    }
	//
	//    /// <summary>Contains basic parcel information data returned from the 
	//    /// simulator in response to an <see cref="RequestParcelInfo"/> request</summary>
	//    public class ParcelInfoReplyEventArgs : EventArgs
	//    {        
	//        private readonly ParcelInfo m_Parcel;
	//
	//        /// <summary>Get the <see cref="ParcelInfo"/> object containing basic parcel info</summary>
	//        public ParcelInfo Parcel { get { return m_Parcel; } }
	//
	//        /// <summary>
	//        /// Construct a new instance of the ParcelInfoReplyEventArgs class
	//        /// </summary>
	//        /// <param name="parcel">The <see cref="ParcelInfo"/> object containing basic parcel info</param>
	//        public ParcelInfoReplyEventArgs(ParcelInfo parcel)
	//        {
	//            this.m_Parcel = parcel;
	//        }
	//    }
	//
	//    /// <summary>Contains basic parcel information data returned from the simulator in response to an <see cref="RequestParcelInfo"/> request</summary>
	//    public class ParcelPropertiesEventArgs : EventArgs
	//    {
	//        private readonly Simulator m_Simulator;
	//        private Parcel m_Parcel;
	//        private readonly ParcelResult m_Result;
	//        private readonly int m_SelectedPrims;
	//        private readonly int m_SequenceID;
	//        private readonly bool m_SnapSelection;
	//
	//        /// <summary>Get the simulator the parcel is located in</summary>
	//        public Simulator Simulator { get { return m_Simulator; } }
	//        /// <summary>Get the <see cref="Parcel"/> object containing the details</summary>
	//        /// <remarks>If Result is NoData, this object will not contain valid data</remarks>
	//        public Parcel Parcel { get { return m_Parcel; } }
	//        /// <summary>Get the result of the request</summary>
	//        public ParcelResult Result { get { return m_Result; } }
	//        /// <summary>Get the number of primitieves your agent is 
	//        /// currently selecting and or sitting on in this parcel</summary>
	//        public int SelectedPrims { get { return m_SelectedPrims; } }
	//        /// <summary>Get the user assigned ID used to correlate a request with
	//        /// these results</summary>
	//        public int SequenceID { get { return m_SequenceID; } }
	//        /// <summary>TODO:</summary>
	//        public bool SnapSelection { get { return m_SnapSelection; } }
	//
	//        /// <summary>
	//        /// Construct a new instance of the ParcelPropertiesEventArgs class
	//        /// </summary>
	//        /// <param name="simulator">The <see cref="Parcel"/> object containing the details</param>
	//        /// <param name="parcel">The <see cref="Parcel"/> object containing the details</param>
	//        /// <param name="result">The result of the request</param>
	//        /// <param name="selectedPrims">The number of primitieves your agent is 
	//        /// currently selecting and or sitting on in this parcel</param>
	//        /// <param name="sequenceID">The user assigned ID used to correlate a request with
	//        /// these results</param>
	//        /// <param name="snapSelection">TODO:</param>
	//        public ParcelPropertiesEventArgs(Simulator simulator, Parcel parcel, ParcelResult result, int selectedPrims,
	//            int sequenceID, bool snapSelection)
	//        {
	//            this.m_Simulator = simulator;
	//            this.m_Parcel = parcel;
	//            this.m_Result = result;
	//            this.m_SelectedPrims = selectedPrims;
	//            this.m_SequenceID = sequenceID;
	//            this.m_SnapSelection = snapSelection;
	//        }
	//    }
	//    
	//    /// <summary>Contains blacklist and whitelist data returned from the simulator in response to an <see cref="RequestParcelAccesslist"/> request</summary>
	//    public class ParcelAccessListReplyEventArgs : EventArgs
	//    {
	//        private readonly Simulator m_Simulator;
	//        private readonly int m_SequenceID;
	//        private readonly int m_LocalID;
	//        private readonly uint m_Flags;
	//        private readonly List<ParcelManager.ParcelAccessEntry> m_AccessList;
	//
	//        /// <summary>Get the simulator the parcel is located in</summary>
	//        public Simulator Simulator { get { return m_Simulator; } }
	//        /// <summary>Get the user assigned ID used to correlate a request with
	//        /// these results</summary>
	//        public int SequenceID { get { return m_SequenceID; } }
	//        /// <summary>Get the simulator specific ID of the parcel</summary>
	//        public int LocalID { get { return m_LocalID; } }
	//        /// <summary>TODO:</summary>
	//        public uint Flags { get { return m_Flags; } }
	//        /// <summary>Get the list containing the white/blacklisted agents for the parcel</summary>
	//        public List<ParcelManager.ParcelAccessEntry> AccessList { get { return m_AccessList; } }
	//
	//        /// <summary>
	//        /// Construct a new instance of the ParcelAccessListReplyEventArgs class
	//        /// </summary>
	//        /// <param name="simulator">The simulator the parcel is located in</param>
	//        /// <param name="sequenceID">The user assigned ID used to correlate a request with
	//        /// these results</param>
	//        /// <param name="localID">The simulator specific ID of the parcel</param>
	//        /// <param name="flags">TODO:</param>
	//        /// <param name="accessEntries">The list containing the white/blacklisted agents for the parcel</param>
	//        public ParcelAccessListReplyEventArgs(Simulator simulator, int sequenceID, int localID, uint flags, List<ParcelManager.ParcelAccessEntry> accessEntries)
	//        {
	//            this.m_Simulator = simulator;
	//            this.m_SequenceID = sequenceID;
	//            this.m_LocalID = localID;
	//            this.m_Flags = flags;
	//            this.m_AccessList = accessEntries;
	//        }
	//    }
	//    
	//    /// <summary>Contains blacklist and whitelist data returned from the 
	//    /// simulator in response to an <see cref="RequestParcelAccesslist"/> request</summary>
	//    public class ParcelObjectOwnersReplyEventArgs : EventArgs
	//    {
	//        private readonly Simulator m_Simulator;
	//        private readonly List<ParcelManager.ParcelPrimOwners> m_Owners;
	//
	//        /// <summary>Get the simulator the parcel is located in</summary>
	//        public Simulator Simulator { get { return m_Simulator; } }
	//        /// <summary>Get the list containing prim ownership counts</summary>
	//        public List<ParcelManager.ParcelPrimOwners> PrimOwners { get { return m_Owners; } }
	//
	//        /// <summary>
	//        /// Construct a new instance of the ParcelObjectOwnersReplyEventArgs class
	//        /// </summary>
	//        /// <param name="simulator">The simulator the parcel is located in</param>
	//        /// <param name="primOwners">The list containing prim ownership counts</param>
	//        public ParcelObjectOwnersReplyEventArgs(Simulator simulator, List<ParcelManager.ParcelPrimOwners> primOwners)
	//        {
	//            this.m_Simulator = simulator;
	//            this.m_Owners = primOwners;
	//        }
	//    }
	//
	//    /// <summary>Contains the data returned when all parcel data has been retrieved from a simulator</summary>
	//    public class SimParcelsDownloadedEventArgs : EventArgs
	//    {
	//        private readonly Simulator m_Simulator;
	//        private readonly InternalDictionary<int, Parcel> m_Parcels;
	//        private readonly int[,] m_ParcelMap;
	//
	//        /// <summary>Get the simulator the parcel data was retrieved from</summary>
	//        public Simulator Simulator { get { return m_Simulator; } }
	//        /// <summary>A dictionary containing the parcel data where the key correlates to the ParcelMap entry</summary>
	//        public InternalDictionary<int, Parcel> Parcels { get { return m_Parcels; } }
	//        /// <summary>Get the multidimensional array containing a x,y grid mapped
	//        /// to each 64x64 parcel's LocalID.</summary>
	//        public int[,] ParcelMap { get { return m_ParcelMap; } }
	//
	//        /// <summary>
	//        /// Construct a new instance of the SimParcelsDownloadedEventArgs class
	//        /// </summary>
	//        /// <param name="simulator">The simulator the parcel data was retrieved from</param>
	//        /// <param name="simParcels">The dictionary containing the parcel data</param>
	//        /// <param name="parcelMap">The multidimensional array containing a x,y grid mapped
	//        /// to each 64x64 parcel's LocalID.</param>
	//        public SimParcelsDownloadedEventArgs(Simulator simulator, InternalDictionary<int, Parcel> simParcels, int[,] parcelMap)
	//        {
	//            this.m_Simulator = simulator;
	//            this.m_Parcels = simParcels;
	//            this.m_ParcelMap = parcelMap;
	//        }
	//    }
	//    
	//    /// <summary>Contains the data returned when a <see cref="RequestForceSelectObjects"/> request</summary>
	//    public class ForceSelectObjectsReplyEventArgs : EventArgs
	//    {
	//        private readonly Simulator m_Simulator;
	//        private readonly List<uint> m_ObjectIDs;
	//        private readonly bool m_ResetList;
	//
	//        /// <summary>Get the simulator the parcel data was retrieved from</summary>
	//        public Simulator Simulator { get { return m_Simulator; } }
	//        /// <summary>Get the list of primitive IDs</summary>
	//        public List<uint> ObjectIDs { get { return m_ObjectIDs; } }
	//        /// <summary>true if the list is clean and contains the information
	//        /// only for a given request</summary>
	//        public bool ResetList { get { return m_ResetList; } }
	//
	//        /// <summary>
	//        /// Construct a new instance of the ForceSelectObjectsReplyEventArgs class
	//        /// </summary>
	//        /// <param name="simulator">The simulator the parcel data was retrieved from</param>
	//        /// <param name="objectIDs">The list of primitive IDs</param>
	//        /// <param name="resetList">true if the list is clean and contains the information
	//        /// only for a given request</param>
	//        public ForceSelectObjectsReplyEventArgs(Simulator simulator, List<uint> objectIDs, bool resetList)
	//        {
	//            this.m_Simulator = simulator;
	//            this.m_ObjectIDs = objectIDs;
	//            this.m_ResetList = resetList;
	//        }
	//    }
	//   
	//    /// <summary>Contains data when the media data for a parcel the avatar is on changes</summary>
	//    public class ParcelMediaUpdateReplyEventArgs : EventArgs
	//    {
	//        private readonly Simulator m_Simulator;
	//        private readonly ParcelMedia m_ParcelMedia;
	//
	//        /// <summary>Get the simulator the parcel media data was updated in</summary>
	//        public Simulator Simulator { get { return m_Simulator; } }
	//        /// <summary>Get the updated media information</summary>
	//        public ParcelMedia Media { get { return m_ParcelMedia; } }
	//        
	//        /// <summary>
	//        /// Construct a new instance of the ParcelMediaUpdateReplyEventArgs class
	//        /// </summary>
	//        /// <param name="simulator">the simulator the parcel media data was updated in</param>
	//        /// <param name="media">The updated media information</param>
	//        public ParcelMediaUpdateReplyEventArgs(Simulator simulator, ParcelMedia media)
	//        {
	//            this.m_Simulator = simulator;
	//            this.m_ParcelMedia = media;
	//        }
	//    }
	//
	//    /// <summary>Contains the media command for a parcel the agent is currently on</summary>
	//    public class ParcelMediaCommandEventArgs : EventArgs
	//    {
	//        private readonly Simulator m_Simulator;
	//        private readonly uint m_Sequence;
	//        private readonly ParcelFlags m_ParcelFlags;
	//        private readonly ParcelMediaCommand m_MediaCommand;
	//        private readonly float m_Time;
	//
	//        /// <summary>Get the simulator the parcel media command was issued in</summary>
	//        public Simulator Simulator { get { return m_Simulator; } }
	//        /// <summary></summary>
	//        public uint Sequence { get { return m_Sequence; } }
	//        /// <summary></summary>
	//        public ParcelFlags ParcelFlags { get { return m_ParcelFlags; } }
	//        /// <summary>Get the media command that was sent</summary>
	//        public ParcelMediaCommand MediaCommand { get { return m_MediaCommand; } }
	//        /// <summary></summary>
	//        public float Time { get { return m_Time; } }
	//
	//        /// <summary>
	//        /// Construct a new instance of the ParcelMediaCommandEventArgs class
	//        /// </summary>
	//        /// <param name="simulator">The simulator the parcel media command was issued in</param>
	//        /// <param name="sequence"></param>
	//        /// <param name="flags"></param>
	//        /// <param name="command">The media command that was sent</param>
	//        /// <param name="time"></param>
	//        public ParcelMediaCommandEventArgs(Simulator simulator, uint sequence, ParcelFlags flags, ParcelMediaCommand command, float time)
	//        {
	//            this.m_Simulator = simulator;
	//            this.m_Sequence = sequence;
	//            this.m_ParcelFlags = flags;
	//            this.m_MediaCommand = command;
	//            this.m_Time = time;
	//        }
	//    }
	//    //endregion


//	/// <summary>
//	/// Category parcel is listed in under search
//	/// </summary>
//	public static enum ParcelCategory 
//	{
//		/// <summary>No assigned category</summary>
//		None((byte)0),
//		/// <summary>Linden Infohub or public area</summary>
//		Linden((byte)1),
//		/// <summary>Adult themed area</summary>
//		Adult((byte)2),
//		/// <summary>Arts and Culture</summary>
//		Arts((byte)3),
//		/// <summary>Business</summary>
//		Business((byte)4),
//		/// <summary>Educational</summary>
//		Educational((byte)5),
//		/// <summary>Gaming</summary>
//		Gaming((byte)6),
//		/// <summary>Hangout or Club</summary>
//		Hangout((byte)7),
//		/// <summary>Newcomer friendly</summary>
//		Newcomer((byte)8),
//		/// <summary>Parks and Nature</summary>
//		Park((byte)9),
//		/// <summary>Residential</summary>
//		Residential((byte)10),
//		/// <summary>Shopping</summary>
//		Shopping((byte)11),
//		/// <summary>Not Used?</summary>
//		Stage((byte)12),
//		/// <summary>Other</summary>
//		Other((byte)13),
//		/// <summary>Not an actual category, only used for queries</summary>
//		Any((byte)-1);
//
//		private byte index;
//		ParcelCategory(byte index)
//		{
//			this.index = index;
//		}     
//
//		public byte getIndex()
//		{
//			return index;
//		}
//
//		//    		private static final Map<Byte,SaleType> lookup  = new HashMap<Byte,SaleType>();
//		//
//		//    		static {
//		//    			for(SaleType s : EnumSet.allOf(SaleType.class))
//		//    				lookup.put(s.getIndex(), s);
//		//    		}
//		//
//		//    		public static SaleType get(Byte index)
//		//    		{
//		//    			return lookup.get(index);
//		//    		}	
//	}
}
