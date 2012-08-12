package com.ngt.jopenmetaverse.shared.sim.events.am;

import com.ngt.jopenmetaverse.shared.sim.message.LindenMessages.AttachmentResourcesMessage;

public class AttachmentResourcesCallbackArg {
	boolean success;
	AttachmentResourcesMessage info;
	public AttachmentResourcesCallbackArg(boolean success,
			AttachmentResourcesMessage info) {
		super();
		this.success = success;
		this.info = info;
	}
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public AttachmentResourcesMessage getInfo() {
		return info;
	}
	public void setInfo(AttachmentResourcesMessage info) {
		this.info = info;
	}

}
