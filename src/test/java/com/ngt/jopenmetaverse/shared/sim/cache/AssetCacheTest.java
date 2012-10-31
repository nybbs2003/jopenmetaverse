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
package com.ngt.jopenmetaverse.shared.sim.cache;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.ngt.jopenmetaverse.shared.sim.GridClient;
import com.ngt.jopenmetaverse.shared.sim.NetworkManager;
import com.ngt.jopenmetaverse.shared.types.UUID;
import com.ngt.jopenmetaverse.shared.util.PlatformUtils;
import com.ngt.jopenmetaverse.shared.util.Utils;

public class AssetCacheTest {
	
	@Before
	public void setup()
	{
		
	}
	
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

	@Test
	public void compressAndSaveImageToCacheTests()
	{
		GridClient client = new GridClient();
		AssetCache assetCache = new AssetCache(client);
		Login(client);
		PlatformUtils.sleep(5000);
		byte[] bytes;
		Map<UUID, LoadCachedImageResult> inventory = new HashMap<UUID, LoadCachedImageResult>();
		
		try
		{
			saveAndTestCompressedData(assetCache, inventory, UUID.Random(), 
					new LoadCachedImageResult(generateRandomBytes(32000), true, true, false));

			saveAndTestCompressedData(assetCache, inventory, UUID.Random(), 
					new LoadCachedImageResult(generateRandomBytes(24000), false, true, false));
			saveAndTestCompressedData(assetCache, inventory, UUID.Random(), 
					new LoadCachedImageResult(generateRandomBytes(56000), true, false, false));
			saveAndTestCompressedData(assetCache, inventory, UUID.Random(), 
					new LoadCachedImageResult(generateRandomBytes(100000), true, true, true));
			saveAndTestCompressedData(assetCache, inventory, UUID.Random(), 
					new LoadCachedImageResult(generateRandomBytes(100000), false, false, false));
			
		}
		catch(Exception e)
		{Assert.fail(Utils.getExceptionStackTraceAsString(e));}
		
//		assetCache.clear();

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

	private void saveAndTestCompressedData(AssetCache assetCache, Map<UUID, LoadCachedImageResult> inventory,
			UUID tmpAsset, LoadCachedImageResult img) throws IOException
	{
		boolean status;
		inventory.put(tmpAsset, img);
		status = assetCache.compressAndSaveImageToCache(tmpAsset, img.data, img.hasAlpha, img.fullAlpha, img.isMask);
		Assert.assertTrue(status);
		System.out.println("Saved Image: " + tmpAsset.toString());
		LoadCachedImageResult result = assetCache.loadCompressedImageFromCache(tmpAsset);
		assertEquals(img, result);
	}
	
	private void assertEquals(LoadCachedImageResult exp, LoadCachedImageResult target)
	{
		System.out.println("Expected Data\n" + Utils.bytesToHexDebugString(exp.data, 900, "Expected"));
		System.out.println("Actual Data\n" + Utils.bytesToHexDebugString(target.data, 900, "Actual"));
		
		Assert.assertArrayEquals(exp.data, target.data);
		Assert.assertEquals(exp.hasAlpha, target.hasAlpha);
		Assert.assertEquals(exp.fullAlpha, target.fullAlpha);
		Assert.assertEquals(exp.isMask, target.isMask);
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
