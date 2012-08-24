package com.ngt.jopenmetaverse.shared.sim.cache;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Assert;
import org.junit.Test;

import com.ngt.jopenmetaverse.shared.sim.GridClient;
import com.ngt.jopenmetaverse.shared.sim.NetworkManager;
import com.ngt.jopenmetaverse.shared.types.UUID;
import com.ngt.jopenmetaverse.shared.util.PlatformUtils;
import com.ngt.jopenmetaverse.shared.util.Utils;

public class AssetCacheTest {
	
	@Test
	public void saveAssetToCacheTests()
	{
		GridClient client = new GridClient();
		AssetCache assetCache = new AssetCache(client);
		Login(client);
		PlatformUtils.sleep(5000);
		
		Map<UUID, byte[]> inventory = new HashMap<UUID, byte[]>();
		UUID tmpAsset = UUID.Random();
		byte[] bytes;
//		boolean status;
		
		saveAndTest(assetCache, inventory, UUID.Random(), Utils.stringToBytes("This is test message"));
		saveAndTest(assetCache, inventory, UUID.Random(), Utils.stringToBytes("This is test message2"));
		saveAndTest(assetCache, inventory, UUID.Random(), Utils.stringToBytes("This is test message3"));
		saveAndTest(assetCache, inventory, UUID.Random(), Utils.stringToBytes("This is test message4"));
		saveAndTest(assetCache, inventory, UUID.Random(), Utils.stringToBytes(""));

		
		for(Entry<UUID, byte[]> e: inventory.entrySet())
		{
			bytes = assetCache.getCachedAssetBytes(e.getKey());
			Assert.assertArrayEquals(e.getValue(), bytes);
		}
		
		assetCache.clear();

		for(Entry<UUID, byte[]> e: inventory.entrySet())
		{
			bytes = assetCache.getCachedAssetBytes(e.getKey());
			Assert.assertNull(bytes);
		}
		
		logout(client);
	}
	
	@Test
	public void saveLargeAssetToCacheTests()
	{
		GridClient client = new GridClient();
		AssetCache assetCache = new AssetCache(client);
		Login(client);
		PlatformUtils.sleep(5000);
		byte[] bytes;
		Map<UUID, byte[]> inventory = new HashMap<UUID, byte[]>();
		
		
		try
		{
			saveAndTest(assetCache, inventory, UUID.Random(), generateRandomBytes(32000));
			saveAndTest(assetCache, inventory, UUID.Random(), generateRandomBytes(3200));
			saveAndTest(assetCache, inventory, UUID.Random(), generateRandomBytes(10000000));
			saveAndTest(assetCache, inventory, UUID.Random(), generateRandomBytes(100));
			
		}
		catch(Exception e)
		{Assert.fail(Utils.getExceptionStackTraceAsString(e));}
		
		PlatformUtils.sleep(5000);

		
		for(Entry<UUID, byte[]> e: inventory.entrySet())
		{
			bytes = assetCache.getCachedAssetBytes(e.getKey());
			Assert.assertArrayEquals(e.getValue(), bytes);
		}
		
		assetCache.clear();

		for(Entry<UUID, byte[]> e: inventory.entrySet())
		{
			bytes = assetCache.getCachedAssetBytes(e.getKey());
			Assert.assertNull(bytes);
		}
		
		logout(client);
	}

	/*
	 * @input 
	 * 	len: max number of bytes to generates
	 * 
	 */
	private byte[] generateRandomBytes(int len) throws IOException
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		for(int i = 0; i < len; i += 32)
		{
			baos.write(Utils.stringToBytes(UUID.Random().toString()));
		}
		
		return baos.toByteArray();
	}
	
	
	private void saveAndTest(AssetCache assetCache, Map<UUID, byte[]> inventory, UUID tmpAsset, byte[] bytes)
	{
		boolean status;
		inventory.put(tmpAsset, bytes);
		status = assetCache.saveAssetToCache(tmpAsset, inventory.get(tmpAsset));
		Assert.assertTrue(status);
		bytes = assetCache.getCachedAssetBytes(tmpAsset);
		Assert.assertArrayEquals(inventory.get(tmpAsset), bytes);		
	}
	
	
	private void Login(GridClient client)
	{
		try{			
		NetworkManager networkManager = client.network;
		networkManager.Login("jitendra", "chauhan81", "jchauhan", "Opera", "last", "1.0");
		}
		catch(Exception e)
		{
			e.printStackTrace();
			Assert.fail();
		}
	}
	
	
	private void logout(GridClient client)
	{
		client.network.Logout();
	}

	
}
