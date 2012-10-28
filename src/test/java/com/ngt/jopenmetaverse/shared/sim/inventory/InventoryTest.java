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
