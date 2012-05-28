package com.ngt.jopenmetaverse.shared.protocol.primitives;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import com.ngt.jopenmetaverse.shared.structureddata.OSD;
import com.ngt.jopenmetaverse.shared.structureddata.OSDMap;

/// <summary>
/// 
/// </summary>
//[Serializable()]
public class Permissions
{
	public PermissionMask BaseMask;
	public PermissionMask EveryoneMask;
	public PermissionMask GroupMask;
	public PermissionMask NextOwnerMask;
	public PermissionMask OwnerMask;

	/// <summary>
	/// 
	/// </summary>

	public enum PermissionMask
	{
		None   ((long)0),
		Transfer    ((long)1 << 13),
		Modify      ((long)1 << 14),
		Copy        ((long)1 << 15),
		//[Obsolete]
				//EnterParcel = 1 << 16,
		//[Obsolete]
		//Terraform   = 1 << 17,
		//[Obsolete]
		//OwnerDebit  = 1 << 18,
		Move        ((long)1 << 19),
		Damage      ((long)1 << 20),
		All         ((long)0x7FFFFFFF);

		private long index;
		private static final Map<Long,PermissionMask> lookup  = new HashMap<Long,PermissionMask>();

		static {
			for(PermissionMask s : EnumSet.allOf(PermissionMask.class))
				lookup.put(s.getIndex(), s);
		}

		public static PermissionMask get(long index)
		{
			return lookup.get(index);
		}
		
		PermissionMask(long index)
		{
			this.index = index;
		}     

		public long getIndex()
		{
			return index;
		}
	}

	/// <summary>
	/// 
	/// </summary>

	public enum PermissionWho 
	{
		/// <summary></summary>
		Base ((byte)0x01),
		/// <summary></summary>
		Owner ((byte)0x02),
		/// <summary></summary>
		Group ((byte)0x04),
		/// <summary></summary>
		Everyone ((byte)0x08),
		/// <summary></summary>
		NextOwner ((byte)0x10),
		/// <summary></summary>
		All ((byte)0x1F);

		private byte index;
		private static final Map<Byte,PermissionWho> lookup  = new HashMap<Byte,PermissionWho>();

		static {
			for(PermissionWho s : EnumSet.allOf(PermissionWho.class))
				lookup.put(s.getIndex(), s);
		}

		PermissionWho(byte index)
		{
			this.index = index;
		}     

		public byte getIndex()
		{
			return index;
		}

		public static PermissionWho get(byte index)
		{
			return lookup.get(index);
		}
	}


	public Permissions()
	{
		this(0,0,0,0,0);
	}
	
	public Permissions(long baseMask, long everyoneMask, long groupMask, long nextOwnerMask, long ownerMask)
	{
		BaseMask = PermissionMask.get(baseMask);
		EveryoneMask = PermissionMask.get(everyoneMask);
		GroupMask = PermissionMask.get(groupMask);
		NextOwnerMask = PermissionMask.get(nextOwnerMask);
		OwnerMask = PermissionMask.get(ownerMask);
	}

	public Permissions GetNextPermissions()
	{
		long nextMask = (long)NextOwnerMask.getIndex();

		return new Permissions(
				(long)BaseMask.getIndex() & nextMask,
				(long)EveryoneMask.getIndex() & nextMask,
				(long)GroupMask.getIndex() & nextMask,
				(long)NextOwnerMask.getIndex(),
				(long)OwnerMask.getIndex() & nextMask
				);
	}

	public OSD GetOSD()
	{
		OSDMap permissions = new OSDMap(5);
		permissions.put("base_mask", OSD.FromLong((long)BaseMask.getIndex()));
		permissions.put("everyone_mask", OSD.FromLong((long)EveryoneMask.getIndex()));
		permissions.put("group_mask", OSD.FromLong((long)GroupMask.getIndex()));
		permissions.put("next_owner_mask", OSD.FromLong((long)NextOwnerMask.getIndex()));
		permissions.put("owner_mask", OSD.FromLong((long)OwnerMask.getIndex()));
		return permissions;
	}

	public static Permissions FromOSD(OSD llsd)
	{
		Permissions permissions = new Permissions();
		OSDMap map =  (OSDMap) llsd;

		if (map != null)
		{
			permissions.BaseMask = PermissionMask.get(map.get("base_mask").asLong());
			permissions.EveryoneMask = PermissionMask.get(map.get("everyone_mask").asLong());
			permissions.GroupMask = PermissionMask.get(map.get("group_mask").asLong());
			permissions.NextOwnerMask = PermissionMask.get(map.get("next_owner_mask").asLong());
			permissions.OwnerMask = PermissionMask.get(map.get("owner_mask").asLong());
		}

		return permissions;
	}

	public String ToString()
	{
		return String.format("Base: %d, Everyone: %d, Group: %d, NextOwner: %d, Owner: %d",
				BaseMask, EveryoneMask, GroupMask, NextOwnerMask, OwnerMask);
	}

	public  int hashCode()
	{
		return BaseMask.hashCode() ^ EveryoneMask.hashCode() ^ GroupMask.hashCode() ^
				NextOwnerMask.hashCode() ^ OwnerMask.hashCode();
	}

	public boolean equals(Object obj)
	{
		return (obj instanceof Permissions) ? this == (Permissions)obj : false;
	}

	public boolean equals(Permissions other)
	{
		return equals(this, other);
	}

	public static boolean equals(Permissions lhs, Permissions rhs)
			{
		return (lhs.BaseMask == rhs.BaseMask) && (lhs.EveryoneMask == rhs.EveryoneMask) &&
				(lhs.GroupMask == rhs.GroupMask) && (lhs.NextOwnerMask == rhs.NextOwnerMask) &&
				(lhs.OwnerMask == rhs.OwnerMask);
			}

	public static boolean notEquals(Permissions lhs, Permissions rhs)
			{
		return !equals(lhs, rhs);
			}

	public static boolean hasPermissions(PermissionMask perms, PermissionMask checkPerms)
	{
		return (perms.getIndex() & checkPerms.getIndex()) == checkPerms.getIndex();
	}

	public static final Permissions NoPermissions = new Permissions();
	public static final Permissions FullPermissions = new Permissions((long)PermissionMask.All.getIndex(), (long)PermissionMask.All.getIndex(),
			(long)PermissionMask.All.getIndex(), (long)PermissionMask.All.getIndex(), (long)PermissionMask.All.getIndex());
}
