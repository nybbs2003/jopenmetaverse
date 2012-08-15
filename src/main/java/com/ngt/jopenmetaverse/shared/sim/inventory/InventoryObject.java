package com.ngt.jopenmetaverse.shared.sim.inventory;

import java.util.EnumSet;
import java.util.Map;

import com.ngt.jopenmetaverse.shared.sim.InventoryManager.InventoryItemFlags;
import com.ngt.jopenmetaverse.shared.types.EnumsPrimitive.AttachmentPoint;
import com.ngt.jopenmetaverse.shared.types.UUID;

/// <summary>
/// InventoryObject Class contains details on a primitive or coalesced set of primitives
/// </summary>
public class InventoryObject extends InventoryItem
{
	/// <summary>
	/// Construct an InventoryObject object
	/// </summary>
	/// <param name="itemID">A <seealso cref="OpenMetaverse.UUID"/> which becomes the 
	/// <seealso cref="OpenMetaverse.InventoryItem"/> objects AssetUUID</param>
	public InventoryObject(UUID itemID)
	{
		super(itemID);
		InventoryType = InventoryType.Object;
	}

	/// <summary>
	/// Construct an InventoryObject object from a serialization stream
	/// </summary>
	public InventoryObject(Map<String, Object> info)

	{
		super(info);
		InventoryType = InventoryType.Object;
	}

	/// <summary>
	/// Gets or sets the upper byte of the Flags value
	/// </summary>
	public EnumSet<InventoryItemFlags> getItemFlags()
	{return InventoryItemFlags.get(Flags & ~0xFF);}
	
	public void setItemFlags(EnumSet<InventoryItemFlags> value)
	{Flags = InventoryItemFlags.getIndex(value) | (Flags & 0xFF);}


	/// <summary>
	/// Gets or sets the object attachment point, the lower byte of the Flags value
	/// </summary>
	public AttachmentPoint getAttachPoint()
	{return AttachmentPoint.get((byte)(Flags & 0xFF));}
	
	public void setAttachPoint(AttachmentPoint value)
	{Flags = (long)value.getIndex() | (Flags & 0xFFFFFF00);}

}
