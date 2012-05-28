package com.ngt.jopenmetaverse.shared.structureddata;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/// <summary>
/// 
/// </summary>
public final class OSDMap extends OSD
{
    private Map<String, OSD> value;

     public  final OSDType type =  OSDType.Map;

    public OSDType getType()
    {
    	return type;
    }
    
    public OSDMap()
    {
        value = new HashMap<String, OSD>();
    }

    public OSDMap(int capacity)
    {
        value = new HashMap<String, OSD>(capacity);
    }

    public OSDMap(Map<String, OSD> value)
    {
        if (value != null)
            this.value = value;
        else
            this.value = new HashMap<String, OSD>();
    }

    public  boolean asBoolean() { return value.size() > 0; }

    public  String toString()
    {
    	//TODO Need to implement
        //return OSDParser.SerializeJsonString(this, true);
    	return "";
    }

    //region IDictionary Implementation

    public int count() { return value.size(); } 
    public boolean isReadOnly() { return false; } 
    public Set<String> keys() { return value.keySet(); } 
    public Collection<OSD> values() { return value.values(); }
    public OSD get(String key)
    {
            OSD llsd = this.value.get(key);
            if (llsd != null)
                return llsd;
            else
                return new OSD();
    }

    public boolean containsKey(String key)
    {
        return value.containsKey(key);
    }

    public void put(String key, OSD llsd)
    {
        value.put(key, llsd);
    }

    public void put(Map.Entry<String, OSD> kvp)
    {
        value.put(kvp.getKey(), kvp.getValue());
    }

    public OSD remove(String key)
    {
        return value.remove(key);
    }

    public void clear()
    {
        value.clear();
    }

//    public boolean Contains(Map.Entry<String, OSD> kvp)
//    {
//        // This is a bizarre function... we don't really implement it
//        // properly, hopefully no one wants to use it
//        return value.ContainsKey(kvp.Key);
//    }

//    public void CopyTo(Map.Entry<String, OSD>[] array, int index)
//    {
//        throw new NotImplementedException();
//    }

    public OSD remove(Map.Entry<String, OSD> kvp)
    {
        return this.value.remove(kvp.getKey());
    }

    public Set<Map.Entry<String, OSD>> entrySet()
    {
        return value.entrySet();
    }

//    IEnumerator<Map.Entry<String, OSD>> IEnumerable<Map.Entry<String, OSD>>.GetEnumerator()
//    {
//        return null;
//    }
//
//    IEnumerator IEnumerable.GetEnumerator()
//    {
//        return value.GetEnumerator();
//    }

    //endregion IDictionary Implementation
}