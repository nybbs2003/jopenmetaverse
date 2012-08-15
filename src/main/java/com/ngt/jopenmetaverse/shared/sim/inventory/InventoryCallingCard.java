package com.ngt.jopenmetaverse.shared.sim.inventory;

import java.util.Map;

import com.ngt.jopenmetaverse.shared.types.UUID;

/// <summary>
	/// InventoryCallingCard Class, contains information on another avatar
	/// </summary>
	public class InventoryCallingCard extends InventoryItem
	{
		/// <summary>
		/// Construct an InventoryCallingCard object
		/// </summary>
		/// <param name="itemID">A <seealso cref="OpenMetaverse.UUID"/> which becomes the 
		/// <seealso cref="OpenMetaverse.InventoryItem"/> objects AssetUUID</param>
		public InventoryCallingCard(UUID itemID)

		{
			super(itemID);
			InventoryType = InventoryType.CallingCard;
		}

		/// <summary>
		/// Construct an InventoryCallingCard object from a serialization stream
		/// </summary>
		public InventoryCallingCard(Map<String, Object> info)
		{
			super(info);
			InventoryType = InventoryType.CallingCard;
		}
	}