package com.ngt.jopenmetaverse.shared.cap.http;

import com.ngt.jopenmetaverse.shared.structureddata.OSDMap;

public class EventQueueClientEventObservableArg {
	String eventName; 
	OSDMap body;
	
	public EventQueueClientEventObservableArg(String eventName, OSDMap body) 
	{
		super();
		this.eventName = eventName;
		this.body = body;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public OSDMap getBody() {
		return body;
	}

	public void setBody(OSDMap body) {
		this.body = body;
	}
}
