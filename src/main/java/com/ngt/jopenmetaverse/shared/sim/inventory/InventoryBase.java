package com.ngt.jopenmetaverse.shared.sim.inventory;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.ngt.jopenmetaverse.shared.types.UUID;
import com.ngt.jopenmetaverse.shared.util.JLogger;

/// <summary>
/// Base Class for Inventory Items
/// </summary>
//	    [Serializable()]
public abstract class InventoryBase implements Serializable
{
	/// <summary><seealso cref="OpenMetaverse.UUID"/> of item/folder</summary>
	public UUID UUID;
	/// <summary><seealso cref="OpenMetaverse.UUID"/> of parent folder</summary>
	public UUID ParentUUID;
	/// <summary>Name of item/folder</summary>
	public String Name;
	/// <summary>Item/Folder Owners <seealso cref="OpenMetaverse.UUID"/></summary>
	public UUID OwnerID;

	/// <summary>
	/// Constructor, takes an itemID as a parameter
	/// </summary>
	/// <param name="itemID">The <seealso cref="OpenMetaverse.UUID"/> of the item</param>
	public InventoryBase(UUID itemID)
	{
		if (itemID.equals(UUID.Zero))
			JLogger.warn("Initializing an InventoryBase with UUID.Zero");
		UUID = itemID;
	}

	/// <summary>
	/// 
	/// </summary>
	/// <returns></returns>
	//virtual
	public Map<String, Object> getObjectData()
	{
		Map<String, Object> info = new HashMap<String, Object>(); 
		info.put("UUID", UUID);
		info.put("ParentUUID", ParentUUID);
		info.put("Name", Name);
		info.put("OwnerID", OwnerID);
		return info;
	}

	/// <summary>
	/// 
	/// </summary>
	/// <returns></returns>
	public InventoryBase(Map<String, Object> info)
	{
		UUID = (UUID)info.get("UUID");
		ParentUUID = (UUID)info.get("ParentUUID");
		Name = (String)info.get("Name");
		OwnerID = (UUID)info.get("OwnerID");
	}

	/// <summary>
	/// Generates a number corresponding to the value of the object to support the use of a hash table,
	/// suitable for use in hashing algorithms and data structures such as a hash table
	/// </summary>
	/// <returns>A Hashcode of all the combined InventoryBase fields</returns>
	@Override
	public int hashCode()
	{
		return UUID.GetHashCode() ^ ParentUUID.GetHashCode() ^ Name.hashCode() ^ OwnerID.GetHashCode();
	}

	/// <summary>
	/// Determine whether the specified <seealso cref="OpenMetaverse.InventoryBase"/> object is equal to the current object
	/// </summary>
	/// <param name="o">InventoryBase object to compare against</param>
	/// <returns>true if objects are the same</returns>
	@Override
	public boolean equals(Object o)
	{
		InventoryBase inv = (InventoryBase)o ;
		return inv != null && equals(inv);
	}

	/// <summary>
	/// Determine whether the specified <seealso cref="OpenMetaverse.InventoryBase"/> object is equal to the current object
	/// </summary>
	/// <param name="o">InventoryBase object to compare against</param>
	/// <returns>true if objects are the same</returns>
	//virtual
	public boolean equals(InventoryBase o)
	{
		return o.UUID.equals(UUID)
				&& o.ParentUUID.equals(ParentUUID)
				&& o.Name.equals(Name)
				&& o.OwnerID.equals(OwnerID);
	}
}

