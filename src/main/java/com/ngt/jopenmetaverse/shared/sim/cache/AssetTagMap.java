package com.ngt.jopenmetaverse.shared.sim.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import com.ngt.jopenmetaverse.shared.types.UUID;
import com.ngt.jopenmetaverse.shared.util.Utils;

public class AssetTagMap {
    private Map<UUID, Long> assets = new HashMap<UUID, Long>();    
    private SortedMap<Long, List<UUID>> timestamps = new TreeMap<Long, List<UUID>>();
    
    public AssetTagMap()
    { }
    
    public synchronized void assetAdded(UUID assetID)
    {	
    	Long currentTime = Utils.getUnixTime();
    	if(assets.containsKey(assetID))
    	{
    		long timestamp = assets.get(assetID);
    		List<UUID> tmpAssetIds = timestamps.get(timestamp);
    		tmpAssetIds.remove(assetID);
    		if(tmpAssetIds.isEmpty())
    			timestamps.remove(timestamp);
    	}
    	
    	if(!timestamps.containsKey(currentTime))
    		timestamps.put(currentTime, new ArrayList<UUID>());
    	
		assets.put(assetID, currentTime);
		timestamps.get(currentTime).add(assetID);
    }

    public synchronized void assetRemoved(UUID assetID)
    {
    	if(assets.containsKey(assetID))
    	{
    		long timestamp = assets.get(assetID);
    		List<UUID> tmpAssetIds = timestamps.get(timestamp);
    		tmpAssetIds.remove(assetID);
    		if(tmpAssetIds.isEmpty())
    			timestamps.remove(timestamp);
    		
    		assets.remove(assetID);
    	}
    }    
    
    public synchronized void assetAccessed(UUID assetID)
    {
    	assetAdded(assetID);
    } 
    
	public synchronized List<UUID> getAssets()
    {
		List<UUID> list = new ArrayList<UUID>();

		for(Entry<Long, List<UUID>> e :timestamps.entrySet())
		{
			System.out.println(String.format("Adding Assets= %d for timestamp %d", e.getValue().size(), e.getKey()));
			list.addAll(e.getValue());
		}
    	 return list;
    }
 
    public synchronized void clear()
    {
    	assets.clear();
    	timestamps.clear();
    }
}