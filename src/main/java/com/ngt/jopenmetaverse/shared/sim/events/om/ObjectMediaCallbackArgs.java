package com.ngt.jopenmetaverse.shared.sim.events.om;

import com.ngt.jopenmetaverse.shared.protocol.primitives.MediaEntry;

public class ObjectMediaCallbackArgs {

	boolean success;
	String version;
	MediaEntry[] faceMedia;
	
	public ObjectMediaCallbackArgs(boolean success, String version,
			MediaEntry[] faceMedia) {
		super();
		this.success = success;
		this.version = version;
		this.faceMedia = faceMedia;
	}
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public MediaEntry[] getFaceMedia() {
		return faceMedia;
	}
	public void setFaceMedia(MediaEntry[] faceMedia) {
		this.faceMedia = faceMedia;
	}
}
