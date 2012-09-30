package com.ngt.jopenmetaverse.shared.sim.cache;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import com.ngt.jopenmetaverse.shared.types.UUID;
import com.ngt.jopenmetaverse.shared.util.PlatformUtils;
import com.ngt.jopenmetaverse.shared.util.Utils;

public class AssetTagMapTest {
	
	@Test
	public void entryAddAndRemoveTests()
	{
		AssetTagMap assetTagMap1 = new AssetTagMap();
		List<String> assetIDs = generateUUIDs(10); 
		
		for(String assetID :assetIDs)
		{
			assetTagMap1.assetAdded(assetID.toString());
		}
		
		List<String> sortedAssetIDs = assetTagMap1.getAssets();
		assertEqualsUUIDArray(assetIDs, sortedAssetIDs);
		
		PlatformUtils.sleep(2);
		
		assetTagMap1.assetAccessed(assetIDs.get(5));
		PlatformUtils.sleep(2);
		assetTagMap1.assetAccessed(assetIDs.get(1));
		PlatformUtils.sleep(2);

		assetTagMap1.assetAccessed(assetIDs.get(5));
	
		assertEqualsUUIDArray(assetIDs, assetTagMap1.getAssets());
		
		PlatformUtils.sleep(2);

		assetTagMap1.assetRemoved(assetIDs.get(1));
		assetIDs.remove(1);
		assertEqualsUUIDArray(assetIDs, assetTagMap1.getAssets());

	}
	
	@Test
	public void entryAddAndRemove2Tests()
	{
		AssetTagMap assetTagMap1 = new AssetTagMap();
		List<String> assetIDs = generateUUIDs(100000); 
		
		for(String assetID :assetIDs)
		{
			assetTagMap1.assetAdded(assetID);
		}
		
		long start = Utils.getUnixTime();
		List<String> sortedAssetIDs = assetTagMap1.getAssets();
		Assert.assertArrayEquals(assetIDs.toArray(new UUID[0]), sortedAssetIDs.toArray(new UUID[0]));
		
		System.out.println("System Took time to sort entires: " + (Utils.getUnixTime() - start));
		PlatformUtils.sleep(2);
		
		assetTagMap1.assetAccessed(assetIDs.get(5));
		assetAccessed(assetIDs, 5);
		PlatformUtils.sleep(2);
		assetTagMap1.assetAccessed(assetIDs.get(1));
		assetAccessed(assetIDs, 1);
		PlatformUtils.sleep(2);
		assetTagMap1.assetAccessed(assetIDs.get(5));
		assetAccessed(assetIDs, 5);
		
		Assert.assertArrayEquals(assetIDs.toArray(new UUID[0]), assetTagMap1.getAssets().toArray(new UUID[0]));
		
		PlatformUtils.sleep(2);
		assetTagMap1.assetRemoved(assetIDs.get(1));
		assetRemoved(assetIDs, 1);

		Assert.assertArrayEquals(assetIDs.toArray(new UUID[0]), assetTagMap1.getAssets().toArray(new UUID[0]));
	}
	
	private void assetAccessed(List<String> assetIDs, int index)
	{
		String u = assetIDs.remove(index);
		assetIDs.add(u);
	}

	private void assetAdded(List<String> assetIDs, String e)
	{
		assetIDs.add(e);
	}

	private void assetRemoved(List<String> assetIDs, int index)
	{
		String u = assetIDs.remove(index);
	}
	
	private List<String> generateUUIDs(int size)
	{
		List<String> assetIDs = new ArrayList<String>();		
		for(int i = 0; i < size; i++)
			assetIDs.add(UUID.Random().toString());
		return assetIDs;
	}
	
	private void assertEqualsUUIDArray(Collection<String> expected, Collection<String> actual)
	{
		Assert.assertEquals(expected.size(), actual.size());
		for(String ele: expected)
		{
			Assert.assertTrue(actual.contains(ele));
		}
	}
	
}
