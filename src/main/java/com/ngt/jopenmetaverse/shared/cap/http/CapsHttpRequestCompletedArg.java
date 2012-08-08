package com.ngt.jopenmetaverse.shared.cap.http;

import com.ngt.jopenmetaverse.shared.structureddata.OSD;

public class CapsHttpRequestCompletedArg {

	protected CapsHttpClient client;
	protected OSD result; 
	protected Exception error;
	
	public CapsHttpRequestCompletedArg(CapsHttpClient client, OSD result,
			Exception error) {
		super();
		this.client = client;
		this.result = result;
		this.error = error;
	}
	public CapsHttpClient getClient() {
		return client;
	}
	public void setClient(CapsHttpClient client) {
		this.client = client;
	}
	public OSD getResult() {
		return result;
	}
	public void setResult(OSD result) {
		this.result = result;
	}
	public Exception getError() {
		return error;
	}
	public void setError(Exception error) {
		this.error = error;
	}
	
}
