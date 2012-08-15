package com.ngt.jopenmetaverse.shared.sim.inventory;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.ngt.jopenmetaverse.shared.types.UUID;

public class InventoryNodeDictionary 
{
	  protected Map<UUID, InventoryNode> Dictionary = new HashMap<UUID, InventoryNode>();
      protected InventoryNode parent;
      protected Object syncRoot = new Object();

      public InventoryNode getParent()
      {
    	  return parent; 
    	  }
      public void setParent(InventoryNode value)
      {
        parent = value; 
      }

      public Object getSyncRoot() { return syncRoot; } 

      public int size() { return Dictionary.size(); } 

      public InventoryNodeDictionary(InventoryNode parentNode)
      {
          parent = parentNode;
      }

      public InventoryNode get(UUID key)
      {
          return (InventoryNode)this.Dictionary.get(key); 
      }
      
      public void put(UUID key, InventoryNode value)
      {
              value.setParent(parent);
              synchronized (syncRoot) 
              {this.Dictionary.put(key, value);}
      }

      public Set<UUID> keySet() { return this.Dictionary.keySet(); }
      public Collection<InventoryNode> values() { return this.Dictionary.values(); } 


      public void remove(UUID key)
      {
    	  synchronized (syncRoot) 
    	  {this.Dictionary.remove(key);}
      }

      public boolean containsKey(UUID key)
      {
          return this.Dictionary.containsKey(key);
      }
}
