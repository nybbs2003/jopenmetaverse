package com.ngt.jopenmetaverse.shared.sim.cache;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.ngt.jopenmetaverse.shared.types.UUID;
import com.ngt.jopenmetaverse.shared.util.Utils;

public class AssetTagMap {
//    private SortedMap<Long, UUID> timestamps = new TreeMap<Long, UUID>();
    private Map<UUID, Long> assets = new HashMap<UUID, Long>();    
    
    public AssetTagMap()
    { }
    
    public synchronized void assetAdded(UUID assetID)
    {
    	Long currentTime = Utils.getUnixTime();
		assets.put(assetID, currentTime);
    }

    public synchronized void assetRemoved(UUID assetID)
    {
    	if(assets.containsKey(assetID))
    	{
    		assets.remove(assetID);
    	}
    }    

    
    public synchronized void assetAccessed(UUID assetID)
    {
    	assetAdded(assetID);
    } 
    
	@SuppressWarnings("unchecked")
	public synchronized Collection<Entry<UUID, Long>> getAssets()
    {
		List<Entry<UUID, Long>> list = new ArrayList<Entry<UUID, Long>>(assets.entrySet());
    	 Collections.sort(new ArrayList<Entry<UUID, Long>>(assets.entrySet()), new Comparator<Entry<UUID, Long>>(){
			public int compare(Entry<UUID, Long> obj0, Entry<UUID, Long> obj1) 
			{
				return obj0.getValue().compareTo(obj1.getValue());
			}
    	});
    	 return list;
    }
 
    public synchronized void clear()
    {
    	assets.clear();
    }
}