package com.ngt.jopenmetaverse.shared.sim.events.asm;

import java.math.BigInteger;

import com.ngt.jopenmetaverse.shared.sim.AssetManager.TransferError;
import com.ngt.jopenmetaverse.shared.types.UUID;

public class XferDownload extends Transfer
{
	//ulong
    public BigInteger XferID;
    public UUID VFileID;
    //uint
    public long PacketNum;
    public String Filename = "";
    public TransferError Error = TransferError.None;

    public XferDownload()
    {
    	super();
    }
}
