package com.ngt.jopenmetaverse.shared.sim.events.asm;

import java.math.BigInteger;

import com.ngt.jopenmetaverse.shared.types.Enums;
import com.ngt.jopenmetaverse.shared.types.UUID;

/// <summary>
    /// 
    /// </summary>
    public class AssetUpload extends Transfer
    {
        public UUID AssetID;
        public Enums.AssetType Type;
        //ulong
        public BigInteger XferID;
        //uint
        public long PacketNum;

        public AssetUpload()
        {
        	super();
        }
    }