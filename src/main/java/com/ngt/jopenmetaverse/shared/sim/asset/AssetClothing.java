package com.ngt.jopenmetaverse.shared.sim.asset;

import com.ngt.jopenmetaverse.shared.types.Enums.AssetType;
import com.ngt.jopenmetaverse.shared.types.UUID;
 /// <summary>
    /// Represents an <seealso cref="AssetWearable"/> that can be worn on an avatar
    /// such as a Shirt, Pants, etc.
    /// </summary>
    public class AssetClothing extends AssetWearable
    {
        /// <summary>Override the base classes AssetType</summary>
    	@Override
        public AssetType getAssetType() { return AssetType.Clothing; } 

        /// <summary>Initializes a new instance of an AssetScriptBinary object</summary>
        public AssetClothing() { }

        /// <summary>Initializes a new instance of an AssetScriptBinary object with parameters</summary>
        /// <param name="assetID">A unique <see cref="UUID"/> specific to this asset</param>
        /// <param name="assetData">A byte array containing the raw asset data</param>
        public AssetClothing(UUID assetID, byte[] assetData) 
         { super(assetID, assetData);}
    }