package com.ngt.jopenmetaverse.shared.sim.cache;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.junit.Assert;
import org.junit.Test;
import com.ngt.jopenmetaverse.shared.types.UUID;
import com.ngt.jopenmetaverse.shared.util.PlatformUtils;

public class AssetTagMapTest {
	
	@Test
	public void variousTests()
	{
		AssetTagMap assetTagMap1 = new AssetTagMap();
		List<UUID> assetIDs = generateUUIDs(10); 
		SortedMap<Long, UUID> assetTree = new TreeMap<Long, UUID>();
		
		for(UUID assetID :assetIDs)
		{
			assetTagMap1.assetAdded(assetID);
		}
		
		Collection<Entry<UUID, Long>> sortedAssetIDs = assetTagMap1.getAssets();
		assertEqualsUUIDArray(assetIDs, getList(sortedAssetIDs));
		
		PlatformUtils.sleep(2);
		
		assetTagMap1.assetAccessed(assetIDs.get(5));
		PlatformUtils.sleep(2);
		assetTagMap1.assetAccessed(assetIDs.get(1));
		PlatformUtils.sleep(2);

		assetTagMap1.assetAccessed(assetIDs.get(5));
	
		assertEqualsUUIDArray(assetIDs, getList(assetTagMap1.getAssets()));
		
		PlatformUtils.sleep(2);

		assetTagMap1.assetRemoved(assetIDs.get(1));
		assetIDs.remove(1);
		assertEqualsUUIDArray(assetIDs, getList(assetTagMap1.getAssets()));

	}
	
	private List<UUID> generateUUIDs(int size)
	{
		List<UUID> assetIDs = new ArrayList<UUID>();		
		for(int i = 0; i < size; i++)
			assetIDs.add(UUID.Random());
		return assetIDs;
	}
	
	private void assertEqualsUUIDArray(Collection<UUID> expected, Collection<UUID> actual)
	{
		Assert.assertEquals(expected.size(), actual.size());
		for(UUID ele: expected)
		{
			Assert.assertTrue(actual.contains(ele));
		}
	}
	
	private List<UUID> getList(Collection<Entry<UUID, Long>> slist)
	{
		List<UUID> list = new ArrayList<UUID>();
		for(Entry<UUID, Long> e: slist)
		{
			list.add(e.getKey());
		}
		return list;
	}
	
}
