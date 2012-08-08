package com.ngt.jopenmetaverse.shared.cap.http;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpRequestBase;

public class HttpBaseRequestCompletedArg {

	HttpRequestBase request; 
	HttpResponse response; 
	byte[] responseData;
	Exception error;
	
	public HttpBaseRequestCompletedArg(HttpRequestBase request, HttpResponse response,
			byte[] responseData, Exception error) {
		super();
		this.request = request;
		this.response = response;
		this.responseData = responseData;
		this.error = error;
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
	public byte[] getResponseData() {
		return responseData;
	}
	public void setResponseData(byte[] responseData) {
		this.responseData = responseData;
	}
	public Exception getError() {
		return error;
	}
	public void setError(Exception error) {
		this.error = error;
	}
}
