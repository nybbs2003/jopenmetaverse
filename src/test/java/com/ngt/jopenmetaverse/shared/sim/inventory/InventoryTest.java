package com.ngt.jopenmetaverse.shared.sim.inventory;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;

import com.ngt.jopenmetaverse.shared.sim.GridClient;
import com.ngt.jopenmetaverse.shared.sim.InventoryManager.InventoryFolder;
import com.ngt.jopenmetaverse.shared.types.UUID;

public class InventoryTest {

	@Test
	public void inventoryCreationTests()
	{
		GridClient client = new GridClient();
		UUID uuid1 = new UUID("83a92bbc-d795-458d-baf9-822c75ec0d92");
		Inventory inventory = new Inventory(client, client.inventory, uuid1);
		InventoryFolder rootFolder = new InventoryFolder(uuid1);
		rootFolder.Name = "";
		rootFolder.ParentUUID = UUID.Zero;
		inventory.setRootFolder(rootFolder);
		
		UUID uuid2 = new UUID("83a92bbc-d795-458d-baf9-822c75ec0d92");
		
		inventory.printItems();
		
		Map<UUID, InventoryFolder> testHash = new HashMap<UUID, InventoryFolder>();
		testHash.put(uuid1, rootFolder);
		Assert.assertTrue(uuid2.equals((Object)uuid1));
		Assert.assertTrue(testHash.containsKey(uuid2));
		
		Assert.assertTrue(inventory.contains(uuid1));
		Assert.assertTrue(inventory.contains(uuid2));
		
	}
}
