package com.ngt.jopenmetaverse.shared.sim.asset;

import com.ngt.jopenmetaverse.shared.types.Enums.AssetType;
import com.ngt.jopenmetaverse.shared.types.UUID;
 /// <summary>
    /// Represents an Animation
    /// </summary>
    public class AssetAnimation extends Asset
    {
        /// <summary>Override the base classes AssetType</summary>
    	@Override
        public AssetType getAssetType() { return AssetType.Animation; } 

        /// <summary>Default Constructor</summary>
        public AssetAnimation() { }

        /// <summary>
        /// Construct an Asset object of type Animation
        /// </summary>
        /// <param name="assetID">A unique <see cref="UUID"/> specific to this asset</param>
        /// <param name="assetData">A byte array containing the raw asset data</param>
        public AssetAnimation(UUID assetID, byte[] assetData)
        {
        	super(assetID, assetData);
        }

        @Override
        public void Encode() { }
        @Override
        public boolean Decode() { return true; }
    }