package com.ngt.jopenmetaverse.shared.sim.events.om;

import com.ngt.jopenmetaverse.shared.protocol.primitives.MediaEntry;
import com.ngt.jopenmetaverse.shared.sim.events.EventArgs;


public class ObjectMediaEventArgs extends EventArgs
{
    /// <summary>
    /// Indicates if the operation was successful
    /// </summary>
    private boolean Success;

    /// <summary>
    /// Media version string
    /// </summary>
    private String Version;

    /// <summary>
    /// Array of media entries indexed by face number
    /// </summary>
    private MediaEntry[] FaceMedia;
    
    public boolean isSuccess() {
		return Success;
	}

	public void setSuccess(boolean success) {
		Success = success;
	}

	public String getVersion() {
		return Version;
	}

	public void setVersion(String version) {
		Version = version;
	}

	public MediaEntry[] getFaceMedia() {
		return FaceMedia;
	}

	public void setFaceMedia(MediaEntry[] faceMedia) {
		FaceMedia = faceMedia;
	}

	public ObjectMediaEventArgs(boolean success, String version, MediaEntry[] faceMedia)
    {
        this.Success = success;
        this.Version = version;
        this.FaceMedia = faceMedia;
    }
}
