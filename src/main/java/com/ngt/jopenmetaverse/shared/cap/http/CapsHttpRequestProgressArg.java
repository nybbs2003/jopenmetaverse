package com.ngt.jopenmetaverse.shared.cap.http;

public class CapsHttpRequestProgressArg {
	
	protected CapsHttpClient client;
	protected int bytesReceived;
	protected int totalBytesToReceive;
	
	public CapsHttpRequestProgressArg(CapsHttpClient client,
			int bytesReceived, int totalBytesToReceive) {
		super();
		this.client = client;
		this.bytesReceived = bytesReceived;
		this.totalBytesToReceive = totalBytesToReceive;
	}
	public CapsHttpClient getClient() {
		return client;
	}
	public void setClient(CapsHttpClient client) {
		this.client = client;
	}
	public int getBytesReceived() {
		return bytesReceived;
	}
	public void setBytesReceived(int bytesReceived) {
		this.bytesReceived = bytesReceived;
	}
	public int getTotalBytesToReceive() {
		return totalBytesToReceive;
	}
	public void setTotalBytesToReceive(int totalBytesToReceive) {
		this.totalBytesToReceive = totalBytesToReceive;
	}
}
