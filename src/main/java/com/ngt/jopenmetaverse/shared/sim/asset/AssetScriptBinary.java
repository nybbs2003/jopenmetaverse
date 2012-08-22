package com.ngt.jopenmetaverse.shared.sim.asset;

import com.ngt.jopenmetaverse.shared.types.Enums.AssetType;
import com.ngt.jopenmetaverse.shared.types.UUID;
  /// <summary>
    /// Represents an AssetScriptBinary object containing the 
    /// LSO compiled bytecode of an LSL script
    /// </summary>
    public class AssetScriptBinary extends Asset
    {
        /// <summary>Override the base classes AssetType</summary>
    	@Override
        public AssetType getAssetType() { return AssetType.LSLBytecode; } 

        /// <summary>Initializes a new instance of an AssetScriptBinary object</summary>
        public AssetScriptBinary() { }

        /// <summary>Initializes a new instance of an AssetScriptBinary object with parameters</summary>
        /// <param name="assetID">A unique <see cref="UUID"/> specific to this asset</param>
        /// <param name="assetData">A byte array containing the raw asset data</param>
        public AssetScriptBinary(UUID assetID, byte[] assetData)
        {
            super(assetID, assetData);
        }

        /// <summary>
        /// TODO: Encodes a scripts contents into a LSO Bytecode file
        /// </summary>
        @Override
        public  void Encode() { }

        /// <summary>
        /// TODO: Decode LSO Bytecode into a string
        /// </summary>
        /// <returns>true</returns>
        public boolean Decode() { return true; }
    }