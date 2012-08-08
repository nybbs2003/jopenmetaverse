package com.ngt.jopenmetaverse.shared.cap.http;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpRequestBase;

public class HttpBaseDownloadProgressArg {
	HttpRequestBase request; 
	HttpResponse response; 
	int bytesReceived;
	int totalBytesToReceive;
	
	public HttpBaseDownloadProgressArg(HttpRequestBase request, HttpResponse response,
			int bytesReceived, int totalBytesToReceive) {
		super();
		this.request = request;
		this.response = response;
		this.bytesReceived = bytesReceived;
		this.totalBytesToReceive = totalBytesToReceive;
	}
	public HttpRequestBase getRequest() {
		return request;
	}
	public void setRequest(HttpRequestBase request) {
		this.request = request;
	}
	public HttpResponse getResponse() {
		return response;
	}
	public void setResponse(HttpResponse response) {
		this.response = response;
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
