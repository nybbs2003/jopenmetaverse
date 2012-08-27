package com.ngt.jopenmetaverse.shared.sim.events.asm;

import com.ngt.jopenmetaverse.shared.sim.events.EventArgs;

// <summary>Provides data for InitiateDownloaded event</summary>
public class InitiateDownloadEventArgs extends EventArgs
{
    private String m_SimFileName;
    private String m_ViewerFileName;

    /// <summary>Filename used on the simulator</summary>
    public String getSimFileName() {return m_SimFileName;} 

    /// <summary>Filename used by the client</summary>
    public String getViewerFileName() {return m_ViewerFileName;} 

    public InitiateDownloadEventArgs(String simFilename, String viewerFilename)
    {
        this.m_SimFileName = simFilename;
        this.m_ViewerFileName = viewerFilename;
    }
}