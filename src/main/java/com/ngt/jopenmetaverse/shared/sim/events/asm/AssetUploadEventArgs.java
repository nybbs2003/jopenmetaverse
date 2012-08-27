package com.ngt.jopenmetaverse.shared.sim.events.asm;

import com.ngt.jopenmetaverse.shared.sim.events.EventArgs;


// <summary>Provides data for AssetUploaded event</summary>
public class AssetUploadEventArgs extends EventArgs
{
    private AssetUpload m_Upload;

    /// <summary>Upload data</summary>
    public AssetUpload getUpload() {return m_Upload;} 

    public AssetUploadEventArgs(AssetUpload upload)
    {
        this.m_Upload = upload;
    }
}