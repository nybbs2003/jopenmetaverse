package com.ngt.jopenmetaverse.shared.sim.inventory;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.ngt.jopenmetaverse.shared.exception.NotImplementedException;
import com.ngt.jopenmetaverse.shared.types.UUID;


			    public class InventoryNode implements Serializable
			    {
			        private InventoryBase data;
			        private InventoryNode parent;
			        private UUID parentID; //used for de-seralization 
			        private InventoryNodeDictionary nodes;
			        private boolean needsUpdate = true;

			        /// <summary></summary>
			        public InventoryBase getData()
			        {return data;}
			        public void setData(InventoryBase value)
			        {data = value;}

			        /// <summary></summary>
			        public InventoryNode getParent()
			        {return parent;}
			        public void setParent(InventoryNode value)
			        {parent = value;}

			        /// <summary></summary>
			        public UUID getParentID()
			        {return parentID;}
	

			        /// <summary></summary>
			        public InventoryNodeDictionary getNodes()
			        {
			                if (nodes == null)
			                    nodes = new InventoryNodeDictionary(this);

			                return nodes;
			        }
			        public void setNodes(InventoryNodeDictionary value)
			        {
			            nodes = value;
			        }

			        /// <summary>
			        /// For inventory folder nodes specifies weather the folder needs to be
			        /// refreshed from the server
			        /// </summary>
			        public boolean getNeedsUpdate()
			        {return needsUpdate;}
			        public void setNeedsUpdate(boolean value)
			        {needsUpdate = value;}

			        /// <summary>
			        /// 
			        /// </summary>
			        public InventoryNode()
			        {
			        }

			        /// <summary>
			        /// 
			        /// </summary>
			        /// <param name="data"></param>
			        public InventoryNode(InventoryBase data)
			        {
			            this.data = data;
			        }

			        /// <summary>
			        /// De-serialization constructor for the InventoryNode Class
			        /// </summary>
			        public InventoryNode(InventoryBase data, InventoryNode parent)
			        {
			            this.data = data;
			            this.parent = parent;

			            if (parent != null)
			            {
			                // Add this node to the collection of parent nodes
			            	synchronized (parent.nodes.syncRoot)
			            	{parent.nodes.put(data.UUID, this);}
			            }
			        }

			        /// <summary>
			        /// Serialization handler for the InventoryNode Class
			        /// </summary>
			        public Map<String, Object>  getObjectData()
			        {
			        	Map<String, Object> info = data.getObjectData();  
			            if(parent!=null)
			                info.put("Parent", parent.data.UUID); //We need to track the parent UUID for de-serialization
			            else
			                info.put("Parent", UUID.Zero);

			            info.put("Type", data.getClass());
			            return info;

			        }

			        /// <summary>
			        /// De-serialization handler for the InventoryNode Class
			        /// </summary>
			        public InventoryNode(Map<String, Object> info) throws NotImplementedException
			        {
			        	//TODO Need to verify
			            parentID = (UUID)info.get("Parent");
			            Class type = (Class)info.get("Type");
			           throw new NotImplementedException("need to implement");
//				    // Construct a new inventory object based on the Type stored in Type
//			            System.Reflection.ConstructorInfo ctr = type.GetConstructor(new Type[] {typeof(SerializationInfo),typeof(StreamingContext)});
//			            data = (InventoryBase) ctr.Invoke(new Object[] { info, ctxt });
			        }

			        /// <summary>
			        /// 
			        /// </summary>
			        /// <returns></returns>
			        @Override
			        public String toString()
			        {
			            if (this.data == null) return "[Empty Node]";
			            return this.data.toString();
			        }
			 
}
