package com.ngt.jopenmetaverse.shared.sim;

import java.io.Serializable;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import com.ngt.jopenmetaverse.shared.structureddata.OSD;
import com.ngt.jopenmetaverse.shared.structureddata.OSDMap;

public class Permissions implements Serializable
{
	/// <summary>
	/// 
	/// </summary>

	public static enum PermissionMask
	{
		None((long)0),
		Transfer((long)1 << 13),
		Modify((long)1 << 14),
		Copy((long)1 << 15),
		//[Obsolete]
				//EnterParcel = 1 << 16,
		//[Obsolete]
		//Terraform   = 1 << 17,
		//[Obsolete]
		//OwnerDebit  = 1 << 18,
		Move((long)1 << 19),
		Damage((long)1 << 20),
		All((long)0x7FFFFFFF);
		private long index;
		PermissionMask(long index)
		{
			this.index = index;
		}     

		public long getIndex()
		{
			return index;
		}

		private static final Map<Long,PermissionMask> lookup  = new HashMap<Long,PermissionMask>();

		static {
			for(PermissionMask s : EnumSet.allOf(PermissionMask.class))
				lookup.put(s.getIndex(), s);
		}

		public static PermissionMask get(Long index)
		{
			return lookup.get(index);
		}
	}

	/// <summary>
	/// 
	/// </summary>

	public static enum PermissionWho
	{
		/// <summary></summary>
		Base((short)0x01),
		/// <summary></summary>
		Owner((short)0x02),
		/// <summary></summary>
		Group((short)0x04),
		/// <summary></summary>
		Everyone((short)0x08),
		/// <summary></summary>
		NextOwner((short)0x10),
		/// <summary></summary>
		All((short)0x1F);

		private short index;
		PermissionWho(short index)
		{
			this.index = index;
		}     

		public short getIndex()
		{
			return index;
		}

		private static final Map<Short,PermissionWho> lookup  = new HashMap<Short,PermissionWho>();

		static {
			for(PermissionWho s : EnumSet.allOf(PermissionWho.class))
				lookup.put(s.getIndex(), s);
		}

		public static PermissionWho get(Short index)
		{
			return lookup.get(index);
		}
	}

	public PermissionMask BaseMask;
	public PermissionMask EveryoneMask;
	public PermissionMask GroupMask;
	public PermissionMask NextOwnerMask;
	public PermissionMask OwnerMask;

	public Permissions()
	{
		
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
		long nextMask = NextOwnerMask.getIndex();

		return new Permissions(
				BaseMask.getIndex() & nextMask,
				EveryoneMask.getIndex() & nextMask,
				GroupMask.getIndex() & nextMask,
				NextOwnerMask.getIndex(),
				OwnerMask.getIndex() & nextMask
				);
	}

	public OSD GetOSD()
	{
		OSDMap permissions = new OSDMap(5);
		permissions.put("base_mask", OSD.FromInteger((int)BaseMask.getIndex()));
		permissions.put("everyone_mask",  OSD.FromInteger((int)EveryoneMask.getIndex()));
		permissions.put("group_mask", OSD.FromInteger((int)GroupMask.getIndex()));
		permissions.put("next_owner_mask",  OSD.FromInteger((int)NextOwnerMask.getIndex()));
		permissions.put("owner_mask", OSD.FromInteger((int)OwnerMask.getIndex()));
		return permissions;
	}

	public static Permissions FromOSD(OSD llsd)
	{
		Permissions permissions = new Permissions();
		OSDMap map = (OSDMap)llsd;

		if (map != null)
		{
			permissions.BaseMask = PermissionMask.get(map.get("base_mask").asUInteger());
			permissions.EveryoneMask = PermissionMask.get(map.get("everyone_mask").asUInteger());
			permissions.GroupMask = PermissionMask.get(map.get("group_mask").asUInteger());
			permissions.NextOwnerMask = PermissionMask.get(map.get("next_owner_mask").asUInteger());
			permissions.OwnerMask = PermissionMask.get(map.get("owner_mask").asUInteger());
		}

		return permissions;
	}

	@Override
	public String toString()
	{
		return String.format("Base: %d, Everyone: %d, Group: %d, NextOwner: %d, Owner: %d",
				BaseMask, EveryoneMask, GroupMask, NextOwnerMask, OwnerMask);
	}

	@Override
	public int hashCode()
	{
		return BaseMask.hashCode() ^ EveryoneMask.hashCode() ^ GroupMask.hashCode() ^
				NextOwnerMask.hashCode() ^ OwnerMask.hashCode();
	}

	@Override
	public boolean equals(Object obj)
	{	
		Permissions rhs;
		if(obj instanceof Permissions)
			rhs = (Permissions)obj;
		else
			return false;
		
		return  (this.BaseMask.getIndex() == rhs.BaseMask.getIndex()) 
				&& (this.EveryoneMask.getIndex() == rhs.EveryoneMask.getIndex()) &&
				(this.GroupMask.getIndex() == rhs.GroupMask.getIndex()) 
				&& (this.NextOwnerMask.getIndex() == rhs.NextOwnerMask.getIndex()) &&
				(this.OwnerMask.getIndex() == rhs.OwnerMask.getIndex());
	}

	public boolean Equals(Permissions other)
	{
		return this == other;
	}

	public static boolean hasPermissions(PermissionMask perms, PermissionMask checkPerms)
	{
		return (perms.getIndex() & checkPerms.getIndex()) == checkPerms.getIndex();
	}

	public static final Permissions NoPermissions = new Permissions();
	public static final Permissions FullPermissions = new Permissions((long)PermissionMask.All.getIndex(), PermissionMask.All.getIndex(),
			PermissionMask.All.getIndex(), PermissionMask.All.getIndex(), PermissionMask.All.getIndex());
}
