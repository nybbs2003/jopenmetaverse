package com.ngt.jopenmetaverse.shared.sim.inventory;

import java.util.Map;

import com.ngt.jopenmetaverse.shared.types.UUID;

/// <summary>
/// InventoryTexture Class representing a graphical image
/// </summary>
/// <seealso cref="ManagedImage"/>
public class InventoryTexture extends InventoryItem
{
	/// <summary>
	/// Construct an InventoryTexture object
	/// </summary>
	/// <param name="itemID">A <seealso cref="OpenMetaverse.UUID"/> which becomes the 
	/// <seealso cref="OpenMetaverse.InventoryItem"/> objects AssetUUID</param>
	public InventoryTexture(UUID itemID)
	{
		super(itemID);
		InventoryType = InventoryType.Texture;
	}

	/// <summary>
	/// Construct an InventoryTexture object from a serialization stream
	/// </summary>
	public InventoryTexture(Map<String, Object> info)
	{
		super(info);
		InventoryType = InventoryType.Texture;
	}
}
