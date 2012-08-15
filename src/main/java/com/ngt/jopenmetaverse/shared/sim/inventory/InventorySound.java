package com.ngt.jopenmetaverse.shared.sim.inventory;
import java.util.Map;

import com.ngt.jopenmetaverse.shared.types.UUID;

/// <summary>
	/// InventorySound Class representing a playable sound
	/// </summary>
	public class InventorySound extends InventoryItem
	{
		/// <summary>
		/// Construct an InventorySound object
		/// </summary>
		/// <param name="itemID">A <seealso cref="OpenMetaverse.UUID"/> which becomes the 
		/// <seealso cref="OpenMetaverse.InventoryItem"/> objects AssetUUID</param>
		public InventorySound(UUID itemID)

		{
			super(itemID);
			InventoryType = InventoryType.Sound;
		}

		/// <summary>
		/// Construct an InventorySound object from a serialization stream
		/// </summary>
		public InventorySound(Map<String, Object> info)

		{
			super(info);
			InventoryType = InventoryType.Sound;
		}
	}

